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
 * Convenience class to simplify implementing the {@link BuiltinTypeDecoder} interface.
 *
 * <p>This class provides a default implementation of {@link #decodeAsString} which simply returns
 * {@code decode(data).toString()}. Sub-classes should override {@link #decodeAsString} if more
 * complicated string decoding is required.</p>
 *
 * @author brightSPARK Labs
 */
public abstract class AbstractBuiltinTypeDecoder<T> implements BuiltinTypeDecoder<T>
{
    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BuiltinTypeDecoder
    // -------------------------------------------------------------------------

    @Override
    public String decodeAsString(final byte[] bytes) throws DecodeException
    {
        return decode(bytes).toString();
    }
}
