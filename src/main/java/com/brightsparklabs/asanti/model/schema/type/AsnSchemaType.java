/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.common.VisitableThrowing;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.text.ParseException;

/**
 * A base type used to model the types for objects within ASN.1 schema. These objects can be either
 * Type Definitions, e.g. Type ::= SomeType, or components within a constructed type (SEQUENCE etc),
 * e.g. component SomeType
 *
 * @author brightSPARK Labs
 */
public interface AsnSchemaType extends VisitableThrowing<AsnSchemaTypeVisitor<?>, ParseException>
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** null instance */
    public static final AsnSchemaType.Null NULL = new AsnSchemaType.Null();

    /**
     * Returns all the {@code AsnSchemaComponentType} objects owned by this object, or an empty list
     * if there are none
     *
     * @return all the {@code AsnSchemaComponentType} objects owned by this object, or an empty list
     * if there are none
     */
    ImmutableList<AsnSchemaComponentType> getAllComponents();

    /**
     * Returns the {@code AsnBuiltinType} enum for this type. This is simply a shortcut for
     * getPrimitiveType().getBuiltinType()
     *
     * @return the {@code AsnBuiltinType} enum for this type.
     */
    AsnBuiltinType getBuiltinType();

    /**
     * Returns the constraints of this type definition.  This will be all the constraints the create
     * this type (i.e. this and all the parent types)
     *
     * @return the constraints of this type definition.
     */
    ImmutableSet<AsnSchemaConstraint> getConstraints();

    /**
     * Returns the {@code AsnSchemaComponentType} for the tag, or {@link Optional#absent()} if none
     * found
     *
     * @param tag
     *         a tag within this construct
     * @param decodingSession
     *         The {@link DecodingSession} used to maintain state while decoding a PDU of tags
     *
     * @return the {@code AsnSchemaComponentType} for the tag, or {@link Optional#absent()} if none
     * found
     */
    Optional<AsnSchemaComponentType> getMatchingChild(String tag, DecodingSession decodingSession);

    /**
     * Returns the AsnPrimitiveType of this type definition
     *
     * @return the AsnPrimitiveType of this type definition
     */
    AsnPrimitiveType getPrimitiveType();

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
        public ImmutableList<AsnSchemaComponentType> getAllComponents()
        {
            return ImmutableList.of();
        }

        @Override
        public AsnBuiltinType getBuiltinType()
        {
            return getPrimitiveType().getBuiltinType();
        }

        @Override
        public ImmutableSet<AsnSchemaConstraint> getConstraints()
        {
            return ImmutableSet.of();
        }

        @Override
        public Optional<AsnSchemaComponentType> getMatchingChild(String tag,
                DecodingSession decodingSession)
        {
            return Optional.absent();
        }

        @Override
        public AsnPrimitiveType getPrimitiveType()
        {
            return AsnPrimitiveType.INVALID;
        }


        @Override
        public Object accept(final AsnSchemaTypeVisitor<?> visitor) throws ParseException
        {
            return visitor.visit(this);
        }
    }
}
