/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks.model.schema;

import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentTypeGenerated;
import com.google.common.collect.ImmutableList;

import static org.mockito.Mockito.*;

/**
 * Utility class for obtaining mocked instances of {@link AsnSchemaComponentType} which conform to
 * the test ASN.1 schema defined in the {@linkplain README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockAsnSchemaComponentType
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Default constructor. This is hidden, use one of the factory methods instead.
     */
    private MockAsnSchemaComponentType()
    {
        // private constructor
    }

    /**
     * Creates a mock {@link AsnSchemaComponentType} instance
     *
     * @param tagName
     *         value to return for {@link AsnSchemaComponentType#getTagName()}
     * @param tag
     *         value to return for {@link AsnSchemaComponentType#getTag()}
     * @param typeName
     *         value to return for {@link AsnSchemaComponentType#getTypeName()}
     * @param isOptional
     *         value to return for {@link AsnSchemaComponentType#isOptional()}
     *
     * @return mock instance which returns the supplied values
     */
    public static AsnSchemaComponentType createMockedInstance(String tagName, String tag,
            String typeName, boolean isOptional)
    {
        final AsnSchemaComponentType mockedInstance = mock(AsnSchemaComponentType.class);
        when(mockedInstance.getTagName()).thenReturn(tagName);
        when(mockedInstance.getTag()).thenReturn(tag);
        when(mockedInstance.getTypeName()).thenReturn(typeName);
        when(mockedInstance.isOptional()).thenReturn(isOptional);
        return mockedInstance;
    }

    /**
     * Creates a mock {@link AsnSchemaComponentTypeGenerated} instance
     *
     * @param tagName
     *         value to return for {@link AsnSchemaComponentTypeGenerated#getTagName()}
     * @param tag
     *         value to return for {@link AsnSchemaComponentTypeGenerated#getTag()}
     * @param typeName
     *         value to return for {@link AsnSchemaComponentTypeGenerated#getTypeName()}
     * @param typeDefinitionText
     *         value to return for {@link AsnSchemaComponentTypeGenerated#getTypeName()}
     * @param isOptional
     *         value to return for {@link AsnSchemaComponentTypeGenerated#isOptional()}
     *
     * @return mock instance which returns the supplied values
     */
    public static AsnSchemaComponentTypeGenerated createMockedInstanceForGenerated(String tagName,
            String tag, String typeName, String typeDefinitionText, boolean isOptional)
    {
        final AsnSchemaComponentTypeGenerated mockedInstance
                = mock(AsnSchemaComponentTypeGenerated.class);
        when(mockedInstance.getTagName()).thenReturn(tagName);
        when(mockedInstance.getTag()).thenReturn(tag);
        when(mockedInstance.getTypeName()).thenReturn(typeName);
        when(mockedInstance.getTypeDefinitionText()).thenReturn(typeDefinitionText);
        when(mockedInstance.isOptional()).thenReturn(isOptional);
        return mockedInstance;
    }

    /**
     * Creates mock {@link AsnSchemaComponentType} instances conforming to the {@code Document} type
     * definition in the test ASN.1 schema defined in the {@linkplain README.md} file
     *
     * @return mock instances conforming to schema
     */
    public static ImmutableList<AsnSchemaComponentType> createMockedAsnSchemaComponentTypesForDocument()
    {
        final ImmutableList.Builder<AsnSchemaComponentType> listBuilder = ImmutableList.builder();
        AsnSchemaComponentType componentType = createMockedInstance("header", "1", "Header", false);
        listBuilder.add(componentType);
        componentType = createMockedInstance("body", "2", "Body", false);
        listBuilder.add(componentType);
        componentType = createMockedInstance("footer", "3", "Footer", false);
        listBuilder.add(componentType);
        componentType = createMockedInstance("dueDate", "4", "Date-Due", false);
        listBuilder.add(componentType);
        AsnSchemaComponentTypeGenerated componentTypeGenerated = createMockedInstanceForGenerated(
                "version",
                "5",
                "generated.Document.version",
                "SEQUENCE { majorVersion [0] INTEGER, minorVersion [1] INTEGER }",
                false);
        listBuilder.add(componentTypeGenerated);
        componentTypeGenerated = createMockedInstanceForGenerated("description",
                "6",
                "generated.Document.description",
                "SET { numberLines [0] INTEGER, summary [1] OCTET STRING }",
                true);
        listBuilder.add(componentTypeGenerated);
        return listBuilder.build();
    }

    /**
     * Creates mock {@link AsnSchemaComponentType} instances conforming to the {@code Body} type
     * definition in the test ASN.1 schema defined in the {@linkplain README.md} file
     *
     * @return mock instances conforming to schema
     */
    public static ImmutableList<AsnSchemaComponentType> createMockedAsnSchemaComponentTypesForBody()
    {
        final ImmutableList.Builder<AsnSchemaComponentType> listBuilder = ImmutableList.builder();
        AsnSchemaComponentType componentType = createMockedInstance("lastModified",
                "0",
                "ModificationMetadata",
                false);
        listBuilder.add(componentType);
        componentType = createMockedInstance("prefix", "1", "Section-Note", true);
        listBuilder.add(componentType);
        componentType = createMockedInstance("content", "2", "Section-Main", false);
        listBuilder.add(componentType);
        componentType = createMockedInstance("suffix", "3", "Section-Note", true);
        listBuilder.add(componentType);
        return listBuilder.build();
    }

    /**
     * Creates mock {@link AsnSchemaComponentType} instances conforming to the {@code Section-Main}
     * type definition in the test ASN.1 schema defined in the {@linkplain README.md} file
     *
     * @return mock instances conforming to schema
     */
    public static ImmutableList<AsnSchemaComponentType> createMockedAsnSchemaComponentTypesForSectionMain()
    {
        final ImmutableList.Builder<AsnSchemaComponentType> listBuilder = ImmutableList.builder();
        AsnSchemaComponentType componentType = createMockedInstance("text",
                "1",
                "OCTET STRING",
                true);
        listBuilder.add(componentType);
        componentType = createMockedInstance("paragraphs", "2", "Paragraph", false);
        listBuilder.add(componentType);
        AsnSchemaComponentTypeGenerated componentTypeGenerated = createMockedInstanceForGenerated(
                "sections",
                "3",
                "generated.Section-Main.sections",
                "SET OF SET { number [1] INTEGER, text [2] OCTET STRING }",
                false);
        listBuilder.add(componentTypeGenerated);

        return listBuilder.build();
    }

    /**
     * Creates mock {@link AsnSchemaComponentType} instances conforming to the {@code Person} type
     * definition in the test ASN.1 schema defined in the {@linkplain README.md} file
     *
     * @return mock instances conforming to schema
     */
    public static ImmutableList<AsnSchemaComponentType> createMockedAsnSchemaComponentTypesForPerson()
    {
        final ImmutableList.Builder<AsnSchemaComponentType> listBuilder = ImmutableList.builder();
        AsnSchemaComponentType componentType = createMockedInstance("firstName",
                "1",
                "OCTET STRING",
                false);
        listBuilder.add(componentType);
        componentType = createMockedInstance("lastName", "2", "OCTET STRING", false);
        listBuilder.add(componentType);
        AsnSchemaComponentTypeGenerated componentTypeGenerated = createMockedInstanceForGenerated(
                "title",
                "3",
                "generated.Person.title",
                "ENUMERATED { mr, mrs, ms, dr, rev }",
                true);
        listBuilder.add(componentTypeGenerated);
        componentType = createMockedInstance("gender", "", "Gender", true);
        listBuilder.add(componentType);
        componentTypeGenerated = createMockedInstanceForGenerated("maritalStatus",
                "",
                "generated.Person.maritalStatus",
                "CHOICE { Married [0], Single [1] }",
                false);
        listBuilder.add(componentTypeGenerated);
        return listBuilder.build();
    }
}
