package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.*;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(AsnSchemaTypeConstructed.class);

    /**
     * built-in types which are considered 'constructed'. Currently: SEQUENCE, SET and CHOICE.
     * ENUMERATED is treated differently.
     */
    private static final ImmutableSet<AsnPrimitiveType> validTypes = ImmutableSet.of(
            AsnPrimitiveType.SET,
            AsnPrimitiveType.SEQUENCE,
            AsnPrimitiveType.CHOICE);

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** mapping from raw tag to component type */
    private ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes;

    private final Iterable<AsnSchemaComponentType> componentTypes;
    /** the mode to use for tagging */
    private final AsnModuleTaggingMode tagMode;

    private boolean tagLess;

    private TaggingStrategy taggingStrategy;
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
     *
     * @throws NullPointerException
     *         if {@code name} or {@code builtinType} are {@code null}
     * @throws IllegalArgumentException
     *         if {@code primitiveType} is not a constructed type (Currently: SEQUENCE, SET and
     *         CHOICE)
     */


    public AsnSchemaTypeConstructed(AsnPrimitiveType primitiveType, AsnSchemaConstraint constraint,
            Iterable<AsnSchemaComponentType> componentTypes) throws ParseException
    {
        // TODO MJF - this is only here to not have to update the tests while I am playing.
        this(primitiveType, constraint, componentTypes, AsnModuleTaggingMode.DEFAULT);
    }

    public AsnSchemaTypeConstructed(AsnPrimitiveType primitiveType, AsnSchemaConstraint constraint,
            Iterable<AsnSchemaComponentType> componentTypes, AsnModuleTaggingMode tagMode) throws
            ParseException
    {
        super(primitiveType, constraint);

        checkArgument(validTypes.contains(primitiveType),
                "Type must be either SET, SEQUENCE or CHOICE");

        checkNotNull(componentTypes);
        // TODO MJF - update all call tree (ie all the functions that I added tagMode as a parameter)
        // to include it in javadoc.  Also update javadoc for throw reasons.

        checkNotNull(tagMode);
        this.tagMode = tagMode;
        this.componentTypes = componentTypes;


        // TODO MJF - might Factory this...
        if (primitiveType == AsnPrimitiveType.CHOICE)
        {
            this.taggingStrategy = new ChoiceTaggingStrategy();
        }
        else
        {
            this.taggingStrategy = new SequenceTaggingStrategy();
        }


        // TODO MJF - Yuk!!!  Better design please!!!
        // Do this now so that all the components can be visited later,
        // then once the Placeholders are resolved it should be done again!
        //tagsToComponentTypes = taggingStrategy.getTagsForComponents(componentTypes, tagMode, false);
    }

    public void performTagging() throws ParseException
    {
        tagsToComponentTypes = taggingStrategy.getTagsForComponents(componentTypes, tagMode);
        //tagLess = !anyTagSet;

/*
        final ImmutableMap.Builder<String, AsnSchemaComponentType> tagsToComponentTypesBuilder
                = ImmutableMap.builder();

        // The first pass is to see if any components have specified tags
        boolean anyTagSet = false;
        for (final AsnSchemaComponentType componentType : componentTypes)
        {
            String tag = componentType.getTag();
            if (!Strings.isNullOrEmpty(tag))
            {
                anyTagSet = true;
            }
        }

        boolean appliedAutomaticTags = false;
        // Only add tags if we are in AUTOMATIC tagging mode.
        if (tagMode == AsnModuleTaggingMode.AUTOMATIC)
        {
            // If ANY of the components have a specified tag then do NOT automatically
            // tag any other component.

            if (!anyTagSet)
            {
                // If here then there are no tags already set, and we are in AUTOMATIC mode,
                // so add context-specific tags to all components, starting at 0
                int index = 0;
                for (final AsnSchemaComponentType componentType : componentTypes)
                {
                    String tag = String.valueOf(index);
                    logger.debug("Generated automatic tag [{}] for {}",
                            tag,
                            componentType.getTagName());
                    index++;
                    tagsToComponentTypesBuilder.put(tag, componentType);
                }

                appliedAutomaticTags = true;
            }
        }

        if (!appliedAutomaticTags)
        {
            // TODO MJF
            // Should we store any non specifier set tags as the universal?
            // ie anywhere that does not have [] should we add something
            // we need to do something otherwise we can't store them all as they will
            // all have the same key...
            int index = 0;
            for (final AsnSchemaComponentType componentType : componentTypes)
            {
                String tag = componentType.getTag();
                if (Strings.isNullOrEmpty(tag))
                {
                    tag = String.format("(u.%s.%d)", componentType.getType().getBuiltinType(), index);
                    logger.debug("Generated universal tag {} for {}",
                            tag,
                            componentType.getTagName());
                }
                index++;
                tagsToComponentTypesBuilder.put(tag, componentType);
            }
        }
*/
    }


    @Override
    public AsnSchemaNamedType getMatchingChild(String tag, DecodingSession session)
    {
        return taggingStrategy.getMatchingComponent(tag, tagsToComponentTypes, session);
    }

    /**
     * Returns the {@code AsnSchemaComponentType} for the tag, or null if none found
     *
     * @param tag
     *         a tag within this construct
     *
     * @return the {@code AsnSchemaComponentType} for the tag, or null if none found
     */
    public AsnSchemaComponentType getComponent(String tag)
    {
        final AsnSchemaTag schemaTag = AsnSchemaTag.create(tag);
        if (schemaTag == AsnSchemaTag.NULL)
        {
            logger.warn("Invalid tag supplied. Expected format: 'tag' or 'tag[index]', received: {}",
                    tag);
            return null;
        }
        final AsnSchemaComponentType componentType
                = tagsToComponentTypes.get(schemaTag.getTagNumber());
        return componentType;
    }


    @Override
    public AsnSchemaType getChildType(String tag)
    {
        final AsnSchemaNamedType component = taggingStrategy.getMatchingComponent(tag, tagsToComponentTypes, null);
        return component == null ? AsnSchemaType.NULL : component.getType();


    }

    @Override
    public String getChildName(String tag)
    {
        final AsnSchemaNamedType component = taggingStrategy.getMatchingComponent(tag, tagsToComponentTypes, null);
        return (component == null) ? "" : component.getName();
    }
/*
    @Override
    public AsnSchemaType getChildType(String tag)
    {

        Matcher matcher = PATTERN_UNIVERSAL_TYPE_TAG2.matcher(tag);
        if (matcher.matches())
        {
            // If here then we have the tag group 1, and following
            // that we have a Universal.
            String tagPart = matcher.group(1);
            String universalType = matcher.group(2);
            //AsnBuiltinType typeToMatch = AsnBuiltinType.valueOf(universalType);

            final AsnSchemaComponentType component = getComponent(tagPart);

            // Now from the component, we need to get the Universal type.
            AsnSchemaType type = component.getType();
            return type.getChildType(universalType);

        }

        matcher = PATTERN_UNIVERSAL_TYPE_TAG.matcher(tag);
        if (matcher.matches())
        {
            //if ((primitiveType == AsnPrimitiveType.CHOICE) && (tagLess == true))
            if (tagLess)
            {
                String aa = matcher.group(1);
                AsnBuiltinType typeToMatch = AsnBuiltinType.valueOf(matcher.group(1));

                // This is a universal tag, so we should iterate the components
                // and see which one matches...
                for(Map.Entry<String, AsnSchemaComponentType>  entry : tagsToComponentTypes.entrySet())
                {
                    // TODO MJF - this does not work for Sequence Of, because we deliberately made that
                    // a transparent type
                    AsnSchemaComponentType component = entry.getValue();
                    AsnBuiltinType cT = component.getType().getBuiltinTypeAA();
                    if (match(typeToMatch, cT))
                    {
                        logger.debug("component {} matches type {}", component.getTagName(), cT);
                        return component.getType();
                    }
                }
                logger.debug("did NOT find a match for {}", typeToMatch);
            }
        }
        if ((primitiveType == AsnPrimitiveType.CHOICE) && (tagLess == true))
        //if (tagLess == true)
        {
            // not going to a "raw" type, so return the first tag match from our children
            for(Map.Entry<String, AsnSchemaComponentType>  entry : tagsToComponentTypes.entrySet())
            {

                AsnSchemaComponentType component = entry.getValue();
                if (AsnSchemaType.NULL != component.getType().getChildType(tag))
                {
                    return component.getType().getChildType(tag);
                }
            }

        }


        final AsnSchemaComponentType component = getComponent(tag);
        return component == null ? AsnSchemaType.NULL : component.getType();
    }

    @Override
    public String getChildName(String tag)
    {

        Matcher matcher = PATTERN_UNIVERSAL_TYPE_TAG2.matcher(tag);
        if (matcher.matches())
        {
            // If here then we have the tag group 1, and following
            // that we have a Universal.
            String tagPart = matcher.group(1);
            String universalType = matcher.group(2);
            String tagIndex = matcher.group(5);
            //AsnBuiltinType typeToMatch = AsnBuiltinType.valueOf(universalType);

            final AsnSchemaComponentType component = getComponent(tagPart);

            // Now from the component, we need to get the Universal type.
            AsnBuiltinType cT = component.getType().getBuiltinType();
            AsnSchemaType type = component.getType();

            return component.getTagName() + component.getType().getChildName(universalType);

        }


        matcher = PATTERN_UNIVERSAL_TYPE_TAG.matcher(tag);
        if (matcher.matches())
        {
            //if ((primitiveType == AsnPrimitiveType.CHOICE) && (tagLess == true))
            if (tagLess == true)
            {
                String aa = matcher.group(1);
                String tagIndex = Strings.nullToEmpty(matcher.group(3));
                AsnBuiltinType typeToMatch = AsnBuiltinType.valueOf(matcher.group(1));

                if (primitiveType == AsnPrimitiveType.CHOICE)
                {
                    // If we have a tagless Choice, and we don't explicitly know the type
                    // then we find the first match.
                    // NOTE that there should only bne one match, but we treat the Sequence and
                    // Sequence Of the same, so it will find both.
                    // In that case one of the options will have universal tags.
                    for (Map.Entry<String, AsnSchemaComponentType> entry : tagsToComponentTypes.entrySet())
                    {
                        AsnSchemaComponentType component = entry.getValue();
                        AsnBuiltinType cT = component.getType().getBuiltinTypeAA();
                        if (match(typeToMatch, cT))
                        {
                            logger.debug("component {} matches type {}", component.getTagName(), cT);
                            //return component.getType().getChildName(tag);

                            String append = "";
                            if (component.getType().getBuiltinTypeAA() == AsnBuiltinType.SequenceOf)
                            {
                                append = "[" + tagIndex + "]";
                            }

                            //return "/" + component.getTagName() + append;
                            return component.getTagName() + append;
                        }
                        logger.debug("component {} of type {} is NOT a match for type {}",
                                component.getTagName(),
                                cT,
                                typeToMatch);
                    }
                    logger.debug("did NOT find a match for {}", typeToMatch);
                }
                else
                {
                    // we store components that don't have context-specific tags ([] style) as
                    // u.XX.index, which should be a direct lookup in our map.
                    final AsnSchemaComponentType componentType
                            = tagsToComponentTypes.get(tag);

                    if (componentType != null)
                    {
                        int breakpoint = 0;
                        //return "/" + componentType.getTagName();
                        return componentType.getTagName();
                    }

//                    final AsnSchemaComponentType componentType
//                            = tagsToComponentTypes.get(tagIndex);
//                    return (componentType == null) ? "" : "/" + componentType.getTagName();
                }
            }
        }

        if ((primitiveType == AsnPrimitiveType.CHOICE) && (tagLess == true))
        //if (tagLess == true)
        {
            // not going to a "universal" type, so return the first tag match from our children
            for(Map.Entry<String, AsnSchemaComponentType>  entry : tagsToComponentTypes.entrySet())
            {
                logger.debug("tagless Choice, iterating children");
                AsnSchemaComponentType component = entry.getValue();
                if (AsnSchemaType.NULL != component.getType().getChildType(tag))
                {
                    logger.debug("component {} matches tag {}", component.getTagName(), tag);
                    return component.getTagName() + "/" + component.getType().getChildName(tag);
                }
                logger.debug("tagless Choice, NO MATCH found");
            }
        }

        final AsnSchemaTag schemaTag = AsnSchemaTag.create(tag);
        if (schemaTag == AsnSchemaTag.NULL)
        {
            logger.warn("Invalid tag supplied. Expected format: 'tag' or 'tag[index]', received: {}",
                    tag);
            return "";
        }
        final AsnSchemaComponentType componentType
                = tagsToComponentTypes.get(schemaTag.getTagNumber());
        return (componentType == null) ? "" : componentType.getTagName() + schemaTag.getTagIndex();
    }
*/
    /**
     * This function allows the 'tree' to be walked, by being able to get to child types.
     *
     * @return the Map of components - names to types.
     */
    //public ImmutableMap<String, AsnSchemaComponentType> getAllComponents()
    public Iterable<AsnSchemaComponentType> getAllComponents()
    {
        //return tagsToComponentTypes;
        return componentTypes;
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

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: AsnSchemaTag
    // -------------------------------------------------------------------------

    /**
     * Models a tag in a 'constructed' type definition. A tag conforms to one of the following
     * formats: <ul> <li>tagNumber (e.g. {@code 1}</li> <li>tagNumber[tagIndex] (e.g. {@code
     * 1[0]}</li> </ul>
     */
    private static class AsnSchemaTag
    {
        // ---------------------------------------------------------------------
        // CONSTANTS
        // ---------------------------------------------------------------------

        /** null instance */
        private static final AsnSchemaTag NULL = new AsnSchemaTag("", "");

        /** pattern to check raw tag against */
        private static final Pattern PATTERN_TAG = Pattern.compile("(\\d+)(\\[\\d+\\])?$");

        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /** the tag number component of the raw tag */
        private final String tagNumber;

        /** the tag index component of the raw tag. Blank if no index component */
        private final String tagIndex;

        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor. Private, use {@link #create(String)} instead.
         *
         * @param tagNumber
         *         tag number component of the raw tag
         * @param tagIndex
         *         tag index component of the raw tag. Set to {@code null} if no index component.
         */
        private AsnSchemaTag(String tagNumber, String tagIndex)
        {
            this.tagNumber = tagNumber;
            this.tagIndex = tagIndex == null ? "" : tagIndex;
        }

        /**
         * Creates an instance from the supplied raw tag
         *
         * @param rawTag
         *         raw tag to create instance from
         *
         * @return instance which models the raw tag, or {@link #NULL} if the raw tag is invalid
         */
        public static AsnSchemaTag create(String rawTag)
        {
            if (rawTag == null)
            {
                return NULL;
            }

            final Matcher matcher = PATTERN_TAG.matcher(rawTag);
            if (matcher.matches())
            {
                return new AsnSchemaTag(matcher.group(1), matcher.group(2));
            }
            else
            {
                return NULL;
            }
        }

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

        /**
         * Returns the tag number component of the raw tag. For a raw tag {@code "1[0]"} this is
         * {@code "1"}.
         *
         * @return the tag number
         */
        public String getTagNumber()
        {
            return tagNumber;
        }

        /**
         * Returns the tag index component of the raw tag. For a raw tag {@code "1[0]"} this is
         * {@code "[0]"}.
         *
         * @return the tag index or a blank string if no index component
         */
        public String getTagIndex()
        {
            return tagIndex;
        }
    }
}
