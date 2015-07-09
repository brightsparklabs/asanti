package com.brightsparklabs.asanti.model.schema.tag;

import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaNamedType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.collect.ImmutableMap;

/**
 * A base type used for providing different tag matching capabilities.
 *
 * @author brightSPARK Labs
 */
public interface TagMatchingStrategy
{
    /** TODO MJF */
    AsnSchemaNamedType getMatchingComponent(String tag, ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes, AsnSchemaType type, DecodingSession session);
}
