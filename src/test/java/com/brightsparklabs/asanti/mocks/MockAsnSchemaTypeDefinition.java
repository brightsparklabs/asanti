/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks;

import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;
import com.google.common.collect.ImmutableList;

/**
 * Utility class for obtaining mocked instances of
 * {@link AsnSchemaTypeDefinition} which conform to the test ASN.1 schema
 * defined in the {@linkplain README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockAsnSchemaTypeDefinition
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Default constructor. This is hidden, use
     * {@link #builder(String, AsnBuiltinType)} instead.
     */
    private MockAsnSchemaTypeDefinition()
    {
        // private constructor
    }

    /**
     * Return a builder for creating instances of
     * {@link MockAsnSchemaTypeDefinition}
     *
     * @param name
     *            name of the defined type
     *
     * @param builtinType
     *            the underlying ASN.1 type of the defined type
     */
    public static MockAsnSchemaTypeDefinitionBuilder builder(String name, AsnBuiltinType builtinType)
    {
        return new MockAsnSchemaTypeDefinitionBuilder(name, builtinType);
    }

    /**
     * Creates mock {@link AsnSchemaTypeDefinition} instances conforming to the
     * 'Document-PDU' module in the test ASN.1 schema defined in the
     * {@linkplain README.md} file
     *
     * @return mock instances for use within the 'Document-PDU' module
     */
    public static ImmutableList<AsnSchemaTypeDefinition> createMockedAsnSchemaTypeDefinitionsForDocumentPdu()
    {
        final ImmutableList.Builder<AsnSchemaTypeDefinition> listBuilder = ImmutableList.builder();

        // build Document
        AsnSchemaTypeDefinition mocktypeDefinition =
                MockAsnSchemaTypeDefinition.builder("Document", AsnBuiltinType.Sequence)
                        .addComponentType("1", "header", "Header")
                        .addComponentType("2", "body", "Body")
                        .addComponentType("3", "footer", "Footer")
                        .build();
        listBuilder.add(mocktypeDefinition);

        // build Header
        mocktypeDefinition = MockAsnSchemaTypeDefinition.builder("Header", AsnBuiltinType.Sequence)
                .addComponentType("0", "published", "PublishedMetadata")
                .build();
        listBuilder.add(mocktypeDefinition);

        // build Body
        mocktypeDefinition = MockAsnSchemaTypeDefinition.builder("Body", AsnBuiltinType.Sequence)
                .addComponentType("0", "lastModified", "ModificationMetadata")
                .addComponentType("1", "prefix", "Section-Note")
                .addComponentType("2", "content", "Section-Main")
                .addComponentType("3", "suffix", "Section-Note")
                .build();
        listBuilder.add(mocktypeDefinition);

        // build Footer
        mocktypeDefinition = MockAsnSchemaTypeDefinition.builder("Footer", AsnBuiltinType.Sequence)
                .addComponentType("0", "author", "Person")
                .build();
        listBuilder.add(mocktypeDefinition);

        // build PublishedMetadata
        mocktypeDefinition = MockAsnSchemaTypeDefinition.builder("PublishedMetadata", AsnBuiltinType.Sequence)
                .addComponentType("1", "date", "GeneralizedTime")
                .addComponentType("2", "country", "OCTET STRING")
                .build();
        listBuilder.add(mocktypeDefinition);

        // build PublishedMetadata
        mocktypeDefinition = MockAsnSchemaTypeDefinition.builder("PublishedMetadata", AsnBuiltinType.Sequence)
                .addComponentType("1", "date", "GeneralizedTime")
                .addComponentType("2", "country", "OCTET STRING")
                .build();
        listBuilder.add(mocktypeDefinition);

        // build ModificationMetadata
        mocktypeDefinition = MockAsnSchemaTypeDefinition.builder("ModificationMetadata", AsnBuiltinType.Sequence)
                .addComponentType("0", "date", "GeneralizedTime")
                .addComponentType("1", "modifiedBy", "Person")
                .build();
        listBuilder.add(mocktypeDefinition);

        // build Section-Note
        mocktypeDefinition = MockAsnSchemaTypeDefinition.builder("Section-Note", AsnBuiltinType.Sequence)
                .addComponentType("1", "text", "OCTET STRING")
                .build();
        listBuilder.add(mocktypeDefinition);

        // build Section-Main
        mocktypeDefinition = MockAsnSchemaTypeDefinition.builder("Section-Main", AsnBuiltinType.Sequence)
                .addComponentType("1", "text", "OCTET STRING")
                .addComponentType("2", "paragraphs", "SEQUENCE OF Paragraph")
                .build();
        listBuilder.add(mocktypeDefinition);

        // build Paragraph
        mocktypeDefinition = MockAsnSchemaTypeDefinition.builder("Paragraph", AsnBuiltinType.Sequence)
                .addComponentType("1", "title", "OCTET STRING")
                .addComponentType("2", "contributor", "Person")
                .addComponentType("3", "points", "SEQUENCE OF OCTET STRING")
                .build();
        listBuilder.add(mocktypeDefinition);

        return listBuilder.build();
    }

    /**
     * Creates mock {@link AsnSchemaTypeDefinition} instances conforming to the
     * 'People-Protocol' module in the test ASN.1 schema defined in the
     * {@linkplain README.md} file
     *
     * @return mock instances for use within the 'PeopleProtocol' module
     */
    public static ImmutableList<AsnSchemaTypeDefinition> createMockedAsnSchemaTypeDefinitionsForPeopleProtocol()
    {
        final ImmutableList.Builder<AsnSchemaTypeDefinition> listBuilder = ImmutableList.builder();

        // build People
        AsnSchemaTypeDefinition mocktypeDefinition =
                MockAsnSchemaTypeDefinition.builder("People", AsnBuiltinType.SetOf)
                        .build();
        listBuilder.add(mocktypeDefinition);

        // build Person
        mocktypeDefinition = MockAsnSchemaTypeDefinition.builder("Person", AsnBuiltinType.Sequence)
                .addComponentType("1", "firstName", "OCTET STRING")
                .addComponentType("2", "lastName", "OCTET STRING")
                .addComponentType("3", "title", "ENUMERATED")
                .build();
        listBuilder.add(mocktypeDefinition);

        return listBuilder.build();
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
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        final AsnSchemaTypeDefinition mockedInstance = mock(AsnSchemaTypeDefinition.class);

        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor
         *
         *
         * @param name
         *            name of the defined type
         *
         * @param builtinType
         *            the underlying ASN.1 type of the defined type
         */
        private MockAsnSchemaTypeDefinitionBuilder(String name, AsnBuiltinType builtinType)
        {
            when(mockedInstance.getBuiltinType()).thenReturn(builtinType);
            when(mockedInstance.getName()).thenReturn(name);
        }

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

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