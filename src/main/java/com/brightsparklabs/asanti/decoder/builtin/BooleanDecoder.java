/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;

/**
 * Decoder for data of type {@link AsnBuiltinType#Boolean}
 *
 * @author brightSPARK Labs
 */
public class BooleanDecoder extends AbstractBuiltinTypeDecoder<Boolean>
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static BooleanDecoder instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private BooleanDecoder() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static BooleanDecoder getInstance()
    {
        if (instance == null)
        {
            instance = new BooleanDecoder();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: AbstractBuiltinTypeDecoder
    // -------------------------------------------------------------------------

    @Override
    protected Boolean decodeValidatedBytes(final byte[] bytes) throws DecodeException
    {
        return bytes[0] != 0;
    }
}
