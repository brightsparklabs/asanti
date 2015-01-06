/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import java.text.ParseException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.brightsparklabs.asanti.model.schema.AsnSchemaModule;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;

/**
 * Logic for parsing a module within an ASN.1 schema
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaModuleParser
{
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** pattern to match a type definition */
    private static final Pattern PATTERN_TYPE_DEFINITION = Pattern.compile("^(([A-Za-z0-9\\-]+(\\{[A-Za-z0-9\\-:, ]+\\})?)+) ?::= ?(.+)");

    /** pattern to match a value assignment */
    private static final Pattern PATTERN_VALUE_ASSIGNMENT = Pattern.compile("^(([A-Za-z0-9\\-]+(\\{[A-Za-z0-9\\-:, ]+\\})?)+( [A-Za-z0-9\\-]+)+) ?::= ?(.+)");

    /** error message if schema is missing header keywords */
    private static final String ERROR_MISSING_HEADERS = "Schema does not contain all expected module headers";

    /** error message if schema is missing content */
    private static final String ERROR_MISSING_CONTENT = "Schema does not contain any information within the 'BEGIN' and 'END' keywords";

    /** error message if a type definition or value assignment is not found */
    private static final String ERROR_UNKNOWN_CONTENT = "Parser expected a type definition or value assignment but found: ";

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger log = Logger.getLogger(AsnSchemaModuleParser.class.getName());

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses the supplied module from an ASN.1 schema
     *
     * @param moduleText
     *            all text from module within the ASN.1 schema
     *
     * @throws ParseException
     *             if any errors occur while parsing the module
     *
     */
    public static AsnSchemaModule parse(Iterable<String> moduleText) throws ParseException
    {
        final Iterator<String> iterator = moduleText.iterator();
        final AsnSchemaModule.Builder moduleBuilder = AsnSchemaModule.builder();
        parseModuleHeader(iterator, moduleBuilder);
        parseContent(iterator, moduleBuilder);
        final AsnSchemaModule module = moduleBuilder.build();
        return module;
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses the module header. This data is located before the 'BEGIN'
     * keyword.
     * <p>
     * Prior to calling this method, the iterator should be pointing at the
     * first line of the schema file, or the line containing 'END' of the
     * previous module. I.e. calling {@code iterator.next()} will return the
     * first line of a module within the schema.
     * <p>
     * After calling this method, the iterator will be pointing at the line
     * following the 'BEGIN' keyword. I.e. calling {@code iterator.next()} will
     * return the line following the 'BEGIN' keyword.
     *
     * @param lineIterator
     *            iterator pointing at the first line following the 'BEGIN'
     *            keyword
     *
     * @param moduleBuilder
     *            builder to use to construct module from the parsed information
     *
     * @throws ParseException
     *             if any errors occur while parsing the schema
     */
    private static void parseModuleHeader(Iterator<String> lineIterator, AsnSchemaModule.Builder moduleBuilder)
            throws ParseException
    {
        try
        {
            final String moduleName = lineIterator.next().split(" ")[0];
            log.log(Level.INFO, "Found module: {0}", moduleName);
            moduleBuilder.setName(moduleName);

            // skip through to the BEGIN keyword
            for (String line = lineIterator.next(); !"BEGIN".equals(line); line = lineIterator.next())
            {
            }
        }
        catch (NoSuchElementException ex)
        {
            throw new ParseException(ERROR_MISSING_HEADERS, -1);
        }
    }

    /**
     * Parses the data located between the 'BEGIN' and 'END' keywords.
     * <p>
     * Prior to calling this method, the iterator should be pointing at the line
     * following the 'BEGIN' keyword.I.e. calling {@code iterator.next()} will
     * return the line following the 'BEGIN' keyword.
     * <p>
     * After calling this method, the iterator will be pointing at the line
     * containing the 'END' keyword. I.e. calling {@code iterator.next()} will
     * return the line following the 'END' keyword.
     *
     * @param lineIterator
     *            iterator pointing at the first line following the 'BEGIN'
     *            keyword
     *
     * @param moduleBuilder
     *            builder to use to construct module from the parsed information
     *
     * @throws ParseException
     *             if any errors occur while parsing the schema
     */
    private static void parseContent(Iterator<String> lineIterator, AsnSchemaModule.Builder moduleBuilder)
            throws ParseException
    {
        try
        {
            // skip past 'IMPORTS' and 'EXPORTS' keywords
            String line = lineIterator.next();
            while (line.startsWith("EXPORTS") || line.startsWith("IMPORTS"))
            {
                while (!";".equals(line))
                {
                    line = lineIterator.next();
                }
                line = lineIterator.next();
            }
            parseTypeDefinitionsAndValueAssignments(line, lineIterator, moduleBuilder);
        }
        catch (NoSuchElementException ex)
        {
            throw new ParseException(ERROR_MISSING_CONTENT, -1);
        }
    }

    /**
     * Parses the type definitions and value assignments data. This data located
     * after the imports/exports and before the 'END' keyword.
     * <p>
     * Prior to calling this method, the iterator should be pointing at the line
     * following all imports/exports. I.e. calling {@code iterator.next()} will
     * return the line following the first type definition or value assignment.
     * <p>
     * After calling this method, the iterator will be pointing at the line
     * containing the 'END' keyword. I.e. calling {@code iterator.next()} will
     * return the line following the 'END' keyword.
     *
     * @param firstLine
     *            the first type definition or value assignment (i.e. the first
     *            line following all imports/exports)
     *
     * @param lineIterator
     *            iterator pointing at the first line following all
     *            imports/exports
     *
     * @param moduleBuilder
     *            builder to use to construct module from the parsed information
     * @throws ParseException
     */
    private static void parseTypeDefinitionsAndValueAssignments(String firstLine, Iterator<String> lineIterator,
            AsnSchemaModule.Builder moduleBuilder) throws ParseException
    {
        String line = firstLine;
        while (!"END".equals(line))
        {
            if (!line.contains("::="))
            {
                final String error = ERROR_UNKNOWN_CONTENT + line;
                throw new ParseException(error, -1);
            }

            // read to the next definition or assignment
            final StringBuilder builder = new StringBuilder();
            do
            {
                builder.append(line);
                line = lineIterator.next();
            } while (!line.contains("::=") && !"END".equals(line));

            final String content = builder.toString();
            log.log(Level.FINER, "Found content: {0}", content);

            // check if content is a type definition
            Matcher matcher = PATTERN_TYPE_DEFINITION.matcher(content);
            if (matcher.matches())
            {
                final String name = matcher.group(1);
                final String value = matcher.group(4);
                final AsnSchemaTypeDefinition typeDefinition = AsnSchemaTypeDefinitionParser.parse(name, value);
                moduleBuilder.addType(typeDefinition);
                continue;
            }

            // check if content is a value assignment
            matcher = PATTERN_VALUE_ASSIGNMENT.matcher(content);
            if (matcher.matches())
            {
                parseValueAssignment(matcher, moduleBuilder);
                continue;
            }

            // unknown content
            final String error = ERROR_UNKNOWN_CONTENT + content;
            throw new ParseException(error, -1);
        }
    }

    /**
     * Parses a value assignment
     *
     * @param valueAssignmentMatcher
     *            the matcher that identified the content as a value assignment
     *            (generated from {@link #PATTERN_VALUE_ASSIGNMENT})
     *
     * @param moduleBuilder
     *            builder to use to construct module from the parsed information
     */
    private static void parseValueAssignment(Matcher valueAssignmentMatcher, AsnSchemaModule.Builder moduleBuilder)
    {
        final String name = valueAssignmentMatcher.group(1);
        final String value = valueAssignmentMatcher.group(5);
        log.log(Level.FINE, "Found value assignment: {0} - {1}", new Object[] { name, value });
        // TODO parse value assignment
    }
}
