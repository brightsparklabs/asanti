package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.type.*;

import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.ParseException;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link AsnSchemaTypeParser}
 *
 * @author brightSPARK Labs
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ AsnSchemaConstraintParser.class, AsnSchemaNamedTagParser.class, AsnSchemaComponentTypeParser.class })
public class AsnSchemaTypeParserTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** an argument capture helper for Constraints */
    private ArgumentCaptor<String> constraintArgument;

    /** an argument helper for Distinguished Values */
    private ArgumentCaptor<String> distinguishedValuesArgument;

    /** an argument helper for Enumerated options */
    private ArgumentCaptor<String> enumeratedOptionsArgument;

    /** an argument helper for Components */
    private ArgumentCaptor<String> componentArgument;

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @Before
    public void setUpBeforeTest() throws Exception
    {
        // Mockito does a tear down after each test, so we can't do this as a BeforeClass

        // Setup the mock child parsers.  We are only trying to parse the Type, not the
        // constraints, or named values (Enumerator, Integer).
        // We do want to test that those child parsers get passed the correct information,
        // so we setup the argument captors.

        // mock AsnSchemaConstraintParser.parse static method
        PowerMockito.mockStatic(AsnSchemaConstraintParser.class);
        // we want to capture the constraintArgument that gets passed to the AsnSchemaConstraintParser
        constraintArgument = ArgumentCaptor.forClass(String.class);
        when(AsnSchemaConstraintParser.parse(constraintArgument.capture())).thenReturn(AsnSchemaConstraint.NULL);

        // mock AsnSchemaConstraintParser.parseIntegerDistinguishedValues and parseEnumeratedOptions static methods
        PowerMockito.mockStatic(AsnSchemaNamedTagParser.class);
        distinguishedValuesArgument = ArgumentCaptor.forClass(String.class);
        when(AsnSchemaNamedTagParser.parseIntegerDistinguishedValues(distinguishedValuesArgument.capture())).thenReturn(
                ImmutableList.<AsnSchemaNamedTag>of());
        enumeratedOptionsArgument = ArgumentCaptor.forClass(String.class);
        when(AsnSchemaNamedTagParser.parseEnumeratedOptions(enumeratedOptionsArgument.capture())).thenReturn(
                ImmutableList.<AsnSchemaNamedTag>of());

        // mock AsnSchemaComponentTypeParser.parse, and capture call arguments
        PowerMockito.mockStatic(AsnSchemaComponentTypeParser.class);
        componentArgument = ArgumentCaptor.forClass(String.class);
        when(AsnSchemaComponentTypeParser.parse(componentArgument.capture())).thenReturn(
                ImmutableList.<AsnSchemaComponentType>of());
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    /* TODO ASN-135 - these tests need to be more complete.  They should try to really test the
     * regex matching for all the combinations of constraints, named values etc.
      */


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
        AsnSchemaType result = AsnSchemaTypeParser.parse("INTEGER");
        assertEquals(AsnBuiltinType.Integer, result.getBuiltinType());
        assertThat(result, instanceOf(AsnSchemaTypeWithNamedTags.class));
        // Ensure that the right text got passed to the constraints parser
        assertEquals("", constraintArgument.getValue());
        assertEquals("", distinguishedValuesArgument.getValue());
        assertEquals(1, result.getConstraints().size());


        result = AsnSchemaTypeParser.parse("INTEGER (1..10)");
        assertEquals(AsnBuiltinType.Integer, result.getBuiltinType());
        assertThat(result, instanceOf(AsnSchemaTypeWithNamedTags.class));
        // Ensure that the right text got passed to the constraints parser
        assertEquals("1..10", constraintArgument.getValue());
        assertEquals("", distinguishedValuesArgument.getValue());
        assertEquals(1, result.getConstraints().size());

        result = AsnSchemaTypeParser.parse("INTEGER (1..10, ...)");
        assertEquals(AsnBuiltinType.Integer, result.getBuiltinType());
        assertThat(result, instanceOf(AsnSchemaTypeWithNamedTags.class));
        // Ensure that the right text got passed to the constraints parser
        assertEquals("1..10, ...", constraintArgument.getValue());
        assertEquals("", distinguishedValuesArgument.getValue());
        assertEquals(1, result.getConstraints().size());

        // check distinguished values.
        result = AsnSchemaTypeParser.parse("INTEGER { disk-full(1), no-disk(-1), disk-not-formatted(2) }");
        assertEquals(AsnBuiltinType.Integer, result.getBuiltinType());
        assertThat(result, instanceOf(AsnSchemaTypeWithNamedTags.class));
        // Ensure that the right text got passed to the constraints parser
        assertEquals("", constraintArgument.getValue());
        assertEquals(" disk-full(1), no-disk(-1), disk-not-formatted(2) ", distinguishedValuesArgument.getValue());
        assertEquals(1, result.getConstraints().size());
    }

    @Test
    public void testParseNumericString() throws Exception
    {
        AsnSchemaType result = AsnSchemaTypeParser.parse("NumericString");
        assertEquals(AsnBuiltinType.NumericString, result.getBuiltinType());
        assertThat(result, instanceOf(BaseAsnSchemaType.class));

        //
        result = AsnSchemaTypeParser.parse("NumericString (SIZE(1..100))");
        assertEquals(AsnBuiltinType.NumericString, result.getBuiltinType());
        assertThat(result, instanceOf(BaseAsnSchemaType.class));
        // Ensure that the right text got passed to the constraints parser
        assertEquals("SIZE(1..100)", constraintArgument.getValue());
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
        AsnSchemaType result = AsnSchemaTypeParser.parse("SEQUENCE { someValue [0] UTF8String }");
        assertEquals(AsnBuiltinType.Sequence, result.getBuiltinType());
        assertThat(result, instanceOf(AsnSchemaTypeConstructed.class));
        assertEquals(" someValue [0] UTF8String ", componentArgument.getValue());

        result = AsnSchemaTypeParser.parse("SEQUENCE { someValue [0] SEQUENCE { foo [0] BAR }");
        assertEquals(AsnBuiltinType.Sequence, result.getBuiltinType());
        assertThat(result, instanceOf(AsnSchemaTypeConstructed.class));
        assertEquals(" someValue [0] SEQUENCE { foo [0] BAR ", componentArgument.getValue());
    }

    @Test
    public void testParseSequenceOf() throws Exception
    {
        final AsnSchemaType result = AsnSchemaTypeParser.parse("SEQUENCE (SIZE (1..10)) OF SEQUENCE { someValue [0] SomeType }");
        // TODO ASN-140
        // getPrimitiveType on a collection returns the type of the thing that we are a collection of
        // not the actual collection.
        //assertEquals(AsnBuiltinType.SequenceOf, result.getBuiltinType());
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
        final AsnSchemaType result = AsnSchemaTypeParser.parse("SET (SIZE (10)) OF INTEGER (1..100)");
        // TODO ASN-140
        // getPrimitiveType on a collection returns the type of the thing that we are a collection of
        // not the actual collection.
        //assertEquals(AsnBuiltinType.SetOf, result.getBuiltinType());
        assertThat(result, instanceOf(AsnSchemaTypeCollection.class));
        // the first time we parse constraints is for the SET OF
        assertEquals(" (SIZE (10))", constraintArgument.getAllValues().get(0));

        AsnSchemaTypeCollection collection = (AsnSchemaTypeCollection)result;
        assertEquals(AsnBuiltinType.Integer, collection.getElementType().getBuiltinType());
        // the second time we parse constraints is for the INTEGER
        assertEquals("1..100", constraintArgument.getAllValues().get(1));
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