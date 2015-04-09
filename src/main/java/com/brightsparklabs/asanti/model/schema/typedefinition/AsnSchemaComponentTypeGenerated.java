/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

/**
 * An item within a 'constructed' (SET, SEQUENCE, SET OF, SEQUENCE OF, CHOICE or
 * ENUMERATED) type definition where the referenced type is a pseudo type
 * definition requiring a generated type
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaComponentTypeGenerated extends AsnSchemaComponentType
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** type definition text of the generated component type */
    private final String typeDefinitionText;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param tagName
     *         name of this generated component type (i.e. tag name)
     * @param tag
     *         tag of this generated component type. Will default to an empty
     *         string if {@code null}
     * @param typeName
     *         type of this generated component type
     * @param typeDefinitionText
     *         type definition text of this generated component type
     * @param isOptional
     *         whether this generated component type is optional
     *
     * @throws NullPointerException
     *         if {@code tagName} or {@code typeName} are {@code null}
     * @throws IllegalArgumentException
     *         if {@code tagName} or {@code typeName} are blank
     */
    public AsnSchemaComponentTypeGenerated(String tagName, String tag, String typeName, String typeDefinitionText,
            boolean isOptional)
    {
        super(tagName, tag, typeName, isOptional);

        this.typeDefinitionText = typeDefinitionText;
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * @return the type definition text of this generated component type
     */
    public String getTypeDefinitionText()
    {
        return typeDefinitionText;
    }
}
