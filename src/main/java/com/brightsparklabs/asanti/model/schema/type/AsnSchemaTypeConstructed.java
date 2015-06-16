package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Michael on 16/06/2015.
 */
public class AsnSchemaTypeConstructed extends AbstractAsnSchemaType
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(
            AsnSchemaTypeConstructed.class);

    /** mapping from raw tag to component type */
    private final ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param primitiveType
     *            the underlying primitiveType of the defined primitiveType
     *
     * @param componentTypes
     *            the component types within this defined type
     *
     * @param constraint
     *            The constraint on the type. Use
     *            {@link AsnSchemaConstraint#NULL} if no constraint.
     *            <p>
     *            Example 1<br>
     *            For {@code SET (SIZE (1..100) OF OCTET STRING (SIZE (10))}
     *            this would be {@code (SIZE (10)}.
     *            <p>
     *            Example 2<br>
     *            For {@code INTEGER (1..256)} this would be {@code (1..256)}.
     *
     * @throws NullPointerException
     *             if {@code name} or {@code builtinType} are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} is blank
     */
    public AsnSchemaTypeConstructed(AsnPrimitiveType primitiveType, Iterable<AsnSchemaComponentType> componentTypes, AsnSchemaConstraint constraint)
    {
        super(primitiveType, constraint);

        checkNotNull(componentTypes);

        final ImmutableMap.Builder<String, AsnSchemaComponentType> tagsToComponentTypesBuilder = ImmutableMap.builder();

        // next expected tag is used to generate tags for automatic tagging
        // TODO ASN-80 - ensure that generating for all missing tags is correct
        int nextExpectedTag = 0;

        for (final AsnSchemaComponentType componentType : componentTypes)
        {
            String tag = componentType.getTag();
            if (Strings.isNullOrEmpty(tag))
            {
                tag = String.valueOf(nextExpectedTag);
                logger.debug("Generated automatic tag [{}] for {}", tag, componentType.getTagName());
                nextExpectedTag++;
            }
            else
            {
                nextExpectedTag = Integer.parseInt(tag) + 1;
            }
            tagsToComponentTypesBuilder.put(tag, componentType);
        }
        tagsToComponentTypes = tagsToComponentTypesBuilder.build();
    }


    public String getTagName(String tag)
    {
        final AsnSchemaTag schemaTag = AsnSchemaTag.create(tag);
        if (schemaTag == AsnSchemaTag.NULL)
        {
            logger.warn("Invalid tag supplied. Expected format: 'tag' or 'tag[index]', received: {}", tag);
            return "";
        }

        final AsnSchemaComponentType componentType = tagsToComponentTypes.get(schemaTag.getTagNumber());
        return (componentType == null) ? "" : componentType.getTagName() + schemaTag.getTagIndex();
    }


    /**
     * Returns the {@code AsnSchemaComponentType} for the tag, or null if none found
     * @param tag
     * @return
     */
    public AsnSchemaComponentType getComponent(String tag)
    {
        final AsnSchemaTag schemaTag = AsnSchemaTag.create(tag);
        if (schemaTag == AsnSchemaTag.NULL)
        {
            logger.warn("Invalid tag supplied. Expected format: 'tag' or 'tag[index]', received: {}", tag);
            return null;
        }
        final AsnSchemaComponentType componentType = tagsToComponentTypes.get(schemaTag.getTagNumber());
        return componentType;
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: AsnSchemaTag
    // -------------------------------------------------------------------------

    /**
     * Models a tag in a 'constructed' type definition. A tag conforms to one of
     * the following formats:
     * <ul>
     * <li>tagNumber (e.g. {@code 1}</li>
     * <li>tagNumber[tagIndex] (e.g. {@code 1[0]}</li>
     * </ul>
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
         *            tag number component of the raw tag
         *
         * @param tagIndex
         *            tag index component of the raw tag. Set to {@code null} if
         *            no index component.
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
         *            raw tag to create instance from
         *
         * @return instance which models the raw tag, or {@link #NULL} if the
         *         raw tag is invalid
         */
        public static AsnSchemaTag create(String rawTag)
        {
            if (rawTag == null) { return NULL; }

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
         * Returns the tag number component of the raw tag. For a raw tag
         * {@code "1[0]"} this is {@code "1"}.
         *
         * @return the tag number
         */
        public String getTagNumber()
        {
            return tagNumber;
        }

        /**
         * Returns the tag index component of the raw tag. For a raw tag
         * {@code "1[0]"} this is {@code "[0]"}.
         *
         * @return the tag index or a blank string if no index component
         */
        public String getTagIndex()
        {
            return tagIndex;
        }
    }

}