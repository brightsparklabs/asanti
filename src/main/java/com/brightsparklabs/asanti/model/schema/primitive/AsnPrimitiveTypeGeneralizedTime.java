package com.brightsparklabs.asanti.model.schema.primitive;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnPrimitiveTypeVisitor;

/**
 * A type used to represent the primitive builtin type Generalized Time within ASN.1
 * This class is used as a 'key' for the Visitor.
 * @author brightSPARK Labs
 */
public class AsnPrimitiveTypeGeneralizedTime implements AsnPrimitiveType
{

    @Override
    public AsnBuiltinType getBuiltinType()
    {
        return AsnBuiltinType.GeneralizedTime;
    }

    @Override
    public Object visit(AsnPrimitiveTypeVisitor<?> visitor)
    {
        return visitor.visit(this);
    }

}

