# Asanti Design Decisions

## Component Type Pseudo Type Definitions

ASN.1 allows Component Types within a constructed type (SEQUENCE, SET, etc) to
be defined on the fly (i.e. without an explicit Type Definition). For example:


```
IRI-Parameters ::= SEQUENCE {
  -- component types
  -- ...

  callContentLinkInformation  [10] SEQUENCE {
    cCLink1Characteristics    [1] CallContentLinkCharacteristics OPTIONAL,
      -- Information concerning the Content of Communication Link Tx channel established 
      -- toward the LEMF (or the sum signal channel, in case of mono mode).
    cCLink2Characteristics    [2] CallContentLinkCharacteristics OPTIONAL,
      -- Information concerning the Content of Communication Link Rx channel established
      -- toward the LEMF.
    ...
  } OPTIONAL,

  -- component types
  -- ...
} 
```

The type callContentLinkInformation is being defined within the constructed
type (IRI-Parameters SEQUENCE) as opposed to being defined explicitly as a
Type Definition.  If it were defined as a Type Definition explicitly, the
fragment would look like:


```
IRI-Parameters ::= SEQUENCE {
  -- component types 
  -- ...

  callContentLinkInformation [10] CallContentLinkInformation OPTIONAL,

  -- component types
  -- ...
}

-- somewhere else in file CallContentLinkInformation ::= SEQUENCE {
  cCLink1Characteristics    [1] CallContentLinkCharacteristics OPTIONAL,
  cCLink2Characteristics    [2] CallContentLinkCharacteristics OPTIONAL, 
  ...
} 
```

We'll refer to the first example as an `Pseudo Type Definition`. To be
able to decode tags correctly for `Pseudo Type Definitions` we generate an
`AsnSchemaTypeDefinition` for it as if it were an explicit Type Definition. The
following sections describe the classes and methods involved in parsing Pseudo
Type Definitions.

### Identify Pseudo Type Definition

The `AsnSchemaComponentTypeParser.parseComponentType` method detects
the `Pseudo Type Definition` using a regular expression. A name in
the following format is generated for the `Pseudo Type Definition`:
```
<containingTypeName>.generated.<componentTypeName>
```

For the previous example the name would be:
```
IRI-Parameters.generated.callContentLinkInformation
```

The text of the `Pseudo Type Definition` is formatted to a single line of text. An
`AsnSchemaComponentTypeRaw` object storing the `AsnSchemaComponentType`,
generated name and text of the `Pseudo Type Definition` is returned from the
`AsnSchemaComponentTypeParser.parseComponentType` method.

The `AsnSchemaComponentTypeParser.parse` method processes the
`AsnSchemaComponentTypeRaw` objects to create a `ComponentTypeParseResult`
object which contains the following:

- `ImmutableList<AsnSchemaComponentType>` - containing all parsed Component
Types.
- `ImmutableMap<String, String>` - containing a map of `Pseudo Type
Definitions` where the generated name is the key and the Type Definition text
is the value.

### Parsing Pseudo Type Definitions

When parsing constructed types (SET, SEQUENCE or CHOICE) in the
`AsnSchemaTypeDefinitionParser` class, the Component Types and `Pseudo Type
Definitions` are extracted from the `ComponentTypeParseResult` object. The
constructed Type Definition is created using the parsed Component Types and the
`Pseudo Type Definitions` are parsed into Type Definitions using the parse methods
in the `AsnSchemaTypeDefinitionParser` class.

All parsed Type Definitions are returned in an
`ImmutableList<AbstractAsnSchemaTypeDefinition>` object from the
`AsnSchemaTypeDefinitionParser.parse` method. The `AsnSchemaModuleParser`
adds all Type Definitions returned to the `AsnSchemaModule.types` map.

### Decoding Tags for Pseudo Type Definitions

Given that `Pseudo Type Definitions` are parsed and added to the
`AsnSchemaModue.types` map, decoding tags that reference an `Pseudo Type
Definition` is essentially the same as if the tag were referencing an explicit
type definition. The only difference would be that the resulting type in the
`DecodedTag` object has a generated name.

