/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.validator;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;

/**
 * Represents a rule to validate {@link DecodedAsnData} against.
 *
 * @author brightSPARK Labs
 */
public interface ValidationRule
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates the specified tag in the data
     *
     * @param tag
     *            tag to validate in the data
     *
     * @param decodedAsnData
     *            data to validate
     *
     * @return the results of validation
     */
    public ValidationResult validate(String tag, DecodedAsnData decodedAsnData);
}
