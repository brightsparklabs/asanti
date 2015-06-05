package com.brightsparklabs.asanti.model.schema.tagtype;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;

import static com.google.common.base.Preconditions.*;

/**
 * Convenience class to simplify implementing {@link AsnSchemaTagType}
 *
 * @author brightSPARK Labs
 */
public abstract class AbstractAsnSchemaTagType implements AsnSchemaTagType
{
    /**
     * the underlying ASN.1 built-in type for this type (SEQUENCE, INTEGER, etc)
     */
    private final AsnBuiltinType builtinType;

    /** the constraint on the type */
    private final AsnSchemaConstraint constraint;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param builtinType
     *            the underlying ASN.1 type of the defined type
     *
     * @param constraint
     *            The constraint on the type. Use
     *            {@link AsnSchemaConstraint#NULL} if no constraint.
     *            <p>
     *            Example 1<br>
     *            For {@code SET (SIZE (1..100) OF OCTET STRING (SIZE (10))}
     *            this would be {@code (SIZE (10)}.
     *            <p>
     *            Example 2<br>
     *            For {@code INTEGER (1..256)} this would be {@code (1..256)}.
     *
     * @throws NullPointerException
     *             if {@code name} or {@code builtinType} are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} is blank
     */
    public AbstractAsnSchemaTagType(AsnBuiltinType builtinType, AsnSchemaConstraint constraint)
    {
        checkNotNull(builtinType);

        this.builtinType = builtinType;
        this.constraint = (constraint == null) ? AsnSchemaConstraint.NULL : constraint;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaTagType
    // -------------------------------------------------------------------------
    @Override
    public AsnBuiltinType getBuiltinType()
    {
        return builtinType;
    }

    @Override
    public AsnSchemaConstraint getConstraint()
    {
        return constraint;
    }


}
