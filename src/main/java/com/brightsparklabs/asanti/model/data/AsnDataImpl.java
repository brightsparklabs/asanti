/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.data;

import static com.google.common.base.Preconditions.*;

import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;

/**
 * Default implementation of {@link AsnData}
 *
 * @author brightSPARK Labs
 */
public class AsnDataImpl implements AsnData
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** map of tags to data */
    private final ImmutableMap<String, byte[]> tagsToData;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param tagsToData
     *            map of tags to data
     *
     * @throws NullPointerException
     *             if parameters are {@code null}
     */
    public AsnDataImpl(Map<String, byte[]> tagsToData)
    {
        checkNotNull(tagsToData);
        this.tagsToData = ImmutableMap.copyOf(tagsToData);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnData
    // -------------------------------------------------------------------------

    @Override
    public boolean contains(String tag)
    {
        return tagsToData.containsKey(tag);
    }

    @Override
    public boolean contains(Pattern regex)
    {
        if (regex == null) { return false; }

        for (final String tag : tagsToData.keySet())
        {
            if (regex.matcher(tag).matches()) { return true; }
        }
        return false;
    }

    @Override
    public ImmutableSet<String> getRawTags()
    {
        return ImmutableSortedSet.copyOf(tagsToData.keySet());
    }

    @Override
    public byte[] getBytes(String rawTag)
    {
        final byte[] result = tagsToData.get(rawTag);
        return (result == null) ? new byte[0] : result;
    }

    @Override
    public ImmutableMap<String, byte[]> getBytes()
    {
        return tagsToData;
    }

    @Override
    public ImmutableMap<String, byte[]> getBytesMatching(Pattern regex)
    {
        if (regex == null) { return ImmutableMap.<String, byte[]>of(); }

        final Map<String, byte[]> tags = Maps.newHashMap();
        for (final String tag : tagsToData.keySet())
        {
            if (regex.matcher(tag).matches())
            {
                tags.put(tag, tagsToData.get(tag));
            }
        }
        return ImmutableMap.copyOf(tags);
    }
}
