/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.schema.AsnPrimitiveType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.SchemaConstraintValidationFailure;
import com.google.common.collect.ImmutableSet;

/**
 * Convenience class to simplify implementing {@link AsnSchemaConstraint}. Sub-classes should
 * override {@link #applyToNonNullBytes(byte[], AsnPrimitiveType)}.
 *
 * @author brightSPARK Labs
 */
public abstract class AbstractAsnSchemaConstraint implements AsnSchemaConstraint {
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaConstraint
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<SchemaConstraintValidationFailure> apply(
            final byte[] bytes, final AsnPrimitiveType type) {
        if (bytes == null) {
            final SchemaConstraintValidationFailure failure =
                    new SchemaConstraintValidationFailure(
                            FailureType.DataMissing,
                            "No data found to validate against constraint");
            return ImmutableSet.of(failure);
        }
        return applyToNonNullBytes(bytes, type);
    }

    // -------------------------------------------------------------------------
    // PROTECTED METHODS
    // -------------------------------------------------------------------------

    /**
     * Applies the constraint to the supplied bytes. The bytes parameter is guaranteed to be
     * non-{@code null}.
     *
     * @param bytes the bytes to test
     * @param type The ASN.1 type of the bytes.
     * @return any failures encountered in applying the constraint to the supplied bytes
     */
    protected abstract ImmutableSet<SchemaConstraintValidationFailure> applyToNonNullBytes(
            byte[] bytes, AsnPrimitiveType type);
}
