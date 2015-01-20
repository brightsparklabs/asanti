/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.mocks.MockAsnSchemaComponentType;
import com.google.common.collect.ImmutableList;

/**
 * Unit tests for {@link AsnSchemaTypeDefinitionSequence}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionSequenceTest
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
            new AsnSchemaTypeDefinitionSequence(null, componentTypes);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionSequence("TEST", null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new AsnSchemaTypeDefinitionSequence("", componentTypes);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionSequence(" ", componentTypes);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }
}
