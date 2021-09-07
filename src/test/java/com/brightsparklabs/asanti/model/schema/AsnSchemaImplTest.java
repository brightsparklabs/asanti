/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.model.schema;

import static org.junit.Assert.*;

import com.brightsparklabs.asanti.mocks.model.schema.MockAsnSchemaModule;
import com.google.common.collect.ImmutableMap;
import java.text.ParseException;
import org.junit.BeforeClass;
import org.junit.Test;

public class AsnSchemaImplTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** all modules in the default instance */
    private static ImmutableMap<String, AsnSchemaModule> modules;

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    @BeforeClass
    public static void setUpBeforeClass() throws ParseException {
        modules = MockAsnSchemaModule.createMockedAsnSchemaModules();
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaDefault() throws Exception {
        // test null
        try {
            new AsnSchemaImpl(null, modules);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }
        try {
            new AsnSchemaImpl("Document-PDU", null);
            fail("NullPointerException not thrown");
        } catch (final NullPointerException ex) {
        }

        // test empty
        try {
            new AsnSchemaImpl("", modules);
            fail("IllegalArgumentException not thrown");
        } catch (final IllegalArgumentException ex) {
        }
        try {
            new AsnSchemaImpl(" ", modules);
            fail("IllegalArgumentException not thrown");
        } catch (final IllegalArgumentException ex) {
        }
        try {
            new AsnSchemaImpl("Document-PDU", ImmutableMap.of());
            fail("IllegalArgumentException not thrown");
        } catch (final IllegalArgumentException ex) {
        }

        // test invalid primary module
        try {
            new AsnSchemaImpl("UNKNOWN", modules);
            fail("IllegalArgumentException not thrown");
        } catch (final IllegalArgumentException ex) {
        }
    }
}
