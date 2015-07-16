package com.brightsparklabs.asanti.model.schema.type;

/**
 * Provides an abstraction of the concept of a Name and an AsnSchemaType
 *
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
        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. This is private. Use {@link AsnSchemaNamedType#NULL} to obtain an
         * instance.
         */
        private Null()
        {
        }

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
