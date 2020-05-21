/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.schema;

import com.brightsparklabs.asanti.visitor.Visitable;

/**
 * A base type used to represent the primitive builtin types within ASN.1 This class is used as a
 * 'key' for the Visitor.
 *
 * @author brightSPARK Labs
 */
public interface AsnPrimitiveType extends Visitable<AsnPrimitiveTypeVisitor<?>> {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the ASN.1 built-in type for this type
     *
     * @return the ASN.1 built-in type for this type
     */
    AsnBuiltinType getBuiltinType();

    // -------------------------------------------------------------------------
    // INTERNAL INTERFACES
    // -------------------------------------------------------------------------

    /** Invalid Null instance */
    interface Invalid extends AsnPrimitiveType {}

    /** Represents an ASN.1 BitString */
    interface BitString extends AsnPrimitiveType {}

    /** Represents an ASN.1 BmpString */
    interface BmpString extends AsnPrimitiveType {}

    /** Represents an ASN.1 Boolean */
    interface Boolean extends AsnPrimitiveType {}

    /** Represents an ASN.1 CharacterString */
    interface CharacterString extends AsnPrimitiveType {}

    /** Represents an ASN.1 Choice */
    interface Choice extends AsnPrimitiveType {}

    /** Represents an ASN.1 EmbeddedPdv */
    interface EmbeddedPdv extends AsnPrimitiveType {}

    /** Represents an ASN.1 Enumerated */
    interface Enumerated extends AsnPrimitiveType {}

    /** Represents an ASN.1 GeneralString */
    interface GeneralString extends AsnPrimitiveType {}

    /** Represents an ASN.1 GeneralizedTime */
    interface GeneralizedTime extends AsnPrimitiveType {}

    /** Represents an ASN.1 GraphicString */
    interface GraphicString extends AsnPrimitiveType {}

    /** Represents an ASN.1 IA5String */
    interface IA5String extends AsnPrimitiveType {}

    /** Represents an ASN.1 Integer */
    interface Integer extends AsnPrimitiveType {}

    /** Represents an ASN.1 Null */
    interface Null extends AsnPrimitiveType {}

    /** Represents an ASN.1 NumericString */
    interface NumericString extends AsnPrimitiveType {}

    /** Represents an ASN.1 ObjectDescriptor */
    interface ObjectDescriptor extends AsnPrimitiveType {}

    /** Represents an ASN.1 OctetString */
    interface OctetString extends AsnPrimitiveType {}

    /** Represents an ASN.1 Oid */
    interface Oid extends AsnPrimitiveType {}

    /** Represents an ASN.1 PrintableString */
    interface PrintableString extends AsnPrimitiveType {}

    /** Represents an ASN.1 Real */
    interface Real extends AsnPrimitiveType {}

    /** Represents an ASN.1 RelativeOid */
    interface RelativeOid extends AsnPrimitiveType {}

    /** Represents an ASN.1 Sequence */
    interface Sequence extends AsnPrimitiveType {}

    /** Represents an ASN.1 SequenceOf */
    interface SequenceOf extends AsnPrimitiveType {}

    /** Represents an ASN.1 Set SET */
    interface Set extends AsnPrimitiveType {}

    /** Represents an ASN.1 SetOf */
    interface SetOf extends AsnPrimitiveType {}

    /** Represents an ASN.1 TeletexString */
    interface TeletexString extends AsnPrimitiveType {}

    /** Represents an ASN.1 VideotexString */
    interface VideotexString extends AsnPrimitiveType {}

    /** Represents an ASN.1 UniversalString */
    interface UniversalString extends AsnPrimitiveType {}

    /** Represents an ASN.1 Utf8String */
    interface Utf8String extends AsnPrimitiveType {}

    /** Represents an ASN.1 UtcTime */
    interface UtcTime extends AsnPrimitiveType {}

    /** Represents an ASN.1 VisibleString */
    interface VisibleString extends AsnPrimitiveType {}
}
