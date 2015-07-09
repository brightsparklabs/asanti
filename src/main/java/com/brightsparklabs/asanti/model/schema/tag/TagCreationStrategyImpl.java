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
 * TODO MJF
 */
public class TagCreationStrategyImpl implements TagCreationStrategy
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(TagCreationStrategyImpl.class);

    /** TODO MJF */
    private static final TagDecorator TAG_DECORATOR_SEQUENCE = new SequenceTagDecorator();

    /** TODO MJF */
    private static final TagDecorator TAG_DECORATOR_UNORDERED = new UnorderedTagDecorator();

    /** TODO MJF */
    private static final TagAutomator TAG_AUTOMATOR_CHECK = new TagAutomatorCheck();

    /** TODO MJF */
    private static final TagAutomator TAG_AUTOMATOR_FALSE = new TagAutomatorFalse();

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** TODO MJF */
    TagDecorator tagDecorator;

    /** TODO MJF */
    TagAutomator tagAutomator;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constuctor.  Use {@code create}
     *
     * @param tagDecorator
     *         the TagDecorator to be used to create the tags
     * @param tagAutomator
     *         the TagAutomator to determine whether to automatically create tags
     */
    private TagCreationStrategyImpl(TagDecorator tagDecorator, TagAutomator tagAutomator)
    {
        this.tagDecorator = tagDecorator;
        this.tagAutomator = tagAutomator;
    }

    /**
     * creates a TagCreationStrategy configured for the permutations of type and tagging mode
     *
     * @param type
     *         determines how the Constructed type determines duplicates
     * @param taggingMode
     *         determines whether the Constructed type will automatically generate context-specific
     *         tags
     *
     * @return
     */
    public static TagCreationStrategy create(AsnPrimitiveType type,
            AsnModuleTaggingMode taggingMode)
    {
        TagAutomator tagAutomator = taggingMode == AsnModuleTaggingMode.AUTOMATIC ?
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

        // Key: decorated tag, Value: component name.  We only really need a List, but by
        // tracking the name of the component we can generate better error messages
        Map<String, String> usedTags
                = Maps.newLinkedHashMap(); // use this so that we have known iteration order for later...

        int index = 0;
        int autoTagNumber = 0;
        for (final AsnSchemaComponentType componentType : componentTypes)
        {
            // auto tag if appropriate, otherwise use the components' tag (which will be a context-specific),
            // otherwise use the Universal tag
            final String tag = (autoTag) ?
                    String.valueOf(autoTagNumber) :
                    Strings.isNullOrEmpty(componentType.getTag()) ?
                            AsnSchemaTag.createUniversalPortion(componentType.getType()
                                    .getBuiltinTypeAA()) :
                            componentType.getTag();

            final String decoratedTag = tagDecorator.getDecoratedTag(index, tag);

            // TODO MJF - how to explain this?  Can I put in a link to a design doc?  It is too complex for a comment here.
            if (!componentType.isOptional())
            {
                index++;
            }
            autoTagNumber++;

            if (usedTags.containsKey(decoratedTag))
            {
                logger.warn("Duplicate Tag {} for {}, already have {}",
                        decoratedTag,
                        componentType.getName(),
                        usedTags.get(decoratedTag));
                throw new ParseException("Duplicate Tag", -1);
            }

            usedTags.put(decoratedTag, componentType.getName());

            tagsToComponentTypesBuilder.put(decoratedTag, componentType);
        }

        return tagsToComponentTypesBuilder.build();
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASSES
    // -------------------------------------------------------------------------

    /**
     * Interface for creating the tag that we will store for later matching
     */
    private interface TagDecorator
    {
        /**
         * From the supplied index and Tag create a new string Tag in the expected format
         *
         * @param index
         *         the index portion of the tag to be created
         * @param tag
         *         the Tag portion of the tag to be created
         *
         * @return A string that represents the tag that we will store
         */
        String getDecoratedTag(int index, String tag);
    }

    /**
     * TagDecorator for Sequence types, where the index is required for determination of duplication
     * and for later tag matching
     */
    private static class SequenceTagDecorator implements TagDecorator
    {
        @Override
        public String getDecoratedTag(int index, String tag)
        {
            return AsnSchemaTag.createRawTag(index, tag);
        }
    }

    /**
     * TagDecorator for unordered types (ie Set and Choice) where the index is not required for
     * either determination of duplicates or for later tag matching
     */
    private static class UnorderedTagDecorator implements TagDecorator
    {
        @Override
        public String getDecoratedTag(int index, String tag)
        {
            return String.format("%s", tag);
        }
    }

    /**
     * Interface used to determine whether or not Automatic tags should be generated
     */
    private interface TagAutomator
    {
        /**
         * Given the input components, determine whether it is appropriate to create Automatic tags
         *
         * @param componentTypes
         *         input components to check
         *
         * @return true if the Constructed type should create Automatic tags.
         */
        boolean canAutomate(Iterable<AsnSchemaComponentType> componentTypes);
    }

    /**
     * TagAutomator to use when tagging mode is {@link AsnModuleTaggingMode#AUTOMATIC} It will
     * perform the appropriate calculations
     */
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

    /**
     * TagAutomator to use when the tagging mode is not {@link AsnModuleTaggingMode#AUTOMATIC},
     * simply returns false - ie don't create an automatic tag
     */
    private static class TagAutomatorFalse implements TagAutomator
    {
        @Override
        public boolean canAutomate(Iterable<AsnSchemaComponentType> componentTypes)
        {
            return false;
        }
    }
}
