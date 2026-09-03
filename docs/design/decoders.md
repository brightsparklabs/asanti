# Design Decisions and Concepts - Decoders

## Categories

There are two main categories of decoders:

1. `Byte Decoders` which decode bytes that correctly conform to the encoding
   rules used by various ASN.1 built-in types. These are tested for conformance
   using [Validators][].

2. `Custom Decoders` which extend `Byte Decoders` by allowing developers
   to add custom decoding logic around specific `Type Definitions` in the
   ASN.1 schema.


## Notes about Byte Decoders

use this section to describe any non-obvious design/behaviour for the byte decoders

### GeneralizedTime

The ASN.1 GeneralizedTime type is an extension of VisibleString, the standard says:
`GeneralizedTime ::= [UNIVERSAL 24] IMPLICIT VisibleString` 

The decoder is implemented entirely on top of `java.time` (no third-party date/time libraries) and
produces a `java.time.OffsetDateTime`. Both `GeneralizedTime` and `UTCTime` share a single parser,
`AsnTimeParser`, which normalises the value (expanding `UTCTime`'s two-digit year, folding any
fraction-of-hour/minute and out-of-range offset into the value) and then parses a canonical
`YYYYMMDDHHMMSS[.fraction]` string with a strict `DateTimeFormatter`. The formatters deliberately
avoid optional sections (which roughly double parse cost); field ranges - including month lengths and
leap years - are validated strictly.

An ASN.1 GeneralizedTime has the form:

    YYYYMMDDHH[MM[SS]][(.|,)fraction][Z|(+|-)HH[MM]]

The optional fractional component applies to the smallest time unit that is present:
* fraction of a **second** when seconds are present,
* otherwise fraction of a **minute**,
* otherwise fraction of an **hour**.

All three cases are resolved to **nanosecond** precision (Java Limitation). For the fraction-of-second case, digits
finer than a nanosecond are truncated. This means that data of the form:
* "2000111213.5" (fraction of the hour of the day)
* "200011121314.5" (fraction of the minute of the hour)

are now resolved to nanosecond precision, unlike the previous Joda-Time based implementation which
only ever gave millisecond resolution for those two cases.

Timezone offsets are permitted up to &plusmn;23:59 (the full ASN.1 range, which is wider than
`java.time.ZoneOffset` can represent), so offsets are applied by computing the resulting `Instant`
directly. The returned `OffsetDateTime` is always expressed in the system default zone (the
`Instant` is what is significant); a lowercase `z` is rejected - the standard mandates an uppercase
`Z`.

Given that the returned `OffsetDateTime` does not preserve the original timezone specifier, and that
some fractional-second precision may be discarded, the `decodeAsString` function has been overridden
and will return the "raw" string that was passed in, as long as it validated. This allows the client
to see the "extra" information that was originally passed in.

[validators]:     validators.md
