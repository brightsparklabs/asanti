/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.failure;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.ValidationFailure;

/**
 * Convenience class for implementing {@link ValidationFailure}.
 *
 * @author brightSPARK Labs
 */
public class AbstractValidationFailure implements ValidationFailure {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the type of failure that occurred */
    private final FailureType failureType;

    /** the reason for the failure */
    private final String failureReason;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param failureType the type of failure that occurred
     * @param failureReason the reason for the failure
     * @throws NullPointerException if parameters are {@code null}
     * @throws IllegalArgumentException if location or failureReason are empty
     */
    public AbstractValidationFailure(FailureType failureType, String failureReason) {
        checkNotNull(failureType);
        checkNotNull(failureReason);
        this.failureType = failureType;
        this.failureReason = failureReason.trim();
        checkArgument(!this.failureReason.isEmpty(), "Failure reason cannot be empty");
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: ValidationFailure
    // -------------------------------------------------------------------------

    @Override
    public FailureType getFailureType() {
        return failureType;
    }

    @Override
    public String getFailureReason() {
        return failureReason;
    }

    @Override
    public String getFailureTag() {
        return "";
    }
}
