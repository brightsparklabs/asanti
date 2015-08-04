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
import com.brightsparklabs.asanti.model.schema.primitive.*;

/**
 * Visitor that visits {@link AsnPrimitiveType} objects and returns the most appropriate {@link
 * BuiltinTypeDecoder} pertaining to it.
 *
 * @author brightSPARK Labs
 */
public class DecoderVisitor implements AsnPrimitiveTypeVisitor<BuiltinTypeDecoder<?>>
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnPrimitiveTypeVisitor
    // -------------------------------------------------------------------------

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveType.Invalid visitable)
    {
        return new BuiltinTypeDecoder.Null("invalid instances of AsnPrimitiveType cannot be decoded");
    }

    @Override
    public BitStringDecoder visit(final AsnPrimitiveTypeBitString visitable)
    {
        return BitStringDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveTypeBmpString visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 BMPString decoder not yet implemented");
    }

    @Override
    public BooleanDecoder visit(final AsnPrimitiveTypeBoolean visitable)
    {
        return BooleanDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveTypeCharacterString visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 CharacterString decoder not yet implemented");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveTypeChoice visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 CHOICE types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveTypeEmbeddedPdv visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 EmbeddedPDV decoder not yet implemented");
    }

    @Override
    public EnumeratedDecoder visit(final AsnPrimitiveTypeEnumerated visitable)
    {
        return EnumeratedDecoder.getInstance();
    }

    @Override
    public GeneralizedTimeDecoder visit(final AsnPrimitiveTypeGeneralizedTime visitable)
    {
        return GeneralizedTimeDecoder.getInstance();
    }

    @Override
    public GeneralStringDecoder visit(final AsnPrimitiveTypeGeneralString visitable)
    {
        return GeneralStringDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveTypeGraphicString visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 GraphicString decoder not yet implemented");
    }

    @Override
    public Ia5StringDecoder visit(final AsnPrimitiveTypeIA5String visitable)
    {
        return Ia5StringDecoder.getInstance();
    }

    @Override
    public IntegerDecoder visit(AsnPrimitiveTypeInteger visitable)
    {
        return IntegerDecoder.getInstance();
    }

    @Override
    public NullDecoder visit(final AsnPrimitiveTypeNull visitable)
    {
        return NullDecoder.getInstance();
    }

    @Override
    public NumericStringDecoder visit(final AsnPrimitiveTypeNumericString visitable)
    {
        return NumericStringDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveTypeObjectDescriptor visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 ObjectDescriptor decoder not yet implemented");
    }

    @Override
    public OctetStringDecoder visit(final AsnPrimitiveTypeOctetString visitable)
    {
        return OctetStringDecoder.getInstance();
    }

    @Override
    public OidDecoder visit(final AsnPrimitiveTypeOid visitable)
    {
        return OidDecoder.getInstance();
    }

    @Override
    public PrintableStringDecoder visit(final AsnPrimitiveTypePrintableString visitable)
    {
        return PrintableStringDecoder.getInstance();
    }

    @Override
    public OidDecoder visit(final AsnPrimitiveTypeRelativeOid visitable)
    {
        return OidDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveTypeReal visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 Real decoder not yet implemented");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveTypeSequence visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SEQUENCE types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveTypeSequenceOf visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SEQUENCE OF types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveTypeSet visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SET types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveTypeSetOf visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SET OF types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveTypeUniversalString visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 UniversalString decoder not yet implemented");
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveTypeTeletexString visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 TeletexString decoder not yet implemented");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveTypeUtcTime visitable)
    {
        // TODO ASN-107
        return new BuiltinTypeDecoder.Null("Not yet implemented");
    }

    @Override
    public Utf8StringDecoder visit(final AsnPrimitiveTypeUtf8String visitable)
    {
        return Utf8StringDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder visit(final AsnPrimitiveTypeVideotexString visitable)
    {
        // TODO - ASN-107
        return new BuiltinTypeDecoder.Null("ASN.1 VideotexString decoder not yet implemented");
    }

    @Override
    public VisibleStringDecoder visit(final AsnPrimitiveTypeVisibleString visitable)
    {
        return VisibleStringDecoder.getInstance();
    }
}
