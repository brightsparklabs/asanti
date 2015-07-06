package com.brightsparklabs.asanti.model.schema.type;

/**
 * Created by Michael on 6/07/2015.
 */
public interface AsnSchemaNamedType
{
    public static final AsnSchemaNamedType NULL = new AsnSchemaNamedType.Null();

    String getName();

    AsnSchemaType getType();


    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Null
    // -------------------------------------------------------------------------
    public static class Null implements AsnSchemaNamedType
    {
        @Override
        public String getName()
        {
            return "";
        }

        @Override
        public AsnSchemaType getType()
        {
            return AsnSchemaType.NULL;
        }
    }
}
