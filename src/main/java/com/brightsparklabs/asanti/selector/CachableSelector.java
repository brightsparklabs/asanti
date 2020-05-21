/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.selector;

import com.brightsparklabs.asanti.data.AsnData;
import com.brightsparklabs.asanti.schema.AsnBuiltinType;

/** @author brightSPARK Labs */
public abstract class CachableSelector implements Selector {
    // -------------------------------------------------------------------------
    // IMPLEMENTATION
    // -------------------------------------------------------------------------

    @Override
    public final boolean matches(
            final String tag, final AsnBuiltinType type, final AsnData asnData) {
        return matches(tag, type);
    }

    /**
     * determines if the criteria are met for the provided inputs
     *
     * @param tag the tag that we are decoding
     * @param type the type of the tag according to the schema
     * @return true if the criteria are met for the provided inputs
     */
    public abstract boolean matches(final String tag, final AsnBuiltinType type);

    @Override
    public boolean cachable() {
        return true;
    }
}
