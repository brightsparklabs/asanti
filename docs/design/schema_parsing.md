# Design Decisions and Concepts - Schema Parsing

## Architecture

The components which handle parsing an ASN.1 Schema file have been broken out
into a series of nested classes. Each class handles a specific area of the
schema and delegates downwards to lower level parsers.

The hierarchy is as follows:

- `AsnSchemaParser`
    - `AsnSchemaModuleParser`
        - `AsnSchemaImportsParser`
        - `AsnSchemaTypeDefinitionParser`
            - `AsnSchemaConstraintParser`
            - `AsnSchemaComponentTypeParser`
            - `AsnSchemaNamedTagParser`

The general entry point to the parser framework is `AsnSchemaParser`. It
delegates downwards to each parser as required. Each parser is responsible for
returning objects as indicated by its name. E.g.

- `AsnSchemaModuleParser` returns `AsnSchemaModule` objects
- `AsnSchemaTypeDefinitionParser` returns `AsnSchemaTypeDefinition` objects

## Component Type Pseudo Type Definitions

ASN.1 allows Component Types within a constructed type (SEQUENCE, SET, etc) to
be defined on the fly (i.e. without an explicit Type Definition). For example:

```
IRI-Parameters ::= SEQUENCE
{
  -- component types
  -- ...

  callContentLinkInformation  [10] SEQUENCE
  {
    cCLink1Characteristics    [1] CallContentLinkCharacteristics OPTIONAL,
    cCLink2Characteristics    [2] CallContentLinkCharacteristics OPTIONAL,
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
IRI-Parameters ::= SEQUENCE
{
  -- component types
  -- ...

  callContentLinkInformation [10] CallContentLinkInformation OPTIONAL,

  -- component types
  -- ...
}

-- somewhere else in file
CallContentLinkInformation ::= SEQUENCE
{
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
generated.<containingTypeName>.<componentTypeName>
```

The use of a lowercase type reference name containing dots is invalid in
ASN.1. Using this format:

1. Ensures there are no clashes with valid type reference names
1. Makes it explicitly clear that the name was generated.

For the previous example the name would be:

```
generated.IRI-Parameters.callContentLinkInformation
```

Conceptually, the schema now looks like the following:
```
IRI-Parameters ::= SEQUENCE
{
  -- component types
  -- ...

  callContentLinkInformation [10] generated.IRI-Parameters.CallContentLinkInformation OPTIONAL,

  -- component types
  -- ...
}

generated.IRI-Parameters.CallContentLinkInformation ::= SEQUENCE
{
  cCLink1Characteristics    [1] CallContentLinkCharacteristics OPTIONAL,
  cCLink2Characteristics    [2] CallContentLinkCharacteristics OPTIONAL,
  ...
}
```

For the case of a SEQUENCE/SET OF SEQUENCE/SET pseudo type definition, only the inner SEQUENCE or
SET is extracted as a type definition. For example:

```
ConferencePartyChange ::= SEQUENCE
{
    ...
    communicating [4] SEQUENCE OF SEQUENCE {
            partyId [0] SEQUENCE OF PartyId OPTIONAL,
            cCCId   [1] EXPLICIT CCCId OPTIONAL
        } OPTIONAL,
    ....
}
```

The inner SEQUENCE pseudo type is extracted out as a type definition with the name
`generated.ConferencePartyChange.communicating`. The schema would conceptually look like this:

```
ConferencePartyChange ::= SEQUENCE
{
    ...
    communicating [4] SEQUENCE OF generated.ConferencePartyChange.communicating OPTIONAL,
    ....
}

generated.ConferencePartyChange.communicating ::= SEQUENCE
{
    partyId [0] SEQUENCE OF PartyId OPTIONAL,
    cCCId   [1] EXPLICIT CCCId OPTIONAL
}
```

For explicit type definitions featuring a SEQUENCE/SET OF SEQUENCE/SET, there is no component type
name therefore the generated type name would be in the following format:

```
generated.<containingTypeName>
```

For example, consider the following explicit type definition for a SEQUENCE OF SEQUENCE:

```
 GA-Polygon ::= SEQUENCE (SIZE (1..maxNrOfPoints)) OF
   SEQUENCE
   {
     geographicalCoordinates   GeographicalCoordinates,
     ...
   }
```

This type definition would have the inner SEQUENCE pseudo type extracted with a type name of:

```
generated.GA-Polygon
```

### Store Pseudo Type Definition

Psuedo code illustration:

```
class AsnSchemaComponentTypeGenerated extends AsnSchemaComponentType
{
    String typeDefinitionText =
        "cCLink1Characteristics    [1] CallContentLinkCharacteristics OPTIONAL,"
        + "cCLink2Characteristics    [2] CallContentLinkCharacteristics OPTIONAL,"
        + "...";
}
```

The text of the `Pseudo Type Definition` is formatted to a single line of text.
 An `AsnSchemaComponentTypeGenerated` object is used to store the `Pseudo Type
 Definition`. The `AsnSchemaComponentTypeGenerated` type extends the
 `AsnSchemaComponentType` to add an extra instance variable named
 `typeDefinitionText` which stores the text of the `Pseudo Type Definition`.

### Parsing Pseudo Type Definitions

Psuedo code illustration:

```
class AsnSchemaTypeDefinitionParser
{
    ImmutableList<AsnSchemaTypeDefinition> parseSequence()
    {
        final List<AsnSchemaTypeDefinition> typeDefinitions = Lists.newArrayList();

        // parse raw text
        final ImmutableList<AsnSchemaComponentType> componentTypes =
                AsnSchemaComponentTypeParser.parse(name, componentTypesText);

        // created all generated type definitions
        for (final AsnSchemaComponentType component : componentTypes)
        {
            if (component instanceof AsnSchemaComponentTypeGenerated)
            {
                ImmutableList<AsnSchemaTypeDefinition> generated =
                    parse(component.getTypeName(), component.pseudoTypeDefinitionText);
                typeDefinitions.addAll(generated);
            }
        }

        // create the sequence type definition
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        final AsnSchemaTypeDefinitionChoice typeDefinition =
                new AsnSchemaTypeDefinitionChoice(name, componentTypes, constraint);

        // return all created type definitions
        typeDefinitions.add(typeDefinition);
        return ImmutableList.copyOf(typeDefinitions);
    }
}
```

When parsing constructed types (SET, SEQUENCE, ENUMERATED or CHOICE) in the
`AsnSchemaTypeDefinitionParser` class, the `Pseudo Type Definitions` are
extracted from the `ImmutableList<AsnSchemaComponentType>` returned from the
`AsnSchemaComponentTypeParser.parse` method by checking whether the instance of
`AsnSchemaComponentType` is a `AsnSchemaComponentTypeGenerated`.

Each instance of `AsnSchemaComponentTypeGenerated` is parsed into an
`AsnSchemaTypeDefinition` using the `typeDefinitionText` instance variable
and existing parse methods in the `AsnSchemaTypeDefinitionParser` class. An
list of `AsnSchemaTypeDefinition` objects is generated from all parsed
`Pseudo Type Definitions`.

The constructed Type Definition is then created. All created type definitions
are then returned as a `ImmutableList<AbstractAsnSchemaTypeDefinition>`
by the parse method of the constructed type.

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
