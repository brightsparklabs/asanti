/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.reader.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import java.text.ParseException;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

/**
 * Unit tests for {@link AsnSchemaTypeDefinitionParser}
 *
 * @author brightSPARK Labs
 */
public class AnSchemaTypeDefinitionParserTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    private void parseViaMock(String name, String value, AsnModuleTaggingMode mode)
            throws ParseException {
        /* We care about what gets sent down to the underlying parser (AsnSchemaConstraintParser).
         * So we need to mock it and capture its input arguments.
         * The try-with-resources is needed since `AsnSchemaConstraintParser.parse` is static.
         */
        try (var parser = Mockito.mockStatic(AsnSchemaTypeParser.class)) {
            // we want to capture the parserArgument that gets passed to the AsnSchemaTypeParser
            var parserArgument = ArgumentCaptor.forClass(String.class);
            ArgumentCaptor<AsnModuleTaggingMode> taggingMode =
                    ArgumentCaptor.forClass(AsnModuleTaggingMode.class);

            when(AsnSchemaTypeParser.parse(parserArgument.capture(), taggingMode.capture()))
                    .thenReturn(AsnSchemaType.NULL);

            final AsnSchemaTypeDefinition result =
                    AsnSchemaTypeDefinitionParser.parse(name, value, mode);

            assertEquals(name, result.getName());
            assertEquals(value, parserArgument.getValue());
        }
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
            // Expected to get here.
        }

        // blank name
        try {
            AsnSchemaTypeDefinitionParser.parse("", "SomeType", AsnModuleTaggingMode.DEFAULT);
            fail("ParseException not thrown");
        } catch (final ParseException ex) {
            // Expected to get here.
        }
        try {
            AsnSchemaTypeDefinitionParser.parse(" ", "SomeType", AsnModuleTaggingMode.DEFAULT);
            fail("ParseException not thrown");
        } catch (final ParseException ex) {
            // Expected to get here.
        }

        // blank value
        try {
            AsnSchemaTypeDefinitionParser.parse("TEST_NAME", "", AsnModuleTaggingMode.DEFAULT);
            fail("ParseException not thrown");
        } catch (final ParseException ex) {
            // Expected to get here.
        }
        // nulll value
        try {
            AsnSchemaTypeDefinitionParser.parse("TEST_NAME", null, AsnModuleTaggingMode.DEFAULT);
            fail("ParseException not thrown");
        } catch (final ParseException ex) {
            // Expected to get here.
        }
        // nulll tagging mode
        try {
            AsnSchemaTypeDefinitionParser.parse("TEST_NAME", "SomeType", null);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
            // Expected to get here.
        }
    }

    @Test
    public void testParse() throws Exception {
        parseViaMock("TEST_NAME", "IA5String (SIZE (10))", AsnModuleTaggingMode.DEFAULT);
    }

    @Test
    public void testArgumentPassing() throws Exception {
        final String toParser =
                "It does not matter what the content, the AsnSchemaTypeDefinitionParser "
                        + "should just pass it straight through to the parser.\n"
                        + "That is where all the real parsing is done.";

        parseViaMock("TEST_NAME", toParser, AsnModuleTaggingMode.DEFAULT);
    }
}
