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
 * Constructed type for Sequence types - which rely on the order of the components.  It assumes that
 * the tags were created with the matching creation strategy.
 */
public class SequenceTagMatchingStrategy implements TagMatchingStrategy
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    private static final SequenceTagMatchingStrategy instance = new SequenceTagMatchingStrategy();

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor. This is a singleton, use the getInstance.
     */
    private SequenceTagMatchingStrategy()
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
    public static SequenceTagMatchingStrategy getInstance()
    {
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: TagMatchingStrategy
    // -------------------------------------------------------------------------

    @Override
    public AsnSchemaNamedType getMatchingComponent(String rawTag,
            ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes,
            DecodingSession session)
    {
        // Use the session to translate this tag to have an index appropriate for the session (ie how
        // many OPTIONAL items have been received)
        AsnSchemaTag tag = AsnSchemaTag.create(rawTag);
        tag = session.getTag(tag);
        final AsnSchemaComponentType result = tagsToComponentTypes.get(tag.getRawTag());

        if (result != null)
        {
            session.setTag(tag, result.isOptional());
            return result;
        }

        return AsnSchemaNamedType.NULL;
    }

}
