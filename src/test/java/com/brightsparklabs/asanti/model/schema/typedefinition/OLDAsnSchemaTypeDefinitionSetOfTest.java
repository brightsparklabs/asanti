/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.typedefinition;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaTypeDefinitionVisitor;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;

/**
 * Unit tests for {@link OLDAsnSchemaTypeDefinitionSetOf}
 *
 * @author brightSPARK Labs
 */
public class OLDAsnSchemaTypeDefinitionSetOfTest
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
            new OLDAsnSchemaTypeDefinitionSetOf(null, "person", AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new OLDAsnSchemaTypeDefinitionSetOf("", "person", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionSetOf(" ", "person", AsnSchemaConstraint.NULL);
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
            new OLDAsnSchemaTypeDefinitionSetOf("NAME", null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new OLDAsnSchemaTypeDefinitionSetOf("", "", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionSetOf(" ", " ", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testAsnSchemaConstructedTypeDefinition_Constraint() throws Exception
    {
        // test null
        try
        {
            new OLDAsnSchemaTypeDefinitionSetOf("NAME", "person", null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        final OLDAsnSchemaTypeDefinitionSetOf instance =
                new OLDAsnSchemaTypeDefinitionSetOf("NAME", "person", AsnSchemaConstraint.NULL);
        assertEquals(AsnBuiltinType.SetOf, instance.getBuiltinType());
    }

    @Test
    public void testVisit() throws Exception
    {
        final OLDAsnSchemaTypeDefinitionSetOf instance =
                new OLDAsnSchemaTypeDefinitionSetOf("NAME", "person", AsnSchemaConstraint.NULL);
        assertEquals(OLDAsnSchemaTypeDefinitionSetOf.class.getCanonicalName(),
                instance.visit(MockAsnSchemaTypeDefinitionVisitor.getInstance()));
    }
}
