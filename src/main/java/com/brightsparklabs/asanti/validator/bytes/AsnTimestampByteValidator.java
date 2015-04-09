/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.bytes;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.ValidationResult;
import com.brightsparklabs.asanti.validator.ValidationResultImpl;

/**
 * Utility class for decoding bytes from ASN.1 Date/Time Types.
 * <p/>
 * This class was created to prevent {@link AsnByteValidator} from containing too much logic. The methods in here are
 * designed to be called by {@link AsnByteValidator} (hence why they use package visibility). Do not call the methods in
 * this class directly. Use {@link AsnByteValidator} instead.
 *
 * @author brightSPARK Labs
 */
class AsnTimestampByteValidator
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     */
    private AsnTimestampByteValidator()
    {
        // constructor should never be called for static class
        assert false;
    }

    // -------------------------------------------------------------------------
    //  METHODS
    // -------------------------------------------------------------------------

    /*
     * NOTE: Please keep these methods ordered alphabetically for simplicity
     */

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Date}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    static ValidationResult validateAsDate(byte[] bytes)
    {
        // TODO: ASN-8
        return ValidationResultImpl.builder().build();
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#DateTime}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    static ValidationResult validateAsDateTime(byte[] bytes)
    {
        // TODO: ASN-8
        return ValidationResultImpl.builder().build();
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#GeneralizedTime}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    static ValidationResult validateAsGeneralizedTime(byte[] bytes)
    {
        // TODO: ASN-8
        return ValidationResultImpl.builder().build();
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Time}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    static ValidationResult validateAsTime(byte[] bytes)
    {
        // TODO: ASN-8
        return ValidationResultImpl.builder().build();
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#TimeOfDay}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    static ValidationResult validateAsTimeOfDay(byte[] bytes)
    {
        // TODO: ASN-8
        return ValidationResultImpl.builder().build();
    }
}
