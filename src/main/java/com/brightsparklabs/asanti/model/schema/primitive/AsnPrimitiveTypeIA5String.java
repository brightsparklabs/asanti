/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.primitive;

import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.schema.AsnPrimitiveType;
import com.brightsparklabs.asanti.schema.AsnPrimitiveTypeVisitor;

/**
 * A type used to represent the primitive builtin type IA5String within ASN.1 This class is used as
 * a 'key' for the Visitor.
 *
 * @author brightSPARK Labs
 */
public class AsnPrimitiveTypeIA5String implements AsnPrimitiveType.IA5String {
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnPrimitiveType
    // -------------------------------------------------------------------------

    @Override
    public AsnBuiltinType getBuiltinType() {
        return AsnBuiltinType.Ia5String;
    }

    @Override
    public Object accept(AsnPrimitiveTypeVisitor<?> visitor) {
        return visitor.visit(this);
    }
}
