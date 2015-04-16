/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

/**
 * The ASN.1 built-in types.
 *
 * @author brightSPARK Labs
 */
public enum AsnBuiltinType
{
    BitString,
    BmpString,
    Boolean,
    CharacterString,
    Choice,
    Date,
    DateTime,
    Duration,
    EmbeddedPDV,
    Enumerated,
    External,
    GeneralizedTime,
    GeneralString,
    GraphicString,
    Ia5String,
    InstanceOf,
    Integer,
    /** Internationalized Resource Identifier */
    IRI,
    Iso646String,
    Null,
    NumericString,
    ObjectClassField,
    OctetString,
    /** Object Identifier */
    Oid,
    /** Object Identifier (Internationalized Resource Identifier) */
    OidIri,
    Prefixed,
    PrintableString,
    Real,
    /** Relative Internationalized Resource Identifier */
    RelativeIri,
    /** Relative Object Identifier */
    RelativeOid,
    /** Relative Object Identifier (Internationalized Resource Identifier) */
    RelativeOidIri,
    Sequence,
    SequenceOf,
    Set,
    SetOf,
    TeletexString,
    Time,
    TimeOfDay,
    UniversalString,
    Utf8String,
    VideotexString,
    VisibleString
}
