package com.brightsparklabs.asanti.model.schema.typedefinition;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;

import static com.google.common.base.Preconditions.*;

/**
 * Created by Michael on 16/06/2015.
 */
public class AsnSchemaTypeDefinitionImpl implements AsnSchemaTypeDefinition
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** name of the type definition */
    private final String name;

    /** type of the type definition */
    private final AsnSchemaType type;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name
     *            name of the defined type
     *
     * @param type
     *            the underlying ASN.1 type of the defined type
     *
     *
     * @throws NullPointerException
     *             if {@code name} or {@code builtinType} are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} is blank
     */
    public AsnSchemaTypeDefinitionImpl(String name, AsnSchemaType type)
    {
        checkNotNull(name);
        checkArgument(!name.trim()
                .isEmpty(), "Tag name must be specified");
        checkNotNull(type);

        this.name = name.trim();
        this.type = type;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaTypeDefinition
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
