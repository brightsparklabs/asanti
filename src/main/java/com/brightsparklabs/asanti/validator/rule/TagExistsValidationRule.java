/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.rule;

import com.brightsparklabs.asanti.data.AsnData;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.ValidationFailure;
import com.brightsparklabs.asanti.validator.ValidationRule;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;

/**
 * Validates that the given tag exists in the supplied data. This is used for when at the schema
 * level the tag is marked as OPTIONAL, but the business layer determines it to be mandatory.
 *
 * <p>This needs to be "triggered" off of a tag that is guaranteed to exist. So for example if the
 * tag to check is down a path of other Optionals and Choices, and the validation rule is that the
 * tag should exist if the parent exists then register this to be triggered from the parent tag.
 *
 * <pre>{@code
 * .withValidationRule(new TagExistsValidationRule(
 *     "/PS-PDU/payload/cCPayloadSequence[0]/cCContents/iPMMCC/frameType"),
 * "/PS-PDU/payload/cCPayloadSequence[0]/cCContents/iPMMCC")
 * }</pre>
 *
 * will create this validator to check the existence of frameType only if the parent sequence
 * (iPMMCC) exists.
 *
 * @author brightSPARK Labs
 */
public class TagExistsValidationRule implements ValidationRule {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the expected value the tag should have */
    private final String tagToFind;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param tag the tag to check the existence of
     */
    public TagExistsValidationRule(final String tag) {
        tagToFind = tag;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: ValidationRule
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<ValidationFailure> validate(final String tag, final AsnData asnData)
            throws DecodeException {
        final ImmutableSet.Builder<ValidationFailure> builder = ImmutableSet.builder();

        if (!asnData.contains(tagToFind)) {
            final DecodedTagValidationFailure failure =
                    new DecodedTagValidationFailure(
                            tagToFind,
                            FailureType.CustomValidationFailed,
                            "No value found for tag");

            builder.add(failure);
        }

        return builder.build();
    }
}
