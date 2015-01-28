/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import static com.google.common.base.Preconditions.*;

/**
 * An option within an ENUMERATED definition
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaEnumeratedOption
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** name of this option (i.e. tag name) */
    private final String tagName;

    /** tag of this option */
    private final String tag;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param tagName
     *            name of this component type (i.e. tag name)
     *
     * @param tag
     *            tag of this component type. Will default to an empty string if
     *            {@code null}
     *
     * @throws NullPointerException
     *             if {@code tagName} is {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code tagName} is blank
     */
    public AsnSchemaEnumeratedOption(String tagName, String tag)
    {
        checkNotNull(tagName);
        checkArgument(!tagName.trim()
                .isEmpty(), "Tag name must be specified");

        this.tagName = tagName.trim();
        this.tag = (tag == null) ? "" : tag.trim();
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the name of this option
     *
     * @return the name of this option (i.e. tag name)
     */
    public String getTagName()
    {
        return tagName;
    }

    /**
     * Returns the tag of this option
     *
     * @return the tag of this option
     */
    public String getTag()
    {
        return tag;
    }
}
