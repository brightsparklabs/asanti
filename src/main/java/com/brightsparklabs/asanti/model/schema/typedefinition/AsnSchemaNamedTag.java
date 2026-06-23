/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import static com.google.common.base.Preconditions.*;

/**
 * A named tag in the ENUMERATED or INTEGER type definitions e.g. an ENUMERATED option or an INTEGER
 * distinguished value.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaNamedTag {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** Name of the option or distinguished value (i.e. tag name). */
    private final String tagName;

    /** Tag of the option or distinguished value. */
    private final String tag;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param tagName Name of this component type (i.e. tag name).
     * @param tag Tag of this component type.
     * @throws NullPointerException if {@code tagName} or {@code tag} are {@code null}.
     * @throws IllegalArgumentException if {@code tagName} or {@code tag} are blank.
     */
    public AsnSchemaNamedTag(final String tagName, final String tag) {
        checkNotNull(tagName);
        checkArgument(!tagName.trim().isEmpty(), "Tag name must be specified");

        checkNotNull(tag);
        checkArgument(!tag.trim().isEmpty(), "Tag must be specified");

        this.tagName = tagName.trim();
        this.tag = tag.trim();
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /** {@return the name of the component type (i.e. tag name)} */
    public String getTagName() {
        return tagName;
    }

    /** {@return the tag of the component type} */
    public String getTag() {
        return tag;
    }
}
