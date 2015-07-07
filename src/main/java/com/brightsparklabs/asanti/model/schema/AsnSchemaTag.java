package com.brightsparklabs.asanti.model.schema;

import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Models a tag in a 'constructed' type. A tag conforms to one of the following
 * formats: <ul> <li>tagNumber (e.g. {@code 1}</li> <li>tagNumber[tagIndex] (e.g. {@code
 * 1[0]}</li> </ul>
 */
public class AsnSchemaTag
{
    // ---------------------------------------------------------------------
    // CONSTANTS
    // ---------------------------------------------------------------------

    /** null instance */
    private static final AsnSchemaTag NULL = new AsnSchemaTag("", "", "");

    /** pattern to match a raw tag coming out of the BER decoder */
    private static final Pattern PATTERN_TAG = Pattern.compile(
            "^([0-9]+)(\\.(([0-9]+)|(u\\.([a-zA-Z0-9]+))))$");

    // ---------------------------------------------------------------------
    // INSTANCE VARIABLES
    // ---------------------------------------------------------------------

    /** the context specific tag number component of the raw tag.  Note that this
     *  and tagUniversal are mutually exclusive */
    private final String tagContextSpecific;

    /** the Universal tag number component of the raw tag.  Note that this and
     *  the tagContextSpecific are mutually exclusive */
    private final String tagUniversal;

    /** the tag index component of the raw tag. Blank if no index component */
    private final String tagIndex;


    // ---------------------------------------------------------------------
    // CONSTRUCTION
    // ---------------------------------------------------------------------

    /**
     * Default constructor. Private, use {@link #create(String)} instead.
     *
     * @param tagIndex
     *         tag index component of the raw tag
     * @param tagContextSpecific
     *         tag context-specific component of the raw tag. Set to {@code null} if no
     *         context-specific component.
     * @param tagUniversal
     *          tag universal component of the raw tag.  Set to (@code null} if no
     *          universal component.
     */
    private AsnSchemaTag(String tagIndex, String tagContextSpecific, String tagUniversal)
    {
        this.tagIndex = tagIndex;
        this.tagContextSpecific = Strings.nullToEmpty(tagContextSpecific);
        this.tagUniversal = Strings.nullToEmpty(tagUniversal);
    }

    /**
     * Creates an instance from the supplied raw tag
     *
     * @param rawTag
     *         raw tag to create instance from
     *
     * @return instance which models the raw tag, or {@link #NULL} if the raw tag is invalid
     */
    public static AsnSchemaTag create(String rawTag)
    {
        if (rawTag == null)
        {
            return NULL;
        }

        final Matcher matcher = PATTERN_TAG.matcher(rawTag);
        if (matcher.matches())
        {

            return new AsnSchemaTag(matcher.group(1), matcher.group(4), matcher.group(5));
        }
        else
        {
            return NULL;
        }
    }

    // ---------------------------------------------------------------------
    // PUBLIC METHODS
    // ---------------------------------------------------------------------

    public String getRawTag()
    {
        return tagIndex + "." + getTagPortion();
    }

    /**
     * Returns the tag index component of the raw tag. For a raw tag {@code "1.2"} this is
     * {@code "1"}.
     *
     * @return the tag index or a blank string if no index component
     */
    public String getTagIndex()
    {
        return tagIndex;
    }

    /**
     * Returns the context-specific component of the raw tag. For a raw tag {@code "0.1"} this is
     * {@code "1"}.  For raw tag {@code "0.u.Integer"} this is {@code ""}.
     *
     * @return the tag number
     */
    public String getTagContextSpecific()
    {
        return tagContextSpecific;
    }

    /**
     * Returns the universal component of the raw tag. For a raw tag {@code "0.u.Integer"} this is
     * {@code "u.Integer"}.  For raw tag {@code "0.1"} this is {@code ""}.
     *
     * @return the tag number
     */
    public String getTagUniversal()
    {
        return tagUniversal;
    }

    /**
     * Returns the non-index component of the raw tag. For a raw tag {@code "0.u.Integer"} this is
     * {@code "u.Integer"}.  For raw tag {@code "0.1"} this is {@code "1"}.  This returns either
     * the context-specific or universal tag, which ever is not empty
     *
     * @return the tag number
     */
    public String getTagPortion()
    {
        return tagUniversal.isEmpty() ? tagContextSpecific  : tagUniversal;
    }

}
