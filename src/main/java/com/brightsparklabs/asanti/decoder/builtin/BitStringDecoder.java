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

/**
 * Decoder for data of type {@link AsnBuiltinType#BitString}
 *
 * @author brightSPARK Labs
 */
public class BitStringDecoder extends AbstractBuiltinTypeDecoder<String>
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static BitStringDecoder instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private BitStringDecoder() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static BitStringDecoder getInstance()
    {
        if (instance == null)
        {
            instance = new BitStringDecoder();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractBuiltinTypeDecoder
    // -------------------------------------------------------------------------

    @Override
    public String decode(final byte[] bytes) throws DecodeException
    {
        final ImmutableSet<ByteValidationFailure> failures = AsnByteValidator.validateAsBitString(
                bytes);
        DecodeException.throwIfHasFailures(failures);

        // loop through all bytes and append binary string
        final StringBuilder bitStringBuilder = new StringBuilder();
        for (final byte b : bytes)
        {
            // convert to unsigned byte while using Integer.toBinaryString method and pad with zeros
            // to ensure 8 bits in length
            bitStringBuilder.append(String.format("%8s", Integer.toBinaryString(b & 0xFF))
                    .replace(' ', '0'));
        }

        return bitStringBuilder.toString();
    }
}
