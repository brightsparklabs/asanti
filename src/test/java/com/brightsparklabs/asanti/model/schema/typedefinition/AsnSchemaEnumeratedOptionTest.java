/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.typedefinition;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaEnumeratedOption;

/**
 * Units tests for {@link AsnSchemaEnumeratedOption}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaEnumeratedOptionTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaEnumeratedOption_Name() throws Exception
    {
        // test null
        try
        {
            new AsnSchemaEnumeratedOption(null, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new AsnSchemaEnumeratedOption("", null);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaEnumeratedOption(" ", null);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetTagName() throws Exception
    {
        final AsnSchemaEnumeratedOption instance = new AsnSchemaEnumeratedOption("TAG_NAME", "0");
        assertEquals("TAG_NAME", instance.getTagName());
    }

    @Test
    public void testGetTag() throws Exception
    {
        AsnSchemaEnumeratedOption instance = new AsnSchemaEnumeratedOption("TAG_NAME", "0");
        assertEquals("0", instance.getTag());

        // test null
        instance = new AsnSchemaEnumeratedOption("TAG_NAME", null);
        assertEquals("", instance.getTag());

        // test blank
        instance = new AsnSchemaEnumeratedOption("TAG_NAME", "");
        assertEquals("", instance.getTag());
        instance = new AsnSchemaEnumeratedOption("TAG_NAME", " ");
        assertEquals("", instance.getTag());
    }
}
