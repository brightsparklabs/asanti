package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;

/**
 * Created by Michael on 16/06/2015.
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
     * @return
     */
    AsnSchemaType getType();

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: AsnSchemaTypeDefinition.Null
    // -------------------------------------------------------------------------

    /**
     * Null instance of {@link AbstractOLDAsnSchemaTypeDefinition}.
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
         * {@link AbstractOLDAsnSchemaTypeDefinition#NULL} to obtain an instance.
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
