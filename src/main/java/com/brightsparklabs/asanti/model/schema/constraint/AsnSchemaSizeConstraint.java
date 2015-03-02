/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.model.schema.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;

/**
 * Models a minimum/maximum value 'bounded' SIZE constraint from within a
 * {@link AsnSchemaTypeDefinition} or {@link AsnSchemaComponentType}. E.g.
 * {@code SIZE (0 .. 256)}.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaSizeConstraint implements AsnSchemaConstraint
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the minimum length the data can be */
    private final int minimumLength;

    /** the minimum length the data can be */
    private final int maximumLength;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param minimumLength
     *            the minimum length the data can be
     *
     * @param maximumLength
     *            the minimum length the data can be
     */
    public AsnSchemaSizeConstraint(int minimumLength, int maximumLength)
    {
        this.minimumLength = minimumLength;
        this.maximumLength = maximumLength;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaConstraint
    // -------------------------------------------------------------------------

    /**
     * Returns true if the length of the supplied array falls between the
     * minimum and maximum bounds of this constraint. The content of the byte
     * array is irrelevant.
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
        return length >= minimumLength && length <= maximumLength;
    }
}
