package com.brightsparklabs.asanti.model.schema.primitive;

import com.brightsparklabs.asanti.common.Visitable;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AbstractOLDAsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTagTypeVisitor;

/**
 * A base type used to represent the primitive builtin types within ASN.1
 * This class is used as a 'key' for the Visitor, and has no behaviour
 * @author brightSPARK Labs
 */
public interface AsnPrimitiveType extends Visitable<AsnSchemaTagTypeVisitor<?>>
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------
    public static final AsnPrimitiveType Null = new AsnPrimitiveType.Null();
    public static final AsnPrimitiveType Sequence = new AsnPrimitiveTypeSequence();
    public static final AsnPrimitiveType Choice = new AsnPrimitiveTypeChoice();
    public static final AsnPrimitiveType Set = new AsnPrimitiveTypeSet();
    public static final AsnPrimitiveType SequenceOf = new AsnPrimitiveTypeSequenceOf();
    public static final AsnPrimitiveType SetOf = new AsnPrimitiveTypeSetOf();
    public static final AsnPrimitiveType Utf8String = new AsnPrimitiveTypeUtf8String();
    public static final AsnPrimitiveType Integer = new AsnPrimitiveTypeInteger();
    public static final AsnPrimitiveType Oid = new AsnPrimitiveTypeOid();
    public static final AsnPrimitiveType PrintableString = new AsnPrimitiveTypePrintableString();
    public static final AsnPrimitiveType GeneralizedTime = new AsnPrimitiveTypeGeneralizedTime();
    public static final AsnPrimitiveType BitString = new AsnPrimitiveTypeBitString();
    public static final AsnPrimitiveType OctetString = new AsnPrimitiveTypeOctetString();


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
     * Null instance of {@link AbstractOLDAsnSchemaTypeDefinition}.
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
