/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
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
        final BuiltinTypeValidator.Null result = instance.visit(AsnPrimitiveType.NULL);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionBitString()
    {
        final BitStringValidator result = instance.visit(AsnPrimitiveType.BIT_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionChoice()
    {
        // TODO: ASN-113
        final Object result = instance.visit(AsnPrimitiveType.CHOICE);
        assertEquals(BuiltinTypeValidator.NULL, result);
        //assertNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionEnumerated()
    {
        // TODO: ASN-113
        final Object result = instance.visit(AsnPrimitiveType.ENUMERATED);
        assertEquals(BuiltinTypeValidator.NULL, result);
        //assertNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralizedTime()
    {
        final GeneralizedTimeValidator result = instance.visit(AsnPrimitiveType.GENERALIZED_TIME);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralString()
    {
        final GeneralStringValidator result = instance.visit(AsnPrimitiveType.GENERAL_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionIA5String()
    {
        final Ia5StringValidator result = instance.visit(AsnPrimitiveType.IA5_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionInteger()
    {
        final IntegerValidator result = instance.visit(AsnPrimitiveType.INTEGER);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionNumericString()
    {
        final NumericStringValidator result = instance.visit(AsnPrimitiveType.NUMERIC_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionOctetString()
    {

        final OctetStringValidator result = instance.visit(AsnPrimitiveType.OCTET_STRING);
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

        assertEquals(null, instance.visit(AsnPrimitiveType.SEQUENCE_OF));
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

        assertEquals(null, instance.visit(AsnPrimitiveType.SET_OF));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionUtf8String()
    {
        final Utf8StringValidator result = instance.visit(AsnPrimitiveType.UTF8_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionVisibleString()
    {
        final VisibleStringValidator result = instance.visit(AsnPrimitiveType.VISIBLE_STRING);
        assertNotNull(result);
    }
}
