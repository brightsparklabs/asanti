/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

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

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------
    public AsnSchemaComponentType(String tagName, String tag, String typeName, boolean isOptional)
    {
        this.tagName = tagName;
        this.tag = tag;
        this.typeName = typeName;
        this.isOptional = isOptional;
    }

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
}
