/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks.validator;

import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.ValidationFailure;

/**
 * Utility class for obtaining mocked instances of {@link ValidationFailure}
 *
 * @author brightSPARK Labs
 */
public class MockValidationFailure
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns a mocked instance of a {@link ValidationFailure}
     *
     * @param tag
     *            tag to return from {@link ValidationFailure#getLocation()}
     *
     * @param failureType
     *            tag to return from {@link ValidationFailure#getFailureType()}
     *
     * @param failureReason
     *            tag to return from
     *            {@link ValidationFailure#getFailureReason()}
     *
     * @return a successful {@link ValidationFailure}
     */
    public static ValidationFailure createFailedValidationResult(String tag, FailureType failureType,
            String failureReason)
    {
        final ValidationFailure instance = mock(ValidationFailure.class);
        when(instance.getLocation()).thenReturn(tag);
        when(instance.getFailureType()).thenReturn(failureType);
        when(instance.getFailureReason()).thenReturn(failureReason);
        return instance;
    }
}
