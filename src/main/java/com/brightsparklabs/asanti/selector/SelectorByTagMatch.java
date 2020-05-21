/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.selector;

import com.brightsparklabs.assam.schema.AsnBuiltinType;
import com.google.common.collect.ImmutableSet;

/**
 * Selector which matches if a tag is an exact match.
 *
 * @author brightSPARK Labs
 */
public class SelectorByTagMatch extends CachableSelector {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** The tag to match. */
    private final ImmutableSet<String> tagsToMatch;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param tags Tags which trigger a match.
     */
    public SelectorByTagMatch(final Iterable<String> tags) {
        this.tagsToMatch = ImmutableSet.copyOf(tags);
    }

    /**
     * Creates a selector which only matches a single tag.
     *
     * @param tag Tag which triggers a match.
     */
    public SelectorByTagMatch(final String tag) {
        this(ImmutableSet.of(tag));
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION:
    // -------------------------------------------------------------------------

    @Override
    public boolean matches(final String tag, final AsnBuiltinType type) {
        return tagsToMatch.contains(tag);
    }
}
