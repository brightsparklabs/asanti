/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.decoder.builtin.GeneralizedTimeDecoder;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.assam.schema.AsnBuiltinType;
import com.google.common.collect.ImmutableSet;

import java.time.OffsetDateTime;

/**
 * Validator for data of type {@link AsnBuiltinType#GeneralizedTime}
 *
 * @author brightSPARK Labs
 */
public class GeneralizedTimeValidator extends PrimitiveBuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static GeneralizedTimeValidator instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private GeneralizedTimeValidator() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static GeneralizedTimeValidator getInstance()
    {
        if (instance == null)
        {
            instance = new GeneralizedTimeValidator();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: PrimitiveBuiltinTypeValidator
    // -------------------------------------------------------------------------

    @Override
    protected ImmutableSet<ByteValidationFailure> validateNonNullBytes(final byte[] bytes)
    {

        final OperationResult<OffsetDateTime, ImmutableSet<ByteValidationFailure>> result
                = GeneralizedTimeDecoder.validateAndDecode(bytes);

        return result.getFailureReason().orElse(ImmutableSet.of());
    }
}
