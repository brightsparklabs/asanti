/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.common.OperationResult;
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
     * Returns the result from applying the constraint to the supplied data
     *
     * @param data
     *            the data to test
     *
     * @return a successful result if the data conforms to this constraint; an
     *         unsuccessful result otherwise. The result contains the data which
     *         was tested to generate the result.
     */
    public OperationResult<byte[]> apply(byte[] data);

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
        public OperationResult<byte[]> apply(byte[] data)
        {
            return OperationResult.createSuccessfulInstance(data);
        }
    }
}
