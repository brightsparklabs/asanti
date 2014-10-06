/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model;

import static com.google.common.base.Preconditions.*;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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

    /** ASN data to decode */
    private final AsnData asnData;

    /** ASN schema used to decode data */
    private final AsnSchema asnSchema;

    /** mapping of decoded tags to raw tags found in the data */
    private final ImmutableMap<String, String> decodedToRawTags;

    /** unmapped tags found in the data */
    private final ImmutableSet<String> unmappedTags;

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
        final byte[] result = asnData.getData(rawTag);
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
        final byte[] bytes = getBytes(tag);
        return asnSchema.getPrintableString(tag, bytes);
    }

    @Override
    public Object getDecodedObject(String tag)
    {
        final byte[] bytes = getBytes(tag);
        return asnSchema.getDecodedObject(tag, bytes);
    }
}
