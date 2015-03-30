/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaNamedTag;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaTypeDefinitionVisitor;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link AsnSchemaTypeDefinitionEnumerated}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionEnumeratedTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** an empty list of options */
    private static final ImmutableList<AsnSchemaNamedTag> emptyOptions =
            ImmutableList.<AsnSchemaNamedTag>of();

    /**
     * options within the {@code Person} type definition from the example schema
     */
    private static final ImmutableList<AsnSchemaNamedTag> personOptions =
            MockAsnSchemaNamedTag.createMockedAsnSchemaNamedTagsForPerson();

    /**
     * options within the {@code Gender} type definition from the example schema
     */
    private static final ImmutableList<AsnSchemaNamedTag> genderOptions =
            MockAsnSchemaNamedTag.createMockedAsnSchemaNamedTagsForGender();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeDefinitionEnumerated_Name() throws Exception
    {
        // test null
        try
        {
            new AsnSchemaTypeDefinitionEnumerated(null, emptyOptions);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test blank
        try
        {
            new AsnSchemaTypeDefinitionEnumerated("", emptyOptions);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            new AsnSchemaTypeDefinitionEnumerated(" ", emptyOptions);
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testAsnSchemaTypeDefinitionEnumerated_Options() throws Exception
    {
        // test null
        try
        {
            new AsnSchemaTypeDefinitionEnumerated("TYPE_NAME", null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
    }

    @Test
    public void testGetTagName() throws Exception
    {
        // test generated tag numbers
        AsnSchemaTypeDefinitionEnumerated instance = new AsnSchemaTypeDefinitionEnumerated("TYPE_NAME", personOptions);
        assertEquals("mr", instance.getTagName("0"));
        assertEquals("mrs", instance.getTagName("1"));
        assertEquals("ms", instance.getTagName("2"));
        assertEquals("dr", instance.getTagName("3"));
        assertEquals("rev", instance.getTagName("4"));
        // test unknown
        assertEquals("", instance.getTagName("6"));
        // test null
        assertEquals("", instance.getTagName(null));
        // test empty
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTagName(" "));

        // test explicit tag numbers
        instance = new AsnSchemaTypeDefinitionEnumerated("TYPE_NAME", genderOptions);
        assertEquals("male", instance.getTagName("0"));
        assertEquals("female", instance.getTagName("1"));
        // test unknown
        assertEquals("", instance.getTagName("2"));
        // test null
        assertEquals("", instance.getTagName(null));
        // test empty
        assertEquals("", instance.getTagName(""));
        assertEquals("", instance.getTagName(" "));

        // test empty options
        instance = new AsnSchemaTypeDefinitionEnumerated("TYPE_NAME", emptyOptions);
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
        AsnSchemaTypeDefinitionEnumerated instance = new AsnSchemaTypeDefinitionEnumerated("TYPE_NAME", personOptions);
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

        // test explicit tag numbers
        instance = new AsnSchemaTypeDefinitionEnumerated("TYPE_NAME", genderOptions);
        assertEquals("", instance.getTypeName("0"));
        assertEquals("", instance.getTypeName("1"));
        // test unknown
        assertEquals("", instance.getTypeName("2"));
        // test null
        assertEquals("", instance.getTypeName(null));
        // test empty
        assertEquals("", instance.getTypeName(""));
        assertEquals("", instance.getTypeName(" "));

        // test empty options
        instance = new AsnSchemaTypeDefinitionEnumerated("TYPE_NAME", emptyOptions);
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
        final AsnSchemaTypeDefinitionEnumerated instance =
                new AsnSchemaTypeDefinitionEnumerated("TYPE_NAME", personOptions);
        assertEquals(AsnBuiltinType.Enumerated, instance.getBuiltinType());
    }

    @Test
    public void testVisit() throws Exception
    {
        final AsnSchemaTypeDefinitionEnumerated instance =
                new AsnSchemaTypeDefinitionEnumerated("TYPE_NAME", personOptions);
        assertEquals(AsnSchemaTypeDefinitionEnumerated.class.getCanonicalName(),
                instance.visit(MockAsnSchemaTypeDefinitionVisitor.getInstance()));
    }
}
