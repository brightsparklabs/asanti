/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.typedefinition;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import org.junit.Test;

import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;

/**
 * Units tests for {@link AsnSchemaComponentType}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaComponentTypeTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testGetTagName() throws Exception
    {
        final AsnSchemaComponentType instance = new AsnSchemaComponentType("TAG_NAME", "TAG", "TYPE", true, AsnSchemaType.NULL);
        assertEquals("TAG_NAME", instance.getTagName());

        // test null
        try
        {
            new AsnSchemaComponentType(null, "TAG", "TYPE", true, AsnSchemaType.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test empty
        try
        {
            new AsnSchemaComponentType("", "TAG", "TYPE", true, AsnSchemaType.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaComponentType(" ", "TAG", "TYPE", true, AsnSchemaType.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetTag() throws Exception
    {
        AsnSchemaComponentType instance = new AsnSchemaComponentType("TAG_NAME", "TAG", "TYPE_NAME", true,
                AsnSchemaType.NULL);
        assertEquals("TAG", instance.getTag());

        // test null
        instance = new AsnSchemaComponentType("TAG_NAME", null, "TYPE_NAME", true, AsnSchemaType.NULL);
        assertEquals("", instance.getTag());

        // test empty
        instance = new AsnSchemaComponentType("TAG_NAME", "", "TYPE_NAME", true, AsnSchemaType.NULL);
        assertEquals("", instance.getTag());
    }

    @Test
    public void testGetTypeName() throws Exception
    {
        final AsnSchemaComponentType instance = new AsnSchemaComponentType("TAG_NAME", "TAG", "TYPE_NAME", true, AsnSchemaType.NULL);
        assertEquals("TYPE_NAME", instance.getTypeName());

        // test null
        try
        {
            new AsnSchemaComponentType("TAG_NAME", "TAG", null, true, AsnSchemaType.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        try
        {
            new AsnSchemaComponentType("TAG_NAME", "TAG", "TYPE_NAME", true, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test empty
        try
        {
            new AsnSchemaComponentType("TAG_NAME", "TAG", "", true, AsnSchemaType.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

        try
        {
            new AsnSchemaComponentType("TAG_NAME", "TAG", " ", true, AsnSchemaType.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testIsOptional() throws Exception
    {
        AsnSchemaComponentType instance = new AsnSchemaComponentType("TAG_NAME", "TAG", "TYPE_NAME", true, AsnSchemaType.NULL);
        assertEquals(true, instance.isOptional());

        instance = new AsnSchemaComponentType("TAG_NAME", "TAG", "TYPE_NAME", false, AsnSchemaType.NULL);
        assertEquals(false, instance.isOptional());
    }
}
