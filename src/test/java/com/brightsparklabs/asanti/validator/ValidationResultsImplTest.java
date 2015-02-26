/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.mocks.validator.MockValidationResult;

/**
 * Unit tests for {@link ValidationResultsImpl}
 *
 * @author brightSPARK Labs
 */
public class ValidationResultsImplTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testHasFailures() throws Exception
    {
        // empty results
        ValidationResultsImpl instance = ValidationResultsImpl.builder()
                .build();
        assertEquals(false, instance.hasFailures());

        // no failures
        instance = ValidationResultsImpl.builder()
                .add(MockValidationResult.createSuccessfulValidationResult("TEST_TAG"))
                .add(MockValidationResult.createSuccessfulValidationResult("TEST_TAG"))
                .build();
        assertEquals(false, instance.hasFailures());

        // one failure
        instance =
                ValidationResultsImpl.builder()
                        .add(MockValidationResult.createSuccessfulValidationResult("TEST_TAG"))
                        .add(MockValidationResult.createFailedValidationResult("TEST_TAG",
                                FailureType.MandatoryFieldMissing,
                                "Field missing"))
                        .add(MockValidationResult.createSuccessfulValidationResult("TEST_TAG"))
                        .build();
        assertEquals(true, instance.hasFailures());

        // multiple failures
        instance =
                ValidationResultsImpl.builder()
                        .add(MockValidationResult.createSuccessfulValidationResult("TEST_TAG"))
                        .add(MockValidationResult.createFailedValidationResult("TEST_TAG",
                                FailureType.MandatoryFieldMissing,
                                "Field missing"))
                        .add(MockValidationResult.createFailedValidationResult("TEST_TAG",
                                FailureType.MandatoryFieldMissing,
                                "Field missing"))
                        .add(MockValidationResult.createSuccessfulValidationResult("TEST_TAG"))
                        .build();
        assertEquals(true, instance.hasFailures());
    }

    @Test
    public void testGetFailures() throws Exception
    {
        // empty results
        ValidationResultsImpl instance = ValidationResultsImpl.builder()
                .build();
        assertEquals(0, instance.getFailures()
                .size());

        // no failures
        instance = ValidationResultsImpl.builder()
                .add(MockValidationResult.createSuccessfulValidationResult("TEST_TAG"))
                .add(MockValidationResult.createSuccessfulValidationResult("TEST_TAG"))
                .build();
        assertEquals(0, instance.getFailures()
                .size());

        // one failure
        instance =
                ValidationResultsImpl.builder()
                        .add(MockValidationResult.createSuccessfulValidationResult("TEST_TAG"))
                        .add(MockValidationResult.createFailedValidationResult("TEST_TAG",
                                FailureType.MandatoryFieldMissing,
                                "Field missing"))
                        .add(MockValidationResult.createSuccessfulValidationResult("TEST_TAG"))
                        .build();
        assertEquals(1, instance.getFailures()
                .size());

        // multiple failures
        instance =
                ValidationResultsImpl.builder()
                        .add(MockValidationResult.createSuccessfulValidationResult("TEST_TAG"))
                        .add(MockValidationResult.createFailedValidationResult("TEST_TAG",
                                FailureType.MandatoryFieldMissing,
                                "Field missing"))
                        .add(MockValidationResult.createFailedValidationResult("TEST_TAG",
                                FailureType.MandatoryFieldMissing,
                                "Field missing"))
                        .add(MockValidationResult.createSuccessfulValidationResult("TEST_TAG"))
                        .build();
        assertEquals(2, instance.getFailures()
                .size());
    }
}
