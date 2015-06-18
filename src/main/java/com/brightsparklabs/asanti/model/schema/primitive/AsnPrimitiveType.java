package com.brightsparklabs.asanti.model.schema.primitive;

import com.brightsparklabs.asanti.common.Visitable;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AbstractOLDAsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnPrimitiveTypeVisitor;

/**
 * A base type used to represent the primitive builtin types within ASN.1
 * This class is used as a 'key' for the Visitor.
 * @author brightSPARK Labs
 */
public interface AsnPrimitiveType extends Visitable<AsnPrimitiveTypeVisitor<?>>
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------
    public static final AsnPrimitiveType.Null NULL = new AsnPrimitiveType.Null();

    public static final AsnPrimitiveTypeBitString BIT_STRING = new AsnPrimitiveTypeBitString();
    public static final AsnPrimitiveTypeBoolean BOOLEAN = new AsnPrimitiveTypeBoolean();
    public static final AsnPrimitiveTypeChoice CHOICE = new AsnPrimitiveTypeChoice();
    public static final AsnPrimitiveTypeEnumerated ENUMERATED = new AsnPrimitiveTypeEnumerated();
    public static final AsnPrimitiveTypeGeneralizedTime GENERALIZED_TIME = new AsnPrimitiveTypeGeneralizedTime();
    public static final AsnPrimitiveTypeIA5String IA5_STRING = new AsnPrimitiveTypeIA5String();
    public static final AsnPrimitiveTypeInteger INTEGER = new AsnPrimitiveTypeInteger();
    public static final AsnPrimitiveTypeNumericString NUMERIC_STRING = new AsnPrimitiveTypeNumericString();
    public static final AsnPrimitiveTypeOctetString OCTET_STRING = new AsnPrimitiveTypeOctetString();
    public static final AsnPrimitiveTypeOid OID = new AsnPrimitiveTypeOid();
    public static final AsnPrimitiveTypePrintableString PRINTABLE_STRING = new AsnPrimitiveTypePrintableString();
    public static final AsnPrimitiveTypeRelativeOid RELATIVE_OID = new AsnPrimitiveTypeRelativeOid();
    public static final AsnPrimitiveTypeSequence SEQUENCE = new AsnPrimitiveTypeSequence();
    public static final AsnPrimitiveTypeSequenceOf SEQUENCE_OF = new AsnPrimitiveTypeSequenceOf();
    public static final AsnPrimitiveTypeSet SET = new AsnPrimitiveTypeSet();
    public static final AsnPrimitiveTypeSetOf SET_OF = new AsnPrimitiveTypeSetOf();
    public static final AsnPrimitiveTypeUtf8String UTF8_STRING = new AsnPrimitiveTypeUtf8String();
    public static final AsnPrimitiveTypeVisibleString VISIBLE_STRING = new AsnPrimitiveTypeVisibleString();

    /**
     * Returns the ASN.1 built-in type for this type
     *
     * @return the ASN.1 built-in type for this type
     */
    public AsnBuiltinType getBuiltinType();



    // -------------------------------------------------------------------------
    // INTERNAL CLASS:
    // -------------------------------------------------------------------------

    /**
     * NULL instance of {@link AbstractOLDAsnSchemaTypeDefinition}.
     * <p>
     * NOTE: This is not named {@code AsnSchemaTypeDefinitionNull} because that
     * is the name used to model an actual ASN.1 {@code NULL} Type Definition.
     */

    public static class Null implements AsnPrimitiveType
    {

        @Override
        public AsnBuiltinType getBuiltinType()
        {
            return AsnBuiltinType.Null;
        }

        @Override
        public Object visit(AsnPrimitiveTypeVisitor<?> visitor)
        {
            return visitor.visit(this);
        }

    }

}
