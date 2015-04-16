/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link AsnSchemaTypeDefinitionPrimitiveParser}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionPrimitiveParserTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testParseIA5String() throws Exception
    {
        // null name
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseIA5String(null, "TEST_CONSTRAINTS");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // blank name
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseIA5String("", "TEST_CONSTRAINTS");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseIA5String(" ", "TEST_CONSTRAINTS");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

        final AsnSchemaTypeDefinition instance =
                AsnSchemaTypeDefinitionPrimitiveParser.parseIA5String("TEST_NAME", "TEST_CONSTRAINTS");
        assertEquals(AsnBuiltinType.Ia5String, instance.getBuiltinType());
        assertEquals("TEST_NAME", instance.getName());
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTypeName(""));
    }

    @Test
    public void testParseOctetString() throws Exception
    {
        // null name
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseOctetString(null, "TEST_CONSTRAINTS");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // blank name
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseOctetString("", "TEST_CONSTRAINTS");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseOctetString(" ", "TEST_CONSTRAINTS");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

        final AsnSchemaTypeDefinition instance =
                AsnSchemaTypeDefinitionPrimitiveParser.parseOctetString("TEST_NAME", "TEST_CONSTRAINTS");
        assertEquals(AsnBuiltinType.OctetString, instance.getBuiltinType());
        assertEquals("TEST_NAME", instance.getName());
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTypeName(""));
    }

    @Test
    public void testParseBitString() throws Exception
    {
        // null name
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseBitString(null, "TEST_CONSTRAINTS");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // blank name
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseBitString("", "TEST_CONSTRAINTS");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseBitString(" ", "TEST_CONSTRAINTS");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

        final AsnSchemaTypeDefinition instance =
                AsnSchemaTypeDefinitionPrimitiveParser.parseBitString("TEST_NAME", "TEST_CONSTRAINTS");
        assertEquals(AsnBuiltinType.BitString, instance.getBuiltinType());
        assertEquals("TEST_NAME", instance.getName());
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTypeName(""));
    }

    @Test
    public void testParseNumericString() throws Exception
    {
        // null name
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseNumericString(null, "TEST_CONSTRAINTS");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // blank name
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseNumericString("", "TEST_CONSTRAINTS");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseNumericString(" ", "TEST_CONSTRAINTS");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

        final AsnSchemaTypeDefinition instance =
                AsnSchemaTypeDefinitionPrimitiveParser.parseNumericString("TEST_NAME", "TEST_CONSTRAINTS");
        assertEquals(AsnBuiltinType.NumericString, instance.getBuiltinType());
        assertEquals("TEST_NAME", instance.getName());
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTypeName(""));
    }

    @Test
    public void testParseVisibleString() throws Exception
    {
        // null name
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseVisibleString(null, "TEST_CONSTRAINTS");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // blank name
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseVisibleString("", "TEST_CONSTRAINTS");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseVisibleString(" ", "TEST_CONSTRAINTS");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

        final AsnSchemaTypeDefinition instance =
                AsnSchemaTypeDefinitionPrimitiveParser.parseVisibleString("TEST_NAME", "TEST_CONSTRAINTS");
        assertEquals(AsnBuiltinType.VisibleString, instance.getBuiltinType());
        assertEquals("TEST_NAME", instance.getName());
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTypeName(""));
    }

    @Test
    public void testParseGeneralString() throws Exception
    {
        // null name
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseGeneralString(null, "TEST_CONSTRAINTS");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // blank name
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseGeneralString("", "TEST_CONSTRAINTS");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseGeneralString(" ", "TEST_CONSTRAINTS");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

        final AsnSchemaTypeDefinition instance
                = AsnSchemaTypeDefinitionPrimitiveParser.parseGeneralString("TEST_NAME",
                "TEST_CONSTRAINTS");
        assertEquals(AsnBuiltinType.GeneralString, instance.getBuiltinType());
        assertEquals("TEST_NAME", instance.getName());
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTypeName(""));
    }

    @Test
    public void testParseInteger() throws Exception
    {
        // null name
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseInteger(null, ImmutableList.<AsnSchemaNamedTag>of(), "TEST_CONSTRAINTS");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // null distinguished values
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseInteger(null, null, "TEST_CONSTRAINTS");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // blank name
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseInteger("", ImmutableList.<AsnSchemaNamedTag>of(), "TEST_CONSTRAINTS");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parseInteger(" ", ImmutableList.<AsnSchemaNamedTag>of(), "TEST_CONSTRAINTS");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

        final AsnSchemaTypeDefinition instance =
                AsnSchemaTypeDefinitionPrimitiveParser.parseInteger("TEST_NAME", ImmutableList.<AsnSchemaNamedTag>of(), "TEST_CONSTRAINTS");
        assertEquals(AsnBuiltinType.Integer, instance.getBuiltinType());
        assertEquals("TEST_NAME", instance.getName());
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTypeName(""));
    }
}
