package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AbstractOLDAsnSchemaTypeDefinition;
import com.google.common.collect.ImmutableSet;

/**
 * A base type used to model the types for objects within ASN.1 schema.
 * These objects can be either Type Definitions, eg Type ::= SomeType,
 * or components within a constructed type (Sequence etc), eg component SomeType
 *
 *
 * @author brightSPARK Labs
 */
public interface AsnSchemaType
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** null instance */
    public static final AsnSchemaType.Null NULL = new AsnSchemaType.Null();


    /**
     * Returns the AsnPrimitiveType of this type definition
     * @return the AsnPrimitiveType of this type definition
     */
    AsnPrimitiveType getPrimitiveType();

    /**
     * Returns the constraints of this type definition.  This will be all the constraints
     * the create this type (ie this and all the parent types)
     * @return
     */
    ImmutableSet<AsnSchemaConstraint> getConstraints();

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: AsnSchemaTagType.Null
    // -------------------------------------------------------------------------

    /**
     * Null instance of {@link AbstractOLDAsnSchemaTypeDefinition}.
     * <p>
     * NOTE: This is not named {@code AsnSchemaTypeDefinitionNull} because that
     * is the name used to model an actual ASN.1 {@code NULL} Type Definition.
     */
    public static class Null implements AsnSchemaType
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
        // IMPLEMENTATION:
        // ---------------------------------------------------------------------
        @Override
        public AsnPrimitiveType getPrimitiveType()
        {
            return AsnPrimitiveType.Null;
        }

        @Override
        public ImmutableSet<AsnSchemaConstraint> getConstraints()
        {
            return ImmutableSet.<AsnSchemaConstraint>of();
        }
    }


}
