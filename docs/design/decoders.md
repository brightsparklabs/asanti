# Design Decisions and Concepts - Decoders

## Categories

There are two main categories of decoders:

1. `Byte Decoders` which decode bytes that correctly conform to the encoding
   rules used by various ASN.1 built-in types. These are tested for conformance
   using [Validators][].

2. `Custom Decoders` which extend `Byte Decoders` by allowing developers
   to add custom decoding logic around specific `Type Definitions` in the
   ASN.1 schema.


[validators]:     validators.md
