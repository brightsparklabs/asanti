/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.data;

import static com.google.common.base.Preconditions.*;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodeResult;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.BaseEncoding;

/**
 * Default implementation of {@link DecodedAsnData}
 *
 * @author brightSPARK Labs
 */
public class DecodedAsnDataDefault implements DecodedAsnData
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** ASN data to decode */
    private final AsnData asnData;

    /** ASN schema used to decode data */
    private final AsnSchema asnSchema;

    /** mapping of decoded tags to raw tags found in the data */
    private final ImmutableMap<String, String> decodedToRawTags;

    /** mapping of unmapped tags to raw tags found in the data */
    private final ImmutableMap<String, String> unmappedToRawTags;

    /** mapping of all tags (decoded and unmapped) to raw tags found in the data */
    private final ImmutableMap<String, String> allTags;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param asnData
     *            data to decode
     *
     * @param asnSchema
     *            schema to use to decode data
     *
     * @param topLevelTypeName
     *            the name of the top level type in this module from which to
     *            begin decoding the raw tag
     *
     * @throws NullPointerException
     *             if any of the parameters are {@code null}
     *
     * @throws IllegalArgumentException
     *             if topLevelTypeName is blank
     */
    public DecodedAsnDataDefault(AsnData asnData, AsnSchema asnSchema, String topLevelTypeName)
    {
        checkNotNull(asnData);
        checkNotNull(asnSchema);
        checkNotNull(topLevelTypeName);
        checkArgument(!topLevelTypeName.trim().isEmpty(), "Top level type name must be specified");

        this.asnData = asnData;
        this.asnSchema = asnSchema;

        // decode the tags in the data
        final Map<String, String> decodedToRawTags = Maps.newHashMap();
        final Map<String, String> unmappedTags = Maps.newHashMap();
        for (String rawTag : asnData.getRawTags())
        {
            final DecodeResult<String> decodeResult = asnSchema.getDecodedTag(rawTag, topLevelTypeName);
            if (decodeResult.wasSuccessful())
            {
                decodedToRawTags.put(decodeResult.getDecodedData(), rawTag);
            }
            else
            {
                // could not decode tag
                unmappedTags.put(decodeResult.getDecodedData(), rawTag);
            }
        }
        this.decodedToRawTags = ImmutableMap.copyOf(decodedToRawTags);
        this.unmappedToRawTags = ImmutableMap.copyOf(unmappedTags);
        this.allTags = ImmutableMap.<String, String>builder().putAll(decodedToRawTags).putAll(unmappedTags).build();
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: DecodedAsnData
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<String> getTags()
    {
        return ImmutableSet.copyOf(decodedToRawTags.keySet());
    }

    @Override
    public ImmutableSet<String> getUnmappedTags()
    {
        return ImmutableSet.copyOf(unmappedToRawTags.keySet());
    }

    @Override
    public boolean contains(String tag)
    {
        return decodedToRawTags.containsKey(tag) || unmappedToRawTags.containsKey(tag);
    }

    @Override
    public byte[] getBytes(String tag)
    {
        String rawTag = allTags.get(tag);
        if (rawTag == null)
        {
            // could not find tag, assume it is already raw tag
            rawTag = tag;
        }
        final byte[] result = asnData.getBytes(rawTag);
        return (result == null) ? new byte[0] : result;
    }

    @Override
    public ImmutableMap<String, byte[]> getBytesMatching(Pattern regex)
    {
        final Map<String, byte[]> result = Maps.newHashMap();
        for (String tag : getMatchingTags(regex))
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
        for (String tag : getMatchingTags(regex))
        {
            result.put(tag, getHexString(tag));
        }

        return ImmutableMap.copyOf(result);
    }

    @Override
    public String getPrintableString(String tag)
    {
        final byte[] bytes = getBytes(tag);
        return asnSchema.getPrintableString(tag, bytes);
    }

    @Override
    public ImmutableMap<String, String> getPrintableStringsMatching(Pattern regex)
    {
        final Map<String, String> result = Maps.newHashMap();
        for (String tag : getMatchingTags(regex))
        {
            result.put(tag, getPrintableString(tag));
        }

        return ImmutableMap.copyOf(result);
    }

    @Override
    public Object getDecodedObject(String tag)
    {
        final byte[] bytes = getBytes(tag);
        return asnSchema.getDecodedObject(tag, bytes);
    }

    @Override
    public ImmutableMap<String, Object> getDecodedObjectsMatching(Pattern regex)
    {
        final Map<String, Object> result = Maps.newHashMap();
        for (String tag : getMatchingTags(regex))
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
     *            regular expression to test tag names against
     *
     * @return all tags which match the supplied regular expression
     */
    private ImmutableSet<String> getMatchingTags(Pattern regex)
    {
        if (regex == null) { return ImmutableSet.<String>of(); }

        final Set<String> tags = Sets.newHashSet();
        for (String tag : allTags.keySet())
        {
            if (regex.matcher(tag).matches())
            {
                tags.add(tag);
            }
        }

        return ImmutableSet.copyOf(tags);
    }
}
