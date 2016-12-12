package com.volvo.softproduct.canparser;

import com.volvo.softproduct.canparser.SignalType;

/**
 * Class used to return the parsed signal (value, unit description).
 * Definition of a signal is:
 * "a part of the 64 bit data value that can be interprented as a presentable value"
 * Note! There can be several signals in one CAN Frame message (each signal on different bits in the 64 bits data value)
 *
 * Output:
 * DoubleValue/LongValue/FlagValue = The value that is parsed.
 * Type = A flag that indicates if the parsed value is DOUBLE, LONG or a FLAG.
 * Unit = Unit of the signal (e.g m/s)
 * SignalDescription = Description of the signal
 */
public class SignalValue
{
    private SignalType _type;
    private double _dValue;
    private long _lValue;
    private byte _bValue;
    private String _unit;
    private String _description;
    private int _canId;
    private int _startBit;

    /**
    * Constructor SignalValue()
    */
    public SignalValue()
    {
        _type = SignalType.NOVALUE;
        _dValue = 0.0;
        _lValue = 0;
        _bValue = 0;
        _canId = -1;
        _startBit = -1;
        _unit = "";
        _description = "";
    }

	 /**
     * getDoubleValue()
     *
     * @return    The value that is parsed.     
     */
    public double getDoubleValue(){return _dValue;};

	 /**
     * getLongValue()
     *
     * @return    The value that is parsed.     
     */
    public long getLongValue(){return _lValue;};

	 /**
     * getByteValue()
     *
     * @return    The value that is parsed.     
     */
    public byte getByteValue(){return _bValue;};

	 /**
     * getTypeValue()
     *
     * @return   A flag that indicates if the parsed value is DOUBLE, LONG or a FLAG..     
     */
    public SignalType getTypeValue(){return _type;};

	 /**
     * getUnit()
     *
     * @return    Unit of the signal (e.g m/s)
     */
    public String getUnit(){return _unit;};

	 /**
     * getSignalDescription()
     *
     * @return    Description of the signal     
     */
    public String getSignalDescription(){return _description;};

    /**
     * getCanId()
     *
     * @return    ID of the CAN Frame.
     */
    public int getCanId(){return _canId;};

    /**
     * getStartBit()
     *
     * @return    Start bit of the signal (counter from least significant bit)
     */
    public int getStartBit(){return _startBit;};

	/**
     * setDoubleValue()
     *
     * @param    value The value that is parsed
     */
    public void setDoubleValue(double value){_dValue = value;};

	/**
     * setLongValue()
     *
     * @param    value The value that is parsed
     */
    public void setLongValue(long value){_lValue = value;};

	/**
     * setByteValue()
     *
     * @param    value The value that is parsed
     */
    public void setByteValue(byte value){_bValue = value;};

	/**
     * setType()
     *
     * @param    value A flag that indicates if the parsed value is DOUBLE, LONG or a FLAG..
     */
    public void setType(SignalType value){_type = value;};

	/**
     * setUnit()
     *
     * @param    value Unit of the signal (e.g m/s).
     */
    public void setUnit(String value){_unit = value;};

	/**
     * setSignalDescription()
     *
     * @param    value Description of the signal
     */
    public void setSignalDescription(String value ){_description = value;};

    /**
     * setCanId()
     *
     * @param    value ID of the CAN Frame.
     */
    public void setCanId(int value){_canId = value;};

    /**
     * setStartBit()
     *
     * @param    value Start bit of the signal (counter from least significant bit)
     */
    public void setStartBit(int value){_startBit = value;};
}
