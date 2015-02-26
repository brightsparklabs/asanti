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
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.io.BaseEncoding;

/**
 * Unit test for {@link AsnBerFileReader}
 *
 * @author brightSPARK Labs
 */
public class AsnBerFileReaderTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** encoding for converting to/from hex */
    private static final BaseEncoding hexEncoding = BaseEncoding.base16().lowerCase();

    /** hex string representing 1000 hyphen characters */
    private static final String HEXSTRING_1000_HYPHENS = Strings.repeat("2d", 1000);

    /** a PDU of a 'People' value assignment setting using a large octet string */
    public static final String EXAMPLE_SCHEMA_PEOPLE_PDU_LARGE_OCTET_STRING = new StringBuilder()
            .append("team People ::= \n")
            .append("{  \n")
            .append("  {")
            // firstName: <1000 hyphens>
            .append(" firstName '")
            .append(HEXSTRING_1000_HYPHENS)
            .append("'H,")
            // lastName: "First name is 1000 octets long"
            .append(" lastName '4669727374206e616d652069732031303030206f6374657473206c6f6e67'H }")
            .append("}\n")
            .toString();

    /**
     * bytes from encoding {@link #EXAMPLE_SCHEMA_PEOPLE_PDU_LARGE_OCTET_STRING}
     * using BER
     */
    // generated using http://asn1-playground.oss.com/
    private static final byte[] EXAMPLE_SCHEMA_PEOPLE_PDU_LARGE_OCTET_STRING_BER = hexEncoding.decode("318204103082040c818203e8"
            + HEXSTRING_1000_HYPHENS + "821e4669727374206e616d652069732031303030206f6374657473206c6f6e67");

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
        for (final AsnData pdu : result)
        {
            assertEquals(16, pdu.getRawTags().size());
            assertArrayEquals("201501020000Z".getBytes(Charsets.UTF_8), pdu.getBytes("/1/0/1"));
            assertArrayEquals("20150101".getBytes(Charsets.UTF_8), pdu.getBytes("/2/0/0"));
            assertArrayEquals("Donald".getBytes(Charsets.UTF_8), pdu.getBytes("/2/0/1/1"));
            assertArrayEquals("Duck".getBytes(Charsets.UTF_8), pdu.getBytes("/2/0/1/2"));
            assertArrayEquals("Paragraph 1".getBytes(Charsets.UTF_8), pdu.getBytes("/2/2/2[0]/1"));
            assertArrayEquals("Point 1.1".getBytes(Charsets.UTF_8), pdu.getBytes("/2/2/2[0]/3[0]"));
            assertArrayEquals("Point 1.2".getBytes(Charsets.UTF_8), pdu.getBytes("/2/2/2[0]/3[1]"));
            assertArrayEquals("Point 1.3".getBytes(Charsets.UTF_8), pdu.getBytes("/2/2/2[0]/3[2]"));
            assertArrayEquals("Paragraph 2".getBytes(Charsets.UTF_8), pdu.getBytes("/2/2/2[1]/1"));
            assertArrayEquals("Point 2.1".getBytes(Charsets.UTF_8), pdu.getBytes("/2/2/2[1]/3[0]"));
            assertArrayEquals("Point 2.2".getBytes(Charsets.UTF_8), pdu.getBytes("/2/2/2[1]/3[1]"));
            assertArrayEquals("Point 2.3".getBytes(Charsets.UTF_8), pdu.getBytes("/2/2/2[1]/3[2]"));
            assertArrayEquals("Mickey".getBytes(Charsets.UTF_8), pdu.getBytes("/3/0[0]/1"));
            assertArrayEquals("Mouse".getBytes(Charsets.UTF_8), pdu.getBytes("/3/0[0]/2"));
            assertArrayEquals("Donald".getBytes(Charsets.UTF_8), pdu.getBytes("/3/0[1]/1"));
            assertArrayEquals("Duck".getBytes(Charsets.UTF_8), pdu.getBytes("/3/0[1]/2"));
        }
    }

    @Test
    public void testReadFile_LargeOctetString() throws Exception
    {
        final File berFile = MockAsnBerFile.createAsnBerFile(5, EXAMPLE_SCHEMA_PEOPLE_PDU_LARGE_OCTET_STRING_BER);
        final ImmutableList<AsnData> result = AsnBerFileReader.read(berFile);
        assertEquals(5, result.size());

        final AsnData pdu = result.get(0);
        assertArrayEquals(hexEncoding.decode(HEXSTRING_1000_HYPHENS), pdu.getBytes("[0]/1"));
        assertArrayEquals("First name is 1000 octets long".getBytes(Charsets.UTF_8), pdu.getBytes("[0]/2"));
    }
}
