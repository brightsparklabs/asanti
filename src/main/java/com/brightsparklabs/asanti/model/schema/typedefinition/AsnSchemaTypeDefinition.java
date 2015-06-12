/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagType;

/**
 * A type definition from a within a module specification within an ASN.1
 * schema.
 *
 * @author brightSPARK Labs
 */
public interface AsnSchemaTypeDefinition extends AsnSchemaTagType
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
     * Null instance of {@link AbstractAsnSchemaTypeDefinition}.
     * <p>
     * NOTE: This is not named {@code AsnSchemaTypeDefinitionNull} because that
     * is the name used to model an actual ASN.1 {@code NULL} Type Definition.
     */
    public static class Null extends AbstractAsnSchemaTypeDefinition
    {
        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private. Use
         * {@link AbstractAsnSchemaTypeDefinition#NULL} to obtain an instance.
         */
        private Null()
        {
            super("NULL", AsnBuiltinType.Null, AsnSchemaConstraint.NULL);
        }

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: AsnSchemaTypeDefinition
        // ---------------------------------------------------------------------

        @Override
        public Object visit(AsnSchemaTagTypeVisitor<?> visitor)
        {
            return visitor.visit(this);
        }

        @Override
        public String getTagName(String tag)
        {
            return "";
        }

        @Override
        public String getTypeName(String tag)
        {
            return "";
        }
    }
}
