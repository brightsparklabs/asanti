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
 * Models an 'exact' numeric value constraint from within a
 * {@link AbstractAsnSchemaTypeDefinition} or {@link AsnSchemaComponentType}.
 * E.g. {@code INTEGER (10)}.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaExactNumericValueConstraint implements AsnSchemaConstraint
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the value the data must be */
    private final BigInteger exactValue;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param exactValue
     *            the value the data must be
     *
     * @throws NullPointerException
     *             if any of the parameters are {@code null}
     */
    public AsnSchemaExactNumericValueConstraint(BigInteger exactValue)
    {
        checkNotNull(exactValue);
        this.exactValue = exactValue;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaConstraint
    // -------------------------------------------------------------------------

    /**
     * Returns true if the value in the supplied array matches the exact value
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
    public OperationResult<byte[]> apply(byte[] data)
    {
        try
        {
            final BigInteger value = new BigInteger(data);
            if (exactValue.equals(value))
            {
                return OperationResult.createSuccessfulInstance(data);
            }
            else
            {
                final String error =
                        String.format("Expected a value of %s, but found: %s", exactValue.toString(), value.toString());
                return OperationResult.createUnsuccessfulInstance(data, error);
            }
        }
        catch (final NumberFormatException ex)
        {
            final String error = String.format("Expected a value of %s, but no value found", exactValue.toString());
            return OperationResult.createUnsuccessfulInstance(data, error);
        }
    }
}
