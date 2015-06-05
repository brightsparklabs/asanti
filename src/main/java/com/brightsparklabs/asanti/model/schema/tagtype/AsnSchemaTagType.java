package com.brightsparklabs.asanti.model.schema.tagtype;

import com.brightsparklabs.asanti.common.Visitable;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTagTypeVisitor;

/**
 * A type used by a component from a within a module specification within an ASN.1
 * schema.
 *
 * @author brightSPARK Labs
 */
public interface AsnSchemaTagType extends Visitable<AsnSchemaTagTypeVisitor<?>>
{


    /**
     * Returns the ASN.1 built-in type for this tagtype
     *
     * @return the ASN.1 built-in type for this tagtype
     */
    public AsnBuiltinType getBuiltinType();

    /**
     * Returns the constraint associated with this tagtype
     *
     * @return the constraint associated with this tagtype or
     *         {@link AsnSchemaConstraint#NULL} if there is no constraint.
     */
    public AsnSchemaConstraint getConstraint();

}
