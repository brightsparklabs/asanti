/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.selector;

import com.brightsparklabs.asanti.data.AsnData;
import com.brightsparklabs.asanti.schema.AsnBuiltinType;

/**
 * Interface for selectors that determine if criteria are met for tag/type/data combinations.
 *
 * @author brightSPARK Labs
 */
public interface Selector {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Determines if the criteria are met for the provided inputs.
     *
     * @param tag the tag that we are decoding.
     * @param type the type of the tag according to the schema.
     * @param asnData the data that will be used to determine if this selector is prepared to
     *     provide a decoder.
     * @return true if the criteria are met for the provided inputs.
     */
    boolean matches(
            final String tag,
            final com.brightsparklabs.asanti.schema.AsnBuiltinType type,
            final AsnData asnData);

    /**
     * {@return true if the result of {@link #matches(String, AsnBuiltinType, AsnData)} can be
     * cached, for the provided input tag or type. ie they will always return the same value,
     * irrespective of what data is passed in}
     */
    boolean cachable();
}
