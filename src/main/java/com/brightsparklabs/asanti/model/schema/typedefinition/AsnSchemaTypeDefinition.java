/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.common.Visitable;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;

/**
 * A type definition from a within a module specification within an ASN.1
 * schema.
 *
 * @author brightSPARK Labs
 */
public interface AsnSchemaTypeDefinition extends Visitable<AsnSchemaTypeDefinitionVisitor<?>>
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** null instance */
    public static final AsnSchemaTypeDefinitionNull NULL = new AsnSchemaTypeDefinitionNull();

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the name of this type definition
     *
     * @return the name of this type definition
     */
    public abstract String getName();

    /**
     * Returns the ASN.1 built-in type for this type definition
     *
     * @return the ASN.1 built-in type for this type definition
     */
    public abstract AsnBuiltinType getBuiltinType();

    /**
     * Returns the name of the specified tag
     *
     * @param tag
     *            a tag within this construct
     *
     * @return name of the specified tag; or an empty string if tag is not
     *         recognised.
     */
    public abstract String getTagName(String tag);

    /**
     * Returns the name of the type definition associated with the specified tag
     *
     * @param tag
     *            a tag within this construct
     *
     * @return name of the type definition associated with the specified tag; or
     *         an empty string if tag is not recognised.
     */
    public abstract String getTypeName(String tag);

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: AsnSchemaConstructTypeDefinitionNull
    // -------------------------------------------------------------------------

    /**
     * Null instance of {@link AbstractAsnSchemaTypeDefinition}
     */
    public static class AsnSchemaTypeDefinitionNull extends AbstractAsnSchemaTypeDefinition
    {
        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private. Use
         * {@link AbstractAsnSchemaTypeDefinition#NULL} to obtain an instance.
         */
        private AsnSchemaTypeDefinitionNull()
        {
            super("NULL", AsnBuiltinType.Null, null);
        }

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: AsnSchemaTypeDefinition
        // ---------------------------------------------------------------------

        @Override
        public Object visit(AsnSchemaTypeDefinitionVisitor<?> visitor)
        {
            return visitor.visit(this);
        }
    }
}
