/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;

/**
 * A CHOICE type definition from a within a module specification within an ASN.1
 * schema.
 *
 * @author brightSPARK Labs
 */
public class OLDAsnSchemaTypeDefinitionChoice extends OLDAsnSchemaTypeDefinitionConstructed
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name
     *            name of the CHOICE type definition
     *
     * @param componentTypes
     *            the choices in this type definition
     *
     * @param constraint
     *            The constraint on the type. Use
     *            {@link AsnSchemaConstraint#NULL} if no constraint.
     *            <p>
     *            E.g. For <code>CHOICE { ... } (CONSTRAINED BY {} ! X)</code>
     *            this would be <code>CONSTRAINED BY {} ! X</code>
     *
     * @throws NullPointerException
     *             if {@code name}, or {@code componentTypes} are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} is blank
     */
    public OLDAsnSchemaTypeDefinitionChoice(String name,
            Iterable<AsnSchemaComponentType> componentTypes, AsnSchemaConstraint constraint)
    {
        super(name, AsnBuiltinType.Choice, componentTypes, constraint);
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
