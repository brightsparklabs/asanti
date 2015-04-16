/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.validator.Validator;
import com.brightsparklabs.asanti.validator.result.DecodedTagValidationResult;

/**
 * Default implementation of {@link Validator}.
 *
 * @author brightSPARK Labs
 */
public interface BuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    public DecodedTagValidationResult validate(String tag, DecodedAsnData decodedAsnData);
}
