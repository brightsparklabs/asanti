/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Unit tests for {@link AsnSchemaTypeDefinition}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /**
     * New definition of test class with dummy abstract methods
     */
    private static class TestInstance extends AsnSchemaTypeDefinition
    {
        public TestInstance(String name, AsnBuiltinType builtinType, AsnSchemaConstraint constraint)
        {
            super(name, builtinType, constraint);
        }

        @Override
        public Object visit(AsnSchemaTypeDefinitionVisitor<?> visitor)
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
        final AsnSchemaTypeDefinition instance = new TestInstance("NAME", AsnBuiltinType.Set, AsnSchemaConstraint.NULL);
        assertEquals("NAME", instance.getName());
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        for (final AsnBuiltinType builtinType : AsnBuiltinType.values())
        {
            final AsnSchemaTypeDefinition instance = new TestInstance("NAME", builtinType, AsnSchemaConstraint.NULL);
            assertEquals(builtinType, instance.getBuiltinType());
        }
    }

    @Test
    public void testGetTagName() throws Exception
    {
        final AsnSchemaTypeDefinition instance = new TestInstance("NAME", AsnBuiltinType.Set, AsnSchemaConstraint.NULL);
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTagName(" "));
        assertEquals("", instance.getTagName("0"));
        assertEquals("", instance.getTagName("NAME"));
        assertEquals("", instance.getTagName("SET"));
    }

    @Test
    public void testGetTypeName() throws Exception
    {
        final AsnSchemaTypeDefinition instance = new TestInstance("NAME", AsnBuiltinType.Set, AsnSchemaConstraint.NULL);
        assertEquals("", instance.getTypeName(""));
        assertEquals("", instance.getTypeName(" "));
        assertEquals("", instance.getTypeName("0"));
        assertEquals("", instance.getTypeName("NAME"));
        assertEquals("", instance.getTypeName("SET"));
    }
}
