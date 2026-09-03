/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.decoder.builtin

import com.brightsparklabs.asanti.exception.DecodeException
import com.brightsparklabs.asanti.model.data.AsantiAsnData
import spock.lang.Specification

import java.nio.charset.StandardCharsets
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Units tests for {@link GeneralizedTimeDecoder}
 *
 * @author brightSPARK Labs
 */
class GeneralizedTimeDecoderSpec extends Specification {
    // -------------------------------------------------------------------------
    // SHARED DATA
    // -------------------------------------------------------------------------

    /** The decoder instance under test. */
    private static final instance = GeneralizedTimeDecoder.getInstance()

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    def "#reason throws for bytes #bytes"() {
        when: "Decoding the invalid bytes to DateTime"
        instance.decode(bytes)

        then: "An exception is thrown"
        thrown(expectedException)

        when: "Decoding the invalid bytes as a String"
        instance.decodeAsString(bytes)

        then: "An exception is thrown"
        thrown(expectedException)

        when: "Decoding the bytes from an ASN.1 Tag"
        AsantiAsnData data = Mock()
        data.getBytes(_ as String) >> Optional.ofNullable(bytes)

        instance.decode("someTag", data)

        then: "An exception is thrown"
        thrown(expectedException)

        where:
        bytes                                                      || expectedException || reason
        null                                                       || DecodeException   || "Null input"
        new byte[0]                                                || DecodeException   || "Empty bytes"
        "19700101".getBytes(StandardCharsets.UTF_8)                || DecodeException   || "No hours"
        "2015022901".getBytes(StandardCharsets.UTF_8)              || DecodeException   || "Not a leap year"
        "2017rubbish".getBytes(StandardCharsets.UTF_8)             || DecodeException   || "Not a valid date"
        "1900010100z".getBytes(StandardCharsets.UTF_8)             || DecodeException   || "Need uppercase Z"
        "\n900010100z".getBytes(StandardCharsets.UTF_8)            || DecodeException   || "Invalid character"
        // Feb 29 in a non-leap century (1900 is not a leap year despite being divisible by 4).
        "19000229120000Z".getBytes(StandardCharsets.UTF_8)         || DecodeException   || "Century non-leap year"
        // Offset minutes out of range (>59).
        "19850416141516.123+2460".getBytes(StandardCharsets.UTF_8) || DecodeException   || "Offset minutes out of range"
        // Offset hours out of range (>23).
        "19850416141516.123+2400".getBytes(StandardCharsets.UTF_8) || DecodeException   || "Offset hours out of range"
        // Offset contains a non-digit.
        "19850416141516.123+2x".getBytes(StandardCharsets.UTF_8)   || DecodeException   || "Malformed offset"
    }

    def "decodes zoned time #time to #expected"() {
        expect: "the explicit offset (or Z) makes the decoded instant absolute"
        instance.decode(time.getBytes(StandardCharsets.UTF_8)).toInstant() == expected

        where:
        time                        || expected
        "1900010100Z"               || Instant.parse("1900-01-01T00:00:00Z")
        "1900010100+0130"           || Instant.parse("1899-12-31T22:30:00Z")
        "19850416141516.123+1030"   || Instant.parse("1985-04-16T03:45:16.123Z")
        // +10:31 is one minute further west than +10:30.
        "19850416141516.123+1031"   || Instant.parse("1985-04-16T03:44:16.123Z")
        "99991231235959.999999999Z" || Instant.parse("9999-12-31T23:59:59.999999999Z")
        "19181111110000.123456789Z" || Instant.parse("1918-11-11T11:00:00.123456789Z")
        "19850416141516.123Z"       || Instant.parse("1985-04-16T14:15:16.123Z")
        "19850416141516.123+0130"   || Instant.parse("1985-04-16T12:45:16.123Z")
        "19850416141516.123-01"     || Instant.parse("1985-04-16T15:15:16.123Z")
        "1970010100Z"               || Instant.parse("1970-01-01T00:00:00Z")
        // The comma is an accepted fraction separator (ASN.1 / ISO 8601) equivalent to a dot.
        "19850416141516,123Z"       || Instant.parse("1985-04-16T14:15:16.123Z")
        // Feb 29 in a leap year (2000 IS a leap year: divisible by 400).
        "20000229120000Z"           || Instant.parse("2000-02-29T12:00:00Z")
        // +23:30 exceeds java.time's +/-18:00 ZoneOffset limit but is valid per ASN.1 (+/-23:59);
        // the decoder resolves it to an instant: 1985-04-16 14:15:16.123 - 23:30.
        "19850416141516.123+2330"   || Instant.parse("1985-04-15T14:45:16.123Z")
    }

    def "decodes sub-second zoned time #time to #expected"() {
        expect: "fractional seconds are resolved to nanosecond precision against an absolute instant"
        instance.decode(time.getBytes(StandardCharsets.UTF_8)).toInstant() == expected

        where:
        time                                                                                                  || expected
        "19700101000000.000000001Z"                                                                           || Instant.parse("1970-01-01T00:00:00.000000001Z")
        "19691231235959.999999999Z"                                                                           || Instant.parse("1969-12-31T23:59:59.999999999Z")
        "19700101000000.000000001-01"                                                                         || Instant.parse("1970-01-01T01:00:00.000000001Z")
        "19691231235959.999999999-01"                                                                         || Instant.parse("1970-01-01T00:59:59.999999999Z")
        "19700101000000.000000001+0130"                                                                       || Instant.parse("1969-12-31T22:30:00.000000001Z")
        "19691231235959.999999999+0130"                                                                       || Instant.parse("1969-12-31T22:29:59.999999999Z")
        // 10 fractional digits: the 10th is truncated (nanosecond is the finest resolution).
        "19700101000000.0000000009Z"                                                                          || Instant.parse("1970-01-01T00:00:00Z")
        "19700101000000.1234Z"                                                                                || Instant.parse("1970-01-01T00:00:00.123400000Z")
        "19181111110000.123456789+01"                                                                         || Instant.parse("1918-11-11T10:00:00.123456789Z")
        // Excess fractional digits beyond nanosecond precision are truncated.
        "19181111110000.123456789123456789123456789123456789123456789123456789123456789123456789123456789Z"   || Instant.parse("1918-11-11T11:00:00.123456789Z")
        "19181111110000.123456789123456789123456789123456789123456789123456789123456789123456789123456789-01" || Instant.parse("1918-11-11T12:00:00.123456789Z")
    }

    def "decodes local time #time in the system default zone"() {
        given: "the fully-resolved wall-clock interpreted in the JVM's zone (offset-less input)"
        final Instant expected = LocalDateTime.parse(iso).atZone(ZoneId.systemDefault()).toInstant()

        expect:
        instance.decode(time.getBytes(StandardCharsets.UTF_8)).toInstant() == expected

        where:
        time                                                                                             || iso
        "1900010100"                                                                                     || "1900-01-01T00:00:00"
        "19850416141516.5"                                                                               || "1985-04-16T14:15:16.500"
        "19850416141516.123"                                                                             || "1985-04-16T14:15:16.123"
        // Comma separator (no offset) resolves identically to the dot form.
        "19850416141516,123"                                                                             || "1985-04-16T14:15:16.123"
        "19181111110000.123456789"                                                                       || "1918-11-11T11:00:00.123456789"
        // Fraction applied to the finest unit present: here 0.1234... of a minute (~7.407s) ...
        "191811111100.123456789123456789123456789123456789123456789123456789123456789123456789123456789" || "1918-11-11T11:00:07.407407347"
        // ... and here 0.1234... of an hour (~7m24.444s).
        "1918111111.123456789123456789123456789123456789123456789123456789123456789123456789123456789"   || "1918-11-11T11:07:24.444440844"
    }

    def "DecodeAsString #time"() {
        given:
        final byte[] bytes = time.getBytes(StandardCharsets.UTF_8)

        when:
        String decoded = instance.decodeAsString(bytes)

        then:
        expected == decoded

        where:
        // decodeAsString should give us back exactly what we passed in if it is valid.
        time                                                                                                || expected
        "1900010100"                                                                                        || "1900010100"
        "1900010100Z"                                                                                       || "1900010100Z"
        "19700101000000.0"                                                                                  || "19700101000000.0"
        "1900010100+0130"                                                                                   || "1900010100+0130"
        "19850416141516.123"                                                                                || "19850416141516.123"
        "99991231235959.999999999"                                                                          || "99991231235959.999999999"
        "19850416141516.123Z"                                                                               || "19850416141516.123Z"
        "19850416141516.123+0130"                                                                           || "19850416141516.123+0130"
        "19850416141516.123+1031"                                                                           || "19850416141516.123+1031"
        "19850416141516.123+2359"                                                                           || "19850416141516.123+2359"
        "19850416141516.123-01"                                                                             || "19850416141516.123-01"
        "1970010100Z"                                                                                       || "1970010100Z"
        "19181111110000.123456789"                                                                          || "19181111110000.123456789"
        "19181111110000.123456789Z"                                                                         || "19181111110000.123456789Z"
        "19181111110000.123456789123456789123456789123456789123456789123456789123456789123456789123456789Z" || "19181111110000.123456789123456789123456789123456789123456789123456789123456789123456789123456789Z"
    }

    def "DecodeAsStringOverload"() {
        given: "A mocked ASN data object"
        AsantiAsnData data = Mock() {
            getBytes("tag1") >> Optional.of("19700101000000.0".getBytes())
            getBytes("tag2") >> Optional.of("1900010100".getBytes())
            getBytes("tag3") >> Optional.of("19850416141516.123".getBytes())
            getBytes("tag4") >> Optional.of("19850416141516.123Z".getBytes())
        }

        when: "Decoding tag1"
        def result = instance.decodeAsString("tag1", data)

        then: "Equals the input"
        result == "19700101000000.0"

        when: "Decoding tag2"
        result = instance.decodeAsString("tag2", data)

        then: "Equals the input"
        result == "1900010100"

        when: "Decoding tag3"
        result = instance.decodeAsString("tag3", data)

        then: "Equals the input"
        result == "19850416141516.123"

        when: "Decoding tag4"
        result = instance.decodeAsString("tag4", data)

        then: "Equals the input"
        result == "19850416141516.123Z"
    }
}
