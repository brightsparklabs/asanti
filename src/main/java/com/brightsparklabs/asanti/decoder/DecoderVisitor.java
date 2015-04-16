/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder;

import com.brightsparklabs.asanti.decoder.builtin.AsnTypeDecoder;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;

/**
 * Visitor that visits {@link AsnSchemaTypeDefinition} objects and returns the most appropriate
 * {@link AsnTypeDecoder} pertaining to it.
 *
 * @author brightSPARK Labs
 */
public class DecoderVisitor implements AsnSchemaTypeDefinitionVisitor<AsnTypeDecoder<?>>
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: Validator
    // -------------------------------------------------------------------------

    @Override
    public AsnTypeDecoder<?> visit(AsnSchemaTypeDefinition.Null visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDecoder<?> visit(AsnSchemaTypeDefinitionBitString visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDecoder<?> visit(AsnSchemaTypeDefinitionChoice visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDecoder<?> visit(AsnSchemaTypeDefinitionEnumerated visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDecoder<?> visit(AsnSchemaTypeDefinitionInteger visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDecoder<?> visit(AsnSchemaTypeDefinitionIa5String visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDecoder<?> visit(AsnSchemaTypeDefinitionNumericString visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDecoder<?> visit(AsnSchemaTypeDefinitionOctetString visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDecoder<?> visit(AsnSchemaTypeDefinitionSequence visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDecoder<?> visit(AsnSchemaTypeDefinitionSequenceOf visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDecoder<?> visit(AsnSchemaTypeDefinitionSet visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDecoder<?> visit(AsnSchemaTypeDefinitionSetOf visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDecoder<?> visit(AsnSchemaTypeDefinitionUtf8String visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDecoder<?> visit(AsnSchemaTypeDefinitionVisibleString visitable)
    {
        return null;
    }
}
