/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator.result;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.Validator;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

/**
 * Contains the results from running a {@link Validator} over {@link DecodedAsnData}.
 *
 * @author brightSPARK Labs
 */
public class DecodedAsnDataValidationResultImpl implements DecodedDataValidationResult
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /**
     * all failures that occurred during validation. Map is of form {tag => failure}
     */
    private final ImmutableSetMultimap<String, DecodedTagValidationFailure> tagsToFailures;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor. This is private, use {@link #builder()} to create instances.
     *
     * @param failures
     *         failures to include in this result set
     */
    private DecodedAsnDataValidationResultImpl(Iterable<DecodedTagValidationFailure> failures)
    {
        final ImmutableSetMultimap.Builder<String, DecodedTagValidationFailure> builder
                = ImmutableSetMultimap.builder();
        for (final DecodedTagValidationFailure failure : failures)
        {
            final String tag = failure.getTag();
            builder.put(tag, failure);
        }
        this.tagsToFailures = builder.build();
    }

    /**
     * Returns a builder for creating instances of {@link DecodedAsnDataValidationResultImpl}
     *
     * @return a builder for creating instances of {@link DecodedAsnDataValidationResultImpl}
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
        return !tagsToFailures.isEmpty();
    }

    @Override
    public ImmutableSet<DecodedTagValidationFailure> getFailures()
    {
        return ImmutableSet.copyOf(tagsToFailures.values());
    }

    @Override
    public boolean hasFailures(String tag)
    {
        return !tagsToFailures.get(tag).isEmpty();
    }

    @Override
    public ImmutableSet<DecodedTagValidationFailure> getFailures(String tag)
    {
        return tagsToFailures.get(tag);
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Builder
    // -------------------------------------------------------------------------

    /**
     * Builder for creating instances of {@link DecodedAsnDataValidationResultImpl}
     */
    public static class Builder
    {
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /** the results to include in the result set */
        private final ImmutableSet.Builder<DecodedTagValidationFailure> failuresBuilder
                = ImmutableSet.builder();

        /** whether this builder contains any {@link DecodedDataValidationResult ValidationResults} */
        private boolean containsResults;

        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private, use {@link DecodedAsnDataValidationResultImpl#builder()}
         * to create an instance.
         */
        private Builder() {}

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

        /**
         * Adds a failure to the result set
         *
         * @param location
         *         the location of the failure
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
        public Builder add(String location, FailureType failureType, String failureReason)
        {
            final DecodedTagValidationFailure failure = new DecodedTagValidationFailure(location,
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
        public Builder add(DecodedTagValidationFailure failures)
        {
            failuresBuilder.add(failures);
            containsResults = true;
            return this;
        }

        /**
         * Adds a failure to the result set
         *
         * @param failures
         *         failures to add
         *
         * @return this builder
         */
        public Builder addAll(Iterable<DecodedTagValidationFailure> failures)
        {
            failuresBuilder.addAll(failures);
            containsResults = true;
            return this;
        }

        /**
         * Creates a new instance of {@link DecodedAsnDataValidationResultImpl} containing all the
         * results which have been added to this builder
         *
         * @return a new instance of {@link DecodedAsnDataValidationResultImpl} containing all the
         * results which have been added to this builder
         */
        public DecodedAsnDataValidationResultImpl build()
        {
            final ImmutableSet<DecodedTagValidationFailure> failures = failuresBuilder.build();
            return new DecodedAsnDataValidationResultImpl(failures);
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
