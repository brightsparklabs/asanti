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

    /** the name of the tag the validation failure occurred on */
    private final String tag;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param tag the name of the tag the validation failure occurred on
     * @param failureType the type of failure that occurred
     * @param failureReason the reason for the failure
     * @throws NullPointerException if parameters are {@code null}
     * @throws IllegalArgumentException if location or failureReason are empty
     */
    public DecodedTagValidationFailure(String tag, FailureType failureType, String failureReason) {
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
