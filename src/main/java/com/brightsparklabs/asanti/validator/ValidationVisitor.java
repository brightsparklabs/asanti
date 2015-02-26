/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition.AsnSchemaTypeDefinitionNull;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionBitString;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionChoice;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionEnumerated;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionIA5String;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionNumericString;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionOctetString;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionSequence;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionSequenceOf;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionSet;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionSetOf;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionUTF8String;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionVisitor;

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
    public ValidationRule visit(AsnSchemaTypeDefinitionNull visitable)
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
}
