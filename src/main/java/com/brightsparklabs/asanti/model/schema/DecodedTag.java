/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import static com.google.common.base.Preconditions.*;

import com.google.common.base.Strings;

/**
 * Represent a decoded ASN.1 tag created by decoding a raw tag path
 *
 * @author brightSPARK Labs
 */
public class DecodedTag
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the full path of the decoded tag */
    private final String tag;

    /** the full path of the raw tag */
    private final String rawTag;

    /** the type of construct represented by the tag */
    private final AsnSchemaTypeDefinition type;

    /** whether the raw tag was completely decoded */
    private final boolean isFullyDecoded;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param decodedTag
     *            the full path of the decoded tag (e.g.
     * @code{"/Document/header/published/date" )
     *
     * @param rawTag
     *            the full path of the raw tag (e.g. @code{"/1/0/1"})
     *
     * @param type
     *            the type of construct represented by the tag
     *
     * @param isFullyDecoded
     *            whether the raw tag was completely decoded
     *
     * @throws NullPointerException
     *             if parameters are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code decodedTag} or {@code rawTag} are blank
     */
    public DecodedTag(String decodedTag, String rawTag, AsnSchemaTypeDefinition type, boolean isFullyDecoded)
    {
        this.tag = Strings.nullToEmpty(decodedTag)
                .trim();
        this.rawTag = Strings.nullToEmpty(rawTag)
                .trim();
        this.type = type;
        this.isFullyDecoded = isFullyDecoded;

        checkArgument(!this.tag.isEmpty(), "Decoded tag cannot be blank");
        checkArgument(!this.rawTag.isEmpty(), "Raw tag cannot be blank");
        checkNotNull(this.type);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the full path of the decoded tag
     *
     * @return the full path of the decoded tag
     */
    public String getTag()
    {
        return tag;
    }

    /**
     * Returns the full path of the decoded tag
     *
     * @return the full path of the decoded tag
     */
    public String getRawTag()
    {
        return rawTag;
    }

    /**
     * Returns the type of construct represented by the tag
     *
     * @return the type of construct represented by the tag
     */

    public AsnSchemaTypeDefinition getType()
    {
        return type;
    }

    public boolean isFullyDecoded()
    {
        return isFullyDecoded;
    }
}
