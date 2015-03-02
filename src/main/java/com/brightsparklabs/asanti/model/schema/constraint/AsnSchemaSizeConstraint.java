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
public class AsnSchemaSizeConstraint implements AsnSchemaConstraint
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the minimum length the data can be */
    private final int minimumBound;

    /** the minimum length the data can be */
    private final int maximumBound;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param minimumBound
     *            the minimum length the data can be
     *
     * @param maximumBound
     *            the minimum length the data can be
     */
    public AsnSchemaSizeConstraint(int minimumBound, int maximumBound)
    {
        this.minimumBound = minimumBound;
        this.maximumBound = maximumBound;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaConstraint
    // -------------------------------------------------------------------------

    /**
     * Returns true if the length of the supplied array falls between the
     * minimum and maximum bounds of this constraints. The contents of the byte
     * array are irrelevant.
     *
     * @param data
     *            the data to test
     *
     * @return {@code true} if the data conforms to this constraint;
     *         {@code false} otherwise
     */
    @Override
    public boolean isMet(byte[] data)
    {
        final int length = data.length;
        return length >= minimumBound && length <= maximumBound;
    }
}
