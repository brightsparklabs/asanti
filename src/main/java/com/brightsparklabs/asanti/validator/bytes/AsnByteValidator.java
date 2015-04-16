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
import com.brightsparklabs.asanti.validator.result.ByteValidationResult;
import com.brightsparklabs.asanti.validator.result.DecodedAsnDataValidationResultImpl;

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
    static final ByteValidationFailure FAILURE_MISSING_DATA = new ByteValidationFailure(-1,
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
     * Decodes the supplied bytes as an {@link AsnBuiltinType#BitString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsBitString(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#BmpString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsBmpString(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Boolean}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsBoolean(byte[] bytes)
    {
        final ByteValidationResult.Builder builder = AsnByteValidator.validationResultBuilderFor(
                bytes);
        if (builder.containsResults())
        {
            // bytes were null, do not continue validating
            // TODO: ASN-92
            return null;
        }

        if (bytes.length != 1)
        {
            final String error = String.format(
                    "ASN.1 BOOLEAN type can only contain one byte. Supplied array contains %d bytes",
                    bytes.length);
            builder.add(bytes.length, FailureType.DataIncorrectlyFormatted, error);
        }
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#CharacterString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    public static ByteValidationResult validateAsCharacterString(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Date}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsDate(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#DateTime}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsDateTime(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Duration}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsDuration(byte[] bytes)
    {
        // TODO: ASN-105
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#EmbeddedPDV}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsEmbeddedPDV(byte[] bytes)
    {
        // TODO: ASN-105
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#External}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsExternal(byte[] bytes)
    {
        // TODO: ASN-105
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#GeneralizedTime}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsGeneralizedTime(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#GeneralString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsGeneralString(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#GraphicString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsGraphicString(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Ia5String}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsIa5String(byte[] bytes)
    {
        // TODO: ASN-92
        final ByteValidationResult.Builder builder = AsnByteValidator.validationResultBuilderFor(
                bytes);
        if (builder.containsResults())
        {
            // bytes were null, do not continue validating
            return builder.build();
        }

        for (int i = 0; i < bytes.length; i++)
        {
            byte b = bytes[i];
            if (b < 0 || b > 127)
            {
                final String error =
                        "Supplied bytes do not conform to the IA5String format. All bytes must be within the range 0 - 127. Supplied bytes contain a byte with value: "
                                + b;
                builder.add(i, FailureType.DataIncorrectlyFormatted, error);
            }
        }
        return builder.build();
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#InstanceOf}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsInstanceOf(byte[] bytes)
    {
        // TODO: ASN-105
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Integer}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsInteger(byte[] bytes)
    {
        // TODO: ASN-105
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Iri}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsIri(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Iso646String}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsIso646String(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Null}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsNull(byte[] bytes)
    {
        // TODO: ASN-105
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#NumericString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsNumericString(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#ObjectClassField}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsObjectClassField(byte[] bytes)
    {
        // TODO: ASN-105
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#OctetString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsOctetString(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Oid}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsOid(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#OidIri}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsOidIri(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Prefixed}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsPrefixed(byte[] bytes)
    {
        // TODO: ASN-105
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#PrintableString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsPrintableString(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Real}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsReal(byte[] bytes)
    {
        // TODO: ASN-105
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeIri}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsRelativeIri(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeOid}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsRelativeOid(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeOidIri}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsRelativeOidIri(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#TeletexString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsTeletexString(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Time}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsTime(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#TimeOfDay}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsTimeOfDay(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#UniversalString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsUniversalString(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Utf8String}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsUtf8String(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#VideotexString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsVideotexString(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#VisibleString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ByteValidationResult validateAsVisibleString(byte[] bytes)
    {
        // TODO: ASN-92
        return null;
    }

    // -------------------------------------------------------------------------
    // PACKAGE METHODS
    // -------------------------------------------------------------------------

    /**
     * Creates a {@link DecodedAsnDataValidationResultImpl.Builder} for storing the results of
     * validating the supplied bytes. If the bytes are {@code null}, the builder will contain a null
     * validation failure. Otherwise it will be empty.
     *
     * @param bytes
     *         bytes to create builder for
     *
     * @return a {@link DecodedAsnDataValidationResultImpl.Builder} for storing the results of
     * validating the supplied bytes
     */
    static ByteValidationResult.Builder validationResultBuilderFor(byte[] bytes)
    {
        final ByteValidationResult.Builder builder = ByteValidationResult.builder();
        if (bytes == null)
        {
            builder.add(AsnByteValidator.FAILURE_MISSING_DATA);
        }
        // TODO: ASN-92
        return null;
    }
}
