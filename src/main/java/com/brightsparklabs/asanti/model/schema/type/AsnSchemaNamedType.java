/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.type;

/**
 * Provides an abstraction of the concept of a Name and an AsnSchemaType
 *
 * @author brightSPARK Labs
 */
public interface AsnSchemaNamedType
{
    public static final AsnSchemaNamedType NULL = new AsnSchemaNamedType.Null();

    /**
     * @return the Name of this named type.
     */
    String getName();

    /**
     * @return the {@link AsnSchemaType} of this named type.
     */
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
