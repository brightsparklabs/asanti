/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import java.text.ParseException;
import java.util.logging.Logger;

import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinitionOctetString;
import com.google.common.base.Strings;

/**
 * Logic for parsing a Type Definition form a module within an ASN.1 schema
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeDefinitionPrimitiveParser
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger log = Logger.getLogger(AsnSchemaTypeDefinitionPrimitiveParser.class.getName());

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses a type definition from a module from an ASN.1 schema
     *
     * @param name
     *            the name of the defined type (i.e. the text on the left hand
     *            side of the {@code ::=})
     * @param value
     *            the value of the defined type (i.e. the text on the right hand
     *            side of the {@code ::=})
     *
     * @return an {@link AsnSchemaTypeDefinition} representing the parsed data
     *
     * @throws ParseException
     *             if any errors occur while parsing the type
     */
    public static AsnSchemaTypeDefinition parse(String builtinType, String name, String constraints)
            throws ParseException
    {
        if (Strings.isNullOrEmpty(builtinType)) { throw new ParseException("A primitive Type Definition must have an underlying built-in type",
                -1); }

        switch (builtinType)
        {
            case "OCTET STRING":
                return new AsnSchemaTypeDefinitionOctetString(name, constraints);

            default:
                final String error =
                        String.format("Cannot parse unsupported ASN.1 built-in type: %s for type: %s",
                                builtinType,
                                name);
                // TODO throw exception (only logging while still implementing)
                // throw new ParseException(error, -1);
                log.warning(error);
                return AsnSchemaTypeDefinition.NULL;
        }
    }
}
