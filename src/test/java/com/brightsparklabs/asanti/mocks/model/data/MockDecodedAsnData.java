/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks.model.data;

import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.google.common.collect.ImmutableSet;

import static org.mockito.Mockito.*;

/**
 * Utility class for obtaining mocked instances of {@link DecodedAsnData} which conform to the test
 * ASN.1 schema defined in the {@code README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockDecodedAsnData
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Return a builder for creating mocked instances of {@link DecodedAsnData}.
     *
     * By default the returned builder will include the following two tags:
     *
     * <ul>
     *
     * <li>"/null" which returns {@code null} when {@link DecodedAsnData#getBytes(String)} is
     * called</li>
     *
     * <li>"/empty" which returns an empty byte array when {@link DecodedAsnData#getBytes(String)}
     * is called</li>
     *
     * </ul>
     *
     * @param type
     *         the ASN.1 Type Definition to return for all calls to {@link
     *         DecodedAsnData#getType(String)}
     */
    public static Builder builder(AsnSchemaType type) throws Exception
    {
        return new Builder(type);
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Builder
    // -------------------------------------------------------------------------

    /**
     * Builder for creating mocked instances of {@link DecodedAsnData}
     *
     * @author brightSPARK Labs
     */
    public static class Builder
    {
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /** instance being built */
        final DecodedAsnData mockedInstance = mock(DecodedAsnData.class);

        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor
         *
         * @param type
         *         the ASN.1 Type Definition to return for all calls of {@link
         *         DecodedAsnData#getType(String)}
         */
        private Builder(AsnSchemaType type)
        {
            when(mockedInstance.getType(anyString())).thenReturn(type);
            when(mockedInstance.getBytes("/empty")).thenReturn(new byte[0]);
            when(mockedInstance.getType("/null")).thenReturn(type);
            when(mockedInstance.getBytes("/null")).thenReturn(null);
            when(mockedInstance.getAllTypes(anyString())).thenReturn(ImmutableSet.<AsnSchemaType>of());

        }

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

        /**
         * Sets the constraint on this definition
         *
         * @param constraint
         *         constraint to use
         *
         * @return this builder
         */
        /**
         * Adds the specified bytes to the mocked instance
         *
         * @param tag
         *         tag the bytes are associated with (e.g. {@code "/Document/header/published/date"})
         * @param bytes
         *         bytes to return for the specified tag
         *
         * @return this instance
         */
        public Builder addBytes(String tag, byte[] bytes)
        {
            when(mockedInstance.getBytes(tag)).thenReturn(bytes);
            return this;
        }

        /**
         * Creates a mocked instance from the data in this builder
         *
         * @return a mocked instance of {@link DecodedAsnData}
         */
        public DecodedAsnData build()
        {
            return mockedInstance;
        }
    }
}
