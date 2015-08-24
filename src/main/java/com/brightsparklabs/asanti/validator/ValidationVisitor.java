/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.validator.builtin.*;
import com.brightsparklabs.assam.validator.ValidationRule;
import com.brightsparklabs.assam.schema.AsnPrimitiveType;
import com.brightsparklabs.assam.schema.AsnPrimitiveTypeVisitor;

/**
 * Visitor that visits {@link AsnPrimitiveTypeVisitor} objects and returns the most appropriate
 * {@link ValidationRule} pertaining to it.
 *
 * @author brightSPARK Labs
 */
public class ValidationVisitor implements AsnPrimitiveTypeVisitor<BuiltinTypeValidator>
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnPrimitiveType.Visitor
    // -------------------------------------------------------------------------

    @Override
    public BuiltinTypeValidator.Null visit(AsnPrimitiveType.Invalid visitable)
    {
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BitStringValidator visit(AsnPrimitiveType.BitString visitable)
    {
        return BitStringValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveType.BmpString visitable)
    {
        // TODO - ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BooleanValidator visit(AsnPrimitiveType.Boolean visitable)
    {
        return BooleanValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveType.CharacterString visitable)
    {
        // TODO - ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveType.Choice visitable)
    {
        // TODO ASN-113
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveType.EmbeddedPdv visitable)
    {
        // TODO - ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public EnumeratedValidator visit(AsnPrimitiveType.Enumerated visitable)
    {
        return EnumeratedValidator.getInstance();
    }

    @Override
    public GeneralizedTimeValidator visit(AsnPrimitiveType.GeneralizedTime visitable)
    {
        return GeneralizedTimeValidator.getInstance();
    }

    @Override
    public GeneralStringValidator visit(AsnPrimitiveType.GeneralString visitable)
    {
        return GeneralStringValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator.Null visit(AsnPrimitiveType.GraphicString visitable)
    {
        // TODO ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public Ia5StringValidator visit(AsnPrimitiveType.IA5String visitable)
    {
        return Ia5StringValidator.getInstance();
    }

    @Override
    public IntegerValidator visit(AsnPrimitiveType.Integer visitable)
    {
        return IntegerValidator.getInstance();
    }

    @Override
    public NullValidator visit(AsnPrimitiveType.Null visitable)
    {
        return NullValidator.getInstance();
    }

    @Override
    public NumericStringValidator visit(AsnPrimitiveType.NumericString visitable)
    {
        return NumericStringValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveType.ObjectDescriptor visitable)
    {
        // TODO ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public OctetStringValidator visit(AsnPrimitiveType.OctetString visitable)
    {
        return OctetStringValidator.getInstance();
    }

    @Override
    public OidValidator visit(AsnPrimitiveType.Oid visitable)
    {
        return OidValidator.getInstance();
    }

    @Override
    public PrintableStringValidator visit(AsnPrimitiveType.PrintableString visitable)
    {
        return PrintableStringValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveType.Real visitable)
    {
        // TODO - ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public OidValidator visit(AsnPrimitiveType.RelativeOid visitable)
    {
        // TODO - ASN-105 - if OidValidator is the right thing to return here then
        // does the RelativeOidValidator need to exist?
        return OidValidator.getInstance();
    }

    @Override
    public ConstructedBuiltinTypeValidator visit(AsnPrimitiveType.Sequence visitable)
    {
        return ConstructedBuiltinTypeValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveType.SequenceOf visitable)
    {
        // TODO ASN-113
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public ConstructedBuiltinTypeValidator visit(AsnPrimitiveType.Set visitable)
    {
        return ConstructedBuiltinTypeValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveType.SetOf visitable)
    {
        // TODO ASN-113
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveType.TeletexString visitable)
    {
        // TODO - ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveType.UniversalString visitable)
    {
        // TODO - ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveType.UtcTime visitable)
    {
        // TODO ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public Utf8StringValidator visit(AsnPrimitiveType.Utf8String visitable)
    {
        return Utf8StringValidator.getInstance();
    }

    @Override
    public BuiltinTypeValidator visit(AsnPrimitiveType.VideotexString visitable)
    {
        // TODO - ASN-105
        return BuiltinTypeValidator.NULL;
    }

    @Override
    public VisibleStringValidator visit(AsnPrimitiveType.VisibleString visitable)
    {
        return VisibleStringValidator.getInstance();
    }

}
