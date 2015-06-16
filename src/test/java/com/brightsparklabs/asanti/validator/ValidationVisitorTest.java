/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;
import com.brightsparklabs.asanti.validator.builtin.*;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link ValidationVisitor}
 *
 * @author brightSPARK Labs
 */
public class ValidationVisitorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final ValidationVisitor instance = new ValidationVisitor();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testVisitAsnSchemaTypeDefinitionNull()
    {
        final OLDAsnSchemaTypeDefinition.Null visitable = OLDAsnSchemaTypeDefinition.NULL;
        final BuiltinTypeValidator.Null result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionBitString()
    {
        final OLDAsnSchemaTypeDefinitionBitString
                visitable = new OLDAsnSchemaTypeDefinitionBitString(
                "TEST_NAME",
                AsnSchemaConstraint.NULL);
        final BitStringValidator result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionChoice()
    {
        final OLDAsnSchemaTypeDefinitionChoice visitable = new OLDAsnSchemaTypeDefinitionChoice(
                "TEST_NAME",
                ImmutableList.<AsnSchemaComponentType>of(),
                AsnSchemaConstraint.NULL);
        // TODO: ASN-113
        final Object result = instance.visit(visitable);
        assertNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionEnumerated()
    {
        final OLDAsnSchemaTypeDefinitionEnumerated
                visitable = new OLDAsnSchemaTypeDefinitionEnumerated(
                "TEST_NAME",
                ImmutableList.<AsnSchemaNamedTag>of());
        // TODO: ASN-113
        final Object result = instance.visit(visitable);
        assertNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralizedTime()
    {
        final OLDAsnSchemaTypeDefinitionGeneralizedTime visitable
                = new OLDAsnSchemaTypeDefinitionGeneralizedTime("TEST_NAME", AsnSchemaConstraint.NULL);
        final GeneralizedTimeValidator result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralString()
    {
        final OLDAsnSchemaTypeDefinitionGeneralString visitable
                = new OLDAsnSchemaTypeDefinitionGeneralString("TEST_NAME", AsnSchemaConstraint.NULL);
        final GeneralStringValidator result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionIA5String()
    {
        final OLDAsnSchemaTypeDefinitionIa5String
                visitable = new OLDAsnSchemaTypeDefinitionIa5String(
                "TEST_NAME",
                AsnSchemaConstraint.NULL);
        final Ia5StringValidator result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionInteger()
    {
        final OLDAsnSchemaTypeDefinitionInteger visitable = new OLDAsnSchemaTypeDefinitionInteger(
                "TEST_NAME",
                ImmutableList.<AsnSchemaNamedTag>of(),
                AsnSchemaConstraint.NULL);
        final IntegerValidator result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionNumericString()
    {
        final OLDAsnSchemaTypeDefinitionNumericString visitable
                = new OLDAsnSchemaTypeDefinitionNumericString("TEST_NAME", AsnSchemaConstraint.NULL);
        final NumericStringValidator result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionOctetString()
    {
        final OLDAsnSchemaTypeDefinitionOctetString
                visitable = new OLDAsnSchemaTypeDefinitionOctetString(
                "TEST_NAME",
                AsnSchemaConstraint.NULL);
        final OctetStringValidator result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequence()
    {
        final OLDAsnSchemaTypeDefinitionSequence visitable = new OLDAsnSchemaTypeDefinitionSequence(
                "TEST_NAME",
                ImmutableList.<AsnSchemaComponentType>of(),
                AsnSchemaConstraint.NULL);
        // TODO: ASN-113
        final Object result = instance.visit(visitable);
        assertNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequenceOf()
    {
        final OLDAsnSchemaTypeDefinitionSequenceOf
                visitable = new OLDAsnSchemaTypeDefinitionSequenceOf(
                "TEST_NAME",
                "TEST_TYPE",
                AsnSchemaConstraint.NULL);
        assertEquals(null, instance.visit(visitable));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSet()
    {
        final OLDAsnSchemaTypeDefinitionSet visitable = new OLDAsnSchemaTypeDefinitionSet("TEST_NAME",
                ImmutableList.<AsnSchemaComponentType>of(),
                AsnSchemaConstraint.NULL);
        // TODO: ASN-113
        final Object result = instance.visit(visitable);
        assertNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSetOf()
    {
        final OLDAsnSchemaTypeDefinitionSetOf visitable = new OLDAsnSchemaTypeDefinitionSetOf("TEST_NAME",
                "TEST_TYPE",
                AsnSchemaConstraint.NULL);
        assertEquals(null, instance.visit(visitable));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionUtf8String()
    {
        final OLDAsnSchemaTypeDefinitionUtf8String
                visitable = new OLDAsnSchemaTypeDefinitionUtf8String(
                "TEST_NAME",
                AsnSchemaConstraint.NULL);
        final Utf8StringValidator result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionVisibleString()
    {
        final OLDAsnSchemaTypeDefinitionVisibleString visitable
                = new OLDAsnSchemaTypeDefinitionVisibleString("TEST_NAME", AsnSchemaConstraint.NULL);
        final VisibleStringValidator result = instance.visit(visitable);
        assertNotNull(result);
    }
}
