/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder;

import com.brightsparklabs.asanti.decoder.builtin.*;
import com.brightsparklabs.assam.schema.AsnPrimitiveType;
import com.brightsparklabs.assam.schema.AsnPrimitiveTypeVisitor;

/**
 * Visitor that visits {@link AsnPrimitiveType} objects and returns the most appropriate {@link
 * BuiltinTypeDecoder} pertaining to it.
 *
 * @author brightSPARK Labs
 */
public class DecoderVisitor implements AsnPrimitiveTypeVisitor<BuiltinTypeDecoder<?>>
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnPrimitiveType.Visitor
    // -------------------------------------------------------------------------

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveType.Invalid visitable)
    {
        return new BuiltinTypeDecoder.Null(
                "invalid instances of AsnPrimitiveType. cannot be decoded");
    }

    @Override
    public BitStringDecoder visit(final AsnPrimitiveType.BitString visitable)
    {
        return BitStringDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveType.BmpString visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 BMPString decoder not yet implemented");
    }

    @Override
    public BooleanDecoder visit(final AsnPrimitiveType.Boolean visitable)
    {
        return BooleanDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveType.CharacterString visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 CharacterString decoder not yet implemented");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveType.Choice visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 CHOICE types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveType.EmbeddedPdv visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 EmbeddedPDV decoder not yet implemented");
    }

    @Override
    public EnumeratedDecoder visit(final AsnPrimitiveType.Enumerated visitable)
    {
        return EnumeratedDecoder.getInstance();
    }

    @Override
    public GeneralizedTimeDecoder visit(final AsnPrimitiveType.GeneralizedTime visitable)
    {
        return GeneralizedTimeDecoder.getInstance();
    }

    @Override
    public GeneralStringDecoder visit(final AsnPrimitiveType.GeneralString visitable)
    {
        return GeneralStringDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveType.GraphicString visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 GraphicString decoder not yet implemented");
    }

    @Override
    public Ia5StringDecoder visit(final AsnPrimitiveType.IA5String visitable)
    {
        return Ia5StringDecoder.getInstance();
    }

    @Override
    public IntegerDecoder visit(final AsnPrimitiveType.Integer visitable)
    {
        return IntegerDecoder.getInstance();
    }

    @Override
    public NullDecoder visit(final AsnPrimitiveType.Null visitable)
    {
        return NullDecoder.getInstance();
    }

    @Override
    public NumericStringDecoder visit(final AsnPrimitiveType.NumericString visitable)
    {
        return NumericStringDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveType.ObjectDescriptor visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 ObjectDescriptor decoder not yet implemented");
    }

    @Override
    public OctetStringDecoder visit(final AsnPrimitiveType.OctetString visitable)
    {
        return OctetStringDecoder.getInstance();
    }

    @Override
    public OidDecoder visit(final AsnPrimitiveType.Oid visitable)
    {
        return OidDecoder.getInstance();
    }

    @Override
    public PrintableStringDecoder visit(final AsnPrimitiveType.PrintableString visitable)
    {
        return PrintableStringDecoder.getInstance();
    }

    @Override
    public OidDecoder visit(final AsnPrimitiveType.RelativeOid visitable)
    {
        return OidDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveType.Real visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 Real decoder not yet implemented");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveType.Sequence visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SEQUENCE types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveType.SequenceOf visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SEQUENCE OF types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveType.Set visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SET types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveType.SetOf visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SET OF types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveType.UniversalString visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 UniversalString decoder not yet implemented");
    }

    @Override
    public UtcTimeDecoder visit(final AsnPrimitiveType.UtcTime visitable)
    {
        return UtcTimeDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveType.TeletexString visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 TeletexString decoder not yet implemented");
    }

    @Override
    public Utf8StringDecoder visit(final AsnPrimitiveType.Utf8String visitable)
    {
        return Utf8StringDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveType.VideotexString visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 VideotexString decoder not yet implemented");
    }

    @Override
    public VisibleStringDecoder visit(final AsnPrimitiveType.VisibleString visitable)
    {
        return VisibleStringDecoder.getInstance();
    }
}
