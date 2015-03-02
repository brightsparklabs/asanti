/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks.model.schema;

import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.collect.ImmutableList;

/**
 * Utility class for obtaining mocked instances of
 * {@link AsnSchemaComponentType} which conform to the test ASN.1 schema defined
 * in the {@linkplain README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockAsnSchemaComponentType
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Default constructor. This is hidden, use one of the factory methods
     * instead.
     */
    private MockAsnSchemaComponentType()
    {
        // private constructor
    }

    /**
     * Creates a mock {@link AsnSchemaComponentType} instance
     *
     * @param tagName
     *            value to return for
     *            {@link AsnSchemaComponentType#getTagName()}
     *
     * @param tag
     *            value to return for {@link AsnSchemaComponentType#getTag()}
     *
     * @param typeName
     *            value to return for
     *            {@link AsnSchemaComponentType#getTypeName()}
     *
     * @param isOptional
     *            value to return for
     *            {@link AsnSchemaComponentType#isOptional()}
     *
     * @return mock instance which returns the supplied values
     */
    public static AsnSchemaComponentType createMockedInstance(String tagName, String tag, String typeName,
            boolean isOptional)
    {
        final AsnSchemaComponentType mockedInstance = mock(AsnSchemaComponentType.class);
        when(mockedInstance.getTagName()).thenReturn(tagName);
        when(mockedInstance.getTag()).thenReturn(tag);
        when(mockedInstance.getTypeName()).thenReturn(typeName);
        when(mockedInstance.isOptional()).thenReturn(isOptional);
        return mockedInstance;
    }

    /**
     * Creates mock {@link AsnSchemaComponentType} instances conforming to the
     * {@code Document} type definition in the test ASN.1 schema defined in the
     * {@linkplain README.md} file
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
        return listBuilder.build();
    }

    /**
     * Creates mock {@link AsnSchemaComponentType} instances conforming to the
     * {@code Body} type definition in the test ASN.1 schema defined in the
     * {@linkplain README.md} file
     *
     * @return mock instances conforming to schema
     */
    public static ImmutableList<AsnSchemaComponentType> createMockedAsnSchemaComponentTypesForBody()
    {
        final ImmutableList.Builder<AsnSchemaComponentType> listBuilder = ImmutableList.builder();
        AsnSchemaComponentType componentType = createMockedInstance("lastModified", "0", "ModificationMetadata", false);
        listBuilder.add(componentType);
        componentType = createMockedInstance("prefix", "1", "Section-Note", true);
        listBuilder.add(componentType);
        componentType = createMockedInstance("content", "2", "Section-Main", false);
        listBuilder.add(componentType);
        componentType = createMockedInstance("suffix", "3", "Section-Note", true);
        listBuilder.add(componentType);
        return listBuilder.build();
    }
}
