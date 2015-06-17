package com.brightsparklabs.asanti.model.schema.tagtype;

import com.brightsparklabs.asanti.common.Visitable;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.typedefinition.AbstractOLDAsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTagTypeVisitor;

/**
 * A type used by a component from a within a module specification within an ASN.1
 * schema.
 *
 * @author brightSPARK Labs
 */
public interface AsnSchemaTagType extends Visitable<AsnSchemaTagTypeVisitor<?>>
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** null instance */
    public static final AsnSchemaTagType.Null NULL = new AsnSchemaTagType.Null();


    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the ASN.1 built-in type for this tagtype
     *
     * @return the ASN.1 built-in type for this tagtype
     */
    public AsnBuiltinType getBuiltinType();

    /**
     * Returns the constraint associated with this tagtype
     *
     * @return the constraint associated with this tagtype or
     *         {@link AsnSchemaConstraint#NULL} if there is no constraint.
     */
    public AsnSchemaConstraint getConstraint();



    // -------------------------------------------------------------------------
    // INTERNAL CLASS: AsnSchemaConstructTagTypeNull
    // -------------------------------------------------------------------------

    /**
     * NULL instance of {@link AbstractOLDAsnSchemaTypeDefinition}.
     * <p>
     * NOTE: This is not named {@code AsnSchemaTypeDefinitionNull} because that
     * is the name used to model an actual ASN.1 {@code NULL} Type Definition.
     */
    public static class Null extends AbstractAsnSchemaTagType
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
            super( AsnBuiltinType.Null, AsnSchemaConstraint.NULL);
        }

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: AsnSchemaTagType
        // ---------------------------------------------------------------------
        @Override
        public Object visit(AsnSchemaTagTypeVisitor<?> visitor)
        {
            return visitor.visit(this);
        }

    }


}
