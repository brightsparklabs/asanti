/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;

/**
 * A {@code GeneralizedTime} type definition from a within a module specification
 * within an ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class OLDAsnSchemaTypeDefinitionGeneralizedTime extends OLDAsnSchemaTypeDefinitionPrimitive
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name
     *            name of the GeneralizedTime type definition
     *
     * @param constraint
     *            The constraint on the type. Use
     *            {@link AsnSchemaConstraint#NULL} if no constraint.
     *
     * @throws NullPointerException
     *             if {@code name} is {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} is blank
     */
    public OLDAsnSchemaTypeDefinitionGeneralizedTime(String name, AsnSchemaConstraint constraint)
    {
        super(name, AsnBuiltinType.GeneralizedTime, constraint);
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
