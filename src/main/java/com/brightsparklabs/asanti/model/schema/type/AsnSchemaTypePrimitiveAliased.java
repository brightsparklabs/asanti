/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.type;

import static java.util.Objects.*;

import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.schema.AsnPrimitiveType;
import com.google.common.collect.ImmutableList;
import java.text.ParseException;
import java.util.Optional;

/**
 * @author brightSPARK Labs
 */
public class AsnSchemaTypePrimitiveAliased extends AbstractAsnSchemaType {

    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** The type that this type is aliasing. */
    private AsnSchemaType aliasedType;

    /** The name of the module that the alias type belongs in (can be empty). */
    private final String module;

    /** The name of the aliased type. */
    private final String type;

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
     * @param module The name of the module that the alias type belongs in (can be empty)
     * @param type The name of the aliased type
     * @throws NullPointerException if {@code primitiveType} is {@code null}
     */
    public AsnSchemaTypePrimitiveAliased(
            final AsnPrimitiveType primitiveType,
            final AsnSchemaConstraint constraint,
            final String module,
            final String type) {
        super(primitiveType, constraint);
        this.module = requireNonNull(module);
        this.type = requireNonNull(type);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Sets the type that this type is aliasing.
     *
     * @param aliasedType The type that this type is aliasing.
     */
    public void setAliasedType(final AsnSchemaType aliasedType) {
        this.aliasedType = aliasedType;
    }

    /**
     * Returns the type that this type is aliasing.
     *
     * @return The type that this type is aliasing.
     */
    public AsnSchemaType getAliasedType() {
        return this.aliasedType;
    }

    /**
     * Returns the name of the module that the alias type belongs in (can be empty).
     *
     * @return The name of the module that the alias type belongs in (can be empty).
     */
    public String getModule() {
        return module;
    }

    /**
     * Returns the name of the aliased type.
     *
     * @return The name of the aliased type.
     */
    public String getType() {
        return type;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BaseAsnSchemaType
    // -------------------------------------------------------------------------

    @Override
    public ImmutableList<AsnSchemaComponentType> getAllComponents() {
        return aliasedType == null ? ImmutableList.of() : aliasedType.getAllComponents();
    }

    @Override
    public Optional<AsnSchemaComponentType> getMatchingChild(
            final String tag, final DecodingSession decodingSession) {
        return aliasedType == null
                ? Optional.empty()
                : aliasedType.getMatchingChild(tag, decodingSession);
    }

    @Override
    public Object accept(final AsnSchemaTypeVisitor<?> visitor) throws ParseException {
        return visitor.visit(this);
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------
}
