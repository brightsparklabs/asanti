/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 * Implementation of DecodingSession
 *
 * @author brightSPARK Labs
 */
public class DecodingSessionImpl implements DecodingSession {

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** storage of the for each index for each "context" */
    private final Map<String, TagToIndex> offsetMap = Maps.newHashMap();

    /** the context is how we provide unique offsets for each level of the hierarchy of data */
    private String context;

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: DecodingSession
    // -------------------------------------------------------------------------

    @Override
    public void setContext(final String context) {
        this.context = context;

        offsetMap.computeIfAbsent(context, key -> new TagToIndex());
    }

    @Override
    public int getIndex(final AsnSchemaTag tag) {

        final TagToIndex tagToIndex = offsetMap.get(context);

        // If this is the first time this context has been used then by default we are at 0
        if (tagToIndex.tag == null) {
            tagToIndex.tag = tag;
            return tagToIndex.index;
        }

        // If this the last tag that we used then the last index
        if (tagToIndex.tag.getRawTag().equals(tag.getRawTag())) {
            return tagToIndex.index;
        }

        // this must be a new tag, so capture it and increment the index.
        tagToIndex.tag = tag;
        tagToIndex.index = tagToIndex.index + 1;

        return tagToIndex.index;
    }

    @Override
    public void setIndex(final AsnSchemaTag tag, int index) {
        final TagToIndex tagToIndex = offsetMap.get(context);
        tagToIndex.tag = tag;
        tagToIndex.index = index;
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS:
    // -------------------------------------------------------------------------

    /**
     * Transfer object to support tracking a tuple of tag and index
     *
     * @author brightSPARK Labs
     */
    private static class TagToIndex {
        /** the tag to keep track of */
        AsnSchemaTag tag = null;

        /** the index associated with the tag */
        Integer index = 0;
    }
}
