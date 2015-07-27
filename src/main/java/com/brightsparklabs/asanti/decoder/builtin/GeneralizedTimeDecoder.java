/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.decoder.AsnByteDecoder;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.AsnByteValidator;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.builtin.GeneralizedTimeValidator;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.DateTimeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Decoder for data of type {@link AsnBuiltinType#GeneralizedTime}
 *
 * @author brightSPARK Labs
 */
public class GeneralizedTimeDecoder extends AbstractBuiltinTypeDecoder<Timestamp>
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static GeneralizedTimeDecoder instance;

    /** class logger */
    private static Logger logger = LoggerFactory.getLogger(GeneralizedTimeDecoder.class);

    private static final DateTimeFormatter core = new DateTimeFormatterBuilder().appendYear(4, 4)
            .appendMonthOfYear(2)
            .appendDayOfMonth(2)
            .appendHourOfDay(2)
            .toFormatter();

    private static final DateTimeParser comma = new DateTimeFormatterBuilder().appendLiteral(",")
            .toParser();

    private static final DateTimeParser dot = new DateTimeFormatterBuilder().appendLiteral(".")
            .toParser();

    private static final DateTimeParser[] decimal = { comma, dot };

    private static final DateTimeParser offset
            = new DateTimeFormatterBuilder().appendTimeZoneOffset("", "Z", false, 1, 2).toParser();

    private static final DateTimeFormatter uptoHours = new DateTimeFormatterBuilder().append(core)
            .appendOptional(new DateTimeFormatterBuilder().append(null, decimal)
                    .appendFractionOfHour(1, 18)
                    .toParser())
            .appendOptional(offset)
            .toFormatter();

    private static final DateTimeFormatter uptoMinutes = new DateTimeFormatterBuilder().append(core)
            .appendMinuteOfHour(2)
            .appendOptional(new DateTimeFormatterBuilder().append(null, decimal)
                    .appendFractionOfMinute(1, 18)
                    .toParser())
            .appendOptional(offset)
            .toFormatter();

    private static final DateTimeFormatter uptoSeconds = new DateTimeFormatterBuilder().append(core)
            .appendMinuteOfHour(2)
            .appendSecondOfMinute(2)
            .appendOptional(new DateTimeFormatterBuilder().append(null, decimal)
                    .appendFractionOfSecond(1, 18)
                    .toParser())
            .appendOptional(offset)
            .toFormatter();

    private static final DateTimeParser[] options = { uptoSeconds.getParser(),
                                                      uptoMinutes.getParser(),
                                                      uptoHours.getParser() };

    private static final DateTimeFormatter parser = new DateTimeFormatterBuilder().append(null,
            options).toFormatter();

    private static final Pattern PATTERN_SUB_MILLI_SECONDS = Pattern.compile(
            "^([0-9]{10,14})(([,\\.])(([0-9]{1,3})([0-9]*)))(Z|((\\+|\\-)[0-9]+))?");

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private GeneralizedTimeDecoder() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static GeneralizedTimeDecoder getInstance()
    {
        if (instance == null)
        {
            instance = new GeneralizedTimeDecoder();
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
        // GeneralizedTime is considered a "useful" type that is a specialisation of VisibleString
        // as such we should just return the "raw" string (if it is valid)

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
     * Validates and decodes the GeneralizedTime bytes. Method was added to avoid calling
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

        // GeneralizedTime is considered a "useful" type that is a specialisation of VisibleString
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

            // There are a few things that Joda is not helping us with.

            // It can't handle less than milliseconds, and we need to go to nano
            final Matcher matcher = PATTERN_SUB_MILLI_SECONDS.matcher(rawDateTime);
            String replacement = rawDateTime;
            int nanos = 0;
            boolean setNanos = false;
            if (matcher.matches())
            {
                final String subMilliSeconds = Strings.nullToEmpty(matcher.group(6));
                final String milliSeconds = Strings.nullToEmpty(matcher.group(5));

                if (!subMilliSeconds.isEmpty())
                {
                    // We can only handle up to 6 digits (giving 9 total, ie down to Nano seconds)
                    final String trimmedSubMilliSeconds = subMilliSeconds.substring(0,
                            Math.min(subMilliSeconds.length(), 6));
                    BigDecimal bd = new BigDecimal("0." + milliSeconds + trimmedSubMilliSeconds);
                    bd = bd.multiply(BigDecimal.valueOf(1000000000L));
                    nanos = bd.intValue();
                    setNanos = true;

                    // Joda only has milli second precision.  It can parse up to 18 decimals, but
                    // it will discard everything after 3 (in the fractionsOfSeconds case).
                    // By replacing here we are protecting against more than 18 decimal places
                    // which would cause Joda to throw an exception
                    replacement = matcher.replaceAll("$1$3$5$7");
                }
            }

            // it doesn't seem to understand that "Z" is not "z" (it seems to be case insensitive)
            // fake it as though it is.  Note we throw
            if (replacement.endsWith("z"))
            {
                final String error = GeneralizedTimeValidator.GENERALIZEDTIME_VALIDATION_ERROR
                        + "Invalid format: \"" + rawDateTime + "\" is malformed at \"z\"";
                return OperationResult.createUnsuccessfulInstance(null,
                        ImmutableSet.of(new ByteValidationFailure(bytes.length,
                                FailureType.DataIncorrectlyFormatted,
                                error)));
            }

            DateTime dateTime = parser.parseDateTime(replacement);

            DateTime dt = dateTime.withZone(DateTimeZone.UTC);
            Timestamp result = new Timestamp(dt.getMillis());
            if (setNanos)
            {
                result.setNanos(nanos);
            }
            return OperationResult.createSuccessfulInstance(result);
        }
        catch (IllegalArgumentException e)
        {
            final String error = GeneralizedTimeValidator.GENERALIZEDTIME_VALIDATION_ERROR
                    + e.getMessage();
            return OperationResult.createUnsuccessfulInstance(null,
                    ImmutableSet.of(new ByteValidationFailure(bytes.length,
                            FailureType.DataIncorrectlyFormatted,
                            error)));
        }
        catch (DecodeException e)
        {
            final String error = GeneralizedTimeValidator.VISIBLESTRING_VALIDATION_ERROR
                    + e.getMessage();
            return OperationResult.createUnsuccessfulInstance(null,
                    ImmutableSet.of(new ByteValidationFailure(bytes.length,
                            FailureType.DataIncorrectlyFormatted,
                            error)));
        }
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: OperationResult
    // -------------------------------------------------------------------------

    public static class OperationResult<T, FailureType>
    {
        // -------------------------------------------------------------------------
        // INSTANCE VARIABLES
        // -------------------------------------------------------------------------

        /** whether the operation was successful */
        private final boolean wasSuccessful;

        /** the resulting output from the operation */
        private final T output;

        /** the reason the operation failed (or an empty string if it did not fail) */
        private final Optional<FailureType> failureReason;

        // -------------------------------------------------------------------------
        // CONSTRUCTION
        // -------------------------------------------------------------------------

        /**
         * Default constructor
         *
         * @param wasSuccessful
         *         {@code true} if the operation was successful
         * @param output
         *         the resulting output from the operation
         * @param failureReason
         *         the reason the operation failed (or an empty string if it did not fail)
         */
        private OperationResult(boolean wasSuccessful, T output, FailureType failureReason)
        {
            this.wasSuccessful = wasSuccessful;
            this.output = output;
            this.failureReason = Optional.fromNullable(failureReason);
        }

        /**
         * Convenience method to create a result indicating the operation was successful. Results
         * can be created via:<br> {@code OperationResult<String> result =
         * createSuccessfulInstance(outputString);}
         *
         * <p>Which is more concise than:<br> {@code OperationResult<String> result = new
         * OperationResult<String>(true, outputString, "");}
         *
         * @param output
         *         the resulting output from the operation
         * @param <T>
         *         the type of the result
         *
         * @return a 'successful' result instance containing the supplied data
         */
        public static <T, FailureType> OperationResult<T, FailureType> createSuccessfulInstance(
                T output)
        {
            return new OperationResult<T, FailureType>(true, output, null);
        }

        /**
         * Convenience method to create a result indicating the operation was unsuccessful. Results
         * can be created via:<br> {@code OperationResult<String> result =
         * createUnsuccessfulInstance(outputString);}
         *
         * <p>Which is more concise than:<br> {@code OperationResult<String> result = new
         * OperationResult<String>(false, outputString, reason);}
         *
         * @param output
         *         the resulting output from the operation
         * @param failureReason
         *         the reason the operation failed
         * @param <T>
         *         the type of the result
         *
         * @return an 'unsuccessful' result instance containing the supplied data
         */
        public static <T, FailureType> OperationResult<T, FailureType> createUnsuccessfulInstance(
                T output, FailureType failureReason)
        {
            return new OperationResult<T, FailureType>(false, output, failureReason);
        }

        // -------------------------------------------------------------------------
        // PUBLIC METHODS
        // -------------------------------------------------------------------------

        /**
         * Returns {@code true} if the operation was successful
         *
         * @return {@code true} if the operation was successful
         */
        public boolean wasSuccessful()
        {
            return wasSuccessful;
        }

        /**
         * Returns the resulting output from the operation
         *
         * @return the resulting output from the operation
         */
        public T getOutput()
        {
            return output;
        }

        /**
         * Returns the reason the operation failed
         *
         * @return the reason the operation failed (or an empty string if it did not fail)
         */
        public Optional<FailureType> getFailureReason()
        {
            return failureReason;
        }
    }
}
