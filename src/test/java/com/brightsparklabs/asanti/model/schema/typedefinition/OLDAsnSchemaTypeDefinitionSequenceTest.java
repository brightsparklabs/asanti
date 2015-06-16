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
 * Unit tests for {@link OLDAsnSchemaTypeDefinitionSequence}
 *
 * @author brightSPARK Labs
 */
public class OLDAsnSchemaTypeDefinitionSequenceTest
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
            new OLDAsnSchemaTypeDefinitionSequence(null, componentTypes, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionSequence("TEST", null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionSequence("TEST", componentTypes, null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }

        // test blank
        try
        {
            new OLDAsnSchemaTypeDefinitionSequence("", componentTypes, AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new OLDAsnSchemaTypeDefinitionSequence(" ", componentTypes, AsnSchemaConstraint.NULL);
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
        final OLDAsnSchemaTypeDefinitionSequence instance =
                new OLDAsnSchemaTypeDefinitionSequence("TEST_NAME", componentTypes, AsnSchemaConstraint.NULL);
        assertEquals(AsnBuiltinType.Sequence, instance.getBuiltinType());
    }

    @Test
    public void testVisit() throws Exception
    {
        final ImmutableList<AsnSchemaComponentType> componentTypes =
                MockAsnSchemaComponentType.createMockedAsnSchemaComponentTypesForBody();
        final OLDAsnSchemaTypeDefinitionSequence instance =
                new OLDAsnSchemaTypeDefinitionSequence("TEST_NAME", componentTypes, AsnSchemaConstraint.NULL);
        assertEquals(OLDAsnSchemaTypeDefinitionSequence.class.getCanonicalName(),
                instance.visit(MockAsnSchemaTypeDefinitionVisitor.getInstance()));
    }
}
