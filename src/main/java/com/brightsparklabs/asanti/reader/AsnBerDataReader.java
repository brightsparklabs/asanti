/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.reader;

import com.brightsparklabs.asanti.model.data.RawAsnData;
import com.brightsparklabs.asanti.model.data.RawAsnDataImpl;
import com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteSource;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.BERTags;

/**
 * Reads data from ASN.1 BER/DER binary files.
 *
 * @author brightSPARK Labs
 */
public class AsnBerDataReader {
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** The minimum size of the input buffer (1KB). */
    private static final int MIN_BUFFER_SIZE = 1024;

    /** The default size of the file buffer (8KB). */
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /** The size of the input stream buffer for medium files (8KB). */
    private static final int MEDIUM_BUFFER_SIZE = 8192;

    /** The threshold for if a file is considered large (64KB). */
    private static final int LARGE_FILE_THRESHOLD = 65536;

    /** The size of the input stream buffer for large files (64KB). */
    private static final int LARGE_BUFFER_SIZE = 65536;

    /** TLV encoding constants */
    private static final int MIN_TLV_OCTETS = 3;

    private static final int SINGLE_BYTE_LENGTH_THRESHOLD = 127;
    private static final int LENGTH_MASK = 0x7f;

    /** Bit masks for tag analysis */
    private static final int TAG_CLASS_MASK = 0xC0;

    private static final int TAG_NUMBER_MASK = 0x1F;
    private static final int CONSTRUCTED_BIT_MASK = 0x20;

    /** Reusable empty byte array to avoid repeated allocations */
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Reads the supplied ASN.1 BER/DER binary data.
     *
     * @param source Data to decode.
     * @return List of {@link RawAsnData} objects found in the data.
     * @throws IOException If any errors occur reading the data.
     */
    public static ImmutableList<RawAsnData> read(final ByteSource source) throws IOException {
        return read(source, 0);
    }

    /**
     * Reads the supplied ASN.1 BER/DER binary data and stops reading when it has read the specified
     * number of PDUs.
     *
     * @param source Data to decode.
     * @param maxPDUs Number of PDUs to read from the data. The returned list will be less than or
     *     equal to this value. Set to {@code 0} for no maximum (or use {@link #read(ByteSource)}
     *     instead).
     * @return List of {@link RawAsnData} objects found in the data.
     * @throws IOException If any errors occur reading the data.
     */
    public static ImmutableList<RawAsnData> read(final ByteSource source, final int maxPDUs)
            throws IOException {
        final int bufferSize = getOptimalBufferSize(source);

        try (final var inputStream = source.openStream();
                final var bufferedStream = new BufferedInputStream(inputStream, bufferSize);
                final var asnInputStream = new ASN1InputStream(bufferedStream)) {

            final List<RawAsnData> result = Lists.newArrayList();

            ASN1Primitive asnObject = asnInputStream.readObject();
            while (asnObject != null) {
                final Map<String, byte[]> tagsToData =
                        Maps.newLinkedHashMap(); // we want to preserve input order

                final StringBuilder tagBuilder = new StringBuilder();

                final int rootType = getType(asnObject);
                processDerObject(asnObject, tagBuilder, tagsToData, 0, rootType);
                final RawAsnData rawAsnData = new RawAsnDataImpl(tagsToData);
                result.add(rawAsnData);
                asnObject = asnInputStream.readObject();

                /*
                 * NOTE: do not use '>=' in the below if statement as we use 0 (or
                 * negative numbers) to indicate no limit. We cannot use
                 * Integer#MAX_VALUE as iterables do not need to be limited to this
                 * size
                 */
                if (result.size() == maxPDUs) {
                    break;
                }
            }

            return ImmutableList.copyOf(result);
        }
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Determines the optimal buffer size based on the source data size.
     *
     * <ul>
     *   <li>Small files (<8KB): use file size to avoid over-allocation
     *   <li>Medium files (8KB-64KB): use 8KB buffer
     *   <li>Large files (>64KB): use larger 64KB buffer for better throughput
     * </ul>
     *
     * @param source The data source to read from.
     * @return Optimal buffer size in bytes.
     */
    private static int getOptimalBufferSize(final ByteSource source) {
        final long size = source.sizeIfKnown().or(0L);
        if (size == 0) {
            // Size unknown, use conservative default
            return DEFAULT_BUFFER_SIZE;
        } else if (size < DEFAULT_BUFFER_SIZE) {
            // Small file: use exact size (rounded up to nearest 1KB to avoid tiny buffers)
            return Math.max(MIN_BUFFER_SIZE, (int) size);
        } else if (size < LARGE_FILE_THRESHOLD) {
            // Medium file: 8KB is efficient
            return MEDIUM_BUFFER_SIZE;
        } else {
            // Large file: use 64KB for better throughput
            // Cap at 64KB to avoid excessive memory usage when processing many files
            return LARGE_BUFFER_SIZE;
        }
    }

    /**
     * Processes a DER object and stores the tags/data found in it
     *
     * @param derObject Object to process.
     * @param tagBuilder StringBuilder for building tag paths efficiently.
     * @param tagsToData Storage for the tags/data found.
     * @param index The index of this item within the parent container.
     * @param objectType The pre-calculated type byte from getType(derObject).
     * @throws IOException If any errors occur reading from the file.
     */
    private static void processDerObject(
            final ASN1Primitive derObject,
            final StringBuilder tagBuilder,
            final Map<String, byte[]> tagsToData,
            final int index,
            final int objectType)
            throws IOException {
        switch (derObject) {
            case ASN1TaggedObject tagged ->
                    processTaggedObject(tagged, tagBuilder, tagsToData, index, objectType);
            case ASN1Sequence sequence -> processElements(sequence, tagBuilder, tagsToData);
            case ASN1Set set -> processElements(set, tagBuilder, tagsToData);
            case ASN1Primitive primitive ->
                    processPrimitiveDerObject(primitive, tagBuilder.toString(), tagsToData);
        }
    }

    /**
     * Processes elements from an ASN.1 Sequence or Set using modern iteration.
     *
     * <p>OPTIMIZATION: Uses Iterator instead of legacy Enumeration. Both ASN1Sequence and ASN1Set
     * implement Iterable&lt;ASN1Encodable&gt;, allowing type-safe iteration without casting.
     *
     * <p>OPTIMIZATION: Calculates type once per element and passes it to processDerObject to avoid
     * redundant getEncoded() calls.
     *
     * @param elements The iterable collection of ASN.1 elements (sequence or set).
     * @param tagBuilder StringBuilder for building tag paths efficiently.
     * @param tagsToData Storage for the tags/data found.
     * @throws IOException If any errors occur reading from the file.
     */
    private static void processElements(
            final Iterable<ASN1Encodable> elements,
            final StringBuilder tagBuilder,
            final Map<String, byte[]> tagsToData)
            throws IOException {
        int index = 0;
        final int baseLength = tagBuilder.length();

        for (final ASN1Encodable encodable : elements) {
            final ASN1Primitive derObject = encodable.toASN1Primitive();

            final int objectType = getType(derObject);
            final boolean isTagged = (derObject instanceof ASN1TaggedObject);

            if (!isTagged) {
                final int tagNumber = getTagNumber(objectType);
                tagBuilder.append('/').append(AsnSchemaTag.createRawTagUniversal(index, tagNumber));
            }

            processDerObject(derObject, tagBuilder, tagsToData, index, objectType);

            // Restore to base length for next iteration
            tagBuilder.setLength(baseLength);
            index++;
        }

        if (index == 0) {
            // Then there were no elements found in the Sequence or Set.  (Having an empty
            // Sequence/Set is valid, for example all the components could be OPTIONAL)
            // Make an empty data object against this tag so that we know we received the
            // Constructed type as this is important for decoding and validation.
            tagsToData.put(tagBuilder.toString(), EMPTY_BYTE_ARRAY);
        }
    }

    /**
     * Processes an ASN.1 'Tagged' object and stores the tags/data found in it.
     *
     * @param asnTaggedObject Object to process.
     * @param tagBuilder StringBuilder for building tag paths efficiently.
     * @param tagsToData Storage for the tags/data found.
     * @param index The index of this item within the parent container.
     * @param containingType The pre-calculated type of the tagged object.
     * @throws IOException If any errors occur reading from the file.
     */
    private static void processTaggedObject(
            final ASN1TaggedObject asnTaggedObject,
            final StringBuilder tagBuilder,
            final Map<String, byte[]> tagsToData,
            final int index,
            final int containingType)
            throws IOException {
        final var obj = asnTaggedObject.getBaseObject().toASN1Primitive();

        if (asnTaggedObject.getTagClass() == BERTags.APPLICATION) {
            // Process ASN.1 application objects.
            tagBuilder.append('/').append(asnTaggedObject.getTagNo());
        } else {
            tagBuilder
                    .append('/')
                    .append(
                            AsnSchemaTag.createRawTag(
                                    index, String.valueOf(asnTaggedObject.getTagNo())));
        }

        final int containerIndex = isConstructedType(containingType) ? 0 : index;
        final int childType = getType(obj);

        // We are looking for where UNIVERSAL tags are used in the encoding, so that we can insert
        // appropriate tags.
        // As part of extracting an object from a tagged object (asnTaggedObject.getObject() above)
        // if it is not explicit (ie it is not a tag around a universal) then the library seems
        // to just give it a universal type of either 16 or 4 (Sequence for constructed and
        // Octet String otherwise)
        // This means that we need to check for isExplicit as well as whether it is a Universal type
        if (asnTaggedObject.isExplicit() && isUniversalType(childType)) {
            int number = getTagNumber(childType);
            tagBuilder
                    .append('/')
                    .append(AsnSchemaTag.createRawTagUniversal(containerIndex, number));
        }

        processDerObject(
                asnTaggedObject.getBaseObject().toASN1Primitive(),
                tagBuilder,
                tagsToData,
                containerIndex,
                childType);
    }

    /**
     * Processes an ASN.1 'primitive' object and stores the binary data in it against the supplied
     * tag
     *
     * @param derObject Object to process.
     * @param tag Tag to associate the data with.
     * @param tagsToData Storage for the data found.
     * @throws IOException If any errors occur reading from the file.
     */
    private static void processPrimitiveDerObject(
            final ASN1Primitive derObject, final String tag, final Map<String, byte[]> tagsToData)
            throws IOException {
        final byte[] tlvData = derObject.getEncoded();
        final byte[] value = extractValueFromTlv(tlvData);
        tagsToData.put(tag, value);
    }

    /**
     * Extracts the value (V) from a TLV (Tag-Length-Value) encoded byte array.
     *
     * @param tlvData The complete TLV-encoded data.
     * @return The value portion, or an empty array if the data is too short.
     */
    private static byte[] extractValueFromTlv(final byte[] tlvData) {
        // Must be at least three octets to contain a value.
        if (tlvData.length < MIN_TLV_OCTETS) {
            return EMPTY_BYTE_ARRAY;
        }

        // Determine the number of bytes which define the length.
        final int lengthByte = tlvData[1] & 0xff;
        final int lengthFieldSize =
                (lengthByte > SINGLE_BYTE_LENGTH_THRESHOLD) ? (lengthByte & LENGTH_MASK) : 0;

        // Extract value (skip tag byte + length field).
        final int valueStartIndex = 2 + lengthFieldSize;
        return Arrays.copyOfRange(tlvData, valueStartIndex, tlvData.length);
    }

    /**
     * Returns the T from a TLV (Type Length Value) triplet from the ASN1Primitive object.
     *
     * @param object The ASN1Primitive to extract the T from.
     * @return The T from the TLV.
     */
    private static int getType(final ASN1Primitive object) throws IOException {
        return object.getEncoded()[0] & 0xff;
    }

    /**
     * Encoding uses TLV (Type Length Value) triplets. This function is checking whether the T is a
     * Universal tag.
     *
     * @param type The T from the TLV encoding.
     * @return true if the type is Universal.
     */
    private static boolean isUniversalType(final int type) {
        return ((type & TAG_CLASS_MASK) == 0);
    }

    /**
     * This masks out the Tag Class and the Constructed/Primitive bits and returns just the number.
     *
     * @param type the T from the TLV triplet.
     * @return the number part of the tag (the lower 5 bits).
     */
    private static int getTagNumber(final int type) {
        return type & TAG_NUMBER_MASK;
    }

    /**
     * Encoding uses TLV (Type Length Value) triplets. This function is checking whether the T is a
     * Constructed type (bit 5 is 1).
     *
     * @param type The T from the TLV encoding.
     * @return {@code true} if the type is Constructed.
     */
    private static boolean isConstructedType(final int type) {
        return (type & CONSTRUCTED_BIT_MASK) == CONSTRUCTED_BIT_MASK;
    }
}
