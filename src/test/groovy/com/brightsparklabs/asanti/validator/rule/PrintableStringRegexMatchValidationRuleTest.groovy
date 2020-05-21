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
    def "testValidate #description"() {
        given:
        def instance = new PrintableStringRegexMatchValidationRule(regex)
        def tag = '/test/tag'
        AsnData asnData = Mock()
        asnData.getPrintableString(_) >> Optional.ofNullable(tagValue)

        when:
        ImmutableSet<ValidationFailure> failures = instance.validate(tag, asnData)

        then:
        failures.size() == expectedFailures
        if (expectedFailures > 0) {
            def failure = failures.asList().get(0)
            failure.getFailureTag() == tag
            failure.getFailureType() == FailureType.CustomValidationFailed
            failure.getFailureReason() == 'hello'
        }

        where:
        description                       | regex        | tagValue                   | expectedFailures
        'end with Z'                      | ~/.+Z$/      | '2020-05-21T10:48:31.123Z' | 0
        'end with Z - no preamble'        | ~/.*Z$/      | 'Z'                        | 0
        'end with Z - no preamble - fail' | ~/.+Z$/      | 'Z'                        | 1
        'end with Z - missing - fail'     | ~/.+Z$/      | '2020-05-21T10:48:31.123'  | 1
        'end with Z - null - fail'        | ~/.+Z$/      | null                       | 1
        'end with Z - blank - fail'       | ~/.+Z$/      | ''                         | 1
        'contains hello - start'          | ~/^hello.*/  | 'hello world'              | 0
        'contains hello - start - fail'   | ~/^hello.*/  | 'helo hello world'         | 1
        'contains hello - middle'         | ~/.+hello.+/ | 'say hello world'          | 0
        'contains hello - middle - fail'  | ~/.+hello.+/ | 'say helo world'           | 1
        'contains hello - end'            | ~/.*hello$/  | 'world hello'              | 0
        'contains hello - fail'           | ~/.*hello$/  | 'hello world helo'         | 1
    }
}
