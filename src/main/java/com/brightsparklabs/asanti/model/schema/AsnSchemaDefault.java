/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

/**
 * Default implementation of {@link AsnSchema}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaDefault implements AsnSchema
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
    public AsnSchemaDefault(String primaryModule, Map<String, AsnSchemaModule> modules)
    {
        Preconditions.checkNotNull(primaryModule);
        Preconditions.checkNotNull(modules);
        Preconditions.checkArgument(!modules.isEmpty(), "A schema cannot contain no modules");
        Preconditions.checkArgument(modules.containsKey(primaryModule),
                "The primary module must be contained in the schema's modules");

        this.primaryModule = modules.get(primaryModule);
        this.modules = ImmutableMap.copyOf(modules);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchema
    // -------------------------------------------------------------------------

    @Override
    public String getDecodedTag(String rawTag)
    {
        return primaryModule.getDecodedTag(rawTag, modules);
    }

    @Override
    public String getRawTag(String decodedTag)
    {
        return primaryModule.getRawTag(decodedTag, modules);
    }

    @Override
    public String getPrintableString(String tag, byte[] data)
    {
        return "";
    }

    @Override
    public Object getDecodedObject(String tag, byte[] data)
    {
        return "";
    }
}
