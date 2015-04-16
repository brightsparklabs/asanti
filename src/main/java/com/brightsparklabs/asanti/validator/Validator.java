/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.validator.result.DecodedDataValidationResult;

/**
 * Used to validate {@link DecodedAsnData} against its associated schema or a
 * custom validation rule.
 *
 * @author brightSPARK Labs
 */
public interface Validator
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates the supplied data using the rules in this validator
     *
     * @param decodedAsnData
     *            data to validate
     *
     * @return the results from validating the data
     */
    public DecodedDataValidationResult validate(DecodedAsnData decodedAsnData);
}
