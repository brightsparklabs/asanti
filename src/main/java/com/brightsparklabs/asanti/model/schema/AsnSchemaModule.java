/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    /** class logger */
    private static final Logger log = Logger.getLogger(AsnSchemaModule.class.getName());

    /** splitter for separating tag strings */
    private static final Splitter tagSplitter = Splitter.on("/").omitEmptyStrings();

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

    /**
     * Returns the name of this module
     *
     * @return the name of this module
     */
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
        final ArrayList<String> tags = Lists.newArrayList(tagSplitter.split(rawTag));
        final ArrayList<String> decodedTags = new ArrayList<>(tags.size());

        // map the first raw tag to the top level type
        String typeName = topLevelTypeName;
        decodedTags.add(typeName);
        AsnSchemaTypeDefinition<?> type = types.get(typeName);

        // try to decode each tag
        for (int i = 1; i < tags.size(); i++)
        {
            if (type == null)
            {
                log.log(Level.WARNING, "Could not resolve type definition \"{0}\" within module \"{1}\"", new Object[] {
                        typeName, name });
                break;
            }

            final String tag = tags.get(i);
            final String tagName = type.getTagName(tag);
            typeName = type.getTypeName(tag);
            if (tagName.isEmpty() || typeName.isEmpty())
            {
                break;
            }
            decodedTags.add(tagName);
            type = types.get(typeName);
        }

        // add any undecoded tags to end
        if (tags.size() != decodedTags.size())
        {
            // could not decode, copy unknown tag into result
            for (int i = decodedTags.size(); i < tags.size(); i++)
            {
                final String unknownTag = tags.get(i);
                decodedTags.add(unknownTag);
            }
        }

        return tagJoiner.join(decodedTags);
    }

    /**
     * Returns the type definition associated with the specified type name
     *
     * @param typeName
     *            name of the type
     *
     * @param allSchemaModules
     *            all modules contained in the schema. This is used if a type
     *            definition resides in a different module.
     *
     * @return the type definition associated with the specified type name or
     *         {@link AsnSchemaTypeDefinition#NULL} if no type definition is
     *         found
     */
    public AsnSchemaTypeDefinition<?> getType(String typeName, ImmutableMap<String, AsnSchemaModule> allSchemaModules)
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

        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /** name of this module */
        private String name = "";

        /**
         * name of the top level type defined in this module. Defaults to the
         * first type added via {@link #addType(AsnSchemaTypeDefinition)}.
         */
        private String topLevelTypeName = "";

        /** name of the top level type defined in this module */
        private final Map<String, AsnSchemaTypeDefinition<?>> types = Maps.newHashMap();

        /**
         * all types imported by this module. Map is of form {typeName =>
         * importedModuleName}
         */
        private final Map<String, String> imports = Maps.newHashMap();

        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /**
         * Default constructor
         */
        private Builder()
        {
            // private constructor
        }

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

        /**
         * Sets the name of the module
         *
         * @param name
         *            name of the module
         *
         * @return this builder
         */
        public Builder setName(String name)
        {
            this.name = name;
            return this;
        }

        /**
         * Sets the top level type of this module to the supplied type name.
         *
         * If not called, then the top level type will default to the first type
         * added by {@link #addType(AsnSchemaTypeDefinition)}.
         *
         * @param topLevelTypeName
         *            name of the top level type in this module
         *
         * @return this builder
         */
        public Builder setTopLevelTypeName(String topLevelTypeName)
        {
            this.topLevelTypeName = topLevelTypeName;
            return this;
        }

        /**
         * Stores a new type definition to this module. I.e. the specified type
         * definition is found in module.
         *
         * If no top level type is set in this builder, it will default to the
         * first type added using this method.
         *
         * @param type
         *            type definition to add
         *
         * @return this builder
         */
        public Builder addType(AsnSchemaTypeDefinition<?> type)
        {
            types.put(type.getName(), type);

            // set top level type to first type added
            if (topLevelTypeName.isEmpty())
            {
                topLevelTypeName = type.getName();
            }

            return this;
        }

        /**
         * Adds a import statement mapping the specified type name to the
         * supplied module
         *
         * @param typeName
         *            name of the type which is imported
         *
         * @param moduleName
         *            name of the module the type is imported from (i.e. module
         *            it is defined in)
         *
         * @return this builder
         */
        public Builder addImport(String typeName, String moduleName)
        {
            imports.put(typeName, moduleName);
            return this;
        }

        /**
         * Creates an instance of {@link AsnSchemaModule} from the information
         * in this builder
         *
         * @return an instance of {@link AsnSchemaModule}
         */
        public AsnSchemaModule build()
        {
            final AsnSchemaModule module = new AsnSchemaModule(name, topLevelTypeName, types, imports);
            return module;
        }
    }
}
