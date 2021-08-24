/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.validator.failure.SchemaConstraintValidationFailure;
import com.google.common.collect.ImmutableSet;

/** @author brightSPARK Labs */
public class AsnSchemaContainsConstraint extends AbstractAsnSchemaConstraint {
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    private final String module;
    private final String type;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    public AsnSchemaContainsConstraint(final String module, final String type) {
        this.module = module;
        this.type = type;
    }
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    @Override
    protected ImmutableSet<SchemaConstraintValidationFailure> applyToNonNullBytes(
            final byte[] bytes) {
        // TODO _ MJF: In theory the validation will apply to the parsed tree here.
        return ImmutableSet.of();
    }

    public String getModule() {
        return module;
    }

    public String getType() {
        return type;
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------
}
