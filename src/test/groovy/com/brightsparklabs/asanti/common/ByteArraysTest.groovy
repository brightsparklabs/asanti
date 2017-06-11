package com.brightsparklabs.asanti.common

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 *
 *
 * @author brightSPARK Labs
 */
class ByteArraysTest extends Specification {

    @Unroll
    def "test toHexWithAsciiString: #input"() {

        when:
        def result = ByteArrays.toHexWithAsciiString(input)
        then:
        result == expected

        where:
        input || expected
        null  || ""
        (byte[]) [] || ""
        (byte[]) [0] || "0x00"
        (byte[]) [0, 1, 2, 3, 4, 5] || "0x000102030405"
        (byte[]) [ 'h', 'e', 'l', 'l', 'o' ] || "0x68656C6C6F (\"hello\")"
    }

    def "test containsNonPrintableChars: #input"() {
        when:
        def result = ByteArrays.containsNonPrintableChars(input)
        then:
        result == expected
        where:
        input || expected
        null  || false
        (byte[]) [] || false
        (byte[]) [ ' ', 'a'] || false
        (byte[]) [ 0 ] || true
        (byte[]) [ 'h', 'e', 'l', 'l', 0 ] || true
        (byte[]) [ 'h', 'e', 'l', 'l', 'p' ] || false
    }
/*
    def "test toString"() {
        given:

        when:
        // TODO implement stimulus
        then:
        // TODO implement assertions
    }
*/
    @Unroll
    def "test toHexString: #input"() {
        when:
        def result = ByteArrays.toHexString(input)
        then:
        result == expected

        where:
        input || expected
        null  || ""
        (byte[]) [] || ""
        (byte[]) [0] || "0x00"
        (byte[]) [0, 1, 2, 3, 4, 5] || "0x000102030405"
        (byte[]) [ 'h', 'e', 'l', 'l', 'o' ] || "0x68656C6C6F"
    }

}
