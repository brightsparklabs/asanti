package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.google.common.collect.ImmutableSet;

import java.util.regex.Matcher;

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

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BaseAsnSchemaType
    // -------------------------------------------------------------------------

    @Override
    public AsnPrimitiveType getPrimitiveType()
    {
        return elementType.getPrimitiveType();
    }

    @Override
    public AsnSchemaType getChildType(String tag)
    {
        Matcher matcher = PATTERN_UNIVERSAL_TYPE_TAG.matcher(tag);
        if (matcher.matches())
        {
            AsnBuiltinType typeToMatch = AsnBuiltinType.valueOf(matcher.group(1));

            //if (typeToMatch == ((BaseAsnSchemaType)elementType).primitiveType.getBuiltinType())
            AsnBuiltinType cT = elementType.getBuiltinTypeAA();
            if (AsnSchemaTypeConstructed.match(typeToMatch, cT))
            {
                return elementType;
            }
        }
        return elementType.getChildType(tag);
    }

    @Override
    public String getChildName(String tag)
    {
        Matcher matcher = PATTERN_UNIVERSAL_TYPE_TAG.matcher(tag);
        if (matcher.matches())
        {
            AsnBuiltinType typeToMatch = AsnBuiltinType.valueOf(matcher.group(1));

            //if (typeToMatch == ((BaseAsnSchemaType)elementType).primitiveType.getBuiltinType())
            AsnBuiltinType cT = elementType.getBuiltinTypeAA();
            if (AsnSchemaTypeConstructed.match(typeToMatch, cT))
            {
                return "[99999]";
            }
        }

        return elementType.getChildName(tag);
    }
}
