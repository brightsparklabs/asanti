/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks;

import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Utility class for obtaining mocked ASN.1 BER files which conform to the test ASN.1 schema defined
 * in the {@code README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockAsnBerFile
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** encoding for converting to/from hex */
    private static final BaseEncoding hexEncoding = BaseEncoding.base16().lowerCase();

    /**
     * a PDU of a 'Document' value assignment setting the minimum required fields
     */
    public static final String EXAMPLE_SCHEMA_DOCUMENT_PDU_MINIMUM = new StringBuilder().append(
            "doc Document ::=\n")
            .append("{\n")
            .append("  header\n")
            .append("  {\n")
            .append("    published { date \"201501020000Z\"}\n")
            .append("  },\n")
            .append("  body\n")
            .append("  {\n")
            .append("    lastModified\n")
            .append("    {\n").append("      date \"2015-01-01\",\n")
                    // firstName: "Donald", lastName: "Duck"
            .append("      modifiedBy {firstName '446f6e616c64'H, lastName '4475636b'H}\n")
            .append("    },\n")
            .append("    content\n")
            .append("    {\n")
            .append("      paragraphs\n")
            .append("      {\n").append("        {\n")
                    // title: "Paragraph 1"
            .append("          title '5061726167726170682031'H,\n")
                    // Points: "Point 1.1", "Point 1.2", "Point 1.3 "
            .append("          points {'506f696e7420312e31'H, '506f696e7420312e32'H, '506f696e7420312e33'H}\n")
            .append("        },\n").append("        {\n")
                    // title: "Paragraph 2"
            .append("          title '5061726167726170682032'H,\n")
                    // Points: "Point 2.1", "Point 2.2", "Point 2.3"
            .append("          points {'506f696e7420322e31'H, '506f696e7420322e32'H, '506f696e7420322e33'H}\n")
            .append("        }\n")
            .append("      }\n")
            .append("    }\n")
            .append("  },\n")
            .append("  footer\n")
            .append("  {\n")
            .append("    authors").append("    {\n")
                    // firstName: "Mickey", lastName: "Mouse"
            .append("      {firstName '4d69636b6579'H, lastName '4d6f757365'H},\n")
                    // firstName: "Donald", lastName: "Duck"
            .append("      {firstName '446f6e616c64'H, lastName '4475636b'H}\n")
            .append("    }\n")
            .append("  }\n")
            .append("}")
            .toString();

    /**
     * bytes from encoding {@link #EXAMPLE_SCHEMA_DOCUMENT_PDU_MINIMUM} using BER
     */
    // generated using http://asn1-playground.oss.com/
    private static final byte[] EXAMPLE_SCHEMA_DOCUMENT_PDU_MINIMUM_BER = hexEncoding.decode(
            "3081bfa111a00f810d3230313530313032303030305aa28184a01a80083230313530313031a10e8106446f6e616c6482044475636ba266a2643030810b5061726167726170682031a3210409506f696e7420312e310409506f696e7420312e320409506f696e7420312e333030810b5061726167726170682032a3210409506f696e7420322e310409506f696e7420322e320409506f696e7420322e33a323a021300f81064d69636b657982054d6f757365300e8106446f6e616c6482044475636b");

    /** a PDU of a 'People' value assignment setting the minimum required fields */
    public static final String EXAMPLE_SCHEMA_PEOPLE_PDU_MINIMUM = new StringBuilder().append(
            "team People ::= \n").append("{  \n")
            // firstName: "Mickey", lastName: "Mouse"
            .append("  { firstName '446f6e616c64'H, lastName '4475636b'H },\n")
                    // firstName: "Donald", lastName: "Duck"
            .append("  { firstName '4d69636b6579'H, lastName '4d6f757365'H }\n")
            .append("}\n")
            .toString();

    /**
     * bytes from encoding {@link #EXAMPLE_SCHEMA_PEOPLE_PDU_MINIMUM} using BER
     */
    // generated using http://asn1-playground.oss.com/
    private static final byte[] EXAMPLE_SCHEMA_PEOPLE_PDU_MINIMUM_BER = hexEncoding.decode(
            "3121300e8106446f6e616c6482044475636b300f81064d69636b657982054d6f757365");

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Creates an ASN.1 BER binary file containing the specified number of PDUs. Each PDU is a copy
     * of {@link #EXAMPLE_SCHEMA_DOCUMENT_PDU_MINIMUM}.
     *
     * @param pduCount
     *         number of PDUs to include in the file
     *
     * @return an ASN.1 BER binary file containing the specified number of PDUs
     *
     * @throws IOException
     *         if any errors occur while creating the file
     */
    public static ByteSource createAsnBerDataContainingDocumentPdus(int pduCount) throws IOException
    {
        return createAsnBerData(pduCount, EXAMPLE_SCHEMA_DOCUMENT_PDU_MINIMUM_BER);
    }

    /**
     * Creates an ASN.1 BER binary file containing the specified number of PDUs. Each PDU is a copy
     * of {@link #EXAMPLE_SCHEMA_PEOPLE_PDU_MINIMUM}.
     *
     * @param pduCount
     *         number of PDUs to include in the file
     *
     * @return an ASN.1 BER binary file containing the specified number of PDUs
     *
     * @throws IOException
     *         if any errors occur while creating the file
     */
    public static ByteSource createAsnBerDataContainingPeoplePdus(int pduCount) throws IOException
    {
        return createAsnBerData(pduCount, EXAMPLE_SCHEMA_PEOPLE_PDU_MINIMUM_BER);
    }

    /**
     * Creates an ASN.1 BER binary file containing the specified number of PDUs. Each PDU is a copy
     * of the supplied pdu bytes.
     *
     * @param pduCount
     *         number of PDUs to include in the file
     * @param pdu
     *         bytes which comprise the PDU
     *
     * @return an ASN.1 BER binary file containing the specified number of PDUs
     *
     * @throws IOException
     *         if any errors occur while creating the file
     */
    public static ByteSource createAsnBerData(int pduCount, byte[] pdu) throws IOException
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        for (int i = 0; i < pduCount; i++)
        {
            stream.write(pdu);
        }

        final ByteSource result = ByteSource.wrap(stream.toByteArray());
        stream.close();
        return result;
    }
}
