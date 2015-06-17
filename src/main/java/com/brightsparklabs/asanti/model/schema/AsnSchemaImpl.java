/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagType;
import com.brightsparklabs.asanti.model.schema.tagtype.AsnSchemaTagTypePlaceHolder;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypeCollection;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypeConstructed;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypePlaceholder;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;
import com.brightsparklabs.asanti.model.schema.typedefinition.OLDAsnSchemaTypeDefinitionConstructed;
import com.brightsparklabs.asanti.model.schema.typedefinition.OLDAsnSchemaTypeDefinitionCollectionOf;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.google.common.base.Preconditions.*;

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
    private static final Logger logger = LoggerFactory.getLogger(AsnSchemaModule.class);

    /** splitter for separating tag strings */
    private static final Splitter tagSplitter = Splitter.on("/").omitEmptyStrings();

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
     *         the primary module defined in this schema
     * @param modules
     *         all modules defined in this schema
     *
     * @throws NullPointerException
     *         if any of the parameters are {@code null}
     * @throws IllegalArgumentException
     *         if no modules are specified or the primary module is not contained in the modules
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
    public OperationResult<DecodedTag> getDecodedTag(String rawTag, String topLevelTypeName)
    {
        if (rawTag.equals("/2/0/2/5/1/1/2/0"))
        {
            int breakpoint = 0;
        }
        final ArrayList<String> tags = Lists.newArrayList(tagSplitter.split(rawTag));
        final DecodedTagsAndType decodedTagsAndType = decodeTags(tags.iterator(),
                topLevelTypeName,
                primaryModule);
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
                logger.debug("Unable to parse " + rawTag + " : " + unknownTag);
            }
        }

        // prefix result with top level type
        decodedTags.add(0, topLevelTypeName);
        decodedTags.add(0, ""); // empty string prefixes just the separator
        final String decodedTagPath = tagJoiner.join(decodedTags);

        final DecodedTag decodedTag = new DecodedTag(decodedTagPath,
                rawTag,
                decodedTagsAndType.type,
                decodedTagsAndType.allTypeDefinitions,
                decodeSuccessful);
        final OperationResult<DecodedTag> result = decodeSuccessful ?
                OperationResult.createSuccessfulInstance(decodedTag) :
                OperationResult.createUnsuccessfulInstance(decodedTag,
                        "The supplied raw tag does not map to a type in this schema");
        return result;
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the decoded tags for the supplied raw tags
     *
     * @param rawTags
     *         raw tags to decode. This should be an iterable in the order of the tags. E.g. The raw
     *         tag {code "/1/0/1"} should be provided as an iterator of {code ["1", "0", "1"]}
     * @param containingTypeName
     *         the type in this module from which to begin decoding the raw tag. E.g. {@code
     *         "Document"} will start decoding the raw tags from the type definition named {@code
     *         Document}
     * @param module
     *         module containing the type
     *
     * @return a list of all decoded tags. If a raw tag could not be decoded then processing stops.
     * E.g. for the raw tags {code "1", "0" "1", "99", "98"}, if the {@code "99"} raw tag cannot be
     * decoded, then a list containing the decoded tags for only the first three raw tags is
     * returned (e.g. {@code ["Header", "Published", "Date"]})
     */
    private DecodedTagsAndType decodeTags(Iterator<String> rawTags, String containingTypeName,
            AsnSchemaModule module)
    {
        String typeName = containingTypeName;
        final DecodedTagsAndType result = new DecodedTagsAndType();

        Set<AsnSchemaType> allTypeDefs = Sets.newHashSet();

        // The module stores all the TypeDefs defined within it.
        // this is now the Type of the tag's container (ie we are assuming this is in a SEQUENCE/Set etc)
        AsnSchemaTypeDefinition typeDefinition = module.getType(typeName);
        AsnSchemaType type = typeDefinition.getType();

        while (rawTags.hasNext())
        {
            if (Strings.isNullOrEmpty(typeName))
            {
                // no type to delve into
                break;
            }

            if (type == AsnSchemaType.NULL)
            {
                // type is not defined in module, test if it is imported
                final AsnSchemaModule importedModule = getImportedModuleFor(typeName, module);
                if (!AsnSchemaModule.NULL.equals(importedModule))
                {
                    // found the module the type is defined in
                    final DecodedTagsAndType tagsAndType = decodeTags(rawTags,
                            typeName,
                            importedModule);
                    result.type = tagsAndType.type;
                    result.decodedTags.addAll(tagsAndType.decodedTags);
                    // TODO MJF.  test this...
                }
                break;
            }

            // Get the tag
            final String tag = rawTags.next();


            if (type instanceof AsnSchemaTypeConstructed)
            {
                AsnSchemaTypeConstructed constructed = (AsnSchemaTypeConstructed)type;
                final String tagName = constructed.getTagName(tag);
                //final String tagName = type.getTagName(tag);

                AsnSchemaComponentType component = constructed.getComponent(tag);

                if (Strings.isNullOrEmpty(tagName))
                {
                    // unknown tag
                    result.type = AsnSchemaType.NULL;
                    break;
                }

                AsnSchemaType tagType = component.getType();

                if (tagType instanceof AsnSchemaTypePlaceholder)
                {
                    // we only want the 'chain' of the type of the tag, not anything of the parent
                    // structures.
                    if (!rawTags.hasNext())
                    {
                        allTypeDefs.add(tagType);
                    }
                    typeName = component.getTypeName();
                    typeDefinition = module.getType(typeName);
                    if (typeDefinition == AsnSchemaTypeDefinition.NULL)
                    {
                        // type is not defined in module, test if it is imported
                        final AsnSchemaModule importedModule = getImportedModuleFor(typeName, module);
                        if (!AsnSchemaModule.NULL.equals(importedModule))
                        {
                            // TODO MJF - this is not working because the Iterator was already advanced, we
                            // needed to have called this before the rawTags.next()

                            // found the module the type is defined in
                            final DecodedTagsAndType tagsAndType = decodeTags(rawTags,
                                    typeName,
                                    importedModule);
                            result.type = tagsAndType.type;
                            result.decodedTags.addAll(tagsAndType.decodedTags);
                            // TODO MJF.  test this...
                        }
                        break;
                    }

                    if (typeDefinition != null)
                    {
                        tagType = typeDefinition.getType();
                    }
                    else
                    {
                        tagType = AsnSchemaType.NULL;
                    }
                }

                // TODO MJF - is this true???
                // We want collections types to be transparent.
                if (tagType instanceof AsnSchemaTypeCollection)
                {
                    tagType = ((AsnSchemaTypeCollection)tagType).getElementType();
                }

                // TODO MJF - this is stupid!  but the "transparent" replacement above for the
                // collection may have given us another placeholder.
                // We need to do a sweep at the end of schema parsing to get rid of all placeholders!!!
                if (tagType instanceof AsnSchemaTypePlaceholder)
                {
                    // we only want the 'chain' of the type of the tag, not anything of the parent
                    // structures.
                    if (!rawTags.hasNext())
                    {
                        allTypeDefs.add(tagType);
                    }

                    //typeDefinition = getTypeDefinition((AsnSchemaTypePlaceholder)tagType, module);

                    typeName = component.getTypeName();
                    typeDefinition = module.getType(typeName);
                    if (typeDefinition != null)
                    {
                        tagType = typeDefinition.getType();
                    }
                    else
                    {
                        tagType = AsnSchemaType.NULL;
                    }
                }


                result.allTypeDefinitions = ImmutableSet.copyOf(allTypeDefs);
                result.type = tagType;
                result.decodedTags.add(tagName);

                // Get the next type.
                type = tagType;

                // TODO MJF.  In theory the next type is tagType...  By now storing the "generated" types directly
                // we don't need to do the module lookup.  The only time we now need that is for Placeholder and import.
                /*
                typeName = component.getTypeName();
                typeDefinition = module.getType(typeName);
                if (typeDefinition != AsnSchemaTypeDefinition.NULL)
                {
                    type = typeDefinition.getType();
                }
                else
                {
                    type = AsnSchemaType.NULL;
                }
                */

            }
            else if (type instanceof AsnSchemaTypeCollection)
            {
                // TODO MJF -
                AsnSchemaTypeCollection
                        collection = (AsnSchemaTypeCollection)type;

                final String tagName = tag;// type.getTagName(tag);
                type = collection.getElementType();

                result.allTypeDefinitions = ImmutableSet.copyOf(allTypeDefs);
                result.type = AsnSchemaType.NULL;
                result.decodedTags.add(tagName);
            }
            else
            {
                // This is NOT a container type, so we can't go any further down - this is the end of the line!
                type = AsnSchemaType.NULL;
            }
        }

        return result;
    }

/*
    private AsnSchemaTypeDefinition getTypeDefinition(AsnSchemaTypePlaceholder placeholder, AsnSchemaModule module)
    {
        String moduleName = placeholder.getModuleName();
        String typeName = placeholder.getTypeName();

        AsnSchemaTypeDefinition typeDefinition = module.getType(typeName);
        if (typeDefinition == AsnSchemaTypeDefinition.NULL)
        {

        }

    }
*/
/*
    private DecodedTagsAndType decodeTags(Iterator<String> rawTags, String containingTypeName,
            AsnSchemaModule module)
    {
        String typeName = containingTypeName;
        final DecodedTagsAndType result = new DecodedTagsAndType();

        Set<AsnSchemaTagType> allTypeDefs = Sets.newHashSet();

        // The module stores all the TypeDefs defined within it.
        // this is now the Type of the tag's container (ie we are assuming this is in a SEQUENCE/Set etc)
        AsnSchemaTypeDefinition type = module.getType(typeName);

        while (rawTags.hasNext())
        {
            if (Strings.isNullOrEmpty(typeName))
            {
                // no type to delve into
                break;
            }

            if (type == OLDAsnSchemaTypeDefinition.NULL)
            {
                // type is not defined in module, test if it is imported
                final AsnSchemaModule importedModule = getImportedModuleFor(typeName, module);
                if (!AsnSchemaModule.NULL.equals(importedModule))
                {
                    // found the module the type is defined in
                    final DecodedTagsAndType tagsAndType = decodeTags(rawTags,
                            typeName,
                            importedModule);
                    result.type = tagsAndType.type;
                    result.decodedTags.addAll(tagsAndType.decodedTags);
                    // TODO MJF.  test this...
                }
                break;
            }

            // Get the tag
            final String tag = rawTags.next();


            // TODO - the reality is that getTagName is not a generic type function, it is only really
            // a function for Container types (sequence, set etc.)
            // What we are doing here is asking a Container type for the Component based on the tag

            // What this is trying to do is follow the sequence of raw tags /0/1/2 etc and map that to a set
            // of names /Human/name/first AND to provide the Type of that last one
            // It is only sensible to have a "lower" tag if the "parent" is a Container type (SEQUENCE, set etc)


            // So instead of dealing with "generic" types we could determine whether something is a container, and if it is
            // then ask it for the appropriate Component.
            // Something like:
            if (type instanceof AsnSchemaTypeDefinitionConstructed)
            {
                AsnSchemaTypeDefinitionConstructed
                        constructed = (AsnSchemaTypeDefinitionConstructed)type;
                //final String tagName = constructed.getTagName(tag);
                final String tagName = type.getTagName(tag);

                AsnSchemaComponentType component = constructed.getComponent(tag);

                if (Strings.isNullOrEmpty(tagName))
                {
                    // unknown tag
                    result.type = AsnSchemaTypeDefinition.NULL;
                    break;
                }

                AsnSchemaTagType tagType = component.getType();

                // TODO MJF.  This is just temporary.  We should do this scan for all PlaceHolders
                // as part of the schema parsing.  But just to test the concept, lets try here...
                // Also TypeDefs may be indirect, eg in a SEQUENCE we may have:
                //    age MyAge
                // then
                //  MyAge ::= ShortInt (1..200)
                //  ShortInt ::= INTEGER (1..32767)

                if (tagType instanceof AsnSchemaTagTypePlaceHolder)
                {
                    // we only want the 'chain' of the type of the tag, not anything of the parent
                    // structures.
                    if (!rawTags.hasNext())
                    {
                        allTypeDefs.add(tagType);
                    }
                    typeName = component.getTypeName();
                    tagType = module.getType(typeName);
                }



                result.allTypeDefinitions = ImmutableSet.copyOf(allTypeDefs);
                result.type = tagType;//type;
                result.decodedTags.add(tagName);

                // TODO - MJF.  We need to prepare for the next go round the while loop (ie the next
                // level down in the chain). So either the component need to be able to give us that
                // child, or we need to be able to get it from somewhere else.  Previously this was
                // what module.getType did.
                // Get the next type.
                typeName = component.getTypeName();
                type = module.getType(typeName);


            }
            else if (type instanceof OLDAsnSchemaTypeDefinitionCollectionOf)
            {
                // TODO MJF -
                OLDAsnSchemaTypeDefinitionCollectionOf
                        collection = (OLDAsnSchemaTypeDefinitionCollectionOf)type;

                final String tagName = tag;// type.getTagName(tag);
                typeName = collection.getTypeName(tag);

                type = module.getType(typeName);

                result.allTypeDefinitions = ImmutableSet.copyOf(allTypeDefs);
                result.type = OLDAsnSchemaTypeDefinition.NULL;
                result.decodedTags.add(tagName);
            }
            else
            {
                // This is NOT a container type, so we can't go any further down - this is the end of the line!
                type = OLDAsnSchemaTypeDefinition.NULL;
            }


            // TODO - Now we need to go back to the parser and ensure that the things that are
            // components end up in this list, and not just the anonymouse sequences, set etc
            // There are a couple of steps to do that.
            // 1/ It is in private static ImmutableList<OLDAsnSchemaTypeDefinition> parseSequence
            //    in AsnSchemaTypeDefinitionParser where we are collecting all these types for the
            //    components.  So it needs to collect more than the anon sequences
            // 2/ It is still using OLDAsnSchemaTypeDefinition, so we need to get this to use the base
            //    which is AsnSchemaTagType.

        }

        return result;
    }
*/
/*
    private DecodedTagsAndType decodeTags(Iterator<String> rawTags, String containingTypeName,
            AsnSchemaModule module)
    {
        String typeName = containingTypeName;
        OLDAsnSchemaTypeDefinition type = OLDAsnSchemaTypeDefinition.NULL;
        final DecodedTagsAndType result = new DecodedTagsAndType();

        while (rawTags.hasNext())
        {
            if (Strings.isNullOrEmpty(typeName))
            {
                // no type to delve into
                break;
            }

            type = module.getType(typeName);
            if (type == OLDAsnSchemaTypeDefinition.NULL)
            {
                // type is not defined in module, test if it is imported
                final AsnSchemaModule importedModule = getImportedModuleFor(typeName, module);
                if (!AsnSchemaModule.NULL.equals(importedModule))
                {
                    // found the module the type is defined in
                    final DecodedTagsAndType tagsAndType = decodeTags(rawTags,
                            typeName,
                            importedModule);
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
                result.type = OLDAsnSchemaTypeDefinition.NULL;
                break;
            }

            result.type = type;
            // TODO - at this point (for the first time through the loop) 'type' is still the type
            // of the containingTypeName, NOT of this tag.



            result.decodedTags.add(tagName);
            typeName = type.getTypeName(tag);
        }

        return result;
    }

*/
    /**
     * Returns the imported module which contains the specified type module.
     *
     * @param typeName
     *         the type in the <b>imported</b> module
     * @param module
     *         the module which imported the specified type
     *
     * @return the imported module which contains the specified type
     */
    private AsnSchemaModule getImportedModuleFor(String typeName, AsnSchemaModule module)
    {
        // not found locally, check if it is from an import
        final String importedModuleName = module.getImportedModuleFor(typeName);
        if (Strings.isNullOrEmpty(importedModuleName))
        {
            logger.warn(
                    "Could not resolve type definition \"{}\". It is is not defined or imported in module \"{}\"",
                    typeName,
                    module.getName());
            return AsnSchemaModule.NULL;
        }

        final AsnSchemaModule importedModule = modules.get(importedModuleName);
        // ensure we do not recursively look into the current module
        if (importedModule == null || importedModule.equals(module))
        {
            logger.warn(
                    "Could not resolve type definition \"{}\". Type is imported from an unknown module \"{}\"",
                    typeName,
                    module.getName());
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
        //private OLDAsnSchemaTypeDefinition type = OLDAsnSchemaTypeDefinition.NULL;
        //private AsnSchemaTagType type = OLDAsnSchemaTypeDefinition.NULL;
        private AsnSchemaType type = null;//AsnSchemaType.NULL;

        private ImmutableSet<AsnSchemaType> allTypeDefinitions = ImmutableSet.<AsnSchemaType>of();
    }
}
