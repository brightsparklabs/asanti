/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import java.text.ParseException;

import com.brightsparklabs.asanti.model.schema.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionOctetString;

/**
 * Logic for parsing a Type Definition form a module within an ASN.1 schema
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionPrimitiveParser
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses an OCTET STRING type definition from a module from an ASN.1 schema
     *
     * @param name
     *            the name of the defined type (i.e. the text on the left hand
     *            side of the {@code ::=})
     *
     * @param constraintText
     *            the constraint text as a string
     *
     * @return an {@link AsnSchemaTypeDefinition} representing the parsed data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    public static AsnSchemaTypeDefinitionOctetString parseOctetString(String name, String constraintText)
            throws ParseException
    {
        final AsnSchemaConstraint constraint = AsnSchemaConstraintParser.parse(constraintText);
        return new AsnSchemaTypeDefinitionOctetString(name, constraint);
    }
}
