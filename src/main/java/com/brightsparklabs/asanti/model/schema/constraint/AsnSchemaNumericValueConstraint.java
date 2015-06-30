/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.SchemaConstraintValidationFailure;
import com.google.common.collect.ImmutableSet;

import java.math.BigInteger;

import static com.google.common.base.Preconditions.*;

/**
 * Models a minimum/maximum value 'bounded' numeric value constraint from within a {@link
 * AsnSchemaType} or {@link AsnSchemaComponentType}. E.g. {@code INTEGER (0 ..
 * 256)}.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaNumericValueConstraint extends AbstractAsnSchemaConstraint
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
     *         the minimum value the data can be
     * @param maximumValue
     *         the minimum value the data can be
     *
     * @throws NullPointerException
     *         if any of the parameters are {@code null}
     */
    public AsnSchemaNumericValueConstraint(BigInteger minimumValue, BigInteger maximumValue)
    {
        checkNotNull(minimumValue);
        checkNotNull(maximumValue);
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractAsnSchemaConstraint
    // -------------------------------------------------------------------------

    /**
     * Checks if the value in the supplied array falls between the minimum and maximum bounds of
     * this constraint. The value of the array is read via {@link BigInteger#BigInteger(byte[])}.
     *
     * @param bytes
     *         the bytes to test
     *
     * @return any failures encountered in applying the constraint to the supplied bytes
     */
    @Override
    public ImmutableSet<SchemaConstraintValidationFailure> applyToNonNullBytes(byte[] bytes)
    {
        try
        {
            final BigInteger value = new BigInteger(bytes);
            final boolean conforms = (value.compareTo(minimumValue) > -1) && (
                    value.compareTo(maximumValue) < 1);
            if (conforms)
            {
                return ImmutableSet.of();
            }
            else
            {
                final String error = String.format(
                        "Expected a value between %s and %s, but found: %s",
                        minimumValue.toString(),
                        maximumValue.toString(),
                        value.toString());
                final SchemaConstraintValidationFailure failure
                        = new SchemaConstraintValidationFailure(FailureType.SchemaConstraint,
                        error);
                return ImmutableSet.of(failure);
            }
        }
        catch (final NumberFormatException ex)
        {
            final String error = String.format(
                    "Expected a value between %s and %s, but no value found",
                    minimumValue.toString(),
                    maximumValue.toString());
            final SchemaConstraintValidationFailure failure = new SchemaConstraintValidationFailure(
                    FailureType.SchemaConstraint,
                    error);
            return ImmutableSet.of(failure);
        }
    }
}
