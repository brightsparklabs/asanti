package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypeVisitor;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypeWithNamedTags;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.brightsparklabs.assam.exception.DecodeException;
import java.util.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
        byte[] bytes = { 0 };
        assertEquals("0", instance.decode(bytes));
        assertEquals("0", instance.decodeAsString(bytes));

        bytes = new byte[] { 10 };
        assertEquals("10", instance.decode(bytes));
        assertEquals("10", instance.decodeAsString(bytes));

        bytes = new byte[0];
        try
        {
            instance.decode(bytes);
            fail("Should have thrown DecodeExceptions");
        }
        catch (DecodeException e)
        {
        }
        try
        {
            instance.decodeAsString(bytes);
            fail("Should have thrown DecodeExceptions");
        }
        catch (DecodeException e)
        {
        }
    }

    @Test
    public void testDecode1() throws Exception
    {
        final AsnSchemaTypeWithNamedTags type = mock(AsnSchemaTypeWithNamedTags.class);
        when(type.getConstraints()).thenReturn(ImmutableSet.of());

        // We need to go to this length so that we can test the "internal" visitor
        when(type.accept(any(AsnSchemaTypeVisitor.class))).thenAnswer(p ->
        {
            Object[] args = p.getArguments();
            AsnSchemaTypeVisitor arg = (AsnSchemaTypeVisitor) args[0];

            return (String) arg.visit(type);
        });
        ImmutableMap<String, AsnSchemaNamedTag> namedTags = ImmutableMap.of("1",
                new AsnSchemaNamedTag("enumValue", "1"));
        when(type.getTagsToNamedValues()).thenReturn(namedTags);

        String tag = "/Foo";
        AsantiAsnData data = mock(AsantiAsnData.class);
        when(data.getType(eq(tag))).thenReturn(Optional.of(type));
        when(data.getBytes(eq(tag))).thenReturn(Optional.of(new byte[] { 1 }));

        assertEquals("enumValue", instance.decode(tag, data));
        assertEquals("enumValue", instance.decodeAsString(tag, data));

        AsantiAsnData dataBad = mock(AsantiAsnData.class);
        when(dataBad.getType(eq(tag))).thenReturn(Optional.of(type));
        when(dataBad.getBytes(eq(tag))).thenReturn(Optional.of(new byte[] { 2 }));

        try
        {
            instance.decode(tag, dataBad);
            fail("Should have thrown DecodeExceptions");
        }
        catch (DecodeException e)
        {

        }

        final String tagEmpty = "emptyBytes";
        when(data.getType(eq(tagEmpty))).thenReturn(Optional.of(type));
        when(data.getBytes(eq(tagEmpty))).thenReturn(Optional.of(new byte[0]));
        try
        {
            instance.decode(tagEmpty, data);
            fail("Should have thrown DecodeExceptions");
        }
        catch (DecodeException e)
        {
        }

        // null
        try
        {
            instance.decode(null, data);
            fail("Should have thrown NullPointerException");
        }
        catch (NullPointerException e)
        {
        }
        try
        {
            instance.decode(tagEmpty, null);
            fail("Should have thrown NullPointerException");
        }
        catch (NullPointerException e)
        {
        }
        try
        {
            instance.decodeAsString(null, data);
            fail("Should have thrown NullPointerException");
        }
        catch (NullPointerException e)
        {
        }
        try
        {
            instance.decodeAsString(tagEmpty, null);
            fail("Should have thrown NullPointerException");
        }
        catch (NullPointerException e)
        {
        }
    }
}
