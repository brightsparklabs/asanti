/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.mocks.MockAsnSchemaTypeDefinitionVisitor;

/**
 * Unit tests for {@link AsnSchemaTypeDefinitionSequenceOf}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionSequenceOfTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaConstructedTypeDefinition_Name() throws Exception
    {
        // test null
        try
        {
            new AsnSchemaTypeDefinitionSequenceOf(null, "person", AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new AsnSchemaTypeDefinitionSequenceOf("", "person", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionSequenceOf(" ", "person", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testAsnSchemaConstructedTypeDefinition_ElementTypeName() throws Exception
    {
        // test null
        try
        {
            new AsnSchemaTypeDefinitionSequenceOf("NAME", null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new AsnSchemaTypeDefinitionSequenceOf("", "", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionSequenceOf(" ", " ", AsnSchemaConstraint.NULL);
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
    public void testAsnSchemaConstructedTypeDefinition_Constraint() throws Exception
    {
        // test null
        try
        {
            new AsnSchemaTypeDefinitionSequenceOf("NAME", "person", null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }
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
