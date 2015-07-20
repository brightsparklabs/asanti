/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.type;

import static com.google.common.base.Preconditions.*;

/**
 * An item within a 'constructed' (SET, SEQUENCE, CHOICE) type definition
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaComponentType
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** name of this component type (i.e. tag name) */
    private final String name;

    /** tag of this component type */
    //private final String tag;
    private String tag;

    /** whether this component type is optional */
    private final boolean isOptional;

    /** the underlying AsnSchemaType that this component is holding */
    private final AsnSchemaType type;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name
     *         name of this component type (i.e. tag name)
     * @param tag
     *         tag of this component type. Will default to an empty string if {@code null}
     * @param isOptional
     *         whether this component type is optional
     * @param type
     *         the underlying {@link AsnSchemaType} for this component
     *
     * @throws NullPointerException
     *         if {@code name}, {@code typeName} or {@code type} are {@code null}
     * @throws IllegalArgumentException
     *         if {@code name} or {@code typeName} are blank
     */
    public AsnSchemaComponentType(String name, String tag, boolean isOptional, AsnSchemaType type)
    {
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

    /**
     * @return the tag of this component type
     */
    public String getTag()
    {
        return tag;
    }

    // TODO MJF
    public void setTag(String tag)
    {
        this.tag = tag;
    }

    /**
     * @return {@code true} if this component type is optional
     */
    public boolean isOptional()
    {
        return isOptional;
    }

    /**
     * @return the {@link AsnSchemaType} of this component
     */
    public AsnSchemaType getType() { return type; }

    /**
     * @return the name of this component
     */
    public String getName()
    {
        return name;
    }
}
