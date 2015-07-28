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

Given that the current version of Asanti are using Java 7 there is no 'good' language
support for parsing ISO8601 style data/time.  
Until the Asanti library moves to Java 8 we are using java.sql.Timestamp as the object to store
decoded ASN.1 GeneralizedTime data.
We are using [Joda-Time][http://www.joda.org/joda-time/] to do the majority of the grunt work of 
parsing.  Unfortunately Joda only provides millisecond precision/resolution, where the ASN.1 
standard defines essentially infinite precision.  The Timestamp object can handle nanosecond, so 
that is what we should be aiming for.  As a workaround there has been some extra parsing added to 
extract the sub milliseconds from the raw string.  This only works if the raw string is providing 
seconds and sub seconds is the decimal point.  That means that data of the form:
* "2000111213.1111111111111111111111111111111111111111"
* "200011121314.11111111111111111111111111111111111111"

will still only have millisecond data.  Note that the above two are legal ASN.1 GeneralizedTime
values, the first has the decimal places specifying fractions of the hour of the day, the second
has the decimal places specifying the fractions of the minute of the hour.  In either of these
cases we truncate down to 18 decimal places, the parsing goes directly to Joda-Time, and we don't 
"supplement" to nanosecond precision

Given that our end object (ie Timestamp) does not contain timezone information, and that we can
discard some precision, the decodeAsString function has been overridden and will return the "raw" 
string that was passed in, as long as it validated.  This allows the client to see the "extra" 
information that was originally passed in.

[validators]:     validators.md
