/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.decoder.builtin.*;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.schema.AsnPrimitiveType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.SchemaConstraintValidationFailure;
import com.google.common.collect.ImmutableSet;

/**
 * Models an 'exact' SIZE constraint from within a {@link AsnSchemaType} or {@link
 * AsnSchemaComponentType}. E.g. {@code SIZE (10)}.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaExactSizeConstraint extends AbstractAsnSchemaConstraint {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the length the data must be */
    private final int exactLength;

    private final SizeDeterminingVisitor sizeDeterminingVisitor = new SizeDeterminingVisitor();

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param exactLength the length the data must be
     */
    public AsnSchemaExactSizeConstraint(int exactLength) {
        this.exactLength = exactLength;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractAsnSchemaConstraint
    // -------------------------------------------------------------------------

    /**
     * Checks if the length of the supplied array matches the exact length of this constraint. The
     * content of the byte array is irrelevant.
     *
     * @param bytes the bytes to test
     * @return any failures encountered in applying the constraint to the supplied bytes
     */
    @Override
    public ImmutableSet<SchemaConstraintValidationFailure> applyToNonNullBytes(
            byte[] bytes, final AsnPrimitiveType type) {

        // The size that we are constraining is determined by the type.  ie for an OctectString
        // it's the size of the byte array.  For others, eg BitString, it's the length of the
        // String resulting from decoding the byte array.
        // Most types other than BitString are the same either way, ie the byte array and the
        // decoded string are the same length.

        final SizeDeterminer sizeDeterminer = (SizeDeterminer) type.accept(sizeDeterminingVisitor);

        try {
            final Integer size = sizeDeterminer.determineSize(bytes);
            if (size != exactLength) {
                final String error =
                        String.format("Expected a value of %d, but found: %d", exactLength, size);
                final SchemaConstraintValidationFailure failure =
                        new SchemaConstraintValidationFailure(FailureType.SchemaConstraint, error);
                return ImmutableSet.of(failure);
            }
            return ImmutableSet.of();
        } catch (DecodeException e) {
            final String error =
                    String.format("Exception while determining size: %s", e.getMessage());
            final SchemaConstraintValidationFailure failure =
                    new SchemaConstraintValidationFailure(
                            FailureType.DataIncorrectlyFormatted, error);
            return ImmutableSet.of(failure);
        }
    }
}
