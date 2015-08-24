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
 * Decoder for data of type {@link AsnBuiltinType#VideotexString}
 *
 * @author brightSPARK Labs
 */
public class VideotexStringDecoder extends AbstractBuiltinTypeDecoder<String>
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static VideotexStringDecoder instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private VideotexStringDecoder() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static VideotexStringDecoder getInstance()
    {
        if (instance == null)
        {
            instance = new VideotexStringDecoder();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractBuiltinTypeDecoder
    // -------------------------------------------------------------------------

    @Override
    public String decode(final byte[] bytes) throws DecodeException
    {
        final ImmutableSet<ByteValidationFailure> failures
                = AsnByteValidator.validateAsVideotexString(bytes);
        DecodeExceptions.throwIfHasFailures(failures);
        // TODO: ASN-107 implement
        return null;
    }
}
