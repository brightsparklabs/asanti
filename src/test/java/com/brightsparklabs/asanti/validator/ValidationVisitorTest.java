/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypes;
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
        final BuiltinTypeValidator.Null result = instance.visit(AsnPrimitiveTypes.INVALID);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionBitString()
    {
        final BitStringValidator result = instance.visit(AsnPrimitiveTypes.BIT_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionBmpString()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveTypes.BMP_STRING);
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionBoolean()
    {
        final BooleanValidator result = instance.visit(AsnPrimitiveTypes.BOOLEAN);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionCharacterString()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveTypes.CHARACTER_STRING);
        // TODO - ASN-105
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionChoice()
    {
        // TODO: ASN-113
        final Object result = instance.visit(AsnPrimitiveTypes.CHOICE);
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionEmbeddedPdv()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveTypes.EMBEDDED_PDV);
        // TODO - ASN-105
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionEnumerated()
    {
        final EnumeratedValidator result = instance.visit(AsnPrimitiveTypes.ENUMERATED);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralizedTime()
    {
        final GeneralizedTimeValidator result = instance.visit(AsnPrimitiveTypes.GENERALIZED_TIME);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralString()
    {
        final GeneralStringValidator result = instance.visit(AsnPrimitiveTypes.GENERAL_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGraphicString()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveTypes.GRAPHIC_STRING);
        // TODO - ASN-105
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionIA5String()
    {
        final Ia5StringValidator result = instance.visit(AsnPrimitiveTypes.IA5_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionInteger()
    {
        final IntegerValidator result = instance.visit(AsnPrimitiveTypes.INTEGER);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionNull()
    {
        final NullValidator result = instance.visit(AsnPrimitiveTypes.NULL);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionNumericString()
    {
        final NumericStringValidator result = instance.visit(AsnPrimitiveTypes.NUMERIC_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionObjectIdentifier()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveTypes.OBJECT_DESCRIPTOR);
        // TODO - ASN-105
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionOctetString()
    {
        final OctetStringValidator result = instance.visit(AsnPrimitiveTypes.OCTET_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionOid()
    {
        final OidValidator result = instance.visit(AsnPrimitiveTypes.OID);
        // TODO - ASN-105
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionPrintableString()
    {
        final PrintableStringValidator result = instance.visit(AsnPrimitiveTypes.PRINTABLE_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionReal()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveTypes.REAL);
        // TODO - ASN-105
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionRelativeOid()
    {
        final OidValidator result = instance.visit(AsnPrimitiveTypes.RELATIVE_OID);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequence()
    {
        final ConstructedBuiltinTypeValidator result = instance.visit(AsnPrimitiveTypes.SEQUENCE);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequenceOf()
    {
        // TODO - ASN-113
        assertEquals(BuiltinTypeValidator.NULL, instance.visit(AsnPrimitiveTypes.SEQUENCE_OF));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSet()
    {
        final ConstructedBuiltinTypeValidator result = instance.visit(AsnPrimitiveTypes.SET);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSetOf()
    {
        // TODO - ASN-113
        assertEquals(BuiltinTypeValidator.NULL, instance.visit(AsnPrimitiveTypes.SET_OF));
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionUtcTime()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveTypes.UTC_TIME);
        // TODO - ASN-105
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionUtf8String()
    {
        final Utf8StringValidator result = instance.visit(AsnPrimitiveTypes.UTF8_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionTeletexString()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveTypes.TELETEX_STRING);
        // TODO - ASN-105
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionUniversalString()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveTypes.UNIVERSAL_STRING);
        // TODO - ASN-105
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionVideotexString()
    {
        final BuiltinTypeValidator result = instance.visit(AsnPrimitiveTypes.VIDEOTEX_STRING);
        assertEquals(BuiltinTypeValidator.NULL, result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionVisibleString()
    {
        final VisibleStringValidator result = instance.visit(AsnPrimitiveTypes.VISIBLE_STRING);
        assertNotNull(result);
    }
}
