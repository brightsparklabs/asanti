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
        final OLDAsnSchemaTypeDefinition.Null visitable = OLDAsnSchemaTypeDefinition.NULL;
        final Object result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionBitString()
    {
        final OLDAsnSchemaTypeDefinitionBitString
                visitable = new OLDAsnSchemaTypeDefinitionBitString(
                "TEST_NAME",
                AsnSchemaConstraint.NULL);
        final BitStringDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionChoice()
    {
        final OLDAsnSchemaTypeDefinitionChoice visitable = new OLDAsnSchemaTypeDefinitionChoice(
                "TEST_NAME",
                ImmutableList.<AsnSchemaComponentType>of(),
                AsnSchemaConstraint.NULL);
        final Object result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionEnumerated()
    {
        final OLDAsnSchemaTypeDefinitionEnumerated
                visitable = new OLDAsnSchemaTypeDefinitionEnumerated(
                "TEST_NAME",
                ImmutableList.<AsnSchemaNamedTag>of());
        final Object result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralizedTime()
    {
        final OLDAsnSchemaTypeDefinitionGeneralizedTime visitable
                = new OLDAsnSchemaTypeDefinitionGeneralizedTime("TEST_NAME", AsnSchemaConstraint.NULL);
        final GeneralizedTimeDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralString()
    {
        final OLDAsnSchemaTypeDefinitionGeneralString visitable
                = new OLDAsnSchemaTypeDefinitionGeneralString("TEST_NAME", AsnSchemaConstraint.NULL);
        final GeneralStringDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionIA5String()
    {
        final OLDAsnSchemaTypeDefinitionIa5String
                visitable = new OLDAsnSchemaTypeDefinitionIa5String(
                "TEST_NAME",
                AsnSchemaConstraint.NULL);
        final Ia5StringDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionInteger()
    {
        final OLDAsnSchemaTypeDefinitionInteger visitable = new OLDAsnSchemaTypeDefinitionInteger(
                "TEST_NAME",
                ImmutableList.<AsnSchemaNamedTag>of(),
                AsnSchemaConstraint.NULL);
        final IntegerDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionNumericString()
    {
        final OLDAsnSchemaTypeDefinitionNumericString visitable
                = new OLDAsnSchemaTypeDefinitionNumericString("TEST_NAME", AsnSchemaConstraint.NULL);
        final NumericStringDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionOctetString()
    {
        final OLDAsnSchemaTypeDefinitionOctetString
                visitable = new OLDAsnSchemaTypeDefinitionOctetString(
                "TEST_NAME",
                AsnSchemaConstraint.NULL);
        final OctetStringDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequence()
    {
        final OLDAsnSchemaTypeDefinitionSequence visitable = new OLDAsnSchemaTypeDefinitionSequence(
                "TEST_NAME",
                ImmutableList.<AsnSchemaComponentType>of(),
                AsnSchemaConstraint.NULL);
        final Object result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequenceOf()
    {
        final OLDAsnSchemaTypeDefinitionSequenceOf
                visitable = new OLDAsnSchemaTypeDefinitionSequenceOf(
                "TEST_NAME",
                "TEST_TYPE",
                AsnSchemaConstraint.NULL);
        final Object result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSet()
    {
        final OLDAsnSchemaTypeDefinitionSet visitable = new OLDAsnSchemaTypeDefinitionSet("TEST_NAME",
                ImmutableList.<AsnSchemaComponentType>of(),
                AsnSchemaConstraint.NULL);
        final Object result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSetOf()
    {
        final OLDAsnSchemaTypeDefinitionSetOf visitable = new OLDAsnSchemaTypeDefinitionSetOf("TEST_NAME",
                "TEST_TYPE",
                AsnSchemaConstraint.NULL);
        final Object result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionUtf8String()
    {
        final OLDAsnSchemaTypeDefinitionUtf8String
                visitable = new OLDAsnSchemaTypeDefinitionUtf8String(
                "TEST_NAME",
                AsnSchemaConstraint.NULL);
        final Utf8StringDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionVisibleString()
    {
        final OLDAsnSchemaTypeDefinitionVisibleString visitable
                = new OLDAsnSchemaTypeDefinitionVisibleString("TEST_NAME", AsnSchemaConstraint.NULL);
        final VisibleStringDecoder result = instance.visit(visitable);
        assertNotNull(result);
    }
}
