/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.schema.AsnPrimitiveType;
import com.google.common.collect.ImmutableList;
import java.text.ParseException;
import java.util.Optional;

/** @author brightSPARK Labs */
public class AsnSchemaTypePrimitive extends AbstractAsnSchemaType {

    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param primitiveType the underlying primitiveType of the defined primitiveType
     * @param constraint The constraint on the type. Use {@link AsnSchemaConstraint#NULL} if no
     *     constraint.
     *     <p>Example 1 <br>
     *     For {@code SET (SIZE (1..100) OF OCTET STRING (SIZE (10))} this would be {@code (SIZE
     *     (10)}.
     *     <p>Example 2 <br>
     *     For {@code INTEGER (1..256)} this would be {@code (1..256)}.
     * @throws NullPointerException if {@code primitiveType} is {@code null}
     */
    public AsnSchemaTypePrimitive(
            final AsnPrimitiveType primitiveType, final AsnSchemaConstraint constraint) {
        super(primitiveType, constraint);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BaseAsnSchemaType
    // -------------------------------------------------------------------------

    @Override
    public ImmutableList<AsnSchemaComponentType> getAllComponents() {
        return ImmutableList.of();
    }

    @Override
    public Optional<AsnSchemaComponentType> getMatchingChild(
            final String tag, final DecodingSession decodingSession) {
        return Optional.empty();
    }

    @Override
    public Object accept(final AsnSchemaTypeVisitor<?> visitor) throws ParseException {
        return visitor.visit(this);
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------
}
