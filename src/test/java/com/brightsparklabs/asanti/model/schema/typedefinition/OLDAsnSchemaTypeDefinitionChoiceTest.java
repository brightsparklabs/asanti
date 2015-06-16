/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.typedefinition;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaComponentType;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaTypeDefinitionVisitor;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.google.common.collect.ImmutableList;

/**
 * Unit tests for {@link OLDAsnSchemaTypeDefinitionChoice}
 *
 * @author brightSPARK Labs
 */
public class OLDAsnSchemaTypeDefinitionChoiceTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinitionChoice() throws Exception
    {
        final ImmutableList<AsnSchemaComponentType> componentTypes =
                MockAsnSchemaComponentType.createMockedAsnSchemaComponentTypesForBody();

        // test null
        try
        {
            new OLDAsnSchemaTypeDefinitionChoice(null, componentTypes, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionChoice("TEST", null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionChoice("TEST", componentTypes, null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }

        // test blank
        try
        {
            new OLDAsnSchemaTypeDefinitionChoice("", componentTypes, AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionChoice(" ", componentTypes, AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        final ImmutableList<AsnSchemaComponentType> componentTypes =
                MockAsnSchemaComponentType.createMockedAsnSchemaComponentTypesForBody();
        final OLDAsnSchemaTypeDefinitionChoice instance =
                new OLDAsnSchemaTypeDefinitionChoice("Test", componentTypes, AsnSchemaConstraint.NULL);
        assertEquals(AsnBuiltinType.Choice, instance.getBuiltinType());
    }

    @Test
    public void testVisit() throws Exception
    {
        final ImmutableList<AsnSchemaComponentType> componentTypes =
                MockAsnSchemaComponentType.createMockedAsnSchemaComponentTypesForBody();
        final OLDAsnSchemaTypeDefinitionChoice instance =
                new OLDAsnSchemaTypeDefinitionChoice("Test", componentTypes, AsnSchemaConstraint.NULL);
        assertEquals(OLDAsnSchemaTypeDefinitionChoice.class.getCanonicalName(),
                instance.visit(MockAsnSchemaTypeDefinitionVisitor.getInstance()));
    }
}
