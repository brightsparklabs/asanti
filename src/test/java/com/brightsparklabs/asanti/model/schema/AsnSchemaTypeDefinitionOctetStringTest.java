/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaTypeDefinitionVisitor;

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
        new AsnSchemaTypeDefinitionOctetString("TEST_NAME", AsnSchemaConstraint.NULL);
        new AsnSchemaTypeDefinitionOctetString("TEST_NAME", AsnSchemaConstraint.NULL);

        // test null
        try
        {
            new AsnSchemaTypeDefinitionOctetString(null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionOctetString("TEST_NAME", null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }

        // test empty
        try
        {
            new AsnSchemaTypeDefinitionOctetString("", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionOctetString(" ", AsnSchemaConstraint.NULL);
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
                new AsnSchemaTypeDefinitionOctetString("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(AsnBuiltinType.OctetString, instance.getBuiltinType());
    }

    @Test
    public void testVisit() throws Exception
    {
        final AsnSchemaTypeDefinitionOctetString instance =
                new AsnSchemaTypeDefinitionOctetString("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals("com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionOctetString",
                instance.visit(MockAsnSchemaTypeDefinitionVisitor.getInstance()));
    }
}
