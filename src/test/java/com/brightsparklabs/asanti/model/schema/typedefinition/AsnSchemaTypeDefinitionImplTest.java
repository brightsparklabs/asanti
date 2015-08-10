/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.typedefinition;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import org.junit.Test;

/**
 * Unit tests for {@link AsnSchemaTypeDefinitionImpl}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionImplTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinition() throws Exception
    {
        // test null
        try
        {
            new AsnSchemaTypeDefinitionImpl(null, AsnSchemaType.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionImpl("TYPE_NAME", null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new AsnSchemaTypeDefinitionImpl("", AsnSchemaType.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetName() throws Exception
    {
        final AsnSchemaTypeDefinition instance = new AsnSchemaTypeDefinitionImpl("NAME", AsnSchemaType.NULL);
        assertEquals("NAME", instance.getName());
    }

    @Test
    public void testGetType() throws Exception
    {
        final AsnSchemaTypeDefinition instance = new AsnSchemaTypeDefinitionImpl("NAME", AsnSchemaType.NULL);
        assertEquals(AsnSchemaType.NULL, instance.getType());
    }
}
