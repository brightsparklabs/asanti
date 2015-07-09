package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.google.common.collect.ImmutableSet;

/**
 * A base type used to model the types for objects within ASN.1 schema. These objects can be either
 * Type Definitions, e.g. Type ::= SomeType, or components within a constructed type (SEQUENCE etc),
 * e.g. component SomeType
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
     *
     * @return the AsnPrimitiveType of this type definition
     */
    AsnPrimitiveType getPrimitiveType();

    /**
     * Returns the {@code AsnBuiltinType} enum for this type. This is simply a shortcut for
     * getPrimitiveType().getBuiltinType()
     *
     * @return the {@code AsnBuiltinType} enum for this type.
     */
    AsnBuiltinType getBuiltinType();

    // TODO MJF - do we still need this?  Is so then rename and document
    AsnBuiltinType getBuiltinTypeAA();

    /**
     * Returns the constraints of this type definition.  This will be all the constraints the create
     * this type (i.e. this and all the parent types)
     *
     * @return the constraints of this type definition.
     */
    ImmutableSet<AsnSchemaConstraint> getConstraints();

    /**
     * Returns the {@code AsnSchemaNamedType} for the tag, or AsnSchemaNamedType#NULL if none found
     *
     * @param tag
     *         a tag within this construct
     * @param session
     *         The {@link DecodingSession} used to maintain state while decoding a collection of
     *         tags
     *
     * @return the {@code AsnSchemaNamedType} for the AsnSchemaNamedType#NULL, or null if none found
     */
    AsnSchemaNamedType getMatchingChild(String tag, DecodingSession session);

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Null
    // -------------------------------------------------------------------------

    /**
     * Null instance of {@link AsnSchemaType}. <p> NOTE: This is not named {@code
     * AsnSchemaTypeDefinitionNull} because that is the name used to model an actual ASN.1 {@code
     * NULL} Type Definition.
     */
    public static class Null implements AsnSchemaType
    {
        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private. Use {@link AsnSchemaType#NULL} to obtain an
         * instance.
         */
        private Null()
        {
        }

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: AsnSchemaType
        // ---------------------------------------------------------------------

        @Override
        public AsnPrimitiveType getPrimitiveType()
        {
            return AsnPrimitiveType.NULL;
        }

        @Override
        public AsnBuiltinType getBuiltinType()
        {
            return getPrimitiveType().getBuiltinType();
        }

        @Override
        public AsnBuiltinType getBuiltinTypeAA()
        {
            return getPrimitiveType().getBuiltinType();
        }

        @Override
        public ImmutableSet<AsnSchemaConstraint> getConstraints()
        {
            return ImmutableSet.of();
        }

        @Override
        public AsnSchemaNamedType getMatchingChild(String tag, DecodingSession session)
        {
            return AsnSchemaNamedType.NULL;
        }
    }
}
