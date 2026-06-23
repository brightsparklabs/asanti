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

    /** The index in the array that the validation failure occurred on. */
    private final int failureIndex;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param failureIndex The index in the array that the validation failure occurred on.
     * @param failureType The type of failure that occurred.
     * @param failureReason The reason for the failure.
     * @throws NullPointerException If parameters are {@code null}.
     * @throws IllegalArgumentException If location or failureReason are empty.
     */
    public ByteValidationFailure(
            final int failureIndex, final FailureType failureType, final String failureReason) {
        super(failureType, failureReason);
        this.failureIndex = failureIndex;
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /** {@return the index in the array that the validation failure occurred on} */
    public int getFailureIndex() {
        return failureIndex;
    }
}
