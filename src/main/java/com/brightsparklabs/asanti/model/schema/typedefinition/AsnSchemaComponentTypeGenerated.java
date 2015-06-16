/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagType;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagTypePlaceHolder;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagTypeSequence;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypePlaceholder;

import static com.google.common.base.Preconditions.*;

/**
 * An item within a 'constructed' (SET, SEQUENCE, SET OF, SEQUENCE OF, CHOICE or ENUMERATED) type
 * definition where the referenced type is a pseudo type definition requiring a generated type
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
     *         tag of this generated component type. Will default to an empty string if {@code
     *         null}
     * @param typeName
     *         type of this generated component type
     * @param typeDefinitionText
     *         type definition text of this generated component type
     * @param isOptional
     *         whether this generated component type is optional
     *
     * @throws NullPointerException
     *         if {@code tagName}, {@code typeName} or {@code typeDefinitionText} are {@code null}
     * @throws IllegalArgumentException
     *         if {@code tagName}, {@code typeName} or {@code typeDefinitionText} are blank
     */
    public AsnSchemaComponentTypeGenerated(String tagName, String tag, String typeName,
            String typeDefinitionText, boolean isOptional)
    {
        // TODO - we should be passing in the constraints (if there is such a thing on a nested sequence/set/enumerated etc?
        // This is making a new "pseudo" type, which is something that we can then lookup later.
        //super(tagName, tag, typeName, isOptional, new AsnSchemaTagTypePlaceHolder("", typeName, AsnSchemaConstraint.NULL));
        super(tagName, tag, typeName, isOptional, new AsnSchemaTypePlaceholder("", typeName, AsnSchemaConstraint.NULL));

        checkNotNull(typeDefinitionText);
        checkArgument(!typeDefinitionText.trim().isEmpty(),
                "Type Definition text must be specified");

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
