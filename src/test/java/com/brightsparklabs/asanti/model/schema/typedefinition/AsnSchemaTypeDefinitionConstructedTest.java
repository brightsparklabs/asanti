/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.typedefinition;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
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
    private static final ImmutableList<AsnSchemaComponentType> emptyComponentTypes =
            ImmutableList.<AsnSchemaComponentType>of();

    /** a valid list of component types */
    private static final ImmutableList<AsnSchemaComponentType> componentTypes =
            ImmutableList.<AsnSchemaComponentType>of(MockAsnSchemaComponentType.createMockedInstance("TAG_0",
                    "",
                    "TYPE_0",
                    true), // automatic
                    MockAsnSchemaComponentType.createMockedInstance("TAG_1", "1", "TYPE_1", true),
                    MockAsnSchemaComponentType.createMockedInstance("TAG_4", "4", "TYPE_4", true),
                    MockAsnSchemaComponentType.createMockedInstance("TAG_5", "", "TYPE_5", true), // automatic
                    MockAsnSchemaComponentType.createMockedInstance("TAG_2", "2", "TYPE_2", true));

    /**
     * New definition of test class with dummy abstract methods
     */
    private static class TestInstance extends AsnSchemaTypeDefinitionConstructed
    {

        public TestInstance(String name, AsnBuiltinType type, Iterable<AsnSchemaComponentType> componentTypes,
                AsnSchemaConstraint constraint)
        {
            super(name, type, componentTypes, constraint);
        }

        @Override
        public Object visit(AsnSchemaTagTypeVisitor<?> visitor)
        {
            return "";
        }
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaConstructedTypeDefinition_Name() throws Exception
    {
        // test null
        try
        {
            new TestInstance(null, AsnBuiltinType.Null, componentTypes, AsnSchemaConstraint.NULL);
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new TestInstance("", AsnBuiltinType.Set, componentTypes, AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new TestInstance(" ", AsnBuiltinType.Set, componentTypes, AsnSchemaConstraint.NULL);
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
            new TestInstance("NAME", null, componentTypes, AsnSchemaConstraint.NULL);
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
                    new TestInstance("NAME", type, componentTypes, AsnSchemaConstraint.NULL);
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
                    new TestInstance("NAME", type, componentTypes, AsnSchemaConstraint.NULL);
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
            new TestInstance("NAME", AsnBuiltinType.Set, null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
    }

    @Test
    public void testAsnSchemaConstructedTypeDefinition_Constraint() throws Exception
    {
        // test null
        try
        {
            new TestInstance("NAME", AsnBuiltinType.Set, componentTypes, null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }
    }

    @Test
    public void testGetTagName() throws Exception
    {
        AsnSchemaTypeDefinitionConstructed instance =
                new TestInstance("NAME", AsnBuiltinType.Set, componentTypes, AsnSchemaConstraint.NULL);
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

        // test indexes
        assertEquals("TAG_1[0]", instance.getTagName("1[0]"));
        assertEquals("TAG_4[3]", instance.getTagName("4[3]"));
        assertEquals("TAG_2[1]", instance.getTagName("2[1]"));
        assertEquals("TAG_0[6]", instance.getTagName("0[6]"));
        assertEquals("TAG_5[4]", instance.getTagName("5[4]"));
        assertEquals("", instance.getTagName("3[0]"));
        assertEquals("", instance.getTagName("6[0]"));

        // test empty components
        instance = new TestInstance("NAME", AsnBuiltinType.Set, emptyComponentTypes, AsnSchemaConstraint.NULL);
        assertEquals("", instance.getTagName("1"));
        assertEquals("", instance.getTagName("2"));
        assertEquals("", instance.getTagName(null));
        assertEquals("", instance.getTagName(""));
    }

    @Test
    public void testGetTypeName() throws Exception
    {
        AsnSchemaTypeDefinitionConstructed instance =
                new TestInstance("NAME", AsnBuiltinType.Set, componentTypes, AsnSchemaConstraint.NULL);
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

        // test indexes
        assertEquals("TYPE_1", instance.getTypeName("1[0]"));
        assertEquals("TYPE_4", instance.getTypeName("4[3]"));
        assertEquals("TYPE_2", instance.getTypeName("2[1]"));
        assertEquals("TYPE_0", instance.getTypeName("0[6]"));
        assertEquals("TYPE_5", instance.getTypeName("5[4]"));
        assertEquals("", instance.getTypeName("3[0]"));
        assertEquals("", instance.getTypeName("6[0]"));

        // test empty components
        instance = new TestInstance("NAME", AsnBuiltinType.Set, emptyComponentTypes, AsnSchemaConstraint.NULL);
        assertEquals("", instance.getTypeName("1"));
        assertEquals("", instance.getTypeName("2"));
        assertEquals("", instance.getTypeName(null));
        assertEquals("", instance.getTypeName(""));
    }
}
