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
 * Validator for data of type {@link AsnBuiltinType#VisibleString}
 *
 * @author brightSPARK Labs
 */
public class VisibleStringValidator extends PrimitiveBuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static VisibleStringValidator instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private VisibleStringValidator() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static VisibleStringValidator getInstance()
    {
        if (instance == null)
        {
            instance = new VisibleStringValidator();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: PrimitiveBuiltinTypeValidator
    // -------------------------------------------------------------------------

    // TODO ASN-105 (review): this seems to be the same validation as PrintableStringValidator. Can we just sub-class and change error message? Perhaps make error a param to constructor than call super(errorMessage)?

    @Override
    protected ImmutableSet<ByteValidationFailure> validateNonNullBytes(final byte[] bytes)
    {
        final Set<ByteValidationFailure> failures = Sets.newHashSet();

        // TODO ASN-105 (review): use enhanced for loop: for(byte b : bytes)
        for (int i = 0; i < bytes.length; i++)
        {
            byte b = bytes[i];
            // TODO ASN-105 (review): use com.brightsparklabs.asanti.common.ByteArrays.containsNonPrintableChars()
            if (b < 32 || b > 126)
            {
                final String error =
                        "Supplied bytes do not conform to the VisibleString format. All bytes must be within the range 32 - 126. Supplied bytes contain a byte with value: "
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
