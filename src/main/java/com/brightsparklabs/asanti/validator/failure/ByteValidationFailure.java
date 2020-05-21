/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.failure;

import com.brightsparklabs.asanti.validator.FailureType;

/**
 * Represents a validation failure from validating a byte array.
 *
 * @author brightSPARK Labs
 */
public class ByteValidationFailure extends AbstractValidationFailure {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the index in the array that the validation failure occurred on */
    private final int failureIndex;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param failureIndex the index in the array that the validation failure occurred on
     * @param failureType the type of failure that occurred
     * @param failureReason the reason for the failure
     * @throws NullPointerException if parameters are {@code null}
     * @throws IllegalArgumentException if location or failureReason are empty
     */
    public ByteValidationFailure(int failureIndex, FailureType failureType, String failureReason) {
        super(failureType, failureReason);
        this.failureIndex = failureIndex;
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the index in the array that the validation failure occurred on
     *
     * @return the index in the array that the validation failure occurred on
     */
    public int getFailureIndex() {
        return failureIndex;
    }
}
