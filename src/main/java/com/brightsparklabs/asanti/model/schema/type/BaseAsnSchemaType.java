package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.google.common.collect.ImmutableSet;

import java.util.regex.Pattern;

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
    // CLASS VARIABLES
    // -------------------------------------------------------------------------
    /** pattern to match a Universal type tag coming out of the BER decoder */
    protected static final Pattern PATTERN_UNIVERSAL_TYPE_TAG = Pattern.compile(
            "^\\(u\\.([a-zA-Z0-9]+)(\\.([0-9]+))?\\)");

    /** pattern to match a Universal type tag coming out of the BER decoder */
    protected static final Pattern PATTERN_UNIVERSAL_TYPE_TAG2 = Pattern.compile(
            "^([0-9]+)(\\((u\\.([a-zA-Z0-9]+)(\\.([0-9]+))?)\\))$");


    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the primitiveType of this definition */ // TODO MJF
    protected final AsnPrimitiveType primitiveType;

    /** the constraint on the primitiveType */
    private final ImmutableSet<AsnSchemaConstraint> constraints;

    /** the parent type if this type is defined in terms on another */
    //private final AsnSchemaType indirectType;

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
    public AsnPrimitiveType getPrimitiveType()
    {
        return primitiveType;
    }

    @Override
    public AsnBuiltinType getBuiltinType()
    {
        // getPrimitiveType may be overridden in some derived classes.
        // by calling it here it means we are less likely to need to override this function too.
        return getPrimitiveType().getBuiltinType();
    }

    @Override
    public AsnBuiltinType getBuiltinTypeAA()
    {
        return primitiveType.getBuiltinType();
    }


    @Override
    public ImmutableSet<AsnSchemaConstraint> getConstraints()
    {
        return constraints;
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
