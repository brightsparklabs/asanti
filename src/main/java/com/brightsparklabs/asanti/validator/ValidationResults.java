/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.google.common.collect.ImmutableSet;

/**
 * Contains the results from running a {@link Validator} over
 * {@link DecodedAsnData}.
 *
 * @author brightSPARK Labs
 */
public interface ValidationResults
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Determines whether the results contain failures
     *
     * @return {@code true} if the results contain failures; {@code false}
     *         otherwise
     */
    public boolean hasFailures();

    /**
     * Returns all failures that occurred during validation
     *
     * @return all failures that occurred during validation
     */
    public ImmutableSet<ValidationResult> getFailures();
}
