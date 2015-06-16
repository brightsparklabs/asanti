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
 * Unit tests for {@link AbstractOLDAsnSchemaTypeDefinition}
 *
 * @author brightSPARK Labs
 */
public class AbstractOLDAsnSchemaTypeDefinitionTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /**
     * New definition of test class with dummy abstract methods
     */
    private static class TestInstance extends AbstractOLDAsnSchemaTypeDefinition
    {
        public TestInstance(String name, AsnBuiltinType builtinType, AsnSchemaConstraint constraint)
        {
            super(name, builtinType, constraint);
        }

        @Override
        public String getTagName(String tag)
        {
            return "";
        }

        @Override
        public String getTypeName(String tag)
        {
            return "";
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
    public void testAsnSchemaTypeDefinition() throws Exception
    {
        // test null
        try
        {
            new TestInstance(null, AsnBuiltinType.Null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new TestInstance("TYPE_NAME", null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new TestInstance("TYPE_NAME", AsnBuiltinType.Null, null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }

        // test blank
        try
        {
            new TestInstance("", AsnBuiltinType.Null, AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new TestInstance(" ", AsnBuiltinType.Null, AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetName() throws Exception
    {
        final OLDAsnSchemaTypeDefinition instance = new TestInstance("NAME", AsnBuiltinType.Set, AsnSchemaConstraint.NULL);
        assertEquals("NAME", instance.getName());
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        for (final AsnBuiltinType builtinType : AsnBuiltinType.values())
        {
            final OLDAsnSchemaTypeDefinition instance = new TestInstance("NAME", builtinType, AsnSchemaConstraint.NULL);
            assertEquals(builtinType, instance.getBuiltinType());
        }
    }

    @Test
    public void testGetTagName() throws Exception
    {
        final OLDAsnSchemaTypeDefinition instance = new TestInstance("NAME", AsnBuiltinType.Set, AsnSchemaConstraint.NULL);
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTagName(" "));
        assertEquals("", instance.getTagName("0"));
        assertEquals("", instance.getTagName("NAME"));
        assertEquals("", instance.getTagName("SET"));
    }

    @Test
    public void testGetTypeName() throws Exception
    {
        final OLDAsnSchemaTypeDefinition instance = new TestInstance("NAME", AsnBuiltinType.Set, AsnSchemaConstraint.NULL);
        assertEquals("", instance.getTypeName(""));
        assertEquals("", instance.getTypeName(" "));
        assertEquals("", instance.getTypeName("0"));
        assertEquals("", instance.getTypeName("NAME"));
        assertEquals("", instance.getTypeName("SET"));
    }
}
