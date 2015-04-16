/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator.result;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.Validator;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;

/**
 * Contains the results from running a {@link Validator} over {@link DecodedAsnData}.
 *
 * @author brightSPARK Labs
 */
public class ByteValidationResult implements ValidationResult<ByteValidationFailure>
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** all failures that occurred during validation */
    private final ImmutableSet<ByteValidationFailure> failures;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor. This is private, use {@link #builder()} to create instances.
     *
     * @param failures
     *         failures to include in this result set
     */
    private ByteValidationResult(Iterable<ByteValidationFailure> failures)
    {
        this.failures = ImmutableSet.copyOf(failures);
    }

    /**
     * Returns a builder for creating instances of {@link ByteValidationResult}
     *
     * @return a builder for creating instances of {@link ByteValidationResult}
     */
    public static Builder builder()
    {
        return new Builder();
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: ValidationResults
    // -------------------------------------------------------------------------

    @Override
    public boolean hasFailures()
    {
        return !failures.isEmpty();
    }

    @Override
    public ImmutableSet<ByteValidationFailure> getFailures()
    {
        return failures;
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Builder
    // -------------------------------------------------------------------------

    /**
     * Builder for creating instances of {@link ByteValidationResult}
     */
    public static class Builder
    {
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /** the results to include in the result set */
        private final ImmutableSet.Builder<ByteValidationFailure> failuresBuilder
                = ImmutableSet.builder();

        /** whether this builder contains any {@link DecodedDataValidationResult ValidationResults} */
        private boolean containsResults;

        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private, use {@link ByteValidationResult#builder()} to
         * create an instance.
         */
        private Builder() {}

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

        /**
         * Adds a failure to the result set
         *
         * @param failureIndex
         *         the index in the array that the validation failure occurred on
         * @param failureType
         *         the type of failure that occurred
         * @param failureReason
         *         the reason for the failure
         *
         * @return this builder
         *
         * @throws NullPointerException
         *         if parameters are {@code null}
         * @throws IllegalArgumentException
         *         if tag or failureReason are empty
         */
        public Builder add(int failureIndex, FailureType failureType, String failureReason)
        {
            final ByteValidationFailure failure = new ByteValidationFailure(failureIndex,
                    failureType,
                    failureReason);
            return add(failure);
        }

        /**
         * Adds a failure to the result set
         *
         * @param failures
         *         result to add
         *
         * @return this builder
         */
        public Builder add(ByteValidationFailure failures)
        {
            failuresBuilder.add(failures);
            containsResults = true;
            return this;
        }

        /**
         * Creates a new instance of {@link ByteValidationResult} containing all the results which
         * have been added to this builder
         *
         * @return a new instance of {@link ByteValidationResult} containing all the results which
         * have been added to this builder
         */
        public ByteValidationResult build()
        {
            final ImmutableSet<ByteValidationFailure> failures = failuresBuilder.build();
            return new ByteValidationResult(failures);
        }

        /**
         * Returns {@code true} if this builder contains any {@link DecodedDataValidationResult
         * ValidationResults}
         *
         * @return {@code true} if this builder contains any {@link DecodedDataValidationResult
         * ValidationResults}
         */
        public boolean containsResults()
        {
            return containsResults;
        }
    }
}
