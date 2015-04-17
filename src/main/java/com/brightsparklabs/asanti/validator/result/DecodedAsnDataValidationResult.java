/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator.result;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.validator.Validator;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

/**
 * Contains the results from running a {@link Validator} over {@link DecodedAsnData}.
 *
 * @author brightSPARK Labs
 */
public class DecodedAsnDataValidationResult implements ValidationResult
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
    private DecodedAsnDataValidationResult(Iterable<DecodedTagValidationFailure> failures)
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
     * Returns a builder for creating instances of {@link DecodedAsnDataValidationResult}
     *
     * @return a builder for creating instances of {@link DecodedAsnDataValidationResult}
     */
    public static Builder builder()
    {
        return new Builder();
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: ValidationResult
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
     * Builder for creating instances of {@link DecodedAsnDataValidationResult}
     */
    public static class Builder
    {
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /** the results to include in the result set */
        private final ImmutableSet.Builder<DecodedTagValidationFailure> failuresBuilder
                = ImmutableSet.builder();

        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private, use {@link DecodedAsnDataValidationResult#builder()}
         * to create an instance.
         */
        private Builder() {}

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

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
            return this;
        }

        /**
         * Creates a new instance of {@link DecodedAsnDataValidationResult} containing all the
         * results which have been added to this builder
         *
         * @return a new instance of {@link DecodedAsnDataValidationResult} containing all the
         * results which have been added to this builder
         */
        public DecodedAsnDataValidationResult build()
        {
            final ImmutableSet<DecodedTagValidationFailure> failures = failuresBuilder.build();
            return new DecodedAsnDataValidationResult(failures);
        }
    }
}
