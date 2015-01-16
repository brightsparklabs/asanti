/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.data;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

/**
 * Unit test for {@link AsnDataDefault}
 *
 * @author brightSPARK Labs
 */
public class AsnDataDefaultTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** default data to construct instance from */
    final ImmutableMap<String, byte[]> tagsToData = ImmutableMap.<String, byte[]>builder()
            .put("/0/0/0", "/0/0/0".getBytes(Charsets.UTF_8))
            .put("/0/1/0", "/0/1/0".getBytes(Charsets.UTF_8))
            .put("/1/0/1", "/1/0/1".getBytes(Charsets.UTF_8))
            .put("/2/2/0", "/2/2/0".getBytes(Charsets.UTF_8))
            .put("/99/0", "/99/0".getBytes(Charsets.UTF_8))
            .build();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnDataDefault() throws Exception
    {
        try
        {
            new AsnDataDefault(null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
    }

    @Test
    public void testGetRawTags() throws Exception
    {
        AsnData instance = new AsnDataDefault(tagsToData);
        ImmutableSet<String> tags = instance.getRawTags();
        assertEquals(tags.size(), 5);
        assertTrue(tags.contains("/0/0/0"));
        assertTrue(tags.contains("/0/1/0"));
        assertTrue(tags.contains("/1/0/1"));
        assertTrue(tags.contains("/2/2/0"));
        assertTrue(tags.contains("/99/0"));

        instance = new AsnDataDefault(Maps.<String, byte[]>newHashMap());
        tags = instance.getRawTags();
        assertEquals(tags.size(), 0);
    }

    @Test
    public void testGetBytesString() throws Exception
    {
        AsnData instance = new AsnDataDefault(tagsToData);
        assertArrayEquals(instance.getBytes("/0/0/0"), "/0/0/0".getBytes(Charsets.UTF_8));
        assertArrayEquals(instance.getBytes("/0/1/0"), "/0/1/0".getBytes(Charsets.UTF_8));
        assertArrayEquals(instance.getBytes("/1/0/1"), "/1/0/1".getBytes(Charsets.UTF_8));
        assertArrayEquals(instance.getBytes("/2/2/0"), "/2/2/0".getBytes(Charsets.UTF_8));
        assertArrayEquals(instance.getBytes("/99/0"), "/99/0".getBytes(Charsets.UTF_8));
        assertArrayEquals(instance.getBytes("/fake/0"), "".getBytes(Charsets.UTF_8));

        instance = new AsnDataDefault(Maps.<String, byte[]>newHashMap());
        assertArrayEquals(instance.getBytes("/0/0/0"), "".getBytes(Charsets.UTF_8));
        assertArrayEquals(instance.getBytes("/0/1/0"), "".getBytes(Charsets.UTF_8));
        assertArrayEquals(instance.getBytes("/1/0/1"), "".getBytes(Charsets.UTF_8));
        assertArrayEquals(instance.getBytes("/2/2/0"), "".getBytes(Charsets.UTF_8));
        assertArrayEquals(instance.getBytes("/99/0"), "".getBytes(Charsets.UTF_8));
        assertArrayEquals(instance.getBytes("/fake/0"), "".getBytes(Charsets.UTF_8));
    }

    @Test
    public void testGetBytes() throws Exception
    {
        AsnData instance = new AsnDataDefault(tagsToData);
        ImmutableMap<String, byte[]> data = instance.getBytes();
        assertEquals(data.size(), 5);
        assertArrayEquals(data.get("/0/0/0"), "/0/0/0".getBytes(Charsets.UTF_8));
        assertArrayEquals(data.get("/0/1/0"), "/0/1/0".getBytes(Charsets.UTF_8));
        assertArrayEquals(data.get("/1/0/1"), "/1/0/1".getBytes(Charsets.UTF_8));
        assertArrayEquals(data.get("/2/2/0"), "/2/2/0".getBytes(Charsets.UTF_8));
        assertArrayEquals(data.get("/99/0"), "/99/0".getBytes(Charsets.UTF_8));

        instance = new AsnDataDefault(Maps.<String, byte[]>newHashMap());
        data = instance.getBytes();
        assertTrue(data.size() == 0);
    }
}
