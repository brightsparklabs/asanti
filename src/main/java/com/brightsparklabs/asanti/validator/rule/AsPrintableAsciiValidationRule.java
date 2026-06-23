/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.rule;

import com.brightsparklabs.asanti.common.ByteArrays;
import com.brightsparklabs.asanti.data.AsnData;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.ValidationFailure;
import com.brightsparklabs.asanti.validator.ValidationRule;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Validation rule to ensure data contains only printable ASCII characters.
 *
 * @author brightSPARK Labs
 */
public class AsPrintableAsciiValidationRule implements ValidationRule {

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** Regex to use to retrieve data. */
    private final Pattern pattern;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param pattern The regex to use to retrieve data.
     */
    public AsPrintableAsciiValidationRule(final Pattern pattern) {
        this.pattern = pattern;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: ValidationRule
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<ValidationFailure> validate(final String ignored, final AsnData asnData)
            throws DecodeException {
        final ImmutableSet.Builder<ValidationFailure> builder = ImmutableSet.builder();

        final ImmutableSet<String> tags = getTags(asnData);

        for (final String tag : tags) {
            final Optional<Object> decodedValue = asnData.getDecodedObject(tag, Object.class);

            if (decodedValue.isEmpty() || !(decodedValue.get() instanceof final byte[] bytes)) {
                // it is not an octet string
                continue;
            }

            if (ByteArrays.containsNonPrintableChars(bytes)) {
                final DecodedTagValidationFailure failure =
                        new DecodedTagValidationFailure(
                                tag,
                                FailureType.CustomValidationFailed,
                                "Field must contain only printable ASCII characters (0x20-0x7E)");

                builder.add(failure);
            }
        }

        return builder.build();
    }

    // -------------------------------------------------------------------------
    // PROTECTED METHODS
    // -------------------------------------------------------------------------

    /**
     * Gets the tags to validate.
     *
     * @param asnData The AsnData holding the tags.
     * @return The tags to validate.
     */
    protected ImmutableSet<String> getTags(final AsnData asnData) {
        return asnData.getTagsMatching(pattern);
    }
}
