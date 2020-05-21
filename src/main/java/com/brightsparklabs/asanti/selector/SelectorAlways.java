/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.selector;

import com.brightsparklabs.asanti.schema.AsnBuiltinType;

/**
 * A class always meets the criteria
 *
 * @author brightSPARK Labs
 */
public class SelectorAlways extends CachableSelector {

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /** Constructor */
    public SelectorAlways() {}

    // -------------------------------------------------------------------------
    // IMPLEMENTATION
    // -------------------------------------------------------------------------
    @Override
    public boolean matches(final String tag, final AsnBuiltinType type) {
        return true;
    }
}
