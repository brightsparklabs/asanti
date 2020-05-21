/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeExceptions;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.AsnByteValidator;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;

/**
 * Decoder for data of type {@link AsnBuiltinType#BitString}
 *
 * @author brightSPARK Labs
 */
public class BitStringDecoder extends AbstractBuiltinTypeDecoder<String> {
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
     * <p>This is private, use {@link #getInstance()} to obtain an instance
     */
    private BitStringDecoder() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static BitStringDecoder getInstance() {
        if (instance == null) {
            instance = new BitStringDecoder();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractBuiltinTypeDecoder
    // -------------------------------------------------------------------------

    @Override
    public String decode(final byte[] bytes) throws DecodeException {
        final ImmutableSet<ByteValidationFailure> failures =
                AsnByteValidator.validateAsBitString(bytes);
        DecodeExceptions.throwIfHasFailures(failures);

        // first byte is always the length of unused bits
        final int unusedBitLength = bytes[0];

        // loop through all bytes after first byte and append binary string
        final StringBuilder bitStringBuilder = new StringBuilder();
        for (int i = 1; i < bytes.length; i++) {
            // convert to unsigned byte while using Integer.toBinaryString method and pad with zeros
            // to ensure 8 bits in length
            bitStringBuilder.append(
                    String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF))
                            .replace(' ', '0'));
        }

        // remove unused bits from final string
        return bitStringBuilder.substring(0, bitStringBuilder.length() - unusedBitLength);
    }
}
