/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.typedefinition;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Units tests for {@link AsnSchemaComponentTypeGenerated}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaComponentTypeGeneratedTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testGetTypeDefinitionText() throws Exception
    {
        final AsnSchemaComponentTypeGenerated instance = new AsnSchemaComponentTypeGenerated(
                "TAG_NAME",
                "TAG",
                "TYPE",
                "TYPE_DEFINITION_TEXT",
                true);

        assertEquals("TYPE_DEFINITION_TEXT", instance.getTypeDefinitionText());

        // test null
        try
        {
            new AsnSchemaComponentTypeGenerated("TAG_NAME", "TAG", "TYPE", null, true);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test empty
        try
        {
            new AsnSchemaComponentTypeGenerated("TAG_NAME", "TAG", "TYPE", "", true);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaComponentTypeGenerated("TAG_NAME", "TAG", "TYPE", " ", true);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }
}
