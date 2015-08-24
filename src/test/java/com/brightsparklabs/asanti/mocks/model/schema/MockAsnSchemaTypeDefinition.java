/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks.model.schema;

import com.brightsparklabs.assam.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.google.common.collect.ImmutableList;

import static org.mockito.Mockito.*;

/**
 * Utility class for obtaining mocked instances of {@link AsnSchemaTypeDefinition} which
 * conform to the test ASN.1 schema defined in the {@code README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockAsnSchemaTypeDefinition
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Default constructor. This is hidden, use {@link #builder(String, AsnBuiltinType)} instead.
     */
    private MockAsnSchemaTypeDefinition()
    {
        // private constructor
    }

    /**
     * Return a builder for creating instances of {@link MockAsnSchemaTypeDefinition}
     *
     * @param name
     *         name of the defined type
     * @param builtinType
     *         the underlying ASN.1 type of the defined type
     */
/* TODO ASN-138 now that AsnSchemaTypeDefinition is so simple it is questionable as to whether
    it needs a builder
    public static MockAsnSchemaTypeDefinitionBuilder builder(String name,
            AsnBuiltinType builtinType)
    {
        return new MockAsnSchemaTypeDefinitionBuilder(name, builtinType);
    }
*/
    /**
     * Creates mock {@link AsnSchemaTypeDefinition} instances conforming to the
     * 'Document-PDU' module in the test ASN.1 schema defined in the {@code README.md} file
     *
     * @return mock instances for use within the 'Document-PDU' module
     */
    public static ImmutableList<AsnSchemaTypeDefinition> createMockedAsnSchemaTypeDefinitionsForDocumentPdu()
    {
        final ImmutableList.Builder<AsnSchemaTypeDefinition> listBuilder
                = ImmutableList.builder();


        listBuilder.add(createMockedAsnSchemaTypeDefinition("Document",
                MockAsnSchemaType.createMockedAsnSchemaTypeForDocumentPdu()));

        listBuilder.add(createMockedAsnSchemaTypeDefinition("Header",
                MockAsnSchemaType.getDocumentHeader()));
        listBuilder.add(createMockedAsnSchemaTypeDefinition("Body",
                MockAsnSchemaType.getDocumentBody()));
        listBuilder.add(createMockedAsnSchemaTypeDefinition("Footer",
                MockAsnSchemaType.getDocumentFooter()));


        listBuilder.add(createMockedAsnSchemaTypeDefinition("PublishedMetadata",
                MockAsnSchemaType.getDocumentPublishedMetadata()));
        listBuilder.add(createMockedAsnSchemaTypeDefinition("ModificationMetadata",
                MockAsnSchemaType.getDocumentModificationMetadataLinked()));
        listBuilder.add(createMockedAsnSchemaTypeDefinition("Section-Note",
                MockAsnSchemaType.getDocumentSectionNote()));
        listBuilder.add(createMockedAsnSchemaTypeDefinition("Section-Main",
                MockAsnSchemaType.getDocumentSectionMain()));
        listBuilder.add(createMockedAsnSchemaTypeDefinition("Paragraph",
                MockAsnSchemaType.getDocumentParagraph()));
        listBuilder.add(createMockedAsnSchemaTypeDefinition("References",
                MockAsnSchemaType.getDocumentReferences()));
        listBuilder.add(createMockedAsnSchemaTypeDefinition("Due-Date",
                MockAsnSchemaType.getDocumentDueDate()));

        return listBuilder.build();
    }

    /**
     * Creates mock {@link AsnSchemaTypeDefinition} instances conforming to the
     * 'People-Protocol' module in the test ASN.1 schema defined in the {@code README.md} file
     *
     * @return mock instances for use within the 'PeopleProtocol' module
     */
    public static ImmutableList<AsnSchemaTypeDefinition> createMockedAsnSchemaTypeDefinitionsForPeopleProtocol()
    {
/* TODO ASN-138
        final ImmutableList.Builder<AsnSchemaTypeDefinition> listBuilder
                = ImmutableList.builder();

        // build People
        AsnSchemaTypeDefinition mocktypeDefinition = MockAsnSchemaTypeDefinition.builder(
                "People",
                AsnBuiltinType.SetOf).build();
        listBuilder.add(mocktypeDefinition);

        // build Person
        mocktypeDefinition = MockAsnSchemaTypeDefinition.builder("Person", AsnBuiltinType.Sequence)
                .addComponentType("1", "firstName", "OCTET STRING")
                .addComponentType("2", "lastName", "OCTET STRING")
                .addComponentType("3", "title", "generated.Person.title")
                .addComponentType("4", "gender", "Gender")
                .addComponentType("5", "maritalStatus", "generated.Person.maritalStatus")
                .build();
        listBuilder.add(mocktypeDefinition);

        // build Gender
        mocktypeDefinition = MockAsnSchemaTypeDefinition.builder("Gender",
                AsnBuiltinType.Enumerated).build();
        listBuilder.add(mocktypeDefinition);

        return listBuilder.build();
*/
        return ImmutableList.<AsnSchemaTypeDefinition>of();
    }

    /**
     * Creates a mocked instance of {@link AsnSchemaTypeDefinition}
     *
     * @param name
     *          value to return for {@link AsnSchemaTypeDefinition#getName()} method
     * @param type
     *          value to return for {@link AsnSchemaTypeDefinition#getType()} method
     * @return
     */
    public static AsnSchemaTypeDefinition createMockedAsnSchemaTypeDefinition(String name,
            AsnSchemaType type)
    {

        final AsnSchemaTypeDefinition mockedInstance = mock(AsnSchemaTypeDefinition.class);

        when(mockedInstance.getName()).thenReturn(name);
        when(mockedInstance.getType()).thenReturn(type);
        return mockedInstance;
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: MockedInstanceBuilder
    // -------------------------------------------------------------------------

// TODO ASN-138 It is questionable as to whether a builder is needed for this simple object
//  If it is then it should try to delegate down to the AsnSchemaType object/mock underneath.
//    /**
//     * Builder for creating mocked instances of {@link AsnSchemaTypeDefinition}
//     *
//     * @author brightSPARK Labs
//     */
//    public static class MockAsnSchemaTypeDefinitionBuilder
//    {
//        // ---------------------------------------------------------------------
//        // INSTANCE VARIABLES
//        // ---------------------------------------------------------------------
//
//        final AsnSchemaTypeDefinition mockedInstance
//                = mock(AsnSchemaTypeDefinition.class);
//
//        // ---------------------------------------------------------------------
//        // CONSTRUCTION
//        // ---------------------------------------------------------------------
//
//        /**
//         * Default constructor
//         *
//         * @param name
//         *         name of the defined type
//         * @param builtinType
//         *         the underlying ASN.1 type of the defined type
//         */
//        private MockAsnSchemaTypeDefinitionBuilder(String name, AsnBuiltinType builtinType)
//        {
//            when(mockedInstance.getName()).thenReturn(name);
//            when(mockedInstance.getType()).thenReturn(AsnSchemaType.NULL);
//        }
//
//        // ---------------------------------------------------------------------
//        // PUBLIC METHODS
//        // ---------------------------------------------------------------------
//
//        /**
//         * Sets the constraint on this definition
//         *
//         * @param constraint
//         *         constraint to use
//         *
//         * @return this builder
//         */
//        public MockAsnSchemaTypeDefinitionBuilder setConstraint(AsnSchemaConstraint constraint)
//        {
//            when(mockedInstance.getConstraint()).thenReturn(constraint);
//            return this;
//        }
//
//        /**
//         * Add a component type to this definition
//         *
//         * @param tag
//         *         tag of the component type
//         * @param tagName
//         *         tag name of the component type
//         * @param typeName
//         *         type name of the component type
//         *
//         * @return this builder
//         */
//        public MockAsnSchemaTypeDefinitionBuilder addComponentType(String tag, String tagName,
//                String typeName)
//        {
//            when(mockedInstance.getTagName(tag)).thenReturn(tagName);
//            when(mockedInstance.getTypeName(tag)).thenReturn(typeName);
//            return this;
//        }
//
//        /**
//         * Add a SEQUENCE OF component type to this definition
//         *
//         * @param tag
//         *         tag of the component type
//         * @param tagName
//         *         tag name of the component type
//         * @param typeName
//         *         type name of the SEQUENCE OF component type E.g. {@code "SEQUENCE OF Paragraph"}
//         *
//         * @return this builder
//         */
//        public MockAsnSchemaTypeDefinitionBuilder addSeqeunceOfComponentType(String tag,
//                String tagName, String typeName)
//        {
//            final String elementTypeName = typeName.replace("SEQUENCE OF ", "");
//            return addCollectionOfComponentType(tag, tagName, elementTypeName);
//        }
//
//        /**
//         * Add a SET OF component type to this definition
//         *
//         * @param tag
//         *         tag of the component type
//         * @param tagName
//         *         tag name of the component type
//         * @param typeName
//         *         type name of the SET OF component type E.g. {@code "SET OF Person"}
//         *
//         * @return this builder
//         */
//        public MockAsnSchemaTypeDefinitionBuilder addSetOfComponentType(String tag, String tagName,
//                String typeName)
//        {
//            final String elementTypeName = typeName.replace("SET OF ", "");
//            return addCollectionOfComponentType(tag, tagName, elementTypeName);
//        }
//
//        /**
//         * Creates a mocked instance from the data in this builder
//         *
//         * @return a mocked instance of {@link AsnSchemaTypeDefinition}
//         */
//        public AsnSchemaTypeDefinition build()
//        {
//            return mockedInstance;
//        }
//
//        // ---------------------------------------------------------------------
//        // PRIVATE METHODS
//        // ---------------------------------------------------------------------
//
//        /**
//         * Add a collection of (SEQUENCE OF/SET OF) component type to this definition
//         *
//         * @param tag
//         *         tag of the component type
//         * @param tagName
//         *         tag name of the component type
//         * @param elementTypeName
//         *         type name of the SEQUENCE OF or SET OF element type E.g. for {@code "SET OF
//         *         Person"} this would be {@code "Person"}
//         *
//         * @return this builder
//         */
//        private MockAsnSchemaTypeDefinitionBuilder addCollectionOfComponentType(String tag,
//                final String tagName, String elementTypeName)
//        {
//            // handle without index supplied
//            when(mockedInstance.getTagName(tag)).thenReturn(tagName);
//            when(mockedInstance.getTypeName(tag)).thenReturn(elementTypeName);
//
//            // handle with index supplied
//            final String regex = tag + "\\[\\d+\\]";
//            when(mockedInstance.getTagName(matches(regex))).thenAnswer(new Answer<String>()
//            {
//                @Override
//                public String answer(InvocationOnMock invocation) throws Throwable
//                {
//                    final String rawTag = (String) invocation.getArguments()[0];
//                    final String tagIndex = rawTag.replaceFirst(".+(\\[.+\\])", "$1");
//                    return tagName + tagIndex;
//                }
//            });
//            when(mockedInstance.getTypeName(matches(regex))).thenReturn(elementTypeName);
//            return this;
//        }
//    }
}
