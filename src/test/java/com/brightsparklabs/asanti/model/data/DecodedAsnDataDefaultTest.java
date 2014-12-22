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
        when(asnSchema.getDecodedTag("/1/0/1")).thenReturn("/Header/Published/Date");
        when(asnSchema.getDecodedTag("/2/0/0")).thenReturn("/Body/LastModified/Date");
        when(asnSchema.getDecodedTag("/2/1/1")).thenReturn("/Body/Prefix/Text");
        when(asnSchema.getDecodedTag("/2/2/1")).thenReturn("/Body/Content/Text");
        when(asnSchema.getDecodedTag("/3/0/1")).thenReturn("/Footer/Author/FirstName");
        when(asnSchema.getDecodedTag("/2/2/99")).thenReturn("");
        when(asnSchema.getDecodedTag("/99/1/1")).thenReturn("");

        final NonEmptyByteArrayMatcher nonEmptyByteArrayMatcher = new NonEmptyByteArrayMatcher();
        when(asnSchema.getPrintableString(anyString(), any(byte[].class))).thenReturn("");
        when(asnSchema.getPrintableString(anyString(), argThat(nonEmptyByteArrayMatcher))).thenReturn("printableString");
        when(asnSchema.getDecodedObject(anyString(), any(byte[].class))).thenReturn("");
        when(asnSchema.getDecodedObject(anyString(), argThat(nonEmptyByteArrayMatcher))).thenReturn("decodedObject");

        instance = new DecodedAsnDataDefault(asnData, asnSchema);

        /** asnData to construct emptyInstance from */
        final AsnData emptyAsnData = new AsnDataDefault(Maps.<String, byte[]>newHashMap());
        emptyInstance = new DecodedAsnDataDefault(emptyAsnData, asnSchema);
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
        try
        {
            new DecodedAsnDataDefault(null, null);
            fail("NullPointerException not thrown");
        }
        catch (NullPointerException ex)
        {
        }
        try
        {
            AsnData mockData = mock(AsnData.class);
            new DecodedAsnDataDefault(mockData, null);
            fail("NullPointerException not thrown");
        }
        catch (NullPointerException ex)
        {
        }
        try
        {
            final AsnSchema mockSchema = mock(AsnSchema.class);
            new DecodedAsnDataDefault(null, mockSchema);
            fail("NullPointerException not thrown");
        }
        catch (NullPointerException ex)
        {
        }
    }

    @Test
    public void testGetTags() throws Exception
    {
        ImmutableSet<String> tags = instance.getTags();
        assertEquals(tags.size(), 5);
        assertTrue(tags.contains("/Header/Published/Date"));
        assertTrue(tags.contains("/Body/LastModified/Date"));
        assertTrue(tags.contains("/Body/Prefix/Text"));
        assertTrue(tags.contains("/Body/Content/Text"));
        assertTrue(tags.contains("/Footer/Author/FirstName"));

        tags = emptyInstance.getTags();
        assertEquals(tags.size(), 0);
    }

    @Test
    public void testGetUnmappedTags() throws Exception
    {
        ImmutableSet<String> tags = instance.getUnmappedTags();
        assertEquals(tags.size(), 2);
        assertTrue(tags.contains("/2/2/99"));
        assertTrue(tags.contains("/99/1/1"));

        tags = emptyInstance.getUnmappedTags();
        assertEquals(tags.size(), 0);
    }

    @Test
    public void testContains() throws Exception
    {
        assertTrue(instance.contains("/Header/Published/Date"));
        assertTrue(instance.contains("/Body/LastModified/Date"));
        assertTrue(instance.contains("/Body/Prefix/Text"));
        assertTrue(instance.contains("/Body/Content/Text"));
        assertTrue(instance.contains("/Footer/Author/FirstName"));
        assertTrue(instance.contains("/2/2/99"));
        assertTrue(instance.contains("/99/1/1"));
        assertFalse(instance.contains(""));
        assertFalse(instance.contains("/0/0/0"));

        assertFalse(emptyInstance.contains(""));
        assertFalse(emptyInstance.contains("/0/0/0"));
        assertFalse(emptyInstance.contains("/Header/Published/Date"));
    }

    @Test
    public void testGetBytes() throws Exception
    {
        assertArrayEquals("/1/0/1".getBytes(Charsets.UTF_8), instance.getBytes("/Header/Published/Date"));
        assertArrayEquals("/2/0/0".getBytes(Charsets.UTF_8), instance.getBytes("/Body/LastModified/Date"));
        assertArrayEquals("/2/1/1".getBytes(Charsets.UTF_8), instance.getBytes("/Body/Prefix/Text"));
        assertArrayEquals("/2/2/1".getBytes(Charsets.UTF_8), instance.getBytes("/Body/Content/Text"));
        assertArrayEquals("/3/0/1".getBytes(Charsets.UTF_8), instance.getBytes("/Footer/Author/FirstName"));
        assertArrayEquals("/2/2/99".getBytes(Charsets.UTF_8), instance.getBytes("/2/2/99"));
        assertArrayEquals("/99/1/1".getBytes(Charsets.UTF_8), instance.getBytes("/99/1/1"));
        assertArrayEquals("".getBytes(Charsets.UTF_8), instance.getBytes(""));
        assertArrayEquals("".getBytes(Charsets.UTF_8), instance.getBytes("/0/0/0"));

        assertArrayEquals("".getBytes(Charsets.UTF_8), emptyInstance.getBytes(""));
        assertArrayEquals("".getBytes(Charsets.UTF_8), emptyInstance.getBytes("/0/0/0"));
        assertArrayEquals("".getBytes(Charsets.UTF_8), emptyInstance.getBytes("/Header/Published/Date"));
    }

    @Test
    public void testGetBytesMatching() throws Exception
    {
        Pattern regex = Pattern.compile("/Body/.+");
        ImmutableMap<String, byte[]> result = instance.getBytesMatching(regex);
        assertEquals(3, result.size());
        assertArrayEquals("/2/0/0".getBytes(Charsets.UTF_8), result.get("/Body/LastModified/Date"));
        assertArrayEquals("/2/1/1".getBytes(Charsets.UTF_8), result.get("/Body/Prefix/Text"));
        assertArrayEquals("/2/2/1".getBytes(Charsets.UTF_8), result.get("/Body/Content/Text"));
        result = emptyInstance.getBytesMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/\\d+/2/\\d{2}");
        result = instance.getBytesMatching(regex);
        assertEquals(1, result.size());
        assertArrayEquals("/2/2/99".getBytes(Charsets.UTF_8), result.get("/2/2/99"));
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
        assertEquals("0x2F312F302F31", instance.getHexString("/Header/Published/Date"));
        assertEquals("0x2F322F302F30", instance.getHexString("/Body/LastModified/Date"));
        assertEquals("0x2F322F312F31", instance.getHexString("/Body/Prefix/Text"));
        assertEquals("0x2F322F322F31", instance.getHexString("/Body/Content/Text"));
        assertEquals("0x2F332F302F31", instance.getHexString("/Footer/Author/FirstName"));
        assertEquals("0x2F322F322F3939", instance.getHexString("/2/2/99"));
        instance.getHexString("/99/1/1");
        assertEquals("0x2F39392F312F31", instance.getHexString("/99/1/1"));
        assertEquals("0x", instance.getHexString(""));
        assertEquals("0x", instance.getHexString("/0/0/0"));

        assertEquals("0x", emptyInstance.getHexString(""));
        assertEquals("0x", emptyInstance.getHexString("/0/0/0"));
        assertEquals("0x", emptyInstance.getHexString("/Header/Published/Date"));
    }

    @Test
    public void testGetHexStringsMatching() throws Exception
    {
        Pattern regex = Pattern.compile("/Bod[x-z]/.+");
        ImmutableMap<String, String> result = instance.getHexStringsMatching(regex);
        assertEquals(3, result.size());
        assertEquals("0x2F322F302F30", result.get("/Body/LastModified/Date"));
        assertEquals("0x2F322F312F31", result.get("/Body/Prefix/Text"));
        assertEquals("0x2F322F322F31", result.get("/Body/Content/Text"));
        result = emptyInstance.getHexStringsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/\\d{2,4}/\\d/1");
        result = instance.getHexStringsMatching(regex);
        assertEquals(1, result.size());
        assertEquals("0x2F39392F312F31", result.get("/99/1/1"));
        result = emptyInstance.getHexStringsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/Header/A.*");
        result = instance.getHexStringsMatching(regex);
        assertEquals(0, result.size());
        result = emptyInstance.getHexStringsMatching(regex);
        assertEquals(0, result.size());
    }

    @Test
    public void testGetPrintableString() throws Exception
    {
        assertEquals("printableString", instance.getPrintableString("/Header/Published/Date"));
        assertEquals("printableString", instance.getPrintableString("/Body/LastModified/Date"));
        assertEquals("printableString", instance.getPrintableString("/Body/Prefix/Text"));
        assertEquals("printableString", instance.getPrintableString("/Body/Content/Text"));
        assertEquals("printableString", instance.getPrintableString("/Footer/Author/FirstName"));
        assertEquals("printableString", instance.getPrintableString("/2/2/99"));
        assertEquals("printableString", instance.getPrintableString("/99/1/1"));
        assertEquals("", instance.getPrintableString(""));
        assertEquals("", instance.getPrintableString("/0/0/0"));

        assertEquals("", emptyInstance.getPrintableString(""));
        assertEquals("", emptyInstance.getPrintableString("/0/0/0"));
        assertEquals("", emptyInstance.getPrintableString("/Header/Published/Date"));
    }

    @Test
    public void testGetPrintableStringsMatching() throws Exception
    {
        Pattern regex = Pattern.compile(".+Text");
        ImmutableMap<String, String> result = instance.getPrintableStringsMatching(regex);
        assertEquals(2, result.size());
        assertEquals("printableString", result.get("/Body/Prefix/Text"));
        assertEquals("printableString", result.get("/Body/Content/Text"));
        result = emptyInstance.getPrintableStringsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile(".+/\\d{1}");
        result = instance.getPrintableStringsMatching(regex);
        assertEquals(1, result.size());
        assertEquals("printableString", result.get("/99/1/1"));
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
        assertEquals("decodedObject", instance.getDecodedObject("/Header/Published/Date"));
        assertEquals("decodedObject", instance.getDecodedObject("/Body/LastModified/Date"));
        assertEquals("decodedObject", instance.getDecodedObject("/Body/Prefix/Text"));
        assertEquals("decodedObject", instance.getDecodedObject("/Body/Content/Text"));
        assertEquals("decodedObject", instance.getDecodedObject("/Footer/Author/FirstName"));
        assertEquals("decodedObject", instance.getDecodedObject("/2/2/99"));
        assertEquals("decodedObject", instance.getDecodedObject("/99/1/1"));
        assertEquals("", instance.getDecodedObject(""));
        assertEquals("", instance.getDecodedObject("/0/0/0"));

        assertEquals("", emptyInstance.getDecodedObject(""));
        assertEquals("", emptyInstance.getDecodedObject("/0/0/0"));
        assertEquals("", emptyInstance.getDecodedObject("/Header/Published/Date"));
    }

    @Test
    public void testGetDecodedObjectsMatching() throws Exception
    {
        Pattern regex = Pattern.compile(".+dy.+");
        ImmutableMap<String, Object> result = instance.getDecodedObjectsMatching(regex);
        assertEquals(3, result.size());
        assertEquals("decodedObject", result.get("/Body/LastModified/Date"));
        assertEquals("decodedObject", result.get("/Body/Prefix/Text"));
        assertEquals("decodedObject", result.get("/Body/Content/Text"));
        result = emptyInstance.getDecodedObjectsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile("/9[0-9]/\\d+/\\d{1}");
        result = instance.getDecodedObjectsMatching(regex);
        assertEquals(1, result.size());
        assertEquals("decodedObject", result.get("/99/1/1"));
        result = emptyInstance.getDecodedObjectsMatching(regex);
        assertEquals(0, result.size());

        regex = Pattern.compile(".*/A[^/]+");
        result = instance.getDecodedObjectsMatching(regex);
        assertEquals(0, result.size());
        result = emptyInstance.getDecodedObjectsMatching(regex);
        assertEquals(0, result.size());
    }
}
