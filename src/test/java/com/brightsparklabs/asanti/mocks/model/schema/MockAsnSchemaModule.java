/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks.model.schema;

import com.brightsparklabs.asanti.model.schema.AsnSchemaModule;
import com.brightsparklabs.asanti.model.schema.AsnSchemaModule.Builder;
import com.brightsparklabs.asanti.model.schema.typedefinition.AbstractAsnSchemaTypeDefinition;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

/**
 * Utility class for obtaining mocked instances of {@link AsnSchemaModule} which
 * conform to the test ASN.1 schema defined in the {@linkplain README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockAsnSchemaModule
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** the example Document-PDU module defined in the {@linkplain README.md} file */
    public static final Iterable<String> TEST_MODULE_DOCUMENT_PDU =
            Lists.newArrayList(
                    "Document-PDU",
                    "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) document(1) }",
                    "DEFINITIONS",
                    "AUTOMATIC TAGS ::=",
                    "BEGIN",
                    "EXPORTS Header, Body",
                    ";",
                    "IMPORTS",
                    "Person FROM People-Protocol { joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }",
                    ";",
                    "Document ::= SEQUENCE {",
                    "header [1] Header,",
                    "body [2] Body,",
                    "footer [3] Footer",
                    "}",
                    "Header ::= SEQUENCE",
                    "{ published [0] PublishedMetadata }",
                    "Body ::= SEQUENCE { lastModified [0] ModificationMetadata, prefix [1] Section-Note OPTIONAL, content [2] Section-Main,",
                    "suffix [3] Section-Note OPTIONAL }",
                    "Footer ::= SEQUENCE { author [0] Person }",
                    "PublishedMetadata ::= SEQUENCE { date [1] GeneralizedTime, country [2] OCTET STRING OPTIONAL }",
                    "ModificationMetadata ::= SEQUENCE { date [0] Date, modifiedBy [1] Person }",
                    "Section-Note ::= SEQUENCE { text [1] OCTET STRING }",
                    "Section-Main ::= SEQUENCE { text [1] OCTET STRING OPTIONAL, paragraphs [2] SEQUENCE OF Paragraph }",
                    "Paragraph ::= SEQUENCE { title [1] OCTET STRING, contributor [2] Person OPTIONAL, points [3] SEQUENCE OF OCTET STRING }",
                    "END");

    /** the example People-Protocol module defined in the {@linkplain README.md} file */
    public static final Iterable<String> TEST_MODULE_PEOPLE_PROTOCOL =
            Lists.newArrayList(
                    "People-Protocol",
                    "{ joint-iso-itu-t internationalRA(23) set(42) set-vendors(9) example(99) modules(2) people(2) }",
                    "DEFINITIONS",
                    "AUTOMATIC TAGS ::=",
                    "BEGIN",
                    "defaultAge INTEGER ::= 45",
                    "People ::= SET OF Person",
                    "Person ::= SEQUENCE",
                    "{",
                    "firstName [1] OCTET STRING,",
                    "lastName [2] OCTET STRING,",
                    "title [3] ENUMERATED",
                    "{ mr, mrs, ms, dr, rev } OPTIONAL,",
                    "gender OPTIONAL",
                    "}",
                    "Gender ::= ENUMERATED",
                    "{ male(0),",
                    "female(1)",
                    "}",
                    "END");

    // -------------------------------------------------------------------------
    // MOCKS
    // -------------------------------------------------------------------------

    /**
     * Creates a mock {@link AsnSchemaModule} instances conforming to the
     * modules in the example schema
     *
     * @return mocked instance
     */
    public static ImmutableMap<String, AsnSchemaModule> createMockedAsnSchemaModules()
    {
        final AsnSchemaModule documentPduModuleinstance = createMockedAsnSchemaModuleForDocumentPdu();
        final AsnSchemaModule peopleProtocolModule = createMockedAsnSchemaModuleForPeopleProtocol();
        final ImmutableMap<String, AsnSchemaModule> modules =
                ImmutableMap.of(documentPduModuleinstance.getName(), documentPduModuleinstance, peopleProtocolModule.getName(), peopleProtocolModule);
        return modules;
    }

    /**
     * Creates a mock {@link AsnSchemaModule} instance conforming to the
     * 'Document-PDU' module in the example schema
     *
     * @return mocked instance
     */
    public static AsnSchemaModule createMockedAsnSchemaModuleForDocumentPdu()
    {
        final Builder moduleBuilder = AsnSchemaModule.builder();
        moduleBuilder.setName("Document-PDU")
                .addImport("People", "People-Protocol")
                .addImport("Person", "People-Protocol");
        final ImmutableList<AbstractAsnSchemaTypeDefinition> typeDefinitions =
                MockAsnSchemaTypeDefinition.createMockedAsnSchemaTypeDefinitionsForDocumentPdu();
        for (final AbstractAsnSchemaTypeDefinition typeDefinition : typeDefinitions)
        {
            moduleBuilder.addType(typeDefinition);
        }
        return moduleBuilder.build();
    }

    /**
     * Creates a mock {@link AsnSchemaModule} instance conforming to the
     * 'Document-PDU' module in the example schema
     *
     * @return mocked instance
     */
    public static AsnSchemaModule createMockedAsnSchemaModuleForPeopleProtocol()
    {
        final Builder moduleBuilder = AsnSchemaModule.builder();
        moduleBuilder.setName("People-Protocol");
        final ImmutableList<AbstractAsnSchemaTypeDefinition> typeDefinitions =
                MockAsnSchemaTypeDefinition.createMockedAsnSchemaTypeDefinitionsForPeopleProtocol();
        for (final AbstractAsnSchemaTypeDefinition typeDefinition : typeDefinitions)
        {
            moduleBuilder.addType(typeDefinition);
        }
        return moduleBuilder.build();
    }
}
