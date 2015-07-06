package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaNamedType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaNamedTypeImpl;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * Created by Michael on 6/07/2015.
 */
public class ChoiceTaggingStrategy implements TaggingStrategy
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(SequenceTaggingStrategy.class);

    @Override
    public AsnSchemaNamedType getMatchingComponent(String tag, ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes)
    {
        Matcher matcher = PATTERN_TAG.matcher(tag);
        if (!matcher.matches())
        {
            logger.warn("unexpected tag format {}", tag);
            return null; // TODO MJF
        }

        String indexPart = matcher.group(1);
        int index = Integer.parseInt(indexPart);
        String rest = matcher.group(3);

        AsnSchemaComponentType result = tagsToComponentTypes.get(rest);

        if (result != null) return result;

        if (rest.startsWith("u"))
        {
            // TODO MJF
            // What makes us come in here?
            AsnBuiltinType typeToMatch = AsnBuiltinType.valueOf(matcher.group(6));
            for (Map.Entry<String, AsnSchemaComponentType> entry : tagsToComponentTypes.entrySet())
            {
                AsnSchemaComponentType component = entry.getValue();
                AsnBuiltinType cT = component.getType().getBuiltinTypeAA();
                if (match(typeToMatch, cT))
                {
                    logger.debug("component {} matches type {}", component.getName(), cT);
                    //return component.getType().getChildName(tag);

                    AsnSchemaNamedType namedType = component.getType().getMatchingChild(tag);

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
                AsnSchemaNamedType namedType = component.getType().getMatchingChild(tag);
                if (AsnSchemaNamedType.NULL != namedType)
                {
                    logger.debug("component {} matches tag {}", component.getTagName(), tag);
                    String newTag = component.getName() + "/" + namedType.getName();
                    return new AsnSchemaNamedTypeImpl(newTag, namedType.getType());
                }
                logger.debug("tagless Choice, NO MATCH found");
            }
        }



        return (result == null) ?
                AsnSchemaNamedType.NULL :
                result;

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
                logger.warn("Duplicate Tag {} for {}, already have {}", decoratedTag, componentType.getTagName(), usedTags.get(decoratedTag));
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