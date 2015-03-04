/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator.rule;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.ValidationFailure;
import com.brightsparklabs.asanti.validator.ValidationFailureImpl;
import com.google.common.collect.ImmutableSet;

/**
 * Represents a rule to validate {@link DecodedAsnData} against.
 *
 * @author brightSPARK Labs
 */
public class PrimitiveValidationRule implements ValidationRule
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the constraint to apply during validation */
    private final AsnSchemaConstraint constraint;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param constraint
     *            the constraint to apply during validation
     */
    public PrimitiveValidationRule(AsnSchemaConstraint constraint)
    {
        this.constraint = constraint;
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<ValidationFailure> validate(String tag, DecodedAsnData decodedAsnData)
    {
        final byte[] bytes = decodedAsnData.getBytes(tag);
        final OperationResult<byte[]> constraintResult = constraint.apply(bytes);
        if (!constraintResult.wasSuccessful())
        {
            final ValidationFailure failure =
                    new ValidationFailureImpl(tag,
                            FailureType.MandatoryFieldMissing,
                            constraintResult.getFailureReason());
            return ImmutableSet.of(failure);
        }
        return ImmutableSet.of();
    }
}
