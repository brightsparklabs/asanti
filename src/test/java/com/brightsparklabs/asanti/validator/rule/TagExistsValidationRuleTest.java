/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.rule;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.brightsparklabs.assam.data.AsnData;
import com.brightsparklabs.assam.validator.FailureType;
import com.brightsparklabs.assam.validator.ValidationFailure;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

/**
 * Units tests for {@link com.brightsparklabs.asanti.validator.rule.TagExistsValidationRule}
 *
 * @author brightSPARK Labs
 */
public class TagExistsValidationRuleTest {

    @Test
    public void testValidate() throws Exception {
        // test valid
        final AsnData mockAsnDataGood = mock(AsnData.class);
        final String goodTag = "/Good/Tag";
        when(mockAsnDataGood.contains(anyString())).thenReturn(false);
        when(mockAsnDataGood.contains(eq(goodTag))).thenReturn(true);
        TagExistsValidationRule instance = new TagExistsValidationRule(goodTag);
        ImmutableSet<ValidationFailure> failures = instance.validate(goodTag, mockAsnDataGood);
        assertEquals(0, failures.size());

        final AsnData mockAsnDataBad = mock(AsnData.class);
        final String badTag = "/Bad/Tag";
        when(mockAsnDataBad.contains(anyString())).thenReturn(false);
        when(mockAsnDataBad.contains(eq(badTag))).thenReturn(true);
        failures = instance.validate(goodTag, mockAsnDataBad);
        assertEquals(1, failures.size());

        final ValidationFailure missingFailure = failures.asList().get(0);
        assertEquals(FailureType.CustomValidationFailed, missingFailure.getFailureType());
        assertEquals("No value found for tag", missingFailure.getFailureReason());
        assertEquals(goodTag, missingFailure.getFailureTag());
    }
}
