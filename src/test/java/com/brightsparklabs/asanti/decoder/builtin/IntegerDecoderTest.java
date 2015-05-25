/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.decoder.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

/**
 * Units tests for {@link IntegerDecoder}
 *
 * @author brightSPARK Labs
 */
public class IntegerDecoderTest
{
    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    /** instance under test */
    private static final IntegerDecoder instance = IntegerDecoder.getInstance();

    // -------------------------------------------------------------------------
    // TESTS
    // -------------------------------------------------------------------------

    @Test
    public void testDecode() throws Exception
    {
        // run through all the one byte values.
        byte[] bytes = new byte[1];
        for (int b = Byte.MAX_VALUE; b >= Byte.MIN_VALUE; b--)
        {
            bytes[0] = (byte)b;
            BigInteger big = instance.decode(bytes);
            assertEquals(big.longValue(), b);
        }

        {
            // check that we can have a leading 0 followed by a byte with a 1 for most significant
            // bit.  ie without the leading 0 this would be treated as a negative number.
            byte[] b2 = {(byte) 0x00, (byte) 0xff};
            BigInteger big2 = instance.decode(b2);
            assertEquals(255L, big2.longValue());
        }

        {
            // test negative one a few different ways.
            byte[] b1 = {(byte) 0xff};
            byte[] b2 = {(byte) 0xff, (byte) 0xff};
            byte[] b3 = {(byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b4 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b5 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b6 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b7 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b8 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b16 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b17 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};

            BigInteger big1 = instance.decode(b1);
            BigInteger big2 = instance.decode(b2);
            BigInteger big3 = instance.decode(b3);
            BigInteger big4 = instance.decode(b4);
            BigInteger big5 = instance.decode(b5);
            BigInteger big6 = instance.decode(b6);
            BigInteger big7 = instance.decode(b7);
            BigInteger big8 = instance.decode(b8);
            BigInteger big16 = instance.decode(b16);
            BigInteger big17 = instance.decode(b17);

            assertEquals(-1, big1.longValue());
            assertEquals(-1, big2.longValue());
            assertEquals(-1, big3.longValue());
            assertEquals(-1, big4.longValue());
            assertEquals(-1, big5.longValue());
            assertEquals(-1, big6.longValue());
            assertEquals(-1, big7.longValue());
            assertEquals(-1, big8.longValue());

            assertEquals(-1, big16.longValue());
            assertEquals(-1, big17.longValue());
        }

        {
            // test biggest negative for given number of bytes
            byte[] b1 = {(byte) 0x80};
            byte[] b2 = {(byte) 0x80, (byte) 0x00};
            byte[] b3 = {(byte) 0x80, (byte) 0x00, (byte) 0x00};
            byte[] b4 = {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            byte[] b5 = {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            byte[] b6 = {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            byte[] b7 = {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            byte[] b8 = {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            byte[] b9 = {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            byte[] b16 = {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            byte[] b17 = {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

            BigInteger big1 = instance.decode(b1);
            BigInteger big2 = instance.decode(b2);
            BigInteger big3 = instance.decode(b3);
            BigInteger big4 = instance.decode(b4);
            BigInteger big5 = instance.decode(b5);
            BigInteger big6 = instance.decode(b6);
            BigInteger big7 = instance.decode(b7);
            BigInteger big8 = instance.decode(b8);
            BigInteger big9 = instance.decode(b9);
            BigInteger big16 = instance.decode(b16);
            BigInteger big17 = instance.decode(b17);

            assertEquals(-128L, big1.longValue());assertEquals(Byte.MIN_VALUE, big1.longValue());
            assertEquals(-32768L, big2.longValue());assertEquals(Short.MIN_VALUE, big2.longValue());
            assertEquals(-8388608L, big3.longValue());
            assertEquals(-2147483648L, big4.longValue());assertEquals(Integer.MIN_VALUE, big4.longValue());
            assertEquals(-549755813888L, big5.longValue());
            assertEquals(-140737488355328L, big6.longValue());
            assertEquals(-36028797018963968L, big7.longValue());
            assertEquals(-9223372036854775808L, big8.longValue());assertEquals(Long.MIN_VALUE, big8.longValue());


            // from here on we run out of primitive type language support, so do some calculations.
            BigInteger two56 = new BigInteger("256");
            BigInteger big9calc = big8.multiply(two56);
            assertEquals(big9calc, big9);

            BigInteger two = BigInteger.ONE.add(BigInteger.ONE);

            BigInteger big16calc = big8.multiply(big8.negate()).multiply(two);
            assertEquals(big16calc, big16);

            BigInteger big17calc = big16calc.multiply(two56);
            assertEquals(big17calc, big17);

        }

        {
            // test biggest positive for given number of bytes
            byte[] b1 = {(byte) 0x7f};
            byte[] b2 = {(byte) 0x7f, (byte) 0xff};
            byte[] b3 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff};
            byte[] b4 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b5 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b6 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b7 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b8 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b9 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b16 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b17 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};

            BigInteger big1 = instance.decode(b1);
            BigInteger big2 = instance.decode(b2);
            BigInteger big3 = instance.decode(b3);
            BigInteger big4 = instance.decode(b4);
            BigInteger big5 = instance.decode(b5);
            BigInteger big6 = instance.decode(b6);
            BigInteger big7 = instance.decode(b7);
            BigInteger big8 = instance.decode(b8);
            BigInteger big9 = instance.decode(b9);
            BigInteger big16 = instance.decode(b16);
            BigInteger big17 = instance.decode(b17);

            assertEquals(127L, big1.longValue());assertEquals(Byte.MAX_VALUE, big1.longValue());
            assertEquals(32767L, big2.longValue());assertEquals(Short.MAX_VALUE, big2.longValue());
            assertEquals(8388607L, big3.longValue());
            assertEquals(2147483647L, big4.longValue());assertEquals(Integer.MAX_VALUE, big4.longValue());
            assertEquals(549755813887L, big5.longValue());
            assertEquals(140737488355327L, big6.longValue());
            assertEquals(36028797018963967L, big7.longValue());
            assertEquals(9223372036854775807L, big8.longValue());assertEquals(Long.MAX_VALUE, big8.longValue());


            // from here on we run out of primitive type language support, so do some basic calculations.

            // shift it
            BigInteger two55 = new BigInteger("255");
            BigInteger big9calc = big8.shiftLeft(8).add(two55);
            assertEquals(big9calc, big9);



            BigInteger big16calc = BigInteger.ONE;
            for(int i = 0; i < 127; ++i)
            {
                big16calc = big16calc.setBit(i);
            }
            assertEquals(big16calc, big16);

            BigInteger big17calc = big16calc.shiftLeft(8).add(two55);
            assertEquals(big17calc, big17);
        }

        // test empty byte array
        try
        {

            byte [] b1 = { };
            instance.decode(b1);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }


        // test null
        try
        {
            instance.decode(null);
            fail("DecodeException not thrown");
        }
        catch (DecodeException ex)
        {
        }
    }

    @Test
    public void testDecodeAsString() throws Exception
    {
        byte[] bytes = new byte[1];
        for (int b = Byte.MAX_VALUE; b >= Byte.MIN_VALUE; b--) {
            bytes[0] = (byte) b;
            String big = instance.decodeAsString(bytes);
            assertEquals(big, Long.toString((long) b));
        }

        {
            // test negative one a few different ways.
            byte[] b1 = {(byte) 0xff};
            byte[] b2 = {(byte) 0xff, (byte) 0xff};
            byte[] b3 = {(byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b4 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b5 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b6 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b7 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b8 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b16 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b17 = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};

            String big1 = instance.decodeAsString(b1);
            String big2 = instance.decodeAsString(b2);
            String big3 = instance.decodeAsString(b3);
            String big4 = instance.decodeAsString(b4);
            String big5 = instance.decodeAsString(b5);
            String big6 = instance.decodeAsString(b6);
            String big7 = instance.decodeAsString(b7);
            String big8 = instance.decodeAsString(b8);
            String big16 = instance.decodeAsString(b16);
            String big17 = instance.decodeAsString(b17);

            assertEquals("-1", big1);
            assertEquals("-1", big2);
            assertEquals("-1", big3);
            assertEquals("-1", big4);
            assertEquals("-1", big5);
            assertEquals("-1", big6);
            assertEquals("-1", big7);
            assertEquals("-1", big8);
            assertEquals("-1", big16);
            assertEquals("-1", big17);
        }

        {
            // test biggest negative for given number of bytes
            byte[] b1 = {(byte) 0x80};
            byte[] b2 = {(byte) 0x80, (byte) 0x00};
            byte[] b3 = {(byte) 0x80, (byte) 0x00, (byte) 0x00};
            byte[] b4 = {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            byte[] b5 = {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            byte[] b6 = {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            byte[] b7 = {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            byte[] b8 = {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            byte[] b9 = {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            byte[] b16 = {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
            byte[] b17 = {(byte) 0x80, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

            String big1 = instance.decodeAsString(b1);
            String big2 = instance.decodeAsString(b2);
            String big3 = instance.decodeAsString(b3);
            String big4 = instance.decodeAsString(b4);
            String big5 = instance.decodeAsString(b5);
            String big6 = instance.decodeAsString(b6);
            String big7 = instance.decodeAsString(b7);
            String big8 = instance.decodeAsString(b8);
            String big9 = instance.decodeAsString(b9);
            String big16 = instance.decodeAsString(b16);
            String big17 = instance.decodeAsString(b17);

            assertEquals("-128", big1);
            assertEquals("-32768", big2);
            assertEquals("-8388608", big3);
            assertEquals("-2147483648", big4);
            assertEquals("-549755813888", big5);
            assertEquals("-140737488355328", big6);
            assertEquals("-36028797018963968", big7);
            assertEquals("-9223372036854775808", big8);

            assertEquals("-2361183241434822606848", big9);
            assertEquals("-170141183460469231731687303715884105728", big16);
            assertEquals("-43556142965880123323311949751266331066368", big17);
        }

        {
            // test biggest positive for given number of bytes
            byte[] b1 = {(byte) 0x7f};
            byte[] b2 = {(byte) 0x7f, (byte) 0xff};
            byte[] b3 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff};
            byte[] b4 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b5 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b6 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b7 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b8 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b9 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b16 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] b17 = {(byte) 0x7f, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};

            String big1 = instance.decodeAsString(b1);
            String big2 = instance.decodeAsString(b2);
            String big3 = instance.decodeAsString(b3);
            String big4 = instance.decodeAsString(b4);
            String big5 = instance.decodeAsString(b5);
            String big6 = instance.decodeAsString(b6);
            String big7 = instance.decodeAsString(b7);
            String big8 = instance.decodeAsString(b8);
            String big9 = instance.decodeAsString(b9);
            String big16 = instance.decodeAsString(b16);
            String big17 = instance.decodeAsString(b17);

            assertEquals("127", big1);
            assertEquals("32767", big2);
            assertEquals("8388607", big3);
            assertEquals("2147483647", big4);
            assertEquals("549755813887", big5);
            assertEquals("140737488355327", big6);
            assertEquals("36028797018963967", big7);
            assertEquals("9223372036854775807", big8);

            assertEquals("2361183241434822606847", big9);
            assertEquals("170141183460469231731687303715884105727", big16);
            assertEquals("43556142965880123323311949751266331066367", big17);
        }

        // test empty byte array
        try {

            byte[] b1 = {};
            instance.decodeAsString(b1);
            fail("DecodeException not thrown");
        } catch (DecodeException ex) {
        }

        // test null
        try {
            instance.decodeAsString(null);
            fail("DecodeException not thrown");
        } catch (DecodeException ex) {
        }
    }
}
