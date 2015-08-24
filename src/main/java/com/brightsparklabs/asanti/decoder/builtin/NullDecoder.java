/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeExceptions;
import com.brightsparklabs.asanti.validator.AsnByteValidator;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.assam.exception.DecodeException;
import com.brightsparklabs.assam.schema.AsnBuiltinType;
import com.google.common.collect.ImmutableSet;

/**
 * Decoder for data of type {@link AsnBuiltinType#Null}
 *
 * @author brightSPARK Labs
 */
public class NullDecoder extends AbstractBuiltinTypeDecoder<String>
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static NullDecoder instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private NullDecoder() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static NullDecoder getInstance()
    {
        if (instance == null)
        {
            instance = new NullDecoder();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractBuiltinTypeDecoder
    // -------------------------------------------------------------------------

    @Override
    public String decode(final byte[] bytes) throws DecodeException
    {
        final ImmutableSet<ByteValidationFailure> failures = AsnByteValidator.validateAsNull(bytes);
        DecodeExceptions.throwIfHasFailures(failures);
        return "";
    }
}
