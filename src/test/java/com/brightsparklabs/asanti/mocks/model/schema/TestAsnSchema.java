/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.mocks.model.schema;

import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.reader.AsnSchemaReader;
import com.google.common.io.CharSource;

/**
 * Utility class for obtaining a test instance of {@link AsnSchema} which conform to the test ASN.1
 * schema defined in the {@code README.md} file
 *
 * @author brightSPARK Labs
 */
public class TestAsnSchema {
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** the example schema defined in the {@code README.md} file */
    public static final String TEST_SCHEMA_TEXT =
            new StringBuilder()
                    .append("Document-PDU\n")
                    .append(
                            "    { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) document(1) }\n")
                    .append("DEFINITIONS")
                    .append("    AUTOMATIC TAGS ::=")
                    .append("BEGIN\n")
                    .append("EXPORTS Header, Body;\n")
                    .append("IMPORTS")
                    .append("  People,")
                    .append("  Person")
                    .append("    FROM People-Protocol")
                    .append(
                            "    { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) };")
                    .append("    Document ::= SEQUENCE")
                    .append("    {\n")
                    .append("        header  [1] Header,\n")
                    .append("        body    [2] Body,\n")
                    .append("        footer  [3] Footer,\n")
                    .append("        dueDate [4] Date-Due,\n")
                    .append("        version [5] SEQUENCE\n")
                    .append("        {\n")
                    .append("            majorVersion [0] INTEGER,\n")
                    .append("            minorVersion [1] INTEGER\n")
                    .append("        },\n")
                    .append("        description [6] SET\n")
                    .append("        {\n")
                    .append("            numberLines [0] INTEGER,\n")
                    .append("            summary     [1] OCTET STRING\n")
                    .append("        } OPTIONAL,\n")
                    .append("        aliasHeader [7] OCTET STRING (CONTAINING Header)\n")
                    .append("    }\n")
                    .append("    Header ::= SEQUENCE\n")
                    .append("    {")
                    .append("        published [0] PublishedMetadata")
                    .append("    }\n")
                    .append("    Body ::= SEQUENCE")
                    .append("    {")
                    .append("        lastModified [0] ModificationMetadata,")
                    .append("        prefix       [1] Section-Note OPTIONAL,")
                    .append("        content      [2] Section-Main,\n")
                    .append("        suffix       [3] Section-Note OPTIONAL")
                    .append("    }\n")
                    .append("    Footer ::= SET")
                    .append("    {")
                    .append("        authors [0] People")
                    .append("    }\n")
                    .append("    PublishedMetadata ::= SEQUENCE")
                    .append("    {")
                    .append("        date    [1] GeneralizedTime,")
                    .append("        country [2] OCTET STRING OPTIONAL")
                    .append("    }\n")
                    .append("    ModificationMetadata ::= SEQUENCE")
                    .append("    {")
                    .append("        date       [0] GeneralizedTime,")
                    .append("        modifiedBy [1] Person")
                    .append("    }\n")
                    .append("    Section-Note ::= SEQUENCE")
                    .append("    {")
                    .append("        text [1] OCTET STRING")
                    .append("    }\n")
                    .append("    Section-Main ::= SEQUENCE")
                    .append("    {")
                    .append("        text       [1] UTF8String OPTIONAL,")
                    .append("        paragraphs [2] SEQUENCE OF Paragraph,")
                    .append("        sections   [3] SET OF")
                    .append("                       SET")
                    .append("                       {")
                    .append("                            number [1] INTEGER,")
                    .append("                            text   [2] OCTET STRING")
                    .append("                       }")
                    .append("    }\n")
                    .append("    Paragraph ::=  SEQUENCE")
                    .append("    {")
                    .append("        title        [1] OCTET STRING,")
                    .append("        contributor  [2] Person OPTIONAL,")
                    .append("        points       [3] SEQUENCE OF OCTET STRING")
                    .append("    }\n")
                    .append("    References ::= SEQUENCE (SIZE (1..50)) OF")
                    .append("    SEQUENCE")
                    .append("    {")
                    .append("        title [1] OCTET STRING,")
                    .append("        url   [2] OCTET STRING")
                    .append("    }\n")
                    .append("    Date-Due ::= INTEGER\n")
                    .append("    {")
                    .append("      tomorrow(0),\n")
                    .append("      three-day(1),\n")
                    .append("      week (2)\n")
                    .append("    } DEFAULT week\n")
                    .append("END")
                    .append("\n")
                    .append("People-Protocol\r\n\r\n")
                    .append(
                            "\t\t{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\r\n")
                    .append("DEFINITIONS\r\n")
                    .append("    AUTOMATIC TAGS ::=\r\n")
                    .append("BEGIN\r\n")
                    .append("    DefaultAge INTEGER ::= 45\r\n")
                    .append("    People ::= SET OF Person\r\n")
                    .append("    Person ::= SEQUENCE\r\n")
                    .append("    {\r\n")
                    .append("      firstName \t[1]\t VisibleString,\r\n")
                    .append("      lastName \t [2]\t OCTET STRING,\r\n")
                    .append("      title\t\t   [3]\t ENUMERATED\r\n")
                    .append("      { mr, mrs, ms, dr, rev } OPTIONAL,\r\n")
                    .append("      gender \t \tGender OPTIONAL\r\n")
                    .append("    }  \n")
                    .append("    Gender ::= ENUMERATED   \r\n")
                    .append("    {")
                    .append("      male(0), \r\n")
                    .append("      female(1)\t\t\r\n")
                    .append("    }\r\n")
                    .append("END\r\n")
                    .toString();

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static AsnSchema instance;

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance
     */
    public static AsnSchema getInstance() throws Exception {
        if (instance != null) {
            return instance;
        }

        final CharSource schemaSource = CharSource.wrap(TEST_SCHEMA_TEXT);
        instance = AsnSchemaReader.read(schemaSource);
        return instance;
    }
}
