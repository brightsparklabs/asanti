/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.decoder.builtin;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 * Shared, formatter-based parser for the ASN.1 time types {@code UTCTime} and {@code
 * GeneralizedTime}.
 *
 * <p>Both types are {@code YYYYMMDDHH...} with an optional fraction on the smallest unit and an
 * optional {@code Z | (+|-)HH[MM]} offset. The awkward bits that neither {@link DateTimeFormatter}
 * nor {@link ZoneOffset} can express - offsets up to the ASN.1 &plusmn;23:59 range, fractions of an
 * hour/minute, and &gt;9-digit fractions - are handled by small pre-normalisation steps here so the
 * decoders can simply feed a canonical {@code YYYYMMDDHHMMSS[.fraction]} string through a single
 * strict formatter.
 *
 * @author brightSPARK Labs
 */
final class AsnTimeParser {
    // -------------------------------------------------------------------------
    // CONSTANTS
    // -------------------------------------------------------------------------

    /** Sentinel returned when no explicit timezone offset is present. */
    static final int NO_OFFSET = Integer.MIN_VALUE;

    /**
     * The number of characters in a canonical seconds-precision core, i.e. {@code YYYYMMDDHHMMSS}.
     */
    private static final int CANONICAL_CORE_LENGTH = 14;

    /** The maximum number of fractional-second digits representable at nanosecond precision. */
    private static final int MAX_FRACTION_DIGITS = 9;

    /** The number of nanoseconds in one minute (used to resolve a fraction-of-minute). */
    private static final long NANOS_PER_MINUTE = 60L * 1_000_000_000L;

    /** The number of nanoseconds in one hour (used to resolve a fraction-of-hour). */
    private static final long NANOS_PER_HOUR = 3600L * 1_000_000_000L;

    /** Strict formatter for a canonical {@code YYYYMMDDHHMMSS} core (no fraction). */
    private static final DateTimeFormatter CANONICAL = canonical(false);

    /** Strict formatter for a canonical {@code YYYYMMDDHHMMSS.fraction} core. */
    private static final DateTimeFormatter CANONICAL_FRACTION = canonical(true);

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /** Private constructor - utility class. */
    private AsnTimeParser() {}

    /**
     * Builds a strict formatter for a canonical, fixed-width {@code YYYYMMDDHHMMSS} core,
     * optionally followed by a nanosecond fraction. Optional sections are deliberately avoided
     * (they roughly double parse cost), so a separate formatter is built for each of the
     * with/without-fraction cases.
     *
     * @param withFraction whether to append an optional-free {@code .fraction} component.
     * @return the built {@link DateTimeFormatter}.
     */
    private static DateTimeFormatter canonical(final boolean withFraction) {
        final DateTimeFormatterBuilder b =
                new DateTimeFormatterBuilder()
                        .appendValue(ChronoField.YEAR, 4)
                        .appendValue(ChronoField.MONTH_OF_YEAR, 2)
                        .appendValue(ChronoField.DAY_OF_MONTH, 2)
                        .appendValue(ChronoField.HOUR_OF_DAY, 2)
                        .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
                        .appendValue(ChronoField.SECOND_OF_MINUTE, 2);
        if (withFraction) {
            b.appendFraction(ChronoField.NANO_OF_SECOND, 1, MAX_FRACTION_DIGITS, true);
        }
        return b.toFormatter(Locale.ROOT).withResolverStyle(ResolverStyle.STRICT);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Parses a GeneralizedTime-shaped value (4-digit year; already validated as a VisibleString)
     * into an {@link OffsetDateTime} expressed in the system default zone. Callers for other types
     * (e.g. UTCTime) normalise their input to this shape first.
     *
     * @param raw the raw time value.
     * @return the decoded {@link OffsetDateTime}.
     * @throws DateTimeException if the value is malformed or invalid.
     */
    static OffsetDateTime parse(final String raw) {
        // Only an uppercase "Z" is a valid UTC designator.
        if (raw.endsWith("z")) {
            throw new DateTimeException("Invalid format: \"" + raw + "\" is malformed at \"z\"");
        }

        // 1. Split off the offset (Z | (+|-)HH[MM], up to +/-23:59) -> seconds.
        final int signOrZ = offsetStart(raw);
        final int offsetSeconds;
        final String local;
        if (signOrZ < 0) {
            offsetSeconds = NO_OFFSET;
            local = raw;
        } else {
            offsetSeconds = parseOffsetSeconds(raw, signOrZ);
            local = raw.substring(0, signOrZ);
        }

        // 2. Normalise the local part to "YYYYMMDDHHMMSS[.frac<=9]" plus any residual sub-second
        //    duration (from a fraction-of-minute/hour, which the formatter cannot express).
        final Normalised normalised = normalise(local);

        // 3. Parse via the appropriate (non-optional) strict formatter, then apply the residual.
        final DateTimeFormatter formatter =
                normalised.canonical().length() > CANONICAL_CORE_LENGTH
                        ? CANONICAL_FRACTION
                        : CANONICAL;
        final LocalDateTime dateTime =
                LocalDateTime.parse(normalised.canonical(), formatter).plus(normalised.residual());

        // 4. Resolve to an instant and express it in the system default zone.
        final Instant instant =
                offsetSeconds == NO_OFFSET
                        ? dateTime.atZone(ZoneId.systemDefault()).toInstant()
                        : dateTime.toInstant(ZoneOffset.UTC).minusSeconds(offsetSeconds);
        return OffsetDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * {@return the number of trailing characters that form a {@code Z} / {@code (+|-)HH[MM]}
     * offset, or 0 if none is present}
     *
     * @param value the time value to inspect.
     */
    static int offsetLength(final String value) {
        final int start = offsetStart(value);
        return start < 0 ? 0 : value.length() - start;
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Normalises a local (offset-stripped) value to canonical {@code YYYYMMDDHHMMSS[.frac]} form.
     * Hour- and minute-precision cores are zero-filled to seconds; a fraction on an hour/minute is
     * converted to a residual {@link Duration}; a fraction on the seconds is truncated to 9 digits.
     *
     * @param local the offset-stripped value.
     * @return the canonical string and any residual sub-second duration.
     * @throws DateTimeException if the core is not a valid length.
     */
    private static Normalised normalise(final String local) {
        final int sep = indexOfSeparator(local);
        final String core = sep < 0 ? local : local.substring(0, sep);
        final String fraction = sep < 0 ? "" : local.substring(sep + 1);

        return switch (core.length()) {
            case 14 ->
                    new Normalised(
                            fraction.isEmpty() ? core : core + '.' + trim9(fraction),
                            Duration.ZERO);
            case 12 -> new Normalised(core + "00", fractionDuration(fraction, NANOS_PER_MINUTE));
            case 10 -> new Normalised(core + "0000", fractionDuration(fraction, NANOS_PER_HOUR));
            default ->
                    throw new DateTimeException(
                            "Invalid format: \"" + local + "\" has an invalid length");
        };
    }

    /**
     * Converts a fraction (of a minute or hour) into a nanosecond {@link Duration}, truncating
     * downwards.
     *
     * @param fraction the fractional digits (without the leading separator), possibly empty.
     * @param nanosPerUnit the number of nanoseconds in one whole unit (minute or hour).
     * @return the {@link Duration}, or {@link Duration#ZERO} if {@code fraction} is empty.
     */
    private static Duration fractionDuration(final String fraction, final long nanosPerUnit) {
        if (fraction.isEmpty()) {
            return Duration.ZERO;
        }
        final long nanos =
                new BigDecimal("0." + fraction)
                        .multiply(BigDecimal.valueOf(nanosPerUnit))
                        .setScale(0, RoundingMode.DOWN)
                        .longValueExact();
        return Duration.ofNanos(nanos);
    }

    /**
     * {@return the first {@value #MAX_FRACTION_DIGITS} digits of a fraction, truncating any finer
     * precision}
     *
     * @param fraction the fractional digits.
     */
    private static String trim9(final String fraction) {
        return fraction.length() > MAX_FRACTION_DIGITS
                ? fraction.substring(0, MAX_FRACTION_DIGITS)
                : fraction;
    }

    /**
     * {@return the index of the first fraction separator ({@code .} or {@code ,}), or -1 if none}
     *
     * @param value the value to search.
     */
    private static int indexOfSeparator(final String value) {
        for (int i = 0; i < value.length(); i++) {
            final char c = value.charAt(i);
            if (c == '.' || c == ',') {
                return i;
            }
        }
        return -1;
    }

    /**
     * {@return the index at which a timezone offset begins}: the position of a trailing {@code Z},
     * or of a {@code +}/{@code -} sign introducing a {@code HH} / {@code HHMM} offset, or -1 if
     * none.
     *
     * @param value the value to inspect.
     */
    private static int offsetStart(final String value) {
        final int len = value.length();
        if (len > 0 && value.charAt(len - 1) == 'Z') {
            return len - 1;
        }
        // A sign can only introduce an offset at len-3 ("+HH") or len-5 ("+HHMM").
        for (final int i : new int[] {len - 3, len - 5}) {
            if (i >= 0 && (value.charAt(i) == '+' || value.charAt(i) == '-')) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Parses the offset that begins at {@code start} into a signed number of seconds. Accepts
     * {@code Z} (0 seconds), {@code (+|-)HH} and {@code (+|-)HHMM}, with hours 0-23 and minutes
     * 0-59.
     *
     * @param value the full time value.
     * @param start the index of the offset introducer (from {@link #offsetStart(String)}).
     * @return the offset in seconds (negative for a {@code -} offset).
     * @throws DateTimeException if the offset is malformed or out of range.
     */
    private static int parseOffsetSeconds(final String value, final int start) {
        if (value.charAt(start) == 'Z') {
            return 0;
        }
        final int hours = twoDigits(value, start + 1);
        final int minutes = (value.length() - start) == 5 ? twoDigits(value, start + 3) : 0;
        if (hours > 23 || minutes > 59) {
            throw new DateTimeException(
                    "Invalid format: \"" + value + "\" has an out-of-range offset");
        }
        final int magnitude = (hours * 60 + minutes) * 60;
        return value.charAt(start) == '-' ? -magnitude : magnitude;
    }

    /**
     * {@return the value of exactly two decimal digits at {@code index}}
     *
     * @param value the string to read from.
     * @param index the index of the first digit.
     * @throws DateTimeException if the two characters are not both digits, or the range extends
     *     past the end of {@code value}.
     */
    private static int twoDigits(final String value, final int index) {
        if (index + 1 >= value.length()) {
            throw new DateTimeException("Invalid format: \"" + value + "\" has a malformed offset");
        }
        final char a = value.charAt(index);
        final char b = value.charAt(index + 1);
        if (a < '0' || a > '9' || b < '0' || b > '9') {
            throw new DateTimeException("Invalid format: \"" + value + "\" has a malformed offset");
        }
        return (a - '0') * 10 + (b - '0');
    }

    // -------------------------------------------------------------------------
    // INTERNAL CLASSES
    // -------------------------------------------------------------------------

    /**
     * The result of normalising a value: a canonical seconds-precision string ready for the
     * formatters, plus any residual sub-second duration (from a fraction-of-minute/hour) to add.
     *
     * @param canonical the canonical {@code YYYYMMDDHHMMSS[.fraction]} string.
     * @param residual the residual {@link Duration} to add after parsing.
     */
    private record Normalised(String canonical, Duration residual) {}
}
