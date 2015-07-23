/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.builtin.GeneralizedTimeValidator;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.Set;

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

    public static Set<ByteValidationFailure> failures;

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
        Timestamp timestamp = validateAndDecode(bytes);
        if (timestamp == null)
        {
            DecodeException.throwIfHasFailures(failures);
        }
        return timestamp;
    }

    /**
     * Validates and decodes the GeneralizedTime bytes. Method was added to avoid calling
     * parseDateTime multiple times.
     *
     * @param bytes
     *         bytes to be decoded.
     *
     * @return Timestamp
     */
    public static Timestamp validateAndDecode(final byte[] bytes)
    {
        DateTime parsedDateTime = null;
        failures = Sets.newHashSet();

        String strBytes = new String(bytes, Charsets.UTF_8);

        //. GeneralizedTime has a minimum 14 characters.
        if (strBytes.length() >= 14)
        {
            if (strBytes.charAt(strBytes.length() - 1) == 'Z')
            {
                // Presence of 'Z' character indicates a universal time.
                try
                {
                    parsedDateTime = GeneralizedTimeValidator.universalTimeFormatter.parseDateTime(
                            strBytes);
                    System.out.println();
                }
                catch (IllegalArgumentException e)
                {
                    final String error = GeneralizedTimeValidator.GENERALIZEDTIME_VALIDATION_ERROR
                            + e.getMessage();
                    final ByteValidationFailure failure = new ByteValidationFailure(bytes.length,
                            FailureType.DataIncorrectlyFormatted,
                            error);
                    failures.add(failure);
                }
            }
            else if (strBytes.charAt(strBytes.length() - 5) == '+'
                    || strBytes.charAt(strBytes.length() - 5) == '-')
            {
                // Presence of '+' or '-' character indicates a zoned time.
                try
                {
                    parsedDateTime = GeneralizedTimeValidator.zonedTimeFormatter.parseDateTime(
                            strBytes);
                    System.out.println();

                }
                catch (IllegalArgumentException e)
                {
                    final String error = GeneralizedTimeValidator.GENERALIZEDTIME_VALIDATION_ERROR
                            + e.getMessage();
                    final ByteValidationFailure failure = new ByteValidationFailure(bytes.length,
                            FailureType.DataIncorrectlyFormatted,
                            error);
                    failures.add(failure);
                }
            }
            else
            {
                try
                {
                    parsedDateTime = GeneralizedTimeValidator.localTimeFormatter.parseDateTime(
                            strBytes);
                    System.out.println();
                }
                catch (IllegalArgumentException e)
                {
                    final String error = GeneralizedTimeValidator.GENERALIZEDTIME_VALIDATION_ERROR
                            + e.getMessage();
                    final ByteValidationFailure failure = new ByteValidationFailure(bytes.length,
                            FailureType.DataIncorrectlyFormatted,
                            error);
                    failures.add(failure);
                }
            }
        }
        else
        {
            final String error = GeneralizedTimeValidator.GENERALIZEDTIME_ERROR_INCOMPLETE;
            final ByteValidationFailure failure = new ByteValidationFailure(bytes.length,
                    FailureType.DataIncorrectlyFormatted,
                    error);
            failures.add(failure);
        }

        if (parsedDateTime != null)
        {
            return new Timestamp(parsedDateTime.getMillis());
        }

        return null;

    }
}
