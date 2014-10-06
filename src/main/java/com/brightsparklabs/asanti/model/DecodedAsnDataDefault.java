/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.io.BaseEncoding;

/**
 * Default implementation of {@link DecodedAsnData}
 *
 * @author brightSPARK Labs <enquire@brightsparklabs.com>
 */
public class DecodedAsnDataDefault implements DecodedAsnData
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** map of tags to data */
    private final ImmutableMap<String, byte[]> tagsToData;

    /** map of 'unmapped' tags to data */
    private final ImmutableMap<String, byte[]> unmappedTagsToData;

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
    public DecodedAsnDataDefault(Map<String, byte[]> tagsToData, Map<String, byte[]> unmappedTagsToData)
    {
        checkNotNull(tagsToData);
        checkNotNull(unmappedTagsToData);
        this.tagsToData = ImmutableMap.copyOf(tagsToData);
        this.unmappedTagsToData = ImmutableMap.copyOf(unmappedTagsToData);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: DecodedAsnData
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<String> getTags()
    {
        return ImmutableSortedSet.copyOf(tagsToData.keySet());
    }

    @Override
    public ImmutableSet<String> getUnmappedTags()
    {
        return ImmutableSortedSet.copyOf(unmappedTagsToData.keySet());
    }

    @Override
    public boolean contains(String tag)
    {
        return tagsToData.containsKey(tag) || unmappedTagsToData.containsKey(tag);
    }

    @Override
    public byte[] getBytes(String tag)
    {
        byte[] result = tagsToData.get(tag);
        if (result == null)
        {
            result = unmappedTagsToData.get(tag);
        }
        return (result == null) ? new byte[0] : result;
    }

    @Override
    public String getHexString(String tag)
    {
        final byte[] bytes = getBytes(tag);
        return "0x" + BaseEncoding.base16().encode(bytes);
    }

    @Override
    public String getPrintableString(String tag)
    {
        // TODO: Determine via schema
        final byte[] bytes = getBytes(tag);
        return new String(bytes);
    }

    @Override
    public Object getDecodedObject(String tag)
    {
        // TODO: Determine via schema
        final byte[] bytes = getBytes(tag);
        return bytes;
    }
}
