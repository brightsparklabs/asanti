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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.regex.Pattern;

/** @author brightSPARK Labs */
public class AsPrintableAsciiValidationRule implements ValidationRule {

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** regex to use to retrieve data */
    private final Pattern pattern;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * default constructor.
     *
     * @param pattern regex to use to retrieve data
     */
    public AsPrintableAsciiValidationRule(Pattern pattern) {
        this.pattern = pattern;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: ValidationRule
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<ValidationFailure> validate(final String tag, final AsnData asnData)
            throws DecodeException {
        final ImmutableSet.Builder<ValidationFailure> builder = ImmutableSet.builder();

        final ImmutableMap<String, Object> decoded = asnData.getDecodedObjectsMatching(pattern);

        for (Map.Entry<String, Object> entry : decoded.entrySet()) {
            final String key = entry.getKey();
            final Object element = entry.getValue();

            if (!(element instanceof byte[])) {
                // it is not an octet string
                continue;
            }

            final byte[] bytes = (byte[]) element;

            if (ByteArrays.containsNonPrintableChars(bytes)) {
                final DecodedTagValidationFailure failure =
                        new DecodedTagValidationFailure(
                                key,
                                FailureType.CustomValidationFailed,
                                "Field must contain only printable ASCII characters (0x20-0x7E)");

                builder.add(failure);
            }
        }

        return builder.build();
    }
}
