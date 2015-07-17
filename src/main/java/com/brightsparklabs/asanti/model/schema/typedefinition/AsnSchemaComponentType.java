/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaNamedType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;

import static com.google.common.base.Preconditions.*;

/**
 * An item within a 'constructed' (SET, SEQUENCE, SET OF, SEQUENCE OF, CHOICE or ENUMERATED) type
 * definition
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaComponentType implements AsnSchemaNamedType
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** name of this component type (i.e. tag name) */
    private final String tagName;

    /** tag of this component type */
    private final String tag;

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
     * @param tagName
     *         name of this component type (i.e. tag name)
     * @param tag
     *         tag of this component type. Will default to an empty string if {@code null}
     * @param isOptional
     *         whether this component type is optional
     * @param type
     *         the underlying {@link AsnSchemaType} for this component
     *
     * @throws NullPointerException
     *         if {@code tagName}, {@code typeName} or {@code type} are {@code null}
     * @throws IllegalArgumentException
     *         if {@code tagName} or {@code typeName} are blank
     */
    public AsnSchemaComponentType(String tagName, String tag, boolean isOptional,
            AsnSchemaType type)
    {
        checkNotNull(tagName);
        checkArgument(!tagName.trim().isEmpty(), "Tag name must be specified");
        checkNotNull(type);

        this.tagName = tagName;
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

    /**
     * @return {@code true} if this component type is optional
     */
    public boolean isOptional()
    {
        return isOptional;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaNamedType
    // -------------------------------------------------------------------------

    @Override
    public AsnSchemaType getType() { return type; }

    @Override
    public String getName()
    {
        return tagName;
    }
}
