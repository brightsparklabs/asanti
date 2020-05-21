/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator;

/**
 * The types of failures that can occur during validation.
 *
 * @author brightSPARK Labs
 */
public enum FailureType {
    /** data is not in the correct format */
    DataIncorrectlyFormatted,
    /** no data to validate */
    DataMissing,
    /** a mandatory field is missing */
    MandatoryFieldMissing,
    /** data does not conform to an ASN.1 schema constraint */
    SchemaConstraint,
    /** failed a custom validation rule */
    CustomValidationFailed,
    /** could not decode raw tag against schema */
    UnknownTag
}
