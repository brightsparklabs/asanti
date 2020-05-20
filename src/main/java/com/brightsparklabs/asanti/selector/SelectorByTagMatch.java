/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.selector;

import com.brightsparklabs.assam.schema.AsnBuiltinType;

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
    private final String tagToMatch;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param tag The tag to match.
     */
    public SelectorByTagMatch(final String tag) {
        this.tagToMatch = tag;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION:
    // -------------------------------------------------------------------------

    @Override
    public boolean matches(final String tag, final AsnBuiltinType type) {
        return tagToMatch.equals(tag);
    }
}
