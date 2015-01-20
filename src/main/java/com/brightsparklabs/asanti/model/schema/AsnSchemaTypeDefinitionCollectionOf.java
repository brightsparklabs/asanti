/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import static com.google.common.base.Preconditions.*;

/**
 * Base type for modeling SET OF / SEQUENCE OF type definition from a within a
 * module specification within an ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionCollectionOf extends AsnSchemaTypeDefinition
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** name of the type for the elements in this collection */
    private final String elementTypeName;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name
     *            name of the SEQUENCE OF type definition
     *
     * @param builtinType
     *            the underlying ASN.1 type of the defined type
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
    public AsnSchemaTypeDefinitionCollectionOf(String name, AsnBuiltinType builtinType, String elementTypeName,
            String constraints)
    {
        super(name, builtinType);
        checkNotNull(elementTypeName);
        checkArgument(!elementTypeName.trim()
                .isEmpty(), "Element type name must be specified");
        this.elementTypeName = elementTypeName;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaTypeDefinition
    // -------------------------------------------------------------------------

    @Override
    public String getTypeName(String tag)
    {
        return elementTypeName;
    }
}
