/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti;

import com.brightsparklabs.asanti.model.data.AsnData;
import com.brightsparklabs.asanti.model.data.AsnDataImpl;
import com.brightsparklabs.asanti.model.data.RawAsnData;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.reader.AsnBerDataReader;
import com.brightsparklabs.asanti.reader.AsnSchemaReader;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;

import java.io.IOException;
import java.util.List;

/**
 * The entry point to the API. This contains the logic for decoding tagged/application ASN.1 data
 *
 * @author brightSPARK Labs
 */
public class Asanti
{
    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Decodes the supplied ASN.1 binary data against the specified schema as objects of the
     * specified top level type
     *
     * @param source
     *         ASN.1 BER binary data to decode
     * @param schema
     *         ASN.1 schema to decode data against
     * @param topLevelType
     *         top level type in the schema to decode objects as
     *
     * @return all decoded ASN.1 data as per the schema
     *
     * @throws IOException
     *         if any errors occur reading from the file
     */
    public static ImmutableList<AsnData> decodeAsnData(ByteSource source, CharSource schema,
            String topLevelType) throws IOException
    {
        // TODO: ASN-78 - cache schema
        final AsnSchema asnSchema = AsnSchemaReader.read(schema);
        return decodeAsnData(source, asnSchema, topLevelType);
    }

    /**
     * Decodes the supplied ASN.1 Data against the specified schema as an object of the specified
     * top level type
     *
     * @param source
     *         ASN.1 BER binary data to decode
     * @param asnSchema
     *         schema to decode data against
     * @param topLevelType
     *         top level type in the schema to decode object as
     *
     * @return the decoded ASN.1 data as per the schema
     *
     * @throws IOException
     *         if any errors occur reading the data
     */
    public static ImmutableList<AsnData> decodeAsnData(ByteSource source,
            AsnSchema asnSchema, String topLevelType) throws IOException
    {
        final ImmutableList<RawAsnData> allRawAsnData = readAsnBerData(source);
        final List<AsnData> allAsnData = Lists.newArrayList();
        for (final RawAsnData rawAsnData : allRawAsnData)
        {
            final AsnData asnData = decodeAsnData(rawAsnData, asnSchema, topLevelType);
            allAsnData.add(asnData);
        }
        return ImmutableList.copyOf(allAsnData);
    }

    /**
     * Decodes the supplied ASN.1 Data against the specified schema as an object of the specified
     * top level type
     *
     * @param rawAsnData
     *         data from an ASN.1 binary file
     * @param asnSchema
     *         schema to decode data against
     * @param topLevelType
     *         top level type in the schema to decode object as
     *
     * @return the decoded ASN.1 data as per the schema
     *
     * @throws IOException
     *         if any errors occur reading from the file
     */
    public static AsnData decodeAsnData(RawAsnData rawAsnData, AsnSchema asnSchema,
            String topLevelType) throws IOException
    {
        return new AsnDataImpl(rawAsnData, asnSchema, topLevelType);
    }

    /**
     * Reads the supplied ASN.1 BER/DER binary data
     *
     * @param source
     *         data to read
     *
     * @return list of {@link RawAsnData} objects found in the file
     *
     * @throws IOException
     *         if any errors occur reading from the file
     */
    public static ImmutableList<RawAsnData> readAsnBerData(ByteSource source) throws IOException
    {
        return AsnBerDataReader.read(source);
    }

    /**
     * Reads the supplied ASN.1 BER/DER binary file and stops reading when it has read the specified
     * number of items
     *
     * @param source
     *         file to decode
     * @param maxPDUs
     *         number of PDUs to read from the file. The returned list will be less than or equal to
     *         this value. To read all items call {@link #readAsnBerData(ByteSource)}) instead.
     *
     * @return list of {@link RawAsnData} objects found in the file
     *
     * @throws IOException
     *         if any errors occur reading from the file
     */
    public static ImmutableList<RawAsnData> readAsnBerData(ByteSource source, int maxPDUs)
            throws IOException
    {
        return AsnBerDataReader.read(source, maxPDUs);
    }
}
