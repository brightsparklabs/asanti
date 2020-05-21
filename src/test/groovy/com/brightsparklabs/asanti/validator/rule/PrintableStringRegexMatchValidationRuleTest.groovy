/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.rule

import com.brightsparklabs.assam.data.AsnData
import com.brightsparklabs.assam.validator.FailureType
import com.brightsparklabs.assam.validator.ValidationFailure
import com.google.common.collect.ImmutableSet
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Unit tests for {@link PrintableStringRegexMatchValidationRule}.
 *
 * @author brightSPARK Labs
 */
class PrintableStringRegexMatchValidationRuleTest extends Specification {
    @Unroll
    def "testValidate success #description"() {
        given:
        def instance = new PrintableStringRegexMatchValidationRule(regex)
        def tag = '/test/tag'
        AsnData asnData = Mock()
        asnData.getPrintableString(_) >> Optional.ofNullable(tagValue)

        when:
        ImmutableSet<ValidationFailure> failures = instance.validate(tag, asnData)

        then:
        failures.isEmpty()

        where:
        description                | regex        | tagValue
        'end with Z'               | ~/.+Z$/      | '2020-05-21T10:48:31.123Z'
        'end with Z - no preamble' | ~/.*Z$/      | 'Z'
        'contains hello - start'   | ~/^hello.*/  | 'hello world'
        'contains hello - middle'  | ~/.+hello.+/ | 'say hello world'
        'contains hello - end'     | ~/.*hello$/  | 'world hello'
    }

    @Unroll
    def "testValidate fail #description"() {
        given:
        def failureReason = 'it just failed'
        def instance = new PrintableStringRegexMatchValidationRule(regex, failureReason)
        def tag = '/test/tag'
        AsnData asnData = Mock()
        asnData.getPrintableString(_) >> Optional.ofNullable(tagValue)

        when:
        ImmutableSet<ValidationFailure> failures = instance.validate(tag, asnData)

        then:
        failures.size() == 1
        def failure = failures.asList().get(0)
        failure.getFailureTag() == tag
        failure.getFailureType() == FailureType.CustomValidationFailed
        failure.getFailureReason() == failureReason

        where:
        description                | regex        | tagValue
        'end with Z - no preamble' | ~/.+Z$/      | 'Z'
        'end with Z - missing'     | ~/.+Z$/      | '2020-05-21T10:48:31.123'
        'end with Z - null'        | ~/.+Z$/      | null
        'end with Z - blank'       | ~/.+Z$/      | ''
        'contains hello - start'   | ~/^hello.*/  | 'helo hello world'
        'contains hello - middle'  | ~/.+hello.+/ | 'say helo world'
        'contains hello'           | ~/.*hello$/  | 'hello world helo'
    }
}
