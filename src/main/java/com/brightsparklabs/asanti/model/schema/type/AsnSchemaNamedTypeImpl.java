package com.brightsparklabs.asanti.model.schema.type;

/**
 * TODO MJF
 */
public class AsnSchemaNamedTypeImpl implements AsnSchemaNamedType
{
    private final String name;
    private final AsnSchemaType type;

    public AsnSchemaNamedTypeImpl(String name, AsnSchemaType type)
    {
        this.name = name;
        this.type = type;
    }

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
