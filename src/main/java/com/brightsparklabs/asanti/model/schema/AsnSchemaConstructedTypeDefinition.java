/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * A 'constructed' (SET, SEQUENCE, SET OF, SEQUENCE OF, CHOICE or ENUMERATED)
 * type definition from a within a module specification within an ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaConstructedTypeDefinition extends AsnSchemaTypeDefinition<ImmutableList>
{

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------
    private final ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes;
    private final ImmutableMap<String, AsnSchemaComponentType> tagNamesToComponentTypes;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    public AsnSchemaConstructedTypeDefinition(String name, AsnBuiltinType type, Iterable<String> items)
    {
        super(name, type);
        // TODO Auto-generated constructor stub
        tagsToComponentTypes = ImmutableMap.of();
        tagNamesToComponentTypes = ImmutableMap.of();
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaTypeDefinition
    // -------------------------------------------------------------------------
    @Override
    public String getTypeName(String tag)
    {
        return "";
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: AsnSchemaConstructedTypeItem
    // -------------------------------------------------------------------------
    /**
     * An item within a 'constructed' (SET, SEQUENCE, SET OF, SEQUENCE OF,
     * CHOICE or ENUMERATED) type definition
     *
     * @author brightSPARK Labs
     */
    public static class AsnSchemaComponentType
    {
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        private final String tagName;

        private final String tag;

        private final String typeName;

        private final AsnBuiltinType builtinType;

        private final boolean isOptional;

        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------
        public AsnSchemaComponentType(String name, String tag, AsnBuiltinType builtinType, String typeName,
                boolean isOptional)
        {
            this.tagName = name;
            this.tag = tag;
            this.builtinType = builtinType;
            this.typeName = typeName;
            this.isOptional = isOptional;
        }
    }
}
