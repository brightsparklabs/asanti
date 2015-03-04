/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import static org.junit.Assert.*;

import org.junit.Test;

import com.brightsparklabs.asanti.mocks.validator.MockValidationFailure;

/**
 * Unit tests for {@link ValidationResultImpl}
 *
 * @author brightSPARK Labs
 */
public class ValidationResultImplTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    private static final ValidationResultImpl noFailures = ValidationResultImpl.builder()
            .build();

    private static final ValidationResultImpl oneFailure = ValidationResultImpl.builder()
            .add(MockValidationFailure.createFailedValidationResult("TEST_TAG1",
                    FailureType.MandatoryFieldMissing,
                    "Field missing"))
            .build();

    private static final ValidationResultImpl multipleFailures = ValidationResultImpl.builder()
            .add(MockValidationFailure.createFailedValidationResult("TEST_TAG1",
                    FailureType.MandatoryFieldMissing,
                    "Field missing"))
            .add(MockValidationFailure.createFailedValidationResult("TEST_TAG1",
                    FailureType.MandatoryFieldMissing,
                    "Field missing"))
            .add(MockValidationFailure.createFailedValidationResult("TEST_TAG3",
                    FailureType.MandatoryFieldMissing,
                    "Field missing"))
            .build();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testHasFailures() throws Exception
    {
        // empty results / no failures
        assertEquals(false, noFailures.hasFailures());

        // one failure
        assertEquals(true, oneFailure.hasFailures());

        // multiple failures
        assertEquals(true, multipleFailures.hasFailures());
    }

    @Test
    public void testHasFailuresString() throws Exception
    {
        // empty results / no failures
        assertEquals(false, noFailures.hasFailures("TEST_TAG1"));
        assertEquals(false, noFailures.hasFailures("TEST_TAG2"));
        assertEquals(false, noFailures.hasFailures("TEST_TAG3"));

        // one failure
        assertEquals(true, oneFailure.hasFailures("TEST_TAG1"));
        assertEquals(false, oneFailure.hasFailures("TEST_TAG2"));
        assertEquals(false, oneFailure.hasFailures("TEST_TAG3"));

        // multiple failures
        assertEquals(true, multipleFailures.hasFailures("TEST_TAG1"));
        assertEquals(false, multipleFailures.hasFailures("TEST_TAG2"));
        assertEquals(true, multipleFailures.hasFailures("TEST_TAG3"));
    }

    @Test
    public void testGetFailures() throws Exception
    {
        // empty results / no failures
        assertEquals(0, noFailures.getFailures()
                .size());

        // one failure
        assertEquals(1, oneFailure.getFailures()
                .size());

        // multiple failures
        assertEquals(3, multipleFailures.getFailures()
                .size());
    }

    @Test
    public void testGetFailuresString() throws Exception
    {
        // empty results / no failures
        assertEquals(0, noFailures.getFailures("TEST_TAG1")
                .size());
        assertEquals(0, noFailures.getFailures("TEST_TAG2")
                .size());
        assertEquals(0, noFailures.getFailures("TEST_TAG3")
                .size());

        // one failure
        assertEquals(1, oneFailure.getFailures("TEST_TAG1")
                .size());
        assertEquals(0, oneFailure.getFailures("TEST_TAG2")
                .size());
        assertEquals(0, oneFailure.getFailures("TEST_TAG3")
                .size());

        // multiple failures
        assertEquals(2, multipleFailures.getFailures("TEST_TAG1")
                .size());
        assertEquals(0, multipleFailures.getFailures("TEST_TAG2")
                .size());
        assertEquals(1, multipleFailures.getFailures("TEST_TAG3")
                .size());
    }
}
