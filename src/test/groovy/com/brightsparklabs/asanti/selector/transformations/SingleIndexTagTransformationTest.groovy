/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.selector.transformations

import spock.lang.Specification

import java.util.regex.Pattern

/**
 * Unit tests for {@link SingleIndexTagTransformation}.
 *
 * @author brightSPARK Labs
 */
class SingleIndexTagTransformationTest extends Specification {

    def "test ApplyPositive"() {
        when:
        final SingleIndexTagTransformation rawAaaTransformation = new SingleIndexTagTransformation(Pattern.compile("/index\\[(\\d+)].*"), "/newindex[%d]");
        def transformedString = rawAaaTransformation.apply(tag)

        then:
        transformedString == expectedResult

        where:
        tag            || expectedResult
        "/index[0]"    || "/newindex[0]"
        "/index[1]"    || "/newindex[1]"
        "/index[2]"    || "/newindex[2]"
        "/index[3000]" || "/newindex[3000]"
    }

    def "test ApplyNegativeValidPattern"() {
        when:
        final SingleIndexTagTransformation rawAaaTransformation = new SingleIndexTagTransformation(pattern, replacementString)
        rawAaaTransformation.apply(tag)

        then:
        thrown RuntimeException

        where:
        pattern                             || replacementString || tag
        Pattern.compile("/index\\[(\\d+)]") || "/index[%d]"      || "/index[garbage]"
        Pattern.compile("/index\\[\\d+]")   || "/index[%d]"      || "/index[0]"
        Pattern.compile("garbage")          || "/index[%d]"      || "/index[0]"
        // empty checks
        Pattern.compile("")                 || "/index[%d]"      || "/index[0]"
        Pattern.compile("/index\\[(\\d+)]") || ""                || "/index[0]"
        Pattern.compile("/index\\[(\\d+)]") || "/index[%d]"      || ""
        // null checks
        null                                || "/index[%s]"      || "/index[0]"
        Pattern.compile("/index\\[(\\d+)]") || null              || "/index[0]"
        Pattern.compile("/index\\[(\\d+)]") || "/index[%d]"      || null
    }
}
