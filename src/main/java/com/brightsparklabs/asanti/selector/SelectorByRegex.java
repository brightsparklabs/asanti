/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.selector;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import java.util.regex.Pattern;

/**
 * Class with criteria that the given tag match a specific regex pattern.
 *
 * @author brightSPARK Labs
 */
public class SelectorByRegex extends CachableSelector {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** The regex pattern to use to match the tag - this is the selection criteria. */
    private final Pattern tagMatcher;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Constructor.
     *
     * @param tagMatcher The regex pattern to use to match the tag.
     */
    public SelectorByRegex(final Pattern tagMatcher) {
        this.tagMatcher = checkNotNull(tagMatcher);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION
    // -------------------------------------------------------------------------
    @Override
    public boolean matches(final String tag, final AsnBuiltinType type) {
        return tagMatcher.matcher(tag).matches();
    }
}
