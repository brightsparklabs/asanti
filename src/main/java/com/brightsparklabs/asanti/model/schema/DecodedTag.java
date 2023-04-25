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
 * @author brightSPARK Labs
 */
public class DecodedTag {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** The full path of the decoded tag. */
    private final String tag;

    /** The full path of the raw tag. */
    private final String rawTag;

    /** The type of construct represented by the tag. */
    private final AsnSchemaType type;

    /** Whether the raw tag was completely decoded. */
    private final boolean isFullyDecoded;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param decodedTag The full path of the decoded tag (e.g. {@code
     *     "/Document/header/published/date"}).
     * @param rawTag The full path of the raw tag (e.g. {@code "/0[1]/0[0]/0[1]"}).
     * @param type The type of construct represented by the tag.
     * @param isFullyDecoded Whether the raw tag was completely decoded.
     * @throws NullPointerException If parameters are {@code null}.
     * @throws IllegalArgumentException If {@code decodedTag} is blank.
     */
    public DecodedTag(
            String decodedTag, String rawTag, AsnSchemaType type, boolean isFullyDecoded) {
        this.tag = Strings.nullToEmpty(decodedTag).trim();
        this.rawTag = Strings.nullToEmpty(rawTag).trim();
        this.type = type;
        this.isFullyDecoded = isFullyDecoded;

        checkNotNull(this.type);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the full path of the decoded tag.
     *
     * @return The full path of the decoded tag.
     */
    public String getTag() {
        return tag;
    }

    /**
     * Returns the full path of the decoded tag.
     *
     * @return The full path of the decoded tag.
     */
    public String getRawTag() {
        return rawTag;
    }

    /**
     * Returns the type of construct represented by the tag.
     *
     * @return The type of construct represented by the tag.
     */
    public AsnSchemaType getType() {
        return type;
    }

    /**
     * Returns {@code true} if the raw tag was completely decoded.
     *
     * @return {@code true} if the raw tag was completely decoded.
     */
    public boolean isFullyDecoded() {
        return isFullyDecoded;
    }
}
