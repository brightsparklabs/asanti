/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.tag;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypeConstructed;
import com.brightsparklabs.asanti.model.schema.type.GetAsnSchemaTypeVisitor;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

/**
 * Creates Tag values for Constructed types (Sequence, Set Choice)
 *
 * @author brightSPARK Labs
 */
public class TagCreator
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(TagCreator.class);

    /**
     * static instance of tag automator that checks the components according to the rules and should
     * be used when module tagging mode is AUTOMATIC
     */
    private static final TagAutomator TAG_AUTOMATOR_CHECK = new TagAutomatorCheck();

    /** static instance of the tag matcher that matches components in a Sequence */
    private static final TagMatchingCreator TAG_MATCHING_CREATOR_SEQUENCE
            = new TagMatchingCreatorSequence();

    /** static instance of the tag matcher that matches components in a Set and Choice */
    private static final TagMatchingCreator TAG_MATCHING_CREATOR_UNORDERED
            = new TagMatchingCreatorUnordered();

    /**
     * static instance of a tag automator that returns false and should be used with the module
     * tagging mode is anything other than AUTOMATIC
     */
    private static final TagAutomator TAG_AUTOMATOR_FALSE = new TagAutomatorFalse();

    /** static instance of a decorator for Sequences */
    private static final TagDecorator TAG_DECORATOR_SEQUENCE = new SequenceTagDecorator();

    /** static instance of a decorator for Set and Choice */
    private static final TagDecorator TAG_DECORATOR_UNORDERED = new UnorderedTagDecorator();

    /** static instance configure for Sequence and Auto tag generation */
    private static final TagCreator TAG_CREATION_SEQUENCE_AUTOMATED = new TagCreator(
            TAG_DECORATOR_SEQUENCE,
            TAG_AUTOMATOR_CHECK,
            TAG_MATCHING_CREATOR_SEQUENCE);

    /** static instance configure for Sequence and No auto tag generation */
    private static final TagCreator TAG_CREATION_SEQUENCE_FALSE = new TagCreator(
            TAG_DECORATOR_SEQUENCE,
            TAG_AUTOMATOR_FALSE,
            TAG_MATCHING_CREATOR_SEQUENCE);

    /** static instance configure for Set/Choice and Auto tag generation */
    private static final TagCreator TAG_CREATION_UNORDERED_AUTOMATED = new TagCreator(
            TAG_DECORATOR_UNORDERED,
            TAG_AUTOMATOR_CHECK,
            TAG_MATCHING_CREATOR_UNORDERED);

    /** static instance configure for Set/Choice and No auto tag generation */
    private static final TagCreator TAG_CREATION_UNORDERED_FALSE = new TagCreator(
            TAG_DECORATOR_UNORDERED,
            TAG_AUTOMATOR_FALSE,
            TAG_MATCHING_CREATOR_UNORDERED);

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    // Seems a little bit heavy weight to use this sort of Strategy pattern, but
    // both of these are decision points that can be made at construction time
    // rather than at loop time.

    /** the Strategy to use for "decorating" tags, ie do we store the index along with the tag */
    private final TagDecorator tagDecorator;

    /** the Strategy to use for determining whether to Automate tag creation */
    private final TagAutomator tagAutomator;

    /** the Strategy to use for matching up tags at decoding time */
    private final TagMatchingCreator tagMatchingCreator;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.  This is private, Use {@code create} instead
     *
     * @param tagDecorator
     *         the TagDecorator to be used to create the tags
     * @param tagAutomator
     *         the TagAutomator to determine whether to automatically create tags
     */
    private TagCreator(TagDecorator tagDecorator, TagAutomator tagAutomator,
            TagMatchingCreator tagMatchingCreator)
    {
        this.tagDecorator = tagDecorator;
        this.tagAutomator = tagAutomator;
        this.tagMatchingCreator = tagMatchingCreator;
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

    /**
     * For the given set of AsnSchemaComponentType objects generate the tags that are expected to be
     * received during decoding. This will vary depending on the Module Tagging mode and whether the
     * Constructed type is a Sequence or Set/Choice.
     *
     * @param componentTypes
     *         AsnSchemaComponentType contained in the Constructed type
     *
     * @return mapping of tag to AsnSchemaComponentType, where the tag is what is expected from the
     * BER file, and the AsnSchemaComponentType objects may have been "fully qualified" to ensure
     * that the full decoded tag path appears in teh XPath style that we desire.
     *
     * @throws ParseException
     *         if there are duplicate tags detected
     */
    //    public ImmutableMap<String, AsnSchemaComponentType> getTagsForComponents(
    //            Iterable<AsnSchemaComponentType> componentTypes) throws ParseException
    public ImmutableList<AsnSchemaComponentType> getTagsForComponents(
            Iterable<AsnSchemaComponentType> componentTypes) throws ParseException
    {
        final ImmutableList.Builder<AsnSchemaComponentType> builder = ImmutableList.builder();

        // Check to see if we need to apply automatic tags
        boolean autoTag = tagAutomator.canAutomate(componentTypes);

        // Key: decorated tag, Value: component name.  We only really need a List, but by
        // tracking the name of the component we can generate better error messages
        final Map<String, String> usedTags = Maps.newHashMap();

        int index = 0;
        int autoTagNumber = 0;

        for (final AsnSchemaComponentType componentType : componentTypes)
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

            if (!checkChoiceForDuplicates(componentType, autoTag, index, usedTags))
            {
                assertNotDuplicate(usedTags, decoratedTag, componentType.getName());
            }

            final AsnSchemaComponentType component = buildComponentType(componentType, rawTag);

            builder.add(component);

            // TODO MJF - how to explain this?  Can I put in a link to a design doc?  It is too complex for a comment here.
            if (!componentType.isOptional())
            {
                index++;
            }
            autoTagNumber++;
        }

        return builder.build();
    }

    /**
     * This is the "orthogonal" part of creating the tags - it matches them back up at decoding
     * time.
     *
     * @param tag
     *         the tag to match
     * @param components
     *         the components to match from
     * @param decodingSession
     *         The {@link DecodingSession} used to maintain state while decoding a PDU of tags
     *
     * @return the matching component or {@link Optional#absent()} if no match
     */
    public Optional<AsnSchemaComponentType> getComponentTypes(AsnSchemaTag tag,
            ImmutableList<AsnSchemaComponentType> components, DecodingSession decodingSession)
    {
        return tagMatchingCreator.getComponent(tag, components, decodingSession);
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

        usedTags.put(decoratedTag, componentName);
    }

    /**
     * TODO MJF
     *
     * @param componentType
     * @param autoTag
     * @param index
     * @param usedTags
     *
     * @return
     *
     * @throws ParseException
     *         if there is a duplicate tag
     */
    private boolean checkChoiceForDuplicates(AsnSchemaComponentType componentType, boolean autoTag,
            int index, Map<String, String> usedTags) throws ParseException
    {
        // TODO MJF - AA so we only go here if it is a Choice not a Collection OF CHOICE
        if ((componentType.getType().getBuiltinTypeAA() == AsnBuiltinType.Choice)
                && componentType.getTag().isEmpty() && (!autoTag))
        {
            // If a component is a Choice, and does not have a tag then the Choice's components
            // can appear at this level
            // Get the components of the choice
            final GetAsnSchemaTypeVisitor visitor = GetAsnSchemaTypeVisitor.getInstance();
            final AsnSchemaType type = componentType.getType();
            final AsnSchemaTypeConstructed choiceType = (AsnSchemaTypeConstructed) type.accept(
                    visitor);
            final ImmutableList<AsnSchemaComponentType> choiceComponents
                    = choiceType.getTagsToComponentTypes();

            // These are already tagged etc.  What we need to do is reset the rawTag to align
            // with the index we are currently at, and reset the decoded tag to be "fully qualified"
            // ie to contain this components name and the Choice components name.
            //for (ImmutableMap.Entry<String, AsnSchemaComponentType> entry : choiceTypes.entrySet())
            for (AsnSchemaComponentType component : choiceComponents)
            {
                final String rawTag = component.getTag();
                final AsnSchemaComponentType choiceComponent = component;
                final String decoratedTag = tagDecorator.getDecoratedTag(index, rawTag);

                //  recursively check if needed.
                if (!checkChoiceForDuplicates(choiceComponent, autoTag, index, usedTags))
                {
                    assertNotDuplicate(usedTags, decoratedTag, choiceComponent.getName());
                }
            }

            return true;
        }

        return false;
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
     *
     * @throws NullPointerException
     *         is rawTag or component are null
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
     *
     * @throws NullPointerException
     *         is prefix or component are null
     */
    private static AsnSchemaComponentType buildFullyQualifiedComponentType(
            AsnSchemaComponentType component, String prefix)
    {
        checkNotNull(prefix);
        checkNotNull(component);

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

    /**
     * Determines if the passed in component is a match for the passed in tag.  This takes into
     * account the behaviour of Choice items (ie a choice component can transparently appear in
     * place of the parent component)
     *
     * @param component
     *         the AsnSchemaComponentType to match
     * @param tag
     *         the AsnSchemaTag to match
     * @param decodingSession
     *         the DecodingSession to use for context
     *
     * @return the matching AsnSchemaComponentType if there is a match, {@link Optional#absent()}
     * otherwise.  Noting that the returned component may be a sub-component if the passed in
     * component was a CHOICE, and in this case the Name will be fully qualified to provide a full
     * path
     */
    private static Optional<AsnSchemaComponentType> isMatch(AsnSchemaComponentType component,
            AsnSchemaTag tag, DecodingSession decodingSession)
    {
        if ((component.getType().getBuiltinTypeAA() == AsnBuiltinType.Choice) && component.getTag()
                .isEmpty())
        {
            try
            {
                // This is a Choice with no tags, which means it will "transparently" replace its
                // children at this level.
                final GetAsnSchemaTypeVisitor visitor = GetAsnSchemaTypeVisitor.getInstance();
                final AsnSchemaType type = component.getType();
                final AsnSchemaTypeConstructed choiceType = (AsnSchemaTypeConstructed) type.accept(
                        visitor);

                final Optional<AsnSchemaComponentType> choiceChild = choiceType.getMatchingChild(tag
                        .getRawTag(), decodingSession);

                if (choiceChild.isPresent())
                {
                    final AsnSchemaComponentType result = choiceChild.get();
                    return Optional.of(buildFullyQualifiedComponentType(result,
                            component.getName()));
                }
            }
            catch (ParseException e)    // TODO MJF - not good...
            {
                return Optional.absent();
            }
        }

        // Is it a direct match
        if (component.getTag().equals(tag.getTagPortion()))
        {
            return Optional.of(component);
        }

        // No match
        return Optional.absent();
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASSES
    // -------------------------------------------------------------------------

    /**
     * Interface for creating the tag that we will store for later matching
     */
    public interface TagDecorator
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
    public static class SequenceTagDecorator implements TagDecorator
    {
        // -------------------------------------------------------------------------
        // IMPLEMENTATION: TagDecorator
        // -------------------------------------------------------------------------
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
    public static class UnorderedTagDecorator implements TagDecorator
    {
        // -------------------------------------------------------------------------
        // IMPLEMENTATION: TagDecorator
        // -------------------------------------------------------------------------
        @Override
        public String getDecoratedTag(int index, String tag)
        {
            return tag;
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
        // -------------------------------------------------------------------------
        // IMPLEMENTATION: TagAutomator
        // -------------------------------------------------------------------------
        @Override
        public boolean canAutomate(Iterable<AsnSchemaComponentType> componentTypes)
        {
            // only need to automatically tag, if the global mode is automatic AND
            // none of the components in the Constructed type have context-specific tags
            boolean canAutomate = true;
            for (final AsnSchemaComponentType componentType : componentTypes)
            {
                if (!Strings.isNullOrEmpty(componentType.getTag()))
                {
                    canAutomate = false;
                    break;  // fail early
                }
            }
            return canAutomate;
        }
    }

    /**
     * TagAutomator to use when the tagging mode is not {@link AsnModuleTaggingMode#AUTOMATIC},
     * simply returns false - ie don't create an automatic tag
     */
    private static class TagAutomatorFalse implements TagAutomator
    {
        // -------------------------------------------------------------------------
        // IMPLEMENTATION: TagAutomator
        // -------------------------------------------------------------------------
        @Override
        public boolean canAutomate(Iterable<AsnSchemaComponentType> componentTypes)
        {
            return false;
        }
    }

    private interface TagMatchingCreator
    {
        /**
         * Returns a AsnSchemaComponentType from the supplied map that matches the supplied tag.
         * "Matches" is not a direct lookup as there may be some tag manipulation required
         *
         * @param tag
         *         tag to match
         * @param components
         *         the input components to match from
         * @param decodingSession
         *         the DecodingSession that holds the state needed to manipulate the tag indexes
         *
         * @return appropriate AsnSchemaComponentType if match found, otherwise {@code
         * Optional.absent()}
         */
        Optional<AsnSchemaComponentType> getComponent(AsnSchemaTag tag,
                List<AsnSchemaComponentType> components, DecodingSession decodingSession);
    }

    /**
     * implementation of TagMatchingCreator for Sequence types
     */
    private static class TagMatchingCreatorSequence implements TagMatchingCreator
    {
        @Override
        public Optional<AsnSchemaComponentType> getComponent(AsnSchemaTag tag,
                List<AsnSchemaComponentType> components, DecodingSession decodingSession)
        {
            // For Sequences, where order matters, we keep track of where we are to in the Sequence
            // during decoding (via the DecodingSession).

            // Get the next expected component.
            int index = decodingSession.getOffset(tag);

            AsnSchemaComponentType component;
            do
            {
                component = components.get(index);
                final Optional<AsnSchemaComponentType> result = isMatch(component,
                        tag,
                        decodingSession);
                if (result.isPresent())
                {
                    decodingSession.setOffset(tag, index);
                    return result;
                }

                // If it was not a match, then if the component was optional and we are not
                // already at the last component, then we can try the next one.
                index++;
            } while (component.isOptional() && (index < components.size()));

            //  No match found.
            return Optional.absent();
        }
    }

    /**
     * implementation of TagMatchingCreator for Set and Choice types
     */
    private static class TagMatchingCreatorUnordered implements TagMatchingCreator
    {
        @Override
        public Optional<AsnSchemaComponentType> getComponent(AsnSchemaTag tag,
                List<AsnSchemaComponentType> components, DecodingSession decodingSession)
        {
            // Unordered (Set and Choice) don't use the index, so don't need to re-align to
            // account for offsets for OPTIONALS.
            for (AsnSchemaComponentType component : components)
            {
                final Optional<AsnSchemaComponentType> result = isMatch(component,
                        tag,
                        decodingSession);
                if (result.isPresent())
                {
                    return result;
                }

                // TODO MJF - how to avoid matching the same thing multiple times?
                // Obviously the decodingSession will have to manage this
            }
            return Optional.absent();
        }
    }
}
