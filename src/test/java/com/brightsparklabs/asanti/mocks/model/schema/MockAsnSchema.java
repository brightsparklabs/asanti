/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks.model.schema;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.decoder.builtin.BuiltinTypeDecoder;
import com.brightsparklabs.asanti.model.data.AsnData;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodedTag;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypeVisitor;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.sql.Timestamp;
import java.util.Set;

import static org.mockito.Mockito.*;

/**
 * Utility class for obtaining mocked instances of {@link AsnSchema} which conform to the test ASN.1
 * schema defined in the {@linkplain README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockAsnSchema
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** the example schema defined in the {@linkplain README.md} file */
    public static final String TEST_SCHEMA_TEXT = new StringBuilder().append("Document-PDU\n")
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
            .append("        } OPTIONAL\n")
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
            .append("\t\t\t\tgender \t \tGender OPTIONAL,\r\n")
            .append("\t\t\t\tmaritalStatus CHOICE\n")
            .append("\t\t\t\t\t{ Married [0], Single [1] }\n")
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

    private static Set<String> rawTags = Sets.newLinkedHashSet();

    private static final Timestamp publishDate = Timestamp.valueOf("2015-01-01 00:00:00.0");

    private static final Timestamp lastModifiedDate = Timestamp.valueOf("2015-02-02 00:00:00.0");
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance
     */
    public static AsnSchema getInstance() throws Exception
    {
        if (instance != null)
        {
            return instance;
        }

        instance = mock(AsnSchema.class);

        when(instance.getType(anyString())).thenReturn(Optional.<AsnSchemaType>absent());

        ImmutableSet<OperationResult<DecodedTag, String>> results
                = ImmutableSet.<OperationResult<DecodedTag, String>>builder()
                .add(configureGetDecodedTag("0[1]/0[0]/1[1]",
                        "/Document/header/published/date",
                        true,
                        AsnBuiltinType.GeneralizedTime,
                        instance,
                        publishDate))
                .add(configureGetDecodedTag("1[2]/0[0]/0[0]",
                        "/Document/body/lastModified/date",
                        true,
                        AsnBuiltinType.GeneralizedTime,
                        instance,
                        lastModifiedDate))
                .add(configureGetDecodedTag("1[2]/1[1]/0[1]",
                        "/Document/body/prefix/text",
                        true,
                        AsnBuiltinType.Utf8String,
                        instance,
                        "prefix text"))
                .add(configureGetDecodedTag("1[2]/2[2]/0[1]",
                        "/Document/body/content/text",
                        true,
                        AsnBuiltinType.Utf8String,
                        instance,
                        "content text"))
                .add(configureGetDecodedTag("2[3]/0[0]/0[UNIVERSAL 16]/0[1]",
                        "/Document/footer/authors[0]/firstName",
                        true,
                        AsnBuiltinType.Utf8String,
                        instance,
                        "firstName"))
                .add(configureGetDecodedTag("1[2]/0[0]/0[99]",
                        "/Document/body/content/0[99]",
                        false,
                        null,
                        instance,
                        ""))
                .add(configureGetDecodedTag("0[99]/0[1]/0[1]",
                        "/Document/0[99]/0[1]/0[1]",
                        false,
                        null,
                        instance,
                        ""))
                .build();

        String topLevelTypeName = "Document";
        when(instance.getDecodedTags(ImmutableSet.copyOf(rawTags), topLevelTypeName)).thenReturn(
                results);

        ImmutableSet<String> emptyTags = ImmutableSet.of();
        ImmutableSet<OperationResult<DecodedTag, String>> emptyResults = ImmutableSet.of();

        when(instance.getDecodedTags(emptyTags, topLevelTypeName)).thenReturn(emptyResults);

        return instance;
    }

    /**
     * Returns (a copy) the timestamps that the mocked "/Document/header/published/date" will
     * provide with calls to {@link AsnData#getDecodedObject}
     *
     * @return (a copy) the timestamps that the mocked "/Document/header/published/date" will
     * provide with calls to {@link AsnData#getDecodedObject}
     */
    public static Timestamp getPublishDate()
    {
        return new Timestamp(publishDate.getTime());
    }

    /**
     * Returns (a copy) the timestamps that the mocked "/Document/body/lastModified/date" will
     * provide with calls to {@link AsnData#getDecodedObject}
     *
     * @return (a copy) the timestamps that the mocked "/Document/body/lastModified/date" will
     * provide with calls to {@link AsnData#getDecodedObject}
     */
    public static Timestamp getLastModifiedDate()
    {
        return new Timestamp(lastModifiedDate.getTime());
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: NonEmptyByteArrayMatcher
    // -------------------------------------------------------------------------

    /**
     * Configures the {@code getDecodedTag()} method on the supplied instance to use the mocked
     * values supplied
     *
     * @param rawTag
     *         the raw tag to return
     * @param decodedTagPath
     *         the value to return for {@link DecodedTag#getTag()}
     * @param isFullyDecoded
     *         the value to return for {@link OperationResult#wasSuccessful()}
     * @param builtinType
     *         the value to return for {@link AsnSchemaType#getBuiltinType()}
     * @param decodedValue
     *         the value to return from decoding any input bytes
     */
    private static <T> OperationResult<DecodedTag, String> configureGetDecodedTag(String rawTag,
            String decodedTagPath, boolean isFullyDecoded, AsnBuiltinType builtinType,
            AsnSchema schema, T decodedValue) throws DecodeException
    {
        final DecodedTag decodedTag = mock(DecodedTag.class);

        final AsnSchemaType type = mock(AsnSchemaType.class);

        final AsnPrimitiveType primitiveType = mock(AsnPrimitiveType.class);
        final BuiltinTypeDecoder decoder = mock(BuiltinTypeDecoder.class);
        when(decoder.decodeAsString(anyString(), any(AsnData.class))).thenReturn(decodedValue
                .toString());
        when(decoder.decode(anyString(), any(AsnData.class))).thenReturn(decodedValue);

        when(primitiveType.accept(any(AsnPrimitiveTypeVisitor.class))).thenReturn(decoder);

        when(type.getPrimitiveType()).thenReturn(primitiveType);
        when(type.getBuiltinType()).thenReturn(builtinType);
        when(decodedTag.getType()).thenReturn(type);

        when(decodedTag.getTag()).thenReturn(decodedTagPath);
        when(decodedTag.getRawTag()).thenReturn(rawTag);

        when(decodedTag.isFullyDecoded()).thenReturn(isFullyDecoded);

        @SuppressWarnings("unchecked")
        OperationResult<DecodedTag, String> result = mock(OperationResult.class);

        when(result.getOutput()).thenReturn(decodedTag);
        when(result.wasSuccessful()).thenReturn(isFullyDecoded);
        when(result.getFailureReason()).thenReturn(Optional.of("mock failure reason"));

        if (isFullyDecoded)
        {
            when(schema.getType(eq(decodedTagPath))).thenReturn(Optional.of(type));
        }

        rawTags.add(rawTag);

        return result;
    }
}
