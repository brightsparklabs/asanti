/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;

/**
 * Convenience class to simplify implementing {@link AsnSchemaTypeDefinition}
 *
 * @author brightSPARK Labs
 */
public abstract class AbstractAsnSchemaTypeDefinition implements AsnSchemaTypeDefinition
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** name of the type definition */
    private final String name;

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
     * @param name
     *            name of the defined type
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
    public AbstractAsnSchemaTypeDefinition(String name, AsnBuiltinType builtinType, AsnSchemaConstraint constraint)
    {
        checkNotNull(name);
        checkArgument(!name.trim()
                .isEmpty(), "Tag name must be specified");
        checkNotNull(builtinType);

        this.name = name.trim();
        this.builtinType = builtinType;
        this.constraint = (constraint == null) ? AsnSchemaConstraint.NULL : constraint;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaTypeDefinition
    // -------------------------------------------------------------------------

    @Override
    public String getName()
    {
        return name;
    }

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

    @Override
    public String getTagName(String tag)
    {
        // no constructs within base type definition
        return "";
    }

    @Override
    public String getTypeName(String tag)
    {
        // no constructs within base type definition
        return "";
    }
}
