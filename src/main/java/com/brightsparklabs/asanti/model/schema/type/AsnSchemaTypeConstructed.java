/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.type;

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
     * @param tagCreator
     *         the TagCreator helper to use to create and match tags.
     *
     * @throws NullPointerException
     *         if {@code componentTypes} or {@code tagCreator} are {@code null}
     * @throws IllegalArgumentException
     *         if {@code primitiveType} is not a constructed type (Currently: SEQUENCE, SET and
     *         CHOICE)
     */
    public AsnSchemaTypeConstructed(AsnPrimitiveType primitiveType, AsnSchemaConstraint constraint,
            List<AsnSchemaComponentType> componentTypes, TagCreator tagCreator)
    {
        super(primitiveType, constraint);

        checkArgument(validTypes.contains(primitiveType),
                "Type must be either SET, SEQUENCE or CHOICE");

        checkNotNull(componentTypes);
        checkNotNull(tagCreator);
        this.componentTypes = ImmutableList.copyOf(componentTypes);
        this.tagCreator = tagCreator;
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Determines if there are any duplicate tags (meaning that decoding would be ambiguous) and
     * throws if there are.  This function requires that all the components have their final tags,
     * i.e. that {@link this.performTagging()} has been called
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
        return tagCreator.getComponentTypes(tag, componentTypes, decodingSession);
    }

    @Override
    public Object accept(final AsnSchemaTypeVisitor<?> visitor) throws ParseException
    {
        return visitor.visit(this);
    }
}
