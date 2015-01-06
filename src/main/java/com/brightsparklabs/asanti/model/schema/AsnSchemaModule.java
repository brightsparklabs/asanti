/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * A module specification within an ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaModule
{

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    private final String name;
    private final String topLevelType;
    private final ImmutableMap<String, AsnSchemaTypeDefinition> types;
    private final ImmutableMap<String, String> imports;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------
    /**
     * Private constructor. Use {@link #builder()} to construct an instance.
     */
    private AsnSchemaModule(String name, String topLevelType, Map<String, AsnSchemaTypeDefinition> types,
            Map<String, String> imports)
    {
        this.name = name;
        this.topLevelType = topLevelType;
        this.types = ImmutableMap.copyOf(types);
        this.imports = ImmutableMap.copyOf(imports);
    }

    public static Builder builder()
    {
        return new Builder();
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    public String getName()
    {
        return name;
    }

    /**
     * Returns the decoded tag for the supplied raw tag. E.g.
     * {@code getDecodedTag("/1/0/1")} => {@code "/Header/Published/Date"}
     *
     * @param rawTag
     *            raw tag to decode
     *
     * @return the decoded tag or an empty string if it cannot be decoded
     */
    public String getDecodedTag(String rawTag)
    {
        return "";
    }

    /**
     * Returns the decoded tag for the supplied raw tag which resides under the
     * specified type. E.g. {@code getDecodedTag("Header", "/0/1")} =>
     * {@code "/Header/Published/Date"}
     *
     * @param containingType
     *            name of the type which contains the raw tag
     *
     * @param rawTag
     *            raw tag to decode
     *
     * @return the decoded tag or an empty string if it cannot be decoded
     */
    public String getDecodedTag(String containingType, String rawTag)
    {
        return "";
    }

    /**
     * Returns the raw tag for the supplied decoded tag. E.g.
     * {@code getRawTag("/Header/Published/Date")} => {@code "/1/0/1"}
     *
     * @param decodedTag
     *            decoded tag to map back to raw tag
     *
     * @return the raw tag or an empty string if it cannot be determined
     */
    public String getRawTag(String decodedTag)
    {
        return "";
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Builder
    // -------------------------------------------------------------------------
    /**
     * Builder for creating an {@link AsnSchemaModule}
     */
    public static class Builder
    {
        private String name;
        private String topLevelType;
        private final Map<String, AsnSchemaTypeDefinition> types;
        private final Map<String, String> imports;

        private Builder()
        {
            types = Maps.newHashMap();
            imports = Maps.newHashMap();
        }

        public Builder setName(String name)
        {
            this.name = name;
            return this;
        }

        public Builder setTopLevelType(String typeName)
        {
            this.topLevelType = typeName;
            return this;
        }

        public Builder addType(AsnSchemaTypeDefinition type)
        {
            types.put(type.getName(), type);
            return this;
        }

        public Builder addImport(String typeName, String moduleName)
        {
            imports.put(typeName, moduleName);
            return this;
        }

        public AsnSchemaModule build()
        {
            final AsnSchemaModule module = new AsnSchemaModule(name, topLevelType, types, imports);
            return module;
        }
    }
}
