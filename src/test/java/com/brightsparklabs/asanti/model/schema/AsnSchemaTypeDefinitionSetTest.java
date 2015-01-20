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
 * Unit tests for {@link AsnSchemaTypeDefinitionSet}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionSetTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinitionSet() throws Exception
    {
        final ImmutableList<AsnSchemaComponentType> componentTypes =
                MockAsnSchemaComponentType.createMockedAsnSchemaComponentTypesForBody();

        // test null
        try
        {
            new AsnSchemaTypeDefinitionSet(null, componentTypes);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionSet("TEST", null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new AsnSchemaTypeDefinitionSet("", componentTypes);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionSet(" ", componentTypes);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }
}
