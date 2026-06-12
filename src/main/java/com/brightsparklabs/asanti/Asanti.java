/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti;

import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.brightsparklabs.asanti.model.data.AsantiAsnDataImpl;
import com.brightsparklabs.asanti.model.data.RawAsnData;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.reader.AsnBerDataReader;
import com.brightsparklabs.asanti.reader.AsnSchemaReader;
import com.brightsparklabs.asanti.reader.AsnBerDataStreamReader;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.CharSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * The entry point to the API. This contains the logic for decoding tagged/application ASN.1 data
 *
 * @author brightSPARK Labs
 */
public class Asanti {
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Decodes the supplied ASN.1 binary data against the specified schema as objects of the
     * specified top level type
     *
     * @param source ASN.1 BER binary data to decode
     * @param schema ASN.1 schema to decode data against
     * @param topLevelType top level type in the schema to decode objects as
     * @return all decoded ASN.1 data as per the schema
     * @throws IOException if any errors occur reading from the file
     */
    public static ImmutableList<AsantiAsnData> decodeAsnData(
            final byte[] source, final CharSource schema, final String topLevelType)
            throws IOException {
        // TODO: ASN-78 - cache schema
        final AsnSchema asnSchema = AsnSchemaReader.read(schema);
        return decodeAsnData(source, asnSchema, topLevelType);
    }

    /**
     * Decodes the supplied ASN.1 Data against the specified schema as an object of the specified
     * top level type
     *
     * @param source ASN.1 BER binary data to decode
     * @param asnSchema schema to decode data against
     * @param topLevelType top level type in the schema to decode object as
     * @return the decoded ASN.1 data as per the schema
     * @throws IOException if any errors occur reading the data
     */
    public static ImmutableList<AsantiAsnData> decodeAsnData(
            final byte[] source, final AsnSchema asnSchema, final String topLevelType)
            throws IOException {
        final ImmutableList<RawAsnData> allRawAsnData = readAsnBerData(source);
        final List<AsantiAsnData> allAsnData = Lists.newArrayList();
        for (final RawAsnData rawAsnData : allRawAsnData) {
            final AsantiAsnData asnData = decodeAsnData(rawAsnData, asnSchema, topLevelType);
            allAsnData.add(asnData);
        }
        return ImmutableList.copyOf(allAsnData);
    }

    /**
     * Decodes the supplied ASN.1 Data against the specified schema as an object of the specified
     * top level type
     *
     * @param rawAsnData data from an ASN.1 binary file
     * @param asnSchema schema to decode data against
     * @param topLevelType top level type in the schema to decode object as
     * @return the decoded ASN.1 data as per the schema
     */
    public static AsantiAsnData decodeAsnData(
            final RawAsnData rawAsnData, final AsnSchema asnSchema, final String topLevelType) {
        return new AsantiAsnDataImpl(rawAsnData, asnSchema, topLevelType);
    }

    /**
     * Reads the supplied ASN.1 BER/DER binary data.
     *
     * <p>For files below {@code 50MB} this will read in all the bytes at once. For files over this
     * limit, a buffered input stream will be used instead. Use {@link #readAsnBerData(InputStream)}
     * or {@link #readAsnBerData(byte[])} if you'd prefer different behaviour.
     *
     * @param source The path to the file containing the ASN.1 BER/DER binary data.
     * @return List of {@link RawAsnData} objects found in the data.
     * @throws IOException If any errors occur reading the data.
     */
    public static ImmutableList<RawAsnData> readAsnBerData(final Path source) throws IOException {
        return AsnBerDataReader.read(source);
    }

    public static Stream<RawAsnData> readAsnBerDataStream(final Path source) throws IOException {
        return AsnBerDataReader.read(source).stream();
    }

    /**
     * Reads the supplied ASN.1 BER/DER binary data.
     *
     * @param source The ASN.1 BER/DER binary data to decode.
     * @return List of {@link RawAsnData} objects found in the data.
     * @throws IOException If any errors occur reading the data.
     */
    public static ImmutableList<RawAsnData> readAsnBerData(final byte[] source) throws IOException {
        return AsnBerDataReader.read(source);
    }

    /**
     * Reads the supplied ASN.1 BER/DER binary data.
     *
     * @param source An input stream containing the ASN.1 BER/DER binary data to decode.
     * @return List of {@link RawAsnData} objects found in the data.
     * @throws IOException If any errors occur reading the data.
     */
    public static ImmutableList<RawAsnData> readAsnBerData(final InputStream source)
            throws IOException {
        return AsnBerDataReader.read(source);
    }
}
