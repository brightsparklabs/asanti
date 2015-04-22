/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.decoder;

import com.brightsparklabs.asanti.decoder.builtin.*;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
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
        final AsnSchemaTypeDefinition.Null visitable = AsnSchemaTypeDefinition.NULL;
        final Object result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionBitString()
    {
        final AsnSchemaTypeDefinitionBitString visitable = new AsnSchemaTypeDefinitionBitString(
                "TEST_NAME",
                AsnSchemaConstraint.NULL);
        final BitStringDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionChoice()
    {
        final AsnSchemaTypeDefinitionChoice visitable = new AsnSchemaTypeDefinitionChoice(
                "TEST_NAME",
                ImmutableList.<AsnSchemaComponentType>of(),
                AsnSchemaConstraint.NULL);
        final Object result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionEnumerated()
    {
        final AsnSchemaTypeDefinitionEnumerated visitable = new AsnSchemaTypeDefinitionEnumerated(
                "TEST_NAME",
                ImmutableList.<AsnSchemaNamedTag>of());
        final Object result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralizedTime()
    {
        final AsnSchemaTypeDefinitionGeneralizedTime visitable
                = new AsnSchemaTypeDefinitionGeneralizedTime("TEST_NAME", AsnSchemaConstraint.NULL);
        final GeneralizedTimeDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralString()
    {
        final AsnSchemaTypeDefinitionGeneralString visitable
                = new AsnSchemaTypeDefinitionGeneralString("TEST_NAME", AsnSchemaConstraint.NULL);
        final GeneralStringDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionIA5String()
    {
        final AsnSchemaTypeDefinitionIa5String visitable = new AsnSchemaTypeDefinitionIa5String(
                "TEST_NAME",
                AsnSchemaConstraint.NULL);
        final Ia5StringDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionInteger()
    {
        final AsnSchemaTypeDefinitionInteger visitable = new AsnSchemaTypeDefinitionInteger(
                "TEST_NAME",
                ImmutableList.<AsnSchemaNamedTag>of(),
                AsnSchemaConstraint.NULL);
        final IntegerDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionNumericString()
    {
        final AsnSchemaTypeDefinitionNumericString visitable
                = new AsnSchemaTypeDefinitionNumericString("TEST_NAME", AsnSchemaConstraint.NULL);
        final NumericStringDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionOctetString()
    {
        final AsnSchemaTypeDefinitionOctetString visitable = new AsnSchemaTypeDefinitionOctetString(
                "TEST_NAME",
                AsnSchemaConstraint.NULL);
        final OctetStringDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequence()
    {
        final AsnSchemaTypeDefinitionSequence visitable = new AsnSchemaTypeDefinitionSequence(
                "TEST_NAME",
                ImmutableList.<AsnSchemaComponentType>of(),
                AsnSchemaConstraint.NULL);
        final Object result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequenceOf()
    {
        final AsnSchemaTypeDefinitionSequenceOf visitable = new AsnSchemaTypeDefinitionSequenceOf(
                "TEST_NAME",
                "TEST_TYPE",
                AsnSchemaConstraint.NULL);
        final Object result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSet()
    {
        final AsnSchemaTypeDefinitionSet visitable = new AsnSchemaTypeDefinitionSet("TEST_NAME",
                ImmutableList.<AsnSchemaComponentType>of(),
                AsnSchemaConstraint.NULL);
        final Object result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSetOf()
    {
        final AsnSchemaTypeDefinitionSetOf visitable = new AsnSchemaTypeDefinitionSetOf("TEST_NAME",
                "TEST_TYPE",
                AsnSchemaConstraint.NULL);
        final Object result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionUtf8String()
    {
        final AsnSchemaTypeDefinitionUtf8String visitable = new AsnSchemaTypeDefinitionUtf8String(
                "TEST_NAME",
                AsnSchemaConstraint.NULL);
        final Utf8StringDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionVisibleString()
    {
        final AsnSchemaTypeDefinitionVisibleString visitable
                = new AsnSchemaTypeDefinitionVisibleString("TEST_NAME", AsnSchemaConstraint.NULL);
        final VisibleStringDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }
}
