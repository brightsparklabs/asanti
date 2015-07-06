package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaNamedType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.collect.ImmutableMap;

import java.text.ParseException;
import java.util.regex.Pattern;

/**
 * TOTO MJF
 */
public interface TaggingStrategy
{
    ImmutableMap<String, AsnSchemaComponentType> getTagsForComponents(Iterable<AsnSchemaComponentType> componentTypes, AsnModuleTaggingMode tagMode) throws
            ParseException;

    AsnSchemaNamedType getMatchingComponent(String tag, ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes);


    /** pattern to match a tag coming out of the BER decoder */
    Pattern PATTERN_TAG = Pattern.compile(
            "^([0-9]+)(\\.(([0-9]+)|(u\\.([a-zA-Z0-9]+))))$");

}
