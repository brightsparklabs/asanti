/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * Unit tests for {@link AsnSchemaTypeDefinitionConstructed}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionConstructedTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** an empty list of component types */
    private static final ImmutableList<AsnSchemaComponentType> emptyComponentTypes = ImmutableList.<AsnSchemaComponentType>of();

    /** a valid list of component types */
    private static final ImmutableList<AsnSchemaComponentType> componentTypes = ImmutableList.<AsnSchemaComponentType>of(new AsnSchemaComponentType(
            "TAG_0", "", "TYPE_0", true), // automatic
            new AsnSchemaComponentType("TAG_1", "1", "TYPE_1", true),
            new AsnSchemaComponentType("TAG_4", "4", "TYPE_4", true),
            new AsnSchemaComponentType("TAG_5", "", "TYPE_5", true), // automatic
            new AsnSchemaComponentType("TAG_2", "2", "TYPE_2", true));

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaConstructedTypeDefinition_Name() throws Exception
    {
        // test null
        try
        {
            new AsnSchemaTypeDefinitionConstructed(null, AsnBuiltinType.Null, componentTypes);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new AsnSchemaTypeDefinitionConstructed("", AsnBuiltinType.Set, componentTypes);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionConstructed(" ", AsnBuiltinType.Set, componentTypes);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testAsnSchemaConstructedTypeDefinition_Type() throws Exception
    {
        // test null
        try
        {
            new AsnSchemaTypeDefinitionConstructed("NAME", null, componentTypes);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test creation using all types
        for (final AsnBuiltinType type : AsnBuiltinType.values())
        {
            if (AsnSchemaTypeDefinitionConstructed.validTypes.contains(type))
            {
                try
                {
                    new AsnSchemaTypeDefinitionConstructed("NAME", type, componentTypes);
                }
                catch (final IllegalArgumentException ex)
                {
                    fail("IllegalAccessException thrown");
                }
            }
            else
            {
                try
                {
                    new AsnSchemaTypeDefinitionConstructed("NAME", type, componentTypes);
                    fail("IllegalAccessException not thrown");
                }
                catch (final IllegalArgumentException ex)
                {
                }
            }
        }
    }

    @Test
    public void testAsnSchemaConstructedTypeDefinition_ComponentTypes() throws Exception
    {
        // test null
        try
        {
            new AsnSchemaTypeDefinitionConstructed("NAME", AsnBuiltinType.Set, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
    }

    @Test
    public void testGetTagName() throws Exception
    {
        AsnSchemaTypeDefinitionConstructed instance = new AsnSchemaTypeDefinitionConstructed("NAME",
                AsnBuiltinType.Set, componentTypes);
        // test explicit tags
        assertEquals("TAG_1", instance.getTagName("1"));
        assertEquals("TAG_4", instance.getTagName("4"));
        assertEquals("TAG_2", instance.getTagName("2"));
        // test automatic tags
        assertEquals("TAG_0", instance.getTagName("0"));
        assertEquals("TAG_5", instance.getTagName("5"));
        // test unknown
        assertEquals("", instance.getTagName("3"));
        assertEquals("", instance.getTagName("6"));
        // test null
        assertEquals("", instance.getTagName(null));
        // test empty
        assertEquals("", instance.getTagName(""));

        // test empty components
        instance = new AsnSchemaTypeDefinitionConstructed("NAME", AsnBuiltinType.Set, emptyComponentTypes);
        assertEquals("", instance.getTagName("1"));
        assertEquals("", instance.getTagName("2"));
        assertEquals("", instance.getTagName(null));
        assertEquals("", instance.getTagName(""));
    }

    @Test
    public void testGetTypeName() throws Exception
    {
        AsnSchemaTypeDefinitionConstructed instance = new AsnSchemaTypeDefinitionConstructed("NAME",
                AsnBuiltinType.Set, componentTypes);
        // test explicit tags
        assertEquals("TYPE_1", instance.getTypeName("1"));
        assertEquals("TYPE_4", instance.getTypeName("4"));
        assertEquals("TYPE_2", instance.getTypeName("2"));
        // test automatic tags
        assertEquals("TYPE_0", instance.getTypeName("0"));
        assertEquals("TYPE_5", instance.getTypeName("5"));
        // test unknown
        assertEquals("", instance.getTypeName("3"));
        assertEquals("", instance.getTypeName("6"));
        // test null
        assertEquals("", instance.getTypeName(null));
        // test empty
        assertEquals("", instance.getTypeName(""));

        // test empty components
        instance = new AsnSchemaTypeDefinitionConstructed("NAME", AsnBuiltinType.Set, emptyComponentTypes);
        assertEquals("", instance.getTypeName("1"));
        assertEquals("", instance.getTypeName("2"));
        assertEquals("", instance.getTypeName(null));
        assertEquals("", instance.getTypeName(""));
    }
}
