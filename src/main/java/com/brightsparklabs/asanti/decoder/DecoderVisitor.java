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
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagTypeInteger;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagTypeSequence;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagTypeUtf8String;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;

/**
 * Visitor that visits {@link AsnSchemaTypeDefinition} objects and returns the most appropriate
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
    public BuiltinTypeDecoder.Null visit(final AsnSchemaTypeDefinition.Null visitable)
    {
        return new BuiltinTypeDecoder.Null(
                "null instances of AsnSchemaTypeDefinition cannot be decoded");
    }

    @Override
    public BitStringDecoder visit(final AsnSchemaTypeDefinitionBitString visitable)
    {
        return BitStringDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnSchemaTypeDefinitionChoice visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 CHOICE types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnSchemaTypeDefinitionEnumerated visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 ENUMERATED types cannot be decoded");
    }

    @Override
    public GeneralizedTimeDecoder visit(final AsnSchemaTypeDefinitionGeneralizedTime visitable)
    {
        return GeneralizedTimeDecoder.getInstance();
    }

    @Override
    public GeneralStringDecoder visit(final AsnSchemaTypeDefinitionGeneralString visitable)
    {
        return GeneralStringDecoder.getInstance();
    }

    @Override
    public Ia5StringDecoder visit(final AsnSchemaTypeDefinitionIa5String visitable)
    {
        return Ia5StringDecoder.getInstance();
    }

    @Override
    public IntegerDecoder visit(final AsnSchemaTypeDefinitionInteger visitable)
    {
        return IntegerDecoder.getInstance();
    }
    public IntegerDecoder visit(AsnSchemaTagTypeInteger visitable)
    {
        return IntegerDecoder.getInstance();
    }

    @Override
    public NumericStringDecoder visit(final AsnSchemaTypeDefinitionNumericString visitable)
    {
        return NumericStringDecoder.getInstance();
    }

    @Override
    public OctetStringDecoder visit(final AsnSchemaTypeDefinitionOctetString visitable)
    {
        return OctetStringDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnSchemaTypeDefinitionSequence visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SEQUENCE types cannot be decoded");
    }
    @Override
    public BuiltinTypeDecoder.Null visit(final AsnSchemaTagTypeSequence visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SEQUENCE types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnSchemaTypeDefinitionSequenceOf visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SEQUENCE OF types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnSchemaTypeDefinitionSet visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SET types cannot be decoded");
    }

    @Override
    public BuiltinTypeDecoder.Null visit(final AsnSchemaTypeDefinitionSetOf visitable)
    {
        return new BuiltinTypeDecoder.Null("ASN.1 SET OF types cannot be decoded");
    }

    @Override
    public Utf8StringDecoder visit(final AsnSchemaTypeDefinitionUtf8String visitable)
    {
        return Utf8StringDecoder.getInstance();
    }
    @Override
    public Utf8StringDecoder visit(final AsnSchemaTagTypeUtf8String visitable)
    {
        return Utf8StringDecoder.getInstance();
    }

    @Override
    public VisibleStringDecoder visit(final AsnSchemaTypeDefinitionVisibleString visitable)
    {
        return VisibleStringDecoder.getInstance();
    }
}
