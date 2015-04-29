# Design Decisions and Concepts

## Purpose

The purpose of the document in this folder are to outline the high level design
decisions and concepts for the library which are not immediately obvious from
looking at the code. The focus is to articulate the main modules which comprise
the system and how they fit together. Detailed design should not be added to
these documents. Only information which helps orientate developers as to how
things fit together.

## Roadmap

The first step is to read about [Models][]. This will give you an understanding
of what the primary models in the library are and what role they play.

Next, read about how [Validators][] are used to verify that data is correctly
formed before we use [Decoders][] to extract content.

Finally, head across to [Schema Parsing][schema_parsing] to get an idea of how
the schema models are created.


[decoders]:       decoders.md
[models]:         models.md
[schema_parsing]: schema_parsing.md
[validators]:     validators.md
