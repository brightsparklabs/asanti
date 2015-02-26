/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.reader.parser;

import java.text.ParseException;

import com.brightsparklabs.asanti.model.schema.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.AsnSchemaTypeDefinition;

/**
 * Logic for parsing Constraints from an {@link AsnSchemaTypeDefinition} or
 * {@link AsnSchemaComponentType}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaConstraintParser
{
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
        // TODO ASN-38 - parse constraint text
        return AsnSchemaConstraint.NULL;
    }
}
