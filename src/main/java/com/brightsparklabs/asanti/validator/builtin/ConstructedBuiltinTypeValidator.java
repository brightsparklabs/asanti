/*
 * Maintained by brightSPARK Labs.
 * www.brightsparklabs.com
 *
 * Refer to LICENSE at repository root for license details.
 */

package com.brightsparklabs.asanti.validator.builtin;

import com.brightsparklabs.asanti.model.data.AsantiAsnData;
import com.brightsparklabs.asanti.model.schema.tag.DecodedTagsHelpers;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaComponentType;
import com.brightsparklabs.asanti.model.schema.type.AsnSchemaType;
import com.brightsparklabs.asanti.schema.AsnBuiltinType;
import com.brightsparklabs.asanti.validator.FailureType;
import com.brightsparklabs.asanti.validator.failure.ByteValidationFailure;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class implementing {@link BuiltinTypeValidator} for Constructed types, ie{@link
 * AsnBuiltinType#Sequence}, {@link AsnBuiltinType#Set} and {@link AsnBuiltinType#Choice} ASN.1
 *
 * @author brightSPARK Labs
 */
public class ConstructedBuiltinTypeValidator implements BuiltinTypeValidator {
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger =
            LoggerFactory.getLogger(ConstructedBuiltinTypeValidator.class);

    /** singleton instance */
    private static ConstructedBuiltinTypeValidator instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Default constructor.
     *
     * <p>This is private, use {@link #getInstance()} to obtain an instance
     */
    private ConstructedBuiltinTypeValidator() {}

    /**
     * Returns a singleton instance of this class
     *
     * @return a singleton instance of this class
     */
    public static ConstructedBuiltinTypeValidator getInstance() {
        if (instance == null) {
            instance = new ConstructedBuiltinTypeValidator();
        }
        return instance;
    }

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: BuiltinTypeValidator
    // -------------------------------------------------------------------------

    @Override
    public ImmutableSet<DecodedTagValidationFailure> validate(String tag, AsantiAsnData asnData) {
        final ImmutableSet<String> childTags =
                DecodedTagsHelpers.getImmediateChildren(asnData, tag);

        // to have gotten a mapped tag the schema look up must have previously worked,
        // so it is safe to assume it will work here.  If it fails then throwing is the right thing
        // to do.  (noting the .get() at the end of the Optional)
        final AsnSchemaType type = asnData.getType(tag).get();

        final Set<DecodedTagValidationFailure> failures = Sets.newHashSet();

        final ImmutableList<AsnSchemaComponentType> allComponents = type.getAllComponents();
        for (AsnSchemaComponentType component : allComponents) {
            if (!component.isOptional()) {
                if (!childTags.contains(component.getName())) {
                    final String fullyQualifiedTag = tag + "/" + component.getName();
                    final DecodedTagValidationFailure failure =
                            new DecodedTagValidationFailure(
                                    fullyQualifiedTag,
                                    FailureType.MandatoryFieldMissing,
                                    "Mandatory field was not found in the data");
                    logger.warn("Mandatory field {} was not found in the data", fullyQualifiedTag);
                    failures.add(failure);
                }
            }
        }

        return ImmutableSet.copyOf(failures);
    }

    @Override
    public ImmutableSet<ByteValidationFailure> validate(final byte[] bytes) {
        return ImmutableSet.of();
    }
}
