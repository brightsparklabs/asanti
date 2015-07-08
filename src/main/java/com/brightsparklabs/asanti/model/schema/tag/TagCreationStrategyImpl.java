package com.brightsparklabs.asanti.model.schema.tag;

import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Map;

/**
 * Created by Michael on 6/07/2015.
 */
public class TagCreationStrategyImpl implements TagCreationStrategy
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(TagCreationStrategyImpl.class);

    // TODO MJF javadoc
    private static final TagDecorator TAG_DECORATOR_SEQUENCE = new SequenceTagDecorator();

    private static final TagDecorator TAG_DECORATOR_UNORDERED = new UnorderedTagDecorator();

    private static final TagAutomator TAG_AUTOMATOR_CHECK = new TagAutomatorCheck();

    private static final TagAutomator TAG_AUTOMATOR_FALSE = new TagAutomatorFalse();

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    TagDecorator tagDecorator;

    TagAutomator tagAutomator;

    private TagCreationStrategyImpl(TagDecorator tagDecorator, TagAutomator tagAutomator)
    {
        this.tagDecorator = tagDecorator;
        this.tagAutomator = tagAutomator;
    }

    public static TagCreationStrategy create(AsnPrimitiveType type, AsnModuleTaggingMode tagMode)
    {
        TagAutomator tagAutomator = tagMode == AsnModuleTaggingMode.AUTOMATIC ?
                TAG_AUTOMATOR_CHECK :
                TAG_AUTOMATOR_FALSE;

        if (type == AsnPrimitiveType.SEQUENCE)
        {
            return new TagCreationStrategyImpl(TAG_DECORATOR_SEQUENCE, tagAutomator);
        }

        return new TagCreationStrategyImpl(TAG_DECORATOR_UNORDERED, tagAutomator);
    }

    @Override
    public ImmutableMap<String, AsnSchemaComponentType> getTagsForComponents(
            Iterable<AsnSchemaComponentType> componentTypes) throws ParseException
    {
        final ImmutableMap.Builder<String, AsnSchemaComponentType> tagsToComponentTypesBuilder
                = ImmutableMap.builder();

        // Check to see if we need to apply automatic tags
        boolean autoTag = tagAutomator.canAutomate(componentTypes);

        // Key: decorated tag, Value: the component name (only for useful error messages)
        //Map<String, String> usedTags = Maps.newHashMap();
        Map<String, String> usedTags
                = Maps.newLinkedHashMap(); // use this so that we have known iteration order for later...

        int index = 0;
        int autoTagNumber = 0;
        for (final AsnSchemaComponentType componentType : componentTypes)
        {
            // auto tag if appropriate, otherwise use the components tag (which will be a context-specific),
            // otherwise use the Universal tag
            final String tag = (autoTag) ?
                    String.valueOf(autoTagNumber) :
                    Strings.isNullOrEmpty(componentType.getTag()) ?
                            AsnSchemaTag.createUniversalPortion(componentType.getType().getBuiltinTypeAA()) :
                            componentType.getTag();

            final String decoratedTag = tagDecorator.getDecoratedTag(index, tag);

            if (!componentType.isOptional())
            {
                index++;
            }
            autoTagNumber++;

            if (usedTags.containsKey(decoratedTag))
            {
                logger.warn("Duplicate Tag {} for {}, already have {}",
                        decoratedTag,
                        componentType.getTagName(),
                        usedTags.get(decoratedTag));
                throw new ParseException("Duplicate Tag", -1);
            }

            usedTags.put(decoratedTag, componentType.getTagName());

            tagsToComponentTypesBuilder.put(decoratedTag, componentType);
        }

        return tagsToComponentTypesBuilder.build();
    }

    private interface TagDecorator
    {
        String getDecoratedTag(int index, String tag);
    }

    private static class SequenceTagDecorator implements TagDecorator
    {
        @Override
        public String getDecoratedTag(int index, String tag)
        {
            return AsnSchemaTag.createRawTag(index, tag);
        }
    }

    private static class UnorderedTagDecorator implements TagDecorator
    {
        @Override
        public String getDecoratedTag(int index, String tag)
        {
            return String.format("%s", tag);
        }
    }

    private interface TagAutomator
    {
        boolean canAutomate(Iterable<AsnSchemaComponentType> componentTypes);
    }

    private static class TagAutomatorCheck implements TagAutomator
    {
        @Override
        public boolean canAutomate(Iterable<AsnSchemaComponentType> componentTypes)
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
            return !anyTagSet;
        }
    }

    private static class TagAutomatorFalse implements TagAutomator
    {
        @Override
        public boolean canAutomate(Iterable<AsnSchemaComponentType> componentTypes)
        {
            return false;
        }
    }

}
