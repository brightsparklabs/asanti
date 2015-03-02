/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator.rule;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.validator.ValidationResult;

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

    private final AsnSchemaConstraint constraint;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    public PrimitiveValidationRule(AsnSchemaConstraint constraint)
    {
        this.constraint = constraint;
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    @Override
    public ValidationResult validate(String tag, DecodedAsnData decodedAsnData)
    {
//        final byte[] bytes = decodedAsnData.getBytes(tag);
//        final boolean met = constraint.isMet(bytes);
//        // final ValidationResult a = new
//        // ValidationResultImpl.ValidationFailure();
        return null;

    }
}
