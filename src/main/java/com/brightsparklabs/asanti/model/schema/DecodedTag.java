/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;

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
    //private final OLDAsnSchemaTypeDefinition type;
    //private final AsnSchemaTagType type;
    private final AsnSchemaType type;

    /** the Type Definition chain that got to the end primitive type */
    private final ImmutableSet<AsnSchemaType> allTypeDefinitions;

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
     *            {@code "/Document/header/published/date"})
     *
     * @param rawTag
     *            the full path of the raw tag (e.g. {@code "/1/0/1"})
     *
     * @param type
     *            the type of construct represented by the tag
     *
     * @param allTypeDefinitions
     *            the chain of Type Definitions to ended with the {@code type}
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
    //public DecodedTag(String decodedTag, String rawTag, OLDAsnSchemaTypeDefinition type, boolean isFullyDecoded)
    //public DecodedTag(String decodedTag, String rawTag, AsnSchemaTagType type, boolean isFullyDecoded)
    public DecodedTag(String decodedTag,
                      String rawTag,
                      AsnSchemaType type,
                      ImmutableSet<AsnSchemaType> allTypeDefinitions,
                      boolean isFullyDecoded)
    {
        this.tag = Strings.nullToEmpty(decodedTag)
                .trim();
        this.rawTag = Strings.nullToEmpty(rawTag)
                .trim();
        this.type = type;
        this.allTypeDefinitions = allTypeDefinitions;
        this.isFullyDecoded = isFullyDecoded;

        checkArgument(!this.tag.isEmpty(), "Decoded tag cannot be blank");
        checkArgument(!this.rawTag.isEmpty(), "Raw tag cannot be blank");


        if (this.type == null)
        {
        int breakpoint = 0;
        }


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

    //public OLDAsnSchemaTypeDefinition getType()
    public AsnSchemaType getType()
    {
        return type;
    }

    /**
     * Returns the chain of Type Definitions that may have led to (@code type}
     *
     * @return the chain of Type Definitions that may have led to (@code type}
     */
    public ImmutableSet<AsnSchemaType>  getAllTypes()
    {
        return allTypeDefinitions;
    }


    public boolean isFullyDecoded()
    {
        return isFullyDecoded;
    }
}
