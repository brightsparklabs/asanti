/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import org.mockito.ArgumentMatcher;

import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodeResult;

/**
 * Utility class for obtaining mocked instances of {@link AsnSchema} which
 * conform to the test ASN.1 schema defined in the {@linkplain README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockAsnSchema
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** the example schema defined in the {@linkplain README.md} file */
    public static final String TEST_SCHEMA_TEXT =
            new StringBuilder().append("Document-PDU\n")
                    .append("    { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) document(1) }\n")
                    .append("DEFINITIONS")
                    .append("    AUTOMATIC TAGS ::=")
                    .append("IMPORTS")
                    .append("  Person")
                    .append("    FROM People-Protocol")
                    .append("    { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }")
                    .append("BEGIN")
                    .append("    Document ::= SEQUENCE")
                    .append("    {\n")
                    .append("        header [1] Header,\n")
                    .append("        body   [2] Body,\n")
                    .append("        footer [3] Footer\n")
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
                    .append("    Footer ::= SEQUENCE")
                    .append("    {")
                    .append("        author      [0] Person")
                    .append("    }\n")
                    .append("    PublishedMetadata ::= SEQUENCE")
                    .append("    {")
                    .append("        date    [1] GeneralizedTime,")
                    .append("        country [2] OCTET STRING OPTIONAL")
                    .append("    }\n")
                    .append("    ModificationMetadata ::= SEQUENCE")
                    .append("    {")
                    .append("        date       [0] Date,")
                    .append("        modifiedBy [1] Person")
                    .append("    }\n")
                    .append("    Section-Note ::= SEQUENCE")
                    .append("    {")
                    .append("        text [1] OCTET STRING")
                    .append("    }\n")
                    .append("    Section-Main ::= SEQUENCE")
                    .append("    {")
                    .append("        text       [1] OCTET STRING OPTIONAL,")
                    .append("        paragraphs [2] SEQUENCE OF Paragraph")
                    .append("    }\n")
                    .append("    Paragraph ::=  SEQUENCE")
                    .append("    {")
                    .append("        title        [1] OCTET STRING,")
                    .append("        contributor  [2] Person OPTIONAL,")
                    .append("        points       [3] SEQUENCE OF OCTET STRING")
                    .append("    }\n")
                    .append("END")
                    .append("\n")
                    .append("People-Protocol\r\n\r\n")
                    .append("\t\t{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }\r\n")
                    .append("DEFINITIONS\r\n")
                    .append("\t\tAUTOMATIC TAGS ::=\r\n")
                    .append("BEGIN\r\n")
                    .append("\t\tPeople ::= SET OF Person\r\n")
                    .append("\t\tPerson ::= SEQUENCE\r\n")
                    .append("\t\t{\r\n")
                    .append("\t\t\t\tfirstName \t[1]\t OCTET STRING,\r\n")
                    .append("\t\t\t\tlastName \t [2]\t OCTET STRING,\r\n")
                    .append("\t\t\t\ttitle\t\t   [3]\t ENUMERATED\r\n")
                    .append("\t\t\t\t\t\t{ mr, mrs, ms, dr, rev } OPTIONAL,\r\n")
                    .append("\t\t\t\tgender \t OPTIONAL\r\n")
                    .append("\t\t}  \n")
                    .append("\t\tGender ::= ENUMERATED   \r\n")
                    .append("\t\t{")
                    .append("\t\t\t\tmale(0), \r\n")
                    .append("\t\t\t\tfemale(1)\t\t\r\n")
                    .append("\t\t}\r\n")
                    .append("END\r\n")
                    .toString();

    /** the example Document-PDU module defined in the {@linkplain README.md} file */
    public static final Iterable<String> TEST_MODULE_DOCUMENT_PDU =
            Lists.newArrayList(
                    "Document-PDU",
                    "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) document(1) }",
                    "DEFINITIONS",
                    "AUTOMATIC TAGS ::=",
                    "IMPORTS",
                    "Person FROM People-Protocol { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }",
                    "BEGIN",
                    "Document ::= SEQUENCE {",
                    "header [1] Header,",
                    "body [2] Body,",
                    "footer [3] Footer",
                    "}",
                    "Header ::= SEQUENCE",
                    "{ published [0] PublishedMetadata }",
                    "Body ::= SEQUENCE { lastModified [0] ModificationMetadata, prefix [1] Section-Note OPTIONAL, content [2] Section-Main,",
                    "suffix [3] Section-Note OPTIONAL }",
                    "Footer ::= SEQUENCE { author [0] Person }",
                    "PublishedMetadata ::= SEQUENCE { date [1] GeneralizedTime, country [2] OCTET STRING OPTIONAL }",
                    "ModificationMetadata ::= SEQUENCE { date [0] Date, modifiedBy [1] Person }",
                    "Section-Note ::= SEQUENCE { text [1] OCTET STRING }",
                    "Section-Main ::= SEQUENCE { text [1] OCTET STRING OPTIONAL, paragraphs [2] SEQUENCE OF Paragraph }",
                    "Paragraph ::= SEQUENCE { title [1] OCTET STRING, contributor [2] Person OPTIONAL, points [3] SEQUENCE OF OCTET STRING }",
                    "END");

    /** the example People-Protocol module defined in the {@linkplain README.md} file */
    public static final Iterable<String> TEST_MODULE_PEOPLE_PROTOCOL =
            Lists.newArrayList(
                    "People-Protocol",
                    "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }",
                    "DEFINITIONS",
                    "AUTOMATIC TAGS ::=",
                    "BEGIN",
                    "People ::= SET OF Person",
                    "Person ::= SEQUENCE",
                    "{",
                    "firstName [1] OCTET STRING,",
                    "lastName [2] OCTET STRING,",
                    "title [3] ENUMERATED",
                    "{ mr, mrs, ms, dr, rev } OPTIONAL,",
                    "gender OPTIONAL",
                    "}",
                    "Gender ::= ENUMERATED",
                    "{ male(0),",
                    "female(1)",
                    "}",
                    "END");

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
    public static AsnSchema getInstance()
    {
        if (instance != null) { return instance; }

        instance = mock(AsnSchema.class);
        when(instance.getDecodedTag("/1/0/1", "Document")).thenReturn(DecodeResult.create(true,
                "/Document/header/published/date"));
        when(instance.getDecodedTag("/2/0/0", "Document")).thenReturn(DecodeResult.create(true,
                "/Document/body/lastModified/date"));
        when(instance.getDecodedTag("/2/1/1", "Document")).thenReturn(DecodeResult.create(true,
                "/Document/body/prefix/text"));
        when(instance.getDecodedTag("/2/2/1", "Document")).thenReturn(DecodeResult.create(true,
                "/Document/body/content/text"));
        when(instance.getDecodedTag("/3/0/1", "Document")).thenReturn(DecodeResult.create(true,
                "/Document/footer/author/firstName"));
        when(instance.getDecodedTag("/2/2/99", "Document")).thenReturn(DecodeResult.create(false,
                "/Document/body/content/99"));
        when(instance.getDecodedTag("/99/1/1", "Document")).thenReturn(DecodeResult.create(false, "/Document/99/1/1"));

        final NonEmptyByteArrayMatcher nonEmptyByteArrayMatcher = new NonEmptyByteArrayMatcher();
        when(instance.getPrintableString(anyString(), any(byte[].class))).thenReturn("");
        when(instance.getPrintableString(anyString(), argThat(nonEmptyByteArrayMatcher))).thenReturn("printableString");
        when(instance.getDecodedObject(anyString(), any(byte[].class))).thenReturn("");
        when(instance.getDecodedObject(anyString(), argThat(nonEmptyByteArrayMatcher))).thenReturn("decodedObject");

        return instance;
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: NonEmptyByteArrayMatcher
    // -------------------------------------------------------------------------

    /**
     * Matches non-empty byte[] arguments
     */
    private static class NonEmptyByteArrayMatcher extends ArgumentMatcher<byte[]>
    {
        @Override
        public boolean matches(Object item)
        {
            return ((byte[]) item).length > 0;
        }
    };
}
