/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.brightsparklabs.asanti.model.schema.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaFixedNumericValueConstraint;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaFixedSizeConstraint;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaNumericValueConstraint;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaSizeConstraint;
import com.google.common.base.Strings;

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
            Pattern.compile("\\(?\\s*SIZE\\s*\\(\\s*(\\d+)\\s*\\.\\.\\s*(\\d+)[^\\)]*\\)\\s*\\)?");

    /** pattern to match a fixed SIZE constraint */
    private static final Pattern PATTERN_FIXED_SIZE_CONSTRAINT =
            Pattern.compile("\\(?\\s*SIZE\\s*\\(\\s*(\\d+)\\s*\\)\\s*\\)?");

    /** pattern to match a bounded numeric value constraint */
    private static final Pattern PATTERN_NUMERIC_VALUE_CONSTRAINT =
            Pattern.compile("\\(?\\s*(\\d+)\\s*\\.\\.\\s*(\\d+)[^\\)]*\\)?");

    /** pattern to match a bounded numeric value constraint */
    private static final Pattern PATTERN_FIXED_NUMERIC_VALUE_CONSTRAINT = Pattern.compile("\\(?\\s*(\\d+)\\s*\\)?");

    /** error message if an unsupported constraint definition is found */
    private static final String ERROR_UNSUPPORTED_CONSTRAINT = "Unsupported constraint definition found: ";

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
        constraintText = Strings.nullToEmpty(constraintText)
                .trim();
        if (constraintText.isEmpty()) { return AsnSchemaConstraint.NULL; }

        log.log(Level.FINE, "Found constraint: {0}", constraintText);

        // check if defining a bounded SIZE constraint
        Matcher matcher = PATTERN_SIZE_CONSTRAINT.matcher(constraintText);
        if (matcher.matches()) { return parseSizeConstraint(matcher); }

        // check if defining a fixed SIZE constraint
        matcher = PATTERN_FIXED_SIZE_CONSTRAINT.matcher(constraintText);
        if (matcher.matches()) { return parseFixedSizeConstraint(matcher); }

        // check if defining a bounded numeric value constraint
        matcher = PATTERN_NUMERIC_VALUE_CONSTRAINT.matcher(constraintText);
        if (matcher.matches()) { return parseNumericValueConstraint(matcher); }

        // check if defining a fixed numeric value constraint
        matcher = PATTERN_FIXED_NUMERIC_VALUE_CONSTRAINT.matcher(constraintText);
        if (matcher.matches()) { return parseFixedNumericValueConstraint(matcher); }

        final String error = ERROR_UNSUPPORTED_CONSTRAINT + constraintText;
        // TODO ASN-96 - handle value assignment variables in constraints
        // e.g. (SIZE (1..maxNrOfPoints))
        // throw new ParseException(error, -1);
        log.warning(error);
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

    /**
     * Parses a SIZE constraint containing a fixed size
     *
     * @param matcher
     *            matcher which matched on
     *            {@link #PATTERN_FIXED_SIZE_CONSTRAINT}
     *
     * @return an {@link AsnSchemaFixedSizeConstraint} representing the parsed
     *         data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    private static AsnSchemaFixedSizeConstraint parseFixedSizeConstraint(Matcher matcher) throws ParseException
    {
        try
        {
            final int fixedBound = Integer.parseInt(matcher.group(1));
            return new AsnSchemaFixedSizeConstraint(fixedBound);
        }
        catch (final NumberFormatException ex)
        {
            final String error =
                    String.format("Could not convert the following strings into integers: %s", matcher.group(1));
            throw new ParseException(error, -1);
        }
    }

    /**
     * Parses a numeric value constraint containing an upper and lower bound
     *
     * @param matcher
     *            matcher which matched on
     *            {@link #PATTERN_NUMERIC_VALUE_CONSTRAINT}
     *
     * @return an {@link AsnSchemaNumericValueConstraint} representing the
     *         parsed data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    private static AsnSchemaNumericValueConstraint parseNumericValueConstraint(Matcher matcher) throws ParseException
    {
        try
        {
            final BigInteger minimumBound = new BigInteger(matcher.group(1));
            final BigInteger maximumBound = new BigInteger(matcher.group(2));
            return new AsnSchemaNumericValueConstraint(minimumBound, maximumBound);
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

    /**
     * Parses a numeric value constraint containing a fixed value
     *
     * @param matcher
     *            matcher which matched on
     *            {@link #PATTERN_FIXED_NUMERIC_VALUE_CONSTRAINT}
     *
     * @return an {@link AsnSchemaFixedNumericValueConstraint} representing the
     *         parsed data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    private static AsnSchemaFixedNumericValueConstraint parseFixedNumericValueConstraint(Matcher matcher)
            throws ParseException
    {
        try
        {
            final BigInteger fixedBound = new BigInteger(matcher.group(1));
            return new AsnSchemaFixedNumericValueConstraint(fixedBound);
        }
        catch (final NumberFormatException ex)
        {
            final String error =
                    String.format("Could not convert the following strings into integers: %s", matcher.group(1));
            throw new ParseException(error, -1);
        }
    }
}
