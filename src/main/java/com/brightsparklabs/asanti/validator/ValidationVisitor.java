/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionBitString;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionChoice;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionEnumerated;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionIA5String;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionInteger;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionNumericString;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionOctetString;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionSequence;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionSequenceOf;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionSet;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionSetOf;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionUTF8String;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionVisibleString;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinitionVisitor;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition.AsnSchemaTypeDefinitionNullInstance;

/**
 * Visitor that visits {@link AsnSchemaTypeDefinition} objects and returns the
 * most appropriate {@link ValidationRule} pertaining to it.
 *
 * @author brightSPARK Labs
 */
public class ValidationVisitor implements AsnSchemaTypeDefinitionVisitor<ValidationRule>
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: Validator
    // -------------------------------------------------------------------------

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionNullInstance visitable)
    {
        return ValidationRule.NULL;
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionBitString visitable)
    {
        return ValidationRule.NULL;
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
    public ValidationRule visit(AsnSchemaTypeDefinitionInteger visitable)
    {
        return ValidationRule.NULL;
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionIA5String visitable)
    {
        return ValidationRule.NULL;
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionNumericString visitable)
    {
        return ValidationRule.NULL;
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionOctetString visitable)
    {
        return ValidationRule.NULL;
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
    public ValidationRule visit(AsnSchemaTypeDefinitionUTF8String visitable)
    {
        return ValidationRule.NULL;
    }

    @Override
    public ValidationRule visit(AsnSchemaTypeDefinitionVisibleString visitable)
    {
        return ValidationRule.NULL;
    }
}
