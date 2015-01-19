/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.brightsparklabs.asanti.model.schema.AsnSchemaModule.Builder;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionTest.MockAsnSchemaTypeDefinitionBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

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
        instance = createMockedAsnSchemaModuleForDocumentPdu();
        final AsnSchemaModule peopleProtocolModule = createMockedAsnSchemaModuleForPeopleProtocol();
        allSchemaModules = ImmutableMap.<String, AsnSchemaModule>builder()
                .put(instance.getName(), instance)
                .put(peopleProtocolModule.getName(), peopleProtocolModule)
                .build();
    }

    // -------------------------------------------------------------------------
    // MOCKS
    // -------------------------------------------------------------------------

    /**
     * Creates a mock {@link AsnSchemaModule} instance conforming to the
     * 'Document-PDU' module in the test schema
     *
     * @return mocked instance
     */
    public static AsnSchemaModule createMockedAsnSchemaModuleForDocumentPdu()
    {
        final Builder moduleBuilder = AsnSchemaModule.builder();
        moduleBuilder.setName("Document-PDU")
                .addImport("Person", "People-Protocol");
        for (final AsnSchemaTypeDefinition typeDefinition : createMockedAsnSchemaTypeDefinitionsForDocumentPdu())
        {
            moduleBuilder.addType(typeDefinition);
        }
        return moduleBuilder.build();
    }

    /**
     * Creates mock {@link AsnSchemaTypeDefinition} instances conforming to the
     * 'Document-PDU' module in the test schema
     *
     * @return mock instances for use within the 'Document-PDU' module
     */
    private static ImmutableList<AsnSchemaTypeDefinition> createMockedAsnSchemaTypeDefinitionsForDocumentPdu()
    {
        final ImmutableList.Builder<AsnSchemaTypeDefinition> listBuilder = ImmutableList.builder();

        // build Document
        AsnSchemaTypeDefinition mocktypeDefinition =
                new MockAsnSchemaTypeDefinitionBuilder("Document", AsnBuiltinType.Sequence).addComponentType("1", "header", "Header")
                        .addComponentType("2", "body", "Body")
                        .addComponentType("3", "footer", "Footer")
                        .build();
        listBuilder.add(mocktypeDefinition);

        // build Header
        mocktypeDefinition =
                new MockAsnSchemaTypeDefinitionBuilder("Header", AsnBuiltinType.Sequence).addComponentType("0", "published", "PublishedMetadata")
                        .build();
        listBuilder.add(mocktypeDefinition);

        // build Body
        mocktypeDefinition =
                new MockAsnSchemaTypeDefinitionBuilder("Body", AsnBuiltinType.Sequence).addComponentType("0", "lastModified", "ModificationMetadata")
                        .addComponentType("1", "prefix", "Section-Note")
                        .addComponentType("2", "content", "Section-Main")
                        .addComponentType("3", "suffix", "Section-Note")
                        .build();
        listBuilder.add(mocktypeDefinition);

        // build Footer
        mocktypeDefinition =
                new MockAsnSchemaTypeDefinitionBuilder("Footer", AsnBuiltinType.Sequence).addComponentType("0", "author", "Person")
                        .build();
        listBuilder.add(mocktypeDefinition);

        // build PublishedMetadata
        mocktypeDefinition =
                new MockAsnSchemaTypeDefinitionBuilder("PublishedMetadata", AsnBuiltinType.Sequence).addComponentType("1", "date", "GeneralizedTime")
                        .addComponentType("2", "country", "OCTET STRING")
                        .build();
        listBuilder.add(mocktypeDefinition);

        // build PublishedMetadata
        mocktypeDefinition =
                new MockAsnSchemaTypeDefinitionBuilder("PublishedMetadata", AsnBuiltinType.Sequence).addComponentType("1", "date", "GeneralizedTime")
                        .addComponentType("2", "country", "OCTET STRING")
                        .build();
        listBuilder.add(mocktypeDefinition);

        // build ModificationMetadata
        mocktypeDefinition =
                new MockAsnSchemaTypeDefinitionBuilder("ModificationMetadata", AsnBuiltinType.Sequence).addComponentType("0", "date", "GeneralizedTime")
                        .addComponentType("1", "modifiedBy", "Person")
                        .build();
        listBuilder.add(mocktypeDefinition);

        // build Section-Note
        mocktypeDefinition =
                new MockAsnSchemaTypeDefinitionBuilder("Section-Note", AsnBuiltinType.Sequence).addComponentType("1", "text", "OCTET STRING")
                        .build();
        listBuilder.add(mocktypeDefinition);

        // build Section-Main
        mocktypeDefinition =
                new MockAsnSchemaTypeDefinitionBuilder("Section-Main", AsnBuiltinType.Sequence).addComponentType("1", "text", "OCTET STRING")
                        .addComponentType("2", "paragraphs", "SEQUENCE OF Paragraph")
                        .build();
        listBuilder.add(mocktypeDefinition);

        // build Paragraph
        mocktypeDefinition =
                new MockAsnSchemaTypeDefinitionBuilder("Paragraph", AsnBuiltinType.Sequence).addComponentType("1", "title", "OCTET STRING")
                        .addComponentType("2", "contributor", "Person")
                        .addComponentType("3", "points", "SEQUENCE OF OCTET STRING")
                        .build();
        listBuilder.add(mocktypeDefinition);

        return listBuilder.build();
    }

    /**
     * Creates a mock {@link AsnSchemaModule} instance conforming to the
     * 'Document-PDU' module in the test schema
     *
     * @return mocked instance
     */
    @SuppressWarnings("unchecked")
    // @SuppressWarnings required for any(ImmutableMap.class)
    public static AsnSchemaModule createMockedAsnSchemaModuleForPeopleProtocol()
    {
        final AsnSchemaModule mockedInstance = mock(AsnSchemaModule.class);
        when(mockedInstance.getName()).thenReturn("People-Protocol");

        final ImmutableList<AsnSchemaTypeDefinition> typeDefinitions =
                createMockedAsnSchemaTypeDefinitionsForPeopleProtocol();
        for (final AsnSchemaTypeDefinition typeDefinition : typeDefinitions)
        {
            when(mockedInstance.getType(eq(typeDefinition.getName()), any(ImmutableMap.class))).thenReturn(typeDefinition);
        }

        when(mockedInstance.getDecodedTag(eq("1"), eq("Person"), any(ImmutableMap.class))).thenReturn(DecodeResult.create(true, "/Person/firstName"));
        when(mockedInstance.getDecodedTag(eq("2"), eq("Person"), any(ImmutableMap.class))).thenReturn(DecodeResult.create(true, "/Person/lastName"));
        when(mockedInstance.getDecodedTag(eq("3"), eq("Person"), any(ImmutableMap.class))).thenReturn(DecodeResult.create(true, "/Person/title"));

        return mockedInstance;
    }

    /**
     * Creates mock {@link AsnSchemaTypeDefinition} instances conforming to the
     * 'People-Protocol' module in the test schema
     *
     * @return mock instances for use within the 'PeopleProtocol' module
     */
    private static ImmutableList<AsnSchemaTypeDefinition> createMockedAsnSchemaTypeDefinitionsForPeopleProtocol()
    {
        final ImmutableList.Builder<AsnSchemaTypeDefinition> listBuilder = ImmutableList.builder();

        // build People
        AsnSchemaTypeDefinition mocktypeDefinition =
                new MockAsnSchemaTypeDefinitionBuilder("People", AsnBuiltinType.SetOf).build();
        listBuilder.add(mocktypeDefinition);

        // build Person
        mocktypeDefinition =
                new MockAsnSchemaTypeDefinitionBuilder("Person", AsnBuiltinType.Sequence).addComponentType("1", "firstName", "OCTET STRING")
                        .addComponentType("2", "lastName", "OCTET STRING")
                        .addComponentType("3", "title", "ENUMERATED")
                        .build();
        listBuilder.add(mocktypeDefinition);

        return listBuilder.build();
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
