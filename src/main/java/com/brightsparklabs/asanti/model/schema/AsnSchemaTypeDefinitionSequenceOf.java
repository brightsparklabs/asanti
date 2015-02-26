/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

/**
 * A SEQUENCE OF type definition from a within a module specification within an
 * ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionSequenceOf extends AsnSchemaTypeDefinitionCollectionOf
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name
     *            name of the SEQUENCE OF type definition
     *
     * @param elementTypeName
     *            the name of the type for the elements in the SEQUENCE OF. E.g.
     *            for
     *            {@code SEQUENCE (SIZE (1..100)) OF OCTET STRING (SIZE (256))},
     *            this would be {@code OCTET STRING}
     *
     * @param constraint
     *            The constraint on the type. Use
     *            {@link AsnSchemaConstraint#NULL} if no constraint.
     *            <p>
     *            E.g for
     *            {@code SEQUENCE (SIZE (1..100)) OF OCTET STRING (SIZE (256))}
     *            this would be {@code SIZE (256)}
     *
     * @throws NullPointerException
     *             if {@code name}, {@code elementTypeName} or
     *             {@code componentTypes} are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} or {@code elementTypeName} is blank
     */
    public AsnSchemaTypeDefinitionSequenceOf(String name, String elementTypeName, AsnSchemaConstraint constraint)
    {
        super(name, AsnBuiltinType.SequenceOf, elementTypeName, constraint);
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
