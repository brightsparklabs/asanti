/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.common.DecodeException;
import com.brightsparklabs.asanti.common.OperationResult;
import com.brightsparklabs.asanti.decoder.AsnByteDecoder;
import com.brightsparklabs.asanti.model.data.AsnData;
import com.brightsparklabs.asanti.model.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.model.schema.type.*;
import com.brightsparklabs.asanti.model.schema.typedefinition.AsnSchemaNamedTag;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Set;

/**
 * Validator for data of type {@link AsnBuiltinType#Enumerated}
 *
 * @author brightSPARK Labs
 */
public class EnumeratedValidator extends PrimitiveBuiltinTypeValidator
{
    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** singleton instance */
    private static EnumeratedValidator instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance</p>
     */
    private EnumeratedValidator() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static EnumeratedValidator getInstance()
    {
        if (instance == null)
        {
            instance = new EnumeratedValidator();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BuiltinTypeValidator
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<DecodedTagValidationFailure> validate(final String tag,
            final AsnData asnData)
    {
        final OperationResult<String, ImmutableSet<DecodedTagValidationFailure>> result
                = validateAndDecode(tag, asnData);

        return result.getFailureReason().or(ImmutableSet.<DecodedTagValidationFailure>of());
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: PrimitiveBuiltinTypeValidator
    // -------------------------------------------------------------------------

    @Override
    protected ImmutableSet<ByteValidationFailure> validateNonNullBytes(final byte[] bytes)
    {
        final Set<ByteValidationFailure> failures = Sets.newHashSet();
        if (bytes.length == 0)
        {
            final String error = String.format(EMPTY_BYTE_ARRAY_VALIDATION_ERROR, "ENUMERATED");
            final ByteValidationFailure failure = new ByteValidationFailure(bytes.length,
                    FailureType.DataIncorrectlyFormatted,
                    error);
            failures.add(failure);
        }
        return ImmutableSet.copyOf(failures);
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Decoding and validating both require this common code.
     *
     * @param tag
     *         tag to validate
     * @param asnData
     *         data to retrieve tag from
     *
     * @return the matching tag if successful, otherwise an appropriate DecodedTagValidationFailure
     */
    public OperationResult<String, ImmutableSet<DecodedTagValidationFailure>> validateAndDecode(
            final String tag, final AsnData asnData)
    {
        final Set<DecodedTagValidationFailure> tagFailures = Sets.newHashSet();

        // Validation of the enumerated type requires that the bytes are valid (they decode
        // to an integer), that it meets its constraints (all can be done through the parent
        // validate)
        tagFailures.addAll(super.validate(tag, asnData));

        try
        {
            // AND that the decoded integer aligns with a named tag for the
            // Enumerated type as defined by its schema.
            final BigInteger value = AsnByteDecoder.decodeAsInteger(asnData.getBytes(tag)
                    .get());
            final AsnSchemaType type = asnData.getType(tag).get();
            final String tagName = (String) type.accept(getNamedTagVisitor(value.toString()));

            if (tagName.isEmpty())
            {
                final String error = "Value of " + value
                        + " does not match enumerated values from schema";
                tagFailures.add(new DecodedTagValidationFailure(tag,
                        FailureType.DataIncorrectlyFormatted,
                        error));
            }
            else
            {
                return OperationResult.createSuccessfulInstance(tagName);
            }
        }
        catch (ParseException | DecodeException e)
        {
            tagFailures.add(new DecodedTagValidationFailure(tag,
                    FailureType.DataIncorrectlyFormatted,
                    "Exception while validating enumerated: " + e.getMessage()));
        }

        return OperationResult.createUnsuccessfulInstance(null, ImmutableSet.copyOf(tagFailures));
    }

    // -------------------------------------------------------------------------
    // PRIVATE METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns a new instance of an AsnSchemaTypeVisitor that can be used to get the matching named
     * value from a AsnSchemaTypeWithNamedTags type
     *
     * @param value
     *         the value that the named tag is keyed off.
     *
     * @return new instance of the visitor - call {@link AsnSchemaType#accept} on the AsnSchemaType
     * with this return value
     */
    private static AsnSchemaTypeVisitor getNamedTagVisitor(final String value)
    {
        return new AsnSchemaTypeVisitor<String>()
        {
            @Override
            public String visit(final AsnSchemaTypeConstructed visitable) throws ParseException
            {
                return "";
            }

            @Override
            public String visit(final BaseAsnSchemaType visitable) throws ParseException
            {
                return "";
            }

            @Override
            public String visit(final AsnSchemaTypeCollection visitable) throws ParseException
            {
                return "";
            }

            @Override
            public String visit(final AsnSchemaTypeWithNamedTags visitable) throws ParseException
            {
                final AsnSchemaNamedTag result = visitable.getTagsToNamedValues().get(value);
                return (result == null) ? "" : result.getTagName();
            }

            @Override
            public String visit(final AsnSchemaTypePlaceholder visitable) throws ParseException
            {
                return (String) visitable.getIndirectType().accept(this);
            }

            @Override
            public String visit(final AsnSchemaType.Null visitable) throws ParseException
            {
                return "";
            }
        };
    }
}
