/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

/**
 * Contains the results from running a {@link Validator} over {@link DecodedAsnData}.
 *
 * @author brightSPARK Labs
 */
public class ValidationResultImpl implements ValidationResult
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /**
     * all failures that occurred during validation. Map is of form {tag => failure}
     */
    private final ImmutableSetMultimap<String, ValidationFailure> tagsToFailures;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor. This is private, use {@link #builder()} to create instances.
     *
     * @param failures
     *         failures to include in this result set
     */
    private ValidationResultImpl(Iterable<ValidationFailure> failures)
    {
        final ImmutableSetMultimap.Builder<String, ValidationFailure> builder = ImmutableSetMultimap
                .builder();
        for (final ValidationFailure failure : failures)
        {
            final String tag = failure.getLocation();
            builder.put(tag, failure);
        }
        this.tagsToFailures = builder.build();
    }

    /**
     * Returns a builder for creating instances of {@link ValidationResultImpl}
     *
     * @return a builder for creating instances of {@link ValidationResultImpl}
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
    public ImmutableSet<ValidationFailure> getFailures()
    {
        return ImmutableSet.copyOf(tagsToFailures.values());
    }

    @Override
    public boolean hasFailures(String tag)
    {
        return !tagsToFailures.get(tag).isEmpty();
    }

    @Override
    public ImmutableSet<ValidationFailure> getFailures(String tag)
    {
        return tagsToFailures.get(tag);
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Builder
    // -------------------------------------------------------------------------

    /**
     * Builder for creating instances of {@link ValidationResultImpl}
     */
    public static class Builder
    {
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /** the results to include in the result set */
        private final ImmutableSet.Builder<ValidationFailure> failuresBuilder
                = ImmutableSet.builder();

        /** whether this builder contains any {@link ValidationResult ValidationResults} */
        private boolean containsResults;

        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private, use {@link ValidationResultImpl#builder()} to
         * create an instance.
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
         * @throws NullPointerException
         *         if parameters are {@code null}
         * @throws IllegalArgumentException
         *         if tag or failureReason are empty
         */
        public Builder add(String location, FailureType failureType, String failureReason)
        {
            final ValidationFailure failure = new ValidationFailureImpl(location,
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
        public Builder add(ValidationFailure failures)
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
        public Builder addAll(Iterable<ValidationFailure> failures)
        {
            failuresBuilder.addAll(failures);
            containsResults = true;
            return this;
        }

        /**
         * Creates a new instance of {@link ValidationResultImpl} containing all the results which
         * have been added to this builder
         *
         * @return a new instance of {@link ValidationResultImpl} containing all the results which
         * have been added to this builder
         */
        public ValidationResultImpl build()
        {
            final ImmutableSet<ValidationFailure> failures = failuresBuilder.build();
            return new ValidationResultImpl(failures);
        }

        /**
         * Returns {@code true} if this builder contains any {@link ValidationResult
         * ValidationResults}
         *
         * @return {@code true} if this builder contains any {@link ValidationResult
         * ValidationResults}
         */
        public boolean containsResults()
        {
            return containsResults;
        }
    }
}
