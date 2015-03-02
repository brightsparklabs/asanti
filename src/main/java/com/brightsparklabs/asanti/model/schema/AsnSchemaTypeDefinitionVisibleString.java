/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;

/**
 * A {@code VisibleString} type definition from a within a module specification
 * within an ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionVisibleString extends AbstractAsnSchemaTypeDefinition
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name
     *            name of the VisibleString type definition
     *
     * @param constraint
     *            The constraint on the type. Use
     *            {@link AsnSchemaConstraint#NULL} if no constraint.
     *            <p>
     *            E.g. For {@code VisibleString (SIZE (3..7, ...))} this would be
     *            {@code SIZE (3..7, ...)}
     *
     * @throws NullPointerException
     *             if {@code name} is {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} is blank
     */
    public AsnSchemaTypeDefinitionVisibleString(String name, AsnSchemaConstraint constraint)
    {
        super(name, AsnBuiltinType.VisibleString, constraint);
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
