/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import static com.google.common.base.Preconditions.*;

import java.math.BigInteger;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.model.schema.typedefinition.AbstractAsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;

/**
 * Models a minimum/maximum value 'bounded' numeric value constraint from within
 * a {@link AbstractAsnSchemaTypeDefinition} or {@link AsnSchemaComponentType}.
 * E.g. {@code INTEGER (0 .. 256)}.
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
     *
     * @throws NullPointerException
     *             if any of the parameters are {@code null}
     */
    public AsnSchemaNumericValueConstraint(BigInteger minimumValue, BigInteger maximumValue)
    {
        checkNotNull(minimumValue);
        checkNotNull(maximumValue);
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaConstraint
    // -------------------------------------------------------------------------

    /**
     * Returns true if the value in the supplied array falls between the minimum
     * and maximum bounds of this constraint. The value of the array is read via
     * {@link BigInteger#BigInteger(byte[])}.
     *
     * @param data
     *            the data to test
     *
     * @return {@code true} if the data conforms to this constraint;
     *         {@code false} otherwise
     */
    @Override
    public OperationResult<byte[]> apply(byte[] data)
    {
        try
        {
            final BigInteger value = new BigInteger(data);
            final boolean conforms = (value.compareTo(minimumValue) > -1) && (value.compareTo(maximumValue) < 1);
            if (conforms)
            {
                return OperationResult.createSuccessfulInstance(data);
            }
            else
            {
                final String error =
                        String.format("Expected a value between %s and %s, but found: %s",
                                minimumValue.toString(),
                                maximumValue.toString(),
                                value.toString());
                return OperationResult.createUnsuccessfulInstance(data, error);
            }
        }
        catch (final NumberFormatException ex)
        {
            final String error =
                    String.format("Expected a value between %s and %s, but no value found",
                            minimumValue.toString(),
                            maximumValue.toString());
            return OperationResult.createUnsuccessfulInstance(data, error);
        }
    }
}
