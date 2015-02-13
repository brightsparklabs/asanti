/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
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

/**
 * Default implementation of {@link AsnSchema}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaImpl implements AsnSchema
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger log = Logger.getLogger(AsnSchemaModule.class.getName());

    /** splitter for separating tag strings */
    private static final Splitter tagSplitter = Splitter.on("/")
            .omitEmptyStrings();

    /** joiner for creating tag strings */
    private static final Joiner tagJoiner = Joiner.on("/");

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
        checkArgument(modules.containsKey(primaryModule),
                "The primary module must be contained in the schema's modules");

        this.primaryModule = modules.get(primaryModule);
        this.modules = ImmutableMap.copyOf(modules);
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchema
    // -------------------------------------------------------------------------

    @Override
    public DecodeResult<DecodedTag> getDecodedTag(String rawTag, String topLevelTypeName)
    {
        final ArrayList<String> tags = Lists.newArrayList(tagSplitter.split(rawTag));
        final DecodedTagsAndType decodedTagsAndType = decodeTags(tags.iterator(), topLevelTypeName, primaryModule);
        final List<String> decodedTags = decodedTagsAndType.decodedTags;

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
        final String decodedTagPath = tagJoiner.join(decodedTags);

        final DecodedTag decodedTag = new DecodedTag(decodedTagPath, rawTag, decodedTagsAndType.type, decodeSuccessful);
        final DecodeResult<DecodedTag> result = DecodeResult.create(decodedTag.isFullyDecoded(), decodedTag);
        return result;
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
     * @param module
     *            module containing the type
     *
     * @return a list of all decoded tags. If a raw tag could not be decoded
     *         then processing stops. E.g. for the raw tags {code "1", "0" "1",
     *         "99", "98"}, if the {@code "99"} raw tag cannot be decoded, then
     *         a list containing the decoded tags for only the first three raw
     *         tags is returned (e.g. {@code ["Header", "Published", "Date"]})
     */
    private DecodedTagsAndType decodeTags(Iterator<String> rawTags, String containingTypeName, AsnSchemaModule module)
    {
        String typeName = containingTypeName;
        AsnSchemaTypeDefinition type = AsnSchemaTypeDefinition.NULL;
        final DecodedTagsAndType result = new DecodedTagsAndType();

        while (rawTags.hasNext())
        {
            if (Strings.isNullOrEmpty(typeName))
            {
                // no type to delve into
                break;
            }

            type = module.getType(typeName);
            if (type == AsnSchemaTypeDefinition.NULL)
            {
                // type is not defined in module, test if it is imported
                final AsnSchemaModule importedModule = getImportedModuleFor(typeName, module);
                if (!AsnSchemaModule.NULL.equals(importedModule))
                {
                    // found the module the type is defined in
                    final DecodedTagsAndType tagsAndType = decodeTags(rawTags, typeName, importedModule);
                    result.type = tagsAndType.type;
                    result.decodedTags.addAll(tagsAndType.decodedTags);
                }
                break;
            }

            final String tag = rawTags.next();
            final String tagName = type.getTagName(tag);
            if (Strings.isNullOrEmpty(tagName))
            {
                // unknown tag
                result.type = AsnSchemaTypeDefinition.NULL;
                break;
            }
            result.type = type;
            result.decodedTags.add(tagName);
            typeName = type.getTypeName(tag);
        }

        return result;
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
    private AsnSchemaModule getImportedModuleFor(String typeName, AsnSchemaModule module)
    {
        // not found locally, check if it is from an import
        final String importedModuleName = module.getImportedModuleFor(typeName);
        if (Strings.isNullOrEmpty(importedModuleName))
        {
            log.log(Level.WARNING,
                    "Could not resolve type definition \"{0}\". It is is not defined or imported in module \"{1}\"",
                    new Object[] { typeName, module.getName() });
            return AsnSchemaModule.NULL;
        }

        final AsnSchemaModule importedModule = modules.get(importedModuleName);
        // ensure we do not recursively look into the current module
        if (importedModule == null || importedModule.equals(module))
        {
            log.log(Level.WARNING,
                    "Could not resolve type definition \"{0}\". Type is imported from an unknown module \"{1}\"",
                    new Object[] { typeName, module.getName() });
            return AsnSchemaModule.NULL;
        }

        return importedModule;
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASS: DecodedTagAndType
    // -------------------------------------------------------------------------

    /**
     * Transfer object to support returning a tuple of "Decoded Tags" and "Type"
     *
     * @author brightSPARK Labs
     */
    private static class DecodedTagsAndType
    {
        /** list of decoded tags */
        private final List<String> decodedTags = Lists.newArrayList();
        /** the type of the final tag */
        private AsnSchemaTypeDefinition type = AsnSchemaTypeDefinition.NULL;
    }
}
