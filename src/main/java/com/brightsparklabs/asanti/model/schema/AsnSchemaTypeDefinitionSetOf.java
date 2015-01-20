/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

/**
 * A SET OF type definition from a within a module specification within an ASN.1
 * schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionSetOf extends AsnSchemaTypeDefinitionCollectionOf
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name
     *            name of the SET OF type definition
     *
     * @param elementTypeName
     *            the name of the type for the elements in this SET OF. E.g. for
     *            {@code SET OF OCTET STRING}, this would be
     *            {@code OCTET STRING}
     *
     * @param constraints
     *            the constraints on the SET OF. E.g for
     *            {@code SET (SIZE (1..100) OF OCTET STRING} this would be
     *            {@code (SIZE (1..100)}
     *
     * @throws NullPointerException
     *             if {@code name}, {@code elementTypeName} or
     *             {@code componentTypes} are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} or {@code elementTypeName} is blank
     */
    public AsnSchemaTypeDefinitionSetOf(String name, String elementTypeName, String constraints)
    {
        super(name, AsnBuiltinType.SetOf, elementTypeName, constraints);
    }
}
