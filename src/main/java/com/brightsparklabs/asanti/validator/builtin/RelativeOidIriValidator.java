/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Set;

/**
 * Validator for data of type {@link AsnBuiltinType#RelativeOidIri}
 *
 * @author brightSPARK Labs
 */
public class RelativeOidIriValidator extends PrimitiveBuiltinTypeValidator {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static RelativeOidIriValidator instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance
     */
    private RelativeOidIriValidator() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static RelativeOidIriValidator getInstance() {
        if (instance == null) {
            instance = new RelativeOidIriValidator();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: PrimitiveBuiltinTypeValidator
    // -------------------------------------------------------------------------

    @Override
    protected ImmutableSet<ByteValidationFailure> validateNonNullBytes(final byte[] bytes) {
        final Set<ByteValidationFailure> failures = Sets.newHashSet();
        // TODO: ASN-105 implement validation logic
        return ImmutableSet.copyOf(failures);
    }
}
