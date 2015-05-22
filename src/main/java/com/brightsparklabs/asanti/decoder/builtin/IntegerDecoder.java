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
        // TODO: ASN-107 implement
        //return null;

        // MJF - just for now, so I can do ASN-123 against a schema with an INTEGER in it, I'll provide this
        // mechanism, which for at least 8 and 16 bit positive integers that I have tried...
        // Whoever does this actual task needs to verify negative numbers, byte ordering and all those cases
        // work with this mechanism.
        return new BigInteger(bytes);
    }
}
