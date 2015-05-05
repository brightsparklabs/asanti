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
 * Validator for data of type {@link AsnBuiltinType#PrintableString}
 *
 * @author brightSPARK Labs
 */
public class PrintableStringValidator extends PrimitiveBuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static PrintableStringValidator instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private PrintableStringValidator() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static PrintableStringValidator getInstance()
    {
        if (instance == null)
        {
            instance = new PrintableStringValidator();
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

            if (!isPrintableByte(b))
            {

                // TODO ASN-105 (review): let's move all error strings into string constants stored in 'BuiltinTypeValidator' interface. Makes it easier to manage/internationalise
                final String error = "Supplied bytes do not conform to the PrintableString format.";
                final ByteValidationFailure failure = new ByteValidationFailure(i,
                        FailureType.DataIncorrectlyFormatted,
                        error);
                failures.add(failure);
            }
        }

        return ImmutableSet.copyOf(failures);
    }

    // TODO ASN-105 (review): get rid of this, use com.brightsparklabs.asanti.common.ByteArrays.containsNonPrintableChars()

    /**
     * Returns true if byte is within the range conforming to PrintableString format. The range
     * comprises of: A, B, ..., Z a, b, ..., z 0, 1, ..., 9 (space) ' ( ) + , - . / : = ?
     *
     * @return true if byte is within the range conforming to PrintableString format
     */
    private boolean isPrintableByte(final byte b)
    {
        if ((b >= 'A') && (b <= 'Z'))
        {
            return true;
        }
        if ((b >= 'a') && (b <= 'z'))
        {
            return true;
        }
        if ((b >= '0') && (b <= '9'))
        {
            return true;
        }
        switch (b)
        {
            case ' ':
            case '\'':
            case '(':
            case ')':
            case '+':
            case ',':
            case '-':
            case '.':
            case '/':
            case ':':
            case '=':
            case '?':
                return true;
        }
        return false;
    }
}
