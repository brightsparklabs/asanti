# Asanti

[![Build Status](https://travis-ci.org/brightsparklabs/asanti.svg?branch=master)](https://travis-ci.org/brightsparklabs/asanti)

A dynamic ASN.1 Parser for decoding tagged BER/DER data. Asanti allows a schema
to be layered on top of parsed data rather than forcing the schema to be
modeled with concrete classes. This allows data to be validated for conformance
against a schema, whilst still allowing the ability to process and manipulate
non-conforming data.

## Setup and Build

```bash
git clone git@github.com:brightsparklabs/asanti.git
cd asanti
./gradlew build
# generate eclipse classpath (optional)
./gradlew eclipse
# generate intellij classpath (optional)
./gradlew idea
```

## Usage

##### Simple Parsing

```java
// parse an ASN BER/DER binary file
final ImmutableList<AsnData> allAsnData = AsnDecoder.readAsnBerFile(berFile);
final AsnData asnData = allAsnData.first();

// print raw tags
asnData.getRawTags();
/* returns elements:
    - "/1/0/1"
    - "/1/0/2"
    - "/2/0/0"
    - "/2/1/1"
    - "/2/2/1"
    - "/3/0/1"
    - "/3/0/99"
    - "/99/1/1"
*/

// get the data as bytes
asnData.getBytes("/1/0/1");
/* returns:
    [ 0x32, 0x30, 0x31, 0x30, 0x30, 0x34, 0x31, 0x33, 0x31, 0x34,
      0x30, 0x37, 0x35, 0x37, 0x2E, 0x37, 0x31, 0x32, 0x5A ]
*/
asnData.getBytes("/50/0");
// returns []
```

##### Decoding data against a schema

Consider the following ASN.1 schema:

```asn
Document-PDU
    { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) document(1) }

DEFINITIONS
    AUTOMATIC TAGS ::=

BEGIN
    EXPORTS Header, Body;

    IMPORTS
      People,
      Person
        FROM People-Protocol
        { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) };

    Document ::= SEQUENCE
    {
        header  [1] Header,
        body    [2] Body,
        footer  [3] Footer,
        dueDate [4] Date-Due
    }

    Header ::= SEQUENCE
    {
        published [0] PublishedMetadata
    }

    Body ::= SEQUENCE
    {
        lastModified [0] ModificationMetadata,
        prefix       [1] Section-Note OPTIONAL,
        content      [2] Section-Main,
        suffix       [3] Section-Note OPTIONAL
    }

    Footer ::= SET
    {
        authors [0] People
    }

    PublishedMetadata ::= SEQUENCE
    {
        date    [1] GeneralizedTime,
        country [2] OCTET STRING OPTIONAL
    }

    ModificationMetadata ::= SEQUENCE
    {
        date       [0] DATE,
        modifiedBy [1] Person
    }

    Section-Note ::= SEQUENCE
    {
        text [1] OCTET STRING
    }

    Section-Main ::= SEQUENCE
    {
        text       [1] OCTET STRING OPTIONAL,
        paragraphs [2] SEQUENCE OF Paragraph
    }

    Paragraph ::=  SEQUENCE
    {
        title        [1] OCTET STRING,
        contributor  [2] Person OPTIONAL,
        points       [3] SEQUENCE OF OCTET STRING
    }

    Date-Due ::= INTEGER
    {
      tomorrow(0),
      three-day(1),
      week (2)
    } DEFAULT week

END

People-Protocol
    { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }

DEFINITIONS
    AUTOMATIC TAGS ::=

BEGIN

    defaultAge INTEGER ::= 45

    People ::= SET OF Person

    Person ::= SEQUENCE
    {
        firstName [1] OCTET STRING,
        lastName  [2] OCTET STRING,
        title     [3] ENUMERATED
            { mr, mrs, ms, dr, rev } OPTIONAL,
        gender        Gender OPTIONAL
    }

    Gender ::= ENUMERATED
    {
        male(0),
        female(1)
    }

END
```

Decoding data against the schema can be achieved via:

```java
final ImmutableList<DecodedAsnData> allDecodedData = AsnDecoder.decodeAsnData(berFile, schemaFile, "Document");
final DecodedAsnData allDecodedData = allDecodedData.first();

// all decoded tags
decodedData.getTags();
/* returns elements:
    - "/Document/header/published/date"
    - "/Document/header/published/country"
    - "/Document/body/lastModified/date"
    - "/Document/body/prefix/text"
    - "/Document/body/content/text"
    - "/Document/footer/author/firstName"
*/

// unmapped tags (i.e. tags which do not exist in schema)
decodedData.getUnmappedTags();
/* returns elements:
    - "/Document/body/content/99"
    - "/Document/99/1/1"
*/

// test presence of tags (fully decoded)
decodedData.contains("/Document/header/published/date"); // returns true
decodedData.contains("/Document/header/published/Date"); // returns false (incorrect capitalization)
decodedData.contains("/Document/header/published");      // returns false ('published' is not a leaf node)
decodedData.contains("/Document/body/prefix/text");      // returns true
decodedData.contains("/Document/body/suffix/text");      // returns false (no 'suffix' node present)
decodedData.contains("/Document/body/content/text");     // returns true

// test presence of unmapped tags
decodedData.contains("/Document/body/content/99");       // returns true
decodedData.contains("/Document/99/1/1");                // returns true

// test presence of non-existent tags
decodedData.contains("/Document/body/content/date");     // returns false
decodedData.contains("/Document/99/2/1");                // returns false
decodedData.contains("/99/2/1");                         // returns false
decodedData.contains("/Car/door/material");              // returns false
```

##### Extracting decoded data

```java
// get data as most appropriate Java Object via explicit cast (requires knowledge of schema)
final Timestamp date = (Timestamp) decodedData.getDecodedObject("/Document/header/published/date");
// returns bytes decoded as a Timestamp

// get data as a printable string
decodedData.getPrintableString("/Document/header/published/date");
// returns "2010-04-13T14:07:57.712Z"

// get data as hex string
decodedData.getHexString("/Document/header/published/date");
// returns "0x32303130303431333134303735372E3731325A"

// get the data as bytes
final byte[] bytes = decodedData.getBytes("/Document/header/published/date");
/* returns:
    [ 0x32, 0x30, 0x31, 0x30, 0x30, 0x34, 0x31, 0x33,
      0x31, 0x34, 0x30, 0x37, 0x35, 0x37, 0x2E, 0x37,
      0x31, 0x32, 0x5A ]
*/

// decode bytes as ASCII
final String ascii = ByteDecoder.asAscii(bytes);
// returns "20100413140757.712Z"

// decode bytes as GeneralizedTime
final Timestamp timestamp = ByteDecoder.asGeneralizedTime(bytes);
```

##### Extracting decoded data via regular expressions

```java
// get data as hex strings from all matching tags
decodedData.getHexStringsMatching("/Document/header/published/.+");
// returns map:
    - "/Document/header/published/date" => "0x32303130303431333134303735372E3731325A"
    - "/Document/header/published/country" => "0x4175737472616C6961"

// get group of bytes
decodedData.getBytesMatching("/Document/header/published/.+");
/* returns map:
    - "/Document/header/published/date" =>
          [ 0x32, 0x30, 0x31, 0x30, 0x30, 0x34, 0x31, 0x33,
            0x31, 0x34, 0x30, 0x37, 0x35, 0x37, 0x2E, 0x37,
            0x31, 0x32, 0x5A ]
    - "/Document/header/published/country" =>
          [ 0x54, 0x68, 0x65, 0x20, 0x67, 0x72, 0x61, 0x73,
            0x73, 0x20, 0x69, 0x73, 0x20, 0x67, 0x72, 0x65,
            0x65, 0x6E, 0x2E ]
*/
```

### Working with `SET OF` and `SEQUENCE OF`

```java
// parse an ASN BER/DER binary file
final ImmutableList<AsnData> allAsnData = AsnDecoder.readAsnBerFile(berFile);
final AsnData asnData = allAsnData.first();

// print raw tags
asnData.getRawTags();
/* returns elements:
    - "/2/2/2[0]/1"
    - "/2/2/2[0]/2/1"
    - "/2/2/2[1]/1"
    - "/2/2/2[1]/3[0]"
    - "/2/2/2[1]/3[1]"
    - "/2/2/2[2]/1"
    - "/2/2/2[2]/2/1"
    - "/2/2/2[2]/2/2"
    - "/2/2/2[2]/3[0]"
*/

// decode against schema
final AsnSchema schema = AsnSchema.builder()
    .fromXsd(xsdFile)
    .build();
final DecodedAsnData decodedData = AsnDecoder.decodeAsnData(asnData, schema);

// all decoded tags
decodedData.getTags();
/* returns elements:
    - "/Document/body/content/paragraph[0]/title"
    - "/Document/body/content/paragraph[0]/contributor/firstName"
    - "/Document/body/content/paragraph[1]/title"
    - "/Document/body/content/paragraph[1]/point[0]"
    - "/Document/body/content/paragraph[1]/point[1]"
    - "/Document/body/content/paragraph[2]/title"
    - "/Document/body/content/paragraph[2]/contributor/firstName
    - "/Document/body/content/paragraph[2]/contributor/lastName"
    - "/Document/body/content/paragraph[2]/point[0]"
*/

// get data as printable strings from all matching tags
decodedData.getPrintableStringsMatching("/Document/Paragraph[.+]/Point[.+]");
/* returns map:
    - "/Document/body/content/paragraph[1]/point[0]" => "The sky is blue."
    - "/Document/body/content/paragraph[1]/point[1]" => "The grass is green."
    - "/Document/body/content/paragraph[2]/point[0]" => "The dog is brown."
*/
```

##### Adding custom data generators

```java
// default generator
decodedData.getPrintableString("/Document/header/published/date");
// returns "2010-04-13T14:07:57.712Z"
final Timestamp date = (Timestamp) decodedData.getDecodedObject("/Document/header/published/date");
// returns bytes decoded as a Timestamp

decodedData.getPrintableString("/Document/body/lastModified/date");
// returns "2014-04-13T14:07:57.712Z"
final Timestamp date = (Timestamp) decodedData.getDecodedObject("/Document/body/lastModified/date");
// returns bytes decoded as a Timestamp

// use custom data generators
final DataGenerator<Integer> relativeTimeGenerator = new RelativeTimeGenerator();
final DataGenerator<Timestamp> offsetGenerator = new OffsetTimestampObjectGenerator(1000);

final DataGenerators generators = DataGenerators.builder()
    .withDataGenerator(relativeTimeGenerator, "/Document/header/published/date")
    .withDataGenerator(relativeTimeGenerator, "/Document/header/copyright/date")
    .withDataGenerator(offsetGenerator, "/Document/body/lastModified/date")
    .build();

final DecodedAsnData decodedData = AsnDecoder.decodeAsnData(asnData, schema, generators);

decodedData.getPrintableString("/Document/header/published/date");
// returns "About 5 months ago"
final Integer date = (Integer) decodedData.getDecodedObject("/Document/header/published/date");
// returns bytes decoded as an Integer

decodedData.getPrintableString("/Document/body/lastModified/date");
// returns "2014-04-13T14:07:58.712Z"
final Timestamp date = (Timestamp) decodedData.getDecodedObject("/Document/body/lastModified/date");
// returns bytes decoded as a Timestamp with offset of 1000 ms
```

##### Validating decoded data

```java
final Validator validator = Validator.getInstance();
final ValidationResult result = validator.validate(decodedAsnData);
final ValidationFailure failure = results.getFailures().first()

failure.getType();     // returns MandatoryFieldMissing
failure.getTag();      // returns "/Document/header/copyright/date"
failure.getMessage();  // returns "The field /Document/header/copyright/date cannot be empty"
```

##### Adding custom validation rules

```java
final ValidationRule rule = new DateCutoffValidationRule("2014-01-01");

final Validator customValidator = Validator.builder()
    .withValidationRule(rule, "/Document/header/published/date")
    .withValidationRule(rule, "/Document/header/copyright/date")
    .withValidationRule(rule, "/Document/body/lastModified/date")
    .build();

final ValidationResult result = customValidator.validate(decodedAsnData);
final ValidationFailure failure = results.getFailures().first()

failure.getType();     // returns DateCutoffFailed
failure.getTag();      // returns "/Document/header/published/date"
failure.getMessage();  // returns "The date in field /Document/header/copyright/date cannot be before 2014-01-01"
```

## Licenses

Refer to the LICENSE file for details.

This project makes use of the following libraries:

- [Bouncy Castle Crypto APIs](http://www.bouncycastle.org/)
- [Google Guava](https://github.com/google/guava)
- [Hamcrest](http://hamcrest.org/)
- [JUnit](http://junit.org/)
- [PowerMock](https://code.google.com/p/powermock/)

## Contributing
Contributions are welcome. Simply fork the repository and create a pull request with your suggested changes.
