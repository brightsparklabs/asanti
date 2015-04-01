/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema.typedefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A ENUMERATED type definition from a within a module specification within an
 * ASN.1 schema.
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionEnumerated extends AbstractAsnSchemaTypeDefinition
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(AsnSchemaTypeDefinitionEnumerated.class);

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** mapping from raw tag to enumerated option */
    private final ImmutableMap<String, AsnSchemaNamedTag> tagsToOptions;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * @param name
     *            name of the ENUMERATED type definition
     *
     * @param options
     *            the options in this ENUMERATED type definition
     *
     * @throws NullPointerException
     *             if {@code name}, or {@code options} are {@code null}
     *
     * @throws IllegalArgumentException
     *             if {@code name} is blank
     */
    public AsnSchemaTypeDefinitionEnumerated(String name, Iterable<AsnSchemaNamedTag> options)
    {
        super(name, AsnBuiltinType.Enumerated, AsnSchemaConstraint.NULL);
        checkNotNull(options);

        final ImmutableMap.Builder<String, AsnSchemaNamedTag> tagsToOptionsBuilder = ImmutableMap.builder();

        // next expected tag is used to generate tags for automatic tagging
        // TODO ASN-80 - ensure that generating for all missing tags is correct
        int nextExpectedTag = 0;

        for (final AsnSchemaNamedTag option : options)
        {
            String tag = option.getTag();
            if (Strings.isNullOrEmpty(tag))
            {
                tag = String.valueOf(nextExpectedTag);
                logger.debug("Generated automatic tag [{}] for {}", tag, option.getTagName());
                nextExpectedTag++;
            }
            else
            {
                nextExpectedTag = Integer.parseInt(tag) + 1;
            }
            tagsToOptionsBuilder.put(tag, option);
        }
        tagsToOptions = tagsToOptionsBuilder.build();
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AsnSchemaTypeDefinition
    // -------------------------------------------------------------------------

    @Override
    public String getTagName(String tag)
    {
        final AsnSchemaNamedTag option = tagsToOptions.get(tag);
        return (option == null) ? "" : option.getTagName();
    }

    @Override
    public String getTypeName(String tag)
    {
        return "";
    }

    @Override
    public Object visit(AsnSchemaTypeDefinitionVisitor<?> visitor)
    {
        return visitor.visit(this);
    }
}
