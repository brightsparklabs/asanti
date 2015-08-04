/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.validator.builtin.*;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link ValidationVisitor}
 *
 * @author brightSPARK Labs
 */
public class ValidationVisitorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final ValidationVisitor instance = new ValidationVisitor();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testVisitAsnSchemaTypeDefinitionInvalid()
    {
        final BuiltinTypeValidator.Null result = instance.visit(AsnPrimitiveType.INVALID);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionBitString()
    {
        final BitStringValidator result = instance.visit(AsnPrimitiveType.BIT_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionBmpString()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveType.BMP_STRING);
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionBoolean()
    {
        final BooleanValidator result = instance.visit(AsnPrimitiveType.BOOLEAN);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionCharacterString()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveType.CHARACTER_STRING);
        // TODO - ASN-105
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionChoice()
    {
        // TODO: ASN-113
        final Object result = instance.visit(AsnPrimitiveType.CHOICE);
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionEmbeddedPdv()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveType.EMBEDDED_PDV);
        // TODO - ASN-105
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionEnumerated()
    {
        final EnumeratedValidator result = instance.visit(AsnPrimitiveType.ENUMERATED);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralizedTime()
    {
        final GeneralizedTimeValidator result = instance.visit(AsnPrimitiveType.GENERALIZED_TIME);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralString()
    {
        final GeneralStringValidator result = instance.visit(AsnPrimitiveType.GENERAL_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGraphicString()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveType.GRAPHIC_STRING);
        // TODO - ASN-105
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionIA5String()
    {
        final Ia5StringValidator result = instance.visit(AsnPrimitiveType.IA5_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionInteger()
    {
        final IntegerValidator result = instance.visit(AsnPrimitiveType.INTEGER);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionNull()
    {
        final NullValidator result = instance.visit(AsnPrimitiveType.NULL);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionNumericString()
    {
        final NumericStringValidator result = instance.visit(AsnPrimitiveType.NUMERIC_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionObjectIdentifier()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveType.OBJECT_DESCRIPTOR);
        // TODO - ASN-105
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionOctetString()
    {
        final OctetStringValidator result = instance.visit(AsnPrimitiveType.OCTET_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionOid()
    {
        final OidValidator result = instance.visit(AsnPrimitiveType.OID);
        // TODO - ASN-105
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionPrintableString()
    {
        final PrintableStringValidator result = instance.visit(AsnPrimitiveType.PRINTABLE_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionReal()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveType.REAL);
        // TODO - ASN-105
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionRelativeOid()
    {
        final OidValidator result = instance.visit(AsnPrimitiveType.RELATIVE_OID);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequence()
    {
        final ConstructedBuiltinTypeValidator result = instance.visit(AsnPrimitiveType.SEQUENCE);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequenceOf()
    {
        // TODO - ASN-113
        assertEquals(BuiltinTypeValidator.NULL, instance.visit(AsnPrimitiveType.SEQUENCE_OF));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSet()
    {
        final ConstructedBuiltinTypeValidator result = instance.visit(AsnPrimitiveType.SET);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSetOf()
    {
        // TODO - ASN-113
        assertEquals(BuiltinTypeValidator.NULL, instance.visit(AsnPrimitiveType.SET_OF));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionUtcTime()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveType.UTC_TIME);
        // TODO - ASN-105
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionUtf8String()
    {
        final Utf8StringValidator result = instance.visit(AsnPrimitiveType.UTF8_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionTeletexString()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveType.TELETEX_STRING);
        // TODO - ASN-105
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionUniversalString()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveType.UNIVERSAL_STRING);
        // TODO - ASN-105
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionVideotexString()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveType.VIDEOTEX_STRING);
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionVisibleString()
    {
        final VisibleStringValidator result = instance.visit(AsnPrimitiveType.VISIBLE_STRING);
        assertNotNull(result);
    }
}
