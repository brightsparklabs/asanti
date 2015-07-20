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
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.text.ParseException;

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
    private final Iterable<AsnSchemaComponentType> componentTypes;

    /** the mechanism to be used for creation of Tags, during schema creation */
    private final TagCreator tagCreator;

    /** mapping from raw tag to component type */
    //private ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes;
    private ImmutableList<AsnSchemaComponentType> components;

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
            Iterable<AsnSchemaComponentType> componentTypes, AsnModuleTaggingMode taggingMode)
    {
        super(primitiveType, constraint);

        checkArgument(validTypes.contains(primitiveType),
                "Type must be either SET, SEQUENCE or CHOICE");

        checkNotNull(componentTypes);
        checkNotNull(taggingMode);
        this.componentTypes = componentTypes;

        this.tagCreator = TagCreator.create(primitiveType, taggingMode);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * This function allows the 'tree' to be walked, by being able to get to child types.
     *
     * @return the Map of components - names to types.
     */
    public Iterable<AsnSchemaComponentType> getAllComponents()
    {
        return componentTypes;
    }

    /**
     * Will only calculate the tags the first time it is called.
     *
     * @return a mapping of raw tags (as received from BER files) to AsnSchemaComponentType object
     * that hold both a fully qualified decoded tag and the AsnSchemaType
     *
     * @throws ParseException
     *         if there are duplicate tags
     */
   // public ImmutableMap<String, AsnSchemaComponentType> getTagsToComponentTypes()
    public ImmutableList<AsnSchemaComponentType> getTagsToComponentTypes()
            throws ParseException
    {

        if (components == null)
        {
            components = tagCreator.getTagsForComponents(componentTypes);
        }

        return components;

//        if (tagsToComponentTypes == null)
//        {
//            tagsToComponentTypes = tagCreator.getTagsForComponents(componentTypes);
//        }
//
//        return tagsToComponentTypes;
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
        components = getTagsToComponentTypes();
        //tagsToComponentTypes = getTagsToComponentTypes();
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BaseAsnSchemaType
    // -------------------------------------------------------------------------

    @Override
    public Optional<AsnSchemaComponentType> getMatchingChild(String rawTag,
            DecodingSession decodingSession)
    {
        // Protect against the fact the tagsToComponentTypes may be null (because
        // getTagsToComponentTypes has not yet been called)
        // TODO ASN-115 - I don't love that this is constructed in a state that we can't really
        // use it, but we can't tag until we know all the types, which is well after creation
        // I would like to rethink the whole construction chain, but that is a bigger job that I'd
        // like to do in this task, so the question is how to handle this here
        // This is not user facing, and the only time it should be null is if getTagsToComponentTypes
        // was not called (programmer error), so I'm almost happy to let it throw a NullPointerException
        //if (tagsToComponentTypes == null)
        if (components == null)
        {
            return Optional.absent();
        }

        AsnSchemaTag tag = AsnSchemaTag.create(rawTag);
        return tagCreator.getComponentTypes(tag, components, decodingSession);
    }

    @Override
    public Object accept(final AsnSchemaTypeVisitor<?> visitor) throws ParseException
    {
        return visitor.visit(this);
    }
}
