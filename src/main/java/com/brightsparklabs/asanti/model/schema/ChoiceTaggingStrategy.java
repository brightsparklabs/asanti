package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaNamedType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaNamedTypeImpl;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Map;

/**
 * TODO MJF
 */
public class ChoiceTaggingStrategy implements TaggingStrategy
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(SequenceTaggingStrategy.class);


    @Override
    public AsnSchemaNamedType getMatchingComponent(String rawTag, ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes, DecodingSession session)
    {
        AsnSchemaTag tag = AsnSchemaTag.create(rawTag);

        AsnSchemaComponentType result = tagsToComponentTypes.get(tag.getTagPortion());

        if (result != null)
        {
            return result;
        }


        if (!tag.getTagUniversal().isEmpty())
        {
            // TODO MJF
            // What makes us come in here?
            String universal = tag.getTagUniversal().substring(2);
            AsnBuiltinType typeToMatch = AsnBuiltinType.valueOf(universal);
            for (Map.Entry<String, AsnSchemaComponentType> entry : tagsToComponentTypes.entrySet())
            {
                AsnSchemaComponentType component = entry.getValue();
                AsnBuiltinType cT = component.getType().getBuiltinTypeAA();
                if (match(typeToMatch, cT))
                {
                    logger.debug("component {} matches type {}", component.getName(), cT);

                    AsnSchemaNamedType namedType = component.getType().getMatchingChild(rawTag, session);

                    String newTag = component.getName() + "/" + namedType.getName();
                    return new AsnSchemaNamedTypeImpl(newTag, namedType.getType());
                }
            }
            logger.debug("did NOT find a match for {}", typeToMatch);

        }
        else
        {
            // TODO MJF
            // What makes us come in here?

            // not going to a "universal" type, so return the first tag match from our children
            for (Map.Entry<String, AsnSchemaComponentType> entry : tagsToComponentTypes.entrySet())
            {
                AsnSchemaComponentType component = entry.getValue();
                //if (AsnSchemaType.NULL != component.getType().getChildType(tag))
                AsnSchemaNamedType namedType = component.getType().getMatchingChild(rawTag, session);
                if (AsnSchemaNamedType.NULL != namedType)
                {
                    logger.debug("component {} matches tag {}", component.getTagName(), tag);
                    String newTag = component.getName() + "/" + namedType.getName();
                    return new AsnSchemaNamedTypeImpl(newTag, namedType.getType());
                }
                logger.debug("tagless Choice, NO MATCH found");
            }
        }

        return AsnSchemaNamedType.NULL;
    }

    @Override
    public ImmutableMap<String, AsnSchemaComponentType> getTagsForComponents(Iterable<AsnSchemaComponentType> componentTypes, AsnModuleTaggingMode tagMode) throws
            ParseException
    {
        final ImmutableMap.Builder<String, AsnSchemaComponentType> tagsToComponentTypesBuilder
                = ImmutableMap.builder();

        // Check to see if we need to apply automatic tags
        boolean autoTag = false;
        if (tagMode == AsnModuleTaggingMode.AUTOMATIC)
        {
            // only need to automatically tag, if the global mode is automatic AND
            // none of the components have context-specific (TODO MJF or application) tags
            boolean anyTagSet = false;
            for (final AsnSchemaComponentType componentType : componentTypes)
            {
                String tag = componentType.getTag();
                if (!Strings.isNullOrEmpty(tag))
                {
                    anyTagSet = true;
                    break;
                }
            }
            autoTag = !anyTagSet;
        }

        // Key: decorated tag, Value: the component name (only for useful error messages)
        //Map<String, String> usedTags = Maps.newHashMap();
        Map<String, String> usedTags = Maps.newLinkedHashMap(); // use this so that we have known iteration order for later...

        int index = 0;
        for (final AsnSchemaComponentType componentType : componentTypes)
        {
            // auto tag if appropriate
            final String contextSpecificTag = (autoTag) ?
                    String.valueOf(index) :
                    componentType.getTag();

            final String tag = (Strings.isNullOrEmpty(contextSpecificTag)) ?
                    String.format("u.%s", componentType.getType().getBuiltinTypeAA()) :
                    contextSpecificTag;

            //final String decoratedTag = String.format("%d.%s", index, tag);
            final String decoratedTag = tag;

            if (!componentType.isOptional())
            {
                index++;
            }
            else
            {
                int breakpoint = 0;
            }

            if (usedTags.containsKey(decoratedTag))
            {
                logger.warn("Choice: Duplicate Tag {} for {}, already have {}", decoratedTag, componentType.getTagName(), usedTags.get(decoratedTag));
                throw new ParseException("Duplicate Tag", -1);
            }

            //logger.debug("{} Decorated tag {}", componentType.getTagName(), decoratedTag);

            usedTags.put(decoratedTag, componentType.getTagName());

            tagsToComponentTypesBuilder.put(decoratedTag, componentType);
        }

        return tagsToComponentTypesBuilder.build();

    }

    public static boolean match(AsnBuiltinType a, AsnBuiltinType b)
    {
        if (a == b)
        {
            return true;
        }

        if (a == AsnBuiltinType.Sequence)
        {
            return b == AsnBuiltinType.SequenceOf;
        }
        if (b == AsnBuiltinType.Sequence)
        {
            return a == AsnBuiltinType.SequenceOf;
        }

        if (a == AsnBuiltinType.Set)
        {
            return b == AsnBuiltinType.SetOf;
        }
        if (b == AsnBuiltinType.Set)
        {
            return a == AsnBuiltinType.SetOf;
        }

        return false;
    }

}