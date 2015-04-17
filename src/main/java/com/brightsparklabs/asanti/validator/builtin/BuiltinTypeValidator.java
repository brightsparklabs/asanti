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
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;

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

    public ImmutableSet<DecodedTagValidationFailure> validate(String tag,
            DecodedAsnData decodedAsnData);
}
