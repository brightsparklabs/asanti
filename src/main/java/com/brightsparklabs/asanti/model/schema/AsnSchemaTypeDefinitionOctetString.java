/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

/**
 * An {@code OCTET STRING} type definition from a within a module specification
 * within an ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionOctetString extends AsnSchemaTypeDefinition
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name
     *            name of the ENUMERATED type definition
     *
     * @param constraints
     *            the constraints on the type. Use {@code null} if no
     *            constraints.
     *
     * @throws NullPointerException
     *             if {@code name} is {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} is blank
     */
    public AsnSchemaTypeDefinitionOctetString(String name, String constraints)
    {
        super(name, AsnBuiltinType.OctetString, constraints);
    }
}
