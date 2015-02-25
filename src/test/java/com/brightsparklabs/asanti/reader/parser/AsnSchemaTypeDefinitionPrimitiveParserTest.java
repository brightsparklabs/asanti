/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.reader.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;

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
        assertEquals(AsnBuiltinType.IA5String, instance.getBuiltinType());
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
}
