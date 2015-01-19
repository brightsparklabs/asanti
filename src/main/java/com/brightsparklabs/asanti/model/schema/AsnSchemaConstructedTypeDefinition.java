/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

import static com.google.common.base.Preconditions.*;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * A 'constructed' (SET, SEQUENCE, SET OF, SEQUENCE OF, CHOICE or ENUMERATED)
 * type definition from a within a module specification within an ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaConstructedTypeDefinition extends AsnSchemaTypeDefinition
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger log = Logger.getLogger(AsnSchemaConstructedTypeDefinition.class.getName());

    /**
     * built-in types which are considered 'constructed'. Currently: SET,
     * SEQUENCE, SET OF, SEQUENCE OF, CHOICE or ENUMERATED
     */
    public static final ImmutableSet<AsnBuiltinType> validTypes =
            ImmutableSet.of(AsnBuiltinType.Set, AsnBuiltinType.Sequence, AsnBuiltinType.SetOf, AsnBuiltinType.SequenceOf, AsnBuiltinType.Choice, AsnBuiltinType.Enumerated);

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** mapping from raw tag to component type */
    private final ImmutableMap<String, AsnSchemaComponentType> tagsToComponentTypes;

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
     *
     * @throws NullPointerException
     *             if {@code name}, {@code type} or {@code componentTypes} are
     *             {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} is blank or {@code type} is not one of the
     *             valid types defined in {@link #validTypes}
     */
    public AsnSchemaConstructedTypeDefinition(String name, AsnBuiltinType type,
            Iterable<AsnSchemaComponentType> componentTypes)
    {
        super(name, type);
        checkArgument(validTypes.contains(type), "Type must be either SET, SEQUENCE, SET OF, SEQUENCE OF, CHOICE or ENUMERATED");
        checkNotNull(componentTypes);

        final ImmutableMap.Builder<String, AsnSchemaComponentType> tagsToComponentTypesBuilder = ImmutableMap.builder();

        // next expected tag is used to generate tags for automatic tagging
        // TODO ensure that generating for all missing tags is correct and safe
        int nextExpectedTag = 0;

        for (final AsnSchemaComponentType componentType : componentTypes)
        {
            String tag = componentType.getTag();
            if (Strings.isNullOrEmpty(tag))
            {
                tag = String.valueOf(nextExpectedTag);
                log.log(Level.FINE, "Generated automatic tag [{0}] for {1}", new Object[] { tag,
                        componentType.getTagName() });
                nextExpectedTag++;
            }
            else
            {
                nextExpectedTag = Integer.parseInt(tag) + 1;
            }
            tagsToComponentTypesBuilder.put(tag, componentType);
        }
        tagsToComponentTypes = tagsToComponentTypesBuilder.build();
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
