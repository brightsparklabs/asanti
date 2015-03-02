/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.validator.rule.ValidationRule;

/**
 * Represents the result from running a {@link ValidationRule} against a tag in
 * a {@link DecodedAsnData} object.
 *
 * @author brightSPARK Labs
 */
public abstract class ValidationResultImpl implements ValidationResult
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the tag the validation result pertains to */
    private final String tag;

    /** whether the validation failed */
    private final boolean isFailure;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param tag
     *            the tag the validation result pertains to
     *
     * @param isFailure
     *            whether the validation failed
     *
     * @throws NullPointerException
     *             if parameters are {@code null}
     *
     * @throws IllegalArgumentException
     *             if tag is {@code null} or empty
     */
    public ValidationResultImpl(String tag, boolean isFailure)
    {
        checkNotNull(tag);
        this.tag = tag.trim();
        checkArgument(!this.tag.isEmpty(), "Tag cannot be null or empty");
        this.isFailure = isFailure;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: ValidationResult
    // -------------------------------------------------------------------------

    @Override
    public String getTag()
    {
        return tag;
    }

    @Override
    public boolean isFailure()
    {
        return isFailure;
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Success
    // -------------------------------------------------------------------------

    /**
     * A validation result which was successful
     *
     * @author brightSPARK Labs
     */
    public static class ValidationSuccess extends ValidationResultImpl
    {
        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor
         *
         * @param tag
         *            the tag the validation succeeded on
         */
        public ValidationSuccess(String tag)
        {
            super(tag, false);
        }

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: ValidationResult
        // ---------------------------------------------------------------------

        @Override
        public FailureType getFailureType()
        {
            return FailureType.None;
        }

        @Override
        public String getFailureReason()
        {
            return "";
        }
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Success
    // -------------------------------------------------------------------------

    /**
     * A validation result which was successful
     *
     * @author brightSPARK Labs
     */
    public static class ValidationFailure extends ValidationResultImpl
    {
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /** the type of failure that occurred */
        private final FailureType failureType;

        /** the reason for the failure */
        private final String failureReason;

        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor
         *
         * @param tag
         *            the tag the validation failed on
         *
         * @param failureType
         *            the type of failure that occurred
         *
         * @param failureReason
         *            the reason for the failure
         */
        public ValidationFailure(String tag, FailureType failureType, String failureReason)
        {
            super(tag, true);
            this.failureType = failureType;
            this.failureReason = failureReason;
        }

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: ValidationResult
        // ---------------------------------------------------------------------

        @Override
        public FailureType getFailureType()
        {
            return failureType;
        }

        @Override
        public String getFailureReason()
        {
            return failureReason;
        }
    }
}
