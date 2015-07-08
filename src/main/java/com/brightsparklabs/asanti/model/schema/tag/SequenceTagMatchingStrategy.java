package com.brightsparklabs.asanti.model.schema.tag;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaNamedType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaNamedTypeImpl;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.collect.ImmutableMap;

/**
 * This provides a mechanism to match raw tags from the AsnData with the way tags are stored in a
 * Constructed type for Sequene types - which rely on the order of the components.  It assumes that
 * the tags were created with the matching creation strategy.
 */
public class SequenceTagMatchingStrategy implements TagMatchingStrategy
{
    public static final SequenceTagMatchingStrategy SEQUENCE_TAG_MATCHING_STRATEGY
            = new SequenceTagMatchingStrategy();

    private SequenceTagMatchingStrategy()
    {
    }

    @Override
    public AsnSchemaNamedType getMatchingComponent(String rawTag,
            ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes, AsnSchemaType type,
            DecodingSession session)
    {
        AsnSchemaTag tag = AsnSchemaTag.create(rawTag);

        tag = session.getTag(type, tag);

        AsnSchemaComponentType result = tagsToComponentTypes.get(tag.getRawTag());

        if (result != null)
        {
            session.setTag(type, tag, result.isOptional());
            return result;
        }

        // Was one of the components a choice (with no tag) that was transparently replaced by the option?
        String choiceTag = AsnSchemaTag.create(tag.getTagIndex(), AsnBuiltinType.Choice).getRawTag();
        result = tagsToComponentTypes.get(choiceTag);
        if (result != null)
        {
            AsnSchemaNamedType namedType = result.getType().getMatchingChild(rawTag, session);

            String newTag = result.getName() + "/" + namedType.getName();

            return new AsnSchemaNamedTypeImpl(newTag, namedType.getType());
        }

        return AsnSchemaNamedType.NULL;
    }

}
