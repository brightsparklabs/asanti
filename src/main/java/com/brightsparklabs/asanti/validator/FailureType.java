/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.validator;

/**
 * The types of failures that can occur during validation.
 *
 * @author brightSPARK Labs
 */
public enum FailureType
{
    /** data is not in the correct format */
    DataIncorrectlyFormatted,
    /** no data to validate */
    DataMissing,
    /** a mandatory field is missing */
    MandatoryFieldMissing,
    /** data does not conform to an ASN.1 schema constraint */
    SchemaConstraint,
    /** could not decode raw tag against schema */
    UnknownTag
}
