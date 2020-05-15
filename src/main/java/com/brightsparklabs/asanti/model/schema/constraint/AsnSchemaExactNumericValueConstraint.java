/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.validator.failure.SchemaConstraintValidationFailure;
import com.brightsparklabs.assam.validator.FailureType;
import com.google.common.collect.ImmutableSet;
import java.math.BigInteger;

/**
 * Models an 'exact' numeric value constraint from within a {@link AsnSchemaType} or {@link
 * AsnSchemaComponentType}. E.g. {@code INTEGER (10)}.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaExactNumericValueConstraint extends AbstractAsnSchemaConstraint {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the value the data must be */
    private final BigInteger exactValue;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param exactValue the value the data must be
     * @throws NullPointerException if any of the parameters are {@code null}
     */
    public AsnSchemaExactNumericValueConstraint(BigInteger exactValue) {
        checkNotNull(exactValue);
        this.exactValue = exactValue;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractAsnSchemaConstraint
    // -------------------------------------------------------------------------

    /**
     * Checks if the value in the supplied array matches the exact value of this constraint. The
     * value of the array is read via {@link BigInteger#BigInteger(byte[])}.
     *
     * @param bytes the bytes to test
     * @return any failures encountered in applying the constraint to the supplied bytes
     */
    @Override
    public ImmutableSet<SchemaConstraintValidationFailure> applyToNonNullBytes(byte[] bytes) {
        try {
            final BigInteger value = new BigInteger(bytes);
            if (exactValue.equals(value)) {
                return ImmutableSet.of();
            } else {
                final String error =
                        String.format(
                                "Expected a value of %s, but found: %s",
                                exactValue.toString(), value.toString());
                final SchemaConstraintValidationFailure failure =
                        new SchemaConstraintValidationFailure(FailureType.SchemaConstraint, error);
                return ImmutableSet.of(failure);
            }
        } catch (final NumberFormatException ex) {
            final String error =
                    String.format(
                            "Expected a value of %s, but no value found", exactValue.toString());
            final SchemaConstraintValidationFailure failure =
                    new SchemaConstraintValidationFailure(FailureType.SchemaConstraint, error);
            return ImmutableSet.of(failure);
        }
    }
}
