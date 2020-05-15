/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.reader.parser;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.model.schema.constraint.*;
import org.junit.Test;

/**
 * Unit tests for {@link AsnSchemaConstraintParser}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaConstraintParserTest {
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testParse() throws Exception {
        // test null
        AsnSchemaConstraint result = AsnSchemaConstraintParser.parse(null);
        assertEquals(AsnSchemaConstraint.NULL, result);

        // test empty
        result = AsnSchemaConstraintParser.parse("");
        assertEquals(AsnSchemaConstraint.NULL, result);
        result = AsnSchemaConstraintParser.parse(" ");
        assertEquals(AsnSchemaConstraint.NULL, result);
        result = AsnSchemaConstraintParser.parse(" \t\r\n");
        assertEquals(AsnSchemaConstraint.NULL, result);
    }

    @Test
    public void testParse_SizeConstraint() throws Exception {
        // sunny day case
        AsnSchemaConstraint result = AsnSchemaConstraintParser.parse("SIZE (1..256)");
        assertTrue(result instanceof AsnSchemaSizeConstraint);
        // extra brackets
        result = AsnSchemaConstraintParser.parse("(SIZE (1..256))");
        assertTrue(result instanceof AsnSchemaSizeConstraint);
        // extra whitespace
        result = AsnSchemaConstraintParser.parse("\r\n\t\tSIZE(\r\n\t\t1 .. 256\r\n\t\t)\r\n\t\t");
        assertTrue(result instanceof AsnSchemaSizeConstraint);
        result = AsnSchemaConstraintParser.parse("\r\n\t\t(\n\t  SIZE(1  ..\t256\n ) \t  )\n");
        assertTrue(result instanceof AsnSchemaSizeConstraint);

        // missing minimum
        result = AsnSchemaConstraintParser.parse("SIZE (1..)");
        assertEquals(AsnSchemaConstraint.NULL, result);
        // missing maximum
        result = AsnSchemaConstraintParser.parse("SIZE (..256)");
        assertEquals(AsnSchemaConstraint.NULL, result);
    }

    @Test
    public void testParse_ExactSizeConstraint() throws Exception {
        // sunny day case
        AsnSchemaConstraint result = AsnSchemaConstraintParser.parse("SIZE (1)");
        assertTrue(result instanceof AsnSchemaExactSizeConstraint);
        // extra brackets
        result = AsnSchemaConstraintParser.parse("(SIZE (1))");
        assertTrue(result instanceof AsnSchemaExactSizeConstraint);
        // extra whitespace
        result = AsnSchemaConstraintParser.parse("\r\n\t\tSIZE(\r\n\t\t1 \r\n\t\t)\r\n\t\t");
        assertTrue(result instanceof AsnSchemaExactSizeConstraint);
        result = AsnSchemaConstraintParser.parse("\r\n\t\t(\n\t  SIZE(1  \t\n ) \t  )\n");
        assertTrue(result instanceof AsnSchemaExactSizeConstraint);
    }

    @Test
    public void testParse_NumericValueConstraint() throws Exception {
        // sunny day case
        AsnSchemaConstraint result = AsnSchemaConstraintParser.parse("1..256");
        assertTrue(result instanceof AsnSchemaNumericValueConstraint);
        // extra brackets
        result = AsnSchemaConstraintParser.parse("(1..256)");
        assertTrue(result instanceof AsnSchemaNumericValueConstraint);
        // extra whitespace
        result = AsnSchemaConstraintParser.parse("\r\n\t\t\r\n\t\t1 .. 256\r\n\t\t\r\n\t\t");
        assertTrue(result instanceof AsnSchemaNumericValueConstraint);
        result = AsnSchemaConstraintParser.parse("\r\n\t\t\n\t  (1  ..\t256\n ) \t  \n");
        assertTrue(result instanceof AsnSchemaNumericValueConstraint);

        // negative numbers
        result = AsnSchemaConstraintParser.parse("(-2147483648..2147483647)");
        assertTrue(result instanceof AsnSchemaNumericValueConstraint);
        result = AsnSchemaConstraintParser.parse("(-2147483648..-1)");
        assertTrue(result instanceof AsnSchemaNumericValueConstraint);

        // missing minimum
        result = AsnSchemaConstraintParser.parse("(1..)");
        assertEquals(AsnSchemaConstraint.NULL, result);
        // missing maximum
        result = AsnSchemaConstraintParser.parse("(..256)");
        assertEquals(AsnSchemaConstraint.NULL, result);
    }

    @Test
    public void testParse_ExactNumericValueConstraint() throws Exception {
        // sunny day case
        AsnSchemaConstraint result = AsnSchemaConstraintParser.parse("1");
        assertTrue(result instanceof AsnSchemaExactNumericValueConstraint);
        // extra brackets
        result = AsnSchemaConstraintParser.parse("(1)");
        assertTrue(result instanceof AsnSchemaExactNumericValueConstraint);
        // extra whitespace
        result = AsnSchemaConstraintParser.parse("\r\n\t\t\r\n\t\t1 \r\n\t\t\r\n\t\t");
        assertTrue(result instanceof AsnSchemaExactNumericValueConstraint);
        result = AsnSchemaConstraintParser.parse("\r\n\t\t\n\t  (1  \t\n ) \t  \n");
        assertTrue(result instanceof AsnSchemaExactNumericValueConstraint);
        // negative number
        result = AsnSchemaConstraintParser.parse("(-2147483648)");
        assertTrue(result instanceof AsnSchemaExactNumericValueConstraint);
    }
}
