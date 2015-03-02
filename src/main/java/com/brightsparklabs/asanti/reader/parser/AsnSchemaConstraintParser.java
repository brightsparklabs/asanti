/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.brightsparklabs.asanti.model.schema.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaSizeConstraint;

/**
 * Logic for parsing Constraints from an {@link AsnSchemaTypeDefinition} or
 * {@link AsnSchemaComponentType}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaConstraintParser
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** pattern to match a bounded SIZE constraint */
    private static final Pattern PATTERN_SIZE_CONSTRAINT =
            Pattern.compile("\\s*SIZE\\s*\\((\\d+)\\s*\\.\\.\\s*(\\d+)\\s*\\)\\s*");

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger log = Logger.getLogger(AsnSchemaTypeDefinitionParser.class.getName());

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses the constraint from an {@link AsnSchemaTypeDefinition} or
     * {@link AsnSchemaComponentType}
     *
     * @param constraintText
     *            the constraint text as a string
     *
     * @return an {@link AsnSchemaConstraint} representing the constraint text
     *
     * @throws ParseException
     *             if any errors occur while parsing the data
     */
    public static AsnSchemaConstraint parse(String constraintText) throws ParseException
    {
        log.log(Level.FINE, "Found constraint: {0}", constraintText);

        // check if defining a bounded SIZE constraint
        final Matcher matcher = PATTERN_SIZE_CONSTRAINT.matcher(constraintText);
        if (matcher.matches()) { return parseSizeConstraint(matcher); }

        // TODO ASN-38 - parse constraint text
        return AsnSchemaConstraint.NULL;
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses a SIZE constraint containing an upper and lower bound
     *
     * @param matcher
     *            matcher which matched on {@link #PATTERN_SIZE_CONSTRAINT}
     *
     * @return an {@link AsnSchemaSizeConstraint} representing the parsed data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    private static AsnSchemaSizeConstraint parseSizeConstraint(Matcher matcher) throws ParseException
    {
        try
        {
            final int minimumBound = Integer.parseInt(matcher.group(1));
            final int maximumBound = Integer.parseInt(matcher.group(2));
            return new AsnSchemaSizeConstraint(minimumBound, maximumBound);
        }
        catch (final NumberFormatException ex)
        {
            final String error =
                    String.format("Could not convert the following strings into integers: %s, %s",
                            matcher.group(1),
                            matcher.group(2));
            throw new ParseException(error, -1);
        }
    }
}
