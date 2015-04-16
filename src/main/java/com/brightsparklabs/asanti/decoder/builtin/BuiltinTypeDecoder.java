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
 * Used to decode bytes based on the kind of Type Definition they come from
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
     * Decodes supplied data based on the the kind of Type Definition represented by this decoder
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
     * Decodes supplied data as a string based on the the kind of Type Definition represented by
     * this decoder
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
