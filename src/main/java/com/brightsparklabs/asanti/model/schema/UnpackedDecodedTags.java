/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.data.RawAsnData;
import com.brightsparklabs.asanti.model.data.RawAsnDataImpl;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypePrimitiveAliased;
import com.google.common.collect.ImmutableMap;

/**
 * A container for an "unpacked" raw ASN data, where {@link AsnSchemaTypePrimitiveAliased aliased}
 * tag values have been extracted, and the associated decoded tags as a {@link PduSchema}.
 *
 * @param unpackedAsnData The "unpacked" raw ASN data.
 * @param pduSchema The decoded tags for the raw ASN data.
 * @author brightSPARK Labs
 */
public record UnpackedDecodedTags(RawAsnData unpackedAsnData, PduSchema pduSchema) {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /** {@return a new builder} */
    static Builder builder() {
        return new Builder();
    }

    // -------------------------------------------------------------------------
    // INNER CLASSES
    // -------------------------------------------------------------------------

    /**
     * Transfer object to support building the {@link UnpackedDecodedTags}.
     *
     * @author brightSPARK Labs
     */
    public static class Builder {
        // ---------------------------------------------------------------------
        // INSTANCE METHODS
        // ---------------------------------------------------------------------

        /** Map of raw ASN tags to their data. */
        private final ImmutableMap.Builder<String, byte[]> rawAsnBuilder = ImmutableMap.builder();

        /** The builder for the PDU schema. */
        private final PduSchema.Builder pduSchemaBuilder = PduSchema.builder();

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

        /**
         * Merges in the tags from the provided schema.
         *
         * @param schema The schema to add all the tags from to this builder.
         * @return The builder instance.
         */
        Builder add(final PduSchema schema) {
            pduSchemaBuilder.addAll(schema);
            return this;
        }

        /**
         * Adds a raw tag and value to the RawAsnData.
         *
         * @param rawTag The raw tag.
         * @param value The value.
         * @return The builder instance.
         */
        Builder add(final String rawTag, final byte[] value) {
            rawAsnBuilder.put(rawTag, value);
            return this;
        }

        /**
         * Adds a collection of raw tags to values to the RawAsnData.
         *
         * @param rawAsnData The raw tags and values to add.
         * @return The builder instance.
         */
        Builder add(final ImmutableMap<String, byte[]> rawAsnData) {
            rawAsnBuilder.putAll(rawAsnData);
            return this;
        }

        /**
         * Adds a decoded tag to the PduSchema.
         *
         * @param decodedTag The decoded tag to add.
         * @return The builder instance.
         */
        Builder add(final DecodedTag decodedTag) {
            pduSchemaBuilder.add(decodedTag);
            return this;
        }

        /** {@return the built collection of the {@link UnpackedDecodedTags} */
        UnpackedDecodedTags build() {
            return new UnpackedDecodedTags(
                    new RawAsnDataImpl(rawAsnBuilder.build()), pduSchemaBuilder.build());
        }
    }
}
