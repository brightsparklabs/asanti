/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * A module specification within an ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaModule
{

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** splitter for separating tag strings */
    private static final Splitter tagSplitter = Splitter.on("/");

    /** splitter for creating tag strings */
    private static final Joiner tagJoiner = Joiner.on("/");

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** name of this module */
    private final String name;

    /** name of the top level type defined in this module */
    private final String topLevelTypeName;

    /** all types defined in this module */
    private final ImmutableMap<String, AsnSchemaTypeDefinition<?>> types;

    /**
     * all types imported by this module. Map is of form {typeName =>
     * importedModuleName}
     */
    private final ImmutableMap<String, String> imports;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Private constructor. Use {@link #builder()} to construct an instance.
     */
    private AsnSchemaModule(String name, String topLevelTypeName, Map<String, AsnSchemaTypeDefinition<?>> types,
            Map<String, String> imports)
    {
        this.name = name;
        this.topLevelTypeName = topLevelTypeName;
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
     * @param allSchemaModules
     *            all modules which are present in the schema. These are used to
     *            resolve imports. Map is of form: {@code moduleName => module}
     *
     * @return the decoded tag or an empty string if it cannot be decoded
     */
    public String getDecodedTag(String rawTag, ImmutableMap<String, AsnSchemaModule> allSchemaModules)
    {
        final List<String> tags = Lists.newArrayList(tagSplitter.split(rawTag));
        final List<String> decodedTags = new ArrayList<>(tags.size());

        // map the first raw tag to the top level type
        tags.remove(0);
        String typeName = topLevelTypeName;
        decodedTags.add(typeName);
        AsnSchemaTypeDefinition<?> type = types.get(typeName);

        for (String tag : tags)
        {
            typeName = type.getTypeName(tag);
            if (typeName.isEmpty())
            {
                break;
            }
            decodedTags.add(typeName);
            type = types.get(typeName);
        }

        if (tags.size() != decodedTags.size())
        {
            // could not decode, copy raw tag into result
        }

        return tagJoiner.join(decodedTags);
    }

    private AsnSchemaTypeDefinition<?> getType(String typeName, ImmutableMap<String, AsnSchemaModule> allSchemaModules)
    {
        AsnSchemaTypeDefinition<?> type = types.get(typeName);
        if (type != null) { return type; }

        // not found locally, check if it is from an import
        final String importedModule = imports.get(typeName);
        if (importedModule != null)
        {
            final AsnSchemaModule module = allSchemaModules.get(importedModule);
            // ensure we do not recursively look into the current module
            if (module != null && !module.equals(this))
            {
                type = module.getType(typeName, allSchemaModules);
                if (type != null) { return type; }
            }
        }

        // cannot resolve the type name
        return AsnSchemaTypeDefinition.NULL;
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
     * @param allSchemaModules
     *            all modules which are present in the schema. These are used to
     *            resolve imports. Map is of form: {@code moduleName => module}
     *
     * @return the decoded tag or an empty string if it cannot be decoded
     */
    public String getDecodedTag(String containingType, String rawTag,
            ImmutableMap<String, AsnSchemaModule> allSchemaModules)
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
     * @param allSchemaModules
     *            all modules which are present in the schema. These are used to
     *            resolve imports. Map is of form: {@code moduleName => module}
     *
     * @return the raw tag or an empty string if it cannot be determined
     */
    public String getRawTag(String decodedTag, ImmutableMap<String, AsnSchemaModule> allSchemaModules)
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
        private String topLevelTypeName;
        private final Map<String, AsnSchemaTypeDefinition<?>> types;
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

        public Builder setTopLevelTypeName(String topLevelTypeName)
        {
            this.topLevelTypeName = topLevelTypeName;
            return this;
        }

        public Builder addType(AsnSchemaTypeDefinition<?> type)
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
            final AsnSchemaModule module = new AsnSchemaModule(name, topLevelTypeName, types, imports);
            return module;
        }
    }
}
