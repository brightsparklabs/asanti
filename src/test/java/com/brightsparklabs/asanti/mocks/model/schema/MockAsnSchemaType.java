/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.mocks.model.schema;

import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.model.schema.DecodingSession;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypes;
import com.brightsparklabs.asanti.model.schema.type.*;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.brightsparklabs.asanti.schema.AsnPrimitiveType;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * Utility class for obtaining mocked instances of {@link AsnSchemaType} which conform to the test
 * ASN.1 schema defined in the {@code README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockAsnSchemaType {
    /** Default constructor. This is hidden, use one of the factory methods instead. */
    private MockAsnSchemaType() {
        // private constructor
    }

    public static AsnSchemaTypeWithNamedTags createMockedInstanceWithNamedValues(
            AsnPrimitiveType primitiveType,
            AsnSchemaConstraint constraint,
            Iterable<AsnSchemaNamedTag> namedValues) {
        final AsnSchemaTypeWithNamedTags mockedInstance = mock(AsnSchemaTypeWithNamedTags.class);

        final ImmutableSet<AsnSchemaConstraint> constraints =
                ImmutableSet.of((constraint == null) ? AsnSchemaConstraint.NULL : constraint);

        when(mockedInstance.getPrimitiveType()).thenReturn(primitiveType);
        when(mockedInstance.getConstraints()).thenReturn(constraints);
        when(mockedInstance.getBuiltinType()).thenReturn(primitiveType.getBuiltinType());
        when(mockedInstance.getMatchingChild(anyString(), any(DecodingSession.class)))
                .thenReturn(Optional.empty());

        return mockedInstance;
    }

    public static AsnSchemaTypePlaceholder createMockedInstancePlaceholder(
            String moduleName,
            String typeName,
            AsnSchemaConstraint constraint,
            AsnSchemaType indirectType) {
        AsnSchemaTypePlaceholder mockedInstance = mock(AsnSchemaTypePlaceholder.class);

        if (indirectType == null) indirectType = AsnSchemaType.NULL;
        if (constraint == null) constraint = AsnSchemaConstraint.NULL;

        final ImmutableSet<AsnSchemaConstraint> constraints =
                new ImmutableSet.Builder<AsnSchemaConstraint>()
                        .add(constraint)
                        .addAll(indirectType.getConstraints())
                        .build();

        when(mockedInstance.getPrimitiveType()).thenReturn(indirectType.getPrimitiveType());
        when(mockedInstance.getConstraints()).thenReturn(constraints);
        when(mockedInstance.getBuiltinType()).thenReturn(indirectType.getBuiltinType());
        // when(mockedInstance.getMatchingChild(anyString(),
        // any(DecodingSession.class))).thenReturn(indirectType.getMatchingChild());

        when(mockedInstance.getModuleName()).thenReturn(moduleName);
        when(mockedInstance.getTypeName()).thenReturn(typeName);

        return mockedInstance;
    }

    /**
     * Creates mock {@link AsnSchemaType} instances conforming to the 'People' Type Definition in
     * the 'People-Protocol' module in the test ASN.1 schema defined in the {@code README.md} file
     *
     * @return mock instance
     */
    public static AsnSchemaType getPeopleProtocolPeople() {
        return builder(AsnPrimitiveTypes.SET_OF)
                .setCollectionType(getPeopleProtocolPerson())
                .build();
    }

    /**
     * Creates mock {@link AsnSchemaType} instances conforming to the 'Person' Type Definition in
     * the 'People-Protocol' module in the test ASN.1 schema defined in the {@code README.md} file
     *
     * @return mock instance
     */
    public static AsnSchemaType getPeopleProtocolPerson() {
        AsnSchemaType gender =
                builder(AsnPrimitiveTypes.ENUMERATED)
                        // TODO ASN-138 - add options.
                        .build();

        return builder(AsnPrimitiveTypes.SEQUENCE)
                .addComponent(
                        "1", "firstName", createMockedAsnSchemaType(AsnPrimitiveTypes.OCTET_STRING))
                .addComponent(
                        "2", "lastName", createMockedAsnSchemaType(AsnPrimitiveTypes.OCTET_STRING))
                .addComponent("3", "title", createMockedAsnSchemaType(AsnPrimitiveTypes.ENUMERATED))
                .addComponent("4", "gender", gender)
                .addComponent(
                        "5",
                        "maritalStatus",
                        createMockedAsnSchemaType(
                                AsnPrimitiveTypes.CHOICE)) // TODO ASN-138 - choice
                .build();
    }

    /**
     * Creates mock {@link AsnSchemaType} instances conforming to the version component of the
     * Document Type Definition in the 'Document-PDU' module in the test ASN.1 schema defined in the
     * {@code README.md} file
     *
     * @return mock instance
     */
    public static AsnSchemaType getDocumentVersion() {
        return builder(AsnPrimitiveTypes.SEQUENCE)
                .addComponent(
                        "0", "majorVersion", createMockedAsnSchemaType(AsnPrimitiveTypes.INTEGER))
                .addComponent(
                        "1", "minorVersion", createMockedAsnSchemaType(AsnPrimitiveTypes.INTEGER))
                .build();
    }

    /**
     * Creates mock {@link AsnSchemaType} instances conforming to the description component of the
     * Document Type Definition in the 'Document-PDU' module in the test ASN.1 schema defined in the
     * {@code README.md} file
     *
     * @return mock instance
     */
    public static AsnSchemaType getDocumentDescription() {
        return builder(AsnPrimitiveTypes.SET)
                .addComponent(
                        "0", "numberLines", createMockedAsnSchemaType(AsnPrimitiveTypes.INTEGER))
                .addComponent(
                        "1",
                        "summary",
                        true,
                        createMockedAsnSchemaType(AsnPrimitiveTypes.OCTET_STRING))
                .build();
    }

    // -------------------------------------------------------------------------
    // The below are helpers to mock the example Asanti schema
    // -------------------------------------------------------------------------

    /**
     * Creates mock {@link AsnSchemaType} instances conforming to the 'Due-Date' Type Definition in
     * the 'Document-PDU' module in the test ASN.1 schema defined in the {@code README.md} file
     *
     * @return mock instance
     */
    public static AsnSchemaType getDocumentDueDate() {
        // TODO ASN-138 - add the distinguished names.
        return createMockedAsnSchemaType(AsnPrimitiveTypes.INTEGER);
    }

    /**
     * Creates mock {@link AsnSchemaType} instances conforming to the 'References' Type Definition
     * in the 'Document-PDU' module in the test ASN.1 schema defined in the {@code README.md} file
     *
     * @return mock instance
     */
    public static AsnSchemaType getDocumentReferences() {
        return builder(AsnPrimitiveTypes.SEQUENCE_OF)
                .setCollectionType(
                        builder(AsnPrimitiveTypes.SEQUENCE)
                                .addComponent(
                                        "1",
                                        "title",
                                        createMockedAsnSchemaType(AsnPrimitiveTypes.OCTET_STRING))
                                .addComponent(
                                        "2",
                                        "url",
                                        createMockedAsnSchemaType(AsnPrimitiveTypes.OCTET_STRING))
                                .build())
                .build();
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------
    public static BaseAsnSchemaType createMockedAsnSchemaType(AsnPrimitiveType primitiveType) {
        return createMockedAsnSchemaType(primitiveType, AsnSchemaConstraint.NULL);
    }

    public static BaseAsnSchemaType createMockedAsnSchemaType(
            AsnPrimitiveType primitiveType, AsnSchemaConstraint constraint) {
        final BaseAsnSchemaType mockedInstance = mock(BaseAsnSchemaType.class);

        final ImmutableSet<AsnSchemaConstraint> constraints =
                ImmutableSet.of((constraint == null) ? AsnSchemaConstraint.NULL : constraint);

        final AsnSchemaComponentType componentType = mock(AsnSchemaComponentType.class);
        when(componentType.getType()).thenReturn(AsnSchemaType.NULL);
        when(componentType.getName()).thenReturn("");

        when(mockedInstance.getPrimitiveType()).thenReturn(primitiveType);
        when(mockedInstance.getConstraints()).thenReturn(constraints);
        when(mockedInstance.getBuiltinType()).thenReturn(primitiveType.getBuiltinType());
        when(mockedInstance.getMatchingChild(anyString(), any(DecodingSession.class)))
                .thenReturn(Optional.empty());

        return mockedInstance;
    }

    /**
     * Returns a builder for constructing mock instances of {@link AsnSchemaType} This is an
     * overload that defaults to constraint of {@code AsnSchemaConstraint.NULL}
     *
     * @param primitiveType value to return for {@link AsnSchemaType#getPrimitiveType()} method
     * @return mock builder
     */
    public static MockAsnSchemaTypeBuilder builder(AsnPrimitiveType primitiveType) {
        return builder(primitiveType, AsnSchemaConstraint.NULL);
    }

    /**
     * Returns a builder for constructing mock instances of {@link AsnSchemaType} This is an
     * overload that defaults to constraint of {@code AsnSchemaConstraint.NULL}
     *
     * @param primitiveType value to return for {@link AsnSchemaType#getPrimitiveType()} method
     * @param constraint value to return for {@link AsnSchemaType#getConstraints()} method
     * @return mock builder
     */
    public static MockAsnSchemaTypeBuilder builder(
            AsnPrimitiveType primitiveType, AsnSchemaConstraint constraint) {
        return new MockAsnSchemaTypeBuilder(primitiveType, constraint);
    }

    /**
     * Creates mock {@link AsnSchemaType} instances conforming to the 'Paragraph' Type Definition in
     * the 'Document-PDU' module in the test ASN.1 schema defined in the {@code README.md} file
     *
     * @return mock instance
     */
    public static AsnSchemaType getDocumentParagraph() {
        return builder(AsnPrimitiveTypes.SEQUENCE_OF)
                .setCollectionType(
                        builder(AsnPrimitiveTypes.SEQUENCE)
                                .addComponent(
                                        "1",
                                        "title",
                                        createMockedAsnSchemaType(AsnPrimitiveTypes.OCTET_STRING))
                                .addComponent("2", "contributor", true, getPeopleProtocolPerson())
                                .addComponent(
                                        "3",
                                        "points",
                                        builder(AsnPrimitiveTypes.SEQUENCE_OF)
                                                .setCollectionType(
                                                        createMockedAsnSchemaType(
                                                                AsnPrimitiveTypes.OCTET_STRING))
                                                .build())
                                .build())
                .build();
    }

    /**
     * Creates mock {@link AsnSchemaType} instances conforming to the 'Section-Note' Type Definition
     * in the 'Document-PDU' module in the test ASN.1 schema defined in the {@code README.md} file
     *
     * @return mock instance
     */
    public static AsnSchemaType getDocumentSectionNote() {
        return builder(AsnPrimitiveTypes.SEQUENCE)
                .addComponent(
                        "1", "text", createMockedAsnSchemaType(AsnPrimitiveTypes.OCTET_STRING))
                .build();
    }

    /**
     * Creates mock {@link AsnSchemaType} instances conforming to the 'Section-Main' Type Definition
     * in the 'Document-PDU' module in the test ASN.1 schema defined in the {@code README.md} file
     *
     * @return mock instance
     */
    public static AsnSchemaType getDocumentSectionMain() {
        return builder(AsnPrimitiveTypes.SEQUENCE)
                .addComponent(
                        "1",
                        "text",
                        true,
                        createMockedAsnSchemaType(AsnPrimitiveTypes.OCTET_STRING))
                .addComponent("2", "paragraphs", getDocumentParagraph())
                .addComponent(
                        "3",
                        "sections",
                        builder(AsnPrimitiveTypes.SET_OF)
                                .setCollectionType(
                                        builder(AsnPrimitiveTypes.SET)
                                                .addComponent(
                                                        "1",
                                                        "number",
                                                        createMockedAsnSchemaType(
                                                                AsnPrimitiveTypes.INTEGER))
                                                .addComponent(
                                                        "2",
                                                        "text",
                                                        createMockedAsnSchemaType(
                                                                AsnPrimitiveTypes.OCTET_STRING))
                                                .build())
                                .build())
                .build();
    }

    /**
     * Creates mock {@link AsnSchemaType} instances conforming to the 'PublishedMetadata' Type
     * Definition in the 'Document-PDU' module in the test ASN.1 schema defined in the {@code
     * README.md} file
     *
     * @return mock instance
     */
    public static AsnSchemaType getDocumentPublishedMetadata() {
        return builder(AsnPrimitiveTypes.SEQUENCE)
                .addComponent(
                        "1", "date", createMockedAsnSchemaType(AsnPrimitiveTypes.GENERALIZED_TIME))
                .addComponent(
                        "2",
                        "country",
                        true,
                        createMockedAsnSchemaType(AsnPrimitiveTypes.OCTET_STRING))
                .build();
    }

    /**
     * Creates mock {@link AsnSchemaType} instances conforming to the 'ModificationMetadata' Type
     * Definition in the 'Document-PDU' module in the test ASN.1 schema defined in the {@code
     * README.md} file
     *
     * @return mock instance
     */
    public static AsnSchemaType getDocumentModificationMetadataLinked() {
        return builder(AsnPrimitiveTypes.SEQUENCE)
                .addComponent(
                        "0", "date", createMockedAsnSchemaType(AsnPrimitiveTypes.GENERALIZED_TIME))
                .addComponent("1", "modifiedBy", getPeopleProtocolPerson())
                .build();
    }

    /**
     * Creates mock {@link AsnSchemaType} instances conforming to the 'Header' Type Definition in
     * the 'Document-PDU' module in the test ASN.1 schema defined in the {@code README.md} file
     *
     * @return mock instance
     */
    public static AsnSchemaType getDocumentHeader() {
        return builder(AsnPrimitiveTypes.SEQUENCE)
                .addComponent("0", "published", getDocumentPublishedMetadata())
                .build();
    }

    /**
     * Creates mock {@link AsnSchemaType} instances conforming to the 'Body' Type Definition in the
     * 'Document-PDU' module in the test ASN.1 schema defined in the {@code README.md} file
     *
     * @return mock instance
     */
    public static AsnSchemaType getDocumentBody() {
        return builder(AsnPrimitiveTypes.SEQUENCE)
                .addComponent("0", "lastModified", getDocumentModificationMetadataLinked())
                .addComponent("1", "prefix", true, getDocumentSectionNote())
                .addComponent("2", "content", getDocumentSectionMain())
                .addComponent("3", "suffix", true, getDocumentSectionNote())
                .build();
    }

    /**
     * Creates mock {@link AsnSchemaType} instances conforming to the 'Footer' Type Definition in
     * the 'Document-PDU' module in the test ASN.1 schema defined in the {@code README.md} file
     *
     * @return mock instance
     */
    public static AsnSchemaType getDocumentFooter() {
        return builder(AsnPrimitiveTypes.SET)
                .addComponent("0", "authors", getPeopleProtocolPeople())
                .build();
    }

    /**
     * Creates mock {@link AsnSchemaType} instances conforming to the 'Document' Type Definition in
     * the 'Document-PDU' module in the test ASN.1 schema defined in the {@code README.md} file
     *
     * @return mock instance
     */
    public static AsnSchemaType createMockedAsnSchemaTypeForDocumentPdu() {
        return builder(AsnPrimitiveTypes.SEQUENCE)
                .addComponent("1", "header", getDocumentHeader())
                .addComponent("2", "body", getDocumentBody())
                .addComponent("3", "footer", getDocumentFooter())
                .addComponent("4", "dueDate", getDocumentDueDate())
                .addComponent("5", "version", getDocumentVersion())
                .addComponent("6", "description", getDocumentDescription())
                .build();
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: MockAsnSchemaTypeBuilder
    // -------------------------------------------------------------------------

    /**
     * Builder for creating mocked instances of {@link AsnSchemaType}
     *
     * @author brightSPARK Labs
     */
    public static class MockAsnSchemaTypeBuilder {
        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        final AsnSchemaTypeConstructed mockedInstance = mock(AsnSchemaTypeConstructed.class);

        final List<AsnSchemaComponentType> components = Lists.newArrayList();
        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor
         *
         * @param primitiveType value to return for {@link AsnSchemaType#getPrimitiveType()} method
         * @param constraint value to return for {@link AsnSchemaType#getConstraints()} method
         */
        private MockAsnSchemaTypeBuilder(
                AsnPrimitiveType primitiveType, AsnSchemaConstraint constraint) {
            final ImmutableSet<AsnSchemaConstraint> constraints =
                    ImmutableSet.of((constraint == null) ? AsnSchemaConstraint.NULL : constraint);

            when(mockedInstance.getPrimitiveType()).thenReturn(primitiveType);
            when(mockedInstance.getConstraints()).thenReturn(constraints);
            when(mockedInstance.getBuiltinType()).thenReturn(primitiveType.getBuiltinType());

            when(mockedInstance.getAllComponents()).thenReturn(ImmutableList.copyOf(components));

            // setup default matchers, when new components are added they will add specific returns
            // for those tags.
            when(mockedInstance.getMatchingChild(anyString(), any(DecodingSession.class)))
                    .thenReturn(Optional.empty());
        }

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

        /**
         * Adds a component to the mocked instance. This is a shortcut that defaults isOptional to
         * false
         *
         * @param rawTag what will be returned by the mocked getTag function
         * @param componentName what will be returned by the mocked getTagName function
         * @param type what will be returned by the mocked getType function
         */
        public MockAsnSchemaTypeBuilder addComponent(
                String rawTag, String componentName, AsnSchemaType type) {
            return addComponent(rawTag, componentName, false, type);
        }

        /**
         * Will construct a mocked AsnSchemaComponentType and add it to the mocked AsnSchemaType
         *
         * @param rawTag what will be returned by the mocked getTag function
         * @param componentName what will be returned by the mocked getTagName function
         * @param isOptional what will be returned by the mocked isOptional function
         * @param type what will be returned by the mocked getType function
         * @return this builder
         */
        public MockAsnSchemaTypeBuilder addComponent(
                String rawTag, String componentName, boolean isOptional, AsnSchemaType type) {
            AsnSchemaComponentType component =
                    MockAsnSchemaComponentType.createMockedComponentType(
                            componentName, rawTag, isOptional, type);

            return addComponent(component);
        }

        /**
         * Adds a Component to the mocked AsnSchemaType. This then allows the getChildType and
         * getChildName functions to delegate to this added Component if the tags match
         *
         * @param component the AsnSchemaComponentType object
         * @return this builder
         */
        public MockAsnSchemaTypeBuilder addComponent(final AsnSchemaComponentType component) {

            components.add(component);
            when(mockedInstance.getAllComponents()).thenReturn(ImmutableList.copyOf(components));
            final String theTag = component.getTag();

            // The tags come to us in the form index[tag].
            //
            // two forms, "2" or "2[0]", the latter being for Collections
            // where the [0] is the index in the collection and could be any number.
            // We want to match either, so we need to make a specific ArgumentMatcher.
            class TagMatcher implements ArgumentMatcher<String> {
                private final Pattern pattern =
                        Pattern.compile("^([0-9]+)(\\[([a-zA-Z0-9 ]+)\\])$");

                @Override
                public boolean matches(final String argument) {
                    final String arg = Strings.nullToEmpty(argument);
                    final Matcher m = pattern.matcher(arg);
                    if (m.matches()) {
                        final String tag = Strings.nullToEmpty(m.group(3));
                        return tag.equals(theTag);
                    }
                    return false;
                }
            }

            // Setup the mocks to match using the specific matcher, and then delegate to the
            // appropriate
            // component type.

            when(mockedInstance.getMatchingChild(
                            argThat(new TagMatcher()), any(DecodingSession.class)))
                    .thenAnswer(
                            new Answer<Optional<AsnSchemaComponentType>>() {
                                @Override
                                public Optional<AsnSchemaComponentType> answer(
                                        InvocationOnMock invocation) throws Throwable {
                                    return Optional.of(component);
                                }
                            });

            return this;
        }

        /**
         * Use this to mock a Collection type, eg SET OF
         *
         * @param element setting the new type that getChildType and getChildName will delegate to
         * @return this builder
         */
        public MockAsnSchemaTypeBuilder setCollectionType(final AsnSchemaType element) {
            when(mockedInstance.getMatchingChild(anyString(), any(DecodingSession.class)))
                    .thenAnswer(
                            new Answer<Optional<AsnSchemaComponentType>>() {
                                private Pattern pattern =
                                        Pattern.compile("^([0-9]+)(\\[([a-zA-Z0-9 ]+)\\])$");

                                @Override
                                public Optional<AsnSchemaComponentType> answer(
                                        InvocationOnMock invocation) throws Throwable {
                                    Object[] args = invocation.getArguments();
                                    String arg = Strings.nullToEmpty((String) args[0]);
                                    Matcher m = pattern.matcher(arg);
                                    if (m.matches()) {
                                        String index = Strings.nullToEmpty(m.group(1));
                                        AsnSchemaComponentType mockResult =
                                                mock(AsnSchemaComponentType.class);
                                        when(mockResult.getType()).thenReturn(element);
                                        when(mockResult.getTag()).thenReturn(arg);
                                        when(mockResult.getName()).thenReturn("[" + index + "]");

                                        return Optional.of(mockResult);
                                    }
                                    return Optional.empty();
                                }
                            });

            return this;
        }

        /**
         * Creates a mocked instance from the data in this builder
         *
         * @return a mocked instance of {@link AsnSchemaTypeConstructed}
         */
        public AsnSchemaType build() {
            return mockedInstance;
        }
    }
}
