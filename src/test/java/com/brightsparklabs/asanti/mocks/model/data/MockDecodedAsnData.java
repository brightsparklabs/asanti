/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.mocks.model.data;

import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import java.util.Optional;

/**
 * Utility class for obtaining mocked instances of {@link AsantiAsnData} which conform to the test
 * ASN.1 schema defined in the {@code README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockDecodedAsnData {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Return a builder for creating mocked instances of {@link AsantiAsnData}.
     *
     * <p>By default the returned builder will include the following two tags:
     *
     * <ul>
     *   <li>"/null" which returns {@code null} when {@link AsantiAsnData#getBytes(String)} is
     *       called
     *   <li>"/empty" which returns an empty byte array when {@link AsantiAsnData#getBytes(String)}
     *       is called
     * </ul>
     *
     * @param type the ASN.1 Type Definition to return for all calls to {@link
     *     AsantiAsnData#getType(String)}
     */
    public static Builder builder(AsnSchemaType type) throws Exception {
        return new Builder(type);
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Builder
    // -------------------------------------------------------------------------

    /**
     * Builder for creating mocked instances of {@link AsantiAsnData}
     *
     * @author brightSPARK Labs
     */
    public static class Builder {
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /** instance being built */
        final AsantiAsnData mockedInstance = mock(AsantiAsnData.class);

        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor
         *
         * @param type the ASN.1 Type Definition to return for all calls of {@link
         *     AsantiAsnData#getType(String)}
         */
        private Builder(AsnSchemaType type) {
            when(mockedInstance.getType(anyString())).thenReturn(Optional.of(type));
            when(mockedInstance.getBytes("/empty")).thenReturn(Optional.of(new byte[0]));
            when(mockedInstance.getType("/null")).thenReturn(Optional.of(type));
            when(mockedInstance.getBytes("/null")).thenReturn(Optional.empty());
        }

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

        /**
         * Sets the constraint on this definition
         *
         * @param constraint constraint to use
         * @return this builder
         */
        /**
         * Adds the specified bytes to the mocked instance
         *
         * @param tag tag the bytes are associated with (e.g. {@code
         *     "/Document/header/published/date"})
         * @param bytes bytes to return for the specified tag
         * @return this instance
         */
        public Builder addBytes(String tag, byte[] bytes) {
            when(mockedInstance.getBytes(tag)).thenReturn(Optional.of(bytes));
            return this;
        }

        /**
         * Creates a mocked instance from the data in this builder
         *
         * @return a mocked instance of {@link AsantiAsnData}
         */
        public AsantiAsnData build() {
            return mockedInstance;
        }
    }
}
