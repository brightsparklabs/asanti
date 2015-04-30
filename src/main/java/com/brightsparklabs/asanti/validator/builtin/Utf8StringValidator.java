/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;

/**
 * Validator for data of type {@link AsnBuiltinType#Utf8String}
 *
 * @author brightSPARK Labs
 */
public class Utf8StringValidator extends PrimitiveBuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static Utf8StringValidator instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private Utf8StringValidator() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static Utf8StringValidator getInstance()
    {
        if (instance == null)
        {
            instance = new Utf8StringValidator();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: PrimitiveBuiltinTypeValidator
    // -------------------------------------------------------------------------

    @Override
    protected ImmutableSet<ByteValidationFailure> validateNonNullBytes(final byte[] bytes)
    {
        /*
         * NOTE: If the below has poor performance we can process the array directly and check each
         *       leading byte is followed by the expected number of trailing bytes. For an example
         *       see http://codereview.stackexchange.com/a/59439
         */
        final CharsetDecoder decoder = Charsets.UTF_8.newDecoder();
        try
        {
            decoder.decode(ByteBuffer.wrap(bytes));
            return ImmutableSet.of();
        }
        catch (CharacterCodingException e)
        {
            final ByteValidationFailure failure = new ByteValidationFailure(bytes.length,
                    FailureType.DataIncorrectlyFormatted,
                    "ASN.1 UTF8String type must be encoded in UTF-8");
            return ImmutableSet.of(failure);
        }
    }
}
