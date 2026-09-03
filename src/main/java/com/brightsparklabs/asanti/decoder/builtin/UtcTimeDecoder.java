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
import com.brightsparklabs.asanti.validator.builtin.TimeValidator;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;
import java.time.DateTimeException;
import java.time.OffsetDateTime;

/**
 * Decoder for data of type {@link AsnBuiltinType#UtcTime}.
 *
 * <p>An ASN.1 {@code UTCTime} is a specialisation of {@code VisibleString} of the form {@code
 * YYMMDDHHMM[SS][Z|(+|-)HH[MM]]} (note: no fractional component). The two-digit year is expanded
 * with a pivot of 2000 (range 1950 to 2049) and the value is then decoded via the shared {@link
 * AsnTimeParser}. When no offset is supplied the value is interpreted in the system default zone.
 *
 * @author brightSPARK Labs
 */
public class UtcTimeDecoder extends AbstractBuiltinTypeDecoder<OffsetDateTime> {
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** Length of a "minutes" precision core, i.e. {@code YYMMDDHHMM}. */
    private static final int LENGTH_MINUTES = 10;

    /** Length of a "seconds" precision core, i.e. {@code YYMMDDHHMMSS}. */
    private static final int LENGTH_SECONDS = 12;

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** Singleton instance. */
    private static UtcTimeDecoder instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance.
     */
    private UtcTimeDecoder() {}

    /** {@return a singleton instance of this class} */
    public static UtcTimeDecoder getInstance() {
        if (instance == null) {
            instance = new UtcTimeDecoder();
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
        // UTCTime is a "useful" specialisation of VisibleString; once validated we return the raw
        // string, as the decoded OffsetDateTime discards the original timezone specifier.
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
     * Validates and decodes UTCTime bytes.
     *
     * @param bytes bytes to be decoded.
     * @return an {@link OffsetDateTime} if successful, or a {@link ByteValidationFailure}
     *     otherwise.
     */
    public static OperationResult<OffsetDateTime, ImmutableSet<ByteValidationFailure>>
            validateAndDecode(final byte[] bytes) {
        // UTCTime is a specialisation of VisibleString, so check that first.
        final ImmutableSet<ByteValidationFailure> failures =
                AsnByteValidator.validateAsVisibleString(bytes);
        if (!failures.isEmpty()) {
            return OperationResult.createUnsuccessfulInstance(null, failures);
        }

        try {
            final String raw = AsnByteDecoder.decodeAsVisibleString(bytes);
            return OperationResult.createSuccessfulInstance(parse(raw));
        } catch (final DateTimeException | IllegalArgumentException | DecodeException e) {
            final String error = TimeValidator.UTCTIME_VALIDATION_ERROR + e.getMessage();
            return OperationResult.createUnsuccessfulInstance(
                    null,
                    ImmutableSet.of(
                            new ByteValidationFailure(
                                    bytes.length, FailureType.DataIncorrectlyFormatted, error)));
        }
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Normalises a UTCTime to GeneralizedTime shape (expanding the two-digit year via a 2000 pivot)
     * and delegates to {@link AsnTimeParser}. UTCTime mandates minutes, forbids a fractional
     * component, and (like GeneralizedTime) requires an uppercase {@code Z}.
     *
     * @param raw the raw (already validated as VisibleString) UTCTime value.
     * @return the decoded {@link OffsetDateTime}.
     * @throws DateTimeException if the value is malformed or invalid.
     */
    private static OffsetDateTime parse(final String raw) {
        if (raw.indexOf('.') >= 0 || raw.indexOf(',') >= 0) {
            throw new DateTimeException(
                    "Invalid format: \"" + raw + "\" - UTCTime does not allow a fraction");
        }

        // The core (before any offset) must be YYMMDDHHMM (10) or YYMMDDHHMMSS (12).
        final int coreLength = raw.length() - AsnTimeParser.offsetLength(raw);
        if (coreLength != LENGTH_MINUTES && coreLength != LENGTH_SECONDS) {
            throw new DateTimeException(
                    "Invalid format: \"" + raw + "\" is not a valid length for a UTCTime");
        }

        final int yy = (raw.charAt(0) - '0') * 10 + (raw.charAt(1) - '0');
        final String century = yy >= 50 ? "19" : "20";
        return AsnTimeParser.parse(century + raw);
    }
}
