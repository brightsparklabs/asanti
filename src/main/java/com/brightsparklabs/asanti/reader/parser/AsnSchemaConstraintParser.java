/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import java.math.BigInteger;
import java.text.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaExactNumericValueConstraint;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaExactSizeConstraint;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaNumericValueConstraint;
import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaSizeConstraint;
import com.brightsparklabs.asanti.model.schema.typedefinition.AbstractOLDAsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaComponentType;
import com.google.common.base.Strings;

/**
 * Logic for parsing Constraints from an {@link AbstractOLDAsnSchemaTypeDefinition} or
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

    /** pattern to match an exact SIZE constraint */
    private static final Pattern PATTERN_EXACT_SIZE_CONSTRAINT =
            Pattern.compile("\\(?\\s*SIZE\\s*\\(\\s*(\\d+)\\s*\\)\\s*\\)?");

    /** pattern to match a bounded numeric value constraint */
    private static final Pattern PATTERN_NUMERIC_VALUE_CONSTRAINT =
            Pattern.compile("\\(?\\s*(\\d+)\\s*\\.\\.\\s*(\\d+)[^\\)]*\\)?");

    /** pattern to match an exact numeric value constraint */
    private static final Pattern PATTERN_EXACT_NUMERIC_VALUE_CONSTRAINT = Pattern.compile("\\(?\\s*(\\d+)\\s*\\)?");

    /** error message if an unsupported constraint definition is found */
    private static final String ERROR_UNSUPPORTED_CONSTRAINT = "Unsupported constraint definition found: ";

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(AsnSchemaTypeDefinitionParser.class);

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses the constraint from an {@link AbstractOLDAsnSchemaTypeDefinition} or
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

        logger.debug("Found constraint: {}", constraintText);

        // check if defining a bounded SIZE constraint
        Matcher matcher = PATTERN_SIZE_CONSTRAINT.matcher(constraintText);
        if (matcher.matches()) { return parseSizeConstraint(matcher); }

        // check if defining a exact SIZE constraint
        matcher = PATTERN_EXACT_SIZE_CONSTRAINT.matcher(constraintText);
        if (matcher.matches()) { return parseExactSizeConstraint(matcher); }

        // check if defining a bounded numeric value constraint
        matcher = PATTERN_NUMERIC_VALUE_CONSTRAINT.matcher(constraintText);
        if (matcher.matches()) { return parseNumericValueConstraint(matcher); }

        // check if defining a exact numeric value constraint
        matcher = PATTERN_EXACT_NUMERIC_VALUE_CONSTRAINT.matcher(constraintText);
        if (matcher.matches()) { return parseExactNumericValueConstraint(matcher); }

        final String error = ERROR_UNSUPPORTED_CONSTRAINT + constraintText;
        // TODO ASN-96 - handle value assignment variables in constraints
        // e.g. (SIZE (1..maxNrOfPoints))
        // throw new ParseException(error, -1);
        logger.warn(error);
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
            final int minimumLength = Integer.parseInt(matcher.group(1));
            final int maximumLength = Integer.parseInt(matcher.group(2));
            return new AsnSchemaSizeConstraint(minimumLength, maximumLength);
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
     * Parses a SIZE constraint containing an exact size
     *
     * @param matcher
     *            matcher which matched on
     *            {@link #PATTERN_EXACT_SIZE_CONSTRAINT}
     *
     * @return an {@link AsnSchemaExactSizeConstraint} representing the parsed
     *         data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    private static AsnSchemaExactSizeConstraint parseExactSizeConstraint(Matcher matcher) throws ParseException
    {
        try
        {
            final int fixedLength = Integer.parseInt(matcher.group(1));
            return new AsnSchemaExactSizeConstraint(fixedLength);
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
            final BigInteger minimumValue = new BigInteger(matcher.group(1));
            final BigInteger maximumValue = new BigInteger(matcher.group(2));
            return new AsnSchemaNumericValueConstraint(minimumValue, maximumValue);
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
     * Parses a numeric value constraint containing an exact value
     *
     * @param matcher
     *            matcher which matched on
     *            {@link #PATTERN_EXACT_NUMERIC_VALUE_CONSTRAINT}
     *
     * @return an {@link AsnSchemaExactNumericValueConstraint} representing the
     *         parsed data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    private static AsnSchemaExactNumericValueConstraint parseExactNumericValueConstraint(Matcher matcher)
            throws ParseException
    {
        try
        {
            final BigInteger exactValue = new BigInteger(matcher.group(1));
            return new AsnSchemaExactNumericValueConstraint(exactValue);
        }
        catch (final NumberFormatException ex)
        {
            final String error =
                    String.format("Could not convert the following strings into integers: %s", matcher.group(1));
            throw new ParseException(error, -1);
        }
    }
}
