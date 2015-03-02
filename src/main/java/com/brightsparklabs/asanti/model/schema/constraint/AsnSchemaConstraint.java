/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.model.schema.typedefinition.AbstractAsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;

/**
 * Models a Constraint from within a {@link AbstractAsnSchemaTypeDefinition} or
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
    public static final AsnSchemaConstraintNull NULL = new AsnSchemaConstraintNull();

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
     * Null instance of {@link AbstractAsnSchemaTypeDefinition}
     */
    public static class AsnSchemaConstraintNull implements AsnSchemaConstraint
    {
        /**
         * Default constructor. Private, use {@link AsnSchemaConstraint#NULL}
         * instead to obtain a singleton instance
         */
        private AsnSchemaConstraintNull()
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
