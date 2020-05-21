/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Set;

/**
 * Validator for data of type {@link AsnBuiltinType#Oid}
 *
 * @author brightSPARK Labs
 */
public class OidValidator extends PrimitiveBuiltinTypeValidator {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static OidValidator instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance
     */
    private OidValidator() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static OidValidator getInstance() {
        if (instance == null) {
            instance = new OidValidator();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: PrimitiveBuiltinTypeValidator
    // -------------------------------------------------------------------------

    @Override
    protected ImmutableSet<ByteValidationFailure> validateNonNullBytes(final byte[] bytes) {
        final Set<ByteValidationFailure> failures = Sets.newHashSet();

        if (bytes.length > 0) {
            int firstByte = bytes[0] & 0xFF;
            if (firstByte > 0x7F) {
                final String error = OID_VALIDATION_ERROR + String.format("0x%02X ", firstByte);
                final ByteValidationFailure failure =
                        new ByteValidationFailure(0, FailureType.DataIncorrectlyFormatted, error);
                failures.add(failure);
            }

            // Check the MSB of the last bit. If this is set, the OID is incomplete.
            int i = bytes.length - 1;
            if (i >= 1) {
                byte b = bytes[i];
                if ((b & (byte) 0x80) != 0) {
                    final String error =
                            OID_VALIDATION_ERROR_INCOMPLETE + String.format("0x%02X ", b);
                    final ByteValidationFailure failure =
                            new ByteValidationFailure(
                                    i, FailureType.DataIncorrectlyFormatted, error);
                    failures.add(failure);
                }
            }
        } else {
            // Empty bytes array
            final String error = String.format(EMPTY_BYTE_ARRAY_VALIDATION_ERROR, "OID");
            final ByteValidationFailure failure =
                    new ByteValidationFailure(0, FailureType.DataMissing, error);
            failures.add(failure);
        }
        return ImmutableSet.copyOf(failures);
    }
}
