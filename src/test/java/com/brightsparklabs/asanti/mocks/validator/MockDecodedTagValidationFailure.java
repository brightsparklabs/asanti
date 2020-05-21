/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.mocks.validator;

import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.ValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;

/**
 * Utility class for obtaining mocked instances of {@link DecodedTagValidationFailure}
 *
 * @author brightSPARK Labs
 */
public class MockDecodedTagValidationFailure {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns a mocked instance of a {@link DecodedTagValidationFailure}
     *
     * @param tag tag to return from {@link DecodedTagValidationFailure#getFailureTag()}
     * @param failureType tag to return from {@link DecodedTagValidationFailure#getFailureType()}
     * @param failureReason tag to return from {@link
     *     DecodedTagValidationFailure#getFailureReason()}
     * @return a successful {@link ValidationFailure}
     */
    public static DecodedTagValidationFailure createFailedValidationResult(
            String tag, FailureType failureType, String failureReason) {
        final DecodedTagValidationFailure instance = mock(DecodedTagValidationFailure.class);
        when(instance.getFailureTag()).thenReturn(tag);
        when(instance.getFailureType()).thenReturn(failureType);
        when(instance.getFailureReason()).thenReturn(failureReason);
        return instance;
    }
}
