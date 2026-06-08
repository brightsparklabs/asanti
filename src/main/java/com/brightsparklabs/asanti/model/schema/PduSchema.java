/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypePrimitiveAliased;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Models a "schema" for the decoded tags of a PDU.
 *
 * @param decodedTags All tags which could be decoded. Map is of form: { decodedTagString =>
 *     decodedTag }.
 * @param unmappedTags All tags which could not be decoded. Map is of form: { decodedTagString =>
 *     decodedTag }
 * @param allTags All tags (decoded and unmapped) found in the data. Map is of form: {
 *     decodedTagString => decodedTag }
 * @param aliasedTags The collection of decoded tags which have a {@link DecodedTag#type()} of
 *     {@link AsnSchemaTypePrimitiveAliased}.
 * @author brightSPARK Labs
 */
public record PduSchema(
        ImmutableMap<String, DecodedTag> decodedTags,
        ImmutableMap<String, DecodedTag> unmappedTags,
        ImmutableMap<String, DecodedTag> allTags,
        ImmutableSet<DecodedTag> aliasedTags) {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /** {@return a new builder} */
    static PduSchema.Builder builder() {
        return new PduSchema.Builder();
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

        /** All tags which could be decoded. Map is of form: { decodedTagString => decodedTag }. */
        final ImmutableMap.Builder<String, DecodedTag> decodedTags = ImmutableMap.builder();

        /**
         * All tags which could not be decoded. Map is of form: { decodedTagString => decodedTag }.
         */
        final ImmutableMap.Builder<String, DecodedTag> unmappedTags = ImmutableMap.builder();

        /**
         * The collection of decoded tags which have a {@link DecodedTag#type()} of {@link
         * AsnSchemaTypePrimitiveAliased}.
         */
        final ImmutableSet.Builder<DecodedTag> aliasedTags = ImmutableSet.builder();

        /**
         * All tags (decoded and unmapped) found in the data. Map is of form: {decodedTagString =>
         * decodedTag }.
         */
        final ImmutableMap.Builder<String, DecodedTag> allTags = ImmutableMap.builder();

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

        /**
         * Merges in the tags from the provided schema.
         *
         * @param schema The schema to add all the tags from to this builder.
         * @return The builder instance.
         */
        Builder addAll(final PduSchema schema) {
            decodedTags.putAll(schema.decodedTags());
            unmappedTags.putAll(schema.unmappedTags());
            aliasedTags.addAll(schema.aliasedTags());
            allTags.putAll(schema.allTags());
            return this;
        }

        /**
         * Adds a new tag to this builder instance.
         *
         * @param decodedTag The decoded tag to add.
         * @return The builder instance.
         */
        Builder add(final DecodedTag decodedTag) {
            allTags.put(decodedTag.tag(), decodedTag);
            if (!decodedTag.isFullyDecoded()) {
                unmappedTags.put(decodedTag.tag(), decodedTag);
            } else {
                decodedTags.put(decodedTag.tag(), decodedTag);
                if (decodedTag.type() instanceof AsnSchemaTypePrimitiveAliased) {
                    aliasedTags.add(decodedTag);
                }
            }
            return this;
        }

        /** {@return the built collection of the {@link UnpackedDecodedTags} */
        PduSchema build() {
            return new PduSchema(
                    decodedTags.build(),
                    unmappedTags.build(),
                    allTags.build(),
                    aliasedTags.build());
        }
    }
}
