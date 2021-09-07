/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.data;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.decoder.DecoderVisitor;
import com.brightsparklabs.asanti.decoder.builtin.BuiltinTypeDecoder;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.model.schema.*;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypePrimitiveAliased;
import com.brightsparklabs.asanti.reader.AsnBerDataReader;
import com.brightsparklabs.asanti.schema.AsnPrimitiveType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteSource;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link AsantiAsnData}
 *
 * @author brightSPARK Labs
 */
public class AsantiAsnDataImpl implements AsantiAsnData {

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(AsantiAsnDataImpl.class);

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** ASN data to decode */
    private final RawAsnData rawAsnData;

    /** all tags which could be decoded. Map is of form: { decodedTagString => decodedTag } */
    private final ImmutableMap<String, DecodedTag> decodedTags;

    /** all tags which could not be decoded. Map is of form: { decodedTagString => decodedTag } */
    private final ImmutableMap<String, DecodedTag> unmappedTags;

    /**
     * all tags (decoded and unmapped) found in the data. Map is of form: { decodedTagString =>
     * decodedTag }
     */
    private final ImmutableMap<String, DecodedTag> allTags;

    /** visitor used to determine which decoder to use for decoding data */
    private final DecoderVisitor decoderVisitor = new DecoderVisitor();

    /** the schema used to decode */
    private final AsnSchema asnSchema;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param rawAsnData data to decode
     * @param asnSchema schema to use to decode data
     * @param topLevelTypeName the name of the top level type in this module from which to begin
     *     decoding the raw tag
     * @throws NullPointerException if any of the parameters are {@code null}
     * @throws IllegalArgumentException if topLevelTypeName is blank
     */
    public AsantiAsnDataImpl(
            final RawAsnData rawAsnData, final AsnSchema asnSchema, final String topLevelTypeName) {
        checkNotNull(asnSchema);
        checkNotNull(topLevelTypeName);
        checkArgument(!topLevelTypeName.trim().isEmpty(), "Top level type name must be specified");

        // The RawAsnData is where we get the data (byte array) associated with a raw tag.
        // Since INS-434 we are supporting "CONTAINS" constraints, that appear in the schema as
        // an octet string, but we should treat as an aliased type.
        // Our current mechanism for handling this is to extract the bytes of the octet string
        // and parse it with our BER/DER parser.  This then produces a new RawAsnData that we
        // slot in to the appropriate spot in the "tree".  We then perform the normal mapping
        // of raw tags to the schema to produce decoded tags.
        final Map<String, byte[]> rawAsnDataBuilder = Maps.newLinkedHashMap();
        rawAsnDataBuilder.putAll(rawAsnData.getBytes());

        final Optional<AsnSchemaType> rootType = asnSchema.getType(topLevelTypeName);
        if (rootType.isEmpty()) {
            throw new RuntimeException("type [" + topLevelTypeName + "] does not exist in schema");
        }

        final String decodedTagRootPrefix = "/" + topLevelTypeName;
        final String rawTagRootPrefix = "";

        // decode the tags in the data, use LinkedHashMap to preserve insertion order
        final Map<String, DecodedTag> decodedToRawTags = Maps.newLinkedHashMap();
        final Map<String, DecodedTag> unmappedTags = Maps.newLinkedHashMap();

        // Decode (match up raw tags to schema), in a way that may need to recurse if we encounter
        // "aliased" types, eg OCTET STRING (CONTAINS otherType)
        recursiveDecode(
                rawAsnData,
                rootType.get(),
                decodedTagRootPrefix,
                rawTagRootPrefix,
                rawAsnDataBuilder,
                decodedToRawTags,
                unmappedTags);

        this.rawAsnData = new RawAsnDataImpl(rawAsnDataBuilder);

        this.asnSchema = asnSchema;
        this.decodedTags = ImmutableMap.copyOf(decodedToRawTags);
        this.unmappedTags = ImmutableMap.copyOf(unmappedTags);
        this.allTags =
                ImmutableMap.<String, DecodedTag>builder()
                        .putAll(decodedToRawTags)
                        .putAll(unmappedTags)
                        .build();
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnData
    // -------------------------------------------------------------------------

    @Override
    public Optional<AsnPrimitiveType> getPrimitiveType(final String tag) {
        return getType(tag).map(AsnSchemaType::getPrimitiveType);
    }

    @Override
    public ImmutableSet<String> getTags() {
        return ImmutableSet.copyOf(decodedTags.keySet());
    }

    @Override
    public ImmutableSet<String> getTagsMatching(final Pattern regex) {
        if (regex == null) {
            return ImmutableSet.of();
        }

        return allTags.keySet().stream()
                .filter(tag -> regex.matcher(tag).matches())
                .collect(ImmutableSet.toImmutableSet());
    }

    @Override
    public ImmutableSet<String> getUnmappedTags() {
        return ImmutableSet.copyOf(unmappedTags.keySet());
    }

    @Override
    public boolean contains(final String tag) {
        return allTags.containsKey(tag);
    }

    @Override
    public boolean contains(final Pattern regex) {
        if (regex == null) {
            return false;
        }

        for (final String tag : allTags.keySet()) {
            if (regex.matcher(tag).matches()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<byte[]> getBytes(final String tag) {
        final DecodedTag decodedTag = allTags.get(tag);
        // if no decoded tag, assume supplied tag is is already raw tag
        final String rawTag = (decodedTag == null) ? tag : decodedTag.getRawTag();
        return rawAsnData.getBytes(rawTag);
    }

    @Override
    public ImmutableMap<String, byte[]> getBytesMatching(final Pattern regex) {
        final Map<String, byte[]> result = Maps.newHashMap();
        for (final String tag : getTagsMatching(regex)) {
            getBytes(tag).ifPresent(b -> result.put(tag, b));
        }

        // check against the raw tags too
        result.putAll(rawAsnData.getBytesMatching(regex));

        return ImmutableMap.copyOf(result);
    }

    @Override
    public Optional<String> getHexString(final String tag) {
        return getBytes(tag).map(bytes -> BaseEncoding.base16().encode(bytes));
    }

    @Override
    public ImmutableMap<String, String> getHexStringsMatching(final Pattern regex) {
        final Map<String, String> result = Maps.newHashMap();
        for (final String tag : getTagsMatching(regex)) {
            getHexString(tag).ifPresent(h -> result.put(tag, h));
        }

        // Add any matched to raw tags
        final Map<String, byte[]> raw = rawAsnData.getBytesMatching(regex);
        for (Map.Entry<String, byte[]> entry : raw.entrySet()) {
            final String hexString = BaseEncoding.base16().encode(entry.getValue());
            result.put(entry.getKey(), hexString);
        }

        return ImmutableMap.copyOf(result);
    }

    @Override
    public Optional<String> getPrintableString(final String tag) throws DecodeException {
        final DecodedTag decodedTag = decodedTags.get(tag);
        if (decodedTag == null) {
            return Optional.empty();
        }

        final AsnSchemaType schemaType = decodedTag.getType();
        final AsnPrimitiveType type = schemaType.getPrimitiveType();
        final BuiltinTypeDecoder<?> decoder = (BuiltinTypeDecoder<?>) type.accept(decoderVisitor);
        final String result = decoder.decodeAsString(tag, this);
        return Optional.of(result);
    }

    @Override
    public ImmutableMap<String, String> getPrintableStringsMatching(final Pattern regex)
            throws DecodeException {
        final Map<String, String> result = Maps.newHashMap();
        for (final String tag : getTagsMatching(regex)) {
            getPrintableString(tag).ifPresent(p -> result.put(tag, p));
        }

        return ImmutableMap.copyOf(result);
    }

    @Override
    public <T> Optional<T> getDecodedObject(final String tag, final Class<T> classOfT)
            throws DecodeException, ClassCastException {
        final DecodedTag decodedTag = decodedTags.get(tag);
        if (decodedTag == null) {
            return Optional.empty();
        }

        final AsnSchemaType schemaType = decodedTag.getType();
        final AsnPrimitiveType type = schemaType.getPrimitiveType();
        final BuiltinTypeDecoder<?> decoder = (BuiltinTypeDecoder<?>) type.accept(decoderVisitor);
        // this should throw a ClassCastException if it the types don't match.
        final T result = classOfT.cast(decoder.decode(tag, this));
        return Optional.of(result);
    }

    @Override
    public ImmutableMap<String, Object> getDecodedObjectsMatching(final Pattern regex)
            throws DecodeException {
        final Map<String, Object> result = Maps.newHashMap();
        for (final String tag : getTagsMatching(regex)) {
            getDecodedObject(tag, Object.class).ifPresent(o -> result.put(tag, o));
        }

        return ImmutableMap.copyOf(result);
    }

    @Override
    public Optional<AsnSchemaType> getType(final String tag) {
        return asnSchema.getType(tag);
    }

    // -------------------------------------------------------------------------
    // PRIVATE
    // -------------------------------------------------------------------------

    /**
     * The process of Decoding is matching the raw data provided with the schema provided. This may
     * need to be recursive in the case of some schema constructs, eg:
     *
     * <p>OCTET STRING (CONTAINS otherType)
     *
     * @param rawAsnData the raw data from (BER) parsing of the binary that this will decode
     * @param rootType the type from the schema that the rawAsnData should align to
     * @param decodedPrefix any prefix that should be applied to decoded tags to "fully qualify"
     *     them
     * @param rawPrefix any prefix that should be applied to raw tags to "fully qualify" them
     * @param rawAsnDataBuilder [OUTPUT] the existing mapping of raw tag to bytes, that will be
     *     appended to
     * @param decodedToRawTags [OUTPUT] the existing mapping of decoded to raw tags, that will be
     *     appended to
     * @param unmappedTags [OUTPUT] the existing mapping of unmapped tags, that will be appended to
     */
    private void recursiveDecode(
            final RawAsnData rawAsnData,
            final AsnSchemaType rootType,
            final String decodedPrefix,
            final String rawPrefix,
            final Map<String, byte[]> rawAsnDataBuilder,
            final Map<String, DecodedTag> decodedToRawTags,
            final Map<String, DecodedTag> unmappedTags) {
        // TODO - passing in a few parameters that we modify, specifically
        // rawAsnDataBuilder, decodedToRawTags and unmappedTags
        final ImmutableSet<OperationResult<DecodedTag, String>> results =
                Decoder.getDecodedTags(rawAsnData.getRawTags(), rootType);

        for (final OperationResult<DecodedTag, String> decodeResult : results) {
            final DecodedTag decodedTag = decodeResult.getOutput();

            // We may be decoding at the "root" or somewhere part way through the schema
            // hierarchy, so we need to be able to establish the fully qualified parth
            // for both decoded and raw tags.
            final DecodedTag fullyQualifiedTag =
                    new DecodedTag(
                            decodedPrefix + "/" + decodedTag.getTag(),
                            rawPrefix + decodedTag.getRawTag(),
                            decodedTag.getType(),
                            decodedTag.isFullyDecoded());

            if (decodeResult.wasSuccessful()) {

                final AsnSchemaType type = decodeResult.getOutput().getType();
                if (type instanceof AsnSchemaTypePrimitiveAliased) {
                    final Optional<byte[]> bytes =
                            rawAsnData.getBytes(fullyQualifiedTag.getRawTag());
                    decodeAliased(
                            bytes.orElse(new byte[0]),
                            fullyQualifiedTag,
                            rawAsnDataBuilder,
                            decodedToRawTags,
                            unmappedTags);
                }
                // TODO INS-434: we can decide if this should be "hidden" if decodeAliased
                // was called and successful.
                decodedToRawTags.put(fullyQualifiedTag.getTag(), fullyQualifiedTag);
            } else {
                // could not decode tag
                unmappedTags.put(fullyQualifiedTag.getTag(), fullyQualifiedTag);
            }
        }
    }

    /**
     * Takes the bytes from an OCTET STRING that is an alias for another type and parses them and
     * then decodes.
     *
     * @param bytes the bytes from the OCTET STRING
     * @param parentTag the tag that the bytes came from
     * @param rawAsnDataBuilder [OUTPUT] add the newly parsed data to this
     * @param decodedToRawTags [OUTPUT] add to this with new decode mappings
     * @param unmappedTags [OUTPUT] add to this with new unmapped tags
     */
    private void decodeAliased(
            final byte[] bytes,
            final DecodedTag parentTag,
            final Map<String, byte[]> rawAsnDataBuilder,
            final Map<String, DecodedTag> decodedToRawTags,
            final Map<String, DecodedTag> unmappedTags) {
        // Now attempt to re-parse the bytes, and realign with the type...
        final ByteSource byteSource = ByteSource.wrap(bytes);
        try {
            final ImmutableList<RawAsnData> readPdus = AsnBerDataReader.read(byteSource);
            // TODO INS-434: should we ever expect anything other than 1 PDU from this???
            // what if the CONTAINS is a collection?
            if (readPdus.isEmpty()) {
                throw new DecodeException(
                        "No pdus found when parsing aliased bytes from " + parentTag.getTag());
            }
            for (final RawAsnData rawAsnData : readPdus) {
                // Tack all these onto this raw tag.
                final ImmutableMap<String, byte[]> bytesMatching = rawAsnData.getBytes();
                final String baseTag = parentTag.getRawTag();
                for (final Map.Entry<String, byte[]> e : bytesMatching.entrySet()) {
                    final String fullQualifiedTag = baseTag + e.getKey();
                    rawAsnDataBuilder.put(fullQualifiedTag, e.getValue());
                }

                recursiveDecode(
                        rawAsnData,
                        parentTag.getType(),
                        parentTag.getTag(),
                        parentTag.getRawTag(),
                        rawAsnDataBuilder,
                        decodedToRawTags,
                        unmappedTags);
            }
        } catch (Exception e) {
            // If we had issues processing the bytes and aligning it to the aliased type
            // then we should attempt to deal with that as a validation issue as opposed
            // to just throwing
            // The AsnSchemaContainsConstraint will also attempt to parse the bytes, so should
            // create a validation failure for issues with that.
            logger.error("Exception while processing aliased type at {}", parentTag.getTag(), e);
        }
    }
}
