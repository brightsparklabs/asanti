package com.brightsparklabs.asanti.model.schema.tag;

import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;

/**
 * A Factory to provide the appropriate Tag creation and matching classes for the giving input
 * parameter permutations.
 *
 * @author brightSPARK Labs
 */
public class TagStrategyFactory
{
    /** TODO MJF */
    public static TagMatchingStrategy getTagMatchingStrategy(AsnPrimitiveType type)
    {
        if (type == AsnPrimitiveType.SEQUENCE)
        {
            return SequenceTagMatchingStrategy.SEQUENCE_TAG_MATCHING_STRATEGY;
        }

        return UnorderedTagMatchingStrategy.UNORDERED_TAG_MATCHING_STRATEGY;
    }

    /** TODO MJF */
    public static TagCreationStrategy getTagCreationStrategy(AsnPrimitiveType type,
            AsnModuleTaggingMode tagMode)
    {
        return TagCreationStrategyImpl.create(type, tagMode);
    }
}
