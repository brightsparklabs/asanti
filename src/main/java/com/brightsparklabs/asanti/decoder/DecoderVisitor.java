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
import com.brightsparklabs.asanti.model.schema.tagtype.*;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;

/**
 * Visitor that visits {@link OLDAsnSchemaTypeDefinition} objects and returns the most appropriate
 * {@link BuiltinTypeDecoder} pertaining to it.
 *
 * @author brightSPARK Labs
 */
public class DecoderVisitor implements AsnSchemaTagTypeVisitor<BuiltinTypeDecoder<?>>
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: Validator
    // -------------------------------------------------------------------------

    @Override
    public BuiltinTypeDecoder.Null visit(final OLDAsnSchemaTypeDefinition.Null visitable)
    {
        return new BuiltinTypeDecoder.Null(
                "null instances of OLDAsnSchemaTypeDefinition cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnSchemaTagType.Null visitable)
    {
        return new BuiltinTypeDecoder.Null(
                "null instances of AsnSchemaTagType cannot be decoded");
    }
    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveType.Null visitable)
    {
        return new BuiltinTypeDecoder.Null(
                "null instances of AsnSchemaTagType cannot be decoded");
    }

    @Override
    public BitStringDecoder visit(final OLDAsnSchemaTypeDefinitionBitString visitable)
    {
        return BitStringDecoder.getInstance();
    }

    @Override
    public BitStringDecoder visit(final AsnSchemaTagTypeBitString visitable)
    {
        return BitStringDecoder.getInstance();
    }

    @Override
    public BitStringDecoder visit(final AsnPrimitiveTypeBitString visitable)
    {
        return BitStringDecoder.getInstance();
    }

    @Override
    public BooleanDecoder visit(final AsnPrimitiveTypeBoolean visitable)
    {
        return BooleanDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final OLDAsnSchemaTypeDefinitionChoice visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 CHOICE types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveTypeChoice visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 CHOICE types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final OLDAsnSchemaTypeDefinitionEnumerated visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 ENUMERATED types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveTypeEnumerated visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 ENUMERATED types cannot be decoded");
    }

    @Override
    public GeneralizedTimeDecoder visit(final OLDAsnSchemaTypeDefinitionGeneralizedTime visitable)
    {
        return GeneralizedTimeDecoder.getInstance();
    }

    @Override
    public GeneralizedTimeDecoder visit(final AsnSchemaTagTypeGeneralizedTime visitable)
    {
        return GeneralizedTimeDecoder.getInstance();
    }

    @Override
    public GeneralizedTimeDecoder visit(final AsnPrimitiveTypeGeneralizedTime visitable)
    {
        return GeneralizedTimeDecoder.getInstance();
    }

    @Override
    public GeneralStringDecoder visit(final OLDAsnSchemaTypeDefinitionGeneralString visitable)
    {
        return GeneralStringDecoder.getInstance();
    }

    @Override
    public PrintableStringDecoder visit(final AsnPrimitiveTypePrintableString visitable)
    {
        return PrintableStringDecoder.getInstance();
    }

    @Override
    public PrintableStringDecoder visit(final AsnSchemaTagTypePrintableString visitable)
    {
        return PrintableStringDecoder.getInstance();
    }

    @Override
    public Ia5StringDecoder visit(final OLDAsnSchemaTypeDefinitionIa5String visitable)
    {
        return Ia5StringDecoder.getInstance();
    }

    @Override
    public Ia5StringDecoder visit(final AsnPrimitiveTypeIA5String visitable)
    {
        return Ia5StringDecoder.getInstance();
    }

    @Override
    public IntegerDecoder visit(final OLDAsnSchemaTypeDefinitionInteger visitable)
    {
        return IntegerDecoder.getInstance();
    }
    @Override
    public IntegerDecoder visit(AsnSchemaTagTypeInteger visitable)
    {
        return IntegerDecoder.getInstance();
    }
    @Override
    public IntegerDecoder visit(AsnPrimitiveTypeInteger visitable)
    {
        return IntegerDecoder.getInstance();
    }

    @Override
    public NumericStringDecoder visit(final OLDAsnSchemaTypeDefinitionNumericString visitable)
    {
        return NumericStringDecoder.getInstance();
    }

    @Override
    public NumericStringDecoder visit(final AsnPrimitiveTypeNumericString visitable)
    {
        return NumericStringDecoder.getInstance();
    }

    @Override
    public OctetStringDecoder visit(final OLDAsnSchemaTypeDefinitionOctetString visitable)
    {
        return OctetStringDecoder.getInstance();
    }
    @Override
    public OctetStringDecoder visit(final AsnSchemaTagTypeOctetString visitable)
    {
        return OctetStringDecoder.getInstance();
    }
    @Override
    public OctetStringDecoder visit(final AsnPrimitiveTypeOctetString visitable)
    {
        return OctetStringDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final OLDAsnSchemaTypeDefinitionSequence visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SEQUENCE types cannot be decoded");
    }
    @Override
    public BuiltinTypeDecoder.Null visit(final AsnSchemaTagTypeSequence visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SEQUENCE types cannot be decoded");
    }
    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveTypeSequence visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SEQUENCE types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final OLDAsnSchemaTypeDefinitionSequenceOf visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SEQUENCE OF types cannot be decoded");
    }
    @Override
    public BuiltinTypeDecoder.Null visit(final AsnSchemaTagTypeSequenceOf visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SEQUENCE OF types cannot be decoded");
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
    public BuiltinTypeDecoder.Null visit(final OLDAsnSchemaTypeDefinitionSet visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SET types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final OLDAsnSchemaTypeDefinitionSetOf visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SET OF types cannot be decoded");
    }
    @Override
    public BuiltinTypeDecoder.Null visit(final AsnSchemaTagTypeSetOf visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SET OF types cannot be decoded");
    }
    @Override
    public BuiltinTypeDecoder.Null visit(final AsnPrimitiveTypeSetOf visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SET OF types cannot be decoded");
    }

    @Override
    public Utf8StringDecoder visit(final OLDAsnSchemaTypeDefinitionUtf8String visitable)
    {
        return Utf8StringDecoder.getInstance();
    }
    @Override
    public Utf8StringDecoder visit(final AsnSchemaTagTypeUtf8String visitable)
    {
        return Utf8StringDecoder.getInstance();
    }
    @Override
    public Utf8StringDecoder visit(final AsnPrimitiveTypeUtf8String visitable)
    {
        return Utf8StringDecoder.getInstance();
    }

    @Override
    public VisibleStringDecoder visit(final OLDAsnSchemaTypeDefinitionVisibleString visitable)
    {
        return VisibleStringDecoder.getInstance();
    }

    @Override
    public VisibleStringDecoder visit(final AsnPrimitiveTypeVisibleString visitable)
    {
        return VisibleStringDecoder.getInstance();
    }

    @Override
    public OidDecoder visit(final AsnSchemaTagTypeObjectIdentifier visitable)
    {
        return OidDecoder.getInstance();
    }

    @Override
    public OidDecoder visit(final AsnPrimitiveTypeOid visitable)
    {
        return OidDecoder.getInstance();
    }

    @Override
    public OidDecoder visit(final AsnPrimitiveTypeRelativeOid visitable)
    {
        return OidDecoder.getInstance();
    }

}
