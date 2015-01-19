/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentMatcher;

import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodeResult;

/**
 * Utility class for obtaining mocked instances of {@link AsnSchema} which
 * conform to the test ASN.1 schema defined in the {@linkplain README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockAsnSchema
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static AsnSchema instance;

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance
     */
    public static AsnSchema getInstance()
    {
        if (instance != null) { return instance; }

        instance = mock(AsnSchema.class);
        when(instance.getDecodedTag("/1/0/1", "Document")).thenReturn(DecodeResult.create(true, "/Document/header/published/date"));
        when(instance.getDecodedTag("/2/0/0", "Document")).thenReturn(DecodeResult.create(true, "/Document/body/lastModified/date"));
        when(instance.getDecodedTag("/2/1/1", "Document")).thenReturn(DecodeResult.create(true, "/Document/body/prefix/text"));
        when(instance.getDecodedTag("/2/2/1", "Document")).thenReturn(DecodeResult.create(true, "/Document/body/content/text"));
        when(instance.getDecodedTag("/3/0/1", "Document")).thenReturn(DecodeResult.create(true, "/Document/footer/author/firstName"));
        when(instance.getDecodedTag("/2/2/99", "Document")).thenReturn(DecodeResult.create(false, "/Document/body/content/99"));
        when(instance.getDecodedTag("/99/1/1", "Document")).thenReturn(DecodeResult.create(false, "/Document/99/1/1"));

        final NonEmptyByteArrayMatcher nonEmptyByteArrayMatcher = new NonEmptyByteArrayMatcher();
        when(instance.getPrintableString(anyString(), any(byte[].class))).thenReturn("");
        when(instance.getPrintableString(anyString(), argThat(nonEmptyByteArrayMatcher))).thenReturn("printableString");
        when(instance.getDecodedObject(anyString(), any(byte[].class))).thenReturn("");
        when(instance.getDecodedObject(anyString(), argThat(nonEmptyByteArrayMatcher))).thenReturn("decodedObject");

        return instance;
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: NonEmptyByteArrayMatcher
    // -------------------------------------------------------------------------

    /**
     * Matches non-empty byte[] arguments
     */
    private static class NonEmptyByteArrayMatcher extends ArgumentMatcher<byte[]>
    {
        @Override
        public boolean matches(Object item)
        {
            return ((byte[]) item).length > 0;
        }
    };
}
