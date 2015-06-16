/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import static com.google.common.base.Preconditions.*;

import java.util.Map;

import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.typedefinition.OLDAsnSchemaTypeDefinition;
import com.google.common.base.Strings;
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
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** null instance */
    public static final AsnSchemaModule.Null NULL = new AsnSchemaModule.Null();

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** name of this module */
    private final String name;

    /** all types defined in this module */
    private final ImmutableMap<String, AsnSchemaTypeDefinition> types;

    /**
     * all types imported by this module. Map is of form {typeName =&gt;
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
     *            =&gt; moduleName}
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
        checkArgument(!name.trim().isEmpty(), "A module from an ASN.1 schema must have a name");
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
     * Returns the type definition associated with the specified type name
     *
     * @param typeName
     *            name of the type. E.g. {@code "Document"}
     *
     * @return the type definition associated with the specified type name or
     *         {@link OLDAsnSchemaTypeDefinition#NULL} if no type definition is
     *         found
     */
    public AsnSchemaTypeDefinition getType(String typeName)
    {
        final AsnSchemaTypeDefinition type = types.get(typeName);
        return type != null ? type
                : AsnSchemaTypeDefinition.NULL;
    }

    /**
     * Returns the name of the imported module which contains the specified type
     * name
     *
     * @param typeName
     *            the name of the type which has been imported
     *
     * @return the name of the imported module which contains the specified type
     *         or an empty string if the type has not been imported
     */
    public String getImportedModuleFor(String typeName)
    {
        final String moduleName = imports.get(typeName);
        return Strings.nullToEmpty(moduleName);
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
         * all types imported by this module. Map is of form {typeName =&gt;
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
         *            {typeName =&gt; moduleName}
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

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Null
    // -------------------------------------------------------------------------

    /**
     * Null instance of {@link AsnSchemaModule}
     *
     * @author brightSPARK Labs
     */
    public static class Null extends AsnSchemaModule
    {
        /**
         * Default constructor. Hidden, use {@link AsnSchemaModule#NULL} to
         * obtain a singleton instance.
         */
        private Null()
        {
            super("NULL", Maps.<String, AsnSchemaTypeDefinition>newHashMap(), Maps.<String, String>newHashMap());
        }
    }
}
