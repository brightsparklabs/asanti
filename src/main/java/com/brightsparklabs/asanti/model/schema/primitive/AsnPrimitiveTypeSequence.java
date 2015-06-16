package com.brightsparklabs.asanti.model.schema.primitive;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTagTypeVisitor;

/**
 * Created by Michael on 16/06/2015.
 */
public class AsnPrimitiveTypeSequence implements AsnPrimitiveType
{

    @Override
    public AsnBuiltinType getBuiltinType()
    {
        return AsnBuiltinType.Sequence;
    }

    @Override
    public Object visit(AsnSchemaTagTypeVisitor<?> visitor)
    {
        return visitor.visit(this);
    }

}
