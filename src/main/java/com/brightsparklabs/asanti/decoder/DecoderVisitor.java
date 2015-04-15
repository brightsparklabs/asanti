/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder;

import com.brightsparklabs.asanti.decoder.typedefinitions.AsnTypeDefinitionDecoder;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;

/**
 * Visitor that visits {@link AsnSchemaTypeDefinition} objects and returns the most appropriate
 * {@link AsnTypeDefinitionDecoder} pertaining to it.
 *
 * @author brightSPARK Labs
 */
public class DecoderVisitor implements AsnSchemaTypeDefinitionVisitor<AsnTypeDefinitionDecoder<?>>
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: Validator
    // -------------------------------------------------------------------------

    @Override
    public AsnTypeDefinitionDecoder<?> visit(AsnSchemaTypeDefinition.Null visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDefinitionDecoder<?> visit(AsnSchemaTypeDefinitionBitString visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDefinitionDecoder<?> visit(AsnSchemaTypeDefinitionChoice visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDefinitionDecoder<?> visit(AsnSchemaTypeDefinitionEnumerated visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDefinitionDecoder<?> visit(AsnSchemaTypeDefinitionInteger visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDefinitionDecoder<?> visit(AsnSchemaTypeDefinitionIa5String visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDefinitionDecoder<?> visit(AsnSchemaTypeDefinitionNumericString visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDefinitionDecoder<?> visit(AsnSchemaTypeDefinitionOctetString visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDefinitionDecoder<?> visit(AsnSchemaTypeDefinitionSequence visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDefinitionDecoder<?> visit(AsnSchemaTypeDefinitionSequenceOf visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDefinitionDecoder<?> visit(AsnSchemaTypeDefinitionSet visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDefinitionDecoder<?> visit(AsnSchemaTypeDefinitionSetOf visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDefinitionDecoder<?> visit(AsnSchemaTypeDefinitionUtf8String visitable)
    {
        return null;
    }

    @Override
    public AsnTypeDefinitionDecoder<?> visit(AsnSchemaTypeDefinitionVisibleString visitable)
    {
        return null;
    }
}
