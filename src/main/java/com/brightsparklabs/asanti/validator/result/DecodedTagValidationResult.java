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
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.brightsparklabs.asanti.validator.failure.ValidationFailure;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;

/**
 * Contains the results from running a {@link Validator} over {@link DecodedAsnData}.
 *
 * @author brightSPARK Labs
 */
public class DecodedTagValidationResult
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
     * Default constructor. This is private, use {@link #builder(String)} to create instances.
     *
     * @param failures
     *         failures to include in this result set
     */
    private DecodedTagValidationResult(Iterable<DecodedTagValidationFailure> failures)
    {
        final ImmutableSetMultimap.Builder<String, ValidationFailure> builder = ImmutableSetMultimap
                .builder();
        for (final DecodedTagValidationFailure failure : failures)
        {
            final String tag = failure.getTag();
            builder.put(tag, failure);
        }
        this.tagsToFailures = builder.build();
    }

    /**
     * Returns a builder for creating instances of {@link DecodedTagValidationResult}
     *
     * @param tag
     *         name of the tag the results pertain to
     *
     * @return a builder for creating instances of {@link DecodedTagValidationResult}
     */
    public static Builder builder(String tag)
    {
        return new Builder(tag);
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
     * Builder for creating instances of {@link DecodedTagValidationResult}
     */
    public static class Builder
    {
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /** name of the tag the results pertain to */
        private final String tag;

        /** the results to include in the result set */
        private final ImmutableSet.Builder<DecodedTagValidationFailure> failuresBuilder
                = ImmutableSet.builder();

        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private, use {@link DecodedTagValidationResult#builder(String)}
         * to create an instance.
         *
         * @param tag
         *         name of the tag the results pertain to
         */
        private Builder(String tag)
        {
            this.tag = tag;
        }

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

        /**
         * Adds a failure to the result set
         *
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
        public Builder add(FailureType failureType, String failureReason)
        {
            final DecodedTagValidationFailure failure = new DecodedTagValidationFailure(tag,
                    failureType,
                    failureReason);
            failuresBuilder.add(failure);
            return this;
        }

        /**
         * Creates a new instance of {@link DecodedTagValidationResult} containing all the results
         * which have been added to this builder
         *
         * @return a new instance of {@link DecodedTagValidationResult} containing all the results
         * which have been added to this builder
         */
        public DecodedTagValidationResult build()
        {
            final ImmutableSet<DecodedTagValidationFailure> failures = failuresBuilder.build();
            return new DecodedTagValidationResult(failures);
        }
    }
}
