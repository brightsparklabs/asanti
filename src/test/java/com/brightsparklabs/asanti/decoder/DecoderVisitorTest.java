/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.decoder;

import com.brightsparklabs.asanti.decoder.builtin.*;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypeBitString;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link DecoderVisitor}
 *
 * @author brightSPARK Labs
 */
public class DecoderVisitorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final DecoderVisitor instance = new DecoderVisitor();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testVisitAsnSchemaTypeDefinitionNullInstance()
    {
        final Object result = instance.visit(AsnPrimitiveType.NULL);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionBitString()
    {
        final BitStringDecoder result = instance.visit(AsnPrimitiveType.BIT_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionChoice()
    {
        final Object result = instance.visit(AsnPrimitiveType.CHOICE);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionEnumerated()
    {
        final Object result = instance.visit(AsnPrimitiveType.ENUMERATED);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralizedTime()
    {
        final GeneralizedTimeDecoder result = instance.visit(AsnPrimitiveType.GENERALIZED_TIME);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralString()
    {
        // TODO MJF final GeneralStringDecoder result = instance.visit(AsnPrimitiveType.GENERAL_STRING);
        //assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionIA5String()
    {
        final Ia5StringDecoder result = instance.visit(AsnPrimitiveType.IA5_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionInteger()
    {
        final IntegerDecoder result = instance.visit(AsnPrimitiveType.INTEGER);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionNumericString()
    {
        final NumericStringDecoder result = instance.visit(AsnPrimitiveType.NUMERIC_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionOctetString()
    {
        final OctetStringDecoder result = instance.visit(AsnPrimitiveType.OCTET_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequence()
    {
        final Object result = instance.visit(AsnPrimitiveType.SEQUENCE);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequenceOf()
    {
        final Object result = instance.visit(AsnPrimitiveType.SEQUENCE_OF);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSet()
    {
        final Object result = instance.visit(AsnPrimitiveType.SET);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSetOf()
    {
        final Object result = instance.visit(AsnPrimitiveType.SET_OF);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionUtf8String()
    {
        final Utf8StringDecoder result = instance.visit(AsnPrimitiveType.UTF8_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionVisibleString()
    {
        final VisibleStringDecoder result = instance.visit(AsnPrimitiveType.VISIBLE_STRING);
        assertNotNull(result);
    }
}
