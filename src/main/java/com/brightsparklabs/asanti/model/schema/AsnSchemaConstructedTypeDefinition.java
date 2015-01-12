/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/**
 * A 'constructed' (SET, SEQUENCE, SET OF, SEQUENCE OF, CHOICE or ENUMERATED)
 * type definition from a within a module specification within an ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaConstructedTypeDefinition extends AsnSchemaTypeDefinition<ImmutableList>
{

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger log = Logger.getLogger(AsnSchemaConstructedTypeDefinition.class.getName());

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** mapping from raw tag to component type */
    private final ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes;

    /** mapping from tag name to component type */
    private final ImmutableMap<String, AsnSchemaComponentType> tagNamesToComponentTypes;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name
     *            name of the defined type
     *
     * @param type
     *            the underlying ASN.1 type of the defined type
     *
     * @param componentTypes
     *            the component types within this defined type
     */
    public AsnSchemaConstructedTypeDefinition(String name, AsnBuiltinType type,
            Iterable<AsnSchemaComponentType> componentTypes)
    {
        super(name, type);

        final ImmutableMap.Builder<String, AsnSchemaComponentType> tagsToTypesBuilder = ImmutableMap.builder();
        final ImmutableMap.Builder<String, AsnSchemaComponentType> tagNamesToTypesBuilder = ImmutableMap.builder();

        // next expected tag is used to generate tags for automatic tagging
        // TODO ensure that generating for all missing tags is correct and safe
        int nextExpectedTag = 0;

        for (AsnSchemaComponentType componentType : componentTypes)
        {
            String tag = componentType.getTag();
            if (tag == null)
            {
                tag = String.valueOf(nextExpectedTag);
                log.log(Level.FINE,
                        "Generated automatic tag [{0}] for {1}",
                        new Object[] { tag, componentType.getTagName() });
                nextExpectedTag++;
            }
            else
            {
                nextExpectedTag = Integer.parseInt(tag) + 1;
            }
            tagsToTypesBuilder.put(tag, componentType);
            tagsToTypesBuilder.put(componentType.getTagName(), componentType);
        }
        tagsToComponentTypes = tagsToTypesBuilder.build();
        tagNamesToComponentTypes = tagNamesToTypesBuilder.build();
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaTypeDefinition
    // -------------------------------------------------------------------------

    @Override
    public String getTagName(String tag)
    {
        final AsnSchemaComponentType componentType = tagsToComponentTypes.get(tag);
        return (componentType == null) ? "" : componentType.getTagName();
    }

    @Override
    public String getTypeName(String tag)
    {
        final AsnSchemaComponentType componentType = tagsToComponentTypes.get(tag);
        return (componentType == null) ? "" : componentType.getTypeName();
    }
}
