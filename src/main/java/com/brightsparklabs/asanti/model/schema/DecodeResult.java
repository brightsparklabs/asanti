/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.model.schema;

/**
 * The result from attempting to decode data
 *
 * @author brightSPARK Labs
 *
 * @param <T>
 *            the type of the decoded data contained in this result
 */
public class DecodeResult<T>
{

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** whether the decoding attempt was successful */
    private final boolean wasSuccessful;

    /** the resulting data from the decoding attempt */
    private final T decodedData;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param wasSuccessful
     *            {@code true} if the decoding attempt was successful
     *
     * @param decodedData
     *            the resulting data from the decoding attempt
     */
    private DecodeResult(boolean wasSuccessful, T decodedData)
    {
        this.wasSuccessful = wasSuccessful;
        this.decodedData = decodedData;
    }

    public static <T> DecodeResult<T> create(boolean wasSuccessful, T decodedData)
    {
        return new DecodeResult<T>(wasSuccessful, decodedData);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns {@code true} if the decoding attempt was successful
     *
     * @return {@code true} if the decoding attempt was successful
     */
    public boolean wasSuccessful()
    {
        return wasSuccessful;
    }

    /**
     * Returns the resulting data from the decoding attempt
     *
     * @return the resulting data from the decoding attempt
     */
    public T getDecodedData()
    {
        return decodedData;
    }
}
