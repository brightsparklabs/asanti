package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaNumericValueConstraint;
import com.brightsparklabs.asanti.model.schema.tagtype.*;

import static org.hamcrest.CoreMatchers.instanceOf;

import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.*;

/**
 * Created by Michael on 9/06/2015.
 */
public class AnsSchemaTagTypeParserTest
{

    @Test
    public void testParse() throws Exception
    {
        try
        {
            // test null
            AsnSchemaTagType result = AnsSchemaTagTypeParser.parse(null, "INTEGER");
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        try
        {
            // test null
            AsnSchemaTagType result = AnsSchemaTagTypeParser.parse("", "INTEGER");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }


        try
        {
            // test null
            AsnSchemaTagType result = AnsSchemaTagTypeParser.parse("foo", null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }

        try
        {
            // test null
            AsnSchemaTagType result = AnsSchemaTagTypeParser.parse("foo", "");
            fail("IllegalArgumentException not thrown");
        }
        catch (final IllegalArgumentException ex)
        {
        }
    }

    @Test
    public void testParse_DocumentPdu() throws Exception
    {
        // Document type
        AsnSchemaTagType result = AnsSchemaTagTypeParser.parse("header", "Header");
        assertThat(result, instanceOf(AsnSchemaTagTypePlaceHolder.class));

        result = AnsSchemaTagTypeParser.parse("body", "Body");
        assertThat(result, instanceOf(AsnSchemaTagTypePlaceHolder.class));

        result = AnsSchemaTagTypeParser.parse("footer", "Footer");
        assertThat(result, instanceOf(AsnSchemaTagTypePlaceHolder.class));

//        result = AnsSchemaTagTypeParser.parse("version", "SEQUENCE");
//        assertThat(result, instanceOf(AsnSchemaTagTypeSequence.class));

        result = AnsSchemaTagTypeParser.parse("majorVersion", "INTEGER");
        assertThat(result, instanceOf(AsnSchemaTagTypeInteger.class));

        result = AnsSchemaTagTypeParser.parse("withConstraints", "INTEGER (0..100)");
        assertThat(result, instanceOf(AsnSchemaTagTypeInteger.class));


        result = AnsSchemaTagTypeParser.parse("summary", "UTF8String");
        assertThat(result, instanceOf(AsnSchemaTagTypeUtf8String.class));

        result = AnsSchemaTagTypeParser.parse("summary", "OCTET STRING");
        assertThat(result, instanceOf(AsnSchemaTagType.class));

        // TODO MJF - test the rest of these as they are implemented

    }
}