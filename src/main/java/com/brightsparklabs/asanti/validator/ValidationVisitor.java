/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.schema.typedefinition.*;
import com.brightsparklabs.asanti.validator.builtin.BuiltinTypeValidator;
import com.brightsparklabs.asanti.validator.rule.ValidationRule;

/**
 * Visitor that visits {@link AsnSchemaTypeDefinition} objects and returns the most appropriate
 * {@link ValidationRule} pertaining to it.
 *
 * @author brightSPARK Labs
 */
public class ValidationVisitor implements AsnSchemaTypeDefinitionVisitor<BuiltinTypeValidator>
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: Validator
    // -------------------------------------------------------------------------

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinition.Null visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionBitString visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionChoice visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionEnumerated visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionInteger visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionIa5String visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionNumericString visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionOctetString visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionSequence visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionSequenceOf visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionSet visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionSetOf visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionUtf8String visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionVisibleString visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionGeneralString visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionGeneralizedTime visitable)
    {
        return null;
    }
}
