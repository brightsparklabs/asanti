/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.reader;

import static com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag.getUniversalTagNumber;

import com.brightsparklabs.asanti.model.data.RawAsnData;
import com.brightsparklabs.asanti.model.data.RawAsnDataImpl;
import com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.SequencedMap;
import java.util.stream.Stream;
import org.bouncycastle.asn1.ASN1BitString;
import org.bouncycastle.asn1.ASN1Boolean;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1Enumerated;
import org.bouncycastle.asn1.ASN1GeneralizedTime;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Null;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1String;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.ASN1UTCTime;
import org.bouncycastle.asn1.BERTags;

/**
 * Streaming-based BER (Basic Encoding Rules) data parser.
 *
 * <p>This parser provides a memory-efficient, streaming approach to parsing ASN.1 BER-encoded data.
 * Unlike the legacy implementation which reads all PDUs into memory at once, this parser lazily
 * processes PDUs one at a time as they are consumed from the stream.
 *
 * <h2>Key Design Principles</h2>
 *
 * <ul>
 *   <li><b>Streaming Processing:</b> PDUs are parsed on-demand, reducing memory footprint for large
 *       files.
 *   <li><b>Tag Path Generation:</b> Each data element is mapped to a hierarchical tag path (e.g.,
 *       "/1[2]/0[0]/0[1]").
 *   <li><b>Parser Object Handling:</b> Automatically converts BouncyCastle parser objects to
 *       primitives.
 *   <li><b>UNIVERSAL Tag Injection:</b> Adds UNIVERSAL tags for untagged elements in
 *       sequences/sets.
 * </ul>
 *
 * <h2>Tag Path Format</h2>
 *
 * <p>Tag paths follow the format: {@code /index[tagNumber]} where:
 *
 * <ul>
 *   <li>{@code index} - The position within the parent container (0-based).
 *   <li>{@code tagNumber} - The ASN.1 tag number.
 *   <li>UNIVERSAL tags are prefixed with "UNIVERSAL " (e.g., "/0[UNIVERSAL 16]").
 *   <li>CONTEXT tags use numeric-only format (e.g., "/0[1]").
 *   <li>APPLICATION tags are prefixed with "APPLICATION " (e.g., "/0[APPLICATION 5]").
 *   <li>PRIVATE tags are prefixed with "PRIVATE " (e.g., "/0[PRIVATE 3]").
 * </ul>
 *
 * @author brightSPARK Labs
 */
public class AsnBerDataReader {
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** Reusable empty byte array to avoid repeated allocations. */
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    /** The minimum size of the input buffer (50MB). */
    private static final int BUFFERED_STREAM_FALLBACK_SIZE = 50 * 1024 * 1024;

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Reads the supplied ASN.1 BER/DER binary data.
     *
     * <p>For files below {@link #BUFFERED_STREAM_FALLBACK_SIZE 50MB} this will read in all the
     * bytes at once. For files over this limit, a buffered input stream will be used instead. Use
     * {@link #read(InputStream)} or {@link #read(byte[])} if you'd prefer different behaviour.
     *
     * @param source The path to the file containing the ASN.1 BER/DER binary data.
     * @return List of {@link RawAsnData} objects found in the data.
     * @throws IOException If any errors occur reading the data.
     */
    public static Stream<RawAsnData> read(final Path source) throws IOException {
        if (Files.size(source) < BUFFERED_STREAM_FALLBACK_SIZE) {
            return read(Files.readAllBytes(source));
        }

        try (final var bufferedStream = new BufferedInputStream(Files.newInputStream(source))) {
            return read(bufferedStream);
        }
    }

    /**
     * Reads the supplied ASN.1 BER/DER binary data.
     *
     * @param source The ASN.1 BER/DER binary data to decode.
     * @return List of {@link RawAsnData} objects found in the data.
     * @throws IOException If any errors occur reading the data.
     */
    public static Stream<RawAsnData> read(final byte[] source) throws IOException {
        return read(new ByteArrayInputStream(source));
    }

    /**
     * Parses a BER-encoded input stream lazily, returning a Stream of RawAsnData objects.
     *
     * <p>Each element in the returned stream represents one complete Protocol Data Unit (PDU) from
     * the input. PDUs are parsed on-demand as the stream is consumed, making this approach
     * memory-efficient for large files.
     *
     * <p><b>Note:</b> The returned stream should be processed sequentially and closed when done to
     * ensure proper resource cleanup.
     *
     * @param inputStream The BER-encoded data stream to parse (will be wrapped in ASN1InputStream).
     * @return A Stream where each element is a RawAsnData containing tag-to-bytes mappings for one
     *     PDU.
     * @throws UncheckedIOException If an I/O error occurs during stream processing.
     */
    public static Stream<RawAsnData> read(final InputStream inputStream) {
        final var asnInputStream = new ASN1InputStream(inputStream);
        return Stream.<RawAsnData>generate(() -> read(asnInputStream))
                .takeWhile(Objects::nonNull)
                .onClose(
                        () -> {
                            try {
                                inputStream.close();
                            } catch (final IOException e) {
                                throw new UncheckedIOException(e);
                            }
                        });
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses a BER-encoded input stream lazily, returning the next found RawAsnData.
     *
     * @param asnInputStream The BER-encoded data stream to parse .
     * @return {@link RawAsnData} containing tag-to-bytes mappings for one PDU.
     */
    private static RawAsnDataImpl read(final ASN1InputStream asnInputStream) {
        try {
            final ASN1Primitive primitive = asnInputStream.readObject();
            if (primitive == null) {
                return null;
            }

            // Pre-sized map for typical PDU (~50 tags) to reduces map rehashing
            // overhead.
            final var pathMap = new LinkedHashMap<String, byte[]>(64);
            var pathBuilder = new StringBuilder(256);

            // Extract the current root node - don't add index prefix for root
            // level. The root is treated as if already inside a tag to prevent
            // adding UNIVERSAL prefix.
            extractElements(primitive, 0, true, pathBuilder, pathMap);

            return new RawAsnDataImpl(pathMap);
        } catch (final IOException e) {
            // Map checked IOExceptions to Unchecked equivalents to play nice
            // with Java Streams.
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Recursively extracts ASN.1 elements and builds tag paths.
     *
     * <p>This is the core recursive method that traverses the ASN.1 structure, building
     * hierarchical tag paths and extracting data values. It handles the complexity of:
     *
     * <ul>
     *   <li>Converting parser objects to primitives.
     *   <li>Detecting and handling tagged vs untagged elements.
     *   <li>Injecting UNIVERSAL tags for untagged primitives in sequences/sets.
     *   <li>Managing explicit vs implicit tagging.
     * </ul>
     *
     * @param derObject The ASN.1 primitive object to process.
     * @param index The index of this element within its parent container (0 for root/tagged
     *     children)
     * @param insideTag {@code true} if we're already inside a tag context (prevents adding
     *     duplicate UNIVERSAL tags).
     * @param pathBuilder Mutable StringBuilder for building the current tag path (restored after
     *     recursion).
     * @param pathMap The output map where tag paths are mapped to their byte data.
     * @throws IOException If encoding/decoding errors occur.
     */
    private static void extractElements(
            final ASN1Primitive derObject,
            final int index,
            final boolean insideTag,
            final StringBuilder pathBuilder,
            final SequencedMap<String, byte[]> pathMap)
            throws IOException {
        final int baseLength = pathBuilder.length();
        switch (derObject) {
            case ASN1TaggedObject taggedObject -> {
                // Append the tag segment (e.g., "/0[1]" for context tag 1 at index 0)
                appendTagSegment(
                        pathBuilder, index, taggedObject.getTagClass(), taggedObject.getTagNo());
                final ASN1Primitive baseObject = taggedObject.getBaseObject().toASN1Primitive();

                // For EXPLICIT tagging with UNIVERSAL base types, inject a UNIVERSAL tag
                // This occurs when an explicit tag wraps a UNIVERSAL type (e.g., [0] EXPLICIT
                // INTEGER). In such cases, the BER encoding includes both the context
                // tag and the UNIVERSAL tag.
                if (taggedObject.isExplicit() && baseObject != null) {
                    // Use type inspection instead of encoding for performance.
                    if (!(baseObject instanceof ASN1TaggedObject)) {
                        int tagNo = getUniversalTagNumber(baseObject);
                        if (tagNo > 0 && tagNo < 0x1F) {
                            // Valid tag, not high-tag-number form.
                            appendUntaggedPrimitive(pathBuilder, 0, baseObject);
                        }
                    }
                }

                if (baseObject != null) {
                    // Recurse into the base object with insideTag=true to prevent duplicate tags.
                    // Index is always 0 for children of tagged objects (only one child).
                    extractElements(baseObject, 0, true, pathBuilder, pathMap);
                }
            }
            case ASN1Sequence sequence ->
                    processConstructedElements(sequence, pathBuilder, pathMap, baseLength);
            case ASN1Set set -> processConstructedElements(set, pathBuilder, pathMap, baseLength);
            case ASN1OctetString octetString -> {
                if (!insideTag) {
                    appendUntaggedPrimitive(pathBuilder, index, octetString);
                }
                pathMap.put(pathBuilder.toString(), octetString.getOctets());
            }
            default -> handlePrimitive(derObject, index, insideTag, pathBuilder, pathMap);
        }

        pathBuilder.setLength(baseLength);
    }

    /**
     * Handles primitive ASN.1 types and stores their data in the path map.
     *
     * <p>Primitive types are terminal nodes in the ASN.1 tree (no children). This method:
     *
     * <ul>
     *   <li>Optionally adds a UNIVERSAL tag if not already inside a tag context.
     *   <li>Extracts the raw byte data for the primitive value.
     *   <li>Stores the data in the map with the current path as the key.
     * </ul>
     *
     * <p>Special handling is provided for common types (INTEGER, STRING, OID, NULL) to extract
     * their values efficiently. All other primitives are encoded as BER bytes.
     *
     * @param primitive The primitive ASN.1 object to process.
     * @param index The index of this element within its parent container.
     * @param insideTag {@code true} if already inside a tag context (skips adding UNIVERSAL tag).
     * @param pathBuilder Mutable StringBuilder for building the current tag path.
     * @param pathMap The output map where tag paths are mapped to their byte data.
     */
    private static void handlePrimitive(
            final ASN1Primitive primitive,
            final int index,
            final boolean insideTag,
            final StringBuilder pathBuilder,
            final SequencedMap<String, byte[]> pathMap) {
        final int baseLength = pathBuilder.length();

        // Only add UNIVERSAL tag if we're not already inside a tag context.
        // This prevents duplicate tags like "/0[UNIVERSAL 2]/0[UNIVERSAL 2]"
        if (!insideTag) {
            appendUntaggedPrimitive(pathBuilder, index, primitive);
        }

        final String pathKey = pathBuilder.toString();
        pathMap.put(pathKey, extractPrimitiveValue(primitive));

        // Restore path for next sibling (if any).
        pathBuilder.setLength(baseLength);
    }

    /**
     * Processes elements within a constructed type (SEQUENCE or SET).
     *
     * <p>Iterates through each child element, injecting UNIVERSAL tags for untagged primitives and
     * recursively processing each child.
     *
     * @param elements The iterable collection of child elements (from SEQUENCE or SET).
     * @param pathBuilder The path builder to append to.
     * @param pathMap The map to store tag-to-data mappings.
     * @param baseLength The base length to restore the path builder to after processing each child.
     * @throws IOException If an error occurs while processing elements.
     */
    private static void processConstructedElements(
            final Iterable<ASN1Encodable> elements,
            final StringBuilder pathBuilder,
            final SequencedMap<String, byte[]> pathMap,
            final int baseLength)
            throws IOException {
        int childIndex = 0;
        for (ASN1Encodable child : elements) {
            final ASN1Primitive childPrimitive = child.toASN1Primitive();
            final boolean isTagged = (childPrimitive instanceof ASN1TaggedObject);

            // Untagged elements (primitives without explicit tags) need UNIVERSAL tags injected.
            // This is required for proper schema mapping - e.g., an INTEGER in a SEQUENCE
            // should have path "/0[UNIVERSAL 2]" not just "/0".
            if (!isTagged) {
                appendUntaggedPrimitive(pathBuilder, childIndex, childPrimitive);
            }

            // Recurse with insideTag=true if we just added a UNIVERSAL tag.
            // This prevents the child from adding another UNIVERSAL tag (avoiding duplicates).
            extractElements(childPrimitive, childIndex, !isTagged, pathBuilder, pathMap);
            childIndex++;

            // Restore path to base length for next sibling.
            pathBuilder.setLength(baseLength);
        }

        // Empty constructed types are represented as an entry with empty byte array.
        // This is important for validation - it indicates the SEQUENCE/SET was present but empty.
        if (childIndex == 0) {
            pathMap.put(pathBuilder.toString(), new byte[0]);
        }
    }

    /**
     * Appends a tag segment to the path builder.
     *
     * <p>Creates a path segment in the format: {@code /index[tagClass tagNumber]} or {@code
     * /index[tagNumber]} for CONTEXT tags.
     *
     * <p>Examples:
     *
     * <ul>
     *   <li>CONTEXT tag 1 at index 0: "/0[1]".
     *   <li>UNIVERSAL tag 16 at index 2: "/2[UNIVERSAL 16]".
     *   <li>APPLICATION tag 5 at index 1: "/1[APPLICATION 5]".
     * </ul>
     *
     * @param pathBuilder The StringBuilder to append to.
     * @param index The element index within its parent container.
     * @param tagClass The ASN.1 tag class (UNIVERSAL, APPLICATION, CONTEXT, or PRIVATE).
     * @param tagNo The tag number.
     */
    private static void appendTagSegment(
            final StringBuilder pathBuilder, final int index, final int tagClass, final int tagNo) {
        final String tagPrefix =
                switch (tagClass) {
                    case BERTags.CONTEXT_SPECIFIC -> "";
                    case BERTags.APPLICATION -> "APPLICATION ";
                    case BERTags.PRIVATE -> "PRIVATE ";
                    default -> "UNIVERSAL ";
                };
        pathBuilder.append('/').append(AsnSchemaTag.createRawTag(index, tagPrefix + tagNo));
    }

    /**
     * Appends a UNIVERSAL tag segment for an untagged primitive.
     *
     * <p>This method is used to inject UNIVERSAL tags for primitives that don't have explicit tags
     * in the encoding. The tag number is extracted from the primitive's BER encoding.
     *
     * <p>Tag number extraction uses the first byte of the BER encoding (bits 4-0), with fallback
     * logic for common types if encoding fails.
     *
     * @param pathBuilder The StringBuilder to append to.
     * @param index The element index within its parent container.
     * @param primitive The primitive to extract the UNIVERSAL tag number from.
     */
    private static void appendUntaggedPrimitive(
            final StringBuilder pathBuilder, final int index, final ASN1Primitive primitive) {
        final int tagNo = getUniversalTagNumber(primitive);
        pathBuilder.append('/').append(AsnSchemaTag.createRawTagUniversal(index, tagNo));
    }

    /**
     * Extracts the byte value from a primitive.
     *
     * <p>Uses direct accessors for common types to avoid encoding overhead, falling back to
     * getEncoded() for less common types.
     *
     * @param primitive The primitive to extract value from.
     * @return The byte representation of the primitive's value.
     */
    private static byte[] extractPrimitiveValue(final ASN1Primitive primitive) {
        return switch (primitive) {
            case ASN1Integer integer -> integer.getValue().toByteArray();
            case ASN1BitString bitString -> bitString.getOctets();
            case ASN1String str -> str.getString().getBytes(StandardCharsets.UTF_8);
            case ASN1Null _ -> EMPTY_BYTE_ARRAY;
            case ASN1Enumerated enumerated -> enumerated.getValue().toByteArray();
            case ASN1Boolean bool -> new byte[] {bool.isTrue() ? (byte) 0xFF : 0x00};
            case ASN1ObjectIdentifier oid -> {
                try {
                    yield oid.getEncoded(ASN1Encoding.BER);
                } catch (IOException _) {
                    yield EMPTY_BYTE_ARRAY;
                }
            }
            // Time types need BER encoding to preserve format.
            case ASN1UTCTime utc -> {
                try {
                    yield utc.getEncoded(ASN1Encoding.BER);
                } catch (IOException _) {
                    yield EMPTY_BYTE_ARRAY;
                }
            }
            case ASN1GeneralizedTime genTime -> {
                try {
                    yield genTime.getEncoded(ASN1Encoding.BER);
                } catch (IOException _) {
                    yield EMPTY_BYTE_ARRAY;
                }
            }
            // Extremely rare types fall back to encoding.
            default -> {
                try {
                    yield primitive.getEncoded(ASN1Encoding.BER);
                } catch (IOException _) {
                    yield EMPTY_BYTE_ARRAY;
                }
            }
        };
    }
}
