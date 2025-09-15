# Asanti

[![Build Status](https://github.com/brightsparklabs/asanti/actions/workflows/java.yml/badge.svg)](https://github.com/brightsparklabs/asanti/actions/workflows/java.yml)
[![Maven](https://img.shields.io/maven-central/v/com.brightsparklabs/asanti)](https://search.maven.org/artifact/com.brightsparklabs/asanti)

A dynamic ASN.1 Parser for decoding tagged BER/DER data. Asanti allows a schema to be layered on top
of parsed data rather than forcing the schema to be modelled with concrete classes. This allows data
to be validated for conformance against a schema, whilst still allowing the ability to process and
manipulate non-conforming data.

## Prerequisites

- Java 17 or higher.

## Setup, Build and Run

```bash
# bash

git clone git@github.com:brightsparklabs/asanti.git
cd asanti
./gradlew build

# parse a schema
./gradlew installDist
./build/install/asanti/asanti.sh /path/to/example.asn
# parse a BER file
./build/install/asanti/asanti.sh /path/to/example.ber
# decode a BER file against schema
./build/install/asanti/asanti.sh /path/to/example.asn /path/to/example.ber TopLevelType
# decode a directory of BER files (recursively) against schema
./build/install/asanti/asanti.sh /path/to/example.asn /path/to/example/directory TopLevelType
```

### Publishing

New versions will be published to Maven Central by CI/CD when merged to `master`:

```bash
git flow relese start <x.y.z> ...
git flow release finish -m '<TICKET>: Tag v<x.y.z>'
git push --all --tags
```

If you need to manually publish a version, do so via:

```bash
git checkout master    # Or the specific version.

# NOTE: sonatype credentials are the API key.
export ORG_GRADLE_PROJECT_signingKey=$(gpg -a --export-secret-keys A068...) \
  ORG_GRADLE_PROJECT_signingPassword='REDACTED' \
  ORG_GRADLE_PROJECT_mavenCentralUsername='y...' \
  ORG_GRADLE_PROJECT_mavenCentralPassword='REDACTED'

./gradlew publishToMavenCentral
```

## Usage

##### Simple Parsing

```java
// java

// parse an ASN BER/DER binary file
final ImmutableList<RawAsnData> allRawAsnData = Asanti.readAsnBerFile(berFile);
final RawAsnData rawAsnData = allRawAsnData.first();

// print raw tags
rawAsnData.getRawTags();
/* => elements:
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
rawAsnData.getBytes("/1/0/1");
/* =>:
    [ 0x32, 0x30, 0x31, 0x30, 0x30, 0x34, 0x31, 0x33, 0x31, 0x34,
      0x30, 0x37, 0x35, 0x37, 0x2E, 0x37, 0x31, 0x32, 0x5A ]
*/
rawAsnData.getBytes("/50/0");
// => []
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
        dueDate [4] Date-Due DEFAULT week,
        version [5] SEQUENCE
        {
            majorVersion [0] INTEGER,
            minorVersion [1] INTEGER
        },
        description [6] SET
        {
            numberLines [0] INTEGER,
            summary     [1] OCTET STRING
        } OPTIONAL
    }

    Header ::= SEQUENCE
    {
        published [0] PublishedMetadata,
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
        paragraphs [2] SEQUENCE OF Paragraph,
        sections   [3] SET OF
                       SET
                       {
                            number [1] INTEGER,
                            text   [2] OCTET STRING
                       }
    }

    Paragraph ::=  SEQUENCE
    {
        title        [1] OCTET STRING,
        contributor  [2] Person OPTIONAL,
        points       [3] SEQUENCE OF OCTET STRING
    }

    References ::= SEQUENCE (SIZE (1..50)) OF
    SEQUENCE
    {
        title [1] OCTET STRING,
        url   [2] OCTET STRING
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
        gender        Gender OPTIONAL,
        maritalStatus CHOICE
            { Married [0], Single [1] }
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
// java
final ImmutableList<AsnData> allDecodedData = Asanti.decodeAsnData(berFile, schemaFile, "Document");
final AsnData asnData = allDecodedData.first();

// all decoded tags
asnData.getTags();
/* => elements:
    - "/Document/header/published/date"
    - "/Document/header/published/country"
    - "/Document/body/lastModified/date"
    - "/Document/body/prefix/text"
    - "/Document/body/content/text"
    - "/Document/footer/author/firstName"
*/

// unmapped tags (i.e. tags which do not exist in schema)
asnData.getUnmappedTags();
/* => elements:
    - "/Document/body/content/99"
    - "/Document/99/1/1"
*/

// test presence of tags (fully decoded)
asnData.contains("/Document/header/published/date"); // => true
asnData.contains("/Document/header/published/Date"); // => false (incorrect capitalization)
asnData.contains("/Document/header/published");      // => false ('published' is not a leaf node)
asnData.contains("/Document/body/prefix/text");      // => true
asnData.contains("/Document/body/suffix/text");      // => false (no 'suffix' node present)
asnData.contains("/Document/body/content/text");     // => true

// test presence of unmapped tags
asnData.contains("/Document/body/content/99");       // => true
asnData.contains("/Document/99/1/1");                // => true

// test presence of non-existent tags
asnData.contains("/Document/body/content/date");     // => false
asnData.contains("/Document/99/2/1");                // => false
asnData.contains("/99/2/1");                         // => false
asnData.contains("/Car/door/material");              // => false
```

##### Extracting decoded data

```java
// java

// get data as most appropriate Java Object via implicit cast (requires knowledge of schema)
final Timestamp date = asnData.getDecodedObject("/Document/header/published/date");
// => bytes decoded as a Timestamp

// get data as a printable string
asnData.getPrintableString("/Document/header/published/date");
// => "2010-04-13T14:07:57.712Z"

// get data as hex string
asnData.getHexString("/Document/header/published/date");
// => "0x32303130303431333134303735372E3731325A"

// get the data as bytes
final byte[] bytes = asnData.getBytes("/Document/header/published/date");
/* =>:
    [ 0x32, 0x30, 0x31, 0x30, 0x30, 0x34, 0x31, 0x33,
      0x31, 0x34, 0x30, 0x37, 0x35, 0x37, 0x2E, 0x37,
      0x31, 0x32, 0x5A ]
*/

// decode bytes as ASCII
final String ascii = ByteDecoder.asAscii(bytes);
// => "20100413140757.712Z"

// decode bytes as GeneralizedTime
final Timestamp timestamp = ByteDecoder.asGeneralizedTime(bytes);
```

##### Extracting decoded data via regular expressions

```java
// java

// get data as hex strings from all matching tags
asnData.getHexStringsMatching("/Document/header/published/.+");
// => map:
    - "/Document/header/published/date" => "0x32303130303431333134303735372E3731325A"
    - "/Document/header/published/country" => "0x4175737472616C6961"

// get group of bytes
asnData.getBytesMatching("/Document/header/published/.+");
/* => map:
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
// java

// parse an ASN BER/DER binary file
final ImmutableList<RawAsnData> allRawAsnData = Asanti.readAsnBerFile(berFile);
final AsnData rawAsnData = allRawAsnData.first();

// print raw tags
rawAsnData.getRawTags();
/* => elements:
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
final AsnSchema schema = AsnSchemaFileReader.read(schemaFile);
final AsnData asnData = Asanti.decodeAsnData(rawAsnData, schema);

// all decoded tags
asnData.getTags();
/* => elements:
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
asnData.getPrintableStringsMatching("/Document/Paragraph[.+]/Point[.+]");
/* => map:
    - "/Document/body/content/paragraph[1]/point[0]" => "The sky is blue."
    - "/Document/body/content/paragraph[1]/point[1]" => "The grass is green."
    - "/Document/body/content/paragraph[2]/point[0]" => "The dog is brown."
*/
```

##### Adding custom decoders

Say decoding the data using the default schema produces the following:

```java
// java

String tag = "/Document/header/published/date";
asnData.getPrintableString(tag);                         // => "2010-04-13T14:07:57.712Z"
final Timestamp date = asnData.getDecodedObject("tag");  // => bytes decoded as a Timestamp

tag = "/Document/body/lastModified/date";
asnData.getPrintableString(tag);                         // => "2014-04-13T14:07:57.712Z"
final Timestamp date = asnData.getDecodedObject(tag);    // => bytes decoded as a Timestamp
```

Custom decoders can be used to produce the following:

```java
// java

// create custom decoders
final CustomDecoder<Integer> relativeTimeGenerator = new RelativeTimeGenerator();
final CustomDecoder<Timestamp> offsetGenerator = new OffsetTimestampObjectGenerator(1000);

// add decoders to schema
final Schema customSchema = Schemas.from(schema)
    .withCustomDecoder(relativeTimeGenerator, "/Document/header/published/date")
    .withCustomDecoder(relativeTimeGenerator, "/Document/header/copyright/date")
    .withCustomDecoder(offsetGenerator, "/Document/body/lastModified/date")
    .build();

// decode using customised schema
final AsnData asnData = AsnAsanti.decodeAsnData(rawAsnData, customSchema);

String tag = "/Document/header/published/date";
asnData.getPrintableString(tag);                       // => "About 5 months ago"
final Integer date = asnData.getDecodedObject(tag);    // => bytes decoded as an Integer

tag = "/Document/body/lastModified/date";
asnData.getPrintableString(tag);                       // => "2014-04-13T14:07:58.712Z"
final Timestamp date = asnData.getDecodedObject(tag);  // => bytes decoded as a Timestamp with offset of 1000 ms
```

##### Validating decoded data

```java
// java

final Validator validator = Validators.getDefault();
final ValidationResult result = validator.validate(asnData);
final ValidationFailure failure = results.getFailures().first()

failure.getType();     // => MandatoryFieldMissing
failure.getTag();      // => "/Document/header/copyright/date"
failure.getMessage();  // => "The field /Document/header/copyright/date cannot be empty"
```

##### Adding custom validation rules

```java
// java

final ValidationRule rule = new DateCutoffValidationRule("2014-01-01");

final Validator customValidator = Validators.newCustomValidatorBuilder()
    .withValidationRule(rule, "/Document/header/published/date")
    .withValidationRule(rule, "/Document/header/copyright/date")
    .withValidationRule(rule, "/Document/body/lastModified/date")
    .build();

final ValidationResult result = customValidator.validate(asnData);
final ValidationFailure failure = results.getFailures().first()

failure.getFailureType();    // => DateCutoffFailed
failure.getFailureTag();     // => "/Document/header/published/date"
failure.getFailureReason();  // => "The date in field /Document/header/copyright/date cannot be before 2014-01-01"
```

## Licenses

Refer to the LICENSE file for details.

## Contributing

Contributions are welcome. Simply fork the repository and create a pull request with your suggested
changes.
