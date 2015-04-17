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
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;

/**
 * Used to validate tags based on the kind of ASN.1 Built-in Type they came from.
 *
 * @author brightSPARK Labs
 */
public interface BuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates the supplied tag in the data based on the the kind of ASN.1 Built-in Type
     * represented by this validator
     *
     * @param tag
     *         tag to validate
     * @param decodedAsnData
     *         data to retrieve tag from
     *
     * @return any failures encountered while validating the tag
     */
    public ImmutableSet<DecodedTagValidationFailure> validate(String tag,
            DecodedAsnData decodedAsnData);

    /**
     * Validates the supplied bytes based on the the kind of ASN.1 Built-in Type represented by this
     * validator
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures encountered while validating the bytes
     */
    public ImmutableSet<ByteValidationFailure> validate(byte[] bytes);
}
