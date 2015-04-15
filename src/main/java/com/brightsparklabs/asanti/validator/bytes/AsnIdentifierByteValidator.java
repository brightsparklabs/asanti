/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.bytes;

import com.brightsparklabs.asanti.decoder.bytes.AsnByteDecoder;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.ValidationResult;
import com.brightsparklabs.asanti.validator.ValidationResultImpl;

/**
 * Utility class for decoding bytes from ASN.1 Identifier Types. <p/> This class was created to
 * prevent {@link AsnByteDecoder} from containing too much logic. The methods in here are designed
 * to be called by {@link AsnByteDecoder} (hence why they use package visibility). Do not call the
 * methods in this class directly. Use {@link AsnByteDecoder} instead.
 *
 * @author brightSPARK Labs
 */
class AsnIdentifierByteValidator
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     */
    private AsnIdentifierByteValidator()
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
     * Validates the supplied bytes as an {@link AsnBuiltinType#IRI}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    static ValidationResult validateAsIri(byte[] bytes)
    {
        // TODO: ASN-8
        final ValidationResultImpl.Builder builder
                = AsnByteValidator.validationResultBuilderFor(bytes);
        return builder.build();
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#Oid}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    static ValidationResult validateAsOid(byte[] bytes)
    {
        // TODO: ASN-8
        final ValidationResultImpl.Builder builder
                = AsnByteValidator.validationResultBuilderFor(bytes);
        return builder.build();
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#OidIri}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    static ValidationResult validateAsOidIri(byte[] bytes)
    {
        // TODO: ASN-8
        final ValidationResultImpl.Builder builder
                = AsnByteValidator.validationResultBuilderFor(bytes);
        return builder.build();
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#RelativeIri}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    static ValidationResult validateAsRelativeIri(byte[] bytes)
    {
        // TODO: ASN-8
        final ValidationResultImpl.Builder builder
                = AsnByteValidator.validationResultBuilderFor(bytes);
        return builder.build();
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#RelativeOid}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    static ValidationResult validateAsRelativeOid(byte[] bytes)
    {
        // TODO: ASN-8
        final ValidationResultImpl.Builder builder
                = AsnByteValidator.validationResultBuilderFor(bytes);
        return builder.build();
    }

    /**
     * Validates the supplied bytes as an {@link AsnBuiltinType#RelativeOidIri}
     *
     * @param bytes
     *         bytes to validate
     *
     * @return the validation result
     */
    static ValidationResult validateAsRelativeOidIri(byte[] bytes)
    {
        // TODO: ASN-8
        final ValidationResultImpl.Builder builder
                = AsnByteValidator.validationResultBuilderFor(bytes);
        return builder.build();
    }
}
