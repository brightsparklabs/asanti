/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator;

/**
 * Represents a validation failure.
 *
 * @author brightSPARK Labs
 */
public interface ValidationFailure {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns a string detailing the type of validation failure that occurred
     *
     * @return a string detailing the type of validation failure that occurred
     */
    FailureType getFailureType();

    /**
     * Returns a string describing why the validation failure occurred
     *
     * @return a string describing why the validation failure occurred
     */
    String getFailureReason();

    /**
     * Returns the name of the tag the validation failure occurred on
     *
     * @return the name of the tag the validation failure occurred to
     */
    String getFailureTag();
}
