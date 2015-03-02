/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.model.schema.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.AbstractAsnSchemaTypeDefinition;

/**
 * Models an 'exact' SIZE constraint from within a
 * {@link AbstractAsnSchemaTypeDefinition} or {@link AsnSchemaComponentType}. E.g.
 * {@code SIZE (10)}.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaExactSizeConstraint implements AsnSchemaConstraint
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the length the data must be */
    private final int exactLength;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param exactLength
     *            the length the data must be
     */
    public AsnSchemaExactSizeConstraint(int exactLength)
    {
        this.exactLength = exactLength;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaConstraint
    // -------------------------------------------------------------------------

    /**
     * Returns true if the length of the supplied array matches the exact length
     * of this constraint. The content of the byte array is irrelevant.
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
        return data.length == exactLength;
    }
}
