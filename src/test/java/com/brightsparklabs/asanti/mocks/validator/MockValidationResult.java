/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks.validator;

import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.ValidationResult;

/**
 * Utility class for obtaining mocked instances of {@link AsnSchema} which
 * conform to the test ASN.1 schema defined in the {@linkplain README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockValidationResult
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns a mocked instance of a successful {@link ValidationResult}
     *
     * @param tag
     *            tag to return from {@link ValidationResult#getTag()}
     *
     * @return a successful {@link ValidationResult}
     */
    public static ValidationResult createSuccessfulValidationResult(String tag)
    {

        final ValidationResult instance = mock(ValidationResult.class);
        when(instance.getTag()).thenReturn(tag);
        when(instance.isFailure()).thenReturn(false);
        when(instance.getFailureType()).thenReturn(FailureType.None);
        when(instance.getFailureReason()).thenReturn("");
        return instance;
    }

    /**
     * Returns a mocked instance of a failed {@link ValidationResult}
     *
     * @param tag
     *            tag to return from {@link ValidationResult#getTag()}
     *
     * @param failureType
     *            tag to return from {@link ValidationResult#getFailureType()}
     *
     * @param failureReason
     *            tag to return from {@link ValidationResult#getFailureReason()}
     *
     * @return a successful {@link ValidationResult}
     */
    public static ValidationResult createFailedValidationResult(String tag, FailureType failureType,
            String failureReason)
    {
        final ValidationResult instance = mock(ValidationResult.class);
        when(instance.getTag()).thenReturn(tag);
        when(instance.isFailure()).thenReturn(true);
        when(instance.getFailureType()).thenReturn(failureType);
        when(instance.getFailureReason()).thenReturn(failureReason);
        return instance;
    }
}
