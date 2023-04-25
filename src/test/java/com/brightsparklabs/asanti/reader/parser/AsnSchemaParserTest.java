/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.reader.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.AsnSchemaModule;
import com.google.common.base.Splitter;
import java.text.ParseException;
import java.util.List;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * Unit tests for {@link AsnSchemaParser}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaParserTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /**
     * number of lines expected after pre-parsing the Document-PDU module from the example schema
     * This takes into account that all lines ending in comma are joined.
     */
    private static final int EXPECTED_DOCUMENT_PDU_MODULE_LINE_COUNT = 34;

    /**
     * number of lines expected after pre-parsing the People-Protocol module from the example
     * schema. This takes into account that all lines ending in comma are joined.
     */
    private static final int EXPECTED_PEOPLE_PROTOCOL_MODULE_LINE_COUNT = 18;

    /** an invalid schema (missing an END tag) */
    private static final String SCHEMA_NO_END =
            """
                    People-Protocol
                        { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }

                    DEFINITIONS
                        AUTOMATIC TAGS ::=

                    BEGIN
                        /*Person ::= SEQUENCE
                        {
                            firstName [1] OCTET STRING,
                            lastName  [2] OCTET STRING,
                            title     [3] ENUMERATED
                                { mr, mrs, ms, dr, rev } OPTIONAL,
                            gender OPTIONAL
                        }*/

                        Gender ::= ENUMERATED
                        {
                            male(0),
                            female(1)
                        }
                    """;

    /** a valid schema to check all regex matches */
    private static final String SCHEMA_FOR_REGEX_TEST =
            """
                    /*Document-PDU\r
                        { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) document(1) }

                    DEFINITIONS
                        AUTOMATIC TAGS ::=

                    IMPORTS
                      Gender
                        FROM People-Protocol
                        { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }

                    *//*********************************
                    BEGIN

                        Document ::= SEQUENCE
                        {
                            header [1] Header,
                            body   [2] Body,
                            footer [3] Footer
                        }
                    END
                    *********************************/
                    People-Protocol

                        { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }

                    DEFINITIONS
                        AUTOMATIC TAGS ::=

                    IMPORTS
                      Hair-type
                        FROM Hair-Protocol
                        { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) hair(2) }; BEGIN

                        /*Person ::= SEQUENCE
                        {
                            firstName [1] OCTET STRING,
                    \t\t\t -- this is a comment --
                            lastName  [2] OCTET STRING,
                            title     [3] ENUMERATED
                                { mr, mrs, ms, dr, rev } OPTIONAL,
                            gender OPTIONAL
                    \t\t\t -- this is a
                    \t\t\t -- multiline comment --
                        }*/

                        Gender ::= ENUMERATED
                        {
                            male(0),
                            female(1)
                        }
                    END

                    /*Hair-Protocol
                    *    { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) hair(2) }
                    *
                    *DEFINITIONS
                    *    AUTOMATIC TAGS ::=
                    *
                    *BEGIN
                    *    Hair ::= SEQUENCE
                    *    {
                    *        color [1] OCTET STRING,
                    *        type  [2] Hair-type
                    *    }
                    *
                    *    Hair-type ::= ENUMERATED
                    *    {
                    *        straight(0),
                    *        curly(1)
                    *    }
                    *END*/""";

    /** The text expected after applying regex matches to {@link #SCHEMA_FOR_REGEX_TEST} */
    private static final String SCHEMA_VALID_PARSE_INPUT =
            """
                    People-Protocol
                    { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }
                    DEFINITIONS
                    AUTOMATIC TAGS ::=
                    IMPORTS
                    Hair-type
                    FROM Hair-Protocol
                    { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) hair(2) }
                    ;
                    BEGIN
                    Gender ::= ENUMERATED
                    {
                    male(0), female(1)
                    }
                    END""";
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testParse_NullOrEmpty() throws Exception {
        // test empty file
        try {
            AsnSchemaParser.parse("");
            fail("ParseException not thrown");
        } catch (final ParseException ex) {
            // Expected to get here.
        }

        // test null argument
        try {
            AsnSchemaParser.parse(null);
            fail("ParseException not thrown");
        } catch (final ParseException ex) {
            // Expected to get here.
        }
    }

    @Test
    public void testParse_Exceptions() throws Exception {
        // test schema with no End keyword
        try {
            AsnSchemaParser.parse(SCHEMA_NO_END);
            fail("ParseException not thrown");
        } catch (final ParseException ex) {
            // Expected to get here.
        }
    }

    //    @Test
    //    public void testParse() throws Exception {
    //        // prepare expected output to AsnSchemaModuleParser.parse()
    //        final AsnSchemaModule.Builder expectedDocumentPduSchemaModule =
    //                MockAsnSchemaModule.createMockedAsnSchemaModuleForDocumentPdu();
    //        final AsnSchemaModule.Builder expectedPeopleProtocolSchemaModule =
    //                MockAsnSchemaModule.createMockedAsnSchemaModuleForPeopleProtocol();
    //
    //        // mock AsnSchemaModuleParser.parse() static method
    //        Mockito.mockStatic(AsnSchemaModuleParser.class);
    //        final Matcher<Iterable<String>> documentPduMatcher =
    //
    // org.hamcrest.Matchers.iterableWithSize(EXPECTED_DOCUMENT_PDU_MODULE_LINE_COUNT);
    //        when(AsnSchemaModuleParser.parse(MockitoHamcrest.argThat(documentPduMatcher)))
    //                .thenReturn(expectedDocumentPduSchemaModule);
    //        final Matcher<Iterable<String>> peopleProtocolMatcher =
    //
    // org.hamcrest.Matchers.iterableWithSize(EXPECTED_PEOPLE_PROTOCOL_MODULE_LINE_COUNT);
    //        when(AsnSchemaModuleParser.parse(MockitoHamcrest.argThat(peopleProtocolMatcher)))
    //                .thenReturn(expectedPeopleProtocolSchemaModule);
    //
    //        // parse the example schema
    //        final AsnSchema actualSchema = AsnSchemaParser.parse(MockAsnSchema.TEST_SCHEMA_TEXT);
    //        assertNotNull(actualSchema);
    //    }

    @Test
    public void testParse_BlockComments() throws Exception {
        // prepare objects for mocking of AsnSchemaModuleParser.parse static method
        // set up mock AsnSchemaModule to return
        final AsnSchemaModule.Builder mockAsnSchemaModule =
                AsnSchemaModule.builder().setName("People-Protocol");

        // prepare expected input to the AsnSchemaModuleParser.parse static method for
        // valid block comments
        final Splitter newLineSplitter = Splitter.on("\n").omitEmptyStrings();
        final List<String> expectedParseInput =
                newLineSplitter.splitToList(SCHEMA_VALID_PARSE_INPUT);

        // mock AsnSchemaModuleParser.parse static method to only return mock object
        // if argument matches expected input
        try (MockedStatic<AsnSchemaModuleParser> parser =
                Mockito.mockStatic(AsnSchemaModuleParser.class)) {
            parser.when(() -> AsnSchemaModuleParser.parse(expectedParseInput))
                    .thenReturn(mockAsnSchemaModule);

            // test parse with a schema which has all regex combinations
            final AsnSchema actualSchema = AsnSchemaParser.parse(SCHEMA_FOR_REGEX_TEST);
            assertNotNull(actualSchema);
        }
    }

    @Test
    public void testParse_EndLineComments() throws Exception {
        // test added in response to bug where comments that are ended with the new line removed the
        // new line.  Sometimes the newline matter, eg when at the "root" level, the newline is the
        // separator between different type defs or value assignments.
        // ASN-184
        final String withComments =
                """
                        People-Protocol
                        { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }
                        DEFINITIONS
                        AUTOMATIC TAGS ::=
                        IMPORTS
                        ;
                        BEGIN
                        Gender ::= ENUMERATED
                        {
                        male(0),
                        female(1)
                        }
                        SomeType ::= INTEGER (0..100) -- this comment ends at the end of this line
                        SomeChoice ::= CHOICE
                        {
                        optA INTEGER,
                        optB OCTET STRING
                        }
                        END""";
        // "END -- comment at the end of file, note no newline, we need this comment to be cleaned
        // out";

        final String cleaned =
                """
                        People-Protocol
                        { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }
                        DEFINITIONS
                        AUTOMATIC TAGS ::=
                        IMPORTS
                        ;
                        BEGIN
                        Gender ::= ENUMERATED
                        {
                        male(0), female(1)
                        }
                        SomeType ::= INTEGER (0..100)
                        SomeChoice ::= CHOICE
                        {
                        optA INTEGER, optB OCTET STRING
                        }
                        END""";

        // prepare objects for mocking of AsnSchemaModuleParser.parse static method
        // set up mock AsnSchemaModule to return
        final AsnSchemaModule.Builder mockAsnSchemaModule =
                AsnSchemaModule.builder().setName("People-Protocol");

        // prepare expected input to the AsnSchemaModuleParser.parse static method for
        // valid block comments
        final Splitter newLineSplitter = Splitter.on("\n").omitEmptyStrings();
        final List<String> expectedParseInput = newLineSplitter.splitToList(cleaned);

        // mock AsnSchemaModuleParser.parse static method to only return mock object
        // if argument matches expected input
        try (MockedStatic<AsnSchemaModuleParser> parser =
                Mockito.mockStatic(AsnSchemaModuleParser.class)) {
            parser.when(() -> AsnSchemaModuleParser.parse(expectedParseInput))
                    .thenReturn(mockAsnSchemaModule);

            // This test is really testing that the preparation of the schema (ie removing comments
            // etc)
            // produces the expected results.

            // test parse with a schema which has all regex combinations
            final AsnSchema actualSchema = AsnSchemaParser.parse(withComments);
            assertNotNull(actualSchema);
        }
    }
}
