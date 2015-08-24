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
 * Validator for data of type {@link AsnBuiltinType#Null}
 *
 * @author brightSPARK Labs
 */
public class NullValidator extends PrimitiveBuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static NullValidator instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private NullValidator() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static NullValidator getInstance()
    {
        if (instance == null)
        {
            instance = new NullValidator();
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

        if (bytes.length != 0)
        {
            final String error = NULL_VALIDATION_ERROR;
            final ByteValidationFailure failure = new ByteValidationFailure(0,
                    FailureType.DataIncorrectlyFormatted,
                    error);
            failures.add(failure);
        }

        return ImmutableSet.copyOf(failures);
    }
}
