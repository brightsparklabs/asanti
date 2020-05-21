/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.SchemaConstraintValidationFailure;
import com.google.common.collect.ImmutableSet;

/**
 * Models a minimum/maximum value 'bounded' SIZE constraint from within a {@link AsnSchemaType} or
 * {@link AsnSchemaComponentType}. E.g. {@code SIZE (0 .. 256)}.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaSizeConstraint extends AbstractAsnSchemaConstraint {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the minimum length the data can be */
    private final int minimumLength;

    /** the minimum length the data can be */
    private final int maximumLength;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param minimumLength the minimum length the data can be
     * @param maximumLength the minimum length the data can be
     */
    public AsnSchemaSizeConstraint(int minimumLength, int maximumLength) {
        this.minimumLength = minimumLength;
        this.maximumLength = maximumLength;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractAsnSchemaConstraint
    // -------------------------------------------------------------------------

    /**
     * Checks if the length of the supplied array falls between the minimum and maximum bounds of
     * this constraint. The content of the byte array is irrelevant.
     *
     * @param bytes the bytes to test
     * @return any failures encountered in applying the constraint to the supplied bytes
     */
    @Override
    public ImmutableSet<SchemaConstraintValidationFailure> applyToNonNullBytes(byte[] bytes) {
        final int length = bytes.length;
        final boolean conforms = (length >= minimumLength) && (length <= maximumLength);
        if (conforms) {
            return ImmutableSet.of();
        } else {
            final String error =
                    String.format(
                            "Expected a value between %d and %d, but found: %d",
                            minimumLength, maximumLength, length);
            final SchemaConstraintValidationFailure failure =
                    new SchemaConstraintValidationFailure(FailureType.SchemaConstraint, error);
            return ImmutableSet.of(failure);
        }
    }
}
