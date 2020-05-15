/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.common;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;

/**
 * Utility class for manipulating bytes
 *
 * @author brightSPARK Labs
 */
public class ByteArrays {
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** basic hex encoding in lowercase */
    private static BaseEncoding hexEncoding = BaseEncoding.base16();

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This should never be called as this is a utility class.
     */
    private ByteArrays() {
        assert false;
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns a hex representation of the bytes in the supplied array, with "0x" prepended to it.
     * If the byte array only contains printable ASCII characters, then decodes the byte array as a
     * string using UTF8 and appends it to the end of the returned string in parentheses.
     *
     * <p>Example 1:<br>
     * Calling this methods with byte array {@code [0x48, 0x45, 0x4C, 0x4C, 0x4F]} will return
     * {@code "0x48454C4C4F ("HELLO")"}
     *
     * <p>Example 2:<br>
     * Calling this methods with byte array {@code [0x00, 0x48, 0x45, 0x4C, 0x4C, 0x4F]} will return
     * {@code "0x0048454C4C4F"}
     *
     * @param bytes bytes to convert to string
     * @return the bytes represented as hex (with ASCII after if appropriate) or an empty string if
     *     byte array was {@code null}/empty
     */
    public static String toHexWithAsciiString(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }

        final StringBuilder result = new StringBuilder(toHexString(bytes));
        if (!containsNonPrintableChars(bytes)) {
            result.append(" (\"").append(toString(bytes)).append("\")");
        }
        return result.toString();
    }

    /**
     * Checks whether the byte array contains non-printable ASCII characters
     *
     * @param bytes bytes to check
     * @return {@code true} if array contains non-printable ASCII characters. {@code false}
     *     otherwise
     */
    public static boolean containsNonPrintableChars(final byte[] bytes) {
        if (bytes != null) {
            for (final byte x : bytes) {
                if (x < 32 || x > 126) {
                    // byte is outside printable range
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Decodes the byte array as a string using UTF8
     *
     * @param bytes bytes to convert to string
     * @return string represented by the bytes or an empty string if byte array was {@code null}
     */
    public static String toString(final byte[] bytes) {
        return bytes == null ? "" : new String(bytes, Charsets.UTF_8);
    }

    /**
     * Returns a hex representation of the bytes in the supplied array, with "0x" prepended to it,
     * or an empty string if byte array was {@code null} or empty. The hex conversion will use an
     * uppercase alphabet, ie "0xAB" as opposed to "0xab".
     *
     * @param bytes bytes to decode
     * @return the bytes represented as hex or an empty string if byte array was {@code null} or
     *     empty
     */
    public static String toHexString(final byte[] bytes) {
        return (bytes == null || bytes.length == 0) ? "" : "0x" + hexEncoding.encode(bytes);
    }
}
