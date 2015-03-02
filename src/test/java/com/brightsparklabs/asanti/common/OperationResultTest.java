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
        OperationResult<String> instance = OperationResult.createSuccessfulInstance("TEST");
        assertEquals(true, instance.wasSuccessful());
        instance = OperationResult.createUnsuccessfulInstance("TEST", "Failed");
        assertEquals(false, instance.wasSuccessful());
    }

    @Test
    public void testGetDecodedData() throws Exception
    {
        OperationResult<String> instanceString = OperationResult.createSuccessfulInstance("TEST");
        assertEquals("TEST", instanceString.getDecodedData());
        instanceString = OperationResult.createUnsuccessfulInstance("TEST", "Failed");
        assertEquals("TEST", instanceString.getDecodedData());

        OperationResult<Integer> instanceInteger = OperationResult.createSuccessfulInstance(Integer.MIN_VALUE);
        assertEquals((Integer) Integer.MIN_VALUE, instanceInteger.getDecodedData());
        instanceInteger = OperationResult.createSuccessfulInstance(Integer.MAX_VALUE);
        assertEquals((Integer) Integer.MAX_VALUE, instanceInteger.getDecodedData());
        instanceInteger = OperationResult.createSuccessfulInstance(0);
        assertEquals((Integer) 0, instanceInteger.getDecodedData());

        final Timestamp expected = new Timestamp(1000);
        final OperationResult<Timestamp> instanceTimestamp = OperationResult.createSuccessfulInstance(expected);
        assertEquals(expected, instanceTimestamp.getDecodedData());
    }

    @Test
    public void testGetFailureReason() throws Exception
    {
        OperationResult<String> instance = OperationResult.createSuccessfulInstance("TEST");
        assertEquals("", instance.getFailureReason());
        instance = OperationResult.createUnsuccessfulInstance("TEST", "Failed");
        assertEquals("Failed", instance.getFailureReason());
    }
}
