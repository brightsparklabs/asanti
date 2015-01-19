/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class AsnSchemaTypeDefinitionTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinition() throws Exception
    {
        // test null
        try
        {
            new AsnSchemaTypeDefinition(null, AsnBuiltinType.Null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinition("TYPE_NAME", null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new AsnSchemaTypeDefinition("", AsnBuiltinType.Null);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinition(" ", AsnBuiltinType.Null);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetName() throws Exception
    {
        final AsnSchemaTypeDefinition instance = new AsnSchemaTypeDefinition("NAME", AsnBuiltinType.Set);
        assertEquals("NAME", instance.getName());
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        for (final AsnBuiltinType builtinType : AsnBuiltinType.values())
        {
            final AsnSchemaTypeDefinition instance = new AsnSchemaTypeDefinition("NAME", builtinType);
            assertEquals(builtinType, instance.getBuiltinType());
        }
    }

    @Test
    public void testGetTagName() throws Exception
    {
        final AsnSchemaTypeDefinition instance = new AsnSchemaTypeDefinition("NAME", AsnBuiltinType.Set);
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTagName(" "));
        assertEquals("", instance.getTagName("0"));
        assertEquals("", instance.getTagName("NAME"));
        assertEquals("", instance.getTagName("SET"));
    }

    @Test
    public void testGetTypeName() throws Exception
    {
        final AsnSchemaTypeDefinition instance = new AsnSchemaTypeDefinition("NAME", AsnBuiltinType.Set);
        assertEquals("", instance.getTypeName(""));
        assertEquals("", instance.getTypeName(" "));
        assertEquals("", instance.getTypeName("0"));
        assertEquals("", instance.getTypeName("NAME"));
        assertEquals("", instance.getTypeName("SET"));
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: MockedInstanceBuilder
    // -------------------------------------------------------------------------

    /**
     * Builder for creating mocked instances of {@link AsnSchemaTypeDefinition}
     *
     * @author brightSPARK Labs
     */
    public static class MockAsnSchemaTypeDefinitionBuilder
    {
        final AsnSchemaTypeDefinition mockedInstance = mock(AsnSchemaTypeDefinition.class);

        /**
         * Default constructor
         *
         * @param name
         * @param builtinType
         */
        public MockAsnSchemaTypeDefinitionBuilder(String name, AsnBuiltinType builtinType)
        {
            when(mockedInstance.getBuiltinType()).thenReturn(builtinType);
            when(mockedInstance.getName()).thenReturn(name);
        }

        /**
         * Add a component type to this definition
         *
         * @param tag
         *            tag of the component type
         *
         * @param tagName
         *            tag name of the component type
         *
         * @param typeName
         *            type name of the component type
         *
         * @return this builder
         */
        public MockAsnSchemaTypeDefinitionBuilder addComponentType(String tag, String tagName, String typeName)
        {
            when(mockedInstance.getTagName(tag)).thenReturn(tagName);
            when(mockedInstance.getTypeName(tag)).thenReturn(typeName);
            return this;
        }

        /**
         * Creates a mocked instance from the data in this builder
         *
         * @return a mocked instance of {@link AsnSchemaTypeDefinition}
         */
        public AsnSchemaTypeDefinition build()
        {
            return mockedInstance;
        }
    }
}
