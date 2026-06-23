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

    /** Null instance. */
    ValidationRule.Null NULL = new ValidationRule.Null();

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates the specified tag in the data.
     *
     * @param tag The tag to validate in the data.
     * @param asnData The data to validate.
     * @return The results of validation.
     * @throws DecodeException If the specified tag could not be decoded.
     */
    ImmutableSet<ValidationFailure> validate(final String tag, final AsnData asnData)
            throws DecodeException;

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: NULL
    // -------------------------------------------------------------------------

    /**
     * NULL instance of {@link ValidationRule}.
     *
     * @author brightSPARK Labs
     */
    class Null implements ValidationRule {
        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private, use {@link ValidationRule#NULL} to obtain an
         * instance.
         */
        private Null() {}

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: ValidationRule
        // ---------------------------------------------------------------------

        @Override
        public ImmutableSet<ValidationFailure> validate(final String tag, final AsnData asnData) {
            return ImmutableSet.of();
        }
    }
}
