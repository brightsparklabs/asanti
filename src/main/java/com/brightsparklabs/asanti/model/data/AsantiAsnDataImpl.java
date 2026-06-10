/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.data;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.brightsparklabs.asanti.decoder.DecoderVisitor;
import com.brightsparklabs.asanti.decoder.builtin.BuiltinTypeDecoder;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodedTag;
import com.brightsparklabs.asanti.model.schema.Decoder;
import com.brightsparklabs.asanti.model.schema.PduSchema;
import com.brightsparklabs.asanti.model.schema.UnpackedDecodedTags;
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
 * Default implementation of {@link AsantiAsnData}.
 *
 * @author brightSPARK Labs
 */
public class AsantiAsnDataImpl implements AsantiAsnData {
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** Visitor used to determine which decoder to use for decoding data. */
    protected static final DecoderVisitor decoderVisitor = new DecoderVisitor();

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** ASN data to decode. */
    protected final RawAsnData rawAsnData;

    /** The derived "Schema" for the PDU data. This maps the decoded to the raw tags. */
    protected final PduSchema pduSchema;

    /** the schema used to decode. */
    protected final AsnSchema asnSchema;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param rawAsnData Data to decode.
     * @param asnSchema Schema to use to decode data.
     * @param topLevelTypeName The name of the top level type in this module from which to begin
     *     decoding the raw tag.
     * @throws NullPointerException If any of the parameters are {@code null}.
     * @throws IllegalArgumentException If topLevelTypeName is blank.
     */
    public AsantiAsnDataImpl(
            final RawAsnData rawAsnData, final AsnSchema asnSchema, final String topLevelTypeName) {
        checkNotNull(rawAsnData);
        checkNotNull(asnSchema);
        checkNotNull(topLevelTypeName);
        checkArgument(!topLevelTypeName.trim().isEmpty(), "Top level type name must be specified");

        this(Decoder.unpackAndDecode(rawAsnData, asnSchema, topLevelTypeName), asnSchema);
    }

    /**
     * Alternative constructor to pass in the pre-computed {@link UnpackedDecodedTags}.
     *
     * @param unpackedDecodedTags The unpacked {@link RawAsnData} and {@link PduSchema} of decoded
     *     tags.
     * @param asnSchema Schema to use to decode data.
     * @throws NullPointerException If any of the parameters are {@code null}.
     * @throws IllegalArgumentException If topLevelTypeName is blank.
     */
    public AsantiAsnDataImpl(
            final UnpackedDecodedTags unpackedDecodedTags, final AsnSchema asnSchema) {
        checkNotNull(unpackedDecodedTags);
        checkNotNull(asnSchema);
        this(unpackedDecodedTags.unpackedAsnData(), unpackedDecodedTags.pduSchema(), asnSchema);
    }

    /**
     * Alternative constructor to {@link AsantiAsnDataImpl(UnpackedDecodedTags, AsnSchema)},
     * allowing the unpacked {@link RawAsnData} and {@link PduSchema} to be passed in directly
     * without the {@link UnpackedDecodedTags} wrapper.
     *
     * @param rawAsnData The unpacked ASN which has been decoded.
     * @param pduSchema The derived "Schema" for the PDU data. This maps the decoded to the raw
     *     tags.
     * @param asnSchema Schema to use to decode data.
     * @throws NullPointerException If any of the parameters are {@code null}.
     */
    public AsantiAsnDataImpl(
            final RawAsnData rawAsnData, final PduSchema pduSchema, final AsnSchema asnSchema) {
        checkNotNull(rawAsnData);
        checkNotNull(pduSchema);
        checkNotNull(asnSchema);
        this.rawAsnData = rawAsnData;
        this.pduSchema = pduSchema;
        this.asnSchema = asnSchema;
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
        return pduSchema.decodedTags().keySet();
    }

    @Override
    public ImmutableSet<String> getAllTags() {
        return pduSchema.allTags().keySet();
    }

    @Override
    public ImmutableSet<String> getTagsMatching(final Pattern regex) {
        if (regex == null) {
            return ImmutableSet.of();
        }

        return pduSchema.allTags().keySet().stream()
                .filter(tag -> regex.matcher(tag).matches())
                .collect(ImmutableSet.toImmutableSet());
    }

    @Override
    public ImmutableSet<String> getUnmappedTags() {
        return pduSchema.unmappedTags().keySet();
    }

    @Override
    public boolean contains(final String tag) {
        return pduSchema.allTags().containsKey(tag);
    }

    @Override
    public boolean contains(final Pattern regex) {
        if (regex == null) {
            return false;
        }

        for (final String tag : pduSchema.allTags().keySet()) {
            if (regex.matcher(tag).matches()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<byte[]> getBytes(final String tag) {
        final DecodedTag decodedTag = pduSchema.allTags().get(tag);
        // if no decoded tag, assume supplied tag is is already raw tag
        final String rawTag = (decodedTag == null) ? tag : decodedTag.rawTag();
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
        final DecodedTag decodedTag = pduSchema.decodedTags().get(tag);
        if (decodedTag == null) {
            return Optional.empty();
        }

        final AsnSchemaType schemaType = decodedTag.type();
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
        final DecodedTag decodedTag = pduSchema.decodedTags().get(tag);
        if (decodedTag == null) {
            return Optional.empty();
        }

        final AsnSchemaType schemaType = decodedTag.type();
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
}
