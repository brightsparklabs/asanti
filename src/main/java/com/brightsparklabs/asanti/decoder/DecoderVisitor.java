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
import com.brightsparklabs.asanti.model.schema.typedefinition.*;

/**
 * Visitor that visits {@link AsnSchemaTypeDefinition} objects and returns the most appropriate
 * {@link BuiltinTypeDecoder} pertaining to it.
 *
 * @author brightSPARK Labs
 */
public class DecoderVisitor implements AsnSchemaTypeDefinitionVisitor<BuiltinTypeDecoder<?>>
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: Validator
    // -------------------------------------------------------------------------

    @Override
    public NullDecoder visit(final AsnSchemaTypeDefinition.Null visitable)
    {
        return NullDecoder.getInstance();
    }

    @Override
    public BitStringDecoder visit(final AsnSchemaTypeDefinitionBitString visitable)
    {
        return BitStringDecoder.getInstance();
    }

    @Override
    public BuiltinTypeDecoder<?> visit(final AsnSchemaTypeDefinitionChoice visitable)
    {
        // TODO: ASN-8 not decodable
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(final AsnSchemaTypeDefinitionEnumerated visitable)
    {
        // TODO: ASN-8 not decodable
        return null;
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
    public BuiltinTypeDecoder<?> visit(final AsnSchemaTypeDefinitionSequence visitable)
    {
        // TODO: ASN-8 not decodable
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(final AsnSchemaTypeDefinitionSequenceOf visitable)
    {
        // TODO: ASN-8 not decodable
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(final AsnSchemaTypeDefinitionSet visitable)
    {
        // TODO: ASN-8 not decodable
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(final AsnSchemaTypeDefinitionSetOf visitable)
    {
        // TODO: ASN-8 not decodable
        return null;
    }

    @Override
    public Utf8StringDecoder visit(final AsnSchemaTypeDefinitionUtf8String visitable)
    {
        return Utf8StringDecoder.getInstance();
    }

    @Override
    public VisibleStringDecoder visit(final AsnSchemaTypeDefinitionVisibleString visitable)
    {
        return VisibleStringDecoder.getInstance();
    }

    @Override
    public GeneralStringDecoder visit(final AsnSchemaTypeDefinitionGeneralString visitable)
    {
        return GeneralStringDecoder.getInstance();
    }

    @Override
    public GeneralizedTimeDecoder visit(final AsnSchemaTypeDefinitionGeneralizedTime visitable)
    {
        return GeneralizedTimeDecoder.getInstance();
    }
}
