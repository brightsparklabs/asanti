/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;

/**
 * A SEQUENCE type definition from a within a module specification within an
 * ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionSequence extends AsnSchemaTypeDefinitionConstructed
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name
     *            name of the SEQUENCE type definition
     *
     * @param componentTypes
     *            the component types in this type definition
     *
     * @param constraint
     *            The constraint on the type. Use
     *            {@link AsnSchemaConstraint#NULL} if no constraint.
     *            <p>
     *            E.g. For
     *            <code>SEQUENCE { ... } (CONSTRAINED BY {Person:title})</code>
     *            this would be <code>CONSTRAINED BY {Person:title}</code>
     *
     * @throws NullPointerException
     *             if {@code name}, or {@code componentTypes} are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} is blank
     */
    public AsnSchemaTypeDefinitionSequence(String name, Iterable<AsnSchemaComponentType> componentTypes,
            AsnSchemaConstraint constraint)
    {
        super(name, AsnBuiltinType.Sequence, componentTypes, constraint);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaTypeDefinition
    // -------------------------------------------------------------------------

    @Override
    public Object visit(AsnSchemaTagTypeVisitor<?> visitor)
    {
        return visitor.visit(this);
    }
}
