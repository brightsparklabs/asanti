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

    AsnBuiltinType getBuiltinTypeAA();


    /**
     * Returns the constraints of this type definition.  This will be all the constraints the create
     * this type (i.e. this and all the parent types)
     *
     * @return the constraints of this type definition.
     */
    ImmutableSet<AsnSchemaConstraint> getConstraints();


    // TODO MJF - add javadoc here and delete the getChildType and getChildName
    AsnSchemaNamedType getMatchingChild(String tag, DecodingSession session);

    /**
     * Returns the {@link AsnSchemaType} of any component type matching tag
     *
     * @param tag
     *         a tag within this construct
     *
     * @return the {@link AsnSchemaType} of any component type matching tag
     */

    AsnSchemaType getChildType(String tag);

    /**
     * Returns the name of the component associated with the specified tag
     *
     * @param tag
     *         a tag within this construct
     *
     * @return name of the specified tag; or an empty string if tag is not recognised.
     */
    String getChildName(String tag);

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
            return new AsnSchemaNamedTypeImpl("", AsnSchemaType.NULL);
        }

        @Override
        public AsnSchemaType getChildType(String tag)
        {
            return AsnSchemaType.NULL;
        }

        @Override
        public String getChildName(String tag)
        {
            return "";
        }
    }
}
