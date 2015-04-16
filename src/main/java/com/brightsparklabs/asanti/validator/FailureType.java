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
    // a mandatory field is missing
    MandatoryFieldMissing,
    // no data to validate
    DataMissing,
    // data is not in the correct format
    DataIncorrectlyFormatted,
    // no failure occurred
    None,
}
