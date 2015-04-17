/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator.result;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.validator.Validator;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;

/**
 * Contains the results from running a {@link Validator} over {@link DecodedAsnData}.
 *
 * @author brightSPARK Labs
 */
public interface ValidationResult
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Determines whether failures occurred during validation
     *
     * @return {@code true} if failures occurred during validation; {@code false} otherwise
     */
    public boolean hasFailures();

    /**
     * Determines whether failures occurred while validating the specified tag
     *
     * @param tag
     *         the tag of interest (e.g. "/Document/header/published/date")
     *
     * @return {@code true} if failures occurred while validating the specified tag; {@code false}
     * otherwise
     */
    public boolean hasFailures(String tag);

    /**
     * Returns all failures that occurred during validation
     *
     * @return all failures that occurred during validation
     */
    public ImmutableSet<DecodedTagValidationFailure> getFailures();

    /**
     * Returns all failures that occurred validating the specified tag
     *
     * @param tag
     *         the tag of interest (e.g. "/Document/header/published/date")
     *
     * @return all failures that occurred validating the specified tag
     */
    public ImmutableSet<DecodedTagValidationFailure> getFailures(String tag);
}
