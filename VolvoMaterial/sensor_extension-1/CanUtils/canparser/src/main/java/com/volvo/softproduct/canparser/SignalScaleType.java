package com.volvo.softproduct.canparser;

/**
 * Class used as a ENUM to describe mathematical operations used to scale the received signal
 * from Integers to presentation values.
 */
public enum SignalScaleType {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE,
    NOVALUE,
    TWOCOMP
}