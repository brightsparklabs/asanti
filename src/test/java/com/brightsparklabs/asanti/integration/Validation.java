/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.integration;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.Asanti;
import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.reader.AsnSchemaReader;
import com.brightsparklabs.asanti.validator.Validators;
import com.brightsparklabs.assam.validator.FailureType;
import com.brightsparklabs.assam.validator.ValidationFailure;
import com.brightsparklabs.assam.validator.ValidationResult;
import com.brightsparklabs.assam.validator.Validator;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import org.junit.Test;

/** Integration tests (meaning they don't use mocks and are end-to-end) to test Validation */
public class Validation {
    @Test
    public void testMissingData() throws Exception {
        final CharSource schemaData =
                Resources.asCharSource(
                        getClass().getResource("/validation/Simple.asn"), Charsets.UTF_8);
        AsnSchema schema = AsnSchemaReader.read(schemaData);

        final ByteSource berData =
                Resources.asByteSource(getClass().getResource("/validation/Simple.ber"));

        String topLevelType = "Human";

        final ImmutableList<AsantiAsnData> pdus =
                Asanti.decodeAsnData(berData, schema, topLevelType);

        AsantiAsnData pdu = pdus.get(0);
        String tag = "/Human/name/first";
        assertEquals("Adam", pdu.getDecodedObject(tag, String.class).get());

        tag = "/Human/name/last";
        assertEquals("Smith", pdu.getDecodedObject(tag, String.class).get());

        final Validator validator = Validators.getDefault();
        final ValidationResult validationresult = validator.validate(pdus.get(0));
        assertTrue(validationresult.hasFailures());

        final ImmutableSet<ValidationFailure> failures = validationresult.getFailures();
        assertEquals(1, failures.size());

        assertEquals(FailureType.MandatoryFieldMissing, failures.asList().get(0).getFailureType());
    }

    @Test
    public void testSequenceOf() throws Exception {
        final CharSource schemaData =
                Resources.asCharSource(
                        getClass().getResource("/validation/Simple2.asn"), Charsets.UTF_8);
        AsnSchema schema = AsnSchemaReader.read(schemaData);

        final ByteSource berData =
                Resources.asByteSource(getClass().getResource("/validation/Simple2.ber"));

        String topLevelType = "Human";

        final ImmutableList<AsantiAsnData> pdus =
                Asanti.decodeAsnData(berData, schema, topLevelType);

        AsantiAsnData pdu = pdus.get(0);
        String tag = "/Human/person/name/first";
        assertEquals("Adam", pdu.getDecodedObject(tag, String.class).get());

        tag = "/Human/person/name/last";
        assertEquals("Smith", pdu.getDecodedObject(tag, String.class).get());

        final Validator validator = Validators.getDefault();
        final ValidationResult validationresult = validator.validate(pdus.get(0));
        assertFalse(validationresult.hasFailures());
    }
}
