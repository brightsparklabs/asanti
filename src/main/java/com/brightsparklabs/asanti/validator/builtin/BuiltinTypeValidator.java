/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.decoder.builtin.BuiltinTypeDecoder;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;

/**
 * Used to validate tags based on the kind of ASN.1 Built-in Type they came from.
 *
 * @author brightSPARK Labs
 */
public interface BuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** validation error string for Bit String failures */
    public static final String BIT_STRING_VALIDATION_ERROR
            = "Supplied bytes do not conform to the BIT STRING format. The first byte must be within the range 0x00 - 0x07. Supplied bytes contain a byte with invalid value: ";

    /** validation error string for Bit String failures */
    public static final String EMPTY_BYTE_ARRAY_VALIDATION_ERROR
            = "ASN.1 %s type must contain at least one byte. Supplied array contains 0 bytes";

    /** validation error string for IA5String failures */
    public static final String IA5STRING_VALIDATION_ERROR
            = "Supplied bytes do not conform to the IA5String format. All bytes must be within the range 0x00 - 0x7f. Supplied bytes contain a byte with invalid value: ";

    /** validation error string for Null failures */
    public static final String NULL_VALIDATION_ERROR = "Null type must be zero length.";

    /** validation error string for NumericString failures */
    public static final String NUMERICSTRING_VALIDATION_ERROR
            = "Supplied bytes do not conform to the NumericString format. All bytes must be within the range '0' - '9' (0x30 - 0x39). Supplied bytes contain a byte with invalid value: ";

    /** validation error string for Oid failures */
    public static final String OID_VALIDATION_ERROR
            = "Supplied bytes do not conform to the OID format. The first byte must be with the range 0x00 - 0x7F. Supplied bytes contain a byte with invalid value: ";

    public static final String OID_VALIDATION_ERROR_INCOMPLETE
            = "Supplied bytes do not conform to the OID format. The OID encoding is incomplete: ";

    /** validation error string for PrintableString failures */
    public static final String PRINTABLESTRING_VALIDATION_ERROR
            = "Supplied bytes do not conform to the PrintableString format. Supplied bytes contain a byte with invalid value: ";

    /** validation error string for VisibleString failures */
    public static final String VISIBLESTRING_VALIDATION_ERROR
            = "Supplied bytes do not conform to the VisibleString format. All bytes must be within the range 0x20 - 0x7e. Supplied bytes contain invalid values: ";

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** null instance */
    public static final BuiltinTypeValidator.Null NULL = new BuiltinTypeValidator.Null();

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Validates the supplied tag in the data based on the the kind of ASN.1 Built-in Type
     * represented by this validator
     *
     * @param tag
     *         tag to validate
     * @param decodedAsnData
     *         data to retrieve tag from
     *
     * @return any failures encountered while validating the tag
     */
    public ImmutableSet<DecodedTagValidationFailure> validate(String tag,
            DecodedAsnData decodedAsnData);

    /**
     * Validates the supplied bytes based on the the kind of ASN.1 Built-in Type represented by this
     * validator
     *
     * @param bytes
     *         bytes to validate
     *
     * @return any failures encountered while validating the bytes
     */
    public ImmutableSet<ByteValidationFailure> validate(byte[] bytes);

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Null
    // -------------------------------------------------------------------------

    /**
     * Null instance of {@link BuiltinTypeDecoder}.
     *
     * <p>The {@code validate} methods will return an empty set.</p>
     *
     * @author brightSPARK Labs
     */
    public static class Null implements BuiltinTypeValidator
    {
        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private, use {@link BuiltinTypeValidator#NULL} to obtain an
         * instance
         */
        private Null()
        {
        }

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: BuiltinTypeValidator
        // ---------------------------------------------------------------------

        @Override
        public ImmutableSet<DecodedTagValidationFailure> validate(final String tag,
                final DecodedAsnData decodedAsnData)
        {
            return ImmutableSet.of();
        }

        @Override
        public ImmutableSet<ByteValidationFailure> validate(final byte[] bytes)
        {
            return ImmutableSet.of();
        }
    }
}
