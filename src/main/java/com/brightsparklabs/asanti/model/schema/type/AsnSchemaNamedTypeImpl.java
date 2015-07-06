package com.brightsparklabs.asanti.model.schema.type;

/**
 * Created by Michael on 6/07/2015.
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
