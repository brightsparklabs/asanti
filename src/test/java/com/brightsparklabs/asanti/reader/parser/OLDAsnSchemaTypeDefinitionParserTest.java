/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.typedefinition.OLDAsnSchemaTypeDefinition;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link AsnSchemaTypeDefinitionParser}
 *
 * @author brightSPARK Labs
 */
public class OLDAsnSchemaTypeDefinitionParserTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testParse() throws Exception
    {
        // null name
        try
        {
            AsnSchemaTypeDefinitionParser.parse(null, "(SIZE (10))");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // blank name
        try
        {
            AsnSchemaTypeDefinitionParser.parse("", "(SIZE (10))");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
        try
        {
            AsnSchemaTypeDefinitionParser.parse(" ", "(SIZE (10))");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // blank value
        try
        {
            AsnSchemaTypeDefinitionParser.parse("TEST_NAME", "");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

    }

    @Test
    public void testParseIa5String() throws Exception
    {
        final ImmutableList<AsnSchemaTypeDefinition> result = AsnSchemaTypeDefinitionParser.parse(
                "TEST_NAME",
                "IA5String (SIZE (10))");
        assertEquals(1, result.size());
        final AsnSchemaTypeDefinition instance = result.get(0);
        assertEquals(AsnBuiltinType.Ia5String, instance.getType().getPrimitiveType().getBuiltinType());
        assertEquals("TEST_NAME", instance.getName());
    }

    @Test
    public void testParseOctetString() throws Exception
    {
        final ImmutableList<AsnSchemaTypeDefinition> result = AsnSchemaTypeDefinitionParser.parse(
                "TEST_NAME",
                "OCTET STRING (SIZE (10))");
        assertEquals(1, result.size());
        final AsnSchemaTypeDefinition instance = result.get(0);
        assertEquals(AsnBuiltinType.OctetString, instance.getType().getPrimitiveType().getBuiltinType());
        assertEquals("TEST_NAME", instance.getName());
    }

    @Test
    public void testParseBitString() throws Exception
    {
        final ImmutableList<AsnSchemaTypeDefinition> result = AsnSchemaTypeDefinitionParser.parse(
                "TEST_NAME",
                "BIT STRING (SIZE (10))");
        assertEquals(1, result.size());
        final AsnSchemaTypeDefinition instance = result.get(0);
        assertEquals(AsnBuiltinType.BitString, instance.getType().getPrimitiveType().getBuiltinType());
        assertEquals("TEST_NAME", instance.getName());
    }

    @Test
    public void testParseNumericString() throws Exception
    {
        final ImmutableList<AsnSchemaTypeDefinition> result = AsnSchemaTypeDefinitionParser.parse(
                "TEST_NAME",
                "NumericString (SIZE (10))");
        assertEquals(1, result.size());
        final AsnSchemaTypeDefinition instance = result.get(0);
        assertEquals(AsnBuiltinType.NumericString, instance.getType().getPrimitiveType().getBuiltinType());
        assertEquals("TEST_NAME", instance.getName());
    }

    @Test
    public void testParseVisibleString() throws Exception
    {
        final ImmutableList<AsnSchemaTypeDefinition> result = AsnSchemaTypeDefinitionParser.parse(
                "TEST_NAME",
                "VisibleString (SIZE (10))");
        final AsnSchemaTypeDefinition instance = result.get(0);
        assertEquals(1, result.size());
        assertEquals(AsnBuiltinType.VisibleString, instance.getType().getPrimitiveType().getBuiltinType());
        assertEquals("TEST_NAME", instance.getName());
    }

    @Test
    public void testParseGeneralString() throws Exception
    {
        final ImmutableList<AsnSchemaTypeDefinition> result = AsnSchemaTypeDefinitionParser.parse(
                "TEST_NAME",
                "GeneralString (SIZE (10))");
        final AsnSchemaTypeDefinition instance = result.get(0);
        assertEquals(1, result.size());
        assertEquals(AsnBuiltinType.GeneralString, instance.getType().getPrimitiveType().getBuiltinType());
        assertEquals("TEST_NAME", instance.getName());
        assertEquals("TEST_NAME", instance.getName());
    }

    @Test
    public void testParseInteger() throws Exception
    {
        final ImmutableList<AsnSchemaTypeDefinition> result = AsnSchemaTypeDefinitionParser.parse(
                "TEST_NAME",
                "INTEGER (SIZE (10))");
        assertEquals(1, result.size());
        final AsnSchemaTypeDefinition instance = result.get(0);
        assertEquals(AsnBuiltinType.Integer, instance.getType().getPrimitiveType().getBuiltinType());
        assertEquals("TEST_NAME", instance.getName());
        assertEquals("TEST_NAME", instance.getName());
    }
}
