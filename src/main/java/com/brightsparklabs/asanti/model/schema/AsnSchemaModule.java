/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import static com.google.common.base.Preconditions.*;

import java.util.Map;

import com.brightsparklabs.asanti.model.schema.type.*;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.typedefinition.OLDAsnSchemaTypeDefinition;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        // -------------------------------------------------------------------------
        // CLASS VARIABLES
        // -------------------------------------------------------------------------

        /** class logger */ // TODO MJF
        private static final Logger logger = LoggerFactory.getLogger(Builder.class);

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
        // TODO ASN-132 - by storing in a Map we can't have the same typeName come from
        // multiple modules, which should be valid.
        private final Map<String, String> imports = Maps.newHashMap();

        /**
         * all of the other module builders, we use this to resolve imports.
         */
        private final Map<String, AsnSchemaModule.Builder> otherModules = Maps.newHashMap();

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
         * Adds the container holding all the other AsnSchemaModule.Builders
         *
         * @param otherModules
         *          the Builders for the other modules (used so we can resolve inports across
         *          the different modules
         * @return this builder
         */
        public Builder addAllModules(Map<String, AsnSchemaModule.Builder> otherModules)
        {
            this.otherModules.putAll(otherModules);
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
            resolveTypes();
            final AsnSchemaModule module = new AsnSchemaModule(name, types, imports);
            return module;
        }


        // TODO MJF - with all this other functionality we are less like a traditional "builder"
        // We need the name so that we can put all the different modules in a map.
        // we need to be able to lookup which module a typedef comes from - we don't want to have
        // to iterate all the modules to find the typedef.
        public String getName()
        {
            return name;
        }

        /**
         * resolve all of the "placeholder" type definitions such that they point to actual types,
         * even across modules.
         */
        private void resolveTypes()
        {
            for(AsnSchemaTypeDefinition typeDef : types.values())
            {
                resolveType(typeDef.getType());
            }
        }

        private void resolveType(AsnSchemaType type)
        {

            // TODO MJF - is it worth trying to avoid the instanceof's here?
            // we could use a visitor, but the logic for looking up imports should stay here I think
            if (type instanceof AsnSchemaTypePlaceholder)
            {
                final AsnSchemaTypePlaceholder placeholder = (AsnSchemaTypePlaceholder)type;
                String moduleName = placeholder.getModuleName();
                final String typeName = placeholder.getTypeName();

                // Assume that the type is in this module.
                AsnSchemaTypeDefinition newTypeDefinition = types.get(typeName);

                if ((newTypeDefinition == AsnSchemaTypeDefinition.NULL) ||
                    (newTypeDefinition == null))
                {
                    // then it will come from an import.

                    // figure out which module
                    // the place holder may have specified it, if not then we need to look it up
                    if (Strings.isNullOrEmpty(moduleName))
                    {
                        moduleName = imports.get(typeName);
                    }

                    if (Strings.isNullOrEmpty(moduleName))
                    {
                        logger.warn("Unable to resolve import of {}, it is not specified as an import",
                                    typeName);
                        // TODO MJF - what to do???
                        return;
                    }

                    final AsnSchemaModule.Builder otherModule = otherModules.get(moduleName);
                    if (otherModule == null)
                    {
                        logger.warn("Unable to resolve import of {} from module {}", typeName, moduleName);
                        // TODO MJF - what to do???
                        return;
                    }

                    newTypeDefinition = otherModule.types.get(typeName);
                    if ((newTypeDefinition == AsnSchemaTypeDefinition.NULL) ||
                        (newTypeDefinition == null))
                    {
                        logger.warn("Unable to resolve import of {} from module {}", typeName, moduleName);
                        // TODO MJF - what to do???
                        return;
                    }

                }

                final AsnSchemaType newType = newTypeDefinition.getType();

                if ((newType == AsnSchemaType.NULL) ||
                    (newType == null))
                {
                    logger.warn("Unable to resolve placeholder {} {}", moduleName, typeName);
                    // TODO MJF - what to do???
                    return;
                }

                //We now have the actual type this placeholder was holding out for, so add it.
                placeholder.setIndirectType(newType);
            }
            else if (type instanceof AsnSchemaTypeConstructed)
            {
                AsnSchemaTypeConstructed constructed = (AsnSchemaTypeConstructed)type;
                final ImmutableMap<String, AsnSchemaComponentType> allComponents
                        = constructed.getAllComponents();

                for(AsnSchemaComponentType componentType: allComponents.values())
                {
                    resolveType(componentType.getType());
                }
            }
            else if (type instanceof AsnSchemaTypeCollection)
            {
                AsnSchemaTypeCollection collection = (AsnSchemaTypeCollection)type;
                resolveType(collection.getElementType());
            }
        }


        public void dumpComponents(int indent, AsnSchemaTypeConstructed components)
        {
            final ImmutableMap<String, AsnSchemaComponentType> allComponents
                    = components.getAllComponents();

            for(Map.Entry<String, AsnSchemaComponentType> entry : allComponents.entrySet())
            {
                String typeName = entry.getKey();
                AsnSchemaComponentType componentType = entry.getValue();

                String tagName = componentType.getTagName();
                AsnSchemaType type = componentType.getType();
                dumpType(indent+1, tagName + " ["+typeName+"]", type);
            }
        }

        public void dumpType(int indent, String name, AsnSchemaType type)
        {

            String indentStr = "\t";
            for (int i = 0; i < indent; ++i ) indentStr += "\t";

            AsnBuiltinType builtInType = type.getBuiltinType();
            if (type instanceof AsnSchemaTypeConstructed)
            {
                logger.debug("{}{} is constructed", indentStr, name);
                dumpComponents(indent+1, (AsnSchemaTypeConstructed)type);


            }
            else if (type instanceof AsnSchemaTypeCollection)
            {
                AsnSchemaTypeCollection collection = (AsnSchemaTypeCollection)type;
                logger.debug("{}{} is {} of ", indentStr, name, builtInType);

                dumpType(indent + 1, "", collection.getElementType());

            }
            else if (type instanceof AsnSchemaTypePlaceholder)
            {
                AsnSchemaTypePlaceholder placeholder = (AsnSchemaTypePlaceholder)type;
                String other = (!placeholder.getModuleName().isEmpty()) ? placeholder.getModuleName() + "." +  placeholder.getTypeName() :
                        placeholder.getTypeName();

                String typeName = placeholder.getTypeName();
                AsnSchemaTypeDefinition ofType = types.get(typeName);
                boolean isInThisModule = ofType != null;
                String ofModule;
                if (isInThisModule)
                {
                    ofModule = "this module";
                }
                else
                {
                    String fromModule = imports.get(typeName);
                    if (fromModule != null)
                    {
                        ofModule = fromModule;
                    }
                    else
                    {
                        ofModule = "unknown module";
                    }

                }

                logger.debug("{}**** Placeholder {} points to {} of {}", indentStr, name, other, ofModule);

            }
            else if ((type instanceof BaseAsnSchemaType) ||
                (type instanceof AsnSchemaTypeWithNamedTags))
            {
                logger.debug("{}{} is {}", indentStr, name, builtInType);
            }
            else
            {
                logger.debug("{} Unknown type!!!", indentStr);
            }
        }

        // TODO MJF
        public void dump()
        {
            logger.debug("Module name: {}", name);

            for(Map.Entry<String, AsnSchemaTypeDefinition> entry : types.entrySet())
            {
                String typeName = entry.getKey();
                AsnSchemaTypeDefinition typeDef = entry.getValue();

                dumpType(0, typeName, typeDef.getType());


            }
        }
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: NULL
    // -------------------------------------------------------------------------

    /**
     * NULL instance of {@link AsnSchemaModule}
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
