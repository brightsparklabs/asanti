/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.model.schema.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;

/**
 * Models a 'fixed' SIZE constraint from within a
 * {@link AsnSchemaTypeDefinition} or {@link AsnSchemaComponentType}. E.g.
 * {@code SIZE (10)}.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaFixedSizeConstraint implements AsnSchemaConstraint
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the length the data must be */
    private final int fixedLength;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param fixedLength
     *            the length the data must be
     */
    public AsnSchemaFixedSizeConstraint(int fixedLength)
    {
        this.fixedLength = fixedLength;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaConstraint
    // -------------------------------------------------------------------------

    /**
     * Returns true if the length of the supplied array matches the fixed length
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
        return data.length == fixedLength;
    }
}
