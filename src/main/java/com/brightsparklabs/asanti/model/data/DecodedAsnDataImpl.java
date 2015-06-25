/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.data;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.decoder.builtin.BuiltinTypeDecoder;
import com.brightsparklabs.asanti.decoder.DecoderVisitor;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodedTag;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.BaseEncoding;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.*;

/**
 * Default implementation of {@link DecodedAsnData}
 *
 * @author brightSPARK Labs
 */
public class DecodedAsnDataImpl implements DecodedAsnData
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** ASN data to decode */
    private final AsnData asnData;

    /** ASN schema used to decode data */
    private final AsnSchema asnSchema;

    /**
     * all tags which could be decoded. Map is of form: { decodedTagString => decodedTag }
     */
    private final ImmutableMap<String, DecodedTag> decodedTags;

    /**
     * all tags which could not be decoded. Map is of form: { decodedTagString => decodedTag }
     */
    private final ImmutableMap<String, DecodedTag> unmappedTags;

    /**
     * all tags (decoded and unmapped) found in the data. Map is of form: { decodedTagString =>
     * decodedTag }
     */
    private final ImmutableMap<String, DecodedTag> allTags;

    /** visitor used to determine which decoder to use for decoding data */
    private final DecoderVisitor decoderVisitor = new DecoderVisitor();

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param asnData
     *         data to decode
     * @param asnSchema
     *         schema to use to decode data
     * @param topLevelTypeName
     *         the name of the top level type in this module from which to begin decoding the raw
     *         tag
     *
     * @throws NullPointerException
     *         if any of the parameters are {@code null}
     * @throws IllegalArgumentException
     *         if topLevelTypeName is blank
     */
    public DecodedAsnDataImpl(AsnData asnData, AsnSchema asnSchema, String topLevelTypeName)
    {
        checkNotNull(asnData);
        checkNotNull(asnSchema);
        checkNotNull(topLevelTypeName);
        checkArgument(!topLevelTypeName.trim().isEmpty(), "Top level type name must be specified");

        this.asnData = asnData;
        this.asnSchema = asnSchema;

        // decode the tags in the data
        final Map<String, DecodedTag> decodedToRawTags = Maps.newHashMap();
        final Map<String, DecodedTag> unmappedTags = Maps.newHashMap();
        for (final String rawTag : asnData.getRawTags())
        {
            final OperationResult<DecodedTag> decodeResult = asnSchema.getDecodedTag(rawTag,
                    topLevelTypeName);
            final DecodedTag decodedTag = decodeResult.getOutput();
            if (decodeResult.wasSuccessful())
            {
                decodedToRawTags.put(decodedTag.getTag(), decodedTag);
            }
            else
            {
                // could not decode tag
                unmappedTags.put(decodedTag.getTag(), decodedTag);
            }
        }
        this.decodedTags = ImmutableMap.copyOf(decodedToRawTags);
        this.unmappedTags = ImmutableMap.copyOf(unmappedTags);
        this.allTags = ImmutableMap.<String, DecodedTag>builder()
                .putAll(decodedToRawTags)
                .putAll(unmappedTags)
                .build();
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: DecodedAsnData
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<String> getTags()
    {
        return ImmutableSet.copyOf(decodedTags.keySet());
    }

    @Override
    public ImmutableSet<String> getUnmappedTags()
    {
        return ImmutableSet.copyOf(unmappedTags.keySet());
    }

    @Override
    public boolean contains(String tag)
    {
        return allTags.containsKey(tag);
    }

    @Override
    public byte[] getBytes(String tag)
    {
        final DecodedTag decodedTag = allTags.get(tag);
        // if no decoded tag, assume supplied tag is is already raw tag
        final String rawTag = (decodedTag == null) ? tag : decodedTag.getRawTag();
        final byte[] result = asnData.getBytes(rawTag);
        return (result == null) ? new byte[0] : result;
    }

    @Override
    public ImmutableMap<String, byte[]> getBytesMatching(Pattern regex)
    {
        final Map<String, byte[]> result = Maps.newHashMap();
        for (final String tag : getMatchingTags(regex))
        {
            result.put(tag, getBytes(tag));
        }

        return ImmutableMap.copyOf(result);
    }

    @Override
    public String getHexString(String tag)
    {
        final byte[] bytes = getBytes(tag);
        return "0x" + BaseEncoding.base16().encode(bytes);
    }

    @Override
    public ImmutableMap<String, String> getHexStringsMatching(Pattern regex)
    {
        final Map<String, String> result = Maps.newHashMap();
        for (final String tag : getMatchingTags(regex))
        {
            result.put(tag, getHexString(tag));
        }

        return ImmutableMap.copyOf(result);
    }

    @Override
    public String getPrintableString(String tag) throws DecodeException
    {
        final DecodedTag decodedTag = decodedTags.get(tag);
        if (decodedTag == null)
        {
            final String error = String.format("Could not resolve tag '%s' against the schema",
                    tag);
            throw new DecodeException(error);
        }

        final byte[] bytes = getBytes(tag);
        final AsnSchemaType schemaType = decodedTag.getType();
        final AsnPrimitiveType type = schemaType.getPrimitiveType();
        final BuiltinTypeDecoder<?> decoder = (BuiltinTypeDecoder<?>) type.visit(
                decoderVisitor);
        return decoder.decodeAsString(bytes);
    }

    @Override
    public ImmutableMap<String, String> getPrintableStringsMatching(Pattern regex)
            throws DecodeException
    {
        final Map<String, String> result = Maps.newHashMap();
        for (final String tag : getMatchingTags(regex))
        {
            result.put(tag, getPrintableString(tag));
        }

        return ImmutableMap.copyOf(result);
    }

    @Override
    public AsnSchemaType getType(String tag)
    {
        final DecodedTag decodedTag = allTags.get(tag);
        return (decodedTag == null) ? AsnSchemaType.NULL : decodedTag.getType();
    }

    @Override
    public Object getDecodedObject(String tag) throws DecodeException
    {
        final DecodedTag decodedTag = decodedTags.get(tag);
        if (decodedTag == null)
        {
            final String error = String.format("Could not resolve tag '%s' against the schema",
                    tag);
            throw new DecodeException(error);
        }

        final byte[] bytes = getBytes(tag);
        final AsnSchemaType schemaType = decodedTag.getType();
        final AsnPrimitiveType type = schemaType.getPrimitiveType();

        final BuiltinTypeDecoder<?> decoder = (BuiltinTypeDecoder<?>) type.visit(
                decoderVisitor);
        return decoder.decode(bytes);
    }


    @Override
    public ImmutableMap<String, Object> getDecodedObjectsMatching(Pattern regex)
            throws DecodeException
    {
        final Map<String, Object> result = Maps.newHashMap();
        for (final String tag : getMatchingTags(regex))
        {
            result.put(tag, getDecodedObject(tag));
        }

        return ImmutableMap.copyOf(result);
    }

    // -------------------------------------------------------------------------
    // PRIVATE
    // -------------------------------------------------------------------------

    /**
     * Returns all tags which match the supplied regular expression
     *
     * @param regex
     *         regular expression to test tag names against
     *
     * @return all tags which match the supplied regular expression
     */
    private ImmutableSet<String> getMatchingTags(Pattern regex)
    {
        if (regex == null)
        {
            return ImmutableSet.<String>of();
        }

        final Set<String> tags = Sets.newHashSet();
        for (final String tag : allTags.keySet())
        {
            if (regex.matcher(tag).matches())
            {
                tags.add(tag);
            }
        }

        return ImmutableSet.copyOf(tags);
    }
}
