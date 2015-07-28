/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.common;

import static org.junit.Assert.*;

import java.sql.Timestamp;

import org.junit.Test;

/**
 * Unit test for {@link OperationResult}
 *
 * @author brightSPARK Labs
 */
public class OperationResultTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testWasSuccessful() throws Exception
    {
        OperationResult<String, String> instance = OperationResult.createSuccessfulInstance("TEST");
        assertEquals(true, instance.wasSuccessful());
        instance = OperationResult.createUnsuccessfulInstance("TEST", "Failed");
        assertEquals(false, instance.wasSuccessful());
    }

    @Test
    public void testGetDecodedData() throws Exception
    {
        OperationResult<String, String> instanceString = OperationResult.createSuccessfulInstance("TEST");
        assertEquals("TEST", instanceString.getOutput());
        instanceString = OperationResult.createUnsuccessfulInstance("TEST", "Failed");
        assertEquals("TEST", instanceString.getOutput());

        OperationResult<Integer, String> instanceInteger = OperationResult.createSuccessfulInstance(Integer.MIN_VALUE);
        assertEquals((Integer) Integer.MIN_VALUE, instanceInteger.getOutput());
        instanceInteger = OperationResult.createSuccessfulInstance(Integer.MAX_VALUE);
        assertEquals((Integer) Integer.MAX_VALUE, instanceInteger.getOutput());
        instanceInteger = OperationResult.createSuccessfulInstance(0);
        assertEquals((Integer) 0, instanceInteger.getOutput());

        final Timestamp expected = new Timestamp(1000);
        final OperationResult<Timestamp, String> instanceTimestamp = OperationResult.createSuccessfulInstance(expected);
        assertEquals(expected, instanceTimestamp.getOutput());
    }

    @Test
    public void testGetFailureReason() throws Exception
    {
        OperationResult<String, String> instance = OperationResult.createSuccessfulInstance("TEST");
        assertEquals("", instance.getFailureReason().or(""));
        instance = OperationResult.createUnsuccessfulInstance("TEST", "Failed");
        assertEquals("Failed", instance.getFailureReason().or(""));
    }
}
