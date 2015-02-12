/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import static com.google.common.base.Preconditions.*;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * Default implementation of {@link AsnSchema}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaImpl implements AsnSchema
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** the primary module defined in this schema (defaults to the first module) */
    private final AsnSchemaModule primaryModule;

    /** all modules defined in this schema. Mapped as: {moduleName => module} */
    private final ImmutableMap<String, AsnSchemaModule> modules;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------
    /**
     * Default constructor
     *
     * @param primaryModule
     *            the primary module defined in this schema
     *
     * @param modules
     *            all modules defined in this schema
     *
     * @throws NullPointerException
     *             if any of the parameters are {@code null}
     *
     * @throws IllegalArgumentException
     *             if no modules are specified or the primary module is not
     *             contained in the modules
     */
    public AsnSchemaImpl(String primaryModule, Map<String, AsnSchemaModule> modules)
    {
        checkNotNull(primaryModule);
        checkNotNull(modules);
        checkArgument(!modules.isEmpty(), "A schema cannot contain no modules");
        checkArgument(modules.containsKey(primaryModule), "The primary module must be contained in the schema's modules");

        this.primaryModule = modules.get(primaryModule);
        this.modules = ImmutableMap.copyOf(modules);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchema
    // -------------------------------------------------------------------------

    @Override
    public DecodeResult<String> getDecodedTag(String rawTag, String topLevelTypeName)
    {
        return primaryModule.getDecodedTag(rawTag, topLevelTypeName, modules);
    }

    @Override
    public String getPrintableString(String tag, byte[] data)
    {
        // TODO ASN-8
        return "";
    }

    @Override
    public Object getDecodedObject(String tag, byte[] data)
    {
        // TODO ASN-8
        return "";
    }
}
