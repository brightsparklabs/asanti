package com.brightsparklabs.asanti.mocks.model.schema;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.type.*;
import com.brightsparklabs.asanti.model.schema.typedefinition.AbstractOLDAsnSchemaTypeDefinition;
import com.google.common.collect.ImmutableSet;

import static org.mockito.Mockito.*;

/**
 * Utility class for obtaining mocked instances of {@link AsnSchemaType} which
 * conform to the test ASN.1 schema defined in the {@code README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockAsnSchemaType
{
    /**
     * Default constructor. This is hidden, use one of the factory methods instead.
     */
    private MockAsnSchemaType()
    {
        // private constructor
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------



    public BaseAsnSchemaType createMockedInstance(AsnPrimitiveType primitiveType, AsnSchemaConstraint constraint)
    {
        final BaseAsnSchemaType mockedInstance = mock(BaseAsnSchemaType.class);

        final ImmutableSet<AsnSchemaConstraint> constraints = ImmutableSet.of((constraint == null) ? AsnSchemaConstraint.NULL :
                constraint);

        when(mockedInstance.getPrimitiveType()).thenReturn(primitiveType);
        when(mockedInstance.getConstraints()).thenReturn(constraints);
        when(mockedInstance.getBuiltinType()).thenReturn(primitiveType.getBuiltinType());
        when(mockedInstance.getChildType(anyString())).thenReturn(AsnSchemaType.NULL);
        when(mockedInstance.getChildName(anyString())).thenReturn("");

        return mockedInstance;
    }

    public AsnSchemaTypeCollection createMockedInstanceCollection(AsnPrimitiveType primitiveType, AsnSchemaConstraint constraint, AsnSchemaType elementType)
    {
        final AsnSchemaTypeCollection mockedInstance = mock(AsnSchemaTypeCollection.class);

        final ImmutableSet<AsnSchemaConstraint> constraints = ImmutableSet.of((constraint == null) ? AsnSchemaConstraint.NULL :
                constraint);

        when(mockedInstance.getPrimitiveType()).thenReturn(primitiveType);
        when(mockedInstance.getConstraints()).thenReturn(constraints);
        when(mockedInstance.getBuiltinType()).thenReturn(primitiveType.getBuiltinType());
        when(mockedInstance.getChildType(anyString())).thenReturn(AsnSchemaType.NULL);
        when(mockedInstance.getChildName(anyString())).thenReturn("");

        when(mockedInstance.getElementType()).thenReturn(elementType);

        return mockedInstance;
    }

    public AsnSchemaTypePlaceholder createMockedInstancePlaceholder(String moduleName, String typeName, AsnSchemaConstraint constraint, AsnSchemaType indirectType)
    {
        AsnSchemaTypePlaceholder mockedInstance = mock(AsnSchemaTypePlaceholder.class);

        if (indirectType == null) indirectType = AsnSchemaType.NULL;
        if (constraint == null) constraint = AsnSchemaConstraint.NULL;

        final ImmutableSet<AsnSchemaConstraint> constraints = new ImmutableSet.Builder<AsnSchemaConstraint>()
                .add(constraint)
                .addAll(indirectType.getConstraints())
                .build();


        when(mockedInstance.getPrimitiveType()).thenReturn(indirectType.getPrimitiveType());
        when(mockedInstance.getConstraints()).thenReturn(constraints);
        when(mockedInstance.getBuiltinType()).thenReturn(indirectType.getBuiltinType());
        when(mockedInstance.getChildType(anyString())).thenReturn(indirectType.getChildType(anyString()));
        when(mockedInstance.getChildName(anyString())).thenReturn(indirectType.getChildName(anyString()));

        return mockedInstance;
    }


    //public AsnSchemaTypeConstructed createMockedInstanceConstructed(AsnPrimitiveType primitiveType, AsnSchemaConstraint constraint, I

    /**
     * Builder for creating mocked instances of {@link AbstractOLDAsnSchemaTypeDefinition}
     *
     * @author brightSPARK Labs
     */
    public static class MockAsnSchemaTypeConstructedBuilder
    {
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        final AsnSchemaTypeConstructed mockedInstance = mock(AsnSchemaTypeConstructed.class);

        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------
        MockAsnSchemaTypeConstructedBuilder(AsnPrimitiveType primitiveType, AsnSchemaConstraint constraint)
        {
            final ImmutableSet<AsnSchemaConstraint> constraints = ImmutableSet.of((constraint == null) ? AsnSchemaConstraint.NULL :
                    constraint);

            when(mockedInstance.getPrimitiveType()).thenReturn(primitiveType);
            when(mockedInstance.getConstraints()).thenReturn(constraints);
            when(mockedInstance.getBuiltinType()).thenReturn(primitiveType.getBuiltinType());

        }

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

        /**
         * Adds a component to the mocked instance. A component is simply a named type.
         * @param tag
         * @param type
         * @return
         */
        MockAsnSchemaTypeConstructedBuilder addComponent(String tag, AsnSchemaType type)
        {
            when(mockedInstance.getChildType(tag)).thenReturn(type);
            when(mockedInstance.getChildName(tag)).thenReturn(tag);
            return this;
        }

        /**
         * Creates a mocked instance from the data in this builder
         *
         * @return a mocked instance of {@link AsnSchemaTypeConstructed}
         */
        public AsnSchemaTypeConstructed build()
        {
            return mockedInstance;
        }
    }
}
