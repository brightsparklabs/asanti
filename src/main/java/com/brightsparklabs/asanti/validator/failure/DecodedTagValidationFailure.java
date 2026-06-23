/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.failure;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodedTag;
import com.brightsparklabs.asanti.validator.FailureType;

/**
 * Represents a validation failure from validating a {@link DecodedTag} against its associated
 * {@link AsnSchema}.
 *
 * @author brightSPARK Labs
 */
public class DecodedTagValidationFailure extends AbstractValidationFailure {
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** The name of the tag the validation failure occurred on. */
    private final String tag;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param tag The name of the tag the validation failure occurred on.
     * @param failureType The type of failure that occurred.
     * @param failureReason The reason for the failure.
     * @throws NullPointerException iI parameters are {@code null}.
     * @throws IllegalArgumentException If location or failureReason are empty.
     */
    public DecodedTagValidationFailure(
            final String tag, final FailureType failureType, final String failureReason) {
        super(failureType, failureReason);
        checkNotNull(tag);
        this.tag = tag.trim();
        checkArgument(!this.tag.isEmpty(), "Tag cannot be empty");
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractValidationFailure
    // -------------------------------------------------------------------------

    @Override
    public String getFailureTag() {
        return tag;
    }
}
