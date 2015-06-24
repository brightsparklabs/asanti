/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaModule;
import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.AsnSchemaModule;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.ParseException;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AsnSchemaModuleParser}
 *
 * @author brightSPARK Labs
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ AsnSchemaImportsParser.class, AsnSchemaTypeDefinitionParser.class })
public class AsnSchemaModuleParserTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** an invalid schema (missing a BEGIN tag) * */
    private static final Iterable<String> TEST_MODULE_NO_BEGIN = Lists.newArrayList(
            "People-Protocol",
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }",
            "DEFINITIONS",
            "AUTOMATIC TAGS ::=",
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

    /** an invalid schema (unknown content after export/imports statements) * */
    private static final Iterable<String> TEST_MODULE_UNKNOWN_CONTENT = Lists.newArrayList(
            "People-Protocol",
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }",
            "DEFINITIONS",
            "AUTOMATIC TAGS ::=",
            "BEGIN",
            "THIS IS UNKNOWN CONTENT",
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

    /** an invalid schema (unknown content after top level type) * */
    private static final Iterable<String> TEST_MODULE_UNKNOWN_CONTENT_AFTER_TOP_LEVEL_TYPE
            = Lists.newArrayList("People-Protocol",
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }",
            "DEFINITIONS",
            "AUTOMATIC TAGS ::=",
            "BEGIN",
            "People ::= SET OF Person",
            "::=",
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

    /** an invalid IMPORTS statement (missing semicolon) * */
    private static final Iterable<String> TEST_MODULE_INVALID_IMPORTS = Lists.newArrayList(
            "Document-PDU",
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) document(1) }",
            "DEFINITIONS",
            "AUTOMATIC TAGS ::=",
            "BEGIN",
            "IMPORTS",
            "Person FROM People-Protocol { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }",
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

    /** an invalid EXPORTS statement (missing semicolon) * */
    private static final Iterable<String> TEST_MODULE_INVALID_EXPORTS = Lists.newArrayList(
            "Document-PDU",
            "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) document(1) }",
            "DEFINITIONS",
            "AUTOMATIC TAGS ::=",
            "BEGIN",
            "EXPORTS Header, Body",
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

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testParse_NullOrEmpty() throws Exception
    {
        // test empty file
        try
        {
            AsnSchemaModuleParser.parse(Lists.<String>newArrayList());
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test null argument
        try
        {
            AsnSchemaModuleParser.parse(null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
    }

    @Test
    public void testParse_Exceptions() throws Exception
    {
        // test module with no BEGIN keyword
        try
        {
            AsnSchemaModuleParser.parse(TEST_MODULE_NO_BEGIN);
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test module with unknown content
        try
        {
            AsnSchemaModuleParser.parse(TEST_MODULE_UNKNOWN_CONTENT);
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
/* TODO ASN-139 should do something like this, but more thorough.  May need different mocks.
        // test module with unknown content after top level type
        try
        {
            // need to mock AsnSchemaTypeDefinitionParser.parse static method for the
            // People type definition
            AsnSchemaTypeDefinition mockedPeopleTypeDefinition = MockAsnSchemaTypeDefinition
                    .builder("People", AsnBuiltinType.SetOf)
                    .build();

            PowerMockito.mockStatic(AsnSchemaTypeDefinitionParser.class);
            when(AsnSchemaTypeDefinitionParser.parse("People", "SET OF Person")).thenReturn(
                    mockedPeopleTypeDefinition);

            AsnSchemaModuleParser.parse(TEST_MODULE_UNKNOWN_CONTENT_AFTER_TOP_LEVEL_TYPE);
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
*/
        // test module with invalid IMPORTS statement
        try
        {
            AsnSchemaModuleParser.parse(TEST_MODULE_INVALID_IMPORTS);
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test module with invalid EXPORTS statement
        try
        {
            AsnSchemaModuleParser.parse(TEST_MODULE_INVALID_EXPORTS);
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
    }

    @Test
    public void testParse_DocumentPdu() throws Exception
    {
        final String expectedImportsStatement
                = "Person FROM People-Protocol { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) } ; ";

        // prepare mocked imports map for mocked AsnSchemaImportsParser.parse method
        final Map<String, String> mockedImportsMap = Maps.<String, String>newHashMap();
        mockedImportsMap.put("Person", "People-Protocol");
        final ImmutableMap<String, String> mockedImportsImmutableMap = ImmutableMap.copyOf(
                mockedImportsMap);

        // mock AsnSchemaImportsParser.parse static method to only return map
        // if argument matches expected input
        PowerMockito.mockStatic(AsnSchemaImportsParser.class);
        when(AsnSchemaImportsParser.parse(expectedImportsStatement)).thenReturn(
                mockedImportsImmutableMap);

        // prepare mocked AsnSchemaTypeDefinitions for Document PDU module
        final ImmutableList<AsnSchemaTypeDefinition> mockedAsnSchemaTypeDefinitions
                = MockAsnSchemaTypeDefinition.createMockedAsnSchemaTypeDefinitionsForDocumentPdu();

        // mock AsnSchemaTypeDefinitionParser.parse static method
        // return mocked OLDAsnSchemaTypeDefinition instances when expected inputs are received
        PowerMockito.mockStatic(AsnSchemaTypeDefinitionParser.class);
        when(AsnSchemaTypeDefinitionParser.parse("Document",
                "SEQUENCE { header [1] Header, body [2] Body, footer [3] Footer, dueDate [4] Date-Due, version [5] SEQUENCE { majorVersion [0] INTEGER, minorVersion [1] INTEGER }, description [6] SET { numberLines [0] INTEGER, summary [1] OCTET STRING } OPTIONAL }"))
                .thenReturn(mockedAsnSchemaTypeDefinitions.get(0));

        when(AsnSchemaTypeDefinitionParser.parse("Header",
                "SEQUENCE { published [0] PublishedMetadata }")).thenReturn(mockedAsnSchemaTypeDefinitions.get(1));

        when(AsnSchemaTypeDefinitionParser.parse("Body",
                "SEQUENCE { lastModified [0] ModificationMetadata, prefix [1] Section-Note OPTIONAL, content [2] Section-Main, suffix [3] Section-Note OPTIONAL }"))
                .thenReturn(mockedAsnSchemaTypeDefinitions.get(2));

        when(AsnSchemaTypeDefinitionParser.parse("Footer",
                "SEQUENCE { author [0] Person }")).thenReturn(mockedAsnSchemaTypeDefinitions.get(3));

        when(AsnSchemaTypeDefinitionParser.parse("PublishedMetadata",
                "SEQUENCE { date [1] GeneralizedTime, country [2] OCTET STRING OPTIONAL }")).thenReturn(
                mockedAsnSchemaTypeDefinitions.get(4));

        when(AsnSchemaTypeDefinitionParser.parse("ModificationMetadata",
                "SEQUENCE { date [0] Date, modifiedBy [1] Person }")).thenReturn(
                mockedAsnSchemaTypeDefinitions.get(5));

        when(AsnSchemaTypeDefinitionParser.parse("Section-Note",
                "SEQUENCE { text [1] OCTET STRING }")).thenReturn(
                mockedAsnSchemaTypeDefinitions.get(6));

        when(AsnSchemaTypeDefinitionParser.parse("Section-Main",
                "SEQUENCE { text [1] OCTET STRING OPTIONAL, paragraphs [2] SEQUENCE OF Paragraph, sections [3] SET OF SET { number [1] INTEGER, text [2] OCTET STRING } }"))
                .thenReturn(mockedAsnSchemaTypeDefinitions.get(7));

        when(AsnSchemaTypeDefinitionParser.parse("Paragraph",
                "SEQUENCE { title [1] OCTET STRING, contributor [2] Person OPTIONAL, points [3] SEQUENCE OF OCTET STRING }"))
                .thenReturn(mockedAsnSchemaTypeDefinitions.get(8));

        when(AsnSchemaTypeDefinitionParser.parse("References",
                "SEQUENCE (SIZE (1..50)) OF SEQUENCE { title [1] OCTET STRING, url [2] OCTET STRING }"))
                .thenReturn(mockedAsnSchemaTypeDefinitions.get(9));

        when(AsnSchemaTypeDefinitionParser.parse("Date-Due",
                "INTEGER { tomorrow(0), three-day(1), week(2) } DEFAULT week")).thenReturn(
                mockedAsnSchemaTypeDefinitions.get(10));

        final AsnSchemaModule.Builder actualModule
                = AsnSchemaModuleParser.parse(MockAsnSchemaModule.TEST_MODULE_DOCUMENT_PDU);
        assertNotNull(actualModule);
    }

    @Test
    public void testParse_PeopleProtocol() throws Exception
    {
/* TODO ASN-138
        // prepare mocked AsnSchemaTypeDefinitions for People Protocol module
        final ImmutableList<AbstractOLDAsnSchemaTypeDefinition> mockedAsnSchemaTypeDefinitions
                = MockAsnSchemaTypeDefinition.createMockedAsnSchemaTypeDefinitionsForPeopleProtocol();

        // mock AsnSchemaTypeDefinitionParser.parse static method
        // return mocked OLDAsnSchemaTypeDefinition instances when expected inputs are received
        PowerMockito.mockStatic(AsnSchemaTypeDefinitionParser.class);
        when(AsnSchemaTypeDefinitionParser.parse("People", "SET OF Person")).thenReturn(
                ImmutableList.<OLDAsnSchemaTypeDefinition>of(mockedAsnSchemaTypeDefinitions.get(0)));

        when(AsnSchemaTypeDefinitionParser.parse("Person",
                "SEQUENCE { firstName [1] OCTET STRING, lastName [2] OCTET STRING, title [3] ENUMERATED { mr, mrs, ms, dr, rev } OPTIONAL, gender Gender OPTIONAL, maritalStatus CHOICE { Married [0], Single [1] } }"))
                .thenReturn(ImmutableList.<OLDAsnSchemaTypeDefinition>of(mockedAsnSchemaTypeDefinitions
                        .get(1)));

        when(AsnSchemaTypeDefinitionParser.parse("Gender",
                "ENUMERATED { male(0), female(1) }")).thenReturn(ImmutableList.<OLDAsnSchemaTypeDefinition>of(
                mockedAsnSchemaTypeDefinitions.get(2)));

        final AsnSchemaModule actualModule
                = AsnSchemaModuleParser.parse(MockAsnSchemaModule.TEST_MODULE_PEOPLE_PROTOCOL);
        assertNotNull(actualModule);
*/
    }
}
