/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator.result;

import com.google.common.collect.ImmutableSet;

/**
 * Contains the results from running a validation.
 *
 * @param <T>
 *         the type of failure results contained in this result
 *
 * @author brightSPARK Labs
 */
public interface ValidationResult<T>
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
     * Returns all failures that occurred during validation
     *
     * @return all failures that occurred during validation
     */
    public ImmutableSet<T> getFailures();
}
