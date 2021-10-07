/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema.constraint;

import com.brightsparklabs.asanti.exception.DecodeException;

/**
 * A helper to help determine the "size" of a value that we are applying a size constraint to. The
 * "size" of a value is dependant on its type, eg an OctetString is the length of the byte array,
 * whereas some "String" types have a size that is the length of the decoded string representation
 * of the bytes.
 *
 * @author brightSPARK Labs
 */
public interface SizeDeterminer {
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the size of the appropriate representation of the bytes.
     *
     * @param bytes the raw bytes containing the value
     * @return the size to be used for constraint comparison purposed
     * @throws DecodeException in the case the bytes couldn't be decoded.
     */
    Integer determineSize(byte[] bytes) throws DecodeException;
}
