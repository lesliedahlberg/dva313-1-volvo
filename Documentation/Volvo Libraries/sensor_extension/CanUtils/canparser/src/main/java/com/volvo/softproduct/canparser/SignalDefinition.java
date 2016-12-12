package com.volvo.softproduct.canparser;

import java.util.List;

/**
 * Class used to describe the signal received in a CAN frame message.
 * Definition of a signal is:
 * "a part of the 64 bit data value that can be interprented as a presentable value"
 * Note! There can be several signals in one CAN Frame message (each signal on different bits in the 64 bits data value)
 */
public class SignalDefinition
{
    private int _canId;
    private int _startBit;
    private int _numberOfDataBits;
    private List<SignalScale> _scaleValues;
    private SignalType _outputType;
    private String _unit;
    private String _description;

    /**
     * Constructor SignalDefinition()
     *
     * @param  canId              ID of the CAN Frame
     * @param  startBit           Start bit of the signal (counter from least significant bit)
     * @param  numberOfDataBits   Number of bit the signal occupies (counted from the startBit)
     * @param  scaleValues        A list of mathematical operations used to scale the received signal from Integers to presentation values.
     * @param  outputType        A flag that indicates if the return value should be DOUBLE, LONG or a FLAG.
     * @param  unit               Unit of the signal (e.g m/s)
     * @param  signalDescription  Description of the signal
     */
    public SignalDefinition(int canId,int startBit, int numberOfDataBits, List<SignalScale> scaleValues, SignalType outputType, String unit, String signalDescription)
    {
        _canId = canId;
        _startBit = startBit;
        _numberOfDataBits = numberOfDataBits;
        _scaleValues = scaleValues;
        _outputType = outputType;
        _unit = unit;
        _description = signalDescription;
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
     * getNumberOfDataBits()
     *
     * @return    Number of bit the signal occupies (counted from the startBit)
     */
    public int getNumberOfDataBits(){return _numberOfDataBits;};

    /**
     * getScaleValues()
     *
     * @return     A list of mathematical operations used to scale the received signal from Integers to presentation values.
     */
    public List<SignalScale> getScaleValues(){return _scaleValues;};

    /**
     * getOutputType()
     *
     * @return    A flag that indicates if the return value should be DOUBLE, LONG or a FLAG.
     */
    public SignalType getOutputType(){return _outputType;};

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
    public void setNumberOfDataBits(int value){_numberOfDataBits = value;};

    /**
     * setScaleValues()
     *
     * @param    values A list of mathematical operations used to scale the received signal from Integers to presentation values.
     */
    public void setScaleValues(List<SignalScale> values){_scaleValues = values;};

    /**
     * setOutputType()
     *
     * @param    value A flag that indicates if the return value should be DOUBLE, LONG or a FLAG.
     */
    public void setOutputType(SignalType value){_outputType = value;};

    /**
     * setUnit()
     *
     * @param    value Unit of the signal (e.g m/s)
     */
    public void setUnit(String value){_unit = value;};

    /**
     * setSignalDescription()
     *
     * @param    value Description of the signal
     */
    public void setSignalDescription(String value ){_description = value;};
}
