/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.ParseException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AsnSchemaTypeDefinitionParser}
 *
 * @author brightSPARK Labs
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AsnSchemaTypeParser.class)
public class AnSchemaTypeDefinitionParserTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** an argument capture helper for AsnSchemaTypeParser */
    private ArgumentCaptor<String> parserArgument;


    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @Before
    public void setUpBeforeTest() throws Exception
    {
        // Mockito does a tear down after each test, so we can't do this as a BeforeClass

        // Setup the mock child parser.
        // We do want to test that the child parser gets passed the correct information,
        // so we setup the argument captors.

        // mock AsnSchemaConstraintParser.parse static method
        PowerMockito.mockStatic(AsnSchemaTypeParser.class);
        // we want to capture the parserArgument that gets passed to the AsnSchemaTypeParser
        parserArgument = ArgumentCaptor.forClass(String.class);
        when(AsnSchemaTypeParser.parse(parserArgument.capture())).thenReturn(AsnSchemaType.NULL);
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testParseErrors() throws Exception
    {
        // null name
        try
        {
            AsnSchemaTypeDefinitionParser.parse(null, "SomeType");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // blank name
        try
        {
            AsnSchemaTypeDefinitionParser.parse("", "SomeType");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
        try
        {
            AsnSchemaTypeDefinitionParser.parse(" ", "SomeType");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // blank value
        try
        {
            AsnSchemaTypeDefinitionParser.parse("TEST_NAME", "");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
        // nulll value
        try
        {
            AsnSchemaTypeDefinitionParser.parse("TEST_NAME", null);
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

    }

    @Test
    public void testParse() throws Exception
    {
        final AsnSchemaTypeDefinition result = AsnSchemaTypeDefinitionParser.parse(
                "TEST_NAME",
                "IA5String (SIZE (10))");

        assertEquals("TEST_NAME", result.getName());
        assertEquals("IA5String (SIZE (10))", parserArgument.getValue());
    }

    @Test
    public void testArgumentPassing() throws Exception
    {
        final String toParser = "It does not matter what the content, the AsnSchemaTypeDefinitionParser " +
                                "should just pass it straight through to the parser.\n" +
                                "That is where all the real parsing is done.";

        final AsnSchemaTypeDefinition result = AsnSchemaTypeDefinitionParser.parse(
                "TEST_NAME", toParser);

        assertEquals("TEST_NAME", result.getName());
        assertEquals(toParser, parserArgument.getValue());
    }

}
