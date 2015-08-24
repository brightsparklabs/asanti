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
 * Decoder for data of type {@link AsnBuiltinType#Iri}
 *
 * @author brightSPARK Labs
 */
public class IriDecoder extends AbstractBuiltinTypeDecoder<String>
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static IriDecoder instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private IriDecoder() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static IriDecoder getInstance()
    {
        if (instance == null)
        {
            instance = new IriDecoder();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractBuiltinTypeDecoder
    // -------------------------------------------------------------------------

    @Override
    public String decode(final byte[] bytes) throws DecodeException
    {
        final ImmutableSet<ByteValidationFailure> failures = AsnByteValidator.validateAsIri(bytes);
        DecodeExceptions.throwIfHasFailures(failures);
        // TODO: ASN-107 implement
        return null;
    }
}
