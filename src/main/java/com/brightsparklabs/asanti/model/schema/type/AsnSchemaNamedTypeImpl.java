/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.type;

import static com.google.common.base.Preconditions.*;

/**
 * Provides an abstraction of the concept of a Name and an AsnSchemaType
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaNamedTypeImpl implements AsnSchemaNamedType
{

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** name */
    private final String name;

    /** type */
    private final AsnSchemaType type;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default Constructor
     *
     * @param name
     *         the name for this AsnSchemaNamedType
     * @param type
     *         the {@link AsnSchemaType} for this AsnSchemaNamedType
     *
     * @throws NullPointerException
     *         if name or type are null
     */
    public AsnSchemaNamedTypeImpl(String name, AsnSchemaType type)
    {
        checkNotNull(name);
        checkNotNull(type);

        this.name = name;
        this.type = type;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaNamedType
    // -------------------------------------------------------------------------

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public AsnSchemaType getType()
    {
        return type;
    }
}
