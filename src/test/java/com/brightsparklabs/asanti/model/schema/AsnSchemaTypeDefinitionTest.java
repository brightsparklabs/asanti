/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;

import org.junit.Test;

public class AsnSchemaTypeDefinitionTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinition() throws Exception
    {
        // test null
        try
        {
            new AsnSchemaTypeDefinition(null, AsnBuiltinType.Null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinition("TYPE_NAME", null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new AsnSchemaTypeDefinition("", AsnBuiltinType.Null);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinition(" ", AsnBuiltinType.Null);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetName() throws Exception
    {
        final AsnSchemaTypeDefinition instance = new AsnSchemaTypeDefinition("NAME", AsnBuiltinType.Set);
        assertEquals("NAME", instance.getName());
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        for (final AsnBuiltinType builtinType : AsnBuiltinType.values())
        {
            final AsnSchemaTypeDefinition instance = new AsnSchemaTypeDefinition("NAME", builtinType);
            assertEquals(builtinType, instance.getBuiltinType());
        }
    }

    @Test
    public void testGetTagName() throws Exception
    {
        final AsnSchemaTypeDefinition instance = new AsnSchemaTypeDefinition("NAME", AsnBuiltinType.Set);
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTagName(" "));
        assertEquals("", instance.getTagName("0"));
        assertEquals("", instance.getTagName("NAME"));
        assertEquals("", instance.getTagName("SET"));
    }

    @Test
    public void testGetTypeName() throws Exception
    {
        final AsnSchemaTypeDefinition instance = new AsnSchemaTypeDefinition("NAME", AsnBuiltinType.Set);
        assertEquals("", instance.getTypeName(""));
        assertEquals("", instance.getTypeName(" "));
        assertEquals("", instance.getTypeName("0"));
        assertEquals("", instance.getTypeName("NAME"));
        assertEquals("", instance.getTypeName("SET"));
    }
}