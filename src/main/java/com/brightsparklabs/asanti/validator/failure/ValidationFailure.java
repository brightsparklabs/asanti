/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator.failure;

import com.brightsparklabs.asanti.validator.FailureType;

/**
 * Represents a validation failure.
 *
 * @author brightSPARK Labs
 */
public interface ValidationFailure
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns a string detailing the type of validation failure that occurred
     *
     * @return a string detailing the type of validation failure that occurred
     */
    public FailureType getFailureType();

    /**
     * Returns a string describing why the validation failure occurred
     *
     * @return a string describing why the validation failure occurred
     */
    public String getFailureReason();
}
