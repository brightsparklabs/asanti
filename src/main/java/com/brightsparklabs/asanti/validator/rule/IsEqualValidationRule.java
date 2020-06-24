/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.rule;

import static com.google.common.base.Preconditions.checkNotNull;

import com.brightsparklabs.asanti.data.AsnData;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.ValidationFailure;
import com.brightsparklabs.asanti.validator.ValidationRule;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import java.util.Optional;

/**
 * Validates that the value of a given tag is equal to a particular value. Equality is tested via
 * {@link Object#equals(Object)}.
 *
 * @param <T> the expected type of the data as returned by {@link AsnData#getDecodedObject}
 * @author brightSPARK Labs
 */
public class IsEqualValidationRule<T> implements ValidationRule {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the expected value the tag should have */
    private final T expectedValue;

    /** adds a bit of type safety */
    private final Class<T> classOfT;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param expectedValue the value to test against
     * @param classOfT the class of the T the class of the T
     * @throws NullPointerException if {@code expectedValue} is {@code null}
     */
    public IsEqualValidationRule(T expectedValue, Class<T> classOfT) {
        checkNotNull(expectedValue);
        this.expectedValue = expectedValue;
        this.classOfT = classOfT;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: ValidationRule
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<ValidationFailure> validate(final String tag, final AsnData asnData)
            throws DecodeException {
        final ImmutableSet.Builder<ValidationFailure> builder = ImmutableSet.builder();
        try {
            final Optional<T> actualValue = asnData.getDecodedObject(tag, classOfT);
            final boolean actualValuePresent = actualValue.isPresent();
            if (!actualValuePresent || !expectedValue.equals(actualValue.get())) {
                final String reason =
                        actualValuePresent
                                ? "Found value: ["
                                        + actualValue.get().toString()
                                        + "] but expected value: ["
                                        + expectedValue.toString()
                                        + "]"
                                : "No value found when expecting value: ["
                                        + expectedValue.toString()
                                        + "]";

                final ValidationFailure failure =
                        new DecodedTagValidationFailure(
                                tag, FailureType.CustomValidationFailed, reason);
                builder.add(failure);
            }
        } catch (ClassCastException ex) {
            final ValidationFailure failure =
                    new DecodedTagValidationFailure(
                            tag,
                            FailureType.CustomValidationFailed,
                            "Data was not in the expected format: " + ex.getMessage());
            builder.add(failure);
        }

        return builder.build();
    }
}
