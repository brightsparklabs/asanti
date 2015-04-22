/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.schema.typedefinition.*;
import com.brightsparklabs.asanti.validator.rule.PrimitiveValidationRule;
import com.brightsparklabs.asanti.validator.rule.ValidationRule;

/**
 * Visitor that visits {@link AsnSchemaTypeDefinition} objects and returns the most appropriate
 * {@link ValidationRule} pertaining to it.
 *
 * @author brightSPARK Labs
 */
public class ValidationVisitor implements AsnSchemaTypeDefinitionVisitor<ValidationRule>
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: Validator
    // -------------------------------------------------------------------------

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinition.Null visitable)
    {
        return ValidationRule.NULL;
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionBitString visitable)
    {
        return createPrimitiveValidationRule(visitable);
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionChoice visitable)
    {
        return ValidationRule.NULL;
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionEnumerated visitable)
    {
        return ValidationRule.NULL;
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionGeneralizedTime visitable)
    {
        return createPrimitiveValidationRule(visitable);
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionGeneralString visitable)
    {
        return createPrimitiveValidationRule(visitable);
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionIa5String visitable)
    {
        return createPrimitiveValidationRule(visitable);
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionInteger visitable)
    {
        return createPrimitiveValidationRule(visitable);
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionNumericString visitable)
    {
        return createPrimitiveValidationRule(visitable);
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionOctetString visitable)
    {
        return createPrimitiveValidationRule(visitable);
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionSequence visitable)
    {
        return ValidationRule.NULL;
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionSequenceOf visitable)
    {
        return ValidationRule.NULL;
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionSet visitable)
    {
        return ValidationRule.NULL;
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionSetOf visitable)
    {
        return ValidationRule.NULL;
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionUtf8String visitable)
    {
        return createPrimitiveValidationRule(visitable);
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionVisibleString visitable)
    {
        return createPrimitiveValidationRule(visitable);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: Validator
    // -------------------------------------------------------------------------

    /**
     * Creates a {@link ValidationRule} to validate data from ASN.1 primitives by applying the
     * constraints defined in the supplied primitive type definition.
     *
     * @param primitive
     *         the primitive type definition to create the validation rule from
     *
     * @return a validation rule which validates data from ASN.1 primitives
     */
    private ValidationRule createPrimitiveValidationRule(AsnSchemaTypeDefinitionPrimitive primitive)
    {
        return new PrimitiveValidationRule(primitive.getConstraint());
    }
}
