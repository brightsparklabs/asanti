/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.data.AsnData;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.google.common.collect.ImmutableSet;

/**
 * Represents a rule to validate {@link AsnData} against.
 *
 * @author brightSPARK Labs
 */
public interface ValidationRule {
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** null instance */
    ValidationRule.Null NULL = new ValidationRule.Null();

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates the specified tag in the data
     *
     * @param tag tag to validate in the data
     * @param asnData data to validate
     * @return the results of validation
     * @throws DecodeException if the specified tag could not be decoded.
     */
    ImmutableSet<ValidationFailure> validate(String tag, AsnData asnData) throws DecodeException;

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: NULL
    // -------------------------------------------------------------------------

    /**
     * NULL instance of {@link ValidationRule}
     *
     * @author brightSPARK Labs
     */
    class Null implements ValidationRule {
        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private, use {@link ValidationRule#NULL} to obtain an
         * instance
         */
        private Null() {}

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: ValidationRule
        // ---------------------------------------------------------------------

        @Override
        public ImmutableSet<ValidationFailure> validate(String tag, AsnData asnData) {
            return ImmutableSet.of();
        }
    }
}
