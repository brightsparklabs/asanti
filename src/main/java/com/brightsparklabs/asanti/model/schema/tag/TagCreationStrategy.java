package com.brightsparklabs.asanti.model.schema.tag;

import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.collect.ImmutableMap;

import java.text.ParseException;

/**
 * A base type used for providing different tag creation capabilities.
 *
 * @author brightSPARK Labs
 */
public interface TagCreationStrategy
{
    /** TODO MJF */
    ImmutableMap<String, AsnSchemaComponentType> getTagsForComponents(
            Iterable<AsnSchemaComponentType> componentTypes) throws ParseException;

}
