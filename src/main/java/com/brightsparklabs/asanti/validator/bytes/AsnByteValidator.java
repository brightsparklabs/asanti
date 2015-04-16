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
import com.brightsparklabs.asanti.validator.*;

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
    static final ValidationFailure FAILURE_MISSING_DATA = new ValidationFailureImpl(
            "Byte at index [0]",
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
    public static ValidationResult validateAsBitString(byte[] bytes)
    {
        return AsnStringByteValidator.validateAsBitString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#BmpString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsBmpString(byte[] bytes)
    {
        return AsnStringByteValidator.validateAsBmpString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Boolean}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsBoolean(byte[] bytes)
    {
        final ValidationResultImpl.Builder builder = AsnByteValidator.validationResultBuilderFor(
                bytes);
        if (builder.containsResults())
        {
            // bytes were null, do not continue validating
            return builder.build();
        }

        if (bytes.length != 1)
        {
            final String error = String.format(
                    "ASN.1 BOOLEAN type can only contain one byte. Supplied array contains %d bytes",
                    bytes.length);
            builder.add("Byte at index [1]", FailureType.DataIncorrectlyFormatted, error);
        }
        return builder.build();
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
    public static ValidationResult validateAsCharacterString(byte[] bytes)
    {
        return AsnStringByteValidator.validateAsCharacterString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Date}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsDate(byte[] bytes)
    {
        return AsnTimestampByteValidator.validateAsDate(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#DateTime}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsDateTime(byte[] bytes)
    {
        return AsnTimestampByteValidator.validateAsDateTime(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Duration}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsDuration(byte[] bytes)
    {
        // TODO: ASN-105
        return ValidationResultImpl.builder().build();
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#EmbeddedPDV}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsEmbeddedPDV(byte[] bytes)
    {
        // TODO: ASN-105
        return ValidationResultImpl.builder().build();
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#External}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsExternal(byte[] bytes)
    {
        // TODO: ASN-105
        return ValidationResultImpl.builder().build();
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#GeneralizedTime}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsGeneralizedTime(byte[] bytes)
    {
        return AsnTimestampByteValidator.validateAsGeneralizedTime(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#GeneralString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsGeneralString(byte[] bytes)
    {
        return AsnStringByteValidator.validateAsGeneralString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#GraphicString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsGraphicString(byte[] bytes)
    {
        return AsnStringByteValidator.validateAsGraphicString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Ia5String}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsIa5String(byte[] bytes)
    {
        return AsnStringByteValidator.validateAsIa5String(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#InstanceOf}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsInstanceOf(byte[] bytes)
    {
        // TODO: ASN-105
        return ValidationResultImpl.builder().build();
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Integer}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsInteger(byte[] bytes)
    {
        // TODO: ASN-105
        return ValidationResultImpl.builder().build();
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#IRI}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsIri(byte[] bytes)
    {
        return AsnIdentifierByteValidator.validateAsIri(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Iso646String}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsIso646String(byte[] bytes)
    {
        return AsnStringByteValidator.validateAsIso646String(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Null}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsNull(byte[] bytes)
    {
        // TODO: ASN-105
        return ValidationResultImpl.builder().build();
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#NumericString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsNumericString(byte[] bytes)
    {
        return AsnStringByteValidator.validateAsNumericString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#ObjectClassField}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsObjectClassField(byte[] bytes)
    {
        // TODO: ASN-105
        return ValidationResultImpl.builder().build();
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#OctetString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsOctetString(byte[] bytes)
    {
        return AsnStringByteValidator.validateAsOctetString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Oid}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsOid(byte[] bytes)
    {
        return AsnIdentifierByteValidator.validateAsOid(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#OidIri}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsOidIri(byte[] bytes)
    {
        return AsnIdentifierByteValidator.validateAsOidIri(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Prefixed}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsPrefixed(byte[] bytes)
    {
        // TODO: ASN-105
        return ValidationResultImpl.builder().build();
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#PrintableString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsPrintableString(byte[] bytes)
    {
        return AsnStringByteValidator.validateAsPrintableString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Real}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsReal(byte[] bytes)
    {
        // TODO: ASN-105
        return ValidationResultImpl.builder().build();
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeIri}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsRelativeIri(byte[] bytes)
    {
        return AsnIdentifierByteValidator.validateAsRelativeIri(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeOid}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsRelativeOid(byte[] bytes)
    {
        return AsnIdentifierByteValidator.validateAsRelativeOid(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeOidIri}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsRelativeOidIri(byte[] bytes)
    {
        return AsnIdentifierByteValidator.validateAsRelativeOidIri(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#TeletexString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsTeletexString(byte[] bytes)
    {
        return AsnStringByteValidator.validateAsTeletexString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Time}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsTime(byte[] bytes)
    {
        return AsnTimestampByteValidator.validateAsTime(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#TimeOfDay}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsTimeOfDay(byte[] bytes)
    {
        return AsnTimestampByteValidator.validateAsTimeOfDay(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#UniversalString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsUniversalString(byte[] bytes)
    {
        return AsnStringByteValidator.validateAsUniversalString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Utf8String}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsUtf8String(byte[] bytes)
    {
        return AsnStringByteValidator.validateAsUtf8String(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#VideotexString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsVideotexString(byte[] bytes)
    {
        return AsnStringByteValidator.validateAsVideotexString(bytes);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#VisibleString}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    public static ValidationResult validateAsVisibleString(byte[] bytes)
    {
        return AsnStringByteValidator.validateAsVisibleString(bytes);
    }

    // -------------------------------------------------------------------------
    //  PACKAGE METHODS
    // -------------------------------------------------------------------------

    /**
     * Creates a {@link ValidationResultImpl.Builder} for storing the results of validating the
     * supplied bytes. If the bytes are {@code null}, the builder will contain a null validation
     * failure. Otherwise it will be empty.
     *
     * @param bytes
     *         bytes to create builder for
     *
     * @return a {@link ValidationResultImpl.Builder} for storing the results of validating the
     * supplied bytes
     */
    static ValidationResultImpl.Builder validationResultBuilderFor(byte[] bytes)
    {
        final ValidationResultImpl.Builder builder = ValidationResultImpl.builder();
        if (bytes == null)
        {
            builder.add(AsnByteValidator.FAILURE_MISSING_DATA);
        }
        return builder;
    }
}
