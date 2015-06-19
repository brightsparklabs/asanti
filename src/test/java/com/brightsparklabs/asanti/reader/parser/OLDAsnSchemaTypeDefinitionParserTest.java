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


    // TODO MJF - move most of tese to a new test TypeParser, and then just have a few simple
    /// cases here to ensure we get a type and a name.

    @Test
    public void testParse() throws Exception
    {
        // null name
        try
        {
            AsnSchemaTypeDefinitionParser.parse(null, "SomeType");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // blank name
        try
        {
            AsnSchemaTypeDefinitionParser.parse("", "SomeType");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
        try
        {
            AsnSchemaTypeDefinitionParser.parse(" ", "SomeType");
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
        // nulll value
        try
        {
            AsnSchemaTypeDefinitionParser.parse("TEST_NAME", null);
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

    }
    // TODO MJF - chnage these to test the different main level parses, Collection, Constructed etc...

    @Test
    public void testParseIa5String() throws Exception
    {
        final AsnSchemaTypeDefinition result = AsnSchemaTypeDefinitionParser.parse(
                "TEST_NAME",
                "IA5String (SIZE (10))");
        assertEquals(AsnBuiltinType.Ia5String, result.getType().getPrimitiveType().getBuiltinType());
        assertEquals("TEST_NAME", result.getName());
    }

    @Test
    public void testParseOctetString() throws Exception
    {
        final AsnSchemaTypeDefinition result = AsnSchemaTypeDefinitionParser.parse(
                "TEST_NAME",
                "OCTET STRING (SIZE (10))");
        assertEquals(AsnBuiltinType.OctetString, result.getType().getPrimitiveType().getBuiltinType());
        assertEquals("TEST_NAME", result.getName());
    }

    @Test
    public void testParseBitString() throws Exception
    {
        final AsnSchemaTypeDefinition result = AsnSchemaTypeDefinitionParser.parse(
                "TEST_NAME",
                "BIT STRING (SIZE (10))");
        assertEquals(AsnBuiltinType.BitString, result.getType().getPrimitiveType().getBuiltinType());
        assertEquals("TEST_NAME", result.getName());
    }

    @Test
    public void testParseNumericString() throws Exception
    {
        final AsnSchemaTypeDefinition result = AsnSchemaTypeDefinitionParser.parse(
                "TEST_NAME",
                "NumericString (SIZE (10))");
        assertEquals(AsnBuiltinType.NumericString, result.getType().getPrimitiveType().getBuiltinType());
        assertEquals("TEST_NAME", result.getName());
    }

    @Test
    public void testParseVisibleString() throws Exception
    {
        final AsnSchemaTypeDefinition result = AsnSchemaTypeDefinitionParser.parse(
                "TEST_NAME",
                "VisibleString (SIZE (10))");
        assertEquals(AsnBuiltinType.VisibleString, result.getType().getPrimitiveType().getBuiltinType());
        assertEquals("TEST_NAME", result.getName());
    }

    @Test
    public void testParseGeneralString() throws Exception
    {
        final AsnSchemaTypeDefinition result = AsnSchemaTypeDefinitionParser.parse(
                "TEST_NAME",
                "GeneralString (SIZE (10))");
        assertEquals(AsnBuiltinType.GeneralString, result.getType().getPrimitiveType().getBuiltinType());
        assertEquals("TEST_NAME", result.getName());
    }

    @Test
    public void testParseInteger() throws Exception
    {
        final AsnSchemaTypeDefinition result = AsnSchemaTypeDefinitionParser.parse(
                "TEST_NAME",
                "INTEGER (SIZE (10))");
        assertEquals(AsnBuiltinType.Integer, result.getType().getPrimitiveType().getBuiltinType());
        assertEquals("TEST_NAME", result.getName());
    }
}
