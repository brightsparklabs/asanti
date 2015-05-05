/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Validator for data of type {@link AsnBuiltinType#NumericString}
 *
 * @author brightSPARK Labs
 */
public class NumericStringValidator extends PrimitiveBuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static NumericStringValidator instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private NumericStringValidator() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static NumericStringValidator getInstance()
    {
        if (instance == null)
        {
            instance = new NumericStringValidator();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: PrimitiveBuiltinTypeValidator
    // -------------------------------------------------------------------------

    @Override
    protected ImmutableSet<ByteValidationFailure> validateNonNullBytes(final byte[] bytes)
    {
        final Set<ByteValidationFailure> failures = Sets.newHashSet();
        // TODO ASN-105 (review): use enhanced for loop: for(byte b : bytes)
        for (int i = 0; i < bytes.length; i++)
        {
            byte b = bytes[i];
            if (b < '0' || b > '9')
            {
                // TODO ASN-105 (review): instead of '48 - 57' use '0x30 - 0x39' (this makes more sense to our users)
                final String error =
                        "Supplied bytes do not conform to the NumericString format. All bytes must be within the range '0' - '9' (48 - 57). Supplied bytes contain a byte with value: "
                                + b;
                final ByteValidationFailure failure = new ByteValidationFailure(i,
                        FailureType.DataIncorrectlyFormatted,
                        error);
                failures.add(failure);
            }
        }
        return ImmutableSet.copyOf(failures);
    }
}
