package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link AsnSchemaTypeWithNamedTags}
 *
 * @author brightSPARK Labs
 */
public class AsnSchemaTypeWithNamedTagsTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testAsnSchemaTypeWithNamedTagsConstructorPreconditions() throws Exception
    {
        // null primitive type
        try
        {
            new AsnSchemaTypeWithNamedTags(null, AsnSchemaConstraint.NULL, ImmutableList.<AsnSchemaNamedTag>of());
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        // null named tags
        try
        {
            new AsnSchemaTypeWithNamedTags(null, AsnSchemaConstraint.NULL, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
    }
}