/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.mocks;

import com.brightsparklabs.asanti.model.schema.AsnSchemaModule;
import com.brightsparklabs.asanti.model.schema.AsnSchemaModule.Builder;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * Utility class for obtaining mocked instances of {@link AsnSchemaModule} which
 * conform to the test ASN.1 schema defined in the {@linkplain README.md} file
 *
 * @author brightSPARK Labs
 */
public class MockAsnSchemaModule
{
    // -------------------------------------------------------------------------
    // MOCKS
    // -------------------------------------------------------------------------

    /**
     * Creates a mock {@link AsnSchemaModule} instances conforming to the
     * modules in the test schema
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
     * 'Document-PDU' module in the test schema
     *
     * @return mocked instance
     */
    public static AsnSchemaModule createMockedAsnSchemaModuleForDocumentPdu()
    {
        final Builder moduleBuilder = AsnSchemaModule.builder();
        moduleBuilder.setName("Document-PDU")
                .addImport("Person", "People-Protocol");
        final ImmutableList<AsnSchemaTypeDefinition> typeDefinitions =
                MockAsnSchemaTypeDefinition.createMockedAsnSchemaTypeDefinitionsForDocumentPdu();
        for (final AsnSchemaTypeDefinition typeDefinition : typeDefinitions)
        {
            moduleBuilder.addType(typeDefinition);
        }
        return moduleBuilder.build();
    }

    /**
     * Creates a mock {@link AsnSchemaModule} instance conforming to the
     * 'Document-PDU' module in the test schema
     *
     * @return mocked instance
     */
    public static AsnSchemaModule createMockedAsnSchemaModuleForPeopleProtocol()
    {
        final Builder moduleBuilder = AsnSchemaModule.builder();
        moduleBuilder.setName("People-Protocol");
        final ImmutableList<AsnSchemaTypeDefinition> typeDefinitions =
                MockAsnSchemaTypeDefinition.createMockedAsnSchemaTypeDefinitionsForPeopleProtocol();
        for (final AsnSchemaTypeDefinition typeDefinition : typeDefinitions)
        {
            moduleBuilder.addType(typeDefinition);
        }
        return moduleBuilder.build();
    }
}
