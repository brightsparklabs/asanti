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
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodedTag;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.schema.AsnPrimitiveType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.io.BaseEncoding;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Default implementation of {@link AsantiAsnData}
 *
 * @author brightSPARK Labs
 */
public class AsantiAsnDataImpl implements AsantiAsnData {
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

        this.rawAsnData = checkNotNull(rawAsnData);

        // decode the tags in the data, use LinkedHashMap to preserve insertion order
        final Map<String, DecodedTag> decodedToRawTags = Maps.newLinkedHashMap();
        final Map<String, DecodedTag> unmappedTags = Maps.newLinkedHashMap();

        final ImmutableSet<OperationResult<DecodedTag, String>> results =
                asnSchema.getDecodedTags(rawAsnData.getRawTags(), topLevelTypeName);
        for (final OperationResult<DecodedTag, String> decodeResult : results) {
            final DecodedTag decodedTag = decodeResult.getOutput();
            if (decodeResult.wasSuccessful()) {
                decodedToRawTags.put(decodedTag.getTag(), decodedTag);
            } else {
                // could not decode tag
                unmappedTags.put(decodedTag.getTag(), decodedTag);
            }
        }

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

    // -------------------------------------------------------------------------
    // PRIVATE
    // -------------------------------------------------------------------------

    @Override
    public Optional<AsnSchemaType> getType(final String tag) {
        return asnSchema.getType(tag);
    }
}
