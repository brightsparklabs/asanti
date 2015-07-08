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
 * Constructed type for Set and Choice types - which do not rely on the order of the components.  It
 * assumes that the tags were created with the matching creation strategy.
 */
public class UnorderedTagMatchingStrategy implements TagMatchingStrategy
{
    public static final UnorderedTagMatchingStrategy UNORDERED_TAG_MATCHING_STRATEGY
            = new UnorderedTagMatchingStrategy();

    private UnorderedTagMatchingStrategy()
    {
    }

    @Override
    public AsnSchemaNamedType getMatchingComponent(String rawTag,
            ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes, AsnSchemaType type,
            DecodingSession session)
    {
        AsnSchemaTag tag = AsnSchemaTag.create(rawTag);

        AsnSchemaNamedType result = tagsToComponentTypes.get(tag.getTagPortion());

        if (result != null)
        {
            return result;
        }

        // Was one of the components a choice (with no tag) that was transparently replaced by the option?
        String choiceTag = AsnSchemaTag.createUniversalPortion(AsnBuiltinType.Choice);
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
