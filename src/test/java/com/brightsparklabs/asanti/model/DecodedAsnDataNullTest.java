/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test
 *
 * @author brightSPARK Labs <enquire@brightsparklabs.com>
 */
public class DecodedAsnDataNullTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testGetTags() throws Exception
    {
        final DecodedAsnData instance = DecodedAsnData.NULL;
        assertTrue(instance.getTags().isEmpty());
    }

    @Test
    public void testGetUnmappedTags() throws Exception
    {
        final DecodedAsnData instance = DecodedAsnData.NULL;
        assertTrue(instance.getUnmappedTags().isEmpty());
    }

    @Test
    public void testContains() throws Exception
    {
        final DecodedAsnData instance = DecodedAsnData.NULL;
        assertFalse(instance.contains("*"));
        assertFalse(instance.contains("/*"));
        assertFalse(instance.contains("/"));
        assertFalse(instance.contains(""));
    }
}
