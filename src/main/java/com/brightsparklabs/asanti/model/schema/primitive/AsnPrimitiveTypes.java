/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.primitive;

import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.schema.AsnPrimitiveTypeVisitor;

/**
 * A base type used to represent the primitive builtin types within ASN.1 This class is used as a
 * 'key' for the Visitor.
 *
 * @author brightSPARK Labs
 */
public interface AsnPrimitiveTypes {
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** Invalid Null instance. */
    AsnPrimitiveTypes.Invalid INVALID = new AsnPrimitiveTypes.Invalid();

    /** Static instance {@link AsnPrimitiveTypeBitString}. */
    AsnPrimitiveTypeBitString BIT_STRING = new AsnPrimitiveTypeBitString();

    /** Static instance {@link AsnPrimitiveTypeBmpString}. */
    AsnPrimitiveTypeBmpString BMP_STRING = new AsnPrimitiveTypeBmpString();

    /** Static instance {@link AsnPrimitiveTypeBoolean}. */
    AsnPrimitiveTypeBoolean BOOLEAN = new AsnPrimitiveTypeBoolean();

    /** Static instance {@link AsnPrimitiveTypeCharacterString}. */
    AsnPrimitiveTypeCharacterString CHARACTER_STRING = new AsnPrimitiveTypeCharacterString();

    /** Static instance {@link AsnPrimitiveTypeChoice}. */
    AsnPrimitiveTypeChoice CHOICE = new AsnPrimitiveTypeChoice();

    /** Static instance {@link AsnPrimitiveTypeEmbeddedPdv}. */
    AsnPrimitiveTypeEmbeddedPdv EMBEDDED_PDV = new AsnPrimitiveTypeEmbeddedPdv();

    /** Static instance {@link AsnPrimitiveTypeEnumerated}. */
    AsnPrimitiveTypeEnumerated ENUMERATED = new AsnPrimitiveTypeEnumerated();

    /** Static instance {@link AsnPrimitiveTypeGeneralString}. */
    AsnPrimitiveTypeGeneralString GENERAL_STRING = new AsnPrimitiveTypeGeneralString();

    /** Static instance {@link AsnPrimitiveTypeGeneralizedTime}. */
    AsnPrimitiveTypeGeneralizedTime GENERALIZED_TIME = new AsnPrimitiveTypeGeneralizedTime();

    /** Static instance {@link AsnPrimitiveTypeGraphicString}. */
    AsnPrimitiveTypeGraphicString GRAPHIC_STRING = new AsnPrimitiveTypeGraphicString();

    /** Static instance {@link AsnPrimitiveTypeIA5String}. */
    AsnPrimitiveTypeIA5String IA5_STRING = new AsnPrimitiveTypeIA5String();

    /** Static instance {@link AsnPrimitiveTypeInteger}. */
    AsnPrimitiveTypeInteger INTEGER = new AsnPrimitiveTypeInteger();

    /** Static instance {@link AsnPrimitiveTypeNull}. */
    AsnPrimitiveTypeNull NULL = new AsnPrimitiveTypeNull();

    /** Static instance {@link AsnPrimitiveTypeNumericString}. */
    AsnPrimitiveTypeNumericString NUMERIC_STRING = new AsnPrimitiveTypeNumericString();

    /** Static instance {@link AsnPrimitiveTypeObjectDescriptor}. */
    AsnPrimitiveTypeObjectDescriptor OBJECT_DESCRIPTOR = new AsnPrimitiveTypeObjectDescriptor();

    /** Static instance {@link AsnPrimitiveTypeOctetString}. */
    AsnPrimitiveTypeOctetString OCTET_STRING = new AsnPrimitiveTypeOctetString();

    /** Static instance {@link AsnPrimitiveTypeOid}. */
    AsnPrimitiveTypeOid OID = new AsnPrimitiveTypeOid();

    /** Static instance {@link AsnPrimitiveTypePrintableString}. */
    AsnPrimitiveTypePrintableString PRINTABLE_STRING = new AsnPrimitiveTypePrintableString();

    /** Static instance {@link AsnPrimitiveTypeReal}. */
    AsnPrimitiveTypeReal REAL = new AsnPrimitiveTypeReal();

    /** Static instance {@link AsnPrimitiveTypeRelativeOid}. */
    AsnPrimitiveTypeRelativeOid RELATIVE_OID = new AsnPrimitiveTypeRelativeOid();

    /** Static instance {@link AsnPrimitiveTypeSequence}. */
    AsnPrimitiveTypeSequence SEQUENCE = new AsnPrimitiveTypeSequence();

    /** Static instance {@link AsnPrimitiveTypeSequenceOf}. */
    AsnPrimitiveTypeSequenceOf SEQUENCE_OF = new AsnPrimitiveTypeSequenceOf();

    /** Static instance {@link AsnPrimitiveTypeSet SET}. */
    AsnPrimitiveTypeSet SET = new AsnPrimitiveTypeSet();

    /** Static instance {@link AsnPrimitiveTypeSetOf}. */
    AsnPrimitiveTypeSetOf SET_OF = new AsnPrimitiveTypeSetOf();

    /** Static instance {@link AsnPrimitiveTypeTeletexString}. */
    AsnPrimitiveTypeTeletexString TELETEX_STRING = new AsnPrimitiveTypeTeletexString();

    /** Static instance {@link AsnPrimitiveTypeVideotexString}. */
    AsnPrimitiveTypeVideotexString VIDEOTEX_STRING = new AsnPrimitiveTypeVideotexString();

    /** Static instance {@link AsnPrimitiveTypeUniversalString}. */
    AsnPrimitiveTypeUniversalString UNIVERSAL_STRING = new AsnPrimitiveTypeUniversalString();

    /** Static instance {@link AsnPrimitiveTypeUtf8String}. */
    AsnPrimitiveTypeUtf8String UTF8_STRING = new AsnPrimitiveTypeUtf8String();

    /** Static instance {@link AsnPrimitiveTypeUtcTime}. */
    AsnPrimitiveTypeUtcTime UTC_TIME = new AsnPrimitiveTypeUtcTime();

    /** Static instance {@link AsnPrimitiveTypeVisibleString}. */
    AsnPrimitiveTypeVisibleString VISIBLE_STRING = new AsnPrimitiveTypeVisibleString();

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Null
    // -------------------------------------------------------------------------

    // TODO ASN-162 - we should consider getting rid of the "Null" classes.

    /**
     * Invalid instance of {@link AsnPrimitiveTypes}.
     *
     * <p>NOTE: This is not named {@code AsnSchemaPrimitiveTypeNull} because that is the name used
     * to model an actual ASN.1 {@code NULL} Type.
     */
    class Invalid implements com.brightsparklabs.asanti.schema.AsnPrimitiveType.Invalid {
        @Override
        public AsnBuiltinType getBuiltinType() {
            return AsnBuiltinType.Null;
        }

        @Override
        public Object accept(AsnPrimitiveTypeVisitor<?> visitor) {
            return visitor.visit(this);
        }
    }
}
