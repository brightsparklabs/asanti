/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.builtin;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypeVisitor;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypeWithNamedTags;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Optional;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/** Test cases for the EnumeratedValidator class */
public class EnumeratedValidatorTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final EnumeratedValidator instance = EnumeratedValidator.getInstance();

    @Test
    public void testGetInstance() throws Exception {

        // check that getInstance returns the same object (ie is singleton)
        assertEquals(instance, EnumeratedValidator.getInstance());
    }

    @Test
    public void testValidate() throws Exception {
        ImmutableSet<ByteValidationFailure> result = instance.validate(null);
        assertEquals(1, result.size());
        assertEquals(FailureType.DataMissing, result.asList().get(0).getFailureType());

        byte[] bytes = {};
        result = instance.validate(bytes);
        assertEquals(1, result.size());
        assertEquals(
                FailureType.DataIncorrectlyFormatted, result.iterator().next().getFailureType());
    }

    @Test
    public void testValidateNonNullBytes() throws Exception {
        byte[] bytes = {1};
        ImmutableSet<ByteValidationFailure> result = instance.validate(bytes);
        assertEquals(0, result.size());
    }

    @Test
    public void testValidateViaTag() throws Exception {
        final AsnSchemaTypeWithNamedTags type = mock(AsnSchemaTypeWithNamedTags.class);
        when(type.getConstraints()).thenReturn(ImmutableSet.of());

        // We need to go to this length so that we can test the "internal" visitor
        when(type.accept(any(AsnSchemaTypeVisitor.class)))
                .thenAnswer(
                        new Answer<String>() {

                            @Override
                            public String answer(InvocationOnMock invocation) throws Throwable {
                                Object[] args = invocation.getArguments();
                                AsnSchemaTypeVisitor arg = (AsnSchemaTypeVisitor) args[0];

                                return (String) arg.visit(type);
                            }
                        });
        ImmutableMap<String, AsnSchemaNamedTag> namedTags =
                ImmutableMap.of("1", new AsnSchemaNamedTag("enumValue", "1"));
        when(type.getTagsToNamedValues()).thenReturn(namedTags);

        String tag = "/Foo";
        AsantiAsnData data = mock(AsantiAsnData.class);
        when(data.getType(eq(tag))).thenReturn(Optional.of(type));
        when(data.getBytes(eq(tag))).thenReturn(Optional.of(new byte[] {1}));

        assertTrue(instance.validateAndDecode(tag, data).wasSuccessful());

        AsantiAsnData dataBad = mock(AsantiAsnData.class);
        when(dataBad.getType(eq(tag))).thenReturn(Optional.of(type));
        when(dataBad.getBytes(eq(tag))).thenReturn(Optional.of(new byte[] {2}));

        final OperationResult<String, ImmutableSet<DecodedTagValidationFailure>> result =
                instance.validateAndDecode(tag, dataBad);

        assertFalse(result.wasSuccessful());

        assertEquals(0, instance.validate(tag, data).size());
        assertEquals(1, instance.validate(tag, dataBad).size());
        assertEquals(
                FailureType.DataIncorrectlyFormatted,
                instance.validate(tag, dataBad).asList().get(0).getFailureType());
    }

    @Test
    public void testValidateDataMissing() throws Exception {
        final AsnSchemaTypeWithNamedTags type = mock(AsnSchemaTypeWithNamedTags.class);
        when(type.getConstraints()).thenReturn(ImmutableSet.of());

        String tag = "/Foo";
        AsantiAsnData data = mock(AsantiAsnData.class);
        when(data.getType(eq(tag))).thenReturn(Optional.of(type));
        // testing for the enum not having any bytes.
        when(data.getBytes(eq(tag))).thenReturn(Optional.empty());

        final OperationResult<String, ImmutableSet<DecodedTagValidationFailure>>
                result = instance.validateAndDecode(tag, data);

        assertFalse(result.wasSuccessful());

        final ImmutableSet<DecodedTagValidationFailure> validate = instance.validate(tag, data);
        assertEquals(1, validate.size());
        assertEquals(
                FailureType.DataMissing,
                validate.asList().get(0).getFailureType());
    }
}
