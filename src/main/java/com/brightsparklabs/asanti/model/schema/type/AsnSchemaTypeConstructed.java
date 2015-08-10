/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypes;
import com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag;
import com.brightsparklabs.assam.schema.AsnBuiltinType;
import com.brightsparklabs.assam.schema.AsnPrimitiveType;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

/**
 * A type used to model the types for objects within ASN.1 schema that are Constructed, meaning that
 * they have Components. These objects can be either Type Definitions, e.g. Type ::= SomeType, or
 * components within a constructed type (SEQUENCE etc), e.g. component SomeType
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeConstructed extends BaseAsnSchemaType
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /**
     * built-in types which are considered 'constructed'. Currently: SEQUENCE, SET and CHOICE.
     */
    private static final ImmutableSet<AsnPrimitiveType> validTypes = ImmutableSet.of(
            AsnPrimitiveTypes.SET,
            AsnPrimitiveTypes.SEQUENCE,
            AsnPrimitiveTypes.CHOICE);

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** all of the AsnSchemaComponentType objects that this Constructed type "owns" */
    private final ImmutableList<AsnSchemaComponentType> componentTypes;

    /** the mechanism to be used for creation of Tags, during schema creation */
    private final TagCreator tagCreator;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param primitiveType
     *         the underlying primitiveType of the defined primitiveType
     * @param constraint
     *         The constraint on the type. Use {@link AsnSchemaConstraint#NULL} if no constraint.
     *
     *         <p> Example 1
     *
     *         <br> For {@code SET (SIZE (1..100) OF OCTET STRING (SIZE (10))} this would be {@code
     *         (SIZE (10)}.
     *
     *         <p> Example 2
     *
     *         <br> For {@code INTEGER (1..256)} this would be {@code (1..256)}.
     * @param componentTypes
     *         the component types within this defined type
     * @param taggingMode
     *         dictates the mode in which to handle/generate tags
     *
     * @throws NullPointerException
     *         if {@code componentTypes} or {@code taggingMode} are {@code null}
     * @throws IllegalArgumentException
     *         if {@code primitiveType} is not a constructed type (Currently: SEQUENCE, SET and
     *         CHOICE)
     */
    public AsnSchemaTypeConstructed(AsnPrimitiveType primitiveType, AsnSchemaConstraint constraint,
            List<AsnSchemaComponentType> componentTypes, AsnModuleTaggingMode taggingMode)
    {
        super(primitiveType, constraint);

        checkArgument(validTypes.contains(primitiveType),
                "Type must be either SET, SEQUENCE or CHOICE");

        checkNotNull(componentTypes);
        checkNotNull(taggingMode);
        this.componentTypes = ImmutableList.copyOf(componentTypes);
        this.tagCreator = TagCreator.create(primitiveType, taggingMode);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Determines if there are any duplicate tags (meaning that decoding would be ambiguous) and
     * throws if there are.  This function requires that all the components have their final tags,
     * i.e. that {@link AsnSchemaTypeConstructed#performTagging()} has been called
     *
     * @throws ParseException
     *         if there are duplicate tags
     */
    public void checkForDuplicates() throws ParseException
    {
        tagCreator.checkForDuplicates(componentTypes);
    }

    /**
     * Will provide an automatic or universal tag for each component as appropriate. This requires
     * that all the types and imports have been fully resolved.
     */
    public void performTagging()
    {
        tagCreator.setTagsForComponents(componentTypes);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BaseAsnSchemaType
    // -------------------------------------------------------------------------

    @Override
    public ImmutableList<AsnSchemaComponentType> getAllComponents()
    {
        return componentTypes;
    }

    @Override
    public Optional<AsnSchemaComponentType> getMatchingChild(String rawTag,
            DecodingSession decodingSession)
    {
        AsnSchemaTag tag = AsnSchemaTag.create(rawTag);
        return tagCreator.getComponentType(tag, componentTypes, decodingSession);
    }

    @Override
    public Object accept(final AsnSchemaTypeVisitor<?> visitor) throws ParseException
    {
        return visitor.visit(this);
    }

    /**
     * Creates Tag values for Constructed types (Sequence, Set Choice), and also subsequently
     * matches them back up during decoding
     *
     * @author brightSPARK Labs
     */
    public static class TagCreator
    {

        // -------------------------------------------------------------------------
        // CLASS VARIABLES
        // -------------------------------------------------------------------------

        /** class logger */
        private static final Logger logger = LoggerFactory.getLogger(TagCreator.class);

        /**
         * static instance of tag automator that checks the components according to the rules and
         * should be used when module tagging mode is AUTOMATIC
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
         *         determines whether the Constructed type will automatically generate
         *         context-specific tags
         *
         * @return a TagCreator object configured appropriately for the input parameter
         *
         * @throws NullPointerException
         *         if either {@code type} or {@code taggingMode} are {@code null}
         */
        public static TagCreator create(AsnPrimitiveType type, AsnModuleTaggingMode taggingMode)
        {
            checkNotNull(type);
            checkNotNull(taggingMode);

            return (type == AsnPrimitiveTypes.SEQUENCE) ?
                    ((taggingMode == AsnModuleTaggingMode.AUTOMATIC) ?
                            TAG_CREATION_SEQUENCE_AUTOMATED :
                            TAG_CREATION_SEQUENCE_FALSE) :
                    ((taggingMode == AsnModuleTaggingMode.AUTOMATIC) ?
                            TAG_CREATION_UNORDERED_AUTOMATED :
                            TAG_CREATION_UNORDERED_FALSE);
        }

        /**
         * For the given set of AsnSchemaComponentType objects generate the tags that are expected
         * to be received during decoding. This will vary depending on the Module Tagging mode and
         * whether the Constructed type is a Sequence or Set/Choice.  This will SET the tag for each
         * component to be either the tag that it already had if one was defined, auto generated if
         * the Module Tagging mode is AUTOMATIC, or the UNIVERSAL type.
         *
         * @param componentTypes
         *         AsnSchemaComponentType contained in the Constructed type
         */
        public void setTagsForComponents(Iterable<AsnSchemaComponentType> componentTypes)
        {
            // Check to see if we need to apply automatic tags
            boolean autoTag = tagAutomator.canAutomate(componentTypes);

            int index = 0;

            for (final AsnSchemaComponentType componentType : componentTypes)
            {
                // auto tag if appropriate, otherwise use the components' tag (which will be a context-specific),
                // otherwise use the Universal tag
                final String originalTag = componentType.getTag();
                final String rawTag = (autoTag) ?
                        String.valueOf(index) :
                        Strings.isNullOrEmpty(originalTag) ?
                                AsnSchemaTag.createUniversalPortion(componentType.getType()
                                        .getBuiltinType()) :
                                originalTag;

                // At the time of construction/parsing the component only had any value for a tag if
                // it was explicitly added in the schema.  Ensure it has the appropriate tag now that
                // the Auto tagging has been run and the type is known.
                if (!originalTag.equals(rawTag))
                {
                    componentType.setTag(rawTag);
                }
                index++;
            }
        }

        public void checkForDuplicates(Iterable<AsnSchemaComponentType> componentTypes)
                throws ParseException
        {
            // Key: decorated tag, Value: component name.  We only really need a List, but by
            // tracking the name of the component we can generate better error messages
            final Map<String, String> usedTags = Maps.newHashMap();
            int index = 0;

            for (final AsnSchemaComponentType componentType : componentTypes)
            {
                if (isTaglessChoice(componentType))
                {
                    checkChoiceForDuplicates(componentType, index, usedTags);
                }
                else
                {
                    final String decoratedTag = tagDecorator.getDecoratedTag(index,
                            componentType.getTag());
                    assertNotDuplicate(usedTags, decoratedTag, componentType.getName());
                }

                // In order to detect duplication in tags we include an expected 'index' in the sequence
                // as part of the tag, so the "tag portion" for each index needs to be unique.
                // We only generate such a tag ("index[tag]") for calculating duplicates, and only then
                // for Sequence types, not Set or Choice which are not ordered.
                if (!componentType.isOptional())
                {
                    index++;
                }
            }
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
        public Optional<AsnSchemaComponentType> getComponentType(AsnSchemaTag tag,
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
         * Choice components that do not have a context-specific tag (either manually or Auto
         * generated) will not be encoded, instead the selected Choice component will appear in its
         * place.  This function will recurse through such choice components if needed, or will
         * otherwise determine it the passed in component is a match
         *
         * @param componentType
         *         AsnSchemaComponentType to match against whether
         * @param index
         *         where we are up to in the Sequence
         * @param usedTags
         *         the tags that have already been used, how duplicates are detected.
         *
         * @throws ParseException
         *         if there is a duplicate tag
         */
        private void checkChoiceForDuplicates(AsnSchemaComponentType componentType, int index,
                Map<String, String> usedTags) throws ParseException
        {
            if (isTaglessChoice(componentType))
            {
                // Get all the components of the Choice, and check them for duplicates against the
                // other components at this level

                final ImmutableList<AsnSchemaComponentType> choiceComponents
                        = componentType.getType().getAllComponents();

                // These are already tagged etc, so we can just use the component's tag, we don't need
                // to worry about auto tagging etc.
                for (AsnSchemaComponentType choiceComponent : choiceComponents)
                {
                    final String rawTag = choiceComponent.getTag();

                    if (isTaglessChoice(choiceComponent))
                    {
                        // recurse!
                        checkChoiceForDuplicates(choiceComponent, index, usedTags);
                    }
                    else
                    {
                        final String decoratedTag = tagDecorator.getDecoratedTag(index, rawTag);
                        assertNotDuplicate(usedTags, decoratedTag, componentType.getName());
                    }
                }
            }
        }

        /**
         * @param componentType
         *         the component to check
         *
         * @return true if the component is of type Choice and does not have a context-specific tag
         */
        private static boolean isTaglessChoice(AsnSchemaComponentType componentType)
        {
            return ((componentType.getType().getBuiltinType() == AsnBuiltinType.Choice)
                    && componentType.getTag().isEmpty());
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
         * @return the matching AsnSchemaComponentType if there is a match, {@link
         * Optional#absent()} otherwise.  Noting that the returned component may be a sub-component
         * if the passed in component was a CHOICE, and in this case the Name will be fully
         * qualified to provide a full path
         */
        private static Optional<AsnSchemaComponentType> isMatch(AsnSchemaComponentType component,
                AsnSchemaTag tag, DecodingSession decodingSession)
        {
            if (isTaglessChoice(component))
            {

                // TODO ASN-152
                // We currently do not pre-calculate the Choice aspect upfront.  It is easy enough to do
                // and probably has some merit, but we can't just store the Choice components in a
                // "flat" list with all the other components because of OPTIONAL (ie the Choice may have
                // been optional or not)
                // In order for our decoded tag to include the Choice that was followed we need to
                // create a "fully qualified" tag name  (unless we change that loop in the SchemaImpl)
                // That fully qualified tag name is relative and it "owned" by the Constructed type that
                // had the component that was a choice, not the Choice type itself.

                // delegate through to the choice
                final Optional<AsnSchemaComponentType> choiceChild = component.getType()
                        .getMatchingChild(tag.getRawTag(), decodingSession);

                if (choiceChild.isPresent())
                {
                    final AsnSchemaComponentType result = choiceChild.get();
                    // TODO ASN-152 - making a new element here is adding a dependency that
                    // makes this module harder to test
                    return Optional.of(buildFullyQualifiedComponentType(result,
                            component.getName()));
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
         * TagDecorator for Sequence types, where the index is required for determination of
         * duplication and for later tag matching
         */
        private static class SequenceTagDecorator implements TagDecorator
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
        private static class UnorderedTagDecorator implements TagDecorator
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
             * Given the input components, determine whether it is appropriate to create Automatic
             * tags
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
             *         the DecodingSession that holds the state needed to manipulate the tag
             *         indexes
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
                int index = decodingSession.getIndex(tag);

                AsnSchemaComponentType component;
                do
                {
                    // Protect against more data than the schema knows about
                    if (index >= components.size())
                    {
                        return Optional.absent();
                    }

                    component = components.get(index);
                    final Optional<AsnSchemaComponentType> result = isMatch(component,
                            tag,
                            decodingSession);
                    if (result.isPresent())
                    {
                        decodingSession.setIndex(tag, index);
                        return result;
                    }

                    // If it was not a match, then if the component was optional and we are not
                    // already at the last component, then we can try the next one.
                    index++;
                } while (component.isOptional() && (index < components.size()));

                // if we failed a match then ensure all the subsequent components within this context
                // also fail (avoid an 'accidental' match)
                decodingSession.setIndex(tag, components.size());

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
                // TODO ASN-152 .  As part of figuring out ASN-152 consider if it would be more
                // efficient to store the components as a map (Tag->Component) when we are Unordered.
                // Currently the Constructed type "owns" the components and has a TagCreator which is one
                // of the statically created versions.
                // If we moved the "ownership" of the components to the TagCreator then it could store
                // as appropriate for its version, but we would need to create a new one for each
                // Constructed type (not an issue)

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
                }
                return Optional.absent();
            }
        }
    }
}
