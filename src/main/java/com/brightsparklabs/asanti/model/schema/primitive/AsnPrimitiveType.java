package com.brightsparklabs.asanti.model.schema.primitive;

import com.brightsparklabs.asanti.common.Visitable;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;

/**
 * A base type used to represent the primitive builtin types within ASN.1 This class is used as a
 * 'key' for the Visitor.
 *
 * @author brightSPARK Labs
 */
public interface AsnPrimitiveType extends Visitable<AsnPrimitiveTypeVisitor<?>>
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** Invalid Null instance */
    public static final AsnPrimitiveType.Invalid INVALID = new AsnPrimitiveType.Invalid();

    /** static instance {@link AsnPrimitiveTypeBitString} */
    public static final AsnPrimitiveTypeBitString BIT_STRING = new AsnPrimitiveTypeBitString();

    /** static instance {@link AsnPrimitiveTypeBmpString} */
    public static final AsnPrimitiveTypeBmpString BMP_STRING = new AsnPrimitiveTypeBmpString();

    /** static instance {@link AsnPrimitiveTypeBoolean} */
    public static final AsnPrimitiveTypeBoolean BOOLEAN = new AsnPrimitiveTypeBoolean();

    /** static instance {@link AsnPrimitiveTypeCharacterString} */
    public static final AsnPrimitiveTypeCharacterString CHARACTER_STRING
            = new AsnPrimitiveTypeCharacterString();

    /** static instance {@link AsnPrimitiveTypeChoice} */
    public static final AsnPrimitiveTypeChoice CHOICE = new AsnPrimitiveTypeChoice();

    /** static instance {@link AsnPrimitiveTypeEmbeddedPdv} */
    public static final AsnPrimitiveTypeEmbeddedPdv EMBEDDED_PDV
            = new AsnPrimitiveTypeEmbeddedPdv();

    /** static instance {@link AsnPrimitiveTypeEnumerated} */
    public static final AsnPrimitiveTypeEnumerated ENUMERATED = new AsnPrimitiveTypeEnumerated();

    /** static instance {@link AsnPrimitiveTypeGeneralString} */
    public static final AsnPrimitiveTypeGeneralString GENERAL_STRING
            = new AsnPrimitiveTypeGeneralString();

    /** static instance {@link AsnPrimitiveTypeGeneralizedTime} */
    public static final AsnPrimitiveTypeGeneralizedTime GENERALIZED_TIME
            = new AsnPrimitiveTypeGeneralizedTime();

    /** static instance {@link AsnPrimitiveTypeGraphicString} */
    public static final AsnPrimitiveTypeGraphicString GRAPHIC_STRING
            = new AsnPrimitiveTypeGraphicString();

    /** static instance {@link AsnPrimitiveTypeIA5String} */
    public static final AsnPrimitiveTypeIA5String IA5_STRING = new AsnPrimitiveTypeIA5String();

    /** static instance {@link AsnPrimitiveTypeInteger} */
    public static final AsnPrimitiveTypeInteger INTEGER = new AsnPrimitiveTypeInteger();

    /** static instance {@link AsnPrimitiveTypeNull} */
    public static final AsnPrimitiveTypeNull NULL = new AsnPrimitiveTypeNull();

    /** static instance {@link AsnPrimitiveTypeNumericString} */
    public static final AsnPrimitiveTypeNumericString NUMERIC_STRING
            = new AsnPrimitiveTypeNumericString();

    /** static instance {@link AsnPrimitiveTypeObjectDescriptor} */
    public static final AsnPrimitiveTypeObjectDescriptor OBJECT_DESCRIPTOR
            = new AsnPrimitiveTypeObjectDescriptor();

    /** static instance {@link AsnPrimitiveTypeOctetString} */
    public static final AsnPrimitiveTypeOctetString OCTET_STRING
            = new AsnPrimitiveTypeOctetString();

    /** static instance {@link AsnPrimitiveTypeOid} */
    public static final AsnPrimitiveTypeOid OID = new AsnPrimitiveTypeOid();

    /** static instance {@link AsnPrimitiveTypePrintableString} */
    public static final AsnPrimitiveTypePrintableString PRINTABLE_STRING
            = new AsnPrimitiveTypePrintableString();

    /** static instance {@link AsnPrimitiveTypeReal} */
    public static final AsnPrimitiveTypeReal REAL = new AsnPrimitiveTypeReal();

    /** static instance {@link AsnPrimitiveTypeRelativeOid} */
    public static final AsnPrimitiveTypeRelativeOid RELATIVE_OID
            = new AsnPrimitiveTypeRelativeOid();

    /** static instance {@link AsnPrimitiveTypeSequence} */
    public static final AsnPrimitiveTypeSequence SEQUENCE = new AsnPrimitiveTypeSequence();

    /** static instance {@link AsnPrimitiveTypeSequenceOf} */
    public static final AsnPrimitiveTypeSequenceOf SEQUENCE_OF = new AsnPrimitiveTypeSequenceOf();

    /** static instance {@link AsnPrimitiveTypeSet SET =} */
    public static final AsnPrimitiveTypeSet SET = new AsnPrimitiveTypeSet();

    /** static instance {@link AsnPrimitiveTypeSetOf} */
    public static final AsnPrimitiveTypeSetOf SET_OF = new AsnPrimitiveTypeSetOf();

    /** static instance {@link AsnPrimitiveTypeTeletexString} */
    public static final AsnPrimitiveTypeTeletexString TELETEX_STRING
            = new AsnPrimitiveTypeTeletexString();

    /** static instance {@link AsnPrimitiveTypeVideotexString} */
    public static final AsnPrimitiveTypeVideotexString VIDEOTEX_STRING
            = new AsnPrimitiveTypeVideotexString();

    /** static instance {@link AsnPrimitiveTypeUniversalString} */
    public static final AsnPrimitiveTypeUniversalString UNIVERSAL_STRING
            = new AsnPrimitiveTypeUniversalString();

    /** static instance {@link AsnPrimitiveTypeUtf8String} */
    public static final AsnPrimitiveTypeUtf8String UTF8_STRING = new AsnPrimitiveTypeUtf8String();

    /** static instance {@link AsnPrimitiveTypeUtcTime} */
    public static final AsnPrimitiveTypeUtcTime UTC_TIME = new AsnPrimitiveTypeUtcTime();

    /** static instance {@link AsnPrimitiveTypeVisibleString} */
    public static final AsnPrimitiveTypeVisibleString VISIBLE_STRING
            = new AsnPrimitiveTypeVisibleString();

    /**
     * Returns the ASN.1 built-in type for this type
     *
     * @return the ASN.1 built-in type for this type
     */
    public AsnBuiltinType getBuiltinType();

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Null
    // -------------------------------------------------------------------------

    /**
     * Null instance of {@link AsnPrimitiveType}.
     *
     * <p> NOTE: This is not named {@code AsnSchemaPrimitiveTypeNull} because that is the name used
     * to model an actual ASN.1 {@code NULL} Type.
     */
    public static class Invalid implements AsnPrimitiveType
    {
        @Override
        public AsnBuiltinType getBuiltinType()
        {
            return AsnBuiltinType.Null;
        }

        @Override
        public Object accept(AsnPrimitiveTypeVisitor<?> visitor)
        {
            return visitor.visit(this);
        }
    }
}
