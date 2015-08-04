/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.decoder.AsnByteDecoder;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.AsnByteValidator;
import com.brightsparklabs.asanti.validator.builtin.EnumeratedValidator;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decoder for data of type {@link AsnBuiltinType#Enumerated}
 *
 * @author brightSPARK Labs
 */
public class EnumeratedDecoder extends AbstractBuiltinTypeDecoder<String>
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(EnumeratedDecoder.class);

    /** singleton instance */
    private static EnumeratedDecoder instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private EnumeratedDecoder() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static EnumeratedDecoder getInstance()
    {
        if (instance == null)
        {
            instance = new EnumeratedDecoder();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractBuiltinTypeDecoder
    // -------------------------------------------------------------------------

    @Override
    public String decode(final byte[] bytes) throws DecodeException
    {
        final ImmutableSet<ByteValidationFailure> failures = AsnByteValidator.validateAsEnumerated(
                bytes);
        DecodeException.throwIfHasFailures(failures);

        return AsnByteDecoder.decodeAsInteger(bytes).toString();
    }

    @Override
    public String decode(final String tag, final DecodedAsnData decodedAsnData) throws DecodeException
    {
        final OperationResult<String, ImmutableSet<DecodedTagValidationFailure>> result
                = EnumeratedValidator.getInstance().validateAndDecode(tag, decodedAsnData);
        if (!result.wasSuccessful())
        {
            DecodeException.throwIfHasFailures(result.getFailureReason()
                    .or(ImmutableSet.<DecodedTagValidationFailure>of()));
        }

        return result.getOutput();
    }
}
