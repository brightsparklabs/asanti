/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagType;

import static com.google.common.base.Preconditions.*;

/**
 * An item within a 'constructed' (SET, SEQUENCE, SET OF, SEQUENCE OF, CHOICE or
 * ENUMERATED) type definition
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaComponentType
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** name of this component type (i.e. tag name) */
    private final String tagName;

    /** tag of this component type */
    private final String tag;

    /** type of this component type */
    private final String typeName;

    /** whether this component type is optional */
    private final boolean isOptional;

    /**  */
    private final AsnSchemaTagType type;


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
     * @param typeName
     *            type of this component type
     *
     * @param isOptional
     *            whether this component type is optional
     *
     * @throws NullPointerException
     *             if {@code tagName} or {@code typeName} are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code tagName} or {@code typeName} are blank
     */

    public AsnSchemaComponentType(String tagName, String tag, String typeName, boolean isOptional)
    {
        this(tagName, tag, typeName, isOptional, null); // TODO - temporary
    }

    public AsnSchemaComponentType(String tagName, String tag, String typeName, boolean isOptional, AsnSchemaTagType type)
    {
        checkNotNull(tagName);
        checkArgument(!tagName.trim().isEmpty(), "Tag name must be specified");
        checkNotNull(typeName);
        checkArgument(!typeName.trim().isEmpty(), "Type name must be specified");

        this.tagName = tagName;
        this.tag = (tag == null) ? "" : tag;
        this.typeName = typeName;
        this.isOptional = isOptional;
        this.type = type;
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * @return the name of the component type (i.e. tag name)
     */
    public String getTagName()
    {
        return tagName;
    }

    /**
     * @return the tag of this component type
     */
    public String getTag()
    {
        return tag;
    }

    /**
     * @return the type of this component type
     */
    public String getTypeName()
    {
        return typeName;
    }

    /**
     * @return {@code true} if this component type is optional
     */
    public boolean isOptional()
    {
        return isOptional;
    }


    public AsnSchemaTagType getType() { return type; }
}
