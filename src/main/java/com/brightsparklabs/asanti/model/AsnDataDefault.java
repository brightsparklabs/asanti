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

/**
 * Default implementation of {@link AsnData}
 *
 * @author brightSPARK Labs <enquire@brightsparklabs.com>
 */
public class AsnDataDefault implements AsnData
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
    public AsnDataDefault(Map<String, byte[]> tagsToData)
    {
        checkNotNull(tagsToData);
        this.tagsToData = ImmutableMap.copyOf(tagsToData);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnData
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<String> getTags()
    {
        return ImmutableSortedSet.copyOf(tagsToData.keySet());
    }

    @Override
    public byte[] getData(String tag)
    {
        final byte[] result = tagsToData.get(tag);
        return (result == null) ? new byte[0] : result;
    }

    @Override
    public ImmutableMap<String, byte[]> getData()
    {
        return tagsToData;
    }
}
