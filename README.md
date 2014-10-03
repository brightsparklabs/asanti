# Asanti

A dynamic ASN.1 Parser for decoding tagged BER/DER data. Asanti allows a schema
to be layered on top of parsed data rather than forcing the schema to be
modeled with concrete classes. This allows data to be validated for conformance
against a schema, whilst still allowing the ability to process and manipulate
non-conforming data.

## Setup and Build

```bash
git clone git@github.com:brightsparklabs/asanti.git
cd asanti
gradle build
# generate eclipse classpath (optional)
gradle eclipse
```

## Usage

##### Simple Parsing

```java
// parse an ASN BER/DER binary file
final ImmutableList<AsnData> allAsnData = AsnDecoder.readAsnFile(berFile);
final AsnData asnData = allAsnData.first();

// print raw tags
asnData.getTags();
/* returns elements:
    - "/1/0/1"
    - "/2/0/0"
    - "/2/1/1"
    - "/2/2/1"
    - "/3/0/1"
    - "/3/0/99"
    - "/99/1/1"
*/
```

##### Decoding data against a schema

```java
final AsnSchema schema = AsnSchema.builder()
    .fromXsd(xsdFile)
    .build();
final DecodedAsnData decodedData = AsnDecoder.decodeAsnData(asnData, schema);

decodedData.getPath();
// returns "/"

// all decoded tags
decodedData.getTags();
/* returns elements:
     - "/Header/Published/Date"
     - "/Body/LastModified/Date"
     - "/Body/Prefix/Text"
     - "/Body/Content/Text"
     - "/Footer/Author/FirstName"
*/

// unmapped tags (i.e. tags which do not exist in schema)
decodedData.getUnmappedTags();
/* returns elements:
     - "/Body/Content/99"
     - "/99/1/1"
*/

// test presence of tags
decodedData.contains("/Header/Published/Date"); // returns true
decodedData.contains("/Header/Published");      // returns true
decodedData.contains("/Body/Prefix/Text");      // returns true
decodedData.contains("/Body/Suffix/Text");      // returns false
decodedData.contains("/99/1/1");                // returns false
decodedData.contains("/Car/Door/Material");     // returns false
```

##### Working with sub-structures

```java
// select a child structure
final DecodedData bodyDecodedData = decodedData.getChild("/Body");

bodyDecodedData.getPath();
// returns "/Body"

// all decoded tags
bodyDecodedData.getTags();
/* returns elements:
     - "/Body/LastModified/Date"
     - "/Body/Prefix/Text"
     - "/Body/Content/Text"
*/

// unmapped tags (i.e. tags which do not exist in schema)
bodyDecodedData.getUnmappedTags();
/* returns elements:
     - "/Body/Content/99"
*/

// test presence of tags
bodyDecodedData.contains("/Body/Prefix/Text");      // returns true
bodyDecodedData.contains("/Prefix/Text");           // returns true (relative path supported)
bodyDecodedData.contains("/Header/Published/Date"); // returns false
bodyDecodedData.contains("/Header/Published");      // returns false
bodyDecodedData.contains("/Body/Suffix/Text");      // returns false
bodyDecodedData.contains("/99/1/1");                // returns false
bodyDecodedData.contains("/Car/Door/Material");     // returns false
```

##### Working with `SET OF` and `SEQUENCE OF`

```java
// TBD
```

##### Extracting decoded data

```java
// get data as most appropriate Java Object via explicit cast (requires knowledge of schema)
final Timestamp date = (Timestamp) decodedData.getObject("/Header/Published/Date");
// returns bytes decoded as a Timestamp

// get data as a printable string
decodedData.getPrintableString("/Header/Published/Date");
// returns "2010-04-13T14:07:57.712Z"

// get data as hex string
decodedData.getHexString("/Header/Published/Date");
// returns "0x32303130303431333134303735372E3731325A"

// get the data as bytes
final byte[] bytes = decodedData.getBytes("/Header/Published/Date");
/* returns:
     [ 0x32, 0x30, 0x31, 0x30, 0x30, 0x34, 0x31, 0x33, 0x31, 0x34,
       0x30, 0x37, 0x35, 0x37, 0x2E, 0x37, 0x31, 0x32, 0x5A ]
*/

// decode bytes as ASCII
final String ascii = ByteDecoder.asAscii(bytes);
// returns "20100413140757.712Z"

// decode bytes as GeneralizedTime
final Timestamp timestamp = ByteDecoder.asGeneralizedTime(bytes);
```

##### Adding custom data generators

```java
// default generator
decodedData.getPrintableString("/Header/Published/Date");
// returns "2010-04-13T14:07:57.712Z"
final Timestamp date = (Timestamp) decodedData.getObject("/Header/Published/Date");
// returns bytes decoded as a Timestamp

decodedData.getPrintableString("/Body/LastModified/Date");
// returns "2014-04-13T14:07:57.712Z"
final Timestamp date = (Timestamp) decodedData.getObject("/Body/LastModified/Date");
// returns bytes decoded as a Timestamp

// use custom data generators
final DataGenerator<Integer> relativeTimeGenerator = new RelativeTimeGenerator();
final DataGenerator<Timestamp> offsetGenerator = new OffsetTimestampObjectGenerator(1000);

final DataGenerators generators = DataGenerators.builder()
    .withDataGenerator(relativeTimeGenerator, "/Header/Published/Date")
    .withDataGenerator(relativeTimeGenerator, "/Header/Copyright/Date")
    .withDataGenerator(offsetGenerator, "/Body/LastModified/Date")
    .build();

final DecodedAsnData decodedData = AsnDecoder.decodeAsnData(asnData, schema, generators);

decodedData.getPrintableString("/Header/Published/Date");
// returns "About 5 months ago"
final Integer date = (Integer) decodedData.getObject("/Header/Published/Date");
// returns bytes decoded as an Integer

decodedData.getPrintableString("/Body/LastModified/Date");
// returns "2014-04-13T14:07:58.712Z"
final Timestamp date = (Timestamp) decodedData.getObject("/Body/LastModified/Date");
// returns bytes decoded as a Timestamp with offset of 1000 ms
```

##### Validating decoded data

```java
final Validator validator = Validator.getInstance();
final ValidationResult result = validator.validate();
final ValidationFailure failure = results.getFailures().first()

failure.getType();     // returns MandatoryFieldMissing
failure.getLocation(); // returns "/Header/Copyright/Date"
failure.getMessage();  // returns "The field /Header/Copyright/Date cannot be empty"
```

##### Adding custom validation rules

```java
final ValidationRule rule = new DateCutoffValidationRule("2014-01-01");

final Validator customValidator = Validator.builder()
    .withValidationRule(rule, "/Header/Published/Date")
    .withValidationRule(rule, "/Header/Copyright/Date")
    .withValidationRule(rule, "/Body/LastModified/Date")
    .build();

final ValidationResult result = customValidator.validate();
final ValidationFailure failure = results.getFailures().first()

failure.getType();     // returns DateCutoffFailed
failure.getLocation(); // returns "/Header/Published/Date"
failure.getMessage();  // returns "The date in field /Header/Copyright/Date cannot be before 2014-01-01"
```

## Contributing
Contributions are welcome. Simply fork the repository and create a pull request with your suggested changes.
