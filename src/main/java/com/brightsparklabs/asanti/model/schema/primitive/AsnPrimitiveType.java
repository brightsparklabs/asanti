package com.brightsparklabs.asanti.model.schema.primitive;

import com.brightsparklabs.asanti.common.Visitable;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AbstractOLDAsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTagTypeVisitor;

/**
 * A base type used to represent the primitive builtin types within ASN.1
 * This class is used as a 'key' for the Visitor.
 * @author brightSPARK Labs
 */
public interface AsnPrimitiveType extends Visitable<AsnSchemaTagTypeVisitor<?>>
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------
    public static final AsnPrimitiveType NULL = new AsnPrimitiveType.Null();
    public static final AsnPrimitiveType SEQUENCE = new AsnPrimitiveTypeSequence();
    public static final AsnPrimitiveType CHOICE = new AsnPrimitiveTypeChoice();
    public static final AsnPrimitiveType SET = new AsnPrimitiveTypeSet();
    public static final AsnPrimitiveType SEQUENCE_OF = new AsnPrimitiveTypeSequenceOf();
    public static final AsnPrimitiveType SET_OF = new AsnPrimitiveTypeSetOf();
    public static final AsnPrimitiveType UTF8_STRING = new AsnPrimitiveTypeUtf8String();
    public static final AsnPrimitiveType INTEGER = new AsnPrimitiveTypeInteger();
    public static final AsnPrimitiveType OID = new AsnPrimitiveTypeOid();
    public static final AsnPrimitiveType PRINTABLE_STRING = new AsnPrimitiveTypePrintableString();
    public static final AsnPrimitiveType GENERALIZED_TIME = new AsnPrimitiveTypeGeneralizedTime();
    public static final AsnPrimitiveType BIT_STRING = new AsnPrimitiveTypeBitString();
    public static final AsnPrimitiveType OCTET_STRING = new AsnPrimitiveTypeOctetString();
    public static final AsnPrimitiveType IA5_STRING = new AsnPrimitiveTypeIA5String();
    public static final AsnPrimitiveType VISIBLE_STRING = new AsnPrimitiveTypeVisibleString();
    public static final AsnPrimitiveType NUMERIC_STRING = new AsnPrimitiveTypeNumericString();
    public static final AsnPrimitiveType ENUMERATED = new AsnPrimitiveTypeEnumerated();
    public static final AsnPrimitiveType RELATIVE_OID = new AsnPrimitiveTypeRelativeOid();

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
        public Object visit(AsnSchemaTagTypeVisitor<?> visitor)
        {
            return visitor.visit(this);
        }

    }

}
