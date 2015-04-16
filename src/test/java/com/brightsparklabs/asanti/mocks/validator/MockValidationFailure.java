/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks.validator;

import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ValidationFailure;

import static org.mockito.Mockito.*;

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
     * @param location
     *            location to return from {@link ValidationFailure#getLocation()}
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
    public static ValidationFailure createFailedValidationResult(String location, FailureType failureType,
            String failureReason)
    {
        final ValidationFailure instance = mock(ValidationFailure.class);
        when(instance.getLocation()).thenReturn(location);
        when(instance.getFailureType()).thenReturn(failureType);
        when(instance.getFailureReason()).thenReturn(failureReason);
        return instance;
    }
}
