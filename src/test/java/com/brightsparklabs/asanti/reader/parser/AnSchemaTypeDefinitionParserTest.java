/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.reader.parser;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import java.text.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Unit tests for {@link AsnSchemaTypeDefinitionParser}
 *
 * @author brightSPARK Labs
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AsnSchemaTypeParser.class)
public class AnSchemaTypeDefinitionParserTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** an argument capture helper for AsnSchemaTypeParser */
    private ArgumentCaptor<String> parserArgument;

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @Before
    public void setUpBeforeTest() throws Exception {
        // Mockito does a tear down after each test, so we can't do this as a BeforeClass

        // Setup the mock child parser.
        // We do want to test that the child parser gets passed the correct information,
        // so we setup the argument captors.

        // mock AsnSchemaConstraintParser.parse static method
        PowerMockito.mockStatic(AsnSchemaTypeParser.class);
        // we want to capture the parserArgument that gets passed to the AsnSchemaTypeParser
        parserArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<AsnModuleTaggingMode> taggingMode =
                ArgumentCaptor.forClass(AsnModuleTaggingMode.class);
        when(AsnSchemaTypeParser.parse(parserArgument.capture(), taggingMode.capture()))
                .thenReturn(AsnSchemaType.NULL);
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testParseErrors() throws Exception {
        // null name
        try {
            AsnSchemaTypeDefinitionParser.parse(null, "SomeType", AsnModuleTaggingMode.DEFAULT);
            fail("ParseException not thrown");
        } catch (final ParseException ex) {
        }

        // blank name
        try {
            AsnSchemaTypeDefinitionParser.parse("", "SomeType", AsnModuleTaggingMode.DEFAULT);
            fail("ParseException not thrown");
        } catch (final ParseException ex) {
        }
        try {
            AsnSchemaTypeDefinitionParser.parse(" ", "SomeType", AsnModuleTaggingMode.DEFAULT);
            fail("ParseException not thrown");
        } catch (final ParseException ex) {
        }

        // blank value
        try {
            AsnSchemaTypeDefinitionParser.parse("TEST_NAME", "", AsnModuleTaggingMode.DEFAULT);
            fail("ParseException not thrown");
        } catch (final ParseException ex) {
        }
        // nulll value
        try {
            AsnSchemaTypeDefinitionParser.parse("TEST_NAME", null, AsnModuleTaggingMode.DEFAULT);
            fail("ParseException not thrown");
        } catch (final ParseException ex) {
        }
        // nulll tagging mode
        try {
            AsnSchemaTypeDefinitionParser.parse("TEST_NAME", "SomeType", null);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }
    }

    @Test
    public void testParse() throws Exception {
        final AsnSchemaTypeDefinition result =
                AsnSchemaTypeDefinitionParser.parse(
                        "TEST_NAME", "IA5String (SIZE (10))", AsnModuleTaggingMode.DEFAULT);

        assertEquals("TEST_NAME", result.getName());
        assertEquals("IA5String (SIZE (10))", parserArgument.getValue());
    }

    @Test
    public void testArgumentPassing() throws Exception {
        final String toParser =
                "It does not matter what the content, the AsnSchemaTypeDefinitionParser "
                        + "should just pass it straight through to the parser.\n"
                        + "That is where all the real parsing is done.";

        final AsnSchemaTypeDefinition result =
                AsnSchemaTypeDefinitionParser.parse(
                        "TEST_NAME", toParser, AsnModuleTaggingMode.DEFAULT);

        assertEquals("TEST_NAME", result.getName());
        assertEquals(toParser, parserArgument.getValue());
    }
}
