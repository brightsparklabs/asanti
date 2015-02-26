/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import java.util.List;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

/**
 * Contains the results from running a {@link Validator} over
 * {@link DecodedAsnData}.
 *
 * @author brightSPARK Labs
 */
public class ValidationResultsImpl implements ValidationResults
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** all results which are failures */
    private final ImmutableSet<ValidationResult> failures;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor. This is private, use {@link #builder()} to create
     * instances.
     *
     * @param results
     *            results to include in this result set
     */
    private ValidationResultsImpl(Iterable<ValidationResult> results)
    {
        final ImmutableSet.Builder<ValidationResult> failuresBuilder = ImmutableSet.builder();
        for (final ValidationResult result : results)
        {
            if (result.isFailure())
            {
                failuresBuilder.add(result);
            }
        }
        this.failures = failuresBuilder.build();
    }

    /**
     * Returns a builder for creating instances of {@link ValidationResultsImpl}
     *
     * @return a builder for creating instances of {@link ValidationResultsImpl}
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
    public ImmutableSet<ValidationResult> getFailures()
    {
        return failures;
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Builder
    // -------------------------------------------------------------------------

    /**
     * Builder for creating instances of {@link ValidationResultsImpl}
     */
    public static class Builder
    {
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /** the results to include in the result set */
        private final List<ValidationResult> results = Lists.newArrayList();

        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private, use
         * {@link ValidationResultsImpl#builder()} to create an instance.
         */
        private Builder()
        {
        }

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

        /**
         * Adds a result to the result set
         *
         * @param result
         *            result to add
         *
         * @return this builder
         */
        public Builder add(ValidationResult result)
        {
            results.add(result);
            return this;
        }

        /**
         * Creates a new instance of {@link ValidationResultsImpl} containing
         * all the results which have been added to this builder
         *
         * @return a new instance of {@link ValidationResultsImpl} containing
         *         all the results which have been added to this builder
         */
        public ValidationResultsImpl build()
        {
            return new ValidationResultsImpl(results);
        }
    }
}
