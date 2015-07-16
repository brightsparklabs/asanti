/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.tag;

import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaNamedType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.collect.ImmutableMap;

/**
 * A base type used for providing different tag matching capabilities.
 *
 * @author brightSPARK Labs
 */
public interface TagMatchingStrategy
{
    /**
     * @param tag
     *         raw tag to match
     * @param tagsToComponentTypes
     *         the map of tags to components
     * @param decodingSession
     *         holds the state that allows us to disambiguate Sequences with OPTIONAL components.
     *
     * @return the AsnSchemaNamedType for the component matching this tag, AsnSchemaNamedType#NULL
     * if no match
     */
    AsnSchemaNamedType getMatchingComponent(String tag,
            ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes,
            DecodingSession decodingSession);
}
