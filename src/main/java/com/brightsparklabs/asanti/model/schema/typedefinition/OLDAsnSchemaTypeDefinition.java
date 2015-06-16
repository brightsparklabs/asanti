/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;

/**
 * A type definition from a within a module specification within an ASN.1
 * schema.
 *
 * @author brightSPARK Labs
 */
public interface OLDAsnSchemaTypeDefinition extends AsnSchemaTagType
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** null instance */
    public static final OLDAsnSchemaTypeDefinition.Null NULL = new OLDAsnSchemaTypeDefinition.Null();

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the name of this type definition
     *
     * @return the name of this type definition
     */
    public String getName();


    /**
     * Returns the name of the specified tag
     *
     * @param tag
     *            a tag within this construct
     *
     * @return name of the specified tag; or an empty string if tag is not
     *         recognised.
     */
    public String getTagName(String tag);

    /**
     * Returns the name of the type definition associated with the specified tag
     *
     * @param tag
     *            a tag within this construct
     *
     * @return name of the type definition associated with the specified tag; or
     *         an empty string if tag is not recognised.
     */
    public String getTypeName(String tag);

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: AsnSchemaTagType.Null
    // -------------------------------------------------------------------------

    /**
     * Null instance of {@link AbstractOLDAsnSchemaTypeDefinition}.
     * <p>
     * NOTE: This is not named {@code AsnSchemaTypeDefinitionNull} because that
     * is the name used to model an actual ASN.1 {@code NULL} Type Definition.
     */
    public static class Null implements AsnSchemaTypeDefinition
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
