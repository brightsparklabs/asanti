/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.data.AsnData;

/**
 * Used to validate {@link AsnData} against its associated schema or a custom validation rule.
 *
 * @author brightSPARK Labs
 */
public interface Validator {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates the supplied data using the rules in this validator
     *
     * @param asnData data to validate
     * @return the results from validating the data
     */
    ValidationResult validate(AsnData asnData);
}
