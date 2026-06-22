/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;

/**
 * A concrete {@code AsnSchemaTypeDefinition}.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionImpl implements AsnSchemaTypeDefinition {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** Name of the type definition. */
    private final String name;

    /** Type of the type definition. */
    private final AsnSchemaType type;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name Name of the defined type.
     * @param type The underlying ASN.1 type of the defined type.
     * @throws NullPointerException if {@code name} or {@code type} are {@code null}.
     * @throws IllegalArgumentException if {@code name} is blank.
     */
    public AsnSchemaTypeDefinitionImpl(final String name, final AsnSchemaType type) {
        checkNotNull(name);
        checkArgument(!name.trim().isEmpty(), "Tag name must be specified");
        checkNotNull(type);

        this.name = name.trim();
        this.type = type;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaTypeDefinition
    // -------------------------------------------------------------------------

    @Override
    public String getName() {
        return name;
    }

    @Override
    public AsnSchemaType getType() {
        return type;
    }
}
