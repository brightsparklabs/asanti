package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypeVisitor;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypeWithNamedTags;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test cases for the EnumeratedDecoder class
 */
public class EnumeratedDecoderTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final EnumeratedDecoder instance = EnumeratedDecoder.getInstance();

    @Test
    public void testGetInstance() throws Exception
    {
        // check that getInstance returns the same object (ie is singleton)
        assertEquals(instance, EnumeratedDecoder.getInstance());
    }

    @Test
    public void testDecode() throws Exception
    {
        byte [] bytes = { 0 };
        assertEquals("0", instance.decode(bytes));
        assertEquals("0", instance.decodeAsString(bytes));

        bytes = new byte[] { 10 };
        assertEquals("10", instance.decode(bytes));
        assertEquals("10", instance.decodeAsString(bytes));
    }

    @Test
    public void testDecode1() throws Exception
    {
        final AsnSchemaTypeWithNamedTags type = mock(AsnSchemaTypeWithNamedTags.class);
        when(type.getConstraints()).thenReturn(ImmutableSet.<AsnSchemaConstraint>of());

        // We need to go to this length so that we can test the "internal" visitor
        when(type.accept(any(AsnSchemaTypeVisitor.class))).thenAnswer(new Answer<String>()
        {

            @Override
            public String answer(InvocationOnMock invocation) throws Throwable
            {
                Object[] args = invocation.getArguments();
                AsnSchemaTypeVisitor arg = (AsnSchemaTypeVisitor) args[0];

                return (String)arg.visit(type);
            }
        });
        ImmutableMap<String, AsnSchemaNamedTag> namedTags = ImmutableMap.of("1", new AsnSchemaNamedTag("enumValue", "1"));
        when(type.getTagsToNamedValues()).thenReturn(namedTags);

        String tag = "/Foo";
        DecodedAsnData data = mock(DecodedAsnData.class);
        when(data.getType(eq(tag))).thenReturn(Optional.<AsnSchemaType>of(type));
        when(data.getBytes(eq(tag))).thenReturn(Optional.of(new byte [] { 1 }));

        assertEquals("enumValue", instance.decode(tag, data));
        assertEquals("enumValue", instance.decodeAsString(tag, data));

        DecodedAsnData dataBad = mock(DecodedAsnData.class);
        when(dataBad.getType(eq(tag))).thenReturn(Optional.<AsnSchemaType>of(type));
        when(dataBad.getBytes(eq(tag))).thenReturn(Optional.of(new byte [] { 2 }));

        try
        {
            instance.decode(tag, dataBad);
            fail("Should have thrown DecodeException");
        }
        catch (DecodeException e)
        {

        }
    }
}