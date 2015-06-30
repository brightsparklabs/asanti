/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator.rule;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.validator.failure.ValidationFailure;
import com.google.common.collect.ImmutableSet;

/**
 * Represents a rule to validate {@link DecodedAsnData} against.
 *
 * @author brightSPARK Labs
 */
public interface ValidationRule
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** null instance */
    public static final ValidationRule.Null NULL = new ValidationRule.Null();

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates the specified tag in the data
     *
     * @param tag
     *            tag to validate in the data
     *
     * @param decodedAsnData
     *            data to validate
     *
     * @return the results of validation
     */
    public ImmutableSet<ValidationFailure> validate(String tag, DecodedAsnData decodedAsnData);

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: NULL
    // -------------------------------------------------------------------------

    /**
     * NULL instance of {@link ValidationRule}
     *
     * @author brightSPARK Labs
     */
    public static class Null implements ValidationRule
    {
        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private, use {@link ValidationRule#NULL}
         * to obtain an instance
         */
        private Null()
        {
        }

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: ValidationRule
        // ---------------------------------------------------------------------

        @Override
        public ImmutableSet<ValidationFailure> validate(String tag, DecodedAsnData decodedAsnData)
        {
            return ImmutableSet.<ValidationFailure>of();
        }
    }
}
