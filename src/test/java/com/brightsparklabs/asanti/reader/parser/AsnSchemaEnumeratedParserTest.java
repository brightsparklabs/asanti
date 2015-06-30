/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.reader.parser;

import java.text.ParseException;

import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link AsnSchemaNamedTagParser}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaEnumeratedParserTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testParseEnumeratedOptions_NullOrEmpty() throws Exception
    {
        // test null
        ImmutableList<AsnSchemaNamedTag> options = AsnSchemaNamedTagParser.parseEnumeratedOptions(null);
        assertEquals(0, options.size());

        // test empty
        options = AsnSchemaNamedTagParser.parseEnumeratedOptions("");
        assertEquals(0, options.size());
        options = AsnSchemaNamedTagParser.parseEnumeratedOptions(" \t\r\n");
        assertEquals(0, options.size());
        options = AsnSchemaNamedTagParser.parseEnumeratedOptions(" ,\t,\r\n");
        assertEquals(0, options.size());
        options = AsnSchemaNamedTagParser.parseEnumeratedOptions(",,,");
        assertEquals(0, options.size());
    }

    @Test
    public void testParseEnumeratedOption_Exceptions() throws Exception
    {
        // test no tag names
        try
        {
            AsnSchemaNamedTagParser.parseEnumeratedOptions("(0)\t, (1),  (2) ,\n\t  \t\r\n(3),(4)");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test missing tag names
        try
        {
            AsnSchemaNamedTagParser.parseEnumeratedOptions("mr(0)\t, mrs(1),  (2) ,\n\t dr \t\r\n(3),(4)");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
    }

    @Test
    public void testParseEnumeratedOptions() throws Exception
    {
        // test valid
        ImmutableList<AsnSchemaNamedTag> options =
                AsnSchemaNamedTagParser.parseEnumeratedOptions("mr(0)\t, mrs(1), ms (2) ,\n\t dr \t\r\n(3),rev(4)");
        assertEquals(5, options.size());
        testNamedTag(options.get(0), "0", "mr");
        testNamedTag(options.get(1), "1", "mrs");
        testNamedTag(options.get(2), "2", "ms");
        testNamedTag(options.get(3), "3", "dr");
        testNamedTag(options.get(4), "4", "rev");

        // test unordered tags
        options = AsnSchemaNamedTagParser.parseEnumeratedOptions("mrs(1),rev(4), ms (2) ,mr(0)\t, \n\t dr \t\r\n(3)");
        assertEquals(5, options.size());
        testNamedTag(options.get(0), "1", "mrs");
        testNamedTag(options.get(1), "4", "rev");
        testNamedTag(options.get(2), "2", "ms");
        testNamedTag(options.get(3), "0", "mr");
        testNamedTag(options.get(4), "3", "dr");

        // test no tags
        options = AsnSchemaNamedTagParser.parseEnumeratedOptions("mr\t, mrs, ms  ,\n\t dr \t\r\n,rev");
        assertEquals(5, options.size());
        testNamedTag(options.get(0), "0", "mr");
        testNamedTag(options.get(1), "1", "mrs");
        testNamedTag(options.get(2), "2", "ms");
        testNamedTag(options.get(3), "3", "dr");
        testNamedTag(options.get(4), "4", "rev");

        // test tag gaps
        options = AsnSchemaNamedTagParser.parseEnumeratedOptions("mr(0), mrs(3), ms, dr, rev");
        assertEquals(5, options.size());
        testNamedTag(options.get(0), "0", "mr");
        testNamedTag(options.get(1), "3", "mrs");
        testNamedTag(options.get(2), "1", "ms");
        testNamedTag(options.get(3), "2", "dr");
        testNamedTag(options.get(4), "4", "rev");

        options = AsnSchemaNamedTagParser.parseEnumeratedOptions("mr(4), mrs(3), ms, dr(2), rev");
        assertEquals(5, options.size());
        testNamedTag(options.get(0), "4", "mr");
        testNamedTag(options.get(1), "3", "mrs");
        testNamedTag(options.get(2), "0", "ms");
        testNamedTag(options.get(3), "2", "dr");
        testNamedTag(options.get(4), "1", "rev");

        // test missing tags
        options = AsnSchemaNamedTagParser.parseEnumeratedOptions("mr(0)\t, mrs, ms (2) ,\n\t dr \t\r\n,rev(4)");
        assertEquals(5, options.size());
        testNamedTag(options.get(0), "0", "mr");
        testNamedTag(options.get(1), "1", "mrs");
        testNamedTag(options.get(2), "2", "ms");
        testNamedTag(options.get(3), "3", "dr");
        testNamedTag(options.get(4), "4", "rev");

        // test duplicates
        try
        {
            AsnSchemaNamedTagParser.parseEnumeratedOptions("mr(0), mrs, ms(1), dr, rev");
            fail("ParseException should have been thrown");
        }
        catch (ParseException e)
        {
        }

        try
        {
            AsnSchemaNamedTagParser.parseEnumeratedOptions("mr(0), mrs(1), ms(2), dr(3), rev(1)");
            fail("ParseException should have been thrown");
        }
        catch (ParseException e)
        {
        }

    }

    @Test
    public void testParseIntegerDistinguishedValues_NullOrEmpty() throws Exception
    {
        // test null
        ImmutableList<AsnSchemaNamedTag> distinguishedValues = AsnSchemaNamedTagParser.parseIntegerDistinguishedValues(null);
        assertEquals(0, distinguishedValues.size());

        // test empty
        distinguishedValues = AsnSchemaNamedTagParser.parseIntegerDistinguishedValues("");
        assertEquals(0, distinguishedValues.size());
        distinguishedValues = AsnSchemaNamedTagParser.parseIntegerDistinguishedValues(" \t\r\n");
        assertEquals(0, distinguishedValues.size());
        distinguishedValues = AsnSchemaNamedTagParser.parseIntegerDistinguishedValues(" ,\t,\r\n");
        assertEquals(0, distinguishedValues.size());
        distinguishedValues = AsnSchemaNamedTagParser.parseIntegerDistinguishedValues(",,,");
        assertEquals(0, distinguishedValues.size());
    }

    @Test
    public void testParseIntegerDistinguishedValues_Exceptions() throws Exception
    {
        // test no tag names
        try
        {
            AsnSchemaNamedTagParser.parseIntegerDistinguishedValues("(0)\t, (1),  (2) ,\n\t  \t\r\n(3),(4)");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test missing tag names
        try
        {
            AsnSchemaNamedTagParser.parseIntegerDistinguishedValues("mr(0)\t, mrs(1),  (2) ,\n\t dr \t\r\n(3),(4)");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test no tags
        try
        {
            AsnSchemaNamedTagParser.parseIntegerDistinguishedValues("mr\t, mrs,  ms ,\n\t dr \t\r\n,miss");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test missing tags
        try
        {
            AsnSchemaNamedTagParser.parseIntegerDistinguishedValues("mr(1)\t, mrs (2),  ms ,\n\t dr \t\r\n(4),miss(5)");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // test invalid ellipsis
        try
        {
            AsnSchemaNamedTagParser.parseIntegerDistinguishedValues("mr(1)\t, mrs (2),  ms ,\n\t dr \t\r\n(4),miss(5),...");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
    }

    @Test
    public void testParseIntegerDistinguishedValues() throws Exception
    {
        // test valid
        ImmutableList<AsnSchemaNamedTag> distinguishedValues =
                AsnSchemaNamedTagParser.parseIntegerDistinguishedValues("mr(0)\t, mrs(1), ms (2) ,\n\t dr \t\r\n(3),rev(4)");
        assertEquals(5, distinguishedValues.size());
        testNamedTag(distinguishedValues.get(0), "0", "mr");
        testNamedTag(distinguishedValues.get(1), "1", "mrs");
        testNamedTag(distinguishedValues.get(2), "2", "ms");
        testNamedTag(distinguishedValues.get(3), "3", "dr");
        testNamedTag(distinguishedValues.get(4), "4", "rev");

        // test unordered tags
        distinguishedValues =
                AsnSchemaNamedTagParser.parseIntegerDistinguishedValues("mrs(1),rev(4), ms (2) ,mr(0)\t, \n\t dr \t\r\n(3)");
        assertEquals(5, distinguishedValues.size());
        testNamedTag(distinguishedValues.get(0), "1", "mrs");
        testNamedTag(distinguishedValues.get(1), "4", "rev");
        testNamedTag(distinguishedValues.get(2), "2", "ms");
        testNamedTag(distinguishedValues.get(3), "0", "mr");
        testNamedTag(distinguishedValues.get(4), "3", "dr");
    }

    // -------------------------------------------------------------------------
    // UTLITY METHODS
    // -------------------------------------------------------------------------

    /**
     * Tests that the supplied namedTag against an expected tag and tag name
     *
     * @param namedTag
     *            namedTag to test
     *
     * @param expectedTag
     *            expected tag for the option
     *
     * @param expectedTagName
     *            expected tag name for the option
     */
    private void testNamedTag(AsnSchemaNamedTag namedTag, String expectedTag, String expectedTagName)
    {
        assertEquals(expectedTag, namedTag.getTag());
        assertEquals(expectedTagName, namedTag.getTagName());
    }
}
