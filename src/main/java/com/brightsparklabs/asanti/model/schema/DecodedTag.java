/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.google.common.base.Strings;

/**
 * Represent a decoded ASN.1 tag created by decoding a raw tag path.
 *
 * @param tag The full path of the decoded tag.
 * @param rawTag The full path of the raw tag.
 * @param type The type of construct represented by the tag.
 * @param isFullyDecoded Whether the raw tag was completely decoded.
 * @author brightSPARK Labs
 */
public record DecodedTag(String tag, String rawTag, AsnSchemaType type, boolean isFullyDecoded) {
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param tag The full path of the decoded tag (e.g. {@code "/Document/header/published/date"}).
     * @param rawTag The full path of the raw tag (e.g. {@code "/0[1]/0[0]/0[1]"}).
     * @param type The type of construct represented by the tag.
     * @param isFullyDecoded Whether the raw tag was completely decoded.
     * @throws NullPointerException If parameters are {@code null}.
     * @throws IllegalArgumentException If {@code decodedTag} is blank.
     */
    public DecodedTag(
            final String tag,
            final String rawTag,
            final AsnSchemaType type,
            final boolean isFullyDecoded) {
        this.tag = Strings.nullToEmpty(tag).trim();
        this.rawTag = Strings.nullToEmpty(rawTag).trim();
        this.type = type;
        this.isFullyDecoded = isFullyDecoded;

        checkNotNull(this.type);
    }
}
