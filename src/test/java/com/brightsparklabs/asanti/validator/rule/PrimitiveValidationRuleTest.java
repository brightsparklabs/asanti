/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.rule;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link PrimitiveValidationRule}
 *
 * @author brightSPARK Labs
 */
public class PrimitiveValidationRuleTest
{
    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testValidate() throws Exception
    {
        final PrimitiveValidationRule instance = new PrimitiveValidationRule(AsnSchemaConstraint.NULL);
        // TODO:
        //instance.validate(tag, decodedAsnData);
    }
}
