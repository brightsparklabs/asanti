/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypeCollection;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypeConstructed;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypePlaceholder;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Map;

import static com.google.common.base.Preconditions.*;

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
     * all types imported by this module. Map is of form {typeName =&gt; importedModuleName}
     */
    private final ImmutableMap<String, String> imports;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Private constructor. Use {@link #builder()} to construct an instance.
     *
     * @param name
     *         the name of this module
     * @param types
     *         the type definitions defined in this module
     * @param imports
     *         the imports defined in this module. This identifies the module that an imported type
     *         comes from. Map is of format {typeName =&gt; moduleName}
     *
     * @throws NullPointerException
     *         if any of the parameters are {@code null}
     * @throws IllegalArgumentException
     *         if the name is blank
     */
    private AsnSchemaModule(String name, Map<String, AsnSchemaTypeDefinition> types,
            Map<String, String> imports)
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
     *         name of the type. E.g. {@code "Document"}
     *
     * @return the type definition associated with the specified type name or {@link
     * AsnSchemaTypeDefinition#NULL} if no type definition is found
     */
    public AsnSchemaTypeDefinition getType(String typeName)
    {
        final AsnSchemaTypeDefinition type = types.get(typeName);
        return type != null ? type : AsnSchemaTypeDefinition.NULL;
    }

    /**
     * Returns the name of the imported module which contains the specified type name
     *
     * @param typeName
     *         the name of the type which has been imported
     *
     * @return the name of the imported module which contains the specified type or an empty string
     * if the type has not been imported
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
        // -------------------------------------------------------------------------
        // CLASS VARIABLES
        // -------------------------------------------------------------------------

        /** class logger */
        private static final Logger logger = LoggerFactory.getLogger(Builder.class);

        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /** name of this module */
        private String name = "";

        /** name of the top level type defined in this module */
        private final Map<String, AsnSchemaTypeDefinition> types = Maps.newHashMap();

        /**
         * all types imported by this module. Map is of form {typeName =&gt; importedModuleName}
         */
        // TODO ASN-132 - by storing in a Map we can't have the same typeName come from
        // multiple modules, which should be valid.
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
         *         name of the module
         *
         * @return this builder
         */
        public Builder setName(String name)
        {
            this.name = name;
            return this;
        }

        /**
         * Stores a new type definition to this module. I.e. the specified type definition is found
         * in module.
         *
         * If no top level type is set in this builder, it will default to the first type added
         * using this method.
         *
         * @param type
         *         type definition to add
         *
         * @return this builder
         */
        public Builder addType(AsnSchemaTypeDefinition type)
        {
            types.put(type.getName(), type);
            return this;
        }

        /**
         * Adds a import statement mapping the specified type name to the supplied module
         *
         * @param typeName
         *         name of the type which is imported
         * @param moduleName
         *         name of the module the type is imported from (i.e. module it is defined in)
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
         *         the imports defined in this module. This identifies the module that an imported
         *         type comes from. Map is of format {typeName =&gt; moduleName}
         *
         * @return this builder
         */
        public Builder addImports(Map<String, String> imports)
        {
            this.imports.putAll(imports);
            return this;
        }

        /**
         * Creates an instance of {@link AsnSchemaModule} from the information in this builder
         *
         * @param otherModules
         *         the other {@link AsnSchemaModule.Builder AsnSchemaModule.Builders} within the
         *         schema.  They will be used to resolve imports as part of the final build.
         *
         * @return an instance of {@link AsnSchemaModule}
         *
         * @throws ParseException
         *         if there are imports that cannot be resolved
         */
        public AsnSchemaModule build(Iterable<Builder> otherModules) throws ParseException
        {
            // convert to a Map so we can easily key off the name when doing
            // import resolution
            final ImmutableMap.Builder<String, AsnSchemaModule.Builder> builder
                    = ImmutableMap.builder();
            for (Builder moduleBuilder : otherModules)
            {
                builder.put(moduleBuilder.name, moduleBuilder);
            }

            resolveTypes(builder.build());
            final AsnSchemaModule module = new AsnSchemaModule(name, types, imports);
            return module;
        }

        /**
         * Resolve all of the "placeholder" type definitions such that they point to actual types,
         * even across modules.
         *
         * @param otherModules
         *         the other {@link AsnSchemaModule.Builder AsnSchemaModule.Builders} within the
         *         schema.  They will be used to resolve imports as part of the final build.
         *
         * @throws ParseException
         *         if there are imports that cannot be resolved
         */
        private void resolveTypes(Map<String, AsnSchemaModule.Builder> otherModules)
                throws ParseException
        {
            for (AsnSchemaTypeDefinition typeDefinition : types.values())
            {
                resolveType(typeDefinition.getType(), otherModules);
            }
        }

        /**
         * Recursively resolves the placeholder types definitions within the supplied {@link
         * AsnSchemaType}
         *
         * @param type
         *         the type to resolve
         * @param otherModules
         *         the other {@link AsnSchemaModule.Builder AsnSchemaModule.Builders} within the
         *         schema. These should contain the {@link AsnSchemaTypeDefinition
         *         AsnSchemaTypeDefinitions} for the types that need to be imported.
         *
         * @throws ParseException
         *         if there are imports that cannot be resolved
         */
        private void resolveType(AsnSchemaType type,
                Map<String, AsnSchemaModule.Builder> otherModules) throws ParseException
        {
            // At some future time when we need to perform different behaviour depending on
            // the derived type then we will make a Visitor.  For now this will suffice
            if (type instanceof AsnSchemaTypePlaceholder)
            {
                resolvePlaceholderType((AsnSchemaTypePlaceholder) type, otherModules);
            }
            else if (type instanceof AsnSchemaTypeConstructed)
            {
                resolveConstructedType((AsnSchemaTypeConstructed) type, otherModules);
            }
            else if (type instanceof AsnSchemaTypeCollection)
            {
                resolveType(((AsnSchemaTypeCollection) type).getElementType(), otherModules);
            }
        }

        /**
         * Resolves the supplied {@link AsnSchemaTypePlaceholder}
         *
         * @param type
         *         the type to resolve
         * @param otherModules
         *         the other {@link AsnSchemaModule.Builder AsnSchemaModule.Builders} within the
         *         schema. These should contain the {@link AsnSchemaTypeDefinition
         *         AsnSchemaTypeDefinitions} for the types that need to be imported.
         *
         * @throws ParseException
         *         if there are imports that cannot be resolved
         */
        private void resolvePlaceholderType(final AsnSchemaTypePlaceholder type,
                final Map<String, Builder> otherModules) throws ParseException
        {
            final String moduleName = type.getModuleName();
            final String typeName = type.getTypeName();

            // Assume that the type is in this module.
            AsnSchemaTypeDefinition newTypeDefinition = types.get(typeName);
            if ((newTypeDefinition == AsnSchemaTypeDefinition.NULL) || (newTypeDefinition == null))
            {
                // then it will come from an import.
                newTypeDefinition = getImportedTypeDefinition(moduleName, typeName, otherModules);
            }

            final AsnSchemaType newType = newTypeDefinition.getType();
            if ((newType == AsnSchemaType.NULL) || (newType == null))
            {
                final String errorString = String.format(
                        "Unable to resolve placeholder.  TypeDefinition {%s} is badly formed",
                        typeName);
                logger.warn(errorString);
                throw new ParseException(errorString, -1);
            }

            // we now have the actual type this placeholder was holding out for, so add it.
            type.setIndirectType(newType);
        }

        /**
         * Returns an {@link AsnSchemaTypeDefinition} from either the specified module, or by
         * looking up the appropriate module from the Imports.
         *
         * @param moduleName
         *         The name of the other Module to import from.  May be null/empty.
         * @param typeName
         *         The name of the TypeDefinition to import
         * @param otherModules
         *         The other modules - ie the source of the imports
         *
         * @return The AsnSchemaTypeDefinition for the imported type.
         *
         * @throws ParseException
         *         ParseException if there are imports that cannot be resolved
         */
        private AsnSchemaTypeDefinition getImportedTypeDefinition(String moduleName,
                String typeName, Map<String, AsnSchemaModule.Builder> otherModules)
                throws ParseException
        {

            // figure out which module the place holder may have specified it, if not then we need to look it up
            if (Strings.isNullOrEmpty(moduleName))
            {
                moduleName = imports.get(typeName);
            }

            if (Strings.isNullOrEmpty(moduleName))
            {
                final String errorString = String.format(
                        "Unable to resolve import of {%s}, it is not specified as an import",
                        typeName);
                logger.warn(errorString);
                throw new ParseException(errorString, -1);
            }

            final Builder otherModule = otherModules.get(moduleName);
            if (otherModule == null)
            {
                final String errorString = String.format(
                        "Unable to resolve import of {%s}, was not found in module {%s}",
                        typeName,
                        moduleName);
                logger.warn(errorString);
                throw new ParseException(errorString, -1);
            }

            final AsnSchemaTypeDefinition newTypeDefinition = otherModule.types.get(typeName);
            if ((newTypeDefinition == AsnSchemaTypeDefinition.NULL) || (newTypeDefinition == null))
            {
                final String errorString = String.format(
                        "Unable to resolve import of {%s}, was badly formed in module {%s}",
                        typeName,
                        moduleName);
                logger.warn(errorString);
                throw new ParseException(errorString, -1);
            }

            return newTypeDefinition;
        }

        /**
         * Resolves the placeholder types definitions within the supplied {@link
         * AsnSchemaTypeConstructed}
         *
         * @param type
         *         the type to resolve
         * @param otherModules
         *         the other {@link AsnSchemaModule.Builder AsnSchemaModule.Builders} within the
         *         schema. These should contain the {@link AsnSchemaTypeDefinition
         *         AsnSchemaTypeDefinitions} for the types that need to be imported.
         *
         * @throws ParseException
         *         ParseException if there are imports that cannot be resolved
         */
        private void resolveConstructedType(AsnSchemaTypeConstructed type,
                Map<String, AsnSchemaModule.Builder> otherModules) throws ParseException
        {
            final ImmutableMap<String, AsnSchemaComponentType> allComponents
                    = type.getAllComponents();
            for (AsnSchemaComponentType componentType : allComponents.values())
            {
                resolveType(componentType.getType(), otherModules);
            }
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
         * Default constructor. Hidden, use {@link AsnSchemaModule#NULL} to obtain a singleton
         * instance.
         */
        private Null()
        {
            super("NULL",
                    Maps.<String, AsnSchemaTypeDefinition>newHashMap(),
                    Maps.<String, String>newHashMap());
        }
    }
}
