/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.validator.rule.ValidationRule;

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
     * Returns the tag that the validation result pertains to
     *
     * @return the tag that the validation result pertains to
     */
    public String getTag();

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
     * @return a string detailing the type of validation failure that occurred;
     *         or an empty string if the validation did not fail
     */
    public FailureType getFailureType();

    /**
     * Returns a string describing why the validation failure occurred
     *
     * @return a string describing why the validation failure occurred; or an
     *         empty string if the validation did not fail
     */
    public String getFailureReason();
}
