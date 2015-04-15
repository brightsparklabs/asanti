/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.typedefinitions;

/**
 * Used to decode bytes based on the kind of Type Definition they come from
 *
 * @param <T>
 *         type of object returned by this decoder
 *
 * @author brightSPARK Labs
 */
public interface AsnTypeDefinitionDecoder<T>
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Decodes supplied data based on the the kind of Type Definition represented by this decoder
     *
     * @param data
     *         data to decode
     *
     * @return the results from decoding the data
     */
    public T decode(byte[] data);

    /**
     * Decodes supplied data as a string based on the the kind of Type Definition represented by
     * this decoder
     *
     * @param data
     *         data to decode
     *
     * @return the results from decoding the data
     */
    public String decodeAsString(byte[] data);
}
