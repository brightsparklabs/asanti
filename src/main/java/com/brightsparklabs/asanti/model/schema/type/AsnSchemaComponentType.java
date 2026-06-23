/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.type;

import static com.google.common.base.Preconditions.*;

/**
 * An item within a 'constructed' (SET, SEQUENCE, CHOICE) type definition.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaComponentType {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** Name of this component type (i.e. tag name). */
    private final String name;

    /** Tag of this component type. */
    private String tag;

    /** Whether this component type is optional. */
    private final boolean isOptional;

    /** The underlying AsnSchemaType that this component is holding. */
    private final AsnSchemaType type;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name Name of this component type (i.e. tag name).
     * @param tag Tag of this component type. Will default to an empty string if {@code null}.
     * @param isOptional Whether this component type is optional.
     * @param type The underlying {@link AsnSchemaType} for this component.
     * @throws NullPointerException if {@code name}, {@code typeName} or {@code type} are {@code
     *     null}.
     * @throws IllegalArgumentException if {@code name} or {@code typeName} are blank.
     */
    public AsnSchemaComponentType(
            final String name,
            final String tag,
            final boolean isOptional,
            final AsnSchemaType type) {
        checkNotNull(name);
        checkArgument(!name.trim().isEmpty(), "Tag name must be specified");
        checkNotNull(type);

        this.name = name;
        this.tag = (tag == null) ? "" : tag;
        this.isOptional = isOptional;
        this.type = type;
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /** {@return the tag of this component type} */
    public String getTag() {
        return tag;
    }

    /**
     * @param tag Set the tag of this component type. Will default to an empty string if {@code
     *     null}.
     */
    public void setTag(final String tag) {
        this.tag = (tag == null) ? "" : tag;
    }

    /** {@return {@code true} if this component type is optional} */
    public boolean isOptional() {
        return isOptional;
    }

    /** {@return the {@link AsnSchemaType} of this component} */
    public AsnSchemaType getType() {
        return type;
    }

    /** {@return the name of this component} */
    public String getName() {
        return name;
    }
}
