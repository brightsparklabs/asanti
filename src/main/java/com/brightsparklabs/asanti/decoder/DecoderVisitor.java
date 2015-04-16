/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder;

import com.brightsparklabs.asanti.decoder.builtin.BuiltinTypeDecoder;
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
    public BuiltinTypeDecoder<?> visit(AsnSchemaTypeDefinition.Null visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(AsnSchemaTypeDefinitionBitString visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(AsnSchemaTypeDefinitionChoice visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(AsnSchemaTypeDefinitionEnumerated visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(AsnSchemaTypeDefinitionInteger visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(AsnSchemaTypeDefinitionIa5String visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(AsnSchemaTypeDefinitionNumericString visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(AsnSchemaTypeDefinitionOctetString visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(AsnSchemaTypeDefinitionSequence visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(AsnSchemaTypeDefinitionSequenceOf visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(AsnSchemaTypeDefinitionSet visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(AsnSchemaTypeDefinitionSetOf visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(AsnSchemaTypeDefinitionUtf8String visitable)
    {
        return null;
    }

    @Override
    public BuiltinTypeDecoder<?> visit(AsnSchemaTypeDefinitionVisibleString visitable)
    {
        return null;
    }
}
