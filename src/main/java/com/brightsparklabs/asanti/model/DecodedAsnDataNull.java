/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model;

import com.google.common.collect.ImmutableSet;

/**
 * Null instance of {@link DecodedAsnData}
 *
 * @author brightSPARK Labs <enquire@brightsparklabs.com>
 */
public class DecodedAsnDataNull implements DecodedAsnData
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: DecodedAsnData
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<String> getTags()
    {
        return ImmutableSet.<String>of();
    }

    @Override
    public ImmutableSet<String> getUnmappedTags()
    {
        return ImmutableSet.<String>of();
    }

    @Override
    public boolean contains(String tag)
    {
        return false;
    }

    @Override
    public byte[] getBytes(String tag)
    {
        return new byte[0];
    }

    @Override
    public String getHexString(String tag)
    {
        return "0x";
    }

    @Override
    public String getPrintableString(String tag)
    {
        return "";
    }

    @Override
    public Object getDecodedObject(String tag)
    {
        return new byte[0];
    }
}
