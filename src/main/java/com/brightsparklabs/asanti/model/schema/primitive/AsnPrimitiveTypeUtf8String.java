package com.brightsparklabs.asanti.model.schema.primitive;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTagTypeVisitor;

/**
 * A type used to represent the primitive builtin types within ASN.1
 * This class is used as a 'key' for the Visitor.
 * @author brightSPARK Labs
 */
public class AsnPrimitiveTypeUtf8String implements AsnPrimitiveType
{

    protected AsnPrimitiveTypeUtf8String() {}

    @Override
    public AsnBuiltinType getBuiltinType()
    {
        return AsnBuiltinType.Utf8String;
    }

    @Override
    public Object visit(AsnSchemaTagTypeVisitor<?> visitor)
    {
        return visitor.visit(this);
    }

}
