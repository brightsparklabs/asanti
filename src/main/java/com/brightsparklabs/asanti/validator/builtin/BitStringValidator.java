/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.assam.schema.AsnBuiltinType;
import com.brightsparklabs.assam.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Validator for data of type {@link AsnBuiltinType#BitString}
 *
 * @author brightSPARK Labs
 */
public class BitStringValidator extends PrimitiveBuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static BitStringValidator instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private BitStringValidator() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static BitStringValidator getInstance()
    {
        if (instance == null)
        {
            instance = new BitStringValidator();
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

        if (bytes.length > 0)
        {
            int firstByte = bytes[0] & 0xFF;
            if (firstByte > 0x07)
            {
                final String error = BIT_STRING_VALIDATION_ERROR + String.format("0x%02X ",
                        firstByte);
                final ByteValidationFailure failure = new ByteValidationFailure(0,
                        FailureType.DataIncorrectlyFormatted,
                        error);
                failures.add(failure);
            }
        }
        else
        {
            final String error = String.format(EMPTY_BYTE_ARRAY_VALIDATION_ERROR, "BIT STRING");
            final ByteValidationFailure failure = new ByteValidationFailure(0,
                    FailureType.DataIncorrectlyFormatted,
                    error);
            failures.add(failure);
        }

        return ImmutableSet.copyOf(failures);
    }
}
