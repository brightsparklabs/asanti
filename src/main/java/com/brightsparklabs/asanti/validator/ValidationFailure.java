/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.validator.rule.ValidationRule;

/**
 * Represents a validation failure from running a {@link ValidationRule} against
 * {@link DecodedAsnData}.
 *
 * @author brightSPARK Labs
 */
public interface ValidationFailure
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the tag that the validation failure pertains to
     *
     * @return the tag that the validation failure pertains to
     */
    public String getTag();

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
