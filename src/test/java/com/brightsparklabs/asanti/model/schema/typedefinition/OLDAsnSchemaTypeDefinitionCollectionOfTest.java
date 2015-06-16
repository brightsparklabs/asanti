/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.typedefinition;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;

/**
 * Unit tests for {@link TestInstance}
 *
 * @author brightSPARK Labs
 */
public class OLDAsnSchemaTypeDefinitionCollectionOfTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /**
     * New definition of test class with dummy abstract methods
     */
    private static class TestInstance extends OLDAsnSchemaTypeDefinitionCollectionOf
    {
        public TestInstance(String name, AsnBuiltinType builtinType, String elementTypeName,
                AsnSchemaConstraint constraint)
        {
            super(name, builtinType, elementTypeName, constraint);
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
            new TestInstance(null, AsnBuiltinType.SetOf, "person", AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new TestInstance("", AsnBuiltinType.SetOf, "person", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new TestInstance(" ", AsnBuiltinType.SetOf, "person", AsnSchemaConstraint.NULL);
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
            new TestInstance("NAME", null, "person", AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test creation using all types
        for (final AsnBuiltinType type : AsnBuiltinType.values())
        {
            if (OLDAsnSchemaTypeDefinitionCollectionOf.validTypes.contains(type))
            {
                try
                {
                    new TestInstance("NAME", type, "person", AsnSchemaConstraint.NULL);
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
                    new TestInstance("NAME", type, "person", AsnSchemaConstraint.NULL);
                    fail("IllegalAccessException not thrown");
                }
                catch (final IllegalArgumentException ex)
                {
                }
            }
        }
    }

    @Test
    public void testAsnSchemaConstructedTypeDefinition_ElementTypeName() throws Exception
    {
        // test null
        try
        {
            new TestInstance("NAME", AsnBuiltinType.SetOf, null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new TestInstance("", AsnBuiltinType.SetOf, "", AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new TestInstance(" ", AsnBuiltinType.SetOf, " ", AsnSchemaConstraint.NULL);
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
            new TestInstance("NAME", AsnBuiltinType.SetOf, "person", null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }
    }

    @Test
    public void testGetTagName() throws Exception
    {
        final TestInstance instance =
                new TestInstance("NAME", AsnBuiltinType.SetOf, "person", AsnSchemaConstraint.NULL);
        // test explicit tags
        assertEquals("", instance.getTagName("1"));
        assertEquals("", instance.getTagName("2"));
        assertEquals("", instance.getTagName("99"));
        assertEquals("", instance.getTagName("anything"));
        // test null
        assertEquals("", instance.getTagName(null));
        // test empty
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTagName(" "));
    }

    @Test
    public void testGetTypeName() throws Exception
    {
        final TestInstance instance =
                new TestInstance("NAME", AsnBuiltinType.SequenceOf, "person", AsnSchemaConstraint.NULL);
        // test explicit tags
        assertEquals("person", instance.getTypeName("1"));
        assertEquals("person", instance.getTypeName("2"));
        assertEquals("person", instance.getTypeName("99"));
        assertEquals("person", instance.getTypeName("anything"));
        // test null
        assertEquals("person", instance.getTypeName(null));
        // test empty
        assertEquals("person", instance.getTypeName(""));
        assertEquals("person", instance.getTypeName(" "));
    }
}
