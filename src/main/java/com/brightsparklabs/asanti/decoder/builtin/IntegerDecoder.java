/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.AsnByteValidator;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;

import java.math.BigInteger;

/**
 * Decoder for data of type {@link AsnBuiltinType#Integer}
 *
 * @author brightSPARK Labs
 */
public class IntegerDecoder extends AbstractBuiltinTypeDecoder<BigInteger>
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static IntegerDecoder instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private IntegerDecoder() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static IntegerDecoder getInstance()
    {
        if (instance == null)
        {
            instance = new IntegerDecoder();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractBuiltinTypeDecoder
    // -------------------------------------------------------------------------

    @Override
    public BigInteger decode(final byte[] bytes) throws DecodeException
    {
        final ImmutableSet<ByteValidationFailure> failures = AsnByteValidator.validateAsInteger(
                bytes);
        DecodeException.throwIfHasFailures(failures);
        // The Java BigInteger aligns with the ASN.1 concept of Integer, in that
        // it can be arbitrarily many bytes, and is by default signed.
        return new BigInteger(bytes);
    }
}
