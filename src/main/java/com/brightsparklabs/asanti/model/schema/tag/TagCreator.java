/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.tag;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypeConstructed;
import com.brightsparklabs.asanti.model.schema.type.GetAsnSchemaTypeVisitor;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

/**
 * TODO MJF
 */
public class TagCreator
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(TagCreator.class);

    /** static instance of a decorator for Sequences */
    private static final TagDecorator TAG_DECORATOR_SEQUENCE = new SequenceTagDecorator();

    /** static instance of a decorator for Set and Choice */
    private static final TagDecorator TAG_DECORATOR_UNORDERED = new UnorderedTagDecorator();

    /**
     * static instance of tag automator that checks the components according to the rules and should
     * be used when module tagging mode is AUTOMATIC
     */
    private static final TagAutomator TAG_AUTOMATOR_CHECK = new TagAutomatorCheck();

    /**
     * static instance of a tag automator that returns false and should be used with the module
     * tagging mode is anything other than AUTOMATIC
     */
    private static final TagAutomator TAG_AUTOMATOR_FALSE = new TagAutomatorFalse();

    /** static instance configure for Sequence and Auto tag generation */
    private static final TagCreator TAG_CREATION_SEQUENCE_AUTOMATED = new TagCreator(
            TAG_DECORATOR_SEQUENCE,
            TAG_AUTOMATOR_CHECK);

    /** static instance configure for Sequence and No auto tag generation */
    private static final TagCreator TAG_CREATION_SEQUENCE_FALSE = new TagCreator(
            TAG_DECORATOR_SEQUENCE,
            TAG_AUTOMATOR_FALSE);

    /** static instance configure for Set/Choice and Auto tag generation */
    private static final TagCreator TAG_CREATION_UNORDERED_AUTOMATED = new TagCreator(
            TAG_DECORATOR_UNORDERED,
            TAG_AUTOMATOR_CHECK);

    /** static instance configure for Set/Choice and No auto tag generation */
    private static final TagCreator TAG_CREATION_UNORDERED_FALSE = new TagCreator(
            TAG_DECORATOR_UNORDERED,
            TAG_AUTOMATOR_FALSE);

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    // Seems a little bit heavy weight to use this sort of Strategy pattern, but
    // both of these are decision points that can be made at construction time
    // rather than at run time.

    /** the Strategy to use for "decorating" tags, ie do we store the index along with the tag */
    private final TagDecorator tagDecorator;

    /** the Strategy to use for determining whether to Automate tag creation */
    private final TagAutomator tagAutomator;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.  Use {@code create}
     *
     * @param tagDecorator
     *         the TagDecorator to be used to create the tags
     * @param tagAutomator
     *         the TagAutomator to determine whether to automatically create tags
     */
    private TagCreator(TagDecorator tagDecorator, TagAutomator tagAutomator)
    {
        this.tagDecorator = tagDecorator;
        this.tagAutomator = tagAutomator;
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * creates a TagCreator configured for the permutations of type and tagging mode
     *
     * @param type
     *         determines how the Constructed type determines duplicates
     * @param taggingMode
     *         determines whether the Constructed type will automatically generate context-specific
     *         tags
     *
     * @return a TagCreator object configured appropriately for the input parameter
     */
    public static TagCreator create(AsnPrimitiveType type, AsnModuleTaggingMode taggingMode)
    {
        return (type == AsnPrimitiveType.SEQUENCE) ?
                ((taggingMode == AsnModuleTaggingMode.AUTOMATIC) ?
                        TAG_CREATION_SEQUENCE_AUTOMATED :
                        TAG_CREATION_SEQUENCE_FALSE) :
                ((taggingMode == AsnModuleTaggingMode.AUTOMATIC) ?
                        TAG_CREATION_UNORDERED_AUTOMATED :
                        TAG_CREATION_UNORDERED_FALSE);
    }

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
            // TODO MJF - AA so we only go here if it is a Choice not a Collection OF CHOICE
            if ((componentType.getType().getBuiltinTypeAA() == AsnBuiltinType.Choice)
                    && componentType.getTag().isEmpty() && (!autoTag))
            {
                // If a component is a Choice, and does not have a tag then the Choice's components
                // can appear at this level
                GetAsnSchemaTypeVisitor visitor = GetAsnSchemaTypeVisitor.getInstance();
                AsnSchemaType type = componentType.getType();
                AsnSchemaTypeConstructed choiceType
                        = (AsnSchemaTypeConstructed) type.accept(visitor);

                final ImmutableMap<String, AsnSchemaComponentType> choiceTypes
                        = choiceType.getTagsToComponentTypes();

                // These are already tagged etc.  What we need to do is reset the rawTag to align
                // with the index we are currently at, and reset the decoded tag to be "fully qualified"
                // ie to contain this components name and the Choice components name.
                for (ImmutableMap.Entry<String, AsnSchemaComponentType> entry : choiceTypes.entrySet())
                {
                    String rawTag = entry.getKey();
                    AsnSchemaComponentType choiceComponent = entry.getValue();

                    final String decoratedTag = tagDecorator.getDecoratedTag(index, rawTag);

                    assertNotDuplicate(usedTags, decoratedTag, choiceComponent.getName());

                    final AsnSchemaComponentType component = buildFullyQualifiedComponentType(
                            choiceComponent,
                            componentType.getName());

                    tagsToComponentTypesBuilder.put(decoratedTag, component);
                }
            }
            else
            {
                // auto tag if appropriate, otherwise use the components' tag (which will be a context-specific),
                // otherwise use the Universal tag
                final String rawTag = (autoTag) ?
                        String.valueOf(autoTagNumber) :
                        Strings.isNullOrEmpty(componentType.getTag()) ?
                                AsnSchemaTag.createUniversalPortion(componentType.getType()
                                        .getBuiltinTypeAA()) :
                                componentType.getTag();

                final String decoratedTag = tagDecorator.getDecoratedTag(index, rawTag);

                assertNotDuplicate(usedTags, decoratedTag, componentType.getName());

                AsnSchemaComponentType component = buildComponentType(componentType, rawTag);
                tagsToComponentTypesBuilder.put(decoratedTag, component);
            }

            // TODO MJF - how to explain this?  Can I put in a link to a design doc?  It is too complex for a comment here.
            if (!componentType.isOptional())
            {
                index++;
            }
            autoTagNumber++;

        }

        return tagsToComponentTypesBuilder.build();
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * @param usedTags
     *         the Map of tags that have already been used
     * @param decoratedTag
     *         the tag to check for uniqueness
     * @param componentName
     *         the name of the component we are checking (only needed for better error logging)
     *
     * @throws ParseException
     *         if the decoratedTag passed in has already been used in this Constructed type.
     */
    private static void assertNotDuplicate(Map<String, String> usedTags, String decoratedTag,
            String componentName) throws ParseException
    {
        if (usedTags.containsKey(decoratedTag))
        {
            logger.warn("Duplicate Tag {} for {}, already have {}",
                    decoratedTag,
                    componentName,
                    usedTags.get(decoratedTag));
            throw new ParseException("Duplicate Tag", -1);
        }

        // We need to fully qualify the decodedTag to have the Choice name
        usedTags.put(decoratedTag, componentName);
    }

    /**
     * If needed builds a new AsnSchemaComponentType so that it has the new rawTag.  If the tags are
     * already correct then is returns the original
     *
     * @param component
     *         the component we are copying from
     * @param rawTag
     *         the new rawTag to check/insert
     *
     * @return an AsnSchemaComponentType with the desired rawTag
     */
    private static AsnSchemaComponentType buildComponentType(AsnSchemaComponentType component,
            String rawTag)
    {
        checkNotNull(rawTag);
        checkNotNull(component);

        if (rawTag.equals(component.getTag()))
        {
            return component;
        }

        return new AsnSchemaComponentType(component.getName(),
                rawTag,
                component.isOptional(),
                component.getType());
    }

    /**
     * builds a new AsnSchemaComponentType with a "fully qualified" tag name.
     *
     * @param component
     *         the component we are copying from
     * @param prefix
     *         the prefix to add to the component name to fully qualify it.
     *
     * @return new AsnSchemaComponentType with fully qualified name
     */
    private static AsnSchemaComponentType buildFullyQualifiedComponentType(
            AsnSchemaComponentType component, String prefix)
    {
        if (Strings.isNullOrEmpty(prefix))
        {
            return component;
        }

        final String newName = prefix + "/" + component.getName();

        return new AsnSchemaComponentType(newName,
                component.getTag(),
                component.isOptional(),
                component.getType());
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
            return tag;// TODO MJFString.format("%s", tag);
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
