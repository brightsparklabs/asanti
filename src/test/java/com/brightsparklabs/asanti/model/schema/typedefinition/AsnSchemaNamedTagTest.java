/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.typedefinition;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link AsnSchemaNamedTag}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaNamedTagTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaNamedTag_Name() throws Exception
    {
        // test null
        try
        {
            new AsnSchemaNamedTag(null, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new AsnSchemaNamedTag("", null);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaNamedTag(" ", null);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetTagName() throws Exception
    {
        final AsnSchemaNamedTag instance = new AsnSchemaNamedTag("TAG_NAME", "0");
        assertEquals("TAG_NAME", instance.getTagName());
    }

    @Test
    public void testGetTag() throws Exception
    {
        AsnSchemaNamedTag instance = new AsnSchemaNamedTag("TAG_NAME", "0");
        assertEquals("0", instance.getTag());

        // test null
        instance = new AsnSchemaNamedTag("TAG_NAME", null);
        assertEquals("", instance.getTag());

        // test blank
        instance = new AsnSchemaNamedTag("TAG_NAME", "");
        assertEquals("", instance.getTag());
        instance = new AsnSchemaNamedTag("TAG_NAME", " ");
        assertEquals("", instance.getTag());
    }
}
