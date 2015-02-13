/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.reader;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.brightsparklabs.asanti.mocks.MockAsnBerFile;
import com.brightsparklabs.asanti.model.data.AsnData;
import com.google.common.collect.ImmutableList;

/**
 * Unit test for {@link AsnBerFileReader}
 *
 * @author brightSPARK Labs
 */
public class AsnBerFileReaderTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testReadFileInt() throws Exception
    {
        final File berFile = MockAsnBerFile.createAsnBerFileContainingPeoplePdus(5);

        // test minimum
        ImmutableList<AsnData> result = AsnBerFileReader.read(berFile, 1);
        assertEquals(1, result.size());

        // test middle
        result = AsnBerFileReader.read(berFile, 3);
        assertEquals(3, result.size());

        // test maximum
        result = AsnBerFileReader.read(berFile, 5);
        assertEquals(5, result.size());

        // test over-specified
        result = AsnBerFileReader.read(berFile, Integer.MAX_VALUE);
        assertEquals(5, result.size());

        // test unlimited
        result = AsnBerFileReader.read(berFile, 0);
        assertEquals(5, result.size());
        result = AsnBerFileReader.read(berFile, -1);
        assertEquals(5, result.size());
        result = AsnBerFileReader.read(berFile, Integer.MIN_VALUE);
        assertEquals(5, result.size());
    }

    @Test
    public void testReadFile() throws Exception
    {
        final File berFile = MockAsnBerFile.createAsnBerFileContainingDocumentPdus(5);
        final ImmutableList<AsnData> result = AsnBerFileReader.read(berFile);
        assertEquals(5, result.size());
    }
}
