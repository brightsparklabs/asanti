/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.decoder.builtin.BuiltinTypeDecoder;
import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;

/**
 * Used to validate tags based on the kind of ASN.1 Built-in Type they came from.
 *
 * @author brightSPARK Labs
 */
public interface BuiltinTypeValidator {
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** validation error string for Bit String failures */
    String BIT_STRING_VALIDATION_ERROR =
            "Supplied bytes do not conform to the BIT STRING format. The first byte must be within the range 0x00 - 0x07. Supplied bytes contain a byte with invalid value: ";

    /** validation error string for GeneralizedTime failures */
    String GENERALIZEDTIME_VALIDATION_ERROR =
            "Supplied bytes do not conform to the GeneralizedTime format. Supplied bytes contain a byte with invalid value: ";

    /** validation error string for Bit String failures */
    String EMPTY_BYTE_ARRAY_VALIDATION_ERROR =
            "ASN.1 %s type must contain at least one byte. Supplied array contains 0 bytes";

    /** validation error string for IA5String failures */
    String IA5STRING_VALIDATION_ERROR =
            "Supplied bytes do not conform to the IA5String format. All bytes must be within the range 0x00 - 0x7f. Supplied bytes contain a byte with invalid value: ";

    /** validation error string for Null failures */
    String NULL_VALIDATION_ERROR = "Null type must be zero length.";

    /** validation error string for NumericString failures */
    String NUMERICSTRING_VALIDATION_ERROR =
            "Supplied bytes do not conform to the NumericString format. All bytes must be within the range '0' - '9' (0x30 - 0x39). Supplied bytes contain a byte with invalid value: ";

    /** validation error string for Oid failures */
    String OID_VALIDATION_ERROR =
            "Supplied bytes do not conform to the OID format. The first byte must be with the range 0x00 - 0x7F. Supplied bytes contain a byte with invalid value: ";

    String OID_VALIDATION_ERROR_INCOMPLETE =
            "Supplied bytes do not conform to the OID format. The OID encoding is incomplete: ";

    /** validation error string for PrintableString failures */
    String PRINTABLESTRING_VALIDATION_ERROR =
            "Supplied bytes do not conform to the PrintableString format. Supplied bytes contain a byte with invalid value: ";

    /** validation error string for UTCTime failures */
    String UTCTIME_VALIDATION_ERROR =
            "Supplied bytes do not conform to the UTCTime format. Supplied bytes contain a byte with invalid value: ";

    /** validation error string for VisibleString failures */
    String VISIBLESTRING_VALIDATION_ERROR =
            "Supplied bytes do not conform to the VisibleString format. All bytes must be within the range 0x20 - 0x7e. Supplied bytes contain invalid values: ";

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** null instance */
    BuiltinTypeValidator.Null NULL = new BuiltinTypeValidator.Null();

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates the supplied tag in the data based on the the kind of ASN.1 Built-in Type
     * represented by this validator
     *
     * @param tag tag to validate
     * @param asnData data to retrieve tag from
     * @return any failures encountered while validating the tag
     */
    ImmutableSet<DecodedTagValidationFailure> validate(String tag, AsantiAsnData asnData);

    /**
     * Validates the supplied bytes based on the the kind of ASN.1 Built-in Type represented by this
     * validator
     *
     * @param bytes bytes to validate
     * @return any failures encountered while validating the bytes
     */
    ImmutableSet<ByteValidationFailure> validate(byte[] bytes);

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Null
    // -------------------------------------------------------------------------

    /**
     * Null instance of {@link BuiltinTypeDecoder}.
     *
     * <p>The {@code validate} methods will return an empty set.
     *
     * @author brightSPARK Labs
     */
    class Null implements BuiltinTypeValidator {
        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private, use {@link BuiltinTypeValidator#NULL} to obtain an
         * instance
         */
        private Null() {}

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: BuiltinTypeValidator
        // ---------------------------------------------------------------------

        @Override
        public ImmutableSet<DecodedTagValidationFailure> validate(
                final String tag, final AsantiAsnData asnData) {
            return ImmutableSet.of();
        }

        @Override
        public ImmutableSet<ByteValidationFailure> validate(final byte[] bytes) {
            return ImmutableSet.of();
        }
    }
}
