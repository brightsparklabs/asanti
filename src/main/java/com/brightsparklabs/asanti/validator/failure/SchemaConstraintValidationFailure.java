/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.failure;

import com.brightsparklabs.asanti.validator.FailureType;

/**
 * Represents a validation failure from checking a schema constraint.
 *
 * @author brightSPARK Labs
 */
public class SchemaConstraintValidationFailure extends AbstractValidationFailure {
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param failureType The type of failure that occurred.
     * @param failureReason The reason for the failure.
     * @throws NullPointerException If parameters are {@code null}.
     * @throws IllegalArgumentException If location or failureReason are empty.
     */
    public SchemaConstraintValidationFailure(
            final FailureType failureType, final String failureReason) {
        super(failureType, failureReason);
    }
}
