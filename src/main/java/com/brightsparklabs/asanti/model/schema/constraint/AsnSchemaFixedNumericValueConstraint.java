/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import static com.google.common.base.Preconditions.*;

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
public class AsnSchemaFixedNumericValueConstraint implements AsnSchemaConstraint
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the length the data must be */
    private final BigInteger fixedValue;

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
     *
     * @throws NullPointerException
     *             if any of the parameters are {@code null}
     */
    public AsnSchemaFixedNumericValueConstraint(BigInteger fixedValue)
    {
        checkNotNull(fixedValue);
        this.fixedValue = fixedValue;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaConstraint
    // -------------------------------------------------------------------------

    /**
     * Returns true if the value in the supplied array matches the fixed value
     * of this constraint. The value of the array is read via
     * {@link BigInteger#BigInteger(byte[])}.
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
        try
        {
            final BigInteger value = new BigInteger(data);
            return fixedValue.equals(value);
        }
        catch (final NumberFormatException ex)
        {
            return false;
        }
    }
}
