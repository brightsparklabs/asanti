/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.typedefinitions;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.ValidationResult;
import com.brightsparklabs.asanti.validator.bytes.AsnByteValidator;
import com.google.common.base.Charsets;

/**
 * Decoder for data of type {@link AsnBuiltinType#Ia5String}
 *
 * @author brightSPARK Labs
 */
public class Ia5StringDecoder implements AsnTypeDefinitionDecoder
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static Ia5StringDecoder instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private Ia5StringDecoder() { }

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static Ia5StringDecoder getInstance()
    {
        if (instance == null)
        {
            instance = new Ia5StringDecoder();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnTypeDefinitionDecoder
    // -------------------------------------------------------------------------

    @Override
    public String decode(final byte[] bytes) throws DecodeException
    {
        final ValidationResult validationResult = AsnByteValidator.validateAsIa5String(bytes);
        DecodeException.throwIfHasFailures(validationResult);
        return new String(bytes, Charsets.UTF_8);
    }

    @Override
    public String decodeAsString(final byte[] data) throws DecodeException
    {
        return decode(data);
    }
}
