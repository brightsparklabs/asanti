/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.mocks.MockAsnSchemaTypeDefinitionVisitor;

/**
 * Unit tests for {@link AsnSchemaTypeDefinitionUTF8String}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionUTF8Test
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinitionUTF8String() throws Exception
    {
        // test valid
        new AsnSchemaTypeDefinitionUTF8String("TEST_NAME", AsnSchemaConstraint.NULL);
        new AsnSchemaTypeDefinitionUTF8String("TEST_NAME", AsnSchemaConstraint.NULL);

        // test null
        try
        {
            new AsnSchemaTypeDefinitionUTF8String(null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionUTF8String("TEST_NAME", null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }

        // test empty
        try
        {
            new AsnSchemaTypeDefinitionUTF8String("", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionUTF8String(" ", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        final AsnSchemaTypeDefinitionUTF8String instance =
                new AsnSchemaTypeDefinitionUTF8String("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals(AsnBuiltinType.UTF8String, instance.getBuiltinType());
    }

    @Test
    public void testVisit() throws Exception
    {
        final AsnSchemaTypeDefinitionUTF8String instance =
                new AsnSchemaTypeDefinitionUTF8String("TEST_NAME", AsnSchemaConstraint.NULL);
        assertEquals("com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionUTF8String",
                instance.visit(MockAsnSchemaTypeDefinitionVisitor.getInstance()));
    }
}
