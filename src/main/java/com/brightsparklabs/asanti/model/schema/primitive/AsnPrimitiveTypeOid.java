package com.brightsparklabs.asanti.model.schema.primitive;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTagTypeVisitor;

/**
 * A type used to represent the primitive builtin type OBJECT IDENTIFIER within ASN.1
 * This class is used as a 'key' for the Visitor.
 * @author brightSPARK Labs
 */
public class AsnPrimitiveTypeOid implements AsnPrimitiveType
{
    @Override
    public AsnBuiltinType getBuiltinType()
    {
        return AsnBuiltinType.Oid;
    }

    @Override
    public Object visit(AsnSchemaTagTypeVisitor<?> visitor)
    {
        return visitor.visit(this);
    }

}
