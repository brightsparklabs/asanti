/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.brightsparklabs.assam.validator.FailureType;
import org.junit.Test;

/**
 * Unit tests for {@link DecodedTagValidationFailure}
 *
 * @author brightSPARK Labs
 */
public class ValidationFailureImplTest {
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidationResultImpl() throws Exception {
        // test valid
        new DecodedTagValidationFailure("TEST_TAG", FailureType.UnknownTag, "TEST_REASON");

        // test null
        try {
            new DecodedTagValidationFailure(null, FailureType.UnknownTag, "TEST_REASON");
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }
        try {
            new DecodedTagValidationFailure("TEST_TAG", null, "TEST_REASON");
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }
        try {
            new DecodedTagValidationFailure("TEST_TAG", FailureType.UnknownTag, null);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }

        // test empty
        try {
            new DecodedTagValidationFailure("", FailureType.UnknownTag, "TEST_REASON");
            fail("IllegalArgumentException not thrown");
        } catch (final IllegalArgumentException ex) {
        }
        try {
            new DecodedTagValidationFailure(" ", FailureType.UnknownTag, "TEST_REASON");
            fail("IllegalArgumentException not thrown");
        } catch (final IllegalArgumentException ex) {
        }
        try {
            new DecodedTagValidationFailure("TEST_TAG", FailureType.UnknownTag, "");
            fail("IllegalArgumentException not thrown");
        } catch (final IllegalArgumentException ex) {
        }
        try {
            new DecodedTagValidationFailure("TEST_TAG", FailureType.UnknownTag, " ");
            fail("IllegalArgumentException not thrown");
        } catch (final IllegalArgumentException ex) {
        }
    }

    @Test
    public void testGetTag() throws Exception {
        final DecodedTagValidationFailure instance =
                new DecodedTagValidationFailure("TEST_TAG", FailureType.UnknownTag, "TEST_REASON");
        assertEquals("TEST_TAG", instance.getFailureTag());
    }

    @Test
    public void testGetFailureType() throws Exception {
        DecodedTagValidationFailure instance =
                new DecodedTagValidationFailure("TEST_TAG", FailureType.UnknownTag, "TEST_REASON");
        assertEquals(FailureType.UnknownTag, instance.getFailureType());
        instance =
                new DecodedTagValidationFailure(
                        "TEST_TAG", FailureType.MandatoryFieldMissing, "TEST_REASON");
        assertEquals(FailureType.MandatoryFieldMissing, instance.getFailureType());
    }

    @Test
    public void testGetFailureReason() throws Exception {
        final DecodedTagValidationFailure instance =
                new DecodedTagValidationFailure("TEST_TAG", FailureType.UnknownTag, "TEST_REASON");
        assertEquals("TEST_REASON", instance.getFailureReason());
    }
}
