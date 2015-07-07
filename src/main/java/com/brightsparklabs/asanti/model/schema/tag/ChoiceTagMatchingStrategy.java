package com.brightsparklabs.asanti.model.schema.tag;

import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaNamedType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaNamedTypeImpl;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.collect.ImmutableMap;

/**
 * Created by Michael on 7/07/2015.
 */
public class ChoiceTagMatchingStrategy implements TagMatchingStrategy
{
    public static final ChoiceTagMatchingStrategy CHOICE_TAG_MATCHING_STRATEGY
            = new ChoiceTagMatchingStrategy();

    private ChoiceTagMatchingStrategy()
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
        String choiceTag = "u.Choice";
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
