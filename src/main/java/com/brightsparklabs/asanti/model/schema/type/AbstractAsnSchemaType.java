/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.type;

import static java.util.Objects.*;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.schema.AsnPrimitiveType;
import com.google.common.collect.ImmutableSet;

/**
 * A concrete implementation of class that can model the types for objects within ASN.1 schema.
 * These objects can be either Type Definitions, e.g. Type ::= SomeType, or components within a
 * constructed type (SEQUENCE etc), e.g. component SomeType
 *
 * @author brightSPARK Labs
 */
public abstract class AbstractAsnSchemaType implements AsnSchemaType {

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the primitiveType of this definition */
    private final AsnPrimitiveType primitiveType;

    /** the constraints on the primitiveType */
    private final ImmutableSet<AsnSchemaConstraint> constraints;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param primitiveType the underlying primitiveType of the defined primitiveType
     * @param constraint The constraint on the type. Use {@link AsnSchemaConstraint#NULL} if no
     *     constraint.
     *     <p>Example 1 <br>
     *     For {@code SET (SIZE (1..100) OF OCTET STRING (SIZE (10))} this would be {@code (SIZE
     *     (10)}.
     *     <p>Example 2 <br>
     *     For {@code INTEGER (1..256)} this would be {@code (1..256)}.
     * @throws NullPointerException if {@code primitiveType} is {@code null}
     */
    protected AbstractAsnSchemaType(
            AsnPrimitiveType primitiveType, AsnSchemaConstraint constraint) {

        this.primitiveType = requireNonNull(primitiveType);
        this.constraints =
                ImmutableSet.of((constraint == null) ? AsnSchemaConstraint.NULL : constraint);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaType
    // -------------------------------------------------------------------------

    @Override
    public AsnBuiltinType getBuiltinType() {
        // getPrimitiveType may be overridden in some derived classes.
        // by calling it here it means we are less likely to need to override this function too.
        return getPrimitiveType().getBuiltinType();
    }

    @Override
    public ImmutableSet<AsnSchemaConstraint> getConstraints() {
        return constraints;
    }

    @Override
    public AsnPrimitiveType getPrimitiveType() {
        return primitiveType;
    }
}
