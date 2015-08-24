/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.assam.data.AsnData;
import com.brightsparklabs.assam.validator.Validator;

/**
 * Static utility methods pertaining to {@link Validator} instances.
 *
 * @author brightSPARK Labs
 */
public class Validators
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** the default validator */
    private static Validator instance;

    // -------------------------------------------------------------------------
    // CONSTRUCTION
    // -------------------------------------------------------------------------

    /**
     * Private constructor.
     */
    private Validators()
    {
        // static utility class should never be instantiated
        throw new AssertionError();
    }

    // -------------------------------------------------------------------------
    // PUBLIC METHODS
    // -------------------------------------------------------------------------

    /**
     * Returns the default validator which validates {@link AsnData} against its corresponding
     * schema.
     *
     * @return the default validator
     */
    public static Validator getDefault()
    {
        /*
         * NOTE: Not thread safe, but unlikely this method will actually be called. Generally a
         * custom validator will be used.
         */
        if (instance == null)
        {
            instance = ValidatorImpl.builder().build();
        }
        return instance;
    }

    /**
     * Returns a builder for creating a Validator which which validates {@link AsnData} against its
     * corresponding schema as well as any custom validation rules.
     *
     * @return the default validator
     */
    public static ValidatorImpl.Builder newCustomValidatorBuilder()
    {
        return ValidatorImpl.builder();
    }
}
