/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.brightsparklabs.asanti.mocks.MockAsnSchemaModule;
import com.brightsparklabs.asanti.model.schema.AsnSchemaModule.Builder;
import com.google.common.collect.ImmutableMap;

/**
 * Unit tests for {@link AsnSchemaModule}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaModuleTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** default instance to test */
    private static AsnSchemaModule instance;

    /** all schema modules referenced in the tests */
    private static ImmutableMap<String, AsnSchemaModule> allSchemaModules;

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @BeforeClass
    public static void setUpBeforeClass()
    {
        allSchemaModules = MockAsnSchemaModule.createMockedAsnSchemaModules();
        instance = allSchemaModules.get("Document-PDU");
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testBuilder() throws Exception
    {
        // test standard build works
        final Builder builder = AsnSchemaModule.builder();
        builder.setName("TEST")
                .build();

        // test null name
        try
        {
            builder.setName(null)
                    .build();
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        // test empty name
        try
        {
            builder.setName("")
                    .build();
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
        try
        {
            builder.setName(" ")
                    .build();
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetName() throws Exception
    {
        assertEquals("Document-PDU", instance.getName());
    }

    @Test
    public void testGetDecodedTag() throws Exception
    {
        assertEquals("/Document/header/published", instance.getDecodedTag("1/0", "Document", allSchemaModules)
                .getDecodedData());
        assertEquals("/Document/body/lastModified/date", instance.getDecodedTag("2/0/0", "Document", allSchemaModules)
                .getDecodedData());
        assertEquals("/Document/body/lastModified/modifiedBy/firstName", instance.getDecodedTag("2/0/1/1", "Document", allSchemaModules)
                .getDecodedData());
        assertEquals("/Document/body/lastModified/modifiedBy/lastName", instance.getDecodedTag("2/0/1/2", "Document", allSchemaModules)
                .getDecodedData());
        assertEquals("/Document/body/lastModified/modifiedBy/title", instance.getDecodedTag("2/0/1/3", "Document", allSchemaModules)
                .getDecodedData());
        assertEquals("/Document/body/prefix/text", instance.getDecodedTag("2/1/1", "Document", allSchemaModules)
                .getDecodedData());
        assertEquals("/Document/body/content/text", instance.getDecodedTag("2/2/1", "Document", allSchemaModules)
                .getDecodedData());
        // TODO support SET OF and SEQUENCE OF
        // assertEquals("/Document/body/content/paragraphs/title",
        // instance.getDecodedTag("2/2/2/1", "Document", allSchemaModules)
        // .getDecodedData());
        // assertEquals("/Document/body/content/paragraphs/contributor/firstName",
        // instance.getDecodedTag("2/2/2/2/1", "Document", allSchemaModules)
        // .getDecodedData());
        // assertEquals("/Document/body/content/paragraphs/contributor/lastName",
        // instance.getDecodedTag("2/2/2/2/2", "Document", allSchemaModules)
        // .getDecodedData());
        // assertEquals("/Document/body/content/paragraphs/contributor/title",
        // instance.getDecodedTag("2/2/2/2/3", "Document", allSchemaModules)
        // .getDecodedData());
        // assertEquals("/Document/body/content/paragraphs/points",
        // instance.getDecodedTag("2/2/2/3", "Document", allSchemaModules)
        // .getDecodedData());
        // assertEquals("/Document/body/content/paragraphs/points[0]",
        // instance.getDecodedTag("2/2/2/3[0]", "Document", allSchemaModules));
        // assertEquals("/Document/body/content/paragraphs[99]/title",
        // instance.getDecodedTag("2/2/2[99]/1", "Document", allSchemaModules));
        // assertEquals("/Document/body/content/paragraphs[99]/contributor/firstName",
        // instance.getDecodedTag("2/2/2[99]/2/1", "Document",
        // allSchemaModules));
        // assertEquals("/Document/body/content/paragraphs[99]/contributor/lastName",
        // instance.getDecodedTag("2/2/2[99]/2/2", "Document",
        // allSchemaModules));
        // assertEquals("/Document/body/content/paragraphs[99]/contributor/title",
        // instance.getDecodedTag("2/2/2[99]/2/3", "Document",
        // allSchemaModules));
        // assertEquals("/Document/body/content/paragraphs[99]/points",
        // instance.getDecodedTag("2/2/2[99]/3", "Document", allSchemaModules));
        // assertEquals("/Document/body/content/paragraphs[99]/points[99]",
        // instance.getDecodedTag("2/2/2[99]/3[99]", "Document",
        // allSchemaModules));
        assertEquals("/Document/body/suffix/text", instance.getDecodedTag("2/3/1", "Document", allSchemaModules)
                .getDecodedData());
    }

    @Test
    public void testGetType() throws Exception
    {
        AsnSchemaTypeDefinition typeDefinition = testGetType("Document", AsnBuiltinType.Sequence);
        checkTypeDefinition(typeDefinition, "1", "header", "Header");
        checkTypeDefinition(typeDefinition, "2", "body", "Body");
        checkTypeDefinition(typeDefinition, "3", "footer", "Footer");

        typeDefinition = testGetType("Header", AsnBuiltinType.Sequence);
        checkTypeDefinition(typeDefinition, "0", "published", "PublishedMetadata");

        typeDefinition = testGetType("Body", AsnBuiltinType.Sequence);
        checkTypeDefinition(typeDefinition, "0", "lastModified", "ModificationMetadata");
        checkTypeDefinition(typeDefinition, "1", "prefix", "Section-Note");
        checkTypeDefinition(typeDefinition, "2", "content", "Section-Main");
        checkTypeDefinition(typeDefinition, "3", "suffix", "Section-Note");

        typeDefinition = testGetType("Footer", AsnBuiltinType.Sequence);
        checkTypeDefinition(typeDefinition, "0", "author", "Person");

        typeDefinition = testGetType("PublishedMetadata", AsnBuiltinType.Sequence);
        checkTypeDefinition(typeDefinition, "1", "date", "GeneralizedTime");
        checkTypeDefinition(typeDefinition, "2", "country", "OCTET STRING");

        typeDefinition = testGetType("ModificationMetadata", AsnBuiltinType.Sequence);
        checkTypeDefinition(typeDefinition, "0", "date", "GeneralizedTime");
        checkTypeDefinition(typeDefinition, "1", "modifiedBy", "Person");

        typeDefinition = testGetType("Section-Note", AsnBuiltinType.Sequence);
        checkTypeDefinition(typeDefinition, "1", "text", "OCTET STRING");

        typeDefinition = testGetType("Section-Main", AsnBuiltinType.Sequence);
        checkTypeDefinition(typeDefinition, "1", "text", "OCTET STRING");
        checkTypeDefinition(typeDefinition, "2", "paragraphs", "SEQUENCE OF Paragraph");

        typeDefinition = testGetType("Paragraph", AsnBuiltinType.Sequence);
        checkTypeDefinition(typeDefinition, "1", "title", "OCTET STRING");
        checkTypeDefinition(typeDefinition, "2", "contributor", "Person");
        checkTypeDefinition(typeDefinition, "3", "points", "SEQUENCE OF OCTET STRING");

        typeDefinition = testGetType("Person", AsnBuiltinType.Sequence);
        checkTypeDefinition(typeDefinition, "1", "firstName", "OCTET STRING");
        checkTypeDefinition(typeDefinition, "2", "lastName", "OCTET STRING");
        checkTypeDefinition(typeDefinition, "3", "title", "ENUMERATED");

        // 'People' was not imported so should not be found
        assertEquals(AsnSchemaTypeDefinition.NULL, instance.getType("People", allSchemaModules));
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Utility method to test the object returned by calling
     * {@link AsnSchemaModule#getType(String, ImmutableMap)} using the supplied
     * name
     *
     * @param name
     *            name of the {@link AsnSchemaTypeDefinition} to retrieved
     *
     * @param builtinType
     *            expected ASN.1 built-in type of the retrieved
     *            {@link AsnSchemaTypeDefinition}
     *
     * @return the retrieved {@link AsnSchemaTypeDefinition}
     */
    private AsnSchemaTypeDefinition testGetType(String name, AsnBuiltinType builtinType)
    {
        final AsnSchemaTypeDefinition typeDefinition = instance.getType(name, allSchemaModules);
        assertEquals(name, typeDefinition.getName());
        assertEquals(builtinType, typeDefinition.getBuiltinType());
        return typeDefinition;
    }

    /**
     * Utility method to test the values associated with a tag on a given
     * {@link AsnSchemaTypeDefinition}
     *
     * @param typeDefinition
     *            {@link AsnSchemaTypeDefinition} to interrogate
     *
     * @param tag
     *            tag to retrieve
     *
     * @param tagName
     *            expected tag name associated with tag
     *
     * @param typeName
     *            expected type name associated with tag
     */
    private void checkTypeDefinition(AsnSchemaTypeDefinition typeDefinition, String tag, String tagName, String typeName)
    {
        assertEquals(tagName, typeDefinition.getTagName(tag));
        assertEquals(typeName, typeDefinition.getTypeName(tag));
    }
}
