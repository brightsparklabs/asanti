/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.result;

import com.brightsparklabs.asanti.validator.ValidationFailure;
import com.brightsparklabs.asanti.validator.ValidationResult;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

/**
 * Implementation of {@link ValidationResult}.
 *
 * @author brightSPARK Labs
 */
public class ValidationResultImpl implements ValidationResult {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** all failures that occurred during validation. Map is of form {tag => failure} */
    private final ImmutableSetMultimap<String, ValidationFailure> tagsToFailures;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor. This is private, use {@link #builder()} to create instances.
     *
     * @param failures failures to include in this result set
     */
    private ValidationResultImpl(Iterable<ValidationFailure> failures) {
        final ImmutableSetMultimap.Builder<String, ValidationFailure> builder =
                ImmutableSetMultimap.builder();
        for (final ValidationFailure failure : failures) {
            final String tag = failure.getFailureTag();
            builder.put(tag, failure);
        }
        this.tagsToFailures = builder.build();
    }

    /**
     * Returns a builder for creating instances of {@link ValidationResultImpl}
     *
     * @return a builder for creating instances of {@link ValidationResultImpl}
     */
    public static Builder builder() {
        return new Builder();
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: ValidationResult
    // -------------------------------------------------------------------------

    @Override
    public boolean hasFailures() {
        return !tagsToFailures.isEmpty();
    }

    @Override
    public ImmutableSet<ValidationFailure> getFailures() {
        return ImmutableSet.copyOf(tagsToFailures.values());
    }

    @Override
    public boolean hasFailures(String tag) {
        return !tagsToFailures.get(tag).isEmpty();
    }

    @Override
    public ImmutableSet<ValidationFailure> getFailures(String tag) {
        return tagsToFailures.get(tag);
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Builder
    // -------------------------------------------------------------------------

    /** Builder for creating instances of {@link ValidationResultImpl} */
    public static class Builder {
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /** the results to include in the result set */
        private final ImmutableSet.Builder<ValidationFailure> failuresBuilder =
                ImmutableSet.builder();

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
         * @param failures result to add
         * @return this builder
         */
        @CanIgnoreReturnValue
        public Builder add(ValidationFailure failures) {
            failuresBuilder.add(failures);
            return this;
        }

        /**
         * Adds a failure to the result set
         *
         * @param failures failures to add
         * @return this builder
         */
        @CanIgnoreReturnValue
        public Builder addAll(Iterable<? extends ValidationFailure> failures) {
            failuresBuilder.addAll(failures);
            return this;
        }

        /**
         * Creates a new instance of {@link ValidationResultImpl} containing all the results which
         * have been added to this builder
         *
         * @return a new instance of {@link ValidationResultImpl} containing all the results which
         *     have been added to this builder
         */
        public ValidationResultImpl build() {
            final ImmutableSet<ValidationFailure> failures = failuresBuilder.build();
            return new ValidationResultImpl(failures);
        }
    }
}
