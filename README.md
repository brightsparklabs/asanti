# Asanti

A dynamic ASN.1 Parser for decoding tagged BER/DER data. Asanti allows a schema
to be retrospectively applied on top of the data rather forcing data to be
parsed in accordance with a schema. This allows data to be validated for
conformance against a schema, whilst still allowing the ability to process and
manipulate non-conforming data.

## Setup and Build

```bash
git clone git@github.com:brightsparklabs/asanti.git
cd asanti
gradle build

# (optional) generate eclipse classpath
gradle eclipse

```

## Usage

##### Simple Parsing

```java
// parse an ASN BER/DER binary file
final ImmutableList<AsnData> allAsnData = AsnDecoder.readAsnFile(file);
final AsnData asnData = allAsnData.first();

// print raw tags
for (String tag : asnData.getTags())
{
    System.out.println(tag);
    /* Returns:
     *   /1/0/1
     *   /2/0/0
     *   /2/1/1
     *   /3/0/1
     *   /3/0/99
     *   /99/1/1
     */
}
```

##### Decode AsnData using a schema

```java
final AsnSchema schema = AsnSchema.fromXsd(file);
final DecodedAsnData decodedData = AsnDecoder.decodeAsnData(asnData, schema);

// print decoded tags
for (String tag : decodedData.getTags())
{
    System.out.println(tag);
    /* Returns:
     *   /Header/Published/Date
     *   /Body/Prefix/Text
     *   /Body/Content/Text
     *   /Footer/Author/FirstName
     */
}

// print unmapped tags (i.e. tags which do not exist in schema)
for (String tag : decodedData.getUnmappedTags())
{
    System.out.println(tag);
    /* Returns:
     *   /Body/Content/99
     *   /99/1/1
     */
}

// test presence of tags
decodedData.contains("/Header/Published/Date"); // returns true
decodedData.contains("/Header/Published");      // returns true
decodedData.contains("/Body/Prefix/Text");      // returns true
decodedData.contains("/Body/Suffix/Text");      // returns false
decodedData.contains("/99/1/1");                // returns false
decodedData.contains("/Car/Door/Material");     // returns false

```

##### Extract decoded data

```java
```

##### Working with sub-structures

```java
```

##### Working with `SET OF` and `SEQUENCE OF`

```java
```

##### Adding custom string renderers

```java
```

##### Adding custom byte processors

```java
```

##### Validating decoded data

```java
```

## Contributing
Contributions are welcome. Simply fork the repository and create a pull request with your suggested changes.
