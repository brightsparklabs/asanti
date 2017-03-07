package com.brightsparklabs.asanti.decoder.builtin

import com.brightsparklabs.assam.exception.DecodeException
import com.google.common.base.Charsets
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 *
 *
 * @author brightSPARK Labs
 */
class GeneralizedTimeDecoderTestSpock extends Specification {


    @Shared
    private static final GeneralizedTimeDecoder instance = GeneralizedTimeDecoder.getInstance()
    private static final Calendar calendar = Calendar.getInstance()
    @Shared
    private static final long rawOffsetInMs = calendar.getTimeZone().getRawOffset()
    private static final long rawOffsetInSeconds = rawOffsetInMs / 1000
    private static final int hours = rawOffsetInSeconds / 3600
    private static final int minutes = rawOffsetInSeconds - (hours * 3600)
    @Shared
    private static final ZoneOffset localOffset = ZoneOffset.ofHoursMinutes(hours, minutes)
    @Shared
    private static final ZoneOffset oneHour30offset = ZoneOffset.ofHoursMinutes(1, 30)
    @Shared
    private static final long rawOffsetInMsOneHour30 = ((1 * 60) + 30) * 60 * 1000
    @Shared
    private static final ZoneOffset minusOneHouroffset = ZoneOffset.ofHoursMinutes(-1, 0)
    @Shared
    private static final long rawOffsetInMsMinusOneHour = ((-1 * 60) + 0) * 60 * 1000

    @Unroll
    def "bad inputs throw: #bytes"() {

        when:
        instance.decode(bytes)

        then:
        thrown(expectedException)

        where:
        bytes                                   || expectedException || reason
        null                                    || DecodeException   || "null input"
        "19700101".getBytes(Charsets.UTF_8)     || DecodeException   || "no hours"
        "2015022901".getBytes(Charsets.UTF_8)   || DecodeException   || "not a leap year"
        "2017rubbish".getBytes(Charsets.UTF_8)  || DecodeException   || "not a valid date"
        "1900010100z".getBytes(Charsets.UTF_8)  || DecodeException   || "need uppercase Z"
        "\n900010100z".getBytes(Charsets.UTF_8) || DecodeException   || "invalid character"
    }

    @Unroll
    def "Decode #time"() {
        given:
        final byte[] bytes = time.getBytes(Charsets.UTF_8)

        when:
        OffsetDateTime decoded = instance.decode(bytes)

        then:
        expected.toInstant() == decoded.toInstant()
        expectedMs == decoded.toInstant().toEpochMilli() + testOffset

        where:

        time                      || expectedMs      || expected                                                                      || testOffset
        "1900010100"              || -2208988800000L || OffsetDateTime.of(1900, 1, 1, 0, 0, 0, 0, localOffset)                        || rawOffsetInMs
        "1900010100Z"             || -2208988800000L || OffsetDateTime.of(1900, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)                     || 0
        "1900010100+0130"         || -2208988800000L || OffsetDateTime.of(1900, 1, 1, 0, 0, 0, 0, oneHour30offset)                    || rawOffsetInMsOneHour30
        "19850416141516.123"      || 482508916123L   || OffsetDateTime.of(1985, 4, 16, 14, 15, 16, 123 * 1000000, localOffset)        || rawOffsetInMs
        "19850416141516.123Z"     || 482508916123L   || OffsetDateTime.of(1985, 4, 16, 14, 15, 16, 123 * 1000000, ZoneOffset.UTC)     || 0
        "19850416141516.123+0130" || 482508916123L   || OffsetDateTime.of(1985, 4, 16, 14, 15, 16, 123 * 1000000, oneHour30offset)    || rawOffsetInMsOneHour30
        "19850416141516.123-01"   || 482508916123L   || OffsetDateTime.of(1985, 4, 16, 14, 15, 16, 123 * 1000000, minusOneHouroffset) || rawOffsetInMsMinusOneHour
        "1970010100Z"             || 0L              || OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)                     || 0
    }

    @Unroll
    def "decode sub milliseconds #time"() {
        given:
        final byte[] bytes = time.getBytes(Charsets.UTF_8)

        when:
        OffsetDateTime decoded = instance.decode(bytes)

        then:
        expected.toInstant() == decoded.toInstant()

        where:
        time                                                                                                  || expected
        "19700101000000.000000001Z"                                                                           || OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 000000001, ZoneOffset.UTC)
        "19691231235959.999999999Z"                                                                           || OffsetDateTime.of(1969, 12, 31, 23, 59, 59, 999999999, ZoneOffset.UTC)
        "19700101000000.000000001-01"                                                                         || OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 000000001, minusOneHouroffset)
        "19691231235959.999999999-01"                                                                         || OffsetDateTime.of(1969, 12, 31, 23, 59, 59, 999999999, minusOneHouroffset)
        "19700101000000.000000001+0130"                                                                       || OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 000000001, oneHour30offset)
        "19691231235959.999999999+0130"                                                                       || OffsetDateTime.of(1969, 12, 31, 23, 59, 59, 999999999, oneHour30offset)
        "19700101000000.0000000009Z"                                                                          || OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)
        "19181111110000.123456789123456789123456789123456789123456789123456789123456789123456789123456789Z"   || OffsetDateTime.of(1918, 11, 11, 11, 0, 0, 123456789, ZoneOffset.UTC)
        "19181111110000.123456789123456789123456789123456789123456789123456789123456789123456789123456789-01" || OffsetDateTime.of(1918, 11, 11, 11, 0, 0, 123456789, minusOneHouroffset)
        "19181111110000.123456789"                                                                            || OffsetDateTime.of(1918, 11, 11, 11, 0, 0, 123456789, localOffset)
    }


    @Unroll
    def "DecodeAsString #time"() {
        given:
        final byte[] bytes = time.getBytes(Charsets.UTF_8)

        when:
        String decoded = instance.decodeAsString(bytes)

        then:
        expected == decoded

        where:
        // decodeAsString should give us back exactly what we passed in if it is valid.
        time                                                                                                || expected
        "1900010100"                                                                                        || "1900010100"
        "1900010100Z"                                                                                       || "1900010100Z"
        "1900010100+0130"                                                                                   || "1900010100+0130"
        "19850416141516.123"                                                                                || "19850416141516.123"
        "19850416141516.123Z"                                                                               || "19850416141516.123Z"
        "19850416141516.123+0130"                                                                           || "19850416141516.123+0130"
        "19850416141516.123-01"                                                                             || "19850416141516.123-01"
        "1970010100Z"                                                                                       || "1970010100Z"
        "19181111110000.123456789123456789123456789123456789123456789123456789123456789123456789123456789Z" || "19181111110000.123456789123456789123456789123456789123456789123456789123456789123456789123456789Z"
    }

}
