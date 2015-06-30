/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import static com.google.common.base.Preconditions.*;

/**
 * A named tag in the ENUMERATED or INTEGER type definitions e.g. an ENUMERATED option or an INTEGER
 * distinguished value.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaNamedTag
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** name of the option or distinguished value (i.e. tag name) */
    private final String tagName;

    /** tag of the option or distinguished value */
    private final String tag;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param tagName
     *         name of this component type (i.e. tag name)
     * @param tag
     *         tag of this component type. Will default to an empty string if {@code null}
     *
     * @throws NullPointerException
     *         if {@code tagName} is {@code null}
     * @throws IllegalArgumentException
     *         if {@code tagName} is blank
     */
    public AsnSchemaNamedTag(String tagName, String tag)
    {
        checkNotNull(tagName);
        checkArgument(!tagName.trim().isEmpty(), "Tag name must be specified");

        this.tagName = tagName.trim();
        // TODO ASN-133 - is this the correct behavior
        this.tag = (tag == null) ? "" : tag.trim();
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the name of the component type
     *
     * @return the name of the component type (i.e. tag name)
     */
    public String getTagName()
    {
        return tagName;
    }

    /**
     * Returns the tag of the component type
     *
     * @return the tag of the component type
     */
    public String getTag()
    {
        return tag;
    }
}
