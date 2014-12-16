/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model;

import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Null instance of {@link DecodedAsnData}
 *
 * @author brightSPARK Labs
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
    public ImmutableMap<String, byte[]> getBytesMatching(Pattern regex)
    {
        return ImmutableMap.<String, byte[]>of();
    }

    @Override
    public String getHexString(String tag)
    {
        return "0x";
    }

    @Override
    public ImmutableMap<String, String> getHexStringsMatching(Pattern regex)
    {
        return ImmutableMap.<String, String>of();
    }

    @Override
    public String getPrintableString(String tag)
    {
        return "";
    }

    @Override
    public ImmutableMap<String, String> getPrintableStringsMatching(Pattern regex)
    {
        return ImmutableMap.<String, String>of();
    }

    @Override
    public Object getDecodedObject(String tag)
    {
        return new byte[0];
    }

    @Override
    public ImmutableMap<String, Object> getDecodedObjectsMatching(Pattern regex)
    {
        return ImmutableMap.<String, Object>of();
    }
}
