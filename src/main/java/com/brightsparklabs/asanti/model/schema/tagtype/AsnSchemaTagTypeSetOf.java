package com.brightsparklabs.asanti.model.schema.tagtype;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTagTypeVisitor;

/**
 * A {@code SETOF} component/tag type from a within a module specification
 * within an ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTagTypeSetOf extends AbstractAsnSchemaTagType
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param constraint
     *            The constraint on the type. Use
     *            {@link AsnSchemaConstraint#NULL} if no constraint.
     *            <p>
     *            E.g for
     *            {@code SET (SIZE (1..100)) OF OCTET STRING (SIZE (256))}
     *            this would be {@code SIZE (1..100)}
     *
     * @throws NullPointerException
     *             if {@code name}, {@code elementTypeName} or
     *             {@code componentTypes} are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} or {@code elementTypeName} is blank
     */
    public AsnSchemaTagTypeSetOf(AsnSchemaConstraint constraint)
    {
        super(AsnBuiltinType.SetOf, constraint);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: OLDAsnSchemaTypeDefinition
    // -------------------------------------------------------------------------

    @Override
    public Object visit(AsnSchemaTagTypeVisitor<?> visitor)
    {
        return visitor.visit(this);
    }

}
