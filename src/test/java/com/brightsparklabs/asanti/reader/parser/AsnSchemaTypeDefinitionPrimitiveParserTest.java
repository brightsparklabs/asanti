/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.reader.parser;

import static org.junit.Assert.*;

import java.text.ParseException;

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
    public void testParse() throws Exception
    {
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parse("TEST_NAME", "", "TEST_CONSTRAINTS");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
    }

    @Test
    public void testParse_OctetString() throws Exception
    {
        // null name
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parse(null, "OCTET STRING", "TEST_CONSTRAINTS");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // blank name
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parse("", "OCTET STRING", "TEST_CONSTRAINTS");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            AsnSchemaTypeDefinitionPrimitiveParser.parse(" ", "OCTET STRING", "TEST_CONSTRAINTS");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

        final AsnSchemaTypeDefinition instance =
                AsnSchemaTypeDefinitionPrimitiveParser.parse("TEST_NAME", "OCTET STRING", "TEST_CONSTRAINTS");
        assertEquals(AsnBuiltinType.OctetString, instance.getBuiltinType());
        assertEquals("TEST_NAME", instance.getName());
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTypeName(""));
    }
}
