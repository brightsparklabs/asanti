package com.brightsparklabs.asanti.model.schema.primitive;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTagTypeVisitor;

/**
 * A type used to represent the primitive builtin type Octet String within ASN.1
 * This class is used as a 'key' for the Visitor.
 * @author brightSPARK Labs
 */
public class AsnPrimitiveTypeOctetString implements AsnPrimitiveType
{

    @Override
    public AsnBuiltinType getBuiltinType()
    {
        return AsnBuiltinType.SequenceOf;
    }

    @Override
    public Object visit(AsnSchemaTagTypeVisitor<?> visitor)
    {
        return visitor.visit(this);
    }

}
