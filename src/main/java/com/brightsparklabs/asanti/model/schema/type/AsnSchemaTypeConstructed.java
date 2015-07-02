package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
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
    private final ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes;

    private final boolean tagLess;
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
            Iterable<AsnSchemaComponentType> componentTypes)
    {
        super(primitiveType, constraint);

        checkArgument(validTypes.contains(primitiveType),
                "Type must be either SET, SEQUENCE or CHOICE");

        checkNotNull(componentTypes);

        final ImmutableMap.Builder<String, AsnSchemaComponentType> tagsToComponentTypesBuilder
                = ImmutableMap.builder();

        // next expected tag is used to generate tags for automatic tagging
        // TODO ASN-80 - ensure that generating for all missing tags is correct
        int nextExpectedTag = 0;

        boolean anyTagSet = false;

        for (final AsnSchemaComponentType componentType : componentTypes)
        {
            String tag = componentType.getTag();
            if (Strings.isNullOrEmpty(tag))
            {
                tag = String.valueOf(nextExpectedTag);
                logger.debug("Generated automatic tag [{}] for {}",
                        tag,
                        componentType.getTagName());
                nextExpectedTag++;
            }
            else
            {
                nextExpectedTag = Integer.parseInt(tag) + 1;
                anyTagSet = true;
            }
            tagsToComponentTypesBuilder.put(tag, componentType);
        }
        tagsToComponentTypes = tagsToComponentTypesBuilder.build();

        tagLess = !anyTagSet;

        if ((primitiveType == AsnPrimitiveType.CHOICE) && (tagLess == true))
        {
            int breakpoint = 0;
        }

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
            if (tagLess == true)
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
        //if ((primitiveType == AsnPrimitiveType.CHOICE) && (tagLess == true))
        if (tagLess == true)
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
        final AsnSchemaTag schemaTag = AsnSchemaTag.create(tag);
        if (!schemaTag.getTagIndex().isEmpty())
        {
            final AsnSchemaComponentType component = getComponent(schemaTag.getTagNumber());
            // Now from the component, we need to get the Universal type.
            AsnSchemaType type = component.getType();
            if ((primitiveType == AsnPrimitiveType.CHOICE) && (tagLess == true))
            {

                String universalType = "u." + AsnBuiltinType.Sequence;

                logger.debug(
                        "AsnSchemaTypeConstructed.getChildType has {} - match component {}",
                        schemaTag.getTagIndex(),
                        component.getTagName());

                return type.getChildType(universalType);
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

                // This is a universal tag, so we should iterate the components
                // and see which one matches...
                for(Map.Entry<String, AsnSchemaComponentType>  entry : tagsToComponentTypes.entrySet())
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

                        return "/" + component.getTagName() + append;
                    }
                    logger.debug("component {} of type {} is NOT a match for type {}", component.getTagName(), cT, typeToMatch);
                }
                logger.debug("did NOT find a match for {}", typeToMatch);
            }
        }

        //if ((primitiveType == AsnPrimitiveType.CHOICE) && (tagLess == true))
        if (tagLess == true)
        {
            // not going to a "raw" type, so return the first tag match from our children
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


        if (!schemaTag.getTagIndex().isEmpty())
        {
            final AsnSchemaComponentType component = getComponent(schemaTag.getTagNumber());
            // Now from the component, we need to get the Universal type.
            AsnSchemaType type = component.getType();

            if ((primitiveType == AsnPrimitiveType.CHOICE) && (tagLess == true))
            {
                String universalType = "u." + AsnBuiltinType.Sequence;

                logger.debug("AsnSchemaTypeConstructed.getChildName has {} - match component {}",
                        schemaTag.getTagIndex(),
                        component.getTagName());

                return component.getTagName() + type.getChildName(universalType) + schemaTag.getTagIndex();
            }
        }


        final AsnSchemaComponentType componentType
                = tagsToComponentTypes.get(schemaTag.getTagNumber());
        return (componentType == null) ? "" : componentType.getTagName() + schemaTag.getTagIndex();
    }

    /**
     * This function allows the 'tree' to be walked, by being able to get to child types.
     *
     * @return the Map of components - names to types.
     */
    public ImmutableMap<String, AsnSchemaComponentType> getAllComponents()
    {
        return tagsToComponentTypes;
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
