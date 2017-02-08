/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.decoder.builtin.UtcTimeDecoder;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.assam.schema.AsnBuiltinType;
import com.google.common.collect.ImmutableSet;

import java.sql.Timestamp;

/**
 * Validator for data of type {@link AsnBuiltinType#UtcTime}
 *
 * @author brightSPARK Labs
 */
public class UtcTimeValidator extends PrimitiveBuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static UtcTimeValidator instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private UtcTimeValidator() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static UtcTimeValidator getInstance()
    {
        if (instance == null)
        {
            instance = new UtcTimeValidator();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: PrimitiveBuiltinTypeValidator
    // -------------------------------------------------------------------------

    @Override
    protected ImmutableSet<ByteValidationFailure> validateNonNullBytes(final byte[] bytes)
    {

        final OperationResult<Timestamp, ImmutableSet<ByteValidationFailure>> result
                = UtcTimeDecoder.validateAndDecode(bytes);

        return result.getFailureReason().orElse(ImmutableSet.of());
    }
}
