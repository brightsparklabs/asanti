/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

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
    private final String decodedTag;

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
     * @param tagsToData
     *            map of tags to data
     *
     * @throws NullPointerException
     *             if parameters are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code decodedTag} or {@code rawTag} are blank
     */
    private DecodedTag(String decodedTag, String rawTag, AsnSchemaTypeDefinition type, boolean isFullyDecoded)
    {
        this.decodedTag = Strings.nullToEmpty(decodedTag)
                .trim();
        this.rawTag = Strings.nullToEmpty(rawTag)
                .trim();
        this.type = type;
        this.isFullyDecoded = isFullyDecoded;

        checkArgument(!this.decodedTag.isEmpty(), "Decoded tag cannot be blank");
        checkArgument(!this.rawTag.isEmpty(), "Raw tag cannot be blank");
        checkNotNull(this.type);
    }

    public static Builder builder()
    {
        return new Builder();
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the full path of the decoded tag
     *
     * @return the full path of the decoded tag
     */
    public String getDecodedTag()
    {
        return decodedTag;
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

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Builder
    // -------------------------------------------------------------------------

    public static class Builder
    {

        /** splitter for separating tag strings */
        private static final Splitter tagSplitter = Splitter.on("/")
                .omitEmptyStrings();

        /** joiner for creating tag strings */
        private static final Joiner tagJoiner = Joiner.on("/");

        private final List<String> decodedTags = Lists.newArrayList();
        AsnSchemaTypeDefinition type = AsnSchemaTypeDefinition.NULL;

        private Builder()
        {
        }

        public Builder addDecodedTag(String decodedTag)
        {
            decodedTags.add(decodedTag);
            return this;
        }

        public Builder setType(AsnSchemaTypeDefinition type)
        {
            this.type = type;
            return this;
        }

        public void merge(Builder builder)
        {
            decodedTags.addAll(builder.decodedTags);
            type = builder.type;
        }

        public DecodedTag build(String rawTag, String topLevelName)
        {

            final ArrayList<String> tags = Lists.newArrayList(tagSplitter.split(rawTag));

            // check if decode was successful
            boolean decodeSuccessful = true;
            if (tags.size() != decodedTags.size())
            {
                // could not decode
                decodeSuccessful = false;

                // copy unknown tags into result
                for (int i = decodedTags.size(); i < tags.size(); i++)
                {
                    final String unknownTag = tags.get(i);
                    addDecodedTag(unknownTag);
                }
            }

            // prefix result with top level type
            decodedTags.add(0, topLevelName);
            decodedTags.add(0, ""); // empty string prefixes just the separator
            final String decodedTagPath = tagJoiner.join(decodedTags);

            return new DecodedTag(decodedTagPath, rawTag, type, decodeSuccessful);
        }
    }
}
