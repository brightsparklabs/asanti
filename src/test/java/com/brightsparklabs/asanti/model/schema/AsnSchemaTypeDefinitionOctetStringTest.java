/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link AsnSchemaTypeDefinitionOctetString}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionOctetStringTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinitionOctetString() throws Exception
    {
        // test valid
        new AsnSchemaTypeDefinitionOctetString("TEST_NAME", "SIZE(1)");
        new AsnSchemaTypeDefinitionOctetString("TEST_NAME", "");
        new AsnSchemaTypeDefinitionOctetString("TEST_NAME", null);

        // test null
        try
        {
            new AsnSchemaTypeDefinitionOctetString(null, "SIZE(1)");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test empty
        try
        {
            new AsnSchemaTypeDefinitionOctetString("", "SIZE(1)");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionOctetString(" ", "SIZE(1)");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        final AsnSchemaTypeDefinitionOctetString instance =
                new AsnSchemaTypeDefinitionOctetString("TEST_NAME", "SIZE(1)");
        assertEquals(AsnBuiltinType.OctetString, instance.getBuiltinType());
    }
}
