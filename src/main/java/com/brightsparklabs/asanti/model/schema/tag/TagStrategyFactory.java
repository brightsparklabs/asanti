package com.brightsparklabs.asanti.model.schema.tag;

import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;

/**
 * Created by Michael on 7/07/2015.
 */
public class TagStrategyFactory
{
    public static TagMatchingStrategy getTagMatchingStrategy(AsnPrimitiveType type)
    {
        if (type == AsnPrimitiveType.SEQUENCE)
        {
            return SequenceTagMatchingStrategy.SEQUENCE_TAG_MATCHING_STRATEGY;
        }

        return UnorderedTagMatchingStrategy.UNORDERED_TAG_MATCHING_STRATEGY;
    }

    public static TagCreationStrategy getTagCreationStrategy(AsnPrimitiveType type, AsnModuleTaggingMode tagMode)
    {
        return TagCreationStrategyImpl.create(type, tagMode);
    }
}
