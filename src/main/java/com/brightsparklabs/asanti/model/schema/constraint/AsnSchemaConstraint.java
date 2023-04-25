/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.schema.AsnPrimitiveType;
import com.brightsparklabs.asanti.validator.failure.SchemaConstraintValidationFailure;
import com.google.common.collect.ImmutableSet;

/**
 * Models a Constraint from within an {@link AsnSchemaType} or {@link AsnSchemaComponentType}
 *
 * @author brightSPARK Labs
 */
public interface AsnSchemaConstraint {
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** null instance */
    AsnSchemaConstraint.Null NULL = new AsnSchemaConstraint.Null();

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Applies the constraint to the supplied byte.
     *
     * @param bytes the bytes to test.
     * @param type The ASN.1 type of the bytes.
     * @return any failures encountered in applying the constraint to the supplied bytes.
     */
    ImmutableSet<SchemaConstraintValidationFailure> apply(byte[] bytes, AsnPrimitiveType type);

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: AsnSchemaTypeDefinitionNull
    // -------------------------------------------------------------------------

    /** Null instance of {@link AsnSchemaConstraint} */
    class Null implements AsnSchemaConstraint {
        /**
         * Default constructor. Private, use {@link AsnSchemaConstraint#NULL} instead to obtain a
         * singleton instance
         */
        private Null() {}

        // ---------------------------------------------------------------------
        // IMPLEMENTATION: AsnSchemaConstraint
        // ---------------------------------------------------------------------

        @Override
        public ImmutableSet<SchemaConstraintValidationFailure> apply(
                final byte[] bytes, final AsnPrimitiveType type) {
            return ImmutableSet.of();
        }
    }
}
