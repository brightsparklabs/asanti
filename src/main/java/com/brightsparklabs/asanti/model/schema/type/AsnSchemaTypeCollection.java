package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.*;

/**
 * A type used to model the types for objects within ASN.1 schema that are Collections, meaning that
 * they are the equivalent of a List of the element type the surround.
 * These objects can be either Type Definitions, eg Type ::= SomeType,
 * or components within a constructed type (SEQUENCE etc), eg component SomeType
 *
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeCollection extends AbstractAsnSchemaType
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /**
     * built-in types which are considered 'collection'. Currently: SET OF and
     * SEQUENCE OF
     */
    public static final ImmutableSet<AsnPrimitiveType> validTypes = ImmutableSet.of(AsnPrimitiveType.SET_OF,
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
     *            the underlying primitiveType of the defined primitiveType
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
     * @param elementType
     *            the name of the type for the elements in the SET OF / SEQUENCE
     *            OF. E.g. for
     *            {@code SEQUENCE (SIZE (1..100)) OF OCTET STRING (SIZE (256))},
     *            this would be {@code OCTET STRING}
     *
     * @throws NullPointerException
     *             if {@code primitiveType} are {@code null}
     *
     */
    public AsnSchemaTypeCollection(AsnPrimitiveType primitiveType, AsnSchemaConstraint constraint, AsnSchemaType elementType)
    {
        super(primitiveType, constraint);

        checkArgument(validTypes.contains(primitiveType), "Type must be either SET OF or SEQUENCE OF");

        checkNotNull(elementType);
        this.elementType = elementType;

    }


    // -------------------------------------------------------------------------
    // IMPLEMENTATION
    // -------------------------------------------------------------------------

    @Override
    public AsnSchemaType getChildType(String tag)
    {
        return elementType.getChildType(tag);
    }

    @Override
    public String getChildName(String tag)
    {
        return elementType.getChildName(tag); // TODO MJF what to return???
    }

    /**
     *
     * @return the type for this Collection, eg if the definition was
     * SEQUENCE OF Foo
     * then the type for Foo will be returned
     */
    public AsnSchemaType getElementType()
    {
        return elementType;
    }
}
