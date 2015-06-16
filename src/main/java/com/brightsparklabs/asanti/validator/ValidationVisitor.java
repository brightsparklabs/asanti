/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.schema.tagtype.*;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;
import com.brightsparklabs.asanti.validator.builtin.*;
import com.brightsparklabs.asanti.validator.rule.ValidationRule;

/**
 * Visitor that visits {@link AsnSchemaTypeDefinition} objects and returns the most appropriate
 * {@link ValidationRule} pertaining to it.
 *
 * @author brightSPARK Labs
 */
public class ValidationVisitor implements AsnSchemaTagTypeVisitor<BuiltinTypeValidator>
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: Validator
    // -------------------------------------------------------------------------

    @Override
    public BuiltinTypeValidator.Null visit(AsnSchemaTypeDefinition.Null visitable)
    {
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BuiltinTypeValidator.Null visit(AsnSchemaTagType.Null visitable)
    {
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BitStringValidator visit(AsnSchemaTypeDefinitionBitString visitable)
    {
        return BitStringValidator.getInstance();
    }

    @Override
    public BitStringValidator visit(AsnSchemaTagTypeBitString visitable)
    {
        return BitStringValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionChoice visitable)
    {
        // TODO ASN-113
        return BuiltinTypeValidator.NULL;
        //return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionEnumerated visitable)
    {
        // TODO ASN-113
        return BuiltinTypeValidator.NULL;
        //return null;
    }

    @Override
    public GeneralizedTimeValidator visit(AsnSchemaTypeDefinitionGeneralizedTime visitable)
    {
        return GeneralizedTimeValidator.getInstance();
    }

    @Override
    public GeneralizedTimeValidator visit(AsnSchemaTagTypeGeneralizedTime visitable)
    {
        return GeneralizedTimeValidator.getInstance();
    }

    @Override
    public GeneralStringValidator visit(AsnSchemaTypeDefinitionGeneralString visitable)
    {
        return GeneralStringValidator.getInstance();
    }

    @Override
    public PrintableStringValidator visit(AsnSchemaTagTypePrintableString visitable)
    {
        return PrintableStringValidator.getInstance();
    }

    @Override
    public Ia5StringValidator visit(AsnSchemaTypeDefinitionIa5String visitable)
    {
        return Ia5StringValidator.getInstance();
    }

    public IntegerValidator visit(AsnSchemaTypeDefinitionInteger visitable)
    {
        return IntegerValidator.getInstance();
    }
    public IntegerValidator visit(AsnSchemaTagTypeInteger visitable)
    {
        return IntegerValidator.getInstance();
    }

    @Override
    public NumericStringValidator visit(AsnSchemaTypeDefinitionNumericString visitable)
    {
        return NumericStringValidator.getInstance();
    }

    @Override
    public OctetStringValidator visit(AsnSchemaTypeDefinitionOctetString visitable)
    {
        return OctetStringValidator.getInstance();
    }

    @Override
    public OctetStringValidator visit(AsnSchemaTagTypeOctetString visitable)
    {
        return OctetStringValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionSequence visitable)
    {
        // TODO ASN-113
        return null;
    }
    @Override
    public BuiltinTypeValidator visit(AsnSchemaTagTypeSequence visitable)
    {
        // TODO ASN-113
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionSequenceOf visitable)
    {
        // TODO ASN-113
        return null;
    }
    @Override
    public BuiltinTypeValidator visit(AsnSchemaTagTypeSequenceOf visitable)
    {
        // TODO ASN-113
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionSet visitable)
    {
        // TODO ASN-113
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTypeDefinitionSetOf visitable)
    {
        // TODO ASN-113
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnSchemaTagTypeSetOf visitable)
    {
        // TODO ASN-113
        return null;
    }

    @Override
    public Utf8StringValidator visit(AsnSchemaTypeDefinitionUtf8String visitable)
    {
        return Utf8StringValidator.getInstance();
    }

    @Override
    public Utf8StringValidator visit(AsnSchemaTagTypeUtf8String visitable)
    {
        return Utf8StringValidator.getInstance();
    }

    @Override
    public VisibleStringValidator visit(AsnSchemaTypeDefinitionVisibleString visitable)
    {
        return VisibleStringValidator.getInstance();
    }

    @Override
    public OidValidator visit(AsnSchemaTagTypeObjectIdentifier visitable)
    {
        return OidValidator.getInstance();
    }
}
