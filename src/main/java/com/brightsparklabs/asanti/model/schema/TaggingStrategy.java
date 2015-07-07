package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaNamedType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.collect.ImmutableMap;

import java.text.ParseException;

/**
 * TODO MJF
 */
public interface TaggingStrategy
{
    ImmutableMap<String, AsnSchemaComponentType> getTagsForComponents(Iterable<AsnSchemaComponentType> componentTypes, AsnModuleTaggingMode tagMode) throws
            ParseException;

    AsnSchemaNamedType getMatchingComponent(String tag, ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes, DecodingSession session);
}
