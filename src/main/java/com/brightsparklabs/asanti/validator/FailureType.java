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
    /** Data is not in the correct format. */
    DataIncorrectlyFormatted,
    /** No data to validate. */
    DataMissing,
    /** A mandatory field is missing. */
    MandatoryFieldMissing,
    /** Data does not conform to an ASN.1 schema constraint. */
    SchemaConstraint,
    /** Failed a custom validation rule. */
    CustomValidationFailed,
    /** Could not decode raw tag against schema. */
    UnknownTag
}
