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
 * Validator for data of type {@link AsnBuiltinType#Ia5String}
 *
 * @author brightSPARK Labs
 */
public class Ia5StringValidator extends PrimitiveBuiltinTypeValidator {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static Ia5StringValidator instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance
     */
    private Ia5StringValidator() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static Ia5StringValidator getInstance() {
        if (instance == null) {
            instance = new Ia5StringValidator();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: PrimitiveBuiltinTypeValidator
    // -------------------------------------------------------------------------

    @Override
    protected ImmutableSet<ByteValidationFailure> validateNonNullBytes(final byte[] bytes) {
        final Set<ByteValidationFailure> failures = Sets.newHashSet();
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            if (b < 0) {
                final String error = IA5STRING_VALIDATION_ERROR + String.format("0x%02X ", b);
                final ByteValidationFailure failure =
                        new ByteValidationFailure(i, FailureType.DataIncorrectlyFormatted, error);
                failures.add(failure);
            }
        }
        return ImmutableSet.copyOf(failures);
    }
}
