/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import java.math.BigInteger;

import com.brightsparklabs.asanti.model.schema.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;

/**
 * Models a minimum/maximum value 'bounded' numeric value constraint from within
 * a {@link AsnSchemaTypeDefinition} or {@link AsnSchemaComponentType}. E.g.
 * {@code INTEGER (0 .. 256)}.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaNumericValueConstraint implements AsnSchemaConstraint
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the minimum value the data can be */
    private final BigInteger minimumValue;

    /** the minimum value the data can be */
    private final BigInteger maximumValue;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param minimumValue
     *            the minimum value the data can be
     *
     * @param maximumValue
     *            the minimum value the data can be
     */
    public AsnSchemaNumericValueConstraint(BigInteger minimumValue, BigInteger maximumValue)
    {
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
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
        final BigInteger value = new BigInteger(data);
        return (value.compareTo(minimumValue) > -1) && (value.compareTo(maximumValue) < 1);
    }
}
