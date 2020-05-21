/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.data.AsnData;
import com.google.common.collect.ImmutableSet;

/**
 * Contains the results from running a {@link Validator} over {@link AsnData}.
 *
 * @author brightSPARK Labs
 */
public interface ValidationResult {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Determines whether failures occurred during validation
     *
     * @return {@code true} if failures occurred during validation; {@code false} otherwise
     */
    boolean hasFailures();

    /**
     * Determines whether failures occurred while validating the specified tag
     *
     * @param tag the tag of interest (e.g. "/Document/header/published/date")
     * @return {@code true} if failures occurred while validating the specified tag; {@code false}
     *     otherwise
     */
    boolean hasFailures(String tag);

    /**
     * Returns all failures that occurred during validation
     *
     * @return all failures that occurred during validation
     */
    ImmutableSet<ValidationFailure> getFailures();

    /**
     * Returns all failures that occurred validating the specified tag
     *
     * @param tag the tag of interest (e.g. "/Document/header/published/date")
     * @return all failures that occurred validating the specified tag
     */
    ImmutableSet<ValidationFailure> getFailures(String tag);
}
