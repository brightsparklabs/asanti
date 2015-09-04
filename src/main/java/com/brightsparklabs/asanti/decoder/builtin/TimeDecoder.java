/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.decoder.AsnByteDecoder;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.AsnByteValidator;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.builtin.TimeValidator;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

/**
 * Decoder for data of type {@link AsnBuiltinType#Time}
 *
 * @author brightSPARK Labs
 */
public class TimeDecoder extends AbstractBuiltinTypeDecoder<Timestamp>
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(TimeDecoder.class);

    /** singleton instance */
    private static TimeDecoder instance;

    /**
     * parser for the "core" of what an ASN.1 UTCTime MUST consist of. Pivot year 2000 gives a
     * supported range of 1950 to 2049.
     */
    private static final DateTimeFormatter core = new DateTimeFormatterBuilder().appendTwoDigitYear(
            2000).appendMonthOfYear(2).appendDayOfMonth(2).appendHourOfDay(2).toFormatter();

    /** a time zone offset parser, specifying "Z" as no timezone, ie UTC */
    private static final DateTimeParser offset
            = new DateTimeFormatterBuilder().appendTimeZoneOffset("", "Z", false, 1, 2).toParser();

    /** parser option for when only up to the Hours are defined, so optional decimals and offset */
    private static final DateTimeFormatter uptoHours = new DateTimeFormatterBuilder().append(core)
            .appendOptional(new DateTimeFormatterBuilder().appendFractionOfHour(1, 18).toParser())
            .appendOptional(offset)
            .toFormatter();

    /** parser option for when only up to the Minutes are defined, so optional decimals and offset */
    private static final DateTimeFormatter uptoMinutes = new DateTimeFormatterBuilder().append(core)
            .appendMinuteOfHour(2)
            .appendOptional(new DateTimeFormatterBuilder().appendFractionOfMinute(1, 18).toParser())
            .appendOptional(offset)
            .toFormatter();

    /**
     * parser option for when full hours, minutes and seconds are defined, optional offset
     */
    private static final DateTimeFormatter uptoSeconds = new DateTimeFormatterBuilder().append(core)
            .appendMinuteOfHour(2)
            .appendSecondOfMinute(2)
            .appendOptional(offset)
            .toFormatter();

    /** the collection of parsers to try - this is essentially how you do an "OR" with Joda */
    private static final DateTimeParser[] options = { uptoSeconds.getParser(),
                                                      uptoMinutes.getParser(),
                                                      uptoHours.getParser() };

    /**
     * The parser to use, it is an OR of the three precisions, each of which has its optional
     * components
     */
    private static final DateTimeFormatter parser = new DateTimeFormatterBuilder().append(null,
            options).toFormatter();

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private TimeDecoder() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static TimeDecoder getInstance()
    {
        if (instance == null)
        {
            instance = new TimeDecoder();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractBuiltinTypeDecoder
    // -------------------------------------------------------------------------

    @Override
    public Timestamp decode(final byte[] bytes) throws DecodeException
    {
        OperationResult<Timestamp, ImmutableSet<ByteValidationFailure>> result = validateAndDecode(
                bytes);
        if (!result.wasSuccessful())
        {
            DecodeException.throwIfHasFailures(result.getFailureReason()
                    .or(ImmutableSet.<ByteValidationFailure>of()));
        }

        return result.getOutput();
    }

    @Override
    public String decodeAsString(final byte[] bytes) throws DecodeException
    {
        // UTCTime is considered a "useful" type that is a specialisation of VisibleString
        // as such we should just return the "raw" string (if it is valid)
        // This is useful given that the decode to Timestamp discards timezone information.

        OperationResult<Timestamp, ImmutableSet<ByteValidationFailure>> result = validateAndDecode(
                bytes);
        if (!result.wasSuccessful())
        {
            DecodeException.throwIfHasFailures(result.getFailureReason()
                    .or(ImmutableSet.<ByteValidationFailure>of()));
        }

        // Now that we know it is valid, return the raw string.
        return AsnByteDecoder.decodeAsVisibleString(bytes);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates and decodes the UTCTime bytes. Method was added to avoid calling
     * parseDateTime multiple times.
     *
     * @param bytes
     *         bytes to be decoded.
     *
     * @return OperationResult that will contain a Timestamp if successful, or a
     * ByteValidationFailure otherwise
     */
    public static OperationResult<Timestamp, ImmutableSet<ByteValidationFailure>> validateAndDecode(
            final byte[] bytes)
    {
        // UTCTime is considered a "useful" type that is a specialisation of VisibleString
        // as such we should check it against VisibleString first.
        final ImmutableSet<ByteValidationFailure> byteValidationFailures
                = AsnByteValidator.validateAsVisibleString(bytes);

        if (!byteValidationFailures.isEmpty())
        {
            // there are failures, so bail early.
            return OperationResult.createUnsuccessfulInstance(null, byteValidationFailures);
        }

        try
        {
            final String rawDateTime = AsnByteDecoder.decodeAsVisibleString(bytes);

            // There are a few things that Joda-Time is not handling that we need to.

            // Joda doesn't seem to understand that "Z" is not "z" (it seems to be case insensitive)
            if (rawDateTime.endsWith("z"))
            {
                final String error = TimeValidator.UTCTIME_VALIDATION_ERROR + "Invalid format: \""
                        + rawDateTime + "\" is malformed at \"z\"";
                return OperationResult.createUnsuccessfulInstance(null,
                        ImmutableSet.of(new ByteValidationFailure(bytes.length,
                                FailureType.DataIncorrectlyFormatted,
                                error)));
            }

            // use the Joda-Time parser
            final DateTime dateTime = parser.parseDateTime(rawDateTime);
            Timestamp result = new Timestamp(dateTime.getMillis());
            return OperationResult.createSuccessfulInstance(result);
        }
        catch (IllegalArgumentException e)
        {
            final String error = TimeValidator.UTCTIME_VALIDATION_ERROR + e.getMessage();
            return OperationResult.createUnsuccessfulInstance(null,
                    ImmutableSet.of(new ByteValidationFailure(bytes.length,
                            FailureType.DataIncorrectlyFormatted,
                            error)));
        }
        catch (DecodeException e)
        {
            // In theory we should not get here because we explicitly validated the VisibleString
            // above.
            final String error = TimeValidator.UTCTIME_VALIDATION_ERROR + e.getMessage();
            return OperationResult.createUnsuccessfulInstance(null,
                    ImmutableSet.of(new ByteValidationFailure(bytes.length,
                            FailureType.DataIncorrectlyFormatted,
                            error)));
        }
    }
}
