/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema;

import static com.google.common.base.Preconditions.*;

import com.brightsparklabs.asanti.model.schema.type.*;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaTypeDefinition;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.text.ParseException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A module specification within an ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaModule {
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

    /** all types imported by this module. Map is of form {typeName =&gt; importedModuleName} */
    private final ImmutableMap<String, String> imports;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Private constructor. Use {@link #builder()} to construct an instance.
     *
     * @param name the name of this module
     * @param types the type definitions defined in this module
     * @param imports the imports defined in this module. This identifies the module that an
     *     imported type comes from. Map is of format {typeName =&gt; moduleName}
     * @throws NullPointerException if any of the parameters are {@code null}
     * @throws IllegalArgumentException if the name, types, imports or tag is blank
     */
    private AsnSchemaModule(
            String name, Map<String, AsnSchemaTypeDefinition> types, Map<String, String> imports) {
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
    public static Builder builder() {
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
    public String getName() {
        return name;
    }

    /**
     * Returns the type definition associated with the specified type name
     *
     * @param typeName name of the type. E.g. {@code "Document"}
     * @return the type definition associated with the specified type name or {@link
     *     AsnSchemaTypeDefinition#NULL} if no type definition is found
     */
    public AsnSchemaTypeDefinition getType(String typeName) {
        final AsnSchemaTypeDefinition type = types.get(typeName);
        return type != null ? type : AsnSchemaTypeDefinition.NULL;
    }

    /**
     * Returns the name of the imported module which contains the specified type name
     *
     * @param typeName the name of the type which has been imported
     * @return the name of the imported module which contains the specified type or an empty string
     *     if the type has not been imported
     */
    public String getImportedModuleFor(String typeName) {
        final String moduleName = imports.get(typeName);
        return Strings.nullToEmpty(moduleName);
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: Builder
    // -------------------------------------------------------------------------

    /** Builder for creating an {@link AsnSchemaModule} */
    public static class Builder {
        // -------------------------------------------------------------------------
        // CLASS VARIABLES
        // -------------------------------------------------------------------------

        /** class logger */
        private static final Logger logger = LoggerFactory.getLogger(Builder.class);

        /** our mechanism for visiting each AsnSchemaType to apply appropriate tags */
        private static final Tagger TAGGER = new Builder.Tagger();

        /** our mechanism for visiting each AsnSchemaType to check for duplicate tags */
        private static final DuplicateChecker DUPLICATE_CHECKER = new Builder.DuplicateChecker();

        // ---------------------------------------------------------------------
        // INSTANCE VARIABLES
        // ---------------------------------------------------------------------

        /** name of this module */
        private String name = "";

        /** name of the top level type defined in this module */
        private final Map<String, AsnSchemaTypeDefinition> types = Maps.newHashMap();

        /** all types imported by this module. Map is of form {typeName =&gt; importedModuleName} */
        // TODO ASN-132 - by storing in a Map we can't have the same typeName come from
        // multiple modules, which should be valid.
        private final Map<String, String> imports = Maps.newHashMap();

        // ---------------------------------------------------------------------
        // CONSTRUCTION
        // ---------------------------------------------------------------------

        /** Default constructor */
        private Builder() {
            // private constructor
        }

        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

        /**
         * Sets the name of the module
         *
         * @param name name of the module
         * @return this builder
         */
        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Stores a new type definition to this module. I.e. the specified type definition is found
         * in module.
         *
         * <p>If no top level type is set in this builder, it will default to the first type added
         * using this method.
         *
         * @param type type definition to add
         * @return this builder
         */
        public Builder addType(AsnSchemaTypeDefinition type) {
            types.put(type.getName(), type);
            return this;
        }

        /**
         * Adds a import statement mapping the specified type name to the supplied module
         *
         * @param typeName name of the type which is imported
         * @param moduleName name of the module the type is imported from (i.e. module it is defined
         *     in)
         * @return this builder
         */
        public Builder addImport(String typeName, String moduleName) {
            imports.put(typeName, moduleName);
            return this;
        }

        /**
         * Adds all the imports from the supplied map
         *
         * @param imports the imports defined in this module. This identifies the module that an
         *     imported type comes from. Map is of format {typeName =&gt; moduleName}
         * @return this builder
         */
        public Builder addImports(Map<String, String> imports) {
            this.imports.putAll(imports);
            return this;
        }

        /**
         * Creates an instance of {@link AsnSchemaModule} from the information in this builder
         *
         * @return an instance of {@link AsnSchemaModule}
         */
        public AsnSchemaModule build() {
            return new AsnSchemaModule(name, types, imports);
        }

        /**
         * Resolve all of the "placeholder" type definitions such that they point to actual types,
         * even across modules.
         *
         * @param otherModules the other {@link AsnSchemaModule.Builder} within the schema. They
         *     will be used to resolve imports as part of the final build.
         * @throws ParseException if there are imports that cannot be resolved
         */
        public void resolveTypes(Iterable<Builder> otherModules) throws ParseException {
            // convert to a Map so we can easily key off the name when doing
            // import resolution
            final ImmutableMap.Builder<String, AsnSchemaModule.Builder> builder =
                    ImmutableMap.builder();
            for (Builder moduleBuilder : otherModules) {
                builder.put(moduleBuilder.name, moduleBuilder);
            }

            Resolver resolver = this.new Resolver(builder.build());

            for (AsnSchemaTypeDefinition typeDefinition : types.values()) {
                logger.trace("Resolving Type {} in module {}", typeDefinition.getName(), this.name);
                typeDefinition.getType().accept(resolver);
            }
        }

        /**
         * Perform any AUTOMATIC tagging as needed, and if there are components with no tags then
         * give them the appropriate UNIVERSAL tag relevant to their respective type. Should not be
         * called until after {@link AsnSchemaModule.Builder#resolveTypes(Iterable)}
         *
         * @throws ParseException should not throw - needed due to the visitor implementation
         */
        public void performTagging() throws ParseException {
            for (AsnSchemaTypeDefinition typeDefinition : types.values()) {
                logger.trace(
                        "Perform Tagging for TypeDef {} in module {}",
                        typeDefinition.getName(),
                        this.name);

                typeDefinition.getType().accept(TAGGER);
            }
        }

        /**
         * Checks that the Components of a Constructed type do not have duplicate tags that would
         * cause ambiguous decoding. Should not be called until after {@link
         * AsnSchemaModule.Builder#performTagging()}
         *
         * @throws ParseException if duplicates are found
         */
        public void checkForDuplicates() throws ParseException {
            for (AsnSchemaTypeDefinition typeDefinition : types.values()) {
                logger.trace(
                        "Checking Duplicates for TypeDef {} in module {}",
                        typeDefinition.getName(),
                        this.name);

                typeDefinition.getType().accept(DUPLICATE_CHECKER);
            }
        }

        // -------------------------------------------------------------------------
        // INTERNAL CLASS: Builder.Resolver
        // -------------------------------------------------------------------------

        /**
         * Use a double dispatch to avoid instanceof so that we can "visit" each type in the
         * hierarchy of types of each Type in each Type Definition in a schema. We do this so that
         * we can resolve the Placeholder types, linking them up from the type name that they store
         * at the time of parsing to the actual AsnSchemaType that they are, which we need later for
         * tagging, decoding and validation.
         */
        private class Resolver implements AsnSchemaTypeVisitor<Void> {
            // ---------------------------------------------------------------------
            // INSTANCE VARIABLES
            // ---------------------------------------------------------------------

            /** the other modules within a schema, needed to resolve imports */
            private final Map<String, AsnSchemaModule.Builder> otherModules;

            // ---------------------------------------------------------------------
            // CONSTRUCTION
            // ---------------------------------------------------------------------

            /**
             * Default constructor
             *
             * @param otherModules the other {@link AsnSchemaModule.Builder
             *     AsnSchemaModule.Builders} within the schema. They will be used to resolve imports
             *     as part of the final build.
             */
            private Resolver(Map<String, AsnSchemaModule.Builder> otherModules) {
                this.otherModules = otherModules;
            }

            // ---------------------------------------------------------------------
            // PUBLIC METHODS
            // ---------------------------------------------------------------------

            @Override
            public Void visit(AsnSchemaTypeConstructed visitable) throws ParseException {
                // visit all the components to resolve them
                for (AsnSchemaComponentType componentType : visitable.getAllComponents()) {
                    logger.trace("Resolving types for {}", componentType.getName());
                    componentType.getType().accept(this);
                }

                return null;
            }

            @Override
            public Void visit(AsnSchemaTypePrimitive visitable) throws ParseException {
                return null;
            }

            @Override
            public Void visit(AsnSchemaTypePrimitiveAliased visitable) throws ParseException {
                // this is where we can see we have what we think is a leaf node, but is
                // something we should treat as a some other type.
                // eg someField OCTET STRING (CONTAINING otherType)

                final String moduleName = visitable.getModule();
                final String typeName = visitable.getType();

                // If there is a module specified then try import from that first.
                // Otherwise try locally, and if that doesn't find it then try the imports without
                // knowing from which module.
                final AsnSchemaTypeDefinition newTypeDefinition =
                        Strings.isNullOrEmpty(moduleName)
                                ? types.get(typeName)
                                : getImportedTypeDefinition(moduleName, typeName, otherModules);

                final AsnSchemaType newType = newTypeDefinition.getType();
                if (newType == AsnSchemaType.NULL) {
                    final String errorString =
                            String.format(
                                    "Unable to resolve placeholder.  TypeDefinition {%s} is badly formed",
                                    typeName);
                    logger.warn(errorString);
                    throw new ParseException(errorString, -1);
                }

                // we now have the actual type this placeholder was holding out for, so add it.
                visitable.setAliasedType(newType);
                return null;
            }

            @Override
            public Void visit(AsnSchemaTypeCollection visitable) throws ParseException {
                // visit the Element that we are "wrapping"
                visitable.getElementType().accept(this);
                return null;
            }

            @Override
            public Void visit(AsnSchemaTypeWithNamedTags visitable) throws ParseException {
                return null;
            }

            @Override
            public Void visit(AsnSchemaTypePlaceholder visitable) throws ParseException {
                // This is the real work - resolve this by finding the actual Type.

                final String moduleName = visitable.getModuleName();
                final String typeName = visitable.getTypeName();

                // If there is a module specified then try import from that first.
                // Otherwise try locally, and if that doesn't find it then try the imports without
                // knowing from which module.
                AsnSchemaTypeDefinition newTypeDefinition =
                        Strings.isNullOrEmpty(moduleName)
                                ? types.get(typeName)
                                : getImportedTypeDefinition(moduleName, typeName, otherModules);

                if (newTypeDefinition == null) {
                    // then it will come from an import.
                    newTypeDefinition =
                            getImportedTypeDefinition(moduleName, typeName, otherModules);
                }

                final AsnSchemaType newType = newTypeDefinition.getType();
                if (newType == AsnSchemaType.NULL) {
                    final String errorString =
                            String.format(
                                    "Unable to resolve placeholder.  TypeDefinition {%s} is badly formed",
                                    typeName);
                    logger.warn(errorString);
                    throw new ParseException(errorString, -1);
                }

                // we now have the actual type this placeholder was holding out for, so add it.
                visitable.setIndirectType(newType);

                return null;
            }

            @Override
            public Void visit(AsnSchemaType.Null visitable) throws ParseException {
                return null;
            }

            /**
             * Returns an {@link AsnSchemaTypeDefinition} from either the specified module, or by
             * looking up the appropriate module from the Imports.
             *
             * @param moduleName The name of the other Module to import from. May be null/empty.
             * @param typeName The name of the TypeDefinition to import
             * @param otherModules The other modules - ie the source of the imports
             * @return The AsnSchemaTypeDefinition for the imported type.
             * @throws ParseException ParseException if there are imports that cannot be resolved
             */
            private AsnSchemaTypeDefinition getImportedTypeDefinition(
                    String moduleName,
                    String typeName,
                    Map<String, AsnSchemaModule.Builder> otherModules)
                    throws ParseException {

                // figure out which module the place holder may have specified it, if not then we
                // need to look it up
                if (Strings.isNullOrEmpty(moduleName)) {
                    moduleName = imports.get(typeName);
                }

                if (Strings.isNullOrEmpty(moduleName)) {
                    final String errorString =
                            String.format(
                                    "Unable to resolve import of %s, it is not specified as an import",
                                    typeName);
                    logger.warn(errorString);
                    throw new ParseException(errorString, -1);
                }

                final Builder otherModule = otherModules.get(moduleName);
                if (otherModule == null) {
                    final String errorString =
                            String.format(
                                    "Unable to resolve import of %s, could not find module {%s}",
                                    typeName, moduleName);
                    logger.warn(errorString);
                    throw new ParseException(errorString, -1);
                }

                final AsnSchemaTypeDefinition newTypeDefinition = otherModule.types.get(typeName);
                if (newTypeDefinition == null) {
                    final String errorString =
                            String.format(
                                    "Unable to resolve import - could not find type %s in module %s",
                                    typeName, moduleName);
                    logger.warn(errorString);
                    throw new ParseException(errorString, -1);
                }

                return newTypeDefinition;
            }
        }

        /**
         * Use a double dispatch to "visit" each type in the hierarchy of types, allowing us to
         * provide functionality that is type specific. We need to visit each AsnSchemaType, in each
         * Type Definition in a schema. We do this so that we can calculate the tag for all
         * components. A Tag may have been provided in the schema otherwise it might be
         * Automatically tagged, otherwise is has a UNIVERSAL tag, which represents the underlying
         * ASN.1 type of the component. We may not know the type at the time we parse the item (it
         * is defined in terms of a Type Definition elsewhere, including another module).
         */
        private static class Tagger implements AsnSchemaTypeVisitor<Void> {
            // ---------------------------------------------------------------------
            // PUBLIC METHODS
            // ---------------------------------------------------------------------

            @Override
            public Void visit(AsnSchemaTypeConstructed visitable) throws ParseException {
                for (AsnSchemaComponentType component : visitable.getAllComponents()) {
                    logger.trace("tagging component {}", component.getName());
                    component.getType().accept(this);
                }

                // because some tags are not context-specific we need to know the
                // types of items to create Universal tags, which we may not know until all the
                // types (including across modules) have been resolved.
                visitable.performTagging();

                return null;
            }

            @Override
            public Void visit(AsnSchemaTypePrimitive visitable) throws ParseException {
                return null;
            }

            @Override
            public Void visit(AsnSchemaTypePrimitiveAliased visitable) throws ParseException {
                return null;
            }

            @Override
            public Void visit(AsnSchemaTypeCollection visitable) throws ParseException {
                visitable.getElementType().accept(this);

                visitable.performTagging();
                return null;
            }

            @Override
            public Void visit(AsnSchemaTypeWithNamedTags visitable) throws ParseException {
                return null;
            }

            @Override
            public Void visit(AsnSchemaTypePlaceholder visitable) throws ParseException {
                visitable.getIndirectType().accept(this);
                return null;
            }

            @Override
            public Void visit(AsnSchemaType.Null visitable) throws ParseException {
                return null;
            }
        }

        /**
         * Use a double dispatch to "visit" each type in the hierarchy of types, allowing us to
         * provide functionality that is type specific. We need to visit each AsnSchemaType, in each
         * Type Definition in a schema. We do this so that we can check for duplicate tags
         */
        private static class DuplicateChecker implements AsnSchemaTypeVisitor<Void> {
            // ---------------------------------------------------------------------
            // PUBLIC METHODS
            // ---------------------------------------------------------------------

            @Override
            public Void visit(AsnSchemaTypeConstructed visitable) throws ParseException {
                for (AsnSchemaComponentType component : visitable.getAllComponents()) {
                    logger.trace("Checking for duplicates for {}", component.getName());
                    component.getType().accept(this);
                }

                // because checking for duplicates involves knowing the tag of each component,
                // which is not fully set/known until after the TAGGER visit we will set in
                // another visit.
                visitable.checkForDuplicates();
                return null;
            }

            @Override
            public Void visit(AsnSchemaTypePrimitive visitable) throws ParseException {
                return null;
            }

            @Override
            public Void visit(AsnSchemaTypePrimitiveAliased visitable) throws ParseException {
                return null;
            }

            @Override
            public Void visit(AsnSchemaTypeCollection visitable) throws ParseException {
                visitable.getElementType().accept(this);
                return null;
            }

            @Override
            public Void visit(AsnSchemaTypeWithNamedTags visitable) throws ParseException {
                return null;
            }

            @Override
            public Void visit(AsnSchemaTypePlaceholder visitable) throws ParseException {
                visitable.getIndirectType().accept(this);
                return null;
            }

            @Override
            public Void visit(AsnSchemaType.Null visitable) throws ParseException {
                return null;
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
    public static class Null extends AsnSchemaModule {
        /**
         * Default constructor. Hidden, use {@link AsnSchemaModule#NULL} to obtain a singleton
         * instance.
         */
        private Null() {
            super("NULL", Maps.newHashMap(), Maps.newHashMap());
        }
    }
}
