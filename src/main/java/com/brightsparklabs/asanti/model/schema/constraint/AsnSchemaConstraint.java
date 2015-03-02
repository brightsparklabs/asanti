/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.model.schema.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;

/**
 * Models a Constraint from within a {@link AsnSchemaTypeDefinition} or
 * {@link AsnSchemaComponentType}
 *
 * @author brightSPARK Labs
 */
public interface AsnSchemaConstraint
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** null instance */
    public static final AsnSchemaTypeDefinitionNull NULL = new AsnSchemaTypeDefinitionNull();

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns whether the supplied data conforms to this constraint
     *
     * @param data
     *            the data to test
     *
     * @return {@code true} if the data conforms to this constraint;
     *         {@code false} otherwise
     */
    public boolean isMet(byte[] data);

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: AsnSchemaTypeDefinitionNull
    // -------------------------------------------------------------------------

    /**
     * Null instance of {@link AsnSchemaTypeDefinition}
     */
    public static class AsnSchemaTypeDefinitionNull implements AsnSchemaConstraint
    {
        /**
         * Default constructor. Private, use {@link AsnSchemaConstraint#NULL}
         * instead to obtain a singleton instance
         */
        private AsnSchemaTypeDefinitionNull()
        {
        }

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: AsnSchemaConstraint
        // ---------------------------------------------------------------------

        @Override
        public boolean isMet(byte[] data)
        {
            return true;
        }
    }
}
