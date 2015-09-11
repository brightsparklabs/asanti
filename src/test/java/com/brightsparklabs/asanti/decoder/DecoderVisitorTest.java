/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.decoder;

import com.brightsparklabs.asanti.decoder.builtin.*;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypes;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link DecoderVisitor}
 *
 * @author brightSPARK Labs
 */
public class DecoderVisitorTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final DecoderVisitor instance = new DecoderVisitor();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testVisitAsnSchemaTypeDefinitionInvalidInstance()
    {
        final Object result = instance.visit(AsnPrimitiveTypes.INVALID);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionBitString()
    {
        final BitStringDecoder result = instance.visit(AsnPrimitiveTypes.BIT_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionBmpString()
    {
        // TODO - ASN-107
        final BuiltinTypeDecoder result = instance.visit(AsnPrimitiveTypes.BMP_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionBoolean()
    {
        final BooleanDecoder result = instance.visit(AsnPrimitiveTypes.BOOLEAN);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionCharacterString()
    {
        // TODO - ASN-107
        final BuiltinTypeDecoder result = instance.visit(AsnPrimitiveTypes.CHARACTER_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionChoice()
    {
        final Object result = instance.visit(AsnPrimitiveTypes.CHOICE);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionEmbeddedPdv()
    {
        // TODO - ASN-107
        final BuiltinTypeDecoder result = instance.visit(AsnPrimitiveTypes.EMBEDDED_PDV);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionEnumerated()
    {
        final EnumeratedDecoder result = instance.visit(AsnPrimitiveTypes.ENUMERATED);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralizedTime()
    {
        final GeneralizedTimeDecoder result = instance.visit(AsnPrimitiveTypes.GENERALIZED_TIME);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGeneralString()
    {
        final GeneralStringDecoder result = instance.visit(AsnPrimitiveTypes.GENERAL_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionGraphicString()
    {
        // TODO - ASN-107
        final BuiltinTypeDecoder result = instance.visit(AsnPrimitiveTypes.GRAPHIC_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionIA5String()
    {
        final Ia5StringDecoder result = instance.visit(AsnPrimitiveTypes.IA5_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionInteger()
    {
        final IntegerDecoder result = instance.visit(AsnPrimitiveTypes.INTEGER);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionNull()
    {
        final NullDecoder result = instance.visit(AsnPrimitiveTypes.NULL);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionNumericString()
    {
        final NumericStringDecoder result = instance.visit(AsnPrimitiveTypes.NUMERIC_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionObjectDescriptor()
    {
        // TODO - ASN-107
        final BuiltinTypeDecoder result = instance.visit(AsnPrimitiveTypes.OBJECT_DESCRIPTOR);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionOctetString()
    {
        final OctetStringDecoder result = instance.visit(AsnPrimitiveTypes.OCTET_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionOid()
    {
        final OidDecoder result = instance.visit(AsnPrimitiveTypes.OID);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionPrintableString()
    {
        final PrintableStringDecoder result = instance.visit(AsnPrimitiveTypes.PRINTABLE_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionReal()
    {
        // TODO - ASN-107
        final BuiltinTypeDecoder result = instance.visit(AsnPrimitiveTypes.REAL);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionRelativeOid()
    {
        final OidDecoder result = instance.visit(AsnPrimitiveTypes.RELATIVE_OID);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequence()
    {
        final Object result = instance.visit(AsnPrimitiveTypes.SEQUENCE);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSequenceOf()
    {
        final Object result = instance.visit(AsnPrimitiveTypes.SEQUENCE_OF);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSet()
    {
        final Object result = instance.visit(AsnPrimitiveTypes.SET);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionSetOf()
    {
        final Object result = instance.visit(AsnPrimitiveTypes.SET_OF);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionTeletexString()
    {
        // TODO - ASN-107
        final BuiltinTypeDecoder result = instance.visit(AsnPrimitiveTypes.TELETEX_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionUniversalString()
    {
        // TODO - ASN-107
        final BuiltinTypeDecoder result = instance.visit(AsnPrimitiveTypes.UNIVERSAL_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionUtcTime()
    {
        final BuiltinTypeDecoder result = instance.visit(AsnPrimitiveTypes.UTC_TIME);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionUtf8String()
    {
        final Utf8StringDecoder result = instance.visit(AsnPrimitiveTypes.UTF8_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionVideotexString()
    {
        // TODO - ASN-107
        final BuiltinTypeDecoder result = instance.visit(AsnPrimitiveTypes.VIDEOTEX_STRING);
        assertNotNull(result);
    }

    @Test
    public void testVisitAsnSchemaTypeDefinitionVisibleString()
    {
        final VisibleStringDecoder result = instance.visit(AsnPrimitiveTypes.VISIBLE_STRING);
        assertNotNull(result);
    }
}
