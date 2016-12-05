package com.volvo.softproduct.sensorextensionlibrary.helpers;

import com.volvo.softproduct.canparser.SignalType;

public class signal_map
{
    private int _canId;
    private int _startBit;
    private int _numberOfBits;
    private double _factor;
    private double _offset;
    private SignalType _type;
    private String  _unit;
    private String _description;
    private int _storageid;
    private boolean _twocomp;

    /**
     * Constructor signal_map()
     *
     * @param    canId - ID of the CAN Frame.
     * @param    startBit - Start bit of the signal (counter from least significant bit)
     * @param    numberOfBits - Number of bit the signal occupies (counted from the startBit)
     * @param    factor - Factor to scale value with
     * @param    offset - Offset to use in scaling
     * @param    type - Type of return value
     * @param    unit - Unit of signal value
     * @param    description - Description of signal
     * @param    storageid - Storage row name.
     */
    public signal_map(int canId, int startBit, int numberOfBits, double factor, double offset, boolean twocomp,
                       SignalType type, String unit, String description, int storageid)
    {
        _canId = canId;
        _startBit = startBit;
        _numberOfBits = numberOfBits;
        _factor = factor;
        _offset = offset;
        _type = type;
        _unit = unit;
        _description = description;
        _storageid = storageid;
        _twocomp = twocomp;
    }

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
     * getStartBit()
     *
     * @return    Number of bit the signal occupies (counted from the startBit)
     */
    public int getNumberOfBits(){return _numberOfBits;};

    /**
     * getFactor()
     *
     * @return    Factor to scale value with.
     */
    public double getFactor(){return _factor;};

    /**
     * getOffset()
     *
     * @return    Offset to use in scaling
     */
    public double getOffset(){return _offset;};


    /**
     * getType()
     *
     * @return    Type of return value
     */
    public SignalType getType(){return _type;};

    /**
     * getUnit()
     *
     * @return    Unit of signal value
     */
    public String getUnit(){return _unit;};

    /**
     * getDescription()
     *
     * @return    Description of signal
     */
    public String getDescription(){return _description;};

    /**
     * getStorageId()
     *
     * @return    Storage of the signal value
     */
    public int getStorageId(){return _storageid;};

    /**
     * getTwoComp()
     *
     * @return    If value are to be treated as a two compoment value
     */
    public boolean getTwoComp(){return _twocomp;};

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

    /**
     * setNumberOfDataBits()
     *
     * @param    value Number of bit the signal occupies (counted from the startBit)
     */
    public void setNumberOfBits(int value){_numberOfBits = value;};

    /**
     * setFactor()
     *
     * @param    value Factor to scale value with.
     */
    public void setFactor(double value){_factor = value;};

    /**
     * setOffset()
     *
     * @param    value Offset used in scaling.
     */
    public void setOffset(double value){_offset = value;};

    /**
     * setType()
     *
     * @param    value Type of return value.
     */
    public void setType(SignalType value){_type = value;};

    /**
     * setUnit()
     *
     * @param    value Unit of signal value.
     */
    public void setUnit(String value){_unit = value;};

    /**
     * setDescription()
     *
     * @param    value Description of signal value.
     */
    public void setDescription(String value){_description = value;};

    /**
     * setStorageId()
     *
     * @param    value Storage of the signal value.
     */
    public void setStorageId(int value){_storageid = value;};

    /**
     * setTwoComp()
     *
     * @param     value If value are to be treated as a two compoment value
     */
    public void setTwoComp(boolean value ){_twocomp = value;};
}
