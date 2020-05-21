/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.rule;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.data.AsnData;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.ValidationFailure;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.regex.Pattern;
import org.junit.Test;

/**
 * Units tests for {@link com.brightsparklabs.asanti.validator.rule.AsPrintableAsciiValidationRule}
 *
 * @author brightSPARK Labs
 */
public class AsPrintableAsciiValidationRuleTest {

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidate() throws Exception {

        AsnData asnData = mock(AsnData.class);

        ImmutableMap<String, Object> dataGood =
                ImmutableMap.<String, Object>builder()
                        .put("/foo/bar", new byte[] {0x20, 0x65})
                        .put("/foo/far", new byte[] {0x30, 0x31, 32, 33})
                        .build();

        ImmutableMap<String, Object> dataBad =
                ImmutableMap.<String, Object>builder()
                        .put("/foo/bad", new byte[] {0x02, 0x65})
                        .put("/foo/bar", new byte[] {0x30, 0x31, 32, 33})
                        .put("/foo/string", "some string")
                        .build();

        // Note that it doesn't matter, it is only triggering the type of data to return, not
        // actually Matcher'ing
        Pattern patternGood = Pattern.compile("Good");
        Pattern patternBad = Pattern.compile("Bad");

        when(asnData.getDecodedObjectsMatching(eq(patternGood))).thenReturn(dataGood);
        when(asnData.getDecodedObjectsMatching(eq(patternBad))).thenReturn(dataBad);

        final AsPrintableAsciiValidationRule instanceGood =
                new AsPrintableAsciiValidationRule(patternGood);

        final ImmutableSet<ValidationFailure> failuresGood =
                instanceGood.validate("any tag", asnData);
        assertEquals(0, failuresGood.size());

        final AsPrintableAsciiValidationRule instanceBad =
                new AsPrintableAsciiValidationRule(patternBad);
        final ImmutableSet<ValidationFailure> failuresBad =
                instanceBad.validate("any tag", asnData);
        assertEquals(1, failuresBad.size());
        final ValidationFailure missingFailure = failuresBad.asList().get(0);
        assertEquals(FailureType.CustomValidationFailed, missingFailure.getFailureType());
        assertEquals(
                "Field must contain only printable ASCII characters (0x20-0x7E)",
                missingFailure.getFailureReason());
        assertEquals("/foo/bad", missingFailure.getFailureTag());
    }
}
