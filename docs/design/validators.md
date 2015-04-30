# Design Decisions and Concepts - Validators

## Categories

There are three main categories of validators:

1. `Byte Validators` which test if bytes correctly conform to the encoding
   rules used by various ASN.1 built-in types.

2. `Schema Validators` which test if `DecodedAsnData` models conform to the
   rules of the schema they correspond to.

3. `Custom Validators` which extend `Schema Validators` by allowing developers
   to add custom validation logic around specific `Type Definitions` in the
   ASN.1 schema.
