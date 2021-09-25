/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.model.data.RawAsnData;
import com.brightsparklabs.asanti.reader.AsnBerDataReader;
import com.brightsparklabs.asanti.schema.AsnPrimitiveType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.SchemaConstraintValidationFailure;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteSource;
import java.io.IOException;

/** @author brightSPARK Labs */
public class AsnSchemaContainingConstraint extends AbstractAsnSchemaConstraint {
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

    /**
     * Default Constructor.
     *
     * @param module the module that the type id defined
     * @param type the type that this is aliasing
     */
    public AsnSchemaContainingConstraint(final String module, final String type) {
        this.module = module;
        this.type = type;
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    @Override
    protected ImmutableSet<SchemaConstraintValidationFailure> applyToNonNullBytes(
            final byte[] bytes, final AsnPrimitiveType type) {
        // This CONTAINING constraint means that we should be able to parse the bytes
        // as valid ber/der.  The decode will try to do that.  The decode part will deal
        // with alignment to schema, but this constraint should verify that the bytes are
        // valid ber/der.
        // It does mean we'll be doing that during decode, and verify, but oh well...

        ImmutableSet.Builder<SchemaConstraintValidationFailure> builder = ImmutableSet.builder();

        final ByteSource byteSource = ByteSource.wrap(bytes);
        try {
            final ImmutableList<RawAsnData> read = AsnBerDataReader.read(byteSource);
            if (read.isEmpty()) {
                builder.add(
                        new SchemaConstraintValidationFailure(
                                FailureType.DataIncorrectlyFormatted,
                                "Got no PDUs from aliased type, got " + read.size()));
            }
        } catch (final IOException e) {
            builder.add(
                    new SchemaConstraintValidationFailure(
                            FailureType.DataIncorrectlyFormatted,
                            "Failed to parse bytes of aliased type"));
        }
        return builder.build();
    }

    /**
     * Returns the module associated with the type this is aliasing
     *
     * @return the module associated with the type this is aliasing
     */
    public String getModule() {
        return module;
    }

    /**
     * Return the type this is aliasing
     *
     * @return the type this is aliasing
     */
    public String getType() {
        return type;
    }
}
