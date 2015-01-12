/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.data;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.regex.Pattern;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodeResult;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

/**
 * Unit test
 *
 * @author brightSPARK Labs
 */
public class DecodedAsnDataDefaultTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** default instance to test */
    private static DecodedAsnData instance;

    /** empty instance to test */
    private static DecodedAsnData emptyInstance;

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @BeforeClass
    public static void setUpBeforeClass()
    {
        /** data to construct asnData from */
        final ImmutableMap<String, byte[]> tagsToData = ImmutableMap.<String, byte[]>builder()
                .put("/1/0/1", "/1/0/1".getBytes(Charsets.UTF_8))
                .put("/2/0/0", "/2/0/0".getBytes(Charsets.UTF_8))
                .put("/2/1/1", "/2/1/1".getBytes(Charsets.UTF_8))
                .put("/2/2/1", "/2/2/1".getBytes(Charsets.UTF_8))
                .put("/3/0/1", "/3/0/1".getBytes(Charsets.UTF_8))
                .put("/2/2/99", "/2/2/99".getBytes(Charsets.UTF_8))
                .put("/99/1/1", "/99/1/1".getBytes(Charsets.UTF_8))
                .build();

        /** asnData to construct instance from */
        final AsnData asnData = new AsnDataDefault(tagsToData);

        /** asnSchema to construct instance from */
        final AsnSchema asnSchema = mock(AsnSchema.class);
        when(asnSchema.getDecodedTag("/1/0/1", "Document")).thenReturn(DecodeResult.create(true,
                "/Document/Header/Published/Date"));
        when(asnSchema.getDecodedTag("/2/0/0", "Document")).thenReturn(DecodeResult.create(true,
                "/Document/Body/LastModified/Date"));
        when(asnSchema.getDecodedTag("/2/1/1", "Document")).thenReturn(DecodeResult.create(true,
                "/Document/Body/Prefix/Text"));
        when(asnSchema.getDecodedTag("/2/2/1", "Document")).thenReturn(DecodeResult.create(true,
                "/Document/Body/Content/Text"));
        when(asnSchema.getDecodedTag("/3/0/1", "Document")).thenReturn(DecodeResult.create(true,
                "/Document/Footer/Author/FirstName"));
        when(asnSchema.getDecodedTag("/2/2/99", "Document")).thenReturn(DecodeResult.create(false,
                "/Document/Body/Content/99"));
        when(asnSchema.getDecodedTag("/99/1/1", "Document")).thenReturn(DecodeResult.create(false, "/Document/99/1/1"));

        final NonEmptyByteArrayMatcher nonEmptyByteArrayMatcher = new NonEmptyByteArrayMatcher();
        when(asnSchema.getPrintableString(anyString(), any(byte[].class))).thenReturn("");
        when(asnSchema.getPrintableString(anyString(), argThat(nonEmptyByteArrayMatcher))).thenReturn("printableString");
        when(asnSchema.getDecodedObject(anyString(), any(byte[].class))).thenReturn("");
        when(asnSchema.getDecodedObject(anyString(), argThat(nonEmptyByteArrayMatcher))).thenReturn("decodedObject");

        instance = new DecodedAsnDataDefault(asnData, asnSchema, "Document");

        /** asnData to construct emptyInstance from */
        final AsnData emptyAsnData = new AsnDataDefault(Maps.<String, byte[]>newHashMap());
        emptyInstance = new DecodedAsnDataDefault(emptyAsnData, asnSchema, "Document");
    }

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

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecodedAsnDataDefault() throws Exception
    {
        final AsnData mockData = mock(AsnData.class);
        final AsnSchema mockSchema = mock(AsnSchema.class);

        try
        {
            new DecodedAsnDataDefault(null, null, null);
            fail("NullPointerException not thrown");
        }
        catch (NullPointerException ex)
        {
        }
        try
        {
            new DecodedAsnDataDefault(null, null, "Test");
            fail("NullPointerException not thrown");
        }
        catch (NullPointerException ex)
        {
        }
        try
        {
            new DecodedAsnDataDefault(null, mockSchema, null);
            fail("NullPointerException not thrown");
        }
        catch (NullPointerException ex)
        {
        }
        try
        {
            new DecodedAsnDataDefault(null, mockSchema, "Test");
            fail("NullPointerException not thrown");
        }
        catch (NullPointerException ex)
        {
        }
        try
        {
            new DecodedAsnDataDefault(mockData, null, null);
            fail("NullPointerException not thrown");
        }
        catch (NullPointerException ex)
        {
        }
        try
        {
            new DecodedAsnDataDefault(mockData, null, "Test");
            fail("NullPointerException not thrown");
        }
        catch (NullPointerException ex)
        {
        }
        try
        {
            new DecodedAsnDataDefault(mockData, mockSchema, null);
            fail("NullPointerException not thrown");
        }
        catch (NullPointerException ex)
        {
        }
        try
        {
            new DecodedAsnDataDefault(mockData, mockSchema, "");
            fail("IllegalArgumentException not thrown");
        }
        catch (IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testGetTags() throws Exception
    {
        ImmutableSet<String> tags = instance.getTags();
        assertEquals(tags.size(), 5);
        assertTrue(tags.contains("/Document/Header/Published/Date"));
        assertTrue(tags.contains("/Document/Body/LastModified/Date"));
        assertTrue(tags.contains("/Document/Body/Prefix/Text"));
        assertTrue(tags.contains("/Document/Body/Content/Text"));
        assertTrue(tags.contains("/Document/Footer/Author/FirstName"));

        tags = emptyInstance.getTags();
        assertEquals(tags.size(), 0);
    }

    @Test
    public void testGetUnmappedTags() throws Exception
    {
        ImmutableSet<String> tags = instance.getUnmappedTags();
        assertEquals(tags.size(), 2);
        assertTrue(tags.contains("/Document/Body/Content/99"));
        assertTrue(tags.contains("/Document/99/1/1"));

        tags = emptyInstance.getUnmappedTags();
        assertEquals(tags.size(), 0);
    }

    @Test
    public void testContains() throws Exception
    {
        assertTrue(instance.contains("/Document/Header/Published/Date"));
        assertTrue(instance.contains("/Document/Body/LastModified/Date"));
        assertTrue(instance.contains("/Document/Body/Prefix/Text"));
        assertTrue(instance.contains("/Document/Body/Content/Text"));
        assertTrue(instance.contains("/Document/Footer/Author/FirstName"));

        // test unmapped tags
        assertTrue(instance.contains("/Document/Body/Content/99"));
        assertTrue(instance.contains("/Document/99/1/1"));

        // test raw tags
        assertFalse(instance.contains("/2/2/99"));
        assertFalse(instance.contains("/99/1/1"));

        // test unknown tags
        assertFalse(instance.contains(""));
        assertFalse(instance.contains("/Document/0/0/0"));

        assertFalse(emptyInstance.contains(""));
        assertFalse(emptyInstance.contains("/Document/0/0/0"));
        assertFalse(emptyInstance.contains("/Document/Header/Published/Date"));
    }

    @Test
    public void testGetBytes() throws Exception
    {
        assertArrayEquals("/1/0/1".getBytes(Charsets.UTF_8), instance.getBytes("/Document/Header/Published/Date"));
        assertArrayEquals("/2/0/0".getBytes(Charsets.UTF_8), instance.getBytes("/Document/Body/LastModified/Date"));
        assertArrayEquals("/2/1/1".getBytes(Charsets.UTF_8), instance.getBytes("/Document/Body/Prefix/Text"));
        assertArrayEquals("/2/2/1".getBytes(Charsets.UTF_8), instance.getBytes("/Document/Body/Content/Text"));
        assertArrayEquals("/3/0/1".getBytes(Charsets.UTF_8), instance.getBytes("/Document/Footer/Author/FirstName"));

        // test unmapped tags
        assertArrayEquals("/2/2/99".getBytes(Charsets.UTF_8), instance.getBytes("/Document/Body/Content/99"));
        assertArrayEquals("/99/1/1".getBytes(Charsets.UTF_8), instance.getBytes("/Document/99/1/1"));

        // test raw tags
        assertArrayEquals("/2/2/99".getBytes(Charsets.UTF_8), instance.getBytes("/2/2/99"));
        assertArrayEquals("/99/1/1".getBytes(Charsets.UTF_8), instance.getBytes("/99/1/1"));

        // test unknown tags
        assertArrayEquals("".getBytes(Charsets.UTF_8), instance.getBytes(""));
        assertArrayEquals("".getBytes(Charsets.UTF_8), instance.getBytes("/Document/0/0/0"));
        assertArrayEquals("".getBytes(Charsets.UTF_8), instance.getBytes("/Document/2/2/99"));

        assertArrayEquals("".getBytes(Charsets.UTF_8), emptyInstance.getBytes(""));
        assertArrayEquals("".getBytes(Charsets.UTF_8), emptyInstance.getBytes("/Document/0/0/0"));
        assertArrayEquals("".getBytes(Charsets.UTF_8), emptyInstance.getBytes("/Document/Header/Published/Date"));
    }

    @Test
    public void testGetBytesMatching() throws Exception
    {
        Pattern regex = Pattern.compile("/Document/Body/.+");
        ImmutableMap<String, byte[]> result = instance.getBytesMatching(regex);
        assertEquals(4, result.size());
        assertArrayEquals("/2/0/0".getBytes(Charsets.UTF_8), result.get("/Document/Body/LastModified/Date"));
        assertArrayEquals("/2/1/1".getBytes(Charsets.UTF_8), result.get("/Document/Body/Prefix/Text"));
        assertArrayEquals("/2/2/1".getBytes(Charsets.UTF_8), result.get("/Document/Body/Content/Text"));
        assertArrayEquals("/2/2/99".getBytes(Charsets.UTF_8), result.get("/Document/Body/Content/99"));
        assertArrayEquals("".getBytes(Charsets.UTF_8), instance.getBytes("/Document/2/2/99"));
        result = emptyInstance.getBytesMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/Document/\\d+/1/\\d{1}");
        result = instance.getBytesMatching(regex);
        assertEquals(1, result.size());
        assertArrayEquals("/99/1/1".getBytes(Charsets.UTF_8), result.get("/Document/99/1/1"));
        result = emptyInstance.getBytesMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/0/.+");
        result = instance.getBytesMatching(regex);
        assertEquals(0, result.size());
        result = emptyInstance.getBytesMatching(regex);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetHexString() throws Exception
    {
        assertEquals("0x2F312F302F31", instance.getHexString("/Document/Header/Published/Date"));
        assertEquals("0x2F322F302F30", instance.getHexString("/Document/Body/LastModified/Date"));
        assertEquals("0x2F322F312F31", instance.getHexString("/Document/Body/Prefix/Text"));
        assertEquals("0x2F322F322F31", instance.getHexString("/Document/Body/Content/Text"));
        assertEquals("0x2F332F302F31", instance.getHexString("/Document/Footer/Author/FirstName"));

        // test unmapped tags
        assertEquals("0x2F322F322F3939", instance.getHexString("/Document/Body/Content/99"));
        assertEquals("0x2F39392F312F31", instance.getHexString("/Document/99/1/1"));

        // test raw tags
        assertEquals("0x2F322F322F3939", instance.getHexString("/2/2/99"));
        assertEquals("0x2F39392F312F31", instance.getHexString("/99/1/1"));

        // test unknown tags
        assertEquals("0x", instance.getHexString(""));
        assertEquals("0x", instance.getHexString("/0/0/0"));
        assertEquals("0x", instance.getHexString("/Document/2/2/99"));

        assertEquals("0x", emptyInstance.getHexString(""));
        assertEquals("0x", emptyInstance.getHexString("/Document/0/0/0"));
        assertEquals("0x", emptyInstance.getHexString("/Document/Header/Published/Date"));
    }

    @Test
    public void testGetHexStringsMatching() throws Exception
    {
        Pattern regex = Pattern.compile("/Document/Bod[x-z]/.+");
        ImmutableMap<String, String> result = instance.getHexStringsMatching(regex);
        assertEquals(4, result.size());
        assertEquals("0x2F322F302F30", result.get("/Document/Body/LastModified/Date"));
        assertEquals("0x2F322F312F31", result.get("/Document/Body/Prefix/Text"));
        assertEquals("0x2F322F322F31", result.get("/Document/Body/Content/Text"));
        assertEquals("0x2F322F322F3939", result.get("/Document/Body/Content/99"));
        result = emptyInstance.getHexStringsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/Document/\\d{2,4}/\\d/1");
        result = instance.getHexStringsMatching(regex);
        assertEquals(1, result.size());
        assertEquals("0x2F39392F312F31", result.get("/Document/99/1/1"));
        result = emptyInstance.getHexStringsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/Document/Header/A.*");
        result = instance.getHexStringsMatching(regex);
        assertEquals(0, result.size());
        result = emptyInstance.getHexStringsMatching(regex);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetPrintableString() throws Exception
    {
        assertEquals("printableString", instance.getPrintableString("/Document/Header/Published/Date"));
        assertEquals("printableString", instance.getPrintableString("/Document/Body/LastModified/Date"));
        assertEquals("printableString", instance.getPrintableString("/Document/Body/Prefix/Text"));
        assertEquals("printableString", instance.getPrintableString("/Document/Body/Content/Text"));
        assertEquals("printableString", instance.getPrintableString("/Document/Footer/Author/FirstName"));

        // test unmapped tags
        assertEquals("printableString", instance.getPrintableString("/Document/Body/Content/99"));
        assertEquals("printableString", instance.getPrintableString("/Document/99/1/1"));

        // test raw tags
        assertEquals("printableString", instance.getPrintableString("/2/2/99"));
        assertEquals("printableString", instance.getPrintableString("/99/1/1"));

        // test unknown tags
        assertEquals("", instance.getPrintableString(""));
        assertEquals("", instance.getPrintableString("/0/0/0"));
        assertEquals("", instance.getPrintableString("/Document/2/2/99"));

        assertEquals("", emptyInstance.getPrintableString(""));
        assertEquals("", emptyInstance.getPrintableString("/0/0/0"));
        assertEquals("", emptyInstance.getPrintableString("/Document/Header/Published/Date"));
    }

    @Test
    public void testGetPrintableStringsMatching() throws Exception
    {
        Pattern regex = Pattern.compile(".+Text");
        ImmutableMap<String, String> result = instance.getPrintableStringsMatching(regex);
        assertEquals(2, result.size());
        assertEquals("printableString", result.get("/Document/Body/Prefix/Text"));
        assertEquals("printableString", result.get("/Document/Body/Content/Text"));
        result = emptyInstance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/Document.+/\\d{1}");
        result = instance.getPrintableStringsMatching(regex);
        assertEquals(1, result.size());
        assertEquals("printableString", result.get("/Document/99/1/1"));
        result = emptyInstance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile(".*/A[^/]+");
        result = instance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());
        result = emptyInstance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetDecodedObject() throws Exception
    {
        assertEquals("decodedObject", instance.getDecodedObject("/Document/Header/Published/Date"));
        assertEquals("decodedObject", instance.getDecodedObject("/Document/Body/LastModified/Date"));
        assertEquals("decodedObject", instance.getDecodedObject("/Document/Body/Prefix/Text"));
        assertEquals("decodedObject", instance.getDecodedObject("/Document/Body/Content/Text"));
        assertEquals("decodedObject", instance.getDecodedObject("/Document/Footer/Author/FirstName"));

        // test unmapped tags
        assertEquals("decodedObject", instance.getDecodedObject("/Document/Body/Content/99"));
        assertEquals("decodedObject", instance.getDecodedObject("/Document/99/1/1"));

        // test raw tags
        assertEquals("decodedObject", instance.getDecodedObject("/2/2/99"));
        assertEquals("decodedObject", instance.getDecodedObject("/99/1/1"));

        // test unknown tags
        assertEquals("", instance.getDecodedObject(""));
        assertEquals("", instance.getDecodedObject("/Document/0/0/0"));

        assertEquals("", emptyInstance.getDecodedObject(""));
        assertEquals("", emptyInstance.getDecodedObject("/Document/0/0/0"));
        assertEquals("", emptyInstance.getDecodedObject("/Document/Header/Published/Date"));
    }

    @Test
    public void testGetDecodedObjectsMatching() throws Exception
    {
        Pattern regex = Pattern.compile(".+dy.+");
        ImmutableMap<String, Object> result = instance.getDecodedObjectsMatching(regex);
        assertEquals(4, result.size());
        assertEquals("decodedObject", result.get("/Document/Body/LastModified/Date"));
        assertEquals("decodedObject", result.get("/Document/Body/Prefix/Text"));
        assertEquals("decodedObject", result.get("/Document/Body/Content/Text"));
        assertEquals("decodedObject", result.get("/Document/Body/Content/99"));
        result = emptyInstance.getDecodedObjectsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/Document/9[0-9]/\\d+/\\d{1}");
        result = instance.getDecodedObjectsMatching(regex);
        assertEquals(1, result.size());
        assertEquals("decodedObject", result.get("/Document/99/1/1"));
        result = emptyInstance.getDecodedObjectsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile(".*/A[^/]+");
        result = instance.getDecodedObjectsMatching(regex);
        assertEquals(0, result.size());
        result = emptyInstance.getDecodedObjectsMatching(regex);
        assertEquals(0, result.size());
    }
}
