/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaNamedTag;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaTypeDefinitionVisitor;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link AsnSchemaTypeDefinitionInteger}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionIntegerTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** an empty list of distinguished values */
    private static final ImmutableList<AsnSchemaNamedTag> emptyDistinguishedValues =
            ImmutableList.<AsnSchemaNamedTag>of();

    /**
     * named tags within the {@code Date-Due} type definition from the example schema
     */
    private static final ImmutableList<AsnSchemaNamedTag> dateDueOptions =
            MockAsnSchemaNamedTag.createMockedAsnNamedTagsForDateDue();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinitionInteger() throws Exception
    {
        // test valid
        new AsnSchemaTypeDefinitionInteger("TEST_NAME", emptyDistinguishedValues, AsnSchemaConstraint.NULL);

        // test null
        try
        {
            new AsnSchemaTypeDefinitionInteger(null, emptyDistinguishedValues, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionInteger("TEST_NAME", emptyDistinguishedValues, null);
        }
        catch (final NullPointerException ex)
        {
            fail("NullPointerException thrown");
        }

        // test empty
        try
        {
            new AsnSchemaTypeDefinitionInteger("",  emptyDistinguishedValues, AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionInteger(" ",  emptyDistinguishedValues, AsnSchemaConstraint.NULL);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }

        // test null distinguished values
        try
        {
            new AsnSchemaTypeDefinitionInteger("TYPE_NAME", null, AsnSchemaConstraint.NULL);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
    }

    @Test
    public void testGetTagName() throws Exception
    {
        // test distinguished value tags
        AsnSchemaTypeDefinitionInteger instance =
                new AsnSchemaTypeDefinitionInteger("TYPE_NAME", dateDueOptions, AsnSchemaConstraint.NULL);
        assertEquals("tomorrow", instance.getTagName("0"));
        assertEquals("three-day", instance.getTagName("1"));
        assertEquals("week", instance.getTagName("2"));

        // test unknown
        assertEquals("", instance.getTagName("6"));
        // test null
        assertEquals("", instance.getTagName(null));
        // test empty
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTagName(" "));

        // test empty options
        instance = new AsnSchemaTypeDefinitionInteger("TYPE_NAME", emptyDistinguishedValues, AsnSchemaConstraint.NULL);
        assertEquals("", instance.getTagName("0"));
        assertEquals("", instance.getTagName("1"));
        assertEquals("", instance.getTagName("2"));
        assertEquals("", instance.getTagName(null));
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTagName(" "));
    }

    @Test
    public void testGetTypeName() throws Exception
    {
        // test generated tag numbers
        AsnSchemaTypeDefinitionInteger instance =
                new AsnSchemaTypeDefinitionInteger("TYPE_NAME", dateDueOptions, AsnSchemaConstraint.NULL);
        assertEquals("", instance.getTypeName("0"));
        assertEquals("", instance.getTypeName("1"));
        assertEquals("", instance.getTypeName("2"));
        assertEquals("", instance.getTypeName("3"));
        assertEquals("", instance.getTypeName("4"));

        // test unknown
        assertEquals("", instance.getTypeName("6"));
        // test null
        assertEquals("", instance.getTypeName(null));
        // test empty
        assertEquals("", instance.getTypeName(""));
        assertEquals("", instance.getTypeName(" "));

        // test empty options
        instance = new AsnSchemaTypeDefinitionInteger("TYPE_NAME", emptyDistinguishedValues, AsnSchemaConstraint.NULL);
        assertEquals("", instance.getTypeName("0"));
        assertEquals("", instance.getTypeName("1"));
        assertEquals("", instance.getTypeName("2"));
        assertEquals("", instance.getTypeName(null));
        assertEquals("", instance.getTypeName(""));
        assertEquals("", instance.getTypeName(" "));
    }

    @Test
    public void testGetBuiltinType() throws Exception
    {
        final AsnSchemaTypeDefinitionInteger instance =
                new AsnSchemaTypeDefinitionInteger("TEST_NAME", emptyDistinguishedValues, AsnSchemaConstraint.NULL);
        assertEquals(AsnBuiltinType.Integer, instance.getBuiltinType());
    }

    @Test
    public void testVisit() throws Exception
    {
        final AsnSchemaTypeDefinitionInteger instance =
                new AsnSchemaTypeDefinitionInteger("TEST_NAME", emptyDistinguishedValues, AsnSchemaConstraint.NULL);
        assertEquals(AsnSchemaTypeDefinitionInteger.class.getCanonicalName(),
                instance.visit(MockAsnSchemaTypeDefinitionVisitor.getInstance()));
    }
}
