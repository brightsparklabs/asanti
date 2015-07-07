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
import java.util.regex.Matcher;

/**
 * Created by Michael on 6/07/2015.
 */
public class SequenceTaggingStrategy implements TaggingStrategy
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(SequenceTaggingStrategy.class);

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** TODO MJF */
    int lastIndex = 0;
    boolean lastOptional = false;
    //int offset = 0;


    @Override
    public AsnSchemaNamedType getMatchingComponent(String rawTag, ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes, DecodingSession session)
    {
        AsnSchemaTag tag = AsnSchemaTag.create(rawTag);
        int index = Integer.parseInt(tag.getTagIndex());
        // Adjust for offsets to indexes caused by optional components.
//        int offset = session.getOffset(this);
//        if (offset != 0)
//        {
//            tag = AsnSchemaTag.create((index - offset) + "." + tag.getTagPortion());
//        }

        tag = session.getTag(this, tag);

        AsnSchemaComponentType result = tagsToComponentTypes.get(tag.getRawTag());

        if (result != null)
        {
            session.setTag(this, tag, result.isOptional());
//            if (result.isOptional())
//            {
//                session.setOffset(this, offset+1);
//            }

            return result;
        }

        // Was one of the components a choice (with no tag) that was transparently replaced by the option?
        String choiceTag = tag.getTagIndex() + ".u.Choice";
        result = tagsToComponentTypes.get(choiceTag);
        if (result != null)
        {
            AsnSchemaNamedType namedType = result.getType().getMatchingChild(rawTag, session);

            String newTag = result.getName() + "/" + namedType.getName();

            logger.debug("Sequence passing through choice {}", newTag);
            return new AsnSchemaNamedTypeImpl(newTag, namedType.getType());
        }

        return AsnSchemaNamedType.NULL;
    }

    @Override
    public ImmutableMap<String, AsnSchemaComponentType> getTagsForComponents(Iterable<AsnSchemaComponentType> componentTypes, AsnModuleTaggingMode tagMode) throws ParseException
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
        int autoTagNumber = 0;
        for (final AsnSchemaComponentType componentType : componentTypes)
        {
            // auto tag if appropriate
            final String contextSpecificTag = (autoTag) ?
                    String.valueOf(autoTagNumber) :
                    componentType.getTag();

            final String tag = (Strings.isNullOrEmpty(contextSpecificTag)) ?
                    String.format("u.%s", componentType.getType().getBuiltinTypeAA()) :
                    contextSpecificTag;

            final String decoratedTag = String.format("%d.%s", index, tag);

            if (!componentType.isOptional())
            {
                index++;
            }
            autoTagNumber++;

            if (usedTags.containsKey(decoratedTag))
            {
                logger.warn("Duplicate Tag {} for {}, already have {}", decoratedTag, componentType.getTagName(), usedTags.get(
                        decoratedTag));
                throw new ParseException("Duplicate Tag", -1);
            }

            //logger.debug("{} Decorated tag {}", componentType.getTagName(), decoratedTag);

            usedTags.put(decoratedTag, componentType.getTagName());

            tagsToComponentTypesBuilder.put(decoratedTag, componentType);
        }

        return tagsToComponentTypesBuilder.build();
    }

}
