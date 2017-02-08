/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.AsnSchemaModule;
import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypes;
import com.brightsparklabs.assam.schema.AsnPrimitiveType;
import java.util.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.text.ParseException;

import static com.google.common.base.Preconditions.*;

/**
 * A place holder type definition. This is used while parsing the schema file and coming across a
 * Component Type that is using a non-Primitive type (i.e. a Type Definition).  It stores the
 * constraints and the name of the type so that we can do a lookup later for the actual type
 * definition.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypePlaceholder extends BaseAsnSchemaType
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the name of a module the type is defined in */
    private final String moduleName;

    /** the name of the type we are placeholder for */
    private final String typeName;

    /** The actual type.  This is set once the parser has parsed all modules. */
    private AsnSchemaType indirectType;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param moduleName
     *         The name of the {@link AsnSchemaModule} that contains the real type. May be {@code
     *         null} or empty if not needed.
     * @param typeName
     *         The name of the Type Definition that this placeholder is for
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
     *
     * @throws NullPointerException
     *         if {@code typeName} is {@code null}
     * @throws IllegalArgumentException
     *         if {@code typeName} is blank
     */
    public AsnSchemaTypePlaceholder(String moduleName, String typeName,
            AsnSchemaConstraint constraint)
    {
        super(AsnPrimitiveTypes.INVALID, constraint);

        checkNotNull(typeName);
        checkArgument(!typeName.trim().isEmpty(), "Type name must be specified");

        this.typeName = typeName;
        this.moduleName = moduleName;
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the name of the Module that this placeholder should address.
     *
     * @return the name of the Module that this placeholder should address.
     */
    public String getModuleName()
    {
        return moduleName;
    }

    /**
     * Returns the name of the Type that this placeholder should address.
     *
     * @return the name of the Type that this placeholder should address.
     */
    public String getTypeName()
    {
        return typeName;
    }

    /**
     * Changes the placeholder to an object that stores a link to the type. This is essentially an
     * layer of indirection, and allows chaining of Type Definitions
     *
     * <p>E.g.
     * <pre>
     * someComponent [1] PersonAge ...
     * PersonAge ::= ShortInteger (0..200) ShortInteger ::= INTEGER (0..32768)
     * </pre>
     *
     * @param type
     *         the {@link AsnSchemaType}
     */
    public void setIndirectType(AsnSchemaType type)
    {
        indirectType = type;
    }

    /**
     * @return the AsnSchemaType that this placeholder is pointing to, {@link AsnSchemaType#NULL} if
     * none has yet been set.
     */
    public AsnSchemaType getIndirectType()
    {
        return indirectType == null ? AsnSchemaType.NULL : indirectType;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BaseAsnSchemaType
    // -------------------------------------------------------------------------

    @Override
    public ImmutableList<AsnSchemaComponentType> getAllComponents()
    {
        return indirectType == null ?
                ImmutableList.<AsnSchemaComponentType>of() :
                indirectType.getAllComponents();
    }

    @Override
    public ImmutableSet<AsnSchemaConstraint> getConstraints()
    {
        if (indirectType != null)
        {
            // return our own constraints, and those of the type we are delegating to.
            // this allows constraint 'chaining'
            return new ImmutableSet.Builder<AsnSchemaConstraint>().addAll(super.getConstraints())
                    .addAll(indirectType.getConstraints())
                    .build();
        }

        return super.getConstraints();
    }

    @Override
    public Optional<AsnSchemaComponentType> getMatchingChild(String tag,
            DecodingSession decodingSession)
    {
        return indirectType == null ?
                Optional.<AsnSchemaComponentType>empty() :
                indirectType.getMatchingChild(tag, decodingSession);
    }

    @Override
    public AsnPrimitiveType getPrimitiveType()
    {
        return indirectType == null ? AsnPrimitiveTypes.INVALID : indirectType.getPrimitiveType();
    }

    @Override
    public Object accept(final AsnSchemaTypeVisitor<?> visitor) throws ParseException
    {
        return visitor.visit(this);
    }
}
