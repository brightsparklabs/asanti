/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model;

import static com.google.common.base.Preconditions.*;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
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

    /** unmapped tags found in the data */
    private final ImmutableSet<String> unmappedTags;

    /** names of all tags (decoded and unmapped) found in the data */
    private final ImmutableSet<String> allTags;

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
     * @throws NullPointerException
     *             if parameters are {@code null}
     */
    public DecodedAsnDataDefault(AsnData asnData, AsnSchema asnSchema)
    {
        checkNotNull(asnData);
        checkNotNull(asnSchema);
        this.asnData = asnData;
        this.asnSchema = asnSchema;

        // decode the tags in the data
        final Map<String, String> decodedToRawTags = Maps.newHashMap();
        final Set<String> unmappedTags = Sets.newHashSet();
        for (String rawTag : asnData.getRawTags())
        {
            final String decodedTag = asnSchema.getDecodedTag(rawTag);
            if (decodedTag.isEmpty())
            {
                // could not decode tag
                unmappedTags.add(rawTag);
            }
            else
            {
                decodedToRawTags.put(decodedTag, rawTag);
            }
        }
        this.decodedToRawTags = ImmutableMap.copyOf(decodedToRawTags);
        this.unmappedTags = ImmutableSortedSet.copyOf(unmappedTags);
        this.allTags = ImmutableSet.<String>builder()
                .addAll(decodedToRawTags.keySet())
                .addAll(unmappedTags)
                .build();
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
        return unmappedTags;
    }

    @Override
    public boolean contains(String tag)
    {
        return decodedToRawTags.containsKey(tag) || unmappedTags.contains(tag);
    }

    @Override
    public byte[] getBytes(String tag)
    {
        String rawTag = decodedToRawTags.get(tag);
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
        for (String tag : allTags)
        {
            if (regex.matcher(tag).matches())
            {
                tags.add(tag);
            }
        }

        return ImmutableSet.copyOf(tags);
    }
}
