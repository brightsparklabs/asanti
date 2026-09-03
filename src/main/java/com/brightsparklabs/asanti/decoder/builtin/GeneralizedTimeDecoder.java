/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeExceptions;
import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.decoder.AsnByteDecoder;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.AsnByteValidator;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.builtin.GeneralizedTimeValidator;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;
import java.time.DateTimeException;
import java.time.OffsetDateTime;

/**
 * Decoder for data of type {@link AsnBuiltinType#GeneralizedTime}.
 *
 * <p>An ASN.1 {@code GeneralizedTime} is a specialisation of {@code VisibleString} of the form
 * {@code YYYYMMDDHH[MM[SS]][(.|,)fraction][Z|(+|-)HH[MM]]}, where the optional fraction applies to
 * the smallest time unit present. Parsing is delegated to the shared {@link AsnTimeParser}, which
 * is built on {@code java.time} (no third-party date/time libraries) and resolves the fractional
 * component to nanosecond precision. When no offset is supplied the value is interpreted in the
 * system default zone.
 *
 * @author brightSPARK Labs
 */
public class GeneralizedTimeDecoder extends AbstractBuiltinTypeDecoder<OffsetDateTime> {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** Singleton instance. */
    private static GeneralizedTimeDecoder instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance.
     */
    private GeneralizedTimeDecoder() {}

    /** {@return a singleton instance of this class} */
    public static GeneralizedTimeDecoder getInstance() {
        if (instance == null) {
            instance = new GeneralizedTimeDecoder();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractBuiltinTypeDecoder
    // -------------------------------------------------------------------------

    @Override
    public OffsetDateTime decode(final byte[] bytes) throws DecodeException {
        final OperationResult<OffsetDateTime, ImmutableSet<ByteValidationFailure>> result =
                validateAndDecode(bytes);
        if (!result.wasSuccessful()) {
            DecodeExceptions.throwIfHasFailures(
                    result.getFailureReason().orElse(ImmutableSet.of()));
        }
        return result.getOutput();
    }

    @Override
    public String decodeAsString(final byte[] bytes) throws DecodeException {
        // GeneralizedTime is a "useful" specialisation of VisibleString; once validated we return
        // the raw string, as the decoded OffsetDateTime discards the original timezone specifier.
        final OperationResult<OffsetDateTime, ImmutableSet<ByteValidationFailure>> result =
                validateAndDecode(bytes);
        if (!result.wasSuccessful()) {
            DecodeExceptions.throwIfHasFailures(
                    result.getFailureReason().orElse(ImmutableSet.of()));
        }
        return AsnByteDecoder.decodeAsVisibleString(bytes);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates and decodes GeneralizedTime bytes.
     *
     * @param bytes bytes to be decoded.
     * @return an {@link OffsetDateTime} if successful, or a {@link ByteValidationFailure}
     *     otherwise.
     */
    public static OperationResult<OffsetDateTime, ImmutableSet<ByteValidationFailure>>
            validateAndDecode(final byte[] bytes) {
        // GeneralizedTime is a specialisation of VisibleString, so check that first.
        final ImmutableSet<ByteValidationFailure> failures =
                AsnByteValidator.validateAsVisibleString(bytes);
        if (!failures.isEmpty()) {
            return OperationResult.createUnsuccessfulInstance(null, failures);
        }

        try {
            final String raw = AsnByteDecoder.decodeAsVisibleString(bytes);
            return OperationResult.createSuccessfulInstance(AsnTimeParser.parse(raw));
        } catch (final DateTimeException | IllegalArgumentException | DecodeException e) {
            final String error =
                    GeneralizedTimeValidator.GENERALIZEDTIME_VALIDATION_ERROR + e.getMessage();
            return OperationResult.createUnsuccessfulInstance(
                    null,
                    ImmutableSet.of(
                            new ByteValidationFailure(
                                    bytes.length, FailureType.DataIncorrectlyFormatted, error)));
        }
    }
}
