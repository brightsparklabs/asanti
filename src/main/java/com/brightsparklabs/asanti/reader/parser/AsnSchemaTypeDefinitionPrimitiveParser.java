/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.typedefinition.*;

import java.text.ParseException;

/**
 * Logic for parsing a primitive Type Definition from a module within an ASN.1 schema
 *
 * @author brightSPARK Labs
 */
public final class AsnSchemaTypeDefinitionPrimitiveParser
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Private constructor. There should be no need to ever instantiate this static class.
     */
    private AsnSchemaTypeDefinitionPrimitiveParser()
    {
        throw new AssertionError();
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses an Ia5String type definition from a module from an ASN.1 schema
     *
     * @param name
     *         the name of the defined type (i.e. the text on the left hand side of the {@code ::=})
     * @param constraintText
     *         the constraint text as a string
     *
     * @return an {@link AbstractAsnSchemaTypeDefinition} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    public static AsnSchemaTypeDefinitionIa5String parseIA5String(String name, String constraintText)
            throws ParseException
    {
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionIa5String(name, constraint);
    }

    /**
     * Parses an OCTET STRING type definition from a module from an ASN.1 schema
     *
     * @param name
     *         the name of the defined type (i.e. the text on the left hand side of the {@code ::=})
     * @param constraintText
     *         the constraint text as a string
     *
     * @return an {@link AbstractAsnSchemaTypeDefinition} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    public static AsnSchemaTypeDefinitionOctetString parseOctetString(String name, String constraintText)
            throws ParseException
    {
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionOctetString(name, constraint);
    }

    /**
     * Parses a BIT STRING type definition from a module from an ASN.1 schema
     *
     * @param name
     *         the name of the defined type (i.e. the text on the left hand side of the {@code ::=})
     * @param constraintText
     *         the constraint text as a string
     *
     * @return an {@link AbstractAsnSchemaTypeDefinition} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    public static AsnSchemaTypeDefinitionBitString parseBitString(String name, String constraintText)
            throws ParseException
    {
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionBitString(name, constraint);
    }

    /**
     * Parses a Utf8String type definition from a module from an ASN.1 schema
     *
     * @param name
     *         the name of the defined type (i.e. the text on the left hand side of the {@code ::=})
     * @param constraintText
     *         the constraint text as a string
     *
     * @return an {@link AbstractAsnSchemaTypeDefinition} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    public static AsnSchemaTypeDefinitionUtf8String parseUTF8String(String name, String constraintText)
            throws ParseException
    {
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionUtf8String(name, constraint);
    }

    /**
     * Parses a NumericString type definition from a module from an ASN.1 schema
     *
     * @param name
     *         the name of the defined type (i.e. the text on the left hand side of the {@code ::=})
     * @param constraintText
     *         the constraint text as a string
     *
     * @return an {@link AbstractAsnSchemaTypeDefinition} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    public static AsnSchemaTypeDefinitionNumericString parseNumericString(String name, String constraintText)
            throws ParseException
    {
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionNumericString(name, constraint);
    }

    /**
     * Parses a VisibleString type definition from a module from an ASN.1 schema
     *
     * @param name
     *         the name of the defined type (i.e. the text on the left hand side of the {@code ::=})
     * @param constraintText
     *         the constraint text as a string
     *
     * @return an {@link AbstractAsnSchemaTypeDefinition} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    public static AsnSchemaTypeDefinitionVisibleString parseVisibleString(String name, String constraintText)
            throws ParseException
    {
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionVisibleString(name, constraint);
    }

    /**
     * Parses a GeneralString type definition from a module from an ASN.1 schema
     *
     * @param name
     *         the name of the defined type (i.e. the text on the left hand side of the {@code ::=})
     * @param constraintText
     *         the constraint text as a string
     *
     * @return an {@link AbstractAsnSchemaTypeDefinition} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    public static AsnSchemaTypeDefinitionGeneralString parseGeneralString(String name, String constraintText)
            throws ParseException
    {
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionGeneralString(name, constraint);
    }

    /**
     * Parses an Integer type definition from a module from an ASN.1 schema
     *
     * @param name
     *         the name of the defined type (i.e. the text on the left hand side of the {@code ::=})
     * @param distinguishedValues
     *         the optional list of distinguished values
     * @param constraintText
     *         the constraint text as a string
     *
     * @return an {@link AbstractAsnSchemaTypeDefinition} representing the parsed data
     *
     * @throws ParseException
     *         if any errors occur while parsing the type
     */
    public static AsnSchemaTypeDefinitionInteger parseInteger(String name,
            Iterable<AsnSchemaNamedTag> distinguishedValues, String constraintText) throws ParseException
    {
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionInteger(name, distinguishedValues, constraint);
    }
}
