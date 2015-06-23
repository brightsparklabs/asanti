package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.type.*;

import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


/**
 * Unit tests for {@link AsnSchemaTypeParser}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeParserTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testParseParameters() throws Exception
    {
        // null value
        try
        {
            AsnSchemaTypeParser.parse(null);
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // only whitespace
        try
        {
            AsnSchemaTypeParser.parse(" ");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }

        // blank value
        try
        {
            AsnSchemaTypeParser.parse("");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
    }

    @Test
    public void testFailures() throws Exception
    {
        try
        {
            AsnSchemaTypeParser.parse("Fred ::= INTEGER");
            fail("ParseException not thrown");
        }
        catch (final ParseException ex)
        {
        }
    }

    @Test
    public void testParseBitString() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("BIT STRING");
        assertEquals(AsnBuiltinType.BitString, result.getBuiltinType());
        assertThat(result, instanceOf(AsnSchemaTypeWithNamedTags.class));
    }

    @Test
    public void testParseBoolean() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("BOOLEAN");
        assertEquals(AsnBuiltinType.Boolean, result.getBuiltinType());
    }

    @Test
    public void testParseChoice() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("CHOICE { optA [0] SomeType }");
        assertEquals(AsnBuiltinType.Choice, result.getBuiltinType());
    }

    @Test
    public void testParseEnumerated() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("ENUMERATED { optA(0), optB(1) }");
        assertEquals(AsnBuiltinType.Enumerated, result.getBuiltinType());
    }

    @Test
    public void testParseGeneralString() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("GeneralString");
        assertEquals(AsnBuiltinType.GeneralString, result.getBuiltinType());
        assertThat(result, instanceOf(BaseAsnSchemaType.class));
    }

    @Test
    public void testParseGeneralizedTime() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("GeneralizedTime");
        assertEquals(AsnBuiltinType.GeneralizedTime, result.getBuiltinType());
        assertThat(result, instanceOf(BaseAsnSchemaType.class));
    }

    @Test
    public void testParseIA5String() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("IA5String");
        assertEquals(AsnBuiltinType.Ia5String, result.getBuiltinType());
        assertThat(result, instanceOf(BaseAsnSchemaType.class));
    }

    @Test
    public void testParseInteger() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("INTEGER (SIZE (10))");
        assertEquals(AsnBuiltinType.Integer, result.getBuiltinType());
        assertThat(result, instanceOf(AsnSchemaTypeWithNamedTags.class));
    }

    @Test
    public void testParseNumericString() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("NumericString");
        assertEquals(AsnBuiltinType.NumericString, result.getBuiltinType());
        assertThat(result, instanceOf(BaseAsnSchemaType.class));
    }

    @Test
    public void testParseOctetString() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("OCTET STRING");
        assertEquals(AsnBuiltinType.OctetString, result.getBuiltinType());
        assertThat(result, instanceOf(BaseAsnSchemaType.class));
    }

    @Test
    public void testParseOid() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("OBJECT IDENTIFIER");
        assertEquals(AsnBuiltinType.Oid, result.getBuiltinType());
        assertThat(result, instanceOf(BaseAsnSchemaType.class));
    }

    @Test
    public void testParsePrintableString() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("PrintableString");
        assertEquals(AsnBuiltinType.PrintableString, result.getBuiltinType());
        assertThat(result, instanceOf(BaseAsnSchemaType.class));
    }

    @Test
    public void testParseRelativeOid() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("RELATIVE-OID");
        assertEquals(AsnBuiltinType.RelativeOid, result.getBuiltinType());
        assertThat(result, instanceOf(BaseAsnSchemaType.class));
    }

    @Test
    public void testParseSequence() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("SEQUENCE { someValue [0] SomeType }");
        assertEquals(AsnBuiltinType.Sequence, result.getBuiltinType());
        assertThat(result, instanceOf(AsnSchemaTypeConstructed.class));

        // TODO MJF - when we figure out the interface to fix the instance of's in decodeTag then
        // we need to add test checks here to ensure we get the right number/type of components.
    }

    @Test
    public void testParseSequenceOf() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("SEQUENCE (SIZE (1..10)) OF SEQUENCE { someValue [0] SomeType }");
        assertEquals(AsnBuiltinType.SequenceOf, result.getBuiltinType());
        assertThat(result, instanceOf(AsnSchemaTypeCollection.class));
    }

    @Test
    public void testParseSet() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("SET { someValue [0] SomeType }");
        assertEquals(AsnBuiltinType.Set, result.getBuiltinType());
        assertThat(result, instanceOf(AsnSchemaTypeConstructed.class));
    }

    @Test
    public void testParseSetOf() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("SET (SIZE (10)) OF INTEGER");
        assertEquals(AsnBuiltinType.SetOf, result.getBuiltinType());
        assertThat(result, instanceOf(AsnSchemaTypeCollection.class));

        assertEquals(1, result.getConstraints().size());
    }

    @Test
    public void testParseUtf8String() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("UTF8String");
        assertEquals(AsnBuiltinType.Utf8String, result.getBuiltinType());
        assertThat(result, instanceOf(BaseAsnSchemaType.class));
    }

    @Test
    public void testParseVisibleString() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("VisibleString");
        assertEquals(AsnBuiltinType.VisibleString, result.getBuiltinType());
        assertThat(result, instanceOf(BaseAsnSchemaType.class));
    }

    @Test
    public void testParseNonPrimitive() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("SomeType");
        assertEquals(AsnBuiltinType.Null, result.getBuiltinType());
        assertThat(result, instanceOf(AsnSchemaTypePlaceholder.class));
    }

}