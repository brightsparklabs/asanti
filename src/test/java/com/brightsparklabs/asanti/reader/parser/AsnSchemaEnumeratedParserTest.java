/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.reader.parser;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Test;

import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaEnumeratedOption;
import com.google.common.collect.ImmutableList;

/**
 * Unit tests for {@link AsnSchemaEnumeratedParser}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaEnumeratedParserTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testParse_NullOrEmpty() throws Exception
    {
        // test null
        ImmutableList<AsnSchemaEnumeratedOption> options = AsnSchemaEnumeratedParser.parse(null);
        assertEquals(0, options.size());

        // test empty
        options = AsnSchemaEnumeratedParser.parse("");
        assertEquals(0, options.size());
        options = AsnSchemaEnumeratedParser.parse(" \t\r\n");
        assertEquals(0, options.size());
        options = AsnSchemaEnumeratedParser.parse(" ,\t,\r\n");
        assertEquals(0, options.size());
        options = AsnSchemaEnumeratedParser.parse(",,,");
        assertEquals(0, options.size());
    }

    @Test
    public void testParse_Exceptions() throws Exception
    {
        // test no tag names
        try
        {
            AsnSchemaEnumeratedParser.parse("(0)\t, (1),  (2) ,\n\t  \t\r\n(3),(4)");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test missing tag names
        try
        {
            AsnSchemaEnumeratedParser.parse("mr(0)\t, mrs(1),  (2) ,\n\t dr \t\r\n(3),(4)");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
    }

    @Test
    public void testParse() throws Exception
    {
        // test valid
        ImmutableList<AsnSchemaEnumeratedOption> options =
                AsnSchemaEnumeratedParser.parse("mr(0)\t, mrs(1), ms (2) ,\n\t dr \t\r\n(3),rev(4)");
        assertEquals(5, options.size());
        testOption(options.get(0), "0", "mr");
        testOption(options.get(1), "1", "mrs");
        testOption(options.get(2), "2", "ms");
        testOption(options.get(3), "3", "dr");
        testOption(options.get(4), "4", "rev");

        // test no tags
        options = AsnSchemaEnumeratedParser.parse("mr\t, mrs, ms  ,\n\t dr \t\r\n,rev");
        assertEquals(5, options.size());
        testOption(options.get(0), "", "mr");
        testOption(options.get(1), "", "mrs");
        testOption(options.get(2), "", "ms");
        testOption(options.get(3), "", "dr");
        testOption(options.get(4), "", "rev");

        // test missing tags
        options = AsnSchemaEnumeratedParser.parse("mr(0)\t, mrs, ms (2) ,\n\t dr \t\r\n,rev(4)");
        assertEquals(5, options.size());
        testOption(options.get(0), "0", "mr");
        testOption(options.get(1), "", "mrs");
        testOption(options.get(2), "2", "ms");
        testOption(options.get(3), "", "dr");
        testOption(options.get(4), "4", "rev");

        // test unordered tags
        options = AsnSchemaEnumeratedParser.parse("mrs(1),rev(4), ms (2) ,mr(0)\t, \n\t dr \t\r\n(3)");
        assertEquals(5, options.size());
        testOption(options.get(0), "1", "mrs");
        testOption(options.get(1), "4", "rev");
        testOption(options.get(2), "2", "ms");
        testOption(options.get(3), "0", "mr");
        testOption(options.get(4), "3", "dr");
    }

    // -------------------------------------------------------------------------
    // UTLITY METHODS
    // -------------------------------------------------------------------------

    /**
     * Tests that the supplied option against an expected tag and tag name
     *
     * @param option
     *            option to test
     *
     * @param expectedTag
     *            expected tag for the option
     *
     * @param expectedTagName
     *            expected tag name for the option
     */
    private void testOption(AsnSchemaEnumeratedOption option, String expectedTag, String expectedTagName)
    {
        assertEquals(expectedTag, option.getTag());
        assertEquals(expectedTagName, option.getTagName());
    }
}
