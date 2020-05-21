/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.builtin.*;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;

/**
 * Utility class for validating bytes in ASN.1 Types
 *
 * @author brightSPARK Labs
 */
public class AsnByteValidator {
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /** Default constructor */
    private AsnByteValidator() {
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
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsBitString(byte[] bytes) {
        return BitStringValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#BmpString}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsBmpString(byte[] bytes) {
        return BmpStringValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Boolean}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsBoolean(byte[] bytes) {
        return BooleanValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#CharacterString}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     * @throws NullPointerException if parameters are {@code null}
     */
    public static ImmutableSet<ByteValidationFailure> validateAsCharacterString(byte[] bytes) {
        return CharacterStringValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Date}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsDate(byte[] bytes) {
        return DateValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#DateTime}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsDateTime(byte[] bytes) {
        return DateTimeValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Duration}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsDuration(byte[] bytes) {
        return DurationValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#EmbeddedPDV}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsEmbeddedPDV(byte[] bytes) {
        return EmbeddedPDVValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Enumerated}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsEnumerated(byte[] bytes) {
        return EnumeratedValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#External}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsExternal(byte[] bytes) {
        return ExternalValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#GeneralizedTime}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsGeneralizedTime(byte[] bytes) {
        return GeneralizedTimeValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#GeneralString}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsGeneralString(byte[] bytes) {
        return GeneralStringValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#GraphicString}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsGraphicString(byte[] bytes) {
        return GraphicStringValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Ia5String}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsIa5String(byte[] bytes) {
        return Ia5StringValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#InstanceOf}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsInstanceOf(byte[] bytes) {
        return InstanceOfValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Integer}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsInteger(byte[] bytes) {
        return IntegerValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Iri}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsIri(byte[] bytes) {
        return IriValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Iso646String}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsIso646String(byte[] bytes) {
        return Iso646StringValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Null}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsNull(byte[] bytes) {
        return NullValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#NumericString}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsNumericString(byte[] bytes) {
        return NumericStringValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#ObjectClassField}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsObjectClassField(byte[] bytes) {
        return ObjectClassFieldValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#OctetString}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsOctetString(byte[] bytes) {
        return OctetStringValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Oid}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsOid(byte[] bytes) {
        return OidValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#OidIri}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsOidIri(byte[] bytes) {
        return OidIriValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Prefixed}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsPrefixed(byte[] bytes) {
        return PrefixedValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#PrintableString}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsPrintableString(byte[] bytes) {
        return PrintableStringValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Real}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsReal(byte[] bytes) {
        return RealValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#RelativeIri}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsRelativeIri(byte[] bytes) {
        return RelativeIriValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#RelativeOid}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsRelativeOid(byte[] bytes) {
        return RelativeOidValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#RelativeOidIri}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsRelativeOidIri(byte[] bytes) {
        return RelativeOidIriValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#TeletexString}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsTeletexString(byte[] bytes) {
        return TeletexStringValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Time}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsTime(byte[] bytes) {
        return TimeValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#TimeOfDay}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsTimeOfDay(byte[] bytes) {
        return TimeOfDayValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#UniversalString}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsUniversalString(byte[] bytes) {
        return UniversalStringValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#UtcTime}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsUtcTime(byte[] bytes) {
        return UtcTimeValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Utf8String}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsUtf8String(byte[] bytes) {
        return Utf8StringValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#VideotexString}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsVideotexString(byte[] bytes) {
        return VideotexStringValidator.getInstance().validate(bytes);
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#VisibleString}
     *
     * @param bytes bytes to validate
     * @return any failures which occurred while validating the supplied byte
     */
    public static ImmutableSet<ByteValidationFailure> validateAsVisibleString(byte[] bytes) {
        return VisibleStringValidator.getInstance().validate(bytes);
    }
}
