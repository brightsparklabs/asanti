/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
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
    private static final Splitter tagSplitter = Splitter.on("/")
            .omitEmptyStrings();

    /** splitter for creating tag strings */
    private static final Joiner tagJoiner = Joiner.on("/");

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** name of this module */
    private final String name;

    /** all types defined in this module */
    private final ImmutableMap<String, AsnSchemaTypeDefinition> types;

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
     *
     * @param name
     *            the name of this module
     *
     * @param types
     *            the type definitions defined in this module
     *
     * @param imports
     *            the imports defined in this module. This identifies the module
     *            that an imported type comes from. Map is of format {typeName
     *            => moduleName}
     *
     * @throws NullPointerException
     *             if any of the parameters are {@code null}
     *
     * @throws IllegalArgumentException
     *             if the name is blank
     */
    private AsnSchemaModule(String name, Map<String, AsnSchemaTypeDefinition> types, Map<String, String> imports)
    {
        checkNotNull(name);
        checkArgument(!name.trim()
                .isEmpty(), "A module from an ASN.1 schema must have a name");
        checkNotNull(types);
        checkNotNull(imports);

        this.name = name;
        this.types = ImmutableMap.copyOf(types);
        this.imports = ImmutableMap.copyOf(imports);
    }

    /**
     * Returns a builder for constructing instances of {@link AsnSchemaModule}
     *
     * @return a builder for constructing instances of {@link AsnSchemaModule}
     */
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
     * {@code getDecodedTag("/1/0/1", "Document", schemas)} =>
     * {@code "/Document/Header/Published/Date"}
     *
     * @param rawTag
     *            raw tag to decode
     *
     * @param topLevelTypeName
     *            the name of the top level type in this module from which to
     *            begin decoding the raw tag
     *
     * @param allSchemaModules
     *            all modules which are present in the schema. These are used to
     *            resolve imports. Map is of form: {@code moduleName => module}
     *
     * @return the decoded tag or an empty string if it cannot be decoded
     */
    public DecodeResult<String> getDecodedTag(String rawTag, String topLevelTypeName,
            ImmutableMap<String, AsnSchemaModule> allSchemaModules)
    {
        final ArrayList<String> tags = Lists.newArrayList(tagSplitter.split(rawTag));
        final List<String> decodedTags = decodeTags(tags.iterator(), topLevelTypeName, allSchemaModules);

        // check if decode was successful
        boolean decodeSuccessful = true;
        if (tags.size() != decodedTags.size())
        {
            // could not decode
            decodeSuccessful = false;

            // copy unknown tags into result
            for (int i = decodedTags.size(); i < tags.size(); i++)
            {
                final String unknownTag = tags.get(i);
                decodedTags.add(unknownTag);
            }
        }

        // prefix result with top level type
        decodedTags.add(0, topLevelTypeName);
        decodedTags.add(0, ""); // empty string prefixes just the separator
        final String decodedData = tagJoiner.join(decodedTags);

        final DecodeResult<String> result = DecodeResult.create(decodeSuccessful, decodedData);
        return result;
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the decoded tags for the supplied raw tags
     *
     * @param rawTags
     *            raw tags to decode. This should be an iterable in the order of
     *            the tags. E.g. The raw tag {code "/1/0/1"} should be provided
     *            as an iterator of {code ["1", "0", "1"]}
     *
     * @param containingTypeName
     *            the type in this module from which to begin decoding the raw
     *            tag. E.g. {@code "Document"} will start decoding the raw tags
     *            from the type definition named {@code Document}
     *
     * @param allSchemaModules
     *            all modules which are present in the schema. These are used to
     *            resolve imports. Map is of form: {@code moduleName => module}
     *
     * @return a list of all decoded tags. If a raw tag could not be decoded
     *         then processing stops. E.g. for the raw tags {code "1", "0" "1",
     *         "99", "98"}, if the {@code "99"} raw tag cannot be decoded, then
     *         a list containing the decoded tags for only the first three raw
     *         tags is returned (e.g. {@code ["Header", "Published", "Date"]})
     */
    private List<String> decodeTags(Iterator<String> rawTags, String containingTypeName,
            ImmutableMap<String, AsnSchemaModule> allSchemaModules)
    {
        final List<String> decodedTags = Lists.newArrayList();
        String typeName = containingTypeName;

        while (rawTags.hasNext())
        {
            if (Strings.isNullOrEmpty(typeName))
            {
                // no type to delve into
                break;
            }

            final AsnSchemaTypeDefinition type = getType(typeName);
            if (type == AsnSchemaTypeDefinition.NULL)
            {
                // type is not defined locally, decode via imports
                final List<String> importedTags = decodeUsingImportedModule(rawTags, typeName, allSchemaModules);
                decodedTags.addAll(importedTags);
                break;
            }

            final String tag = rawTags.next();
            final String tagName = type.getTagName(tag);
            if (Strings.isNullOrEmpty(tagName))
            {
                // unknown tag
                break;
            }
            decodedTags.add(tagName);
            typeName = type.getTypeName(tag);
        }

        return decodedTags;
    }

    /**
     * Returns the type definition associated with the specified type name
     *
     * @param typeName
     *            name of the type. E.g. {@code "Document"}
     *
     * @return the type definition associated with the specified type name or
     *         {@link AsnSchemaTypeDefinition#NULL} if no type definition is
     *         found
     */
    private AsnSchemaTypeDefinition getType(String typeName)
    {
        final AsnSchemaTypeDefinition type = types.get(typeName);
        return type != null ? type : AsnSchemaTypeDefinition.NULL;
    }

    /**
     * Returns the decoded tags for the supplied raw tags using an imported
     * module.
     *
     * @param rawTags
     *            raw tags to decode. This should be an iterable in the order of
     *            the tags. E.g. The raw tag {code "/1/0/1"} should be provided
     *            as an iterator of {code ["1", "0", "1"]}
     *
     * @param typeName
     *            the type in the <b>imported</b> module from which to begin
     *            decoding the raw tag. E.g. {@code "People"} will start
     *            decoding the raw tags from the type definition named
     *            {@code People}
     *
     * @param allSchemaModules
     *            all modules which are present in the schema. These are used to
     *            resolve imports. Map is of form: {@code moduleName => module}
     *
     * @return a list of all decoded tags. If a raw tag could not be decoded
     *         then processing stops. E.g. for the raw tags {code "1", "0" "1",
     *         "99", "98"}, if the {@code "99"} raw tag cannot be decoded, then
     *         a list containing the decoded tags for only the first three raw
     *         tags is returned (e.g. {@code ["Header", "Published", "Date"]})
     */
    private List<String> decodeUsingImportedModule(Iterator<String> rawTags, String typeName,
            ImmutableMap<String, AsnSchemaModule> allSchemaModules)
    {
        // not found locally, check if it is from an import
        final String importedModuleName = imports.get(typeName);
        if (importedModuleName == null)
        {
            log.log(Level.WARNING,
                    "Could not resolve type definition \"{0}\". It is is not defined or imported in module \"{1}\"",
                    new Object[] { typeName, name });
            return Collections.<String>emptyList();
        }

        final AsnSchemaModule importedModule = allSchemaModules.get(importedModuleName);
        // ensure we do not recursively look into the current module
        if (importedModule == null || importedModule.equals(this))
        {
            log.log(Level.WARNING,
                    "Could not resolve type definition \"{0}\". Type is imported from an unknown module \"{1}\"",
                    new Object[] { typeName, name });
            return Collections.<String>emptyList();
        }

        return importedModule.decodeTags(rawTags, typeName, allSchemaModules);
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

        /** name of the top level type defined in this module */
        private final Map<String, AsnSchemaTypeDefinition> types = Maps.newHashMap();

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
        public Builder addType(AsnSchemaTypeDefinition type)
        {
            types.put(type.getName(), type);
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
         * Adds all the imports from the supplied map
         *
         * @param imports
         *            the imports defined in this module. This identifies the
         *            module that an imported type comes from. Map is of format
         *            {typeName => moduleName}
         *
         * @return this builder
         */
        public Builder addImports(Map<String, String> imports)
        {
            this.imports.putAll(imports);
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
            final AsnSchemaModule module = new AsnSchemaModule(name, types, imports);
            return module;
        }
    }
}
