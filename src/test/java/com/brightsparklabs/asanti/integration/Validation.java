package com.brightsparklabs.asanti.integration;

import com.brightsparklabs.asanti.Asanti;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.reader.AsnSchemaReader;
import com.brightsparklabs.asanti.validator.ValidatorImpl;
import com.brightsparklabs.asanti.validator.result.ValidationResult;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

/**
 * Integration tests (meaning they don't use mocks and are end-to-end) to test Validation
 */
public class Validation
{
    @Test
    public void testMissingData() throws Exception
    {
        final CharSource schemaData = Resources.asCharSource(getClass().getResource(
                "/validation/Simple.asn"), Charsets.UTF_8);
        AsnSchema schema = AsnSchemaReader.read(schemaData);

        final ByteSource berData = Resources.asByteSource(getClass().getResource(
                "/validation/Simple.ber"));

        String topLevelType = "Human";

        final ImmutableList<DecodedAsnData> pdus = Asanti.decodeAsnData(berData,
                schema,
                topLevelType);

        DecodedAsnData pdu = pdus.get(0);
        String tag = "/Human/name/first";
        assertEquals("Adam", pdu.<String>getDecodedObject(tag).get());

        tag = "/Human/name/last";
        assertEquals("Smith", pdu.<String>getDecodedObject(tag).get());


        final ValidatorImpl validator = new ValidatorImpl();
        final ValidationResult validationresult = validator.validate(pdus.get(0));
        assertFalse(validationresult.hasFailures());
    }
}