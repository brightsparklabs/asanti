/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;

/**
 * Used to decode bytes based on the kind of ASN.1 Built-in Type they came from
 *
 * @param <T>
 *         type of object returned by this decoder
 *
 * @author brightSPARK Labs
 */
public interface BuiltinTypeDecoder<T>
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Decodes the supplied bytes based on the kind of ASN.1 Built-in Type represented by this
     * decoder
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the results from decoding the bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied bytes
     */
    public T decode(byte[] bytes) throws DecodeException;

    /**
     * Decodes the supplied bytes as a string based on the the kind of ASN.1 Built-in Type
     * represented by this validator
     *
     * @param bytes
     *         bytes to decode
     *
     * @return the results from decoding the bytes
     *
     * @throws DecodeException
     *         if any errors occur while decoding the supplied bytes
     */
    public String decodeAsString(byte[] bytes) throws DecodeException;
}
