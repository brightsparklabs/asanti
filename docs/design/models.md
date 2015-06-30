# Design Decisions and Concepts - Models

## Categories

There are two main categories of models:

1. `Data Models` which represent parsed ASN.1 binary data. These are:
    - `AsnData` representing ASN.1 binary data read from a BER/DER ASN.1 file.
    - `DecodedAsnData` representing ASN.1 binary data decoded against an ASN.1
      schema.

2. `Schema Models` which represent the elements in an ASN.1 schema. Some of
   these are:
    - `AsnSchema` representing the entire schema.
    - `AsnSchemaModule` representing a single module from a schema.
    - `AsnSchemaTypeDefinition` representing a named Type Definition from a
      module.
    - `AsnSchemaType` representing a single Type from a module.
    - `AsnPrimitiveType` represents an ASN.1 Type.

## Data Models

`Data Models` are the primary object that users of the API will interact with.
These are representative of the parsed ASN.1 binary data. Data from these models
can be sent to [Decoders][] to convert them into more user-friendly Java objects
such as `String`, `BigDecimal`, etc. The data can also be sent to [Validators][]
to check that they are correctly formed.

The `AsnData` model is the simplest model. It provides a means to retrieve the
elements from an binary BER/DER ASN.1 file. Users working with this model will
require an intimate knowledge of the schema used to encode the objects.

The `DecodedAsnData` model is an extension of the `AsnData` model. It has a link
back to the schema used to encode the data. This simplifies working with the
data in the file.

## Schema Models

`Schema Models` are support the classes used by the `DecodedAsnData` model to
map raw data back to a schema. The models themselves simply represent the
schema. They do not provide decoding or validation functionality, although they
contain the information required to do so. The purpose of this is to keep the
models simple, i.e. solely responsible for modeling an ASN.1 schema.

Decoding and validation requires knowledge of the underlying ASN.1 built-in
type behind a Type. The model which contains this information is the
`AsnPrimitiveType` interface, which is retrieved from an `AsnSchemaType`. 
This interface supports the Visitor pattern, specifically for the purpose 
of double dispatch. This allows [Decoders][] and [Validators][] to provide
orthogonal functionality without cluttering the code within `AsnSchemaType`
or `AsnPrimitiveType`.


[decoders]:   decoders.md
[validators]: validators.md
