package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;

/**
 * A Type Definition is a named type that can be used to replace a primitive type when declaring
 * other objects within an ASN.1 Schema, where those objects can be either other Type Definitions
 * or component types.
 * It is essentially a Name and a (@code AsnSchemaType)
 * @author brightSPARK Labs
 */
public interface AsnSchemaTypeDefinition
{

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** null instance */
    public static final AsnSchemaTypeDefinition.Null NULL = new AsnSchemaTypeDefinition.Null();

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the name of this type definition
     * @return the name of this type definition
     */
    String getName();

    /**
     * Returns the AsnSchemaType of this type definition
     * @return the AsnSchemaType of this type definition
     */
    AsnSchemaType getType();

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: AsnSchemaTypeDefinition.NULL
    // -------------------------------------------------------------------------

    /**
     * NULL instance of {@link AsnSchemaTypeDefinitionImpl}.
     * <p>
     * NOTE: This is not named {@code AsnSchemaTypeDefinitionNull} because that
     * is the name used to model an actual ASN.1 {@code NULL} Type Definition.
     */
    public static class Null extends AsnSchemaTypeDefinitionImpl
    {
        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private. Use
         * {@link AsnSchemaTypeDefinition#NULL} to obtain an instance.
         */
        private Null()
        {
            super("NULL", AsnSchemaType.NULL);
        }

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: OLDAsnSchemaTypeDefinition
        // ---------------------------------------------------------------------
        @Override
        public String getName()
        {
            return "";
        }

        @Override
        public AsnSchemaType getType()
        {
            return null;
        }


    }

}
