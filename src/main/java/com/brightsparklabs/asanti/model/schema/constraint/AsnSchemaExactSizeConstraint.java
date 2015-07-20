/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.SchemaConstraintValidationFailure;
import com.google.common.collect.ImmutableSet;

/**
 * Models an 'exact' SIZE constraint from within a {@link AsnSchemaType} or {@link
 * AsnSchemaComponentType}. E.g. {@code SIZE (10)}.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaExactSizeConstraint extends AbstractAsnSchemaConstraint
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the length the data must be */
    private final int exactLength;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param exactLength
     *         the length the data must be
     */
    public AsnSchemaExactSizeConstraint(int exactLength)
    {
        this.exactLength = exactLength;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractAsnSchemaConstraint
    // -------------------------------------------------------------------------

    /**
     * Checks if the length of the supplied array matches the exact length of this constraint. The
     * content of the byte array is irrelevant.
     *
     * @param bytes
     *         the bytes to test
     *
     * @return any failures encountered in applying the constraint to the supplied bytes
     */
    @Override
    public ImmutableSet<SchemaConstraintValidationFailure> applyToNonNullBytes(byte[] bytes)
    {
        if (bytes.length == exactLength)
        {
            return ImmutableSet.of();
        }
        else
        {
            final String error = String.format("Expected a value of %d, but found: %d",
                    exactLength,
                    bytes.length);
            final SchemaConstraintValidationFailure failure = new SchemaConstraintValidationFailure(
                    FailureType.SchemaConstraint,
                    error);
            return ImmutableSet.of(failure);
        }
    }
}
