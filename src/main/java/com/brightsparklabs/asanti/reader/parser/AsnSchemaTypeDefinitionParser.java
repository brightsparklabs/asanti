/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypeSequenceOf;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveTypeUtf8String;
import com.brightsparklabs.asanti.model.schema.type.AbstractAsnSchemaType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypeCollection;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaTypeConstructed;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Logic for parsing a Type Definition from a module within an ASN.1 schema
 *
 * @author brightSPARK Labs
 */
public final class AsnSchemaTypeDefinitionParser
{

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(AsnSchemaTypeDefinitionParser.class
            .getName());

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Private constructor. There should be no need to ever instantiate this static class.
     */
    private AsnSchemaTypeDefinitionParser()
    {
        throw new AssertionError();
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses a type definition from a module from an ASN.1 schema
     *
     * @param name
     *         the name of the defined type (i.e. the text on the left hand side of the {@code
     *         ::=})
     * @param value
     *         the value of the defined type (i.e. the text on the right hand side of the {@code
     *         ::=})
     *
     * @return an ImmutableList of {@link AsnSchemaTypeDefinition} objects representing the parsed
     * type definitions
     *
     * @throws ParseException
     *         if either of the parameters are {@code null}/empty or any errors occur while parsing
     *         the type
     */
    public static ImmutableList<AsnSchemaTypeDefinition> parse(String name, String value)
            throws ParseException
    {
        // TODO MJF - not sure we still need to be returning a list of types.
        // any "generated" types are now scoped internally and are not Type Definitions

        logger.debug("Found type definition: {} = {}", name, value);
        if (name == null || name.trim().isEmpty())
        {
            throw new ParseException("A name must be supplied for a Type Definition", -1);
        }
        if (value == null || value.trim().isEmpty())
        {
            throw new ParseException("A value must be supplied for a Type Definition", -1);
        }


        // Get the underlying type
        AsnSchemaType type = AsnSchemaTypeParser.parse(value);
        logger.debug("\t{} is type {}", name, type.getPrimitiveType().getBuiltinType());
        // wrap it with the typedefinition name.
        return ImmutableList.<AsnSchemaTypeDefinition>of(new AsnSchemaTypeDefinitionImpl(name, type))  ;

    }

}
