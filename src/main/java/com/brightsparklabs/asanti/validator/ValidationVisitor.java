/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.schema.primitive.*;
import com.brightsparklabs.asanti.validator.builtin.*;
import com.brightsparklabs.asanti.validator.rule.ValidationRule;

/**
 * Visitor that visits {@link AsnPrimitiveTypeVisitor} objects and returns the most appropriate
 * {@link ValidationRule} pertaining to it.
 *
 * @author brightSPARK Labs
 */
public class ValidationVisitor implements AsnPrimitiveTypeVisitor<BuiltinTypeValidator>
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnPrimitiveTypeVisitor
    // -------------------------------------------------------------------------

    @Override
    public BuiltinTypeValidator.Null visit(AsnPrimitiveType.Invalid visitable)
    {
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BitStringValidator visit(AsnPrimitiveTypeBitString visitable)
    {
        return BitStringValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveTypeBmpString visitable)
    {
        // TODO - ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BooleanValidator visit(AsnPrimitiveTypeBoolean visitable)
    {
        return BooleanValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveTypeCharacterString visitable)
    {
        // TODO - ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveTypeChoice visitable)
    {
        // TODO ASN-113
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveTypeEmbeddedPdv visitable)
    {
        // TODO - ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public EnumeratedValidator visit(AsnPrimitiveTypeEnumerated visitable)
    {
        return EnumeratedValidator.getInstance();
    }

    @Override
    public GeneralizedTimeValidator visit(AsnPrimitiveTypeGeneralizedTime visitable)
    {
        return GeneralizedTimeValidator.getInstance();
    }

    @Override
    public GeneralStringValidator visit(AsnPrimitiveTypeGeneralString visitable)
    {
        return GeneralStringValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator.Null visit(AsnPrimitiveTypeGraphicString visitable)
    {
        // TODO ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public Ia5StringValidator visit(AsnPrimitiveTypeIA5String visitable)
    {
        return Ia5StringValidator.getInstance();
    }

    @Override
    public IntegerValidator visit(AsnPrimitiveTypeInteger visitable)
    {
        return IntegerValidator.getInstance();
    }

    @Override
    public NullValidator visit(AsnPrimitiveTypeNull visitable)
    {
        return NullValidator.getInstance();
    }

    @Override
    public NumericStringValidator visit(AsnPrimitiveTypeNumericString visitable)
    {
        return NumericStringValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveTypeObjectDescriptor visitable)
    {
        // TODO ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public OctetStringValidator visit(AsnPrimitiveTypeOctetString visitable)
    {
        return OctetStringValidator.getInstance();
    }

    @Override
    public OidValidator visit(AsnPrimitiveTypeOid visitable)
    {
        return OidValidator.getInstance();
    }

    @Override
    public PrintableStringValidator visit(AsnPrimitiveTypePrintableString visitable)
    {
        return PrintableStringValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveTypeReal visitable)
    {
        // TODO - ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public OidValidator visit(AsnPrimitiveTypeRelativeOid visitable)
    {
        // TODO - ASN-105 - if OidValidator is the right thing to return here then
        // does the RelativeOidValidator need to exist?
        return OidValidator.getInstance();
    }

    @Override
    public ConstructedBuiltinTypeValidator visit(AsnPrimitiveTypeSequence visitable)
    {
        return ConstructedBuiltinTypeValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveTypeSequenceOf visitable)
    {
        // TODO ASN-113
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public ConstructedBuiltinTypeValidator visit(AsnPrimitiveTypeSet visitable)
    {
        return ConstructedBuiltinTypeValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveTypeSetOf visitable)
    {
        // TODO ASN-113
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveTypeTeletexString visitable)
    {
        // TODO - ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveTypeUniversalString visitable)
    {
        // TODO - ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveTypeUtcTime visitable)
    {
        // TODO ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public Utf8StringValidator visit(AsnPrimitiveTypeUtf8String visitable)
    {
        return Utf8StringValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveTypeVideotexString visitable)
    {
        // TODO - ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public VisibleStringValidator visit(AsnPrimitiveTypeVisibleString visitable)
    {
        return VisibleStringValidator.getInstance();
    }

}
