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
        // an octet string, but we should treat at the aliased type.
        // Our current mechanism for handling this is to extract the bytes of the octet string
        // and parse it with our BER/DER parser.  This then produces a new RawAsnData that we
        // slot in to the appropriate spot in the "tree".  We then perform the normal mapping
        // of raw tags to the schema to produce decoded tags.
        final ImmutableMap.Builder<String, byte[]> rawAsnDataBuilder = ImmutableMap.builder();
        rawAsnDataBuilder.putAll(rawAsnData.getBytesMatching(Pattern.compile(".*")));

        // decode the tags in the data, use LinkedHashMap to preserve insertion order
        final Map<String, DecodedTag> decodedToRawTags = Maps.newLinkedHashMap();
        final Map<String, DecodedTag> unmappedTags = Maps.newLinkedHashMap();

        final ImmutableSet<OperationResult<DecodedTag, String>> results =
                asnSchema.getDecodedTags(rawAsnData.getRawTags(), topLevelTypeName);

        for (final OperationResult<DecodedTag, String> decodeResult : results) {
            final DecodedTag decodedTag = decodeResult.getOutput();
            if (decodeResult.wasSuccessful()) {

                final AsnSchemaType type = decodeResult.getOutput().getType();
                if (type instanceof AsnSchemaTypePrimitiveAliased) {
                    final Optional<byte[]> bytes = rawAsnData.getBytes(decodedTag.getRawTag());
                    decodeAliased(
                            bytes,
                            rawAsnDataBuilder,
                            decodedToRawTags,
                            unmappedTags,
                            decodedTag,
                            (AsnSchemaTypePrimitiveAliased) type);
                }
                // TODO INS-434: we can decide if this should be "hidden" if decodeAliased
                // was called and successful.
                decodedToRawTags.put(decodedTag.getTag(), decodedTag);
            } else {
                // could not decode tag
                unmappedTags.put(decodedTag.getTag(), decodedTag);
            }
        }

        this.rawAsnData = new RawAsnDataImpl(rawAsnDataBuilder.build());

        this.asnSchema = asnSchema;
        this.decodedTags = ImmutableMap.copyOf(decodedToRawTags);
        this.unmappedTags = ImmutableMap.copyOf(unmappedTags);
        this.allTags =
                ImmutableMap.<String, DecodedTag>builder()
                        .putAll(decodedToRawTags)
                        .putAll(unmappedTags)
                        .build();
    }

    private void decodeAliased(
            final Optional<byte[]> bytes,
            final ImmutableMap.Builder<String, byte[]> rawAsnDataBuilder,
            final Map<String, DecodedTag> decodedToRawTags,
            final Map<String, DecodedTag> unmappedTags,
            final DecodedTag parentTag,
            final AsnSchemaTypePrimitiveAliased type) {
        // TODO INS-434: these is some duplication here with the normal decode.
        // refactor to reduce...
        // In theory we can now attempt to re-parse that binary,
        // and realign with the type...
        final ByteSource byteSource = ByteSource.wrap(bytes.orElse(new byte[0]));
        try {
            // TODO - what to do here if we don't get valid data
            // ie the data is not what the Aliased type suggests it should be
            // or doesn't parse etc...
            final ImmutableList<RawAsnData> read = AsnBerDataReader.read(byteSource);
            // TODO INS-434: should we ever expect anything other than 1 PDU from this???
            if (read.size() != 1) {
                throw new DecodeException(
                        "Got "
                                + read.size()
                                + " pdus when parsing aliased bytes from "
                                + parentTag.getTag());
            }
            final RawAsnData rawAsnData = read.get(0);

            // So we need to tack all these onto this raw tag.
            final ImmutableMap<String, byte[]> bytesMatching =
                    rawAsnData.getBytesMatching(Pattern.compile(".*"));
            final ImmutableSet<Map.Entry<String, byte[]>> entries = bytesMatching.entrySet();
            final String baseTag = parentTag.getRawTag();
            for (Map.Entry<String, byte[]> e : entries) {

                final String fqt = baseTag + e.getKey();
                rawAsnDataBuilder.put(fqt, e.getValue());
            }

            final ImmutableSet<String> rawTags = rawAsnData.getRawTags();
            final DecodingSession decodingSession = new DecodingSessionImpl();

            final AsnSchemaType aliasedType = type.getAliasedType();
            for (final String rawTag : rawTags) {
                final OperationResult<DecodedTag, String> decodedTagResult =
                        Decoder.getDecodedTag(
                                rawTag,
                                aliasedType,
                                decodingSession,
                                Optional.of(parentTag.getTag()));

                final DecodedTag decodedTag = decodedTagResult.getOutput();
                // This has the "decoded" with the full context of the parent
                // but the raw tag is not yet relative to the parent, so we need to
                // make a new DecodedTag that has both the decoded and raw tags being
                // relative to the parent.
                final DecodedTag fullyQualifiedTag =
                        new DecodedTag(
                                decodedTag.getTag(),
                                parentTag.getRawTag() + decodedTag.getRawTag(),
                                decodedTag.getType(),
                                decodedTag.isFullyDecoded());
                if (decodedTagResult.wasSuccessful()) {
                    decodedToRawTags.put(fullyQualifiedTag.getTag(), fullyQualifiedTag);
                } else {
                    unmappedTags.put(fullyQualifiedTag.getTag(), fullyQualifiedTag);
                }
            }
        } catch (Exception e) {
            // If we had issues processing the bytes and aligning it to the aliased type
            // then we should attempt to deal with that as a validation issue as opposed
            // to just throwing
            logger.error("Exception while processing aliased type at {}", parentTag.getTag(), e);
        }
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

}
