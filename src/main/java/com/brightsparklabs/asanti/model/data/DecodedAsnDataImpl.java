/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */
package com.brightsparklabs.asanti.model.data;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.decoder.DecoderVisitor;
import com.brightsparklabs.asanti.decoder.builtin.BuiltinTypeDecoder;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.DecodedTag;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.BaseEncoding;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.*;

/**
 * Default implementation of {@link DecodedAsnData}
 *
 * @author brightSPARK Labs
 */
public class DecodedAsnDataImpl implements DecodedAsnData
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** ASN data to decode */
    private final AsnData asnData;

    /**
     * all tags which could be decoded. Map is of form: { decodedTagString => decodedTag }
     */
    private final ImmutableMap<String, DecodedTag> decodedTags;

    /**
     * all tags which could not be decoded. Map is of form: { decodedTagString => decodedTag }
     */
    private final ImmutableMap<String, DecodedTag> unmappedTags;

    /**
     * all tags (decoded and unmapped) found in the data. Map is of form: { decodedTagString =>
     * decodedTag }
     */
    private final ImmutableMap<String, DecodedTag> allTags;

    /** visitor used to determine which decoder to use for decoding data */
    private final DecoderVisitor decoderVisitor = new DecoderVisitor();

    /** the schema used to decode */
    private final AsnSchema asnSchema;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor
     *
     * @param asnData
     *         data to decode
     * @param asnSchema
     *         schema to use to decode data
     * @param topLevelTypeName
     *         the name of the top level type in this module from which to begin decoding the raw
     *         tag
     *
     * @throws NullPointerException
     *         if any of the parameters are {@code null}
     * @throws IllegalArgumentException
     *         if topLevelTypeName is blank
     */
    public DecodedAsnDataImpl(AsnData asnData, AsnSchema asnSchema, String topLevelTypeName)
    {
        checkNotNull(asnData);
        checkNotNull(asnSchema);
        checkNotNull(topLevelTypeName);
        checkArgument(!topLevelTypeName.trim().isEmpty(), "Top level type name must be specified");

        this.asnData = asnData;

        // decode the tags in the data
        final Map<String, DecodedTag> decodedToRawTags = Maps.newHashMap();
        final Map<String, DecodedTag> unmappedTags = Maps.newHashMap();

        ImmutableSet<OperationResult<DecodedTag, String>> results
                = asnSchema.getDecodedTags(asnData.getRawTags(), topLevelTypeName);
        for (OperationResult<DecodedTag, String> decodeResult : results)
        {
            final DecodedTag decodedTag = decodeResult.getOutput();
            if (decodeResult.wasSuccessful())
            {
                decodedToRawTags.put(decodedTag.getTag(), decodedTag);
            }
            else
            {
                // could not decode tag
                unmappedTags.put(decodedTag.getTag(), decodedTag);
            }
        }

        this.asnSchema = asnSchema;
        this.decodedTags = ImmutableMap.copyOf(decodedToRawTags);
        this.unmappedTags = ImmutableMap.copyOf(unmappedTags);
        this.allTags = ImmutableMap.<String, DecodedTag>builder()
                .putAll(decodedToRawTags)
                .putAll(unmappedTags)
                .build();
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: DecodedAsnData
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<String> getTags()
    {
        return ImmutableSet.copyOf(decodedTags.keySet());
    }

    @Override
    public ImmutableSet<String> getUnmappedTags()
    {
        return ImmutableSet.copyOf(unmappedTags.keySet());
    }

    @Override
    public boolean contains(String tag)
    {
        return allTags.containsKey(tag);
    }

    @Override
    public Optional<byte[]> getBytes(String tag)
    {
        final DecodedTag decodedTag = allTags.get(tag);
        // if no decoded tag, assume supplied tag is is already raw tag
        final String rawTag = (decodedTag == null) ? tag : decodedTag.getRawTag();
        return asnData.getBytes(rawTag);
    }

    @Override
    public ImmutableMap<String, byte[]> getBytesMatching(Pattern regex)
    {
        final Map<String, byte[]> result = Maps.newHashMap();
        for (final String tag : getMatchingTags(regex))
        {
            final Optional<byte[]> bytes = getBytes(tag);
            if (bytes.isPresent())
            {
                result.put(tag, bytes.get());
            }
        }

        // check against the raw tags too
        result.putAll(asnData.getBytesMatching(regex));

        return ImmutableMap.copyOf(result);
    }

    @Override
    public Optional<String> getHexString(String tag)
    {
        final Optional<byte[]> bytes = getBytes(tag);
        if (bytes.isPresent())
        {
            final String result = "0x" + BaseEncoding.base16().encode(bytes.get());
            return Optional.of(result);
        }
        return Optional.absent();
    }

    @Override
    public ImmutableMap<String, String> getHexStringsMatching(Pattern regex)
    {
        final Map<String, String> result = Maps.newHashMap();
        for (final String tag : getMatchingTags(regex))
        {
            final Optional<String> hexString = getHexString(tag);
            if (hexString.isPresent())
            {
                result.put(tag, hexString.get());
            }
        }

        // Add any matched to raw tags
        final Map<String, byte []> raw = asnData.getBytesMatching(regex);
        for(Map.Entry<String, byte []> entry : raw.entrySet())
        {
            final String hexString = "0x" + BaseEncoding.base16().encode(entry.getValue());
            result.put(entry.getKey(), hexString);
        }

        return ImmutableMap.copyOf(result);
    }

    @Override
    public Optional<String> getPrintableString(String tag) throws DecodeException
    {
        final DecodedTag decodedTag = decodedTags.get(tag);
        if (decodedTag == null)
        {
            return Optional.absent();
        }

        final AsnSchemaType schemaType = decodedTag.getType();
        final AsnPrimitiveType type = schemaType.getPrimitiveType();
        final BuiltinTypeDecoder<?> decoder = (BuiltinTypeDecoder<?>) type.accept(decoderVisitor);
        final String result = decoder.decodeAsString(tag, this);
        return Optional.of(result);
    }

    @Override
    public ImmutableMap<String, String> getPrintableStringsMatching(Pattern regex)
            throws DecodeException
    {
        final Map<String, String> result = Maps.newHashMap();
        for (final String tag : getMatchingTags(regex))
        {
            final Optional<String> printableString = getPrintableString(tag);
            if (printableString.isPresent())
            {
                result.put(tag, printableString.get());
            }
        }

        return ImmutableMap.copyOf(result);
    }

    @Override
    public Optional<AsnSchemaType> getType(String tag)
    {
        return asnSchema.getType(tag);
    }

    @Override
    public <T> Optional<T> getDecodedObject(String tag) throws DecodeException
    {
        final DecodedTag decodedTag = decodedTags.get(tag);
        if (decodedTag == null)
        {
            return Optional.absent();
        }

        final AsnSchemaType schemaType = decodedTag.getType();
        final AsnPrimitiveType type = schemaType.getPrimitiveType();
        final BuiltinTypeDecoder<?> decoder = (BuiltinTypeDecoder<?>) type.accept(decoderVisitor);

        // The implementation is getting an object from our decoder, based on a text tag, that is
        // of a type unknowable at compile-time.  This essentially means that somewhere along the
        // chain, no matter what we do we can't prevent type mismatches.  For example if a user
        // asks for tag "x/y/z" then we need to return something.  When we decode "x/y/z", at
        // run-time, we can know its ASN.1 type, and therefore even the type it will decode to.
        // What we can't do is stop the client software from asking for "/x/y/z" as a String when
        // at run-time we know it is a BigInteger or Timestamp.  All we can do is provide the best
        // documentation we can, and ensure that we throw.
        // So, even though this goes against best practice, we will suppress the compiler warning
        // here as it is something we already know about.
        @SuppressWarnings("unchecked")
        final T result = (T) decoder.decode(tag, this);
        return Optional.of(result);
    }

    @Override
    public ImmutableMap<String, Object> getDecodedObjectsMatching(Pattern regex)
            throws DecodeException
    {
        final Map<String, Object> result = Maps.newHashMap();
        for (final String tag : getMatchingTags(regex))
        {
            final Optional<String> decodedObject = getDecodedObject(tag);
            if (decodedObject.isPresent())
            {
                result.put(tag, decodedObject.get());
            }
        }

        return ImmutableMap.copyOf(result);
    }

    // -------------------------------------------------------------------------
    // PRIVATE
    // -------------------------------------------------------------------------

    /**
     * Returns all tags which match the supplied regular expression
     *
     * @param regex
     *         regular expression to test tag names against
     *
     * @return all tags which match the supplied regular expression
     */
    private ImmutableSet<String> getMatchingTags(Pattern regex)
    {
        if (regex == null)
        {
            return ImmutableSet.<String>of();
        }

        final Set<String> tags = Sets.newHashSet();
        for (final String tag : allTags.keySet())
        {
            if (regex.matcher(tag).matches())
            {
                tags.add(tag);
            }
        }

        return ImmutableSet.copyOf(tags);
    }
}
