/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.model.schema.typedefinition.AbstractOLDAsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.brightsparklabs.asanti.validator.failure.SchemaConstraintValidationFailure;
import com.google.common.collect.ImmutableSet;

/**
 * Models a Constraint from within a {@link AbstractOLDAsnSchemaTypeDefinition} or {@link
 * AsnSchemaComponentType}
 *
 * @author brightSPARK Labs
 */
public interface AsnSchemaConstraint
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** null instance */
    public static final AsnSchemaConstraint.Null NULL = new AsnSchemaConstraint.Null();

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Applies the constraint to the supplied bytes
     *
     * @param bytes
     *         the bytes to test
     *
     * @return any failures encountered in applying the constraint to the supplied bytes
     */
    public ImmutableSet<SchemaConstraintValidationFailure> apply(byte[] bytes);

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: AsnSchemaTypeDefinitionNull
    // -------------------------------------------------------------------------

    /**
     * NULL instance of {@link AbstractOLDAsnSchemaTypeDefinition}
     */
    public static class Null implements AsnSchemaConstraint
    {
        /**
         * Default constructor. Private, use {@link AsnSchemaConstraint#NULL} instead to obtain a
         * singleton instance
         */
        private Null() { }

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: AsnSchemaConstraint
        // ---------------------------------------------------------------------

        @Override
        public ImmutableSet<SchemaConstraintValidationFailure> apply(byte[] bytes)
        {
            return ImmutableSet.of();
        }
    }
}
