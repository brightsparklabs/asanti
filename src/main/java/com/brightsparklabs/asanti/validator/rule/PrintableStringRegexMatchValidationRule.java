/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.rule;

import static com.google.common.base.Preconditions.checkNotNull;

import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.brightsparklabs.assam.data.AsnData;
import com.brightsparklabs.assam.exception.DecodeException;
import com.brightsparklabs.assam.validator.FailureType;
import com.brightsparklabs.assam.validator.ValidationFailure;
import com.brightsparklabs.assam.validator.ValidationRule;
import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Validates that the printable string value of a given tag matches a regex. Value is extracted via
 * {@link AsnData#getPrintableString(String)}.
 *
 * @author brightSPARK Labs
 */
public class PrintableStringRegexMatchValidationRule implements ValidationRule {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** regex to use to match value */
    private final Pattern pattern;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param pattern Regex to use to match data.
     */
    public PrintableStringRegexMatchValidationRule(Pattern pattern) {
        checkNotNull(pattern);
        this.pattern = pattern;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: ValidationRule
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<ValidationFailure> validate(final String tag, final AsnData asnData)
            throws DecodeException {
        final Optional<String> value = asnData.getPrintableString(tag);
        if (value.isPresent() && pattern.matcher(value.get()).matches()) {
            // validation successful -> no failures
            return ImmutableSet.of();
        }

        final ValidationFailure failure =
                new DecodedTagValidationFailure(
                        tag,
                        FailureType.CustomValidationFailed,
                        "No value found when expecting a string which matches regex ["
                                + pattern.toString()
                                + "]");
        return ImmutableSet.of(failure);
    }
}
