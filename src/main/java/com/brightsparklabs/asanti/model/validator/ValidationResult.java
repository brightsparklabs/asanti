/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.validator;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;

/**
 * Represents the result from running a {@link ValidationRule} against
 * {@link DecodedAsnData}.
 *
 * @author brightSPARK Labs
 */
public interface ValidationResult
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Determines whether the data failed validation (i.e. the validation rule
     * was unsuccessful).
     *
     * @return {@code true} if the data failed validation; {@code false}
     *         otherwise
     */
    public boolean isFailure();

    /**
     * Returns a string detailing the type of validation failure that occurred
     *
     * @return a string detailing the type of validation failure that occurred
     */
    public String getType();

    /**
     * Returns the tag that the validation failure occurred on
     *
     * @return the tag that the validation failure occurred on
     */
    public String getTag();

    /**
     * Returns a string describing why the validation failure occurred
     *
     * @return a string describing why the validation failure occurred
     */
    public String getReason();
}
