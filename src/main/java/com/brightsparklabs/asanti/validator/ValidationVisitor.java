/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.schema.primitive.*;
import com.brightsparklabs.asanti.model.schema.tagtype.*;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;
import com.brightsparklabs.asanti.validator.builtin.*;
import com.brightsparklabs.asanti.validator.rule.ValidationRule;

/**
 * Visitor that visits {@link OLDAsnSchemaTypeDefinition} objects and returns the most appropriate
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
    public BuiltinTypeValidator.Null visit(OLDAsnSchemaTypeDefinition.Null visitable)
    {
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BuiltinTypeValidator.Null visit(AsnSchemaTagType.Null visitable)
    {
        return BuiltinTypeValidator.NULL;
    }
    @Override
    public BuiltinTypeValidator.Null visit(AsnPrimitiveType.Null visitable)
    {
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BitStringValidator visit(OLDAsnSchemaTypeDefinitionBitString visitable)
    {
        return BitStringValidator.getInstance();
    }

    @Override
    public BitStringValidator visit(AsnSchemaTagTypeBitString visitable)
    {
        return BitStringValidator.getInstance();
    }

    @Override
    public BitStringValidator visit(AsnPrimitiveTypeBitString visitable)
    {
        return BitStringValidator.getInstance();
    }

    @Override
    public BooleanValidator visit(AsnPrimitiveTypeBoolean visitable)
    {
        return BooleanValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(OLDAsnSchemaTypeDefinitionChoice visitable)
    {
        // TODO ASN-113
        return BuiltinTypeValidator.NULL;
        //return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveTypeChoice visitable)
    {
        // TODO ASN-113
        return BuiltinTypeValidator.NULL;
        //return null;
    }

    @Override
    public BuiltinTypeValidator visit(OLDAsnSchemaTypeDefinitionEnumerated visitable)
    {
        // TODO ASN-113
        return BuiltinTypeValidator.NULL;
        //return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveTypeEnumerated visitable)
    {
        // TODO ASN-113
        return BuiltinTypeValidator.NULL;
        //return null;
    }
    @Override
    public GeneralizedTimeValidator visit(OLDAsnSchemaTypeDefinitionGeneralizedTime visitable)
    {
        return GeneralizedTimeValidator.getInstance();
    }

    @Override
    public GeneralizedTimeValidator visit(AsnSchemaTagTypeGeneralizedTime visitable)
    {
        return GeneralizedTimeValidator.getInstance();
    }

    @Override
    public GeneralizedTimeValidator visit(AsnPrimitiveTypeGeneralizedTime visitable)
    {
        return GeneralizedTimeValidator.getInstance();
    }

    @Override
    public GeneralStringValidator visit(OLDAsnSchemaTypeDefinitionGeneralString visitable)
    {
        return GeneralStringValidator.getInstance();
    }

    @Override
    public PrintableStringValidator visit(AsnSchemaTagTypePrintableString visitable)
    {
        return PrintableStringValidator.getInstance();
    }

    @Override
    public PrintableStringValidator visit(AsnPrimitiveTypePrintableString visitable)
    {
        return PrintableStringValidator.getInstance();
    }

    @Override
    public Ia5StringValidator visit(OLDAsnSchemaTypeDefinitionIa5String visitable)
    {
        return Ia5StringValidator.getInstance();
    }

    @Override
    public Ia5StringValidator visit(AsnPrimitiveTypeIA5String visitable)
    {
        return Ia5StringValidator.getInstance();
    }

    @Override
    public IntegerValidator visit(OLDAsnSchemaTypeDefinitionInteger visitable)
    {
        return IntegerValidator.getInstance();
    }
    @Override
    public IntegerValidator visit(AsnSchemaTagTypeInteger visitable)
    {
        return IntegerValidator.getInstance();
    }
    @Override
    public IntegerValidator visit(AsnPrimitiveTypeInteger visitable)
    {
        return IntegerValidator.getInstance();
    }

    @Override
    public NumericStringValidator visit(OLDAsnSchemaTypeDefinitionNumericString visitable)
    {
        return NumericStringValidator.getInstance();
    }

    @Override
    public NumericStringValidator visit(AsnPrimitiveTypeNumericString visitable)
    {
        return NumericStringValidator.getInstance();
    }

    @Override
    public OctetStringValidator visit(OLDAsnSchemaTypeDefinitionOctetString visitable)
    {
        return OctetStringValidator.getInstance();
    }

    @Override
    public OctetStringValidator visit(AsnSchemaTagTypeOctetString visitable)
    {
        return OctetStringValidator.getInstance();
    }

    @Override
    public OctetStringValidator visit(AsnPrimitiveTypeOctetString visitable)
    {
        return OctetStringValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(OLDAsnSchemaTypeDefinitionSequence visitable)
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
    public BuiltinTypeValidator visit(AsnPrimitiveTypeSequence visitable)
    {
        // TODO ASN-113
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(OLDAsnSchemaTypeDefinitionSequenceOf visitable)
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
    public BuiltinTypeValidator visit(AsnPrimitiveTypeSequenceOf visitable)
    {
        // TODO ASN-113
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(OLDAsnSchemaTypeDefinitionSet visitable)
    {
        // TODO ASN-113
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveTypeSet visitable)
    {
        // TODO ASN-113
        return null;
    }

    @Override
    public BuiltinTypeValidator visit(OLDAsnSchemaTypeDefinitionSetOf visitable)
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
    public BuiltinTypeValidator visit(AsnPrimitiveTypeSetOf visitable)
    {
        // TODO ASN-113
        return null;
    }

    @Override
    public Utf8StringValidator visit(OLDAsnSchemaTypeDefinitionUtf8String visitable)
    {
        return Utf8StringValidator.getInstance();
    }

    @Override
    public Utf8StringValidator visit(AsnSchemaTagTypeUtf8String visitable)
    {
        return Utf8StringValidator.getInstance();
    }
    @Override
    public Utf8StringValidator visit(AsnPrimitiveTypeUtf8String visitable)
    {
        return Utf8StringValidator.getInstance();
    }

    @Override
    public VisibleStringValidator visit(AsnPrimitiveTypeVisibleString visitable)
    {
        return VisibleStringValidator.getInstance();
    }

    @Override
    public VisibleStringValidator visit(OLDAsnSchemaTypeDefinitionVisibleString visitable)
    {
        return VisibleStringValidator.getInstance();
    }

    @Override
    public OidValidator visit(AsnSchemaTagTypeObjectIdentifier visitable)
    {
        return OidValidator.getInstance();
    }

    @Override
    public OidValidator visit(AsnPrimitiveTypeOid visitable)
    {
        return OidValidator.getInstance();
    }

    @Override
    public OidValidator visit(AsnPrimitiveTypeRelativeOid visitable)
    {
        return OidValidator.getInstance();
    }
}
