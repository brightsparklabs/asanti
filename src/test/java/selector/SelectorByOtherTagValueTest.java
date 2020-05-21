/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package selector; /*
                   * Created by brightSPARK Labs
                   * www.brightsparklabs.com
                   */

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.brightsparklabs.asanti.data.AsnData;
import com.brightsparklabs.asanti.exception.DecodeException;
import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.selector.SelectorByOtherTagValue;
import com.brightsparklabs.asanti.selector.transformations.SingleIndexTagTransformation;
import java.util.Optional;
import java.util.regex.Pattern;
import org.junit.Before;
import org.junit.Test;

/** @author brightSPARK Labs */
public class SelectorByOtherTagValueTest {
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    private SelectorByOtherTagValue<String> instance;

    private AsnData asnData;

    private AsnData asnDataNoMatch;

    private AsnData asnDataThrows;

    private static final Class<String> classOfT = String.class;

    private static final Pattern PATTERN = Pattern.compile(".*?/foo.*$");

    private static final Pattern PATTERN_INDEXED = Pattern.compile(".*?/foo\\[(\\d+)]$");

    private static final String MATCHING_TAG = "some/sort/of/foo";

    private static final String MATCHING_TAG_INDEXED = "some/sort/of/foo[10]";

    private static final String NON_MATCHING_TAG = "some/sort/of/non/matching/tag";

    private static final String OTHER_TAG = "/tag/to/get/data/from";

    private static final String OTHER_TAG_REPLACEMENT = "/tag/to/get/data[%d]/from";

    private static final String OTHER_TAG_INDEXED = "/tag/to/get/data[10]/from";

    private static final String VALUE = "bar";

    // -------------------------------------------------------------------------
    // SETUP/TEAR-DOWN
    // -------------------------------------------------------------------------
    @Before
    public void setUpBeforeTest() throws Exception {
        asnData = mock(AsnData.class);
        when(asnData.getDecodedObject(eq(OTHER_TAG), eq(String.class)))
                .thenReturn(Optional.of(VALUE));
        when(asnData.getDecodedObject(eq(OTHER_TAG_INDEXED), eq(String.class)))
                .thenReturn(Optional.of(VALUE));

        asnDataNoMatch = mock(AsnData.class);
        when(asnDataNoMatch.getDecodedObject(eq(OTHER_TAG), eq(String.class)))
                .thenReturn(Optional.empty());

        asnDataThrows = mock(AsnData.class);
        when(asnDataThrows.getDecodedObject(eq(OTHER_TAG), eq(String.class)))
                .thenThrow(mock(DecodeException.class));

        instance = new SelectorByOtherTagValue<>(PATTERN, s -> OTHER_TAG, VALUE, classOfT);
    }

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void test() throws Exception {
        final boolean wantIt = instance.matches(MATCHING_TAG, AsnBuiltinType.Utf8String, asnData);

        assertTrue(wantIt);
    }

    @Test
    public void testMatchWithIndexedTags() throws Exception {
        final SelectorByOtherTagValue<String> stringSelectorByOtherTagValue =
                new SelectorByOtherTagValue<>(
                        PATTERN,
                        new SingleIndexTagTransformation(PATTERN_INDEXED, OTHER_TAG_REPLACEMENT),
                        VALUE,
                        classOfT);

        final boolean test =
                stringSelectorByOtherTagValue.matches(
                        MATCHING_TAG_INDEXED, AsnBuiltinType.Utf8String, asnData);

        assertTrue(test);
    }

    @Test
    public void testNoTagMatch() throws Exception {
        final boolean wantIt =
                instance.matches(NON_MATCHING_TAG, AsnBuiltinType.Utf8String, asnData);

        assertFalse(wantIt);
    }

    @Test
    public void testNoDataMatch() throws Exception {
        final boolean wantIt =
                instance.matches(MATCHING_TAG, AsnBuiltinType.Utf8String, asnDataNoMatch);

        assertFalse(wantIt);
    }

    @Test
    public void testDataThrows() throws Exception {
        final boolean wantIt =
                instance.matches(MATCHING_TAG, AsnBuiltinType.Utf8String, asnDataThrows);

        assertFalse(wantIt);
    }

    @Test
    public void testDefault() throws Exception {
        SelectorByOtherTagValue<String> decoderSelector =
                new SelectorByOtherTagValue<>(PATTERN, s -> OTHER_TAG, VALUE, VALUE, classOfT);

        final boolean wantItMatched =
                decoderSelector.matches(MATCHING_TAG, AsnBuiltinType.Utf8String, asnData);

        assertTrue(wantItMatched);

        final boolean wantItNonMatched =
                decoderSelector.matches(MATCHING_TAG, AsnBuiltinType.Utf8String, asnDataNoMatch);
        assertTrue(wantItNonMatched);
    }

    @Test
    public void testDefaultFalse() throws Exception {
        SelectorByOtherTagValue<String> decoderSelector =
                new SelectorByOtherTagValue<>(
                        PATTERN, s -> OTHER_TAG, VALUE, "some non matching value", classOfT);

        final boolean wantItMatched =
                decoderSelector.matches(MATCHING_TAG, AsnBuiltinType.Utf8String, asnData);

        assertTrue(wantItMatched);

        final boolean wantItNonMatched =
                decoderSelector.matches(MATCHING_TAG, AsnBuiltinType.Utf8String, asnDataNoMatch);
        assertFalse(wantItNonMatched);
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

}
