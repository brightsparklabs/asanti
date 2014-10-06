/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Unit test
 *
 * @author brightSPARK Labs <enquire@brightsparklabs.com>
 */
public class DecodedAsnDataDefaultTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** data to construct default instance from */
    final ImmutableMap<String, byte[]> tagsToData = ImmutableMap.<String, byte[]> builder()
            .put("/Header/Published/Date", "/1/0/1".getBytes())
            .put("/Body/LastModified/Date", "/2/0/0".getBytes())
            .put("/Body/Prefix/Text", "/2/1/1".getBytes())
            .put("/Body/Content/Text", "/2/2/1".getBytes())
            .put("/Footer/Author/FirstName", "/3/0/1".getBytes())
            .build();

    /** data to construct default instance from */
    final ImmutableMap<String, byte[]> umappedTagsToData = ImmutableMap.<String, byte[]> builder()
            .put("/Body/Content/99", "/2/2/99".getBytes())
            .put("/99/1/1", "/99/1/1".getBytes())
            .build();

    /** default instance to test */
    final DecodedAsnData instance = new DecodedAsnDataDefault(tagsToData, umappedTagsToData);

    /** data to construct empty instance from */
    final ImmutableMap<String, byte[]> emptyMap = ImmutableMap.of();

    /** empty instance to test */
    final DecodedAsnData emptyInstance = new DecodedAsnDataDefault(emptyMap, emptyMap);

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
            new DecodedAsnDataDefault(emptyMap, null);
            fail("NullPointerException not thrown");
        }
        catch (NullPointerException ex)
        {
        }
        try
        {
            new DecodedAsnDataDefault(null, emptyMap);
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
        assertTrue(tags.contains("/Body/Content/99"));
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
        assertTrue(instance.contains("/Body/Content/99"));
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
        assertArrayEquals(instance.getBytes("/Header/Published/Date"), "/1/0/1".getBytes());
        assertArrayEquals(instance.getBytes("/Body/LastModified/Date"), "/2/0/0".getBytes());
        assertArrayEquals(instance.getBytes("/Body/Prefix/Text"), "/2/1/1".getBytes());
        assertArrayEquals(instance.getBytes("/Body/Content/Text"), "/2/2/1".getBytes());
        assertArrayEquals(instance.getBytes("/Footer/Author/FirstName"), "/3/0/1".getBytes());
        assertArrayEquals(instance.getBytes("/Body/Content/99"), "/2/2/99".getBytes());
        assertArrayEquals(instance.getBytes("/99/1/1"), "/99/1/1".getBytes());
        assertArrayEquals(instance.getBytes(""), "".getBytes());
        assertArrayEquals(instance.getBytes("/0/0/0"), "".getBytes());

        assertArrayEquals(emptyInstance.getBytes(""), "".getBytes());
        assertArrayEquals(emptyInstance.getBytes("/0/0/0"), "".getBytes());
        assertArrayEquals(emptyInstance.getBytes("/Header/Published/Date"), "".getBytes());
    }

    @Test
    public void testGetHexString() throws Exception
    {
        assertEquals(instance.getHexString("/Header/Published/Date"), "0x2F312F302F31");
        assertEquals(instance.getHexString("/Body/LastModified/Date"), "0x2F322F302F30");
        assertEquals(instance.getHexString("/Body/Prefix/Text"), "0x2F322F312F31");
        assertEquals(instance.getHexString("/Body/Content/Text"), "0x2F322F322F31");
        assertEquals(instance.getHexString("/Footer/Author/FirstName"), "0x2F332F302F31");
        assertEquals(instance.getHexString("/Body/Content/99"), "0x2F322F322F3939");
        assertEquals(instance.getHexString("/99/1/1"), "0x2F39392F312F31");
        assertEquals(instance.getHexString(""), "0x");
        assertEquals(instance.getHexString("/0/0/0"), "0x");

        assertEquals(emptyInstance.getHexString(""), "0x");
        assertEquals(emptyInstance.getHexString("/0/0/0"), "0x");
        assertEquals(emptyInstance.getHexString("/Header/Published/Date"), "0x");
    }

    @Test
    public void testGetPrintableString() throws Exception
    {
        assertEquals(instance.getPrintableString("/Header/Published/Date"), "/1/0/1");
        assertEquals(instance.getPrintableString("/Body/LastModified/Date"), "/2/0/0");
        assertEquals(instance.getPrintableString("/Body/Prefix/Text"), "/2/1/1");
        assertEquals(instance.getPrintableString("/Body/Content/Text"), "/2/2/1");
        assertEquals(instance.getPrintableString("/Footer/Author/FirstName"), "/3/0/1");
        assertEquals(instance.getPrintableString("/Body/Content/99"), "/2/2/99");
        assertEquals(instance.getPrintableString("/99/1/1"), "/99/1/1");
        assertEquals(instance.getPrintableString(""), "");
        assertEquals(instance.getPrintableString("/0/0/0"), "");

        assertEquals(emptyInstance.getPrintableString(""), "");
        assertEquals(emptyInstance.getPrintableString("/0/0/0"), "");
        assertEquals(emptyInstance.getPrintableString("/Header/Published/Date"), "");
    }

    @Test
    public void testGetDecodedObject() throws Exception
    {
        assertArrayEquals(instance.getBytes("/Header/Published/Date"), "/1/0/1".getBytes());
        assertArrayEquals(instance.getBytes("/Body/LastModified/Date"), "/2/0/0".getBytes());
        assertArrayEquals(instance.getBytes("/Body/Prefix/Text"), "/2/1/1".getBytes());
        assertArrayEquals(instance.getBytes("/Body/Content/Text"), "/2/2/1".getBytes());
        assertArrayEquals(instance.getBytes("/Footer/Author/FirstName"), "/3/0/1".getBytes());
        assertArrayEquals(instance.getBytes("/Body/Content/99"), "/2/2/99".getBytes());
        assertArrayEquals(instance.getBytes("/99/1/1"), "/99/1/1".getBytes());
        assertArrayEquals(instance.getBytes(""), "".getBytes());
        assertArrayEquals(instance.getBytes("/0/0/0"), "".getBytes());

        assertArrayEquals(emptyInstance.getBytes(""), "".getBytes());
        assertArrayEquals(emptyInstance.getBytes("/0/0/0"), "".getBytes());
        assertArrayEquals(emptyInstance.getBytes("/Header/Published/Date"), "".getBytes());
    }
}
