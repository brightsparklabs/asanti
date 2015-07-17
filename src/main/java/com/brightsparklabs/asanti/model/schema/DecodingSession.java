package com.brightsparklabs.asanti.model.schema;

/**
 * An object to store state during the decoding of all the tags in an AsnData object. This is needed
 * because the ability to map the "raw" tags received from the BER data is dependent on what other
 * data has been received (notably OPTIONAL components of a SEQUENCE)
 */
public interface DecodingSession
{
    /**
     * @param context
     *         a unique string for each level of the hierarchy of a set of tags
     */
    void setContext(String context);

    /**
     * @param index
     *         the index of the tag that was received
     *
     * @return any offset needed to be applied to that index
     */
    int getOffset(int index);

    /**
     * @param index
     *         the index of the tag that was received
     * @param offset
     *         the value to be returned at the next call to {@code getOffset} with the given
     *         context
     */
    void setOffset(int index, int offset);

}
