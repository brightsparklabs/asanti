/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;

/**
 * A {@code NumericString} type definition from a within a module specification
 * within an ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionNumericString extends AbstractAsnSchemaTypeDefinition
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name
     *            name of the NumericString type definition
     *
     * @param constraint
     *            The constraint on the type. Use
     *            {@link AsnSchemaConstraint#NULL} if no constraint.
     *            <p>
     *            E.g. For {@code NumericString (SIZE (1..20))} this would be
     *            {@code SIZE (1..20)}
     *
     * @throws NullPointerException
     *             if {@code name} is {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} is blank
     */
    public AsnSchemaTypeDefinitionNumericString(String name, AsnSchemaConstraint constraint)
    {
        super(name, AsnBuiltinType.NumericString, constraint);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaTypeDefinition
    // -------------------------------------------------------------------------

    @Override
    public Object visit(AsnSchemaTypeDefinitionVisitor<?> visitor)
    {
        return visitor.visit(this);
    }
}
