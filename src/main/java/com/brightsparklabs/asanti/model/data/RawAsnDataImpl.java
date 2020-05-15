/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.data;

import static com.google.common.base.Preconditions.*;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Default implementation of {@link RawAsnData}
 *
 * @author brightSPARK Labs
 */
public class RawAsnDataImpl implements RawAsnData {
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
     * @param tagsToData map of tags to data
     * @throws NullPointerException if parameters are {@code null}
     */
    public RawAsnDataImpl(final Map<String, byte[]> tagsToData) {
        checkNotNull(tagsToData);
        this.tagsToData = ImmutableMap.copyOf(tagsToData);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: RawAsnData
    // -------------------------------------------------------------------------

    @Override
    public boolean contains(final String tag) {
        return tagsToData.containsKey(tag);
    }

    @Override
    public boolean contains(final Pattern regex) {
        if (regex == null) {
            return false;
        }

        return tagsToData.keySet().stream().anyMatch(tag -> regex.matcher(tag).matches());
        /*
                for (final String tag : tagsToData.keySet())
                {
                    if (regex.matcher(tag).matches())
                    {
                        return true;
                    }
                }
                return false;
        */
    }

    @Override
    public ImmutableSet<String> getRawTags() {
        return ImmutableSet.copyOf(tagsToData.keySet());
    }

    @Override
    public Optional<byte[]> getBytes(String rawTag) {
        final byte[] result = tagsToData.get(rawTag);
        return Optional.ofNullable(result);
    }

    @Override
    public ImmutableMap<String, byte[]> getBytes() {
        return tagsToData;
    }

    @Override
    public ImmutableMap<String, byte[]> getBytesMatching(final Pattern regex) {
        if (regex == null) {
            return ImmutableMap.of();
        }

        final Map<String, byte[]> tags = Maps.newHashMap();
        for (final String tag : tagsToData.keySet()) {
            if (regex.matcher(tag).matches()) {
                tags.put(tag, tagsToData.get(tag));
            }
        }
        return ImmutableMap.copyOf(tags);
    }
}
