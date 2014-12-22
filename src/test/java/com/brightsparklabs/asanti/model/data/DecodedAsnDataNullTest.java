/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.data;

import static org.junit.Assert.*;

import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Unit test
 *
 * @author brightSPARK Labs
 */
public class DecodedAsnDataNullTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** default instance to test */
    private static DecodedAsnData instance = DecodedAsnData.NULL;

    /** regex which will match on anything */
    private static Pattern regex = Pattern.compile(".*");

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testGetTags() throws Exception
    {
        assertTrue(instance.getTags().isEmpty());
    }

    @Test
    public void testGetUnmappedTags() throws Exception
    {
        assertTrue(instance.getUnmappedTags().isEmpty());
    }

    @Test
    public void testContains() throws Exception
    {
        assertFalse(instance.contains("*"));
        assertFalse(instance.contains("/*"));
        assertFalse(instance.contains("/"));
        assertFalse(instance.contains(""));
    }

    @Test
    public void testGetBytes() throws Exception
    {
        assertArrayEquals(new byte[0], instance.getBytes("*"));
    }

    @Test
    public void testGetBytesMatching() throws Exception
    {
        assertEquals(0, instance.getBytesMatching(regex).size());
    }

    @Test
    public void testGetHexString() throws Exception
    {
        assertEquals("0x", instance.getHexString("*"));
        assertEquals("0x", instance.getHexString("/*"));
        assertEquals("0x", instance.getHexString("/"));
        assertEquals("0x", instance.getHexString(""));
    }

    @Test
    public void testGetHexStringsMatching() throws Exception
    {
        assertEquals(0, instance.getHexStringsMatching(regex).size());
    }

    @Test
    public void testGetPrintableString() throws Exception
    {
        assertEquals("", instance.getPrintableString("*"));
        assertEquals("", instance.getPrintableString("/*"));
        assertEquals("", instance.getPrintableString("/"));
        assertEquals("", instance.getPrintableString(""));
    }

    @Test
    public void testGetPrintableStringsMatching() throws Exception
    {
        assertEquals(0, instance.getPrintableStringsMatching(regex).size());
    }

    @Test
    public void testGetDecodedObject() throws Exception
    {
        assertArrayEquals(new byte[0], (byte[]) instance.getDecodedObject("*"));
        assertArrayEquals(new byte[0], (byte[]) instance.getDecodedObject("/*"));
        assertArrayEquals(new byte[0], (byte[]) instance.getDecodedObject("/"));
        assertArrayEquals(new byte[0], (byte[]) instance.getDecodedObject(""));
    }

    @Test
    public void testGetDecodedObjectsMatching() throws Exception
    {
        assertEquals(0, instance.getDecodedObjectsMatching(regex).size());
    }
}
