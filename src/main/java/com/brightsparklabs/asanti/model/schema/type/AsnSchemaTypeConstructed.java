/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.AsnModuleTaggingMode;
import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag;
import com.brightsparklabs.asanti.model.schema.tag.TagCreator;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.text.ParseException;
import java.util.List;

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
            AsnPrimitiveType.SET,
            AsnPrimitiveType.SEQUENCE,
            AsnPrimitiveType.CHOICE);

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** all of the AsnSchemaComponentType objects that this Constructed type "owns" */
    private final ImmutableList<AsnSchemaComponentType> componentTypes;

    /** the mechanism to be used for creation of Tags, during schema creation */
    private final TagCreator tagCreator;

    /** keeps track of whether we have performed the post parsing Tagging step */
    private boolean havePerformedTagging = false;

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
     *         the mode to be used to determine tags for components
     *
     * @throws NullPointerException
     *         if {@code name}, {@code builtinType} or {@code taggingMode} are {@code null}
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
     * This function allows the 'tree' to be walked, by being able to get to child types.
     *
     * @return the components
     */
    public ImmutableList<AsnSchemaComponentType> getAllComponents()
    {
        return componentTypes;
    }


    /**
     * We store a mapping of tags to components.  That mapping is based on the rawTag that we are
     * expecting to receive from the data.  Because some tags are not explicit (ie context-specific)
     * we need to know the types of items to create Universal tags.  Until the whole schema has been
     * parsed and all the Type Definitions and import resolved we may not know those types, ie we
     * likely won't know them at the time when this object is constructed. This function allows the
     * builder/coordinator of the schema to tell us when we can perform the tag creation
     *
     * @throws ParseException
     *         if there are duplicate tags
     */
    public void performTagging() throws ParseException
    {
        if (!havePerformedTagging)
        {
            havePerformedTagging = true;
            tagCreator.setTagsForComponents(componentTypes);

        }
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BaseAsnSchemaType
    // -------------------------------------------------------------------------

    @Override
    public Optional<AsnSchemaComponentType> getMatchingChild(String rawTag,
            DecodingSession decodingSession)
    {
        AsnSchemaTag tag = AsnSchemaTag.create(rawTag);
        return tagCreator.getComponentTypes(tag, componentTypes, decodingSession);
    }

    @Override
    public Object accept(final AsnSchemaTypeVisitor<?> visitor) throws ParseException
    {
        return visitor.visit(this);
    }
}
