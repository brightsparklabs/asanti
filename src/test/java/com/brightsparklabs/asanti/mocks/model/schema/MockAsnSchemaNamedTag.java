/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.mocks.model.schema;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.google.common.collect.ImmutableList;

/**
 * Utility class for obtaining mocked instances of {@link AsnSchemaNamedTag} which conform to the
 * test ASN.1 schema defined in the {@linkplain README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockAsnSchemaNamedTag {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /** Default constructor. This is hidden, use one of the factory methods instead. */
    private MockAsnSchemaNamedTag() {
        // private constructor
    }

    /**
     * Creates a mock {@link AsnSchemaNamedTag} instance
     *
     * @param tagName value to return for {@link AsnSchemaNamedTag#getTagName()}
     * @param tag value to return for {@link AsnSchemaNamedTag#getTag()}
     * @return mock instance which returns the supplied values
     */
    public static AsnSchemaNamedTag createMockedInstance(String tagName, String tag) {
        final AsnSchemaNamedTag mockedInstance = mock(AsnSchemaNamedTag.class);
        when(mockedInstance.getTagName()).thenReturn(tagName);
        when(mockedInstance.getTag()).thenReturn(tag);
        return mockedInstance;
    }

    /**
     * Creates mock {@link AsnSchemaNamedTag} instances conforming to the {@code title} component
     * type within the {@code Person} type definition in the test ASN.1 schema defined in the
     * {@linkplain README.md} file
     *
     * @return mock instances conforming to schema
     */
    public static ImmutableList<AsnSchemaNamedTag> createMockedAsnSchemaNamedTagsForPerson() {
        final ImmutableList.Builder<AsnSchemaNamedTag> listBuilder = ImmutableList.builder();
        AsnSchemaNamedTag option = createMockedInstance("mr", "");
        listBuilder.add(option);
        option = createMockedInstance("mrs", "");
        listBuilder.add(option);
        option = createMockedInstance("ms", "");
        listBuilder.add(option);
        option = createMockedInstance("dr", "");
        listBuilder.add(option);
        option = createMockedInstance("rev", "");
        listBuilder.add(option);
        return listBuilder.build();
    }

    /**
     * Creates mock {@link AsnSchemaNamedTag} instances conforming to the {@code Gender} type
     * definition in the test ASN.1 schema defined in the {@linkplain README.md} file
     *
     * @return mock instances conforming to schema
     */
    public static ImmutableList<AsnSchemaNamedTag> createMockedAsnSchemaNamedTagsForGender() {
        final ImmutableList.Builder<AsnSchemaNamedTag> listBuilder = ImmutableList.builder();
        AsnSchemaNamedTag option = createMockedInstance("male", "0");
        listBuilder.add(option);
        option = createMockedInstance("female", "1");
        listBuilder.add(option);
        return listBuilder.build();
    }

    /**
     * Creates mock {@link AsnSchemaNamedTag} instances conforming to the {@code Date-Due} type
     * definition in the test ASN.1 schema defined in the {@linkplain README.md} file
     *
     * @return
     */
    public static ImmutableList<AsnSchemaNamedTag> createMockedAsnNamedTagsForDateDue() {
        final ImmutableList.Builder<AsnSchemaNamedTag> listBuilder = ImmutableList.builder();
        AsnSchemaNamedTag option = createMockedInstance("tomorrow", "0");
        listBuilder.add(option);
        option = createMockedInstance("three-day", "1");
        listBuilder.add(option);
        option = createMockedInstance("week", "2");
        listBuilder.add(option);
        return listBuilder.build();
    }
}
