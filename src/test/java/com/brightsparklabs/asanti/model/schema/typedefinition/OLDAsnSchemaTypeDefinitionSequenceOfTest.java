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
 * Unit tests for {@link OLDAsnSchemaTypeDefinitionSequenceOf}
 *
 * @author brightSPARK Labs
 */
public class OLDAsnSchemaTypeDefinitionSequenceOfTest
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
            new OLDAsnSchemaTypeDefinitionSequenceOf(null, "person", AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new OLDAsnSchemaTypeDefinitionSequenceOf("", "person", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionSequenceOf(" ", "person", AsnSchemaConstraint.NULL);
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
            new OLDAsnSchemaTypeDefinitionSequenceOf("NAME", null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new OLDAsnSchemaTypeDefinitionSequenceOf("", "", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionSequenceOf(" ", " ", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        final OLDAsnSchemaTypeDefinitionSequenceOf instance =
                new OLDAsnSchemaTypeDefinitionSequenceOf("TEST_NAME", "person", AsnSchemaConstraint.NULL);
        assertEquals(AsnBuiltinType.SequenceOf, instance.getBuiltinType());
    }

    @Test
    public void testAsnSchemaConstructedTypeDefinition_Constraint() throws Exception
    {
        // test null
        try
        {
            new OLDAsnSchemaTypeDefinitionSequenceOf("NAME", "person", null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }
    }

    @Test
    public void testVisit() throws Exception
    {
        final OLDAsnSchemaTypeDefinitionSequenceOf instance =
                new OLDAsnSchemaTypeDefinitionSequenceOf("TEST_NAME", "person", AsnSchemaConstraint.NULL);
        assertEquals(OLDAsnSchemaTypeDefinitionSequenceOf.class.getCanonicalName(),
                instance.visit(MockAsnSchemaTypeDefinitionVisitor.getInstance()));
    }
}
