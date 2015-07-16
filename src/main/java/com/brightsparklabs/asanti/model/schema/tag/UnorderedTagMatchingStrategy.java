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
 * This provides a mechanism to match raw tags from the AsnData with the way tags are stored in a
 * Constructed type for Set and Choice types - which do not rely on the order of the components.  It
 * assumes that the tags were created with the matching creation strategy.
 */
public class UnorderedTagMatchingStrategy implements TagMatchingStrategy
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static final UnorderedTagMatchingStrategy instance = new UnorderedTagMatchingStrategy();

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor. This is a singleton, use the getInstance.
     */
    private UnorderedTagMatchingStrategy()
    {
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns an instance of this class
     *
     * @return an instance of this class
     */
    public static UnorderedTagMatchingStrategy getInstance()
    {
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: TagMatchingStrategy
    // -------------------------------------------------------------------------

    @Override
    public AsnSchemaNamedType getMatchingComponent(String rawTag,
            ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes,
            DecodingSession decodingSession)
    {
        // For an unordered type we don't need the decodingSession
        final AsnSchemaTag tag = AsnSchemaTag.create(rawTag);
        final AsnSchemaNamedType result = tagsToComponentTypes.get(tag.getTagPortion());

        return (result != null) ? result : AsnSchemaNamedType.NULL;
    }
}
