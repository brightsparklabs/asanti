/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

/**
 * Unit test
 *
 * @author brightSPARK Labs <enquire@brightsparklabs.com>
 */
public class AsnDataDefaultTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** default data to construct instance from */
    final ImmutableMap<String, byte[]> tagsToData = ImmutableMap.<String, byte[]> builder()
            .put("/0/0/0", "/0/0/0".getBytes())
            .put("/0/1/0", "/0/1/0".getBytes())
            .put("/1/0/1", "/1/0/1".getBytes())
            .put("/2/2/0", "/2/2/0".getBytes())
            .put("/99/0", "/99/0".getBytes())
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
        catch (NullPointerException ex)
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

        instance = new AsnDataDefault(Maps.<String, byte[]> newHashMap());
        tags = instance.getRawTags();
        assertEquals(tags.size(), 0);
    }

    @Test
    public void testGetDataString() throws Exception
    {
        AsnData instance = new AsnDataDefault(tagsToData);
        assertArrayEquals(instance.getData("/0/0/0"), "/0/0/0".getBytes());
        assertArrayEquals(instance.getData("/0/1/0"), "/0/1/0".getBytes());
        assertArrayEquals(instance.getData("/1/0/1"), "/1/0/1".getBytes());
        assertArrayEquals(instance.getData("/2/2/0"), "/2/2/0".getBytes());
        assertArrayEquals(instance.getData("/99/0"), "/99/0".getBytes());
        assertArrayEquals(instance.getData("/fake/0"), "".getBytes());

        instance = new AsnDataDefault(Maps.<String, byte[]> newHashMap());
        assertArrayEquals(instance.getData("/0/0/0"), "".getBytes());
        assertArrayEquals(instance.getData("/0/1/0"), "".getBytes());
        assertArrayEquals(instance.getData("/1/0/1"), "".getBytes());
        assertArrayEquals(instance.getData("/2/2/0"), "".getBytes());
        assertArrayEquals(instance.getData("/99/0"), "".getBytes());
        assertArrayEquals(instance.getData("/fake/0"), "".getBytes());
    }

    @Test
    public void testGetData() throws Exception
    {
        AsnData instance = new AsnDataDefault(tagsToData);
        ImmutableMap<String, byte[]> data = instance.getData();
        assertEquals(data.size(), 5);
        assertArrayEquals(data.get("/0/0/0"), "/0/0/0".getBytes());
        assertArrayEquals(data.get("/0/1/0"), "/0/1/0".getBytes());
        assertArrayEquals(data.get("/1/0/1"), "/1/0/1".getBytes());
        assertArrayEquals(data.get("/2/2/0"), "/2/2/0".getBytes());
        assertArrayEquals(data.get("/99/0"), "/99/0".getBytes());

        instance = new AsnDataDefault(Maps.<String, byte[]> newHashMap());
        data = instance.getData();
        assertTrue(data.size() == 0);
    }
}
