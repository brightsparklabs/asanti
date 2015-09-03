/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.ByteArrays;
import com.brightsparklabs.asanti.common.DecodeExceptions;
import com.brightsparklabs.asanti.validator.AsnByteValidator;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.assam.exception.DecodeException;
import com.brightsparklabs.assam.schema.AsnBuiltinType;
import com.google.common.collect.ImmutableSet;

/**
 * Decoder for data of type {@link AsnBuiltinType#OctetString}
 *
 * @author brightSPARK Labs
 */
public class OctetStringDecoder extends AbstractBuiltinTypeDecoder<byte[]>
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static OctetStringDecoder instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private OctetStringDecoder() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static OctetStringDecoder getInstance()
    {
        if (instance == null)
        {
            instance = new OctetStringDecoder();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractBuiltinTypeDecoder
    // -------------------------------------------------------------------------

    @Override
    public String decodeAsString(final byte[] bytes) throws DecodeException
    {
        final byte[] validatedBytes = decode(bytes);

        return ByteArrays.containsNonPrintableChars(validatedBytes) ?
                ByteArrays.toHexString(validatedBytes) :
                ByteArrays.toString(validatedBytes);
    }

    @Override
    public byte[] decode(final byte[] bytes) throws DecodeException
    {
        final ImmutableSet<ByteValidationFailure> failures = AsnByteValidator.validateAsOctetString(
                bytes);
        DecodeExceptions.throwIfHasFailures(failures);
        return bytes;
    }
}
