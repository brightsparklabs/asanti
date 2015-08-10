/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.reader;

import com.brightsparklabs.asanti.mocks.MockAsnBerFile;
import com.brightsparklabs.asanti.model.data.RawAsnData;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteSource;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for {@link AsnBerDataReader}
 *
 * @author brightSPARK Labs
 */
public class AsnBerDataReaderTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** encoding for converting to/from hex */
    private static final BaseEncoding hexEncoding = BaseEncoding.base16().lowerCase();

    /** hex string representing 1000 hyphen characters */
    private static final String HEXSTRING_1000_HYPHENS = Strings.repeat("2d", 1000);

    /** a PDU of a 'People' value assignment setting using a large octet string */
    public static final String EXAMPLE_SCHEMA_PEOPLE_PDU_LARGE_OCTET_STRING
            = new StringBuilder().append("team People ::= \n").append("{  \n").append("  {")
            // firstName: <1000 hyphens>
            .append(" firstName '").append(HEXSTRING_1000_HYPHENS).append("'H,")
                    // lastName: "First name is 1000 octets long"
            .append(" lastName '4669727374206e616d652069732031303030206f6374657473206c6f6e67'H }")
            .append("}\n")
            .toString();

    /**
     * bytes from encoding {@link #EXAMPLE_SCHEMA_PEOPLE_PDU_LARGE_OCTET_STRING} using BER
     */
    // generated using http://asn1-playground.oss.com/
    private static final byte[] EXAMPLE_SCHEMA_PEOPLE_PDU_LARGE_OCTET_STRING_BER
            = hexEncoding.decode("318204103082040c818203e8" + HEXSTRING_1000_HYPHENS
            + "821e4669727374206e616d652069732031303030206f6374657473206c6f6e67");

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testReadFileInt() throws Exception
    {
        final ByteSource berData = MockAsnBerFile.createAsnBerDataContainingPeoplePdus(5);

        // test minimum
        ImmutableList<RawAsnData> result = AsnBerDataReader.read(berData, 1);
        assertEquals(1, result.size());

        // test middle
        result = AsnBerDataReader.read(berData, 3);
        assertEquals(3, result.size());

        // test maximum
        result = AsnBerDataReader.read(berData, 5);
        assertEquals(5, result.size());

        // test over-specified
        result = AsnBerDataReader.read(berData, Integer.MAX_VALUE);
        assertEquals(5, result.size());

        // test unlimited
        result = AsnBerDataReader.read(berData, 0);
        assertEquals(5, result.size());
        result = AsnBerDataReader.read(berData, -1);
        assertEquals(5, result.size());
        result = AsnBerDataReader.read(berData, Integer.MIN_VALUE);
        assertEquals(5, result.size());
    }

    @Test
    public void testReadFile() throws Exception
    {
        final ByteSource berData = MockAsnBerFile.createAsnBerDataContainingDocumentPdus(5);
        final ImmutableList<RawAsnData> result = AsnBerDataReader.read(berData);
        assertEquals(5, result.size());
        for (final RawAsnData pdu : result)
        {
            assertEquals(16, pdu.getRawTags().size());
            assertArrayEquals("201501020000Z".getBytes(Charsets.UTF_8),
                    pdu.getBytes("/0[1]/0[0]/0[1]").get());
            assertArrayEquals("20150101".getBytes(Charsets.UTF_8), pdu.getBytes("/1[2]/0[0]/0[0]").get());
            assertArrayEquals("Donald".getBytes(Charsets.UTF_8),
                    pdu.getBytes("/1[2]/0[0]/1[1]/0[1]").get());
            assertArrayEquals("Duck".getBytes(Charsets.UTF_8),
                    pdu.getBytes("/1[2]/0[0]/1[1]/1[2]").get());
            assertArrayEquals("Paragraph 1".getBytes(Charsets.UTF_8),
                    pdu.getBytes("/1[2]/1[2]/0[2]/0[UNIVERSAL 16]/0[1]").get());
            assertArrayEquals("Point 1.1".getBytes(Charsets.UTF_8),
                    pdu.getBytes("/1[2]/1[2]/0[2]/0[UNIVERSAL 16]/1[3]/0[UNIVERSAL 4]").get());
            assertArrayEquals("Point 1.2".getBytes(Charsets.UTF_8),
                    pdu.getBytes("/1[2]/1[2]/0[2]/0[UNIVERSAL 16]/1[3]/1[UNIVERSAL 4]").get());
            assertArrayEquals("Point 1.3".getBytes(Charsets.UTF_8),
                    pdu.getBytes("/1[2]/1[2]/0[2]/0[UNIVERSAL 16]/1[3]/2[UNIVERSAL 4]").get());
            assertArrayEquals("Paragraph 2".getBytes(Charsets.UTF_8),
                    pdu.getBytes("/1[2]/1[2]/0[2]/1[UNIVERSAL 16]/0[1]").get());
            assertArrayEquals("Point 2.1".getBytes(Charsets.UTF_8),
                    pdu.getBytes("/1[2]/1[2]/0[2]/1[UNIVERSAL 16]/1[3]/0[UNIVERSAL 4]").get());
            assertArrayEquals("Point 2.2".getBytes(Charsets.UTF_8),
                    pdu.getBytes("/1[2]/1[2]/0[2]/1[UNIVERSAL 16]/1[3]/1[UNIVERSAL 4]").get());
            assertArrayEquals("Point 2.3".getBytes(Charsets.UTF_8),
                    pdu.getBytes("/1[2]/1[2]/0[2]/1[UNIVERSAL 16]/1[3]/2[UNIVERSAL 4]").get());
            assertArrayEquals("Mickey".getBytes(Charsets.UTF_8),
                    pdu.getBytes("/2[3]/0[0]/0[UNIVERSAL 16]/0[1]").get());
            assertArrayEquals("Mouse".getBytes(Charsets.UTF_8),
                    pdu.getBytes("/2[3]/0[0]/0[UNIVERSAL 16]/1[2]").get());
            assertArrayEquals("Donald".getBytes(Charsets.UTF_8),
                    pdu.getBytes("/2[3]/0[0]/1[UNIVERSAL 16]/0[1]").get());
            assertArrayEquals("Duck".getBytes(Charsets.UTF_8),
                    pdu.getBytes("/2[3]/0[0]/1[UNIVERSAL 16]/1[2]").get());
        }
    }

    @Test
    public void testReadFile_LargeOctetString() throws Exception
    {
        final ByteSource berData = MockAsnBerFile.createAsnBerData(5,
                EXAMPLE_SCHEMA_PEOPLE_PDU_LARGE_OCTET_STRING_BER);
        final ImmutableList<RawAsnData> result = AsnBerDataReader.read(berData);
        assertEquals(5, result.size());

        final RawAsnData pdu = result.get(0);
        assertArrayEquals(hexEncoding.decode(HEXSTRING_1000_HYPHENS),
                pdu.getBytes("/0[UNIVERSAL 16]/0[1]").get());
        assertArrayEquals("First name is 1000 octets long".getBytes(Charsets.UTF_8),
                pdu.getBytes("/0[UNIVERSAL 16]/1[2]").get());
    }
}
