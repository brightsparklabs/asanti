/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.exception;

/**
 * Signals that invalid data was supplied to a decoder
 *
 * @author brightSPARK Labs
 */
public class DecodeException extends Exception {
    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param message reason for the failure
     */
    public DecodeException(String message) {
        super(message);
    }

    /**
     * Contructor
     *
     * @param message
     * @param cause
     */

    /**
     * Constructor.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link
     *     #getMessage()} method).
     * @param cause the cause (which is saved for later retrieval by the {@link #getCause()}
     *     method). A {@code null} value is permitted, and indicates that the cause is nonexistent
     *     or unknown.
     */
    public DecodeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
