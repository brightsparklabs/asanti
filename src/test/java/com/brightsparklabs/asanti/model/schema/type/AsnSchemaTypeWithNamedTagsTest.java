package com.brightsparklabs.asanti.model.schema.type;

import com.brightsparklabs.asanti.model.schema.constraint.AsnSchemaConstraint;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.text.ParseException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
            new AsnSchemaTypeWithNamedTags(null,
                    AsnSchemaConstraint.NULL,
                    ImmutableList.<AsnSchemaNamedTag>of());
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
        // null named tags
        try
        {
            new AsnSchemaTypeWithNamedTags(AsnPrimitiveType.INTEGER, AsnSchemaConstraint.NULL, null);
            fail("NullPointerException not thrown");
        }
        catch (final NullPointerException ex)
        {
        }
    }

    @Test
    public void testAsnSchemaTypeWithNamedTags()
    {

        AsnSchemaNamedTag mockTag0 = mock(AsnSchemaNamedTag.class);
        AsnSchemaNamedTag mockTag1 = mock(AsnSchemaNamedTag.class);

        when(mockTag0.getTag()).thenReturn("0");
        when(mockTag1.getTag()).thenReturn("1");

        AsnSchemaTypeWithNamedTags withTags
                = new AsnSchemaTypeWithNamedTags(AsnPrimitiveType.INTEGER,
                AsnSchemaConstraint.NULL,
                ImmutableList.of(mockTag0, mockTag1));

        assertEquals(AsnPrimitiveType.INTEGER, withTags.getPrimitiveType());
        verify(mockTag0).getTag();
        verify(mockTag1).getTag();
    }

    @Test
    public void testVisitor() throws ParseException
    {
        AsnSchemaTypeVisitor v = BaseAsnSchemaTypeTest.getVisitor();

        AsnSchemaTypeWithNamedTags instance = new AsnSchemaTypeWithNamedTags(AsnPrimitiveType.INTEGER,
                AsnSchemaConstraint.NULL,
                ImmutableList.<AsnSchemaNamedTag>of());

        Object o = instance.accept(v);
        assertEquals("Got AsnSchemaTypeWithNamedTags", o);
    }
}