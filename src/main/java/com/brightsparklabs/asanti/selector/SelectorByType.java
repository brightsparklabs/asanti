/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.selector;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.schema.AsnBuiltinType;

/**
 * Selector with criteria that the tag type matches a specific schema type.
 *
 * @author brightSPARK Labs
 */
public class SelectorByType extends CachableSelector {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** The type to match - this is the selection criteria. */
    private final AsnBuiltinType type;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Constructor.
     *
     * @param type The type to match - this is the selection criteria.
     */
    public SelectorByType(final AsnBuiltinType type) {
        this.type = checkNotNull(type);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION
    // -------------------------------------------------------------------------
    @Override
    public boolean matches(final String tag, final AsnBuiltinType type) {
        return this.type.equals(type);
    }
}
