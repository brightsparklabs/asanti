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
        final AsnSchemaTypeDefinition.Null visitable = AsnSchemaTypeDefinition.NULL;
        final BuiltinTypeValidator.Null result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionBitString()
    {
        final AsnSchemaTypeDefinitionBitString visitable = new AsnSchemaTypeDefinitionBitString(
                "TEST_NAME",
                AsnSchemaConstraint.NULL);
        final BitStringValidator result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionChoice()
    {
        final AsnSchemaTypeDefinitionChoice visitable = new AsnSchemaTypeDefinitionChoice(
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
        final AsnSchemaTypeDefinitionEnumerated visitable = new AsnSchemaTypeDefinitionEnumerated(
                "TEST_NAME",
                ImmutableList.<AsnSchemaNamedTag>of());
        // TODO: ASN-113
        final Object result = instance.visit(visitable);
        assertNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralizedTime()
    {
        final AsnSchemaTypeDefinitionGeneralizedTime visitable
                = new AsnSchemaTypeDefinitionGeneralizedTime("TEST_NAME", AsnSchemaConstraint.NULL);
        final GeneralizedTimeValidator result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralString()
    {
        final AsnSchemaTypeDefinitionGeneralString visitable
                = new AsnSchemaTypeDefinitionGeneralString("TEST_NAME", AsnSchemaConstraint.NULL);
        final GeneralStringValidator result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionIA5String()
    {
        final AsnSchemaTypeDefinitionIa5String visitable = new AsnSchemaTypeDefinitionIa5String(
                "TEST_NAME",
                AsnSchemaConstraint.NULL);
        final Ia5StringValidator result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionInteger()
    {
        final AsnSchemaTypeDefinitionInteger visitable = new AsnSchemaTypeDefinitionInteger(
                "TEST_NAME",
                ImmutableList.<AsnSchemaNamedTag>of(),
                AsnSchemaConstraint.NULL);
        final IntegerValidator result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionNumericString()
    {
        final AsnSchemaTypeDefinitionNumericString visitable
                = new AsnSchemaTypeDefinitionNumericString("TEST_NAME", AsnSchemaConstraint.NULL);
        final NumericStringValidator result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionOctetString()
    {
        final AsnSchemaTypeDefinitionOctetString visitable = new AsnSchemaTypeDefinitionOctetString(
                "TEST_NAME",
                AsnSchemaConstraint.NULL);
        final OctetStringValidator result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequence()
    {
        final AsnSchemaTypeDefinitionSequence visitable = new AsnSchemaTypeDefinitionSequence(
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
        final AsnSchemaTypeDefinitionSequenceOf visitable = new AsnSchemaTypeDefinitionSequenceOf(
                "TEST_NAME",
                "TEST_TYPE",
                AsnSchemaConstraint.NULL);
        assertEquals(null, instance.visit(visitable));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSet()
    {
        final AsnSchemaTypeDefinitionSet visitable = new AsnSchemaTypeDefinitionSet("TEST_NAME",
                ImmutableList.<AsnSchemaComponentType>of(),
                AsnSchemaConstraint.NULL);
        // TODO: ASN-113
        final Object result = instance.visit(visitable);
        assertNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSetOf()
    {
        final AsnSchemaTypeDefinitionSetOf visitable = new AsnSchemaTypeDefinitionSetOf("TEST_NAME",
                "TEST_TYPE",
                AsnSchemaConstraint.NULL);
        assertEquals(null, instance.visit(visitable));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionUtf8String()
    {
        final AsnSchemaTypeDefinitionUtf8String visitable = new AsnSchemaTypeDefinitionUtf8String(
                "TEST_NAME",
                AsnSchemaConstraint.NULL);
        final Utf8StringValidator result = instance.visit(visitable);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionVisibleString()
    {
        final AsnSchemaTypeDefinitionVisibleString visitable
                = new AsnSchemaTypeDefinitionVisibleString("TEST_NAME", AsnSchemaConstraint.NULL);
        final VisibleStringValidator result = instance.visit(visitable);
        assertNotNull(result);
    }
}
