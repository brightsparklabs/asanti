/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.bytes;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Utility class for validating bytes in ASN.1 Types
 *
 * @author brightSPARK Labs
 */
public class AsnByteValidator
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** failure indicating that data was missing */
    private static final ByteValidationFailure FAILURE_MISSING_DATA = new ByteValidationFailure(0,
            FailureType.DataMissing,
            "No data present");

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     */
    private AsnByteValidator()
    {
        // constructor should never be called for static class
        assert false;
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /*
     * NOTE: Please keep these methods ordered alphabetically for simplicity
     */

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#BitString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsBitString(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#BmpString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsBmpString(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Boolean}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsBoolean(byte[] bytes)
    {
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        if (!failures.isEmpty())
        {
            // bytes were null, do not continue validating
            return ImmutableSet.copyOf(failures);
        }

        if (bytes.length != 1)
        {
            final String error = String.format(
                    "ASN.1 BOOLEAN type can only contain one byte. Supplied array contains %d bytes",
                    bytes.length);
            final ByteValidationFailure failure = new ByteValidationFailure(bytes.length,
                    FailureType.DataIncorrectlyFormatted,
                    error);
            failures.add(failure);
        }
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#CharacterString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static ImmutableSet<ByteValidationFailure> validateAsCharacterString(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Date}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsDate(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#DateTime}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsDateTime(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Duration}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsDuration(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#EmbeddedPDV}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsEmbeddedPDV(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#External}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsExternal(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#GeneralizedTime}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsGeneralizedTime(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#GeneralString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsGeneralString(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#GraphicString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsGraphicString(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Ia5String}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsIa5String(byte[] bytes)
    {
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        if (!failures.isEmpty())
        {
            // bytes were null, do not continue validating
            return ImmutableSet.copyOf(failures);
        }

        for (int i = 0; i < bytes.length; i++)
        {
            byte b = bytes[i];
            if (b < 0 || b > 127)
            {
                final String error =
                        "Supplied bytes do not conform to the IA5String format. All bytes must be within the range 0 - 127. Supplied bytes contain a byte with value: "
                                + b;
                final ByteValidationFailure failure = new ByteValidationFailure(i,
                        FailureType.DataIncorrectlyFormatted,
                        error);
                failures.add(failure);
            }
        }
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#InstanceOf}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsInstanceOf(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Integer}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsInteger(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Iri}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsIri(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Iso646String}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsIso646String(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Null}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsNull(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#NumericString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsNumericString(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#ObjectClassField}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsObjectClassField(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#OctetString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsOctetString(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Oid}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsOid(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#OidIri}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsOidIri(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Prefixed}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsPrefixed(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#PrintableString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsPrintableString(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Real}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsReal(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#RelativeIri}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsRelativeIri(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#RelativeOid}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsRelativeOid(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#RelativeOidIri}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsRelativeOidIri(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#TeletexString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsTeletexString(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Time}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsTime(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#TimeOfDay}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsTimeOfDay(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#UniversalString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsUniversalString(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Utf8String}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsUtf8String(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#VideotexString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsVideotexString(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#VisibleString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsVisibleString(byte[] bytes)
    {
        // TODO: ASN-105
        final Set<ByteValidationFailure> failures = validateNotNull(bytes);
        return ImmutableSet.copyOf(failures);
    }

    // -------------------------------------------------------------------------
    // PACKAGE METHODS
    // -------------------------------------------------------------------------

    /**
     * Checks if the supplied byte array is {@code null}.
     *
     * @param bytes
     *         bytes to check
     *
     * @return a set containing a failure if the bytes were {@code null}. An empty set otherwise.
     */
    private static Set<ByteValidationFailure> validateNotNull(byte[] bytes)
    {
        final Set<ByteValidationFailure> failures = Sets.newHashSet();
        if (bytes == null)
        {
            failures.add(AsnByteValidator.FAILURE_MISSING_DATA);
        }
        return failures;
    }
}
