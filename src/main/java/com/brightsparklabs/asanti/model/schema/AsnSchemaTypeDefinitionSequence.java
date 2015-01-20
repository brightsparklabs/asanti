/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

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
     * @throws NullPointerException
     *             if {@code name}, or {@code componentTypes} are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} is blank
     */
    public AsnSchemaTypeDefinitionSequence(String name, Iterable<AsnSchemaComponentType> componentTypes)
    {
        super(name, AsnBuiltinType.Sequence, componentTypes);
    }
}
