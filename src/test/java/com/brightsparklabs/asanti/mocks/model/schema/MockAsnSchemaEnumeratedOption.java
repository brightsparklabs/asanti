/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks.model.schema;

import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaEnumeratedOption;
import com.google.common.collect.ImmutableList;

/**
 * Utility class for obtaining mocked instances of
 * {@link AsnSchemaEnumeratedOption} which conform to the test ASN.1 schema
 * defined in the {@linkplain README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockAsnSchemaEnumeratedOption
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Default constructor. This is hidden, use one of the factory methods
     * instead.
     */
    private MockAsnSchemaEnumeratedOption()
    {
        // private constructor
    }

    /**
     * Creates a mock {@link AsnSchemaEnumeratedOption} instance
     *
     * @param tagName
     *            value to return for
     *            {@link AsnSchemaEnumeratedOption#getTagName()}
     *
     * @param tag
     *            value to return for {@link AsnSchemaEnumeratedOption#getTag()}
     *
     * @return mock instance which returns the supplied values
     */
    public static AsnSchemaEnumeratedOption createMockedInstance(String tagName, String tag)
    {
        final AsnSchemaEnumeratedOption mockedInstance = mock(AsnSchemaEnumeratedOption.class);
        when(mockedInstance.getTagName()).thenReturn(tagName);
        when(mockedInstance.getTag()).thenReturn(tag);
        return mockedInstance;
    }

    /**
     * Creates mock {@link AsnSchemaEnumeratedOption} instances conforming to
     * the {@code title} component type within the {@code Person} type
     * definition in the test ASN.1 schema defined in the {@linkplain README.md}
     * file
     *
     * @return mock instances conforming to schema
     */
    public static ImmutableList<AsnSchemaEnumeratedOption> createMockedAsnSchemaEnumeratedOptionsForPerson()
    {
        final ImmutableList.Builder<AsnSchemaEnumeratedOption> listBuilder = ImmutableList.builder();
        AsnSchemaEnumeratedOption option = createMockedInstance("mr", "");
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
     * Creates mock {@link AsnSchemaEnumeratedOption} instances conforming to
     * the {@code Gender} type definition in the test ASN.1 schema defined in
     * the {@linkplain README.md} file
     *
     * @return mock instances conforming to schema
     */
    public static ImmutableList<AsnSchemaEnumeratedOption> createMockedAsnSchemaEnumeratedOptionsForGender()
    {
        final ImmutableList.Builder<AsnSchemaEnumeratedOption> listBuilder = ImmutableList.builder();
        AsnSchemaEnumeratedOption option = createMockedInstance("male", "0");
        listBuilder.add(option);
        option = createMockedInstance("female", "1");
        listBuilder.add(option);
        return listBuilder.build();
    }
}
