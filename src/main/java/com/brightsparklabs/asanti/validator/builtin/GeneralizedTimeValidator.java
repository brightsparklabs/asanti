/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.decoder.builtin.GeneralizedTimeDecoder;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.sql.Timestamp;
import java.util.Set;

/**
 * Validator for data of type {@link AsnBuiltinType#GeneralizedTime}
 *
 * @author brightSPARK Labs
 */
public class GeneralizedTimeValidator extends PrimitiveBuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static GeneralizedTimeValidator instance;

    /** Formatter for universal GeneralizedTime strings, e.g. YYYYMMDDHH[MM[SS[.fff]]]Z */
    public static DateTimeFormatter universalTimeFormatter
            = new DateTimeFormatterBuilder().appendYear(4, 4)
            .appendMonthOfYear(2)
            .appendDayOfMonth(2)
            .appendHourOfDay(2)
            .appendMinuteOfHour(2)
            .appendSecondOfMinute(2)
            .appendLiteral('.')
            .appendFractionOfSecond(0, 18)
            .appendLiteral('Z')
            .toFormatter();

    /** Formatter for local GeneralizedTime strings, e.g. YYYYMMDDHH[MM[SS[.fff]]] */
    public static DateTimeFormatter localTimeFormatter
            = new DateTimeFormatterBuilder().appendYear(4, 4)
            .appendMonthOfYear(2)
            .appendDayOfMonth(2)
            .appendHourOfDay(2)
            .appendMinuteOfHour(2)
            .appendSecondOfMinute(2)
            .appendLiteral('.')
            .appendFractionOfSecond(0, 18)
            .toFormatter();

    /** Formatter for zoned GeneralizedTime strings, e.g. YYYYMMDDHH[MM[SS[.fff]]]+-HHMM */
    public static DateTimeFormatter zonedTimeFormatter
            = new DateTimeFormatterBuilder().appendYear(4, 4)
            .appendMonthOfYear(2)
            .appendDayOfMonth(2)
            .appendHourOfDay(2)
            .appendMinuteOfHour(2)
            .appendSecondOfMinute(2)
            .appendLiteral('.')
            .appendFractionOfSecond(0, 18)
            .appendPattern("Z")
            .toFormatter();
    //            .withOffsetParsed();

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private GeneralizedTimeValidator() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static GeneralizedTimeValidator getInstance()
    {
        if (instance == null)
        {
            instance = new GeneralizedTimeValidator();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: PrimitiveBuiltinTypeValidator
    // -------------------------------------------------------------------------

    @Override
    protected ImmutableSet<ByteValidationFailure> validateNonNullBytes(final byte[] bytes)
    {
        final Set<ByteValidationFailure> failures = Sets.newHashSet();
        Timestamp timestamp = GeneralizedTimeDecoder.validateAndDecode(bytes);

        if (timestamp == null)
        {
            failures.addAll(GeneralizedTimeDecoder.failures);
        }

        return ImmutableSet.copyOf(failures);
    }
}
