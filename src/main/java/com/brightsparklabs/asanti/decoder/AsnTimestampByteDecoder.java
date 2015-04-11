/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder;

import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;

import java.sql.Timestamp;

/**
 * Utility class for decoding bytes from ASN.1 Date/Time Types.
 * <p/>
 * This class was created to prevent {@link AsnByteDecoder} from containing too much logic. The methods in here are
 * designed to be called by {@link AsnByteDecoder} (hence why they use package visibility). Do not call the methods in
 * this class directly. Use {@link AsnByteDecoder} instead.
 *
 * @author brightSPARK Labs
 */
class AsnTimestampByteDecoder
{
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     */
    private AsnTimestampByteDecoder()
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
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Date}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    static Timestamp decodeAsDate(byte[] bytes)
    {
        // TODO: ASN-8
        return new Timestamp(0);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#DateTime}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    static Timestamp decodeAsDateTime(byte[] bytes)
    {
        // TODO: ASN-8
        return new Timestamp(0);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#GeneralizedTime}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    static Timestamp decodeAsGeneralizedTime(byte[] bytes)
    {
        // TODO: ASN-8
        return new Timestamp(0);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#Time}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    static Timestamp decodeAsTime(byte[] bytes)
    {
        // TODO: ASN-8
        return new Timestamp(0);
    }

    /**
     * Decodes the supplied bytes as an {@link AsnBuiltinType#TimeOfDay}
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the decoded bytes
     *
     * @throws NullPointerException
     *         if parameters are {@code null}
     */
    static Timestamp decodeAsTimeOfDay(byte[] bytes)
    {
        // TODO: ASN-8
        return new Timestamp(0);
    }
}
