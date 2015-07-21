/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.tag.AsnSchemaTag;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;

import java.text.ParseException;

import static com.google.common.base.Preconditions.*;

/**
 * A type used to model the types for objects within ASN.1 schema that are Collections, meaning that
 * they are the equivalent of a List of the element type they surround. These objects can be either
 * Type Definitions, e.g. Type ::= SomeType, or components within a constructed type (SEQUENCE etc),
 * e.g. component SomeType
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeCollection extends BaseAsnSchemaType
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /**
     * built-in types which are considered 'collection'. Currently: SET OF and SEQUENCE OF
     */
    private static final ImmutableSet<AsnPrimitiveType> validTypes = ImmutableSet.of(
            AsnPrimitiveType.SET_OF,
            AsnPrimitiveType.SEQUENCE_OF);

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** name of the type for the elements in this collection */
    private final AsnSchemaType elementType;

    /** the Universal tag we should expect */
    private String myUniversalTag = "";

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
     *         <p> Example 1<br> For {@code SET (SIZE (1..100) OF OCTET STRING (SIZE (10))} this
     *         would be {@code (SIZE (10)}. <p> Example 2<br> For {@code INTEGER (1..256)} this
     *         would be {@code (1..256)}.
     * @param elementType
     *         the name of the type for the elements in the SET OF / SEQUENCE OF. E.g. for {@code
     *         SEQUENCE (SIZE (1..100)) OF OCTET STRING (SIZE (256))}, this would be {@code OCTET
     *         STRING}
     *
     * @throws NullPointerException
     *         if {@code primitiveType} is {@code null}
     * @throws IllegalArgumentException
     *         if {@code primitiveType} is not a collection type (Currently: SET OF and SEQUENCE
     *         OF)
     */
    public AsnSchemaTypeCollection(AsnPrimitiveType primitiveType, AsnSchemaConstraint constraint,
            AsnSchemaType elementType)
    {
        super(primitiveType, constraint);

        checkArgument(validTypes.contains(primitiveType),
                "Type must be either SET OF or SEQUENCE OF");

        checkNotNull(elementType);
        this.elementType = elementType;
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the type that this Collection surrounds
     *
     * @return the underlying type for this Collection, e.g. if the definition was SEQUENCE OF Foo
     * then the type for Foo will be returned
     */
    public AsnSchemaType getElementType()
    {
        return elementType;
    }

    /**
     * When decoding we are expecting only certain values.  Some of those values are the Universal
     * tag for the element type.  We may not know the element type's actual type at construction so
     * as a post parsing step this function will be called to allow us to calculate the tags we
     * expect to receive.
     */
    public void performTagging()
    {
        myUniversalTag = AsnSchemaTag.createUniversalPortion(elementType.getBuiltinType());
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BaseAsnSchemaType
    // -------------------------------------------------------------------------

    @Override
    public Optional<AsnSchemaComponentType> getMatchingChild(String rawTag,
            DecodingSession decodingSession)
    {
        final AsnSchemaTag tag = AsnSchemaTag.create(rawTag);

        if (elementType.getBuiltinType() == AsnBuiltinType.Choice)
        {
            // We have a collection of Choice, so we need to insert the choice option
            // We could pre-calculate the options here like we do with Constructed, but given
            // that we have to do work here to sort out the index there is little to be saved
            final Optional<AsnSchemaComponentType> child = elementType.getMatchingChild(rawTag,
                    decodingSession);

            if (child.isPresent())
            {
                final String newTag = "[" + tag.getTagIndex() + "]/" + child.get().getName();
                // TODO ASN-115 (review design)) - I don't like this, as it is a dependency issue.  Makes it
                // hard to test etc.  Ditto a couple of lines lower.
                // as a reference there used to be a getChildName and getChildType
                // I also played with the concept of an Interface AsnSchemaNamedType with a getName
                // and getType methods, with AsnSchemaComponentType implementing it an also making a
                // new light class AsnSchemaNamedTypeImpl to implement just that which we could
                // return here (except that while ever we are calling getMatchingChild recursively
                // (we are in the Constructed side of matching, we need to use AsnSchemaComponentType)
                return Optional.of(new AsnSchemaComponentType(newTag,
                        rawTag,
                        false,
                        child.get().getType()));
            }
        }

        if (tag.getTagPortion().equals(myUniversalTag))
        {
            return Optional.of(new AsnSchemaComponentType("[" + tag.getTagIndex() + "]",
                    rawTag,
                    false,
                    elementType));
        }

        return Optional.absent();
    }

    @Override
    public Object accept(final AsnSchemaTypeVisitor<?> visitor) throws ParseException
    {
        return visitor.visit(this);
    }
}
