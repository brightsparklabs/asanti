/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import static com.google.common.base.Preconditions.*;

import com.google.common.collect.ImmutableSet;

/**
 * Base type for modeling SET OF / SEQUENCE OF type definition from a within a
 * module specification within an ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionCollectionOf extends AsnSchemaTypeDefinition
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /**
     * built-in types which are considered 'constructed'. Currently: SET OF and
     * SEQUENCE OF
     */
    public static final ImmutableSet<AsnBuiltinType> validTypes = ImmutableSet.of(AsnBuiltinType.SetOf,
            AsnBuiltinType.SequenceOf);

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** name of the type for the elements in this collection */
    private final String elementTypeName;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name
     *            name of the SET OF / SEQUENCE OF type definition
     *
     * @param builtinType
     *            the underlying ASN.1 type of the defined type. Must be either
     *            {@value AsnBuiltinType#SetOf} or
     *            {@value AsnBuiltinType#SequenceOf}.
     *
     * @param elementTypeName
     *            the name of the type for the elements in the SET OF / SEQUENCE
     *            OF. E.g. for
     *            {@code SEQUENCE (SIZE (1..100)) OF OCTET STRING (SIZE (256))},
     *            this would be {@code OCTET STRING}
     *
     * @param constraint
     *            the constraints on the element type. Use
     *            {@link AsnSchemaConstraint#NULL} if no constraint.
     *            <p>
     *            E.g for
     *            {@code SEQUENCE (SIZE (1..100)) OF OCTET STRING (SIZE (256))}
     *            this would be {@code SIZE (256)}
     *
     * @throws NullPointerException
     *             if {@code name}, {@code elementTypeName} or
     *             {@code componentTypes} are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} or {@code elementTypeName} is blank or
     *             {@code builtinType} is not one of the valid types defined in
     *             {@link #validTypes}
     */
    public AsnSchemaTypeDefinitionCollectionOf(String name, AsnBuiltinType builtinType, String elementTypeName,
            AsnSchemaConstraint constraint)
    {
        super(name, builtinType, constraint);
        checkArgument(validTypes.contains(builtinType), "Type must be either SET OF or SEQUENCE OF");
        checkNotNull(elementTypeName);
        checkArgument(!elementTypeName.trim()
                .isEmpty(), "Element type name must be specified");
        this.elementTypeName = elementTypeName;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaTypeDefinition
    // -------------------------------------------------------------------------

    @Override
    public String getTypeName(String tag)
    {
        return elementTypeName;
    }
}
