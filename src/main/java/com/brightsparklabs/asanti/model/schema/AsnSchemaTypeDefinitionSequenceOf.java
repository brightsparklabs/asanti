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
     *            the name of the type for the elements in this SEQUENCE OF.
     *            E.g. for {@code SEQUENCE OF OCTET STRING}, this would be
     *            {@code OCTET STRING}
     *
     * @param constraints
     *            the constraints on the SEQUENCE OF. E.g for
     *            {@code SEQUENCE (SIZE (1..100) OF OCTET STRING} this would be
     *            {@code (SIZE (1..100)}
     *
     * @throws NullPointerException
     *             if {@code name}, {@code elementTypeName} or
     *             {@code componentTypes} are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} or {@code elementTypeName} is blank
     */
    public AsnSchemaTypeDefinitionSequenceOf(String name, String elementTypeName, String constraints)
    {
        super(name, AsnBuiltinType.SequenceOf, elementTypeName, constraints);
    }
}
