# Design Decisions and Concepts - Schema Parsing

## Architecture

The components which handle parsing an ASN.1 Schema file have been broken
out into a series of nested classes. Each class handles a specific area of
the schema and delegates downwards to lower level parsers.

The hierarchy is as follows:

- `AsnSchemaParser`
    - `AsnSchemaModuleParser`
        - `AsnSchemaImportsParser`
        - `AsnSchemaTypeDefinitionParser`
            - `AsnSchemaTypeParser`
                - `AsnSchemaConstraintParser`
                - `AsnSchemaComponentTypeParser`
                    - `AsnSchemaTypeParser`
                        - ... (ie recursive)
                - `AsnSchemaNamedTagParser`

The general entry point to the parser framework is `AsnSchemaParser`. It
delegates downwards to each parser as required. Each parser is responsible
for returning objects as indicated by its name. E.g.

- `AsnSchemaModuleParser` returns `AsnSchemaModule` objects
- `AsnSchemaTypeDefinitionParser` returns `AsnSchemaTypeDefinition` objects

## AsnSchemaTypeDefinition, AsnSchemaComponentType and AsnSchemaType

From the hierarchy above it is obvious that there can be a recursive
arrangement when parsing.

### AsnSchemaTypeDefinition

An AsnSchemaTypeDefinition is something that is defined at the root level
of the ASN.1 schema, and is defined using the "::=" syntax.  For example:

```
IRI-Parameters ::= SEQUENCE
{
  conversationDuration        [7] OCTET STRING (SIZE (3)) OPTIONAL,
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

defines a type named `IRI-Parameters`.  At its core the
`AsnSchemaTypeDefinition` is an `AsnSchemaType` with a name.  It can
be re-used throughout the schema module.  In the above example
`callContentLinkInformation` is *not* a type definition, so not an
`AsnSchemaTypeDefinition`, it is a component (`AsnSchemaComponentType`)
of the `AsnSchemaTypeDefinition` and also has a type (`AsnSchemaType`),
that happens to also be a `SEQUENCE`.

### AsnSchemaComponentType

In the above example the type defined as `IRI-Parameters` is known
as a constructed type, and has a number of 'components', which map to
`AsnSchemaComponentType`.  An `AsnSchemaComponentType` is a container
that stores information about that component (name, tag, whether it
is optional, etc) as well as its core type, which is stored inside the
`AsnSchemaComponentType` as an `AsnSchemaType`.  In the example above
`conversationDuration` would be a `AsnSchemaComponentType` and it
would be stored with information defining its tag as `7`, its name as
`conversationDuration`, it *is* optional, and with a `AsnSchemaType` equating
it to the ASN.1 type of `OCTET STRING`.

### AsnSchemaType

Everything must have a type of some sort.  In the example above we can see
that the `AsnSchemaTypeDefinition` would have a type equating it to an ASN.1
`SEQUENCE`.  There are also a number of components (`AsnSchemaComponentType`),
each with a type; for example the component for `conversationDuration` would
have a type equating it to an ASN.1 `OCTET STRING`.  The recursion can occur
because components within a constructed type (e.g. `SEQUENCE`) can also be
constructed types defined inline rather than as a separate type definition,
which is exactly what `callContentLinkInformation` is in the above example.

### Bringing it all together

At the end of the parsing of all the schema modules there is a step to join
together all the types.  In the example above `CallContentLinkCharacteristics`
is a type definition elsewhere in the schema that may, or may not have been
defined at the time `IRI-Parameters` is being parsed. When the parser processes
the component `cCLink1Characteristics` it creates a placeholder for the type
`CallContentLinkCharacteristics`.  The placeholder will will be resolved
to the appropriate `AsnSchemaType` stored in the `AsnSchemaTypeDefinition`
during the final 'sweep' of all the types across all modules.

