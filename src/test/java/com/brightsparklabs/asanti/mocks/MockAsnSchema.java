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
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.DecodeResult;
import com.brightsparklabs.asanti.model.schema.DecodedTag;

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
                    .append("BEGIN")
                    .append("EXPORTS Header, Body;\n")
                    .append("IMPORTS")
                    .append("  People,")
                    .append("  Person")
                    .append("    FROM People-Protocol")
                    .append("    { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) };")
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
                    .append("        date       [0] DATE,")
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
                    .append("\t\tDefaultAge INTEGER ::= 45\r\n")
                    .append("\t\tPeople ::= SET OF Person\r\n")
                    .append("\t\tPerson ::= SEQUENCE\r\n")
                    .append("\t\t{\r\n")
                    .append("\t\t\t\tfirstName \t[1]\t OCTET STRING,\r\n")
                    .append("\t\t\t\tlastName \t [2]\t OCTET STRING,\r\n")
                    .append("\t\t\t\ttitle\t\t   [3]\t ENUMERATED\r\n")
                    .append("\t\t\t\t\t\t{ mr, mrs, ms, dr, rev } OPTIONAL,\r\n")
                    .append("\t\t\t\tgender \t \tGender OPTIONAL\r\n")
                    .append("\t\t}  \n")
                    .append("\t\tGender ::= ENUMERATED   \r\n")
                    .append("\t\t{")
                    .append("\t\t\t\tmale(0), \r\n")
                    .append("\t\t\t\tfemale(1)\t\t\r\n")
                    .append("\t\t}\r\n")
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
    public static AsnSchema getInstance()
    {
        if (instance != null) { return instance; }

        instance = mock(AsnSchema.class);

        final NonEmptyByteArrayMatcher nonEmptyByteArrayMatcher = new NonEmptyByteArrayMatcher();
        when(instance.getPrintableString(anyString(), any(byte[].class))).thenReturn("");
        when(instance.getPrintableString(anyString(), argThat(nonEmptyByteArrayMatcher))).thenReturn("printableString");
        when(instance.getDecodedObject(anyString(), any(byte[].class))).thenReturn("");
        when(instance.getDecodedObject(anyString(), argThat(nonEmptyByteArrayMatcher))).thenReturn("decodedObject");

        configureGetDecodedTag(instance, "/1/0/1", "Document", "/Document/header/published/date", true);
        configureGetDecodedTag(instance, "/2/0/0", "Document", "/Document/body/lastModified/date", true);
        configureGetDecodedTag(instance, "/2/1/1", "Document", "/Document/body/prefix/text", true);
        configureGetDecodedTag(instance, "/2/2/1", "Document", "/Document/body/content/text", true);
        configureGetDecodedTag(instance, "/3/0/1", "Document", "/Document/footer/author/firstName", true);
        configureGetDecodedTag(instance, "/2/2/99", "Document", "/Document/body/content/99", false);
        configureGetDecodedTag(instance, "/99/1/1", "Document", "/Document/99/1/1", false);

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

    /**
     * Configures the {@code getDecodedTag()} method on the supplied instance to
     * use the mocked values supplied
     *
     * @param instance
     *            instance to configure
     *
     * @param rawTag
     *            the raw tag to return
     *
     * @param topLevelTypeName
     *            the top level type name
     *
     * @param decodedTagPath
     *            the value to return for {@link DecodedTag#getDecodedTag()}
     *
     * @param isFullyDecoded
     *            the value to return for {@link DecodeResult#wasSuccessful()}
     */
    private static void configureGetDecodedTag(AsnSchema instance, String rawTag, String topLevelTypeName,
            String decodedTagPath, boolean isFullyDecoded)
    {
        final DecodedTag decodedTag = mock(DecodedTag.class);
        when(decodedTag.getDecodedTag()).thenReturn(decodedTagPath);
        when(decodedTag.getRawTag()).thenReturn(rawTag);
        when(decodedTag.getType()).thenReturn(AsnSchemaTypeDefinition.NULL);
        when(decodedTag.isFullyDecoded()).thenReturn(isFullyDecoded);
        when(instance.getDecodedTag(rawTag, topLevelTypeName)).thenReturn(DecodeResult.create(isFullyDecoded,
                decodedTag));
    }
}
