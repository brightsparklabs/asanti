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
 * Units tests for {@link UtcTimeDecoder}
 *
 * @author brightSPARK Labs
 */
class UtcTimeDecoderSpec extends Specification {
    // -------------------------------------------------------------------------
    // SHARED DATA
    // -------------------------------------------------------------------------

    /** The decoder instance under test. */
    private static final instance = UtcTimeDecoder.getInstance()

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
        bytes                                                || expectedException || reason
        null                                                 || DecodeException   || "Null input"
        new byte[0]                                          || DecodeException   || "Empty bytes"
        "700101".getBytes(StandardCharsets.UTF_8)            || DecodeException   || "No hours"
        "27111100".getBytes(StandardCharsets.UTF_8)          || DecodeException   || "To short"
        "15022901".getBytes(StandardCharsets.UTF_8)          || DecodeException   || "Not a leap year"
        "2017rubbish".getBytes(StandardCharsets.UTF_8)       || DecodeException   || "Not a valid date"
        "850416141516z".getBytes(StandardCharsets.UTF_8)     || DecodeException   || "Need uppercase Z"
        "\n900010100z".getBytes(StandardCharsets.UTF_8)      || DecodeException   || "Invalid character"
        // UTCTime forbids a fractional component (unlike GeneralizedTime) - both separators.
        "850416141516.5".getBytes(StandardCharsets.UTF_8)    || DecodeException   || "Fraction not allowed (dot)"
        "850416141516,5".getBytes(StandardCharsets.UTF_8)    || DecodeException   || "Fraction not allowed (comma)"
        // Offset out of range (minutes >59).
        "850416141516+2460".getBytes(StandardCharsets.UTF_8) || DecodeException   || "Offset minutes out of range"
        // Offset out of range (hours >23).
        "850416141516+2400".getBytes(StandardCharsets.UTF_8) || DecodeException   || "Offset hours out of range"
        // Offset contains a non-digit.
        "850416141516+2x".getBytes(StandardCharsets.UTF_8)   || DecodeException   || "Malformed offset"
        // Feb 29 mapped via the 2000 pivot to a non-leap year (99 -> 1999).
        "990229120000Z".getBytes(StandardCharsets.UTF_8)     || DecodeException   || "Pivoted non-leap year"
    }

    def "decodes zoned time #time to #expected"() {
        expect: "the explicit offset (or Z) makes the decoded instant absolute"
        instance.decode(time.getBytes(StandardCharsets.UTF_8)).toInstant() == expected

        where:
        time                || expected
        "850416141516-01"   || Instant.parse("1985-04-16T15:15:16Z")
        "850416141516+1030" || Instant.parse("1985-04-16T03:45:16Z")
        // +10:31 is one minute further west than +10:30.
        "850416141516+1031" || Instant.parse("1985-04-16T03:44:16Z")
        "991231235959Z"     || Instant.parse("1999-12-31T23:59:59Z")
        // Pivot year 2000: two-digit years 50-99 map to 1950-1999.
        "500101000000Z"     || Instant.parse("1950-01-01T00:00:00Z")
        // Pivot year 2000: two-digit years 00-49 map to 2000-2049.
        "491231235959Z"     || Instant.parse("2049-12-31T23:59:59Z")
        // Feb 29 via the 2000 pivot (00 -> 2000, which IS a leap year).
        "000229120000Z"     || Instant.parse("2000-02-29T12:00:00Z")
        // Unix epoch.
        "7001010000Z"       || Instant.parse("1970-01-01T00:00:00Z")
    }

    def "decodes local time #time in the system default zone"() {
        given: "the same wall-clock resolved in the JVM's zone (how the decoder treats offset-less input)"
        final Instant expected = LocalDateTime.parse(iso).atZone(ZoneId.systemDefault()).toInstant()

        expect:
        instance.decode(time.getBytes(StandardCharsets.UTF_8)).toInstant() == expected

        where:
        time           || iso
        "700101000000" || "1970-01-01T00:00:00"
        "850416141516" || "1985-04-16T14:15:16"
        // Different seasons to exercise any DST rules of the running zone.
        "181111110000" || "2018-11-11T11:00:00"
        "180611110000" || "2018-06-11T11:00:00"
        // Pivot year 2000 minimum (1950), local time.
        "500101000000" || "1950-01-01T00:00:00"
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
        time                || expected
        "700101000000"      || "700101000000"
        "0001010000"        || "0001010000"
        "850416141516"      || "850416141516"
        "850416141516Z"     || "850416141516Z"
        "850416141516-01"   || "850416141516-01"
        "850416141516+1030" || "850416141516+1030"
        "850416141516+1031" || "850416141516+1031"
        "991231235959"      || "991231235959"
        "7001010000Z"       || "7001010000Z"
        "181111110000"      || "181111110000"
        "181111110000+01"   || "181111110000+01"
        "181111110000Z"     || "181111110000Z"
    }

    def "DecodeAsStringOverload"() {
        given: "A mocked ASN data object"
        AsantiAsnData data = Mock() {
            getBytes("tag1") >> Optional.of("700101000000".getBytes())
            getBytes("tag2") >> Optional.of("0001010000".getBytes())
            getBytes("tag3") >> Optional.of("850416141516".getBytes())
            getBytes("tag4") >> Optional.of("850416141516Z".getBytes())
        }

        when: "Decoding tag1"
        def result = instance.decodeAsString("tag1", data)

        then: "Equals the input"
        result == "700101000000"

        when: "Decoding tag2"
        result = instance.decodeAsString("tag2", data)

        then: "Equals the input"
        result == "0001010000"

        when: "Decoding tag3"
        result = instance.decodeAsString("tag3", data)

        then: "Equals the input"
        result == "850416141516"

        when: "Decoding tag4"
        result = instance.decodeAsString("tag4", data)

        then: "Equals the input"
        result == "850416141516Z"
    }
}
