/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link AsnSchemaTypeDefinitionInteger}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionIntegerTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinitionInteger() throws Exception
    {
        // test valid
        new AsnSchemaTypeDefinitionInteger("TEST_NAME", AsnSchemaConstraint.NULL);

        // test null
        try
        {
            new AsnSchemaTypeDefinitionInteger(null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionInteger("TEST_NAME", null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }

        // test empty
        try
        {
            new AsnSchemaTypeDefinitionInteger("", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionInteger(" ", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        final AsnSchemaTypeDefinitionInteger instance =
                new AsnSchemaTypeDefinitionInteger("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(AsnBuiltinType.Integer, instance.getBuiltinType());
    }
}
