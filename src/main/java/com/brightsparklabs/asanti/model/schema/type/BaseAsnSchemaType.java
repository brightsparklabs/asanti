package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.assam.schema.AsnBuiltinType;
import com.brightsparklabs.assam.schema.AsnPrimitiveType;
import java.util.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.text.ParseException;

import static com.google.common.base.Preconditions.*;

/**
 * A concrete implementation of class that can model the types for objects within ASN.1 schema.
 * These objects can be either Type Definitions, e.g. Type ::= SomeType, or components within a
 * constructed type (SEQUENCE etc), e.g. component SomeType
 *
 * @author brightSPARK Labs
 */
public class BaseAsnSchemaType implements AsnSchemaType
{

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
     * @param primitiveType
     *         the underlying primitiveType of the defined primitiveType
     * @param constraint
     *         The constraint on the type. Use {@link AsnSchemaConstraint#NULL} if no constraint.
     *
     *         <p> Example 1
     *
     *         <br> For {@code SET (SIZE (1..100) OF OCTET STRING (SIZE (10))} this would be {@code
     *         (SIZE (10)}.
     *
     *         <p> Example 2
     *
     *         <br> For {@code INTEGER (1..256)} this would be {@code (1..256)}.
     *
     * @throws NullPointerException
     *         if {@code primitiveType} is {@code null}
     */
    public BaseAsnSchemaType(AsnPrimitiveType primitiveType, AsnSchemaConstraint constraint)
    {
        checkNotNull(primitiveType);

        this.primitiveType = primitiveType;
        this.constraints = ImmutableSet.of((constraint == null) ?
                AsnSchemaConstraint.NULL :
                constraint);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaType
    // -------------------------------------------------------------------------

    @Override
    public ImmutableList<AsnSchemaComponentType> getAllComponents()
    {
        return ImmutableList.of();
    }

    @Override
    public AsnBuiltinType getBuiltinType()
    {
        // getPrimitiveType may be overridden in some derived classes.
        // by calling it here it means we are less likely to need to override this function too.
        return getPrimitiveType().getBuiltinType();
    }

    @Override
    public ImmutableSet<AsnSchemaConstraint> getConstraints()
    {
        return constraints;
    }

    @Override
    public Optional<AsnSchemaComponentType> getMatchingChild(String tag,
            DecodingSession decodingSession)
    {
        return Optional.empty();
    }

    @Override
    public AsnPrimitiveType getPrimitiveType()
    {
        return primitiveType;
    }

    @Override
    public Object accept(final AsnSchemaTypeVisitor<?> visitor) throws ParseException
    {
        return visitor.visit(this);
    }
}
