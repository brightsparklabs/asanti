/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;

/**
 * Unit tests for {@link AsnSchemaTypeDefinitionNumericString}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionNumericStringTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinitionNumericString() throws Exception
    {
        // test valid
        new AsnSchemaTypeDefinitionNumericString("TEST_NAME", AsnSchemaConstraint.NULL);

        // test null
        try
        {
            new AsnSchemaTypeDefinitionNumericString(null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionNumericString("TEST_NAME", null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }

        // test empty
        try
        {
            new AsnSchemaTypeDefinitionNumericString("", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionNumericString(" ", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        final AsnSchemaTypeDefinitionNumericString instance =
                new AsnSchemaTypeDefinitionNumericString("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(AsnBuiltinType.NumericString, instance.getBuiltinType());
    }
}
