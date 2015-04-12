/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;

import java.sql.Timestamp;

/**
 * Utility class for decoding bytes from ASN.1 Identifier Types.
 * <p/>
 * This class was created to prevent {@link AsnByteDecoder} from containing too much logic. The methods in here are
 * designed to be called by {@link AsnByteDecoder} (hence why they use package visibility). Do not call the methods in
 * this class directly. Use {@link AsnByteDecoder} instead.
 *
 * @author brightSPARK Labs
 */
class AsnIdentifierByteDecoder
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     */
    private AsnIdentifierByteDecoder()
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
     * Decodes the supplied bytes as an {@link AsnBuiltinType#IRI}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    static String decodeAsIri(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Oid}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    static String decodeAsOid(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#OidIri}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    static String decodeAsOidIri(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeIri}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    static String decodeAsRelativeIri(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeOid}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    static String decodeAsRelativeOid(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#RelativeOidIri}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    static String decodeAsRelativeOidIri(byte[] bytes)
    {
        // TODO: ASN-8
        return "";
    }
}
