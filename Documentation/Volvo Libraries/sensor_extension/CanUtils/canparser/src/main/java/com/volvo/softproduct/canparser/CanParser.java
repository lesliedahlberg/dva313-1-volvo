package com.volvo.softproduct.canparser;

import java.util.LinkedList;
import java.util.List;

/**
 * Class used to parse received CAN frame message.
 * Definition of a signal is:
 * "a part of the 64 bit data value that can be interprented as a presentable value"
 * Note! There can be several signals in one CAN Frame message (each signal on different bits in the 64 bits data value)
 *
 * After a signal is parsed, the CANParser also runs the defined (if any) mathematical operations
 * to scale the value to presentation values.
 * The presentation values are stored and returned (list of SignalValue)
 *
 * The signals are described in a list of SignalDefinition. The
 * signals are added through the method "Add".
 *
 * Each SignalDefinition also contains information (the conversion rules);
 */
public class CanParser
{
    private List<SignalDefinition> _signaldefinitions;

    /**
     * Constructor - CanParser()
     */
    public CanParser()
    {
        _signaldefinitions = new LinkedList<SignalDefinition>();
        _signaldefinitions.clear();
    }

    /**
     * PRIVATE - getSubDataValue()
     *
     * @param  data              64 bits data values (received from the CAN Frame message
     * @param  startBit          Start bit of the signal (counter from least significant bit)
     * @param  numberOfDataBits  Start bit of the signal (counter from least significant bit)
     * @return                   The data is received as a 8 byte array, this is converted to a 64 bit value and signal values is parsed out.
     *
     */
    private long getSubDataValue(byte[] data, int startBit, int numberOfDataBits)
    {

        long dataLongValue = 0;
        for (int i = 7; i >= 0; i--)
        {
            if(i < data.length)
                dataLongValue = (dataLongValue << 8) + (data[i] & 0xff);
            else
                dataLongValue = (dataLongValue << 8) + (0 & 0xff);
        }

        long resValue = dataLongValue >> startBit;

        long maskValue = 0;
        long bitValue = 1;
        int bitCounter = 0;
        for (int i = 0; i < 64; i++)
        {
            if(i < numberOfDataBits)
            {
                maskValue |= (long)(bitValue << i);
            }
        }
        resValue = resValue & maskValue;

        return resValue;
    }

    /**
     * PRIVATE - ParseFrameDouble()
     *
     * @param  message  CanFrame message
     * @param  sd       Definition of the signal that we should parse out.
     * @return          Parsed and scaled signal value.
     *
     */
    private double ParseFrameDouble(CanFrame message, SignalDefinition sd)
    {
        double returnValue = 0.0;
        long resValue = getSubDataValue(message.data,sd.getStartBit(), sd.getNumberOfDataBits());

        returnValue = (double)resValue;
        for (SignalScale s: sd.getScaleValues())
        {
            SignalScaleType theType = s.getScaleType();
            switch (theType)
            {
                case ADD:
                    returnValue += s.getScaleValue();
                    break;
                case SUBTRACT:
                    returnValue -= s.getScaleValue();
                    break;
                case MULTIPLY:
                    returnValue *= s.getScaleValue();
                    break;
                case DIVIDE:
                    returnValue /= s.getScaleValue();
                    break;
                case TWOCOMP:
                    long twoCompMask = 0;
                    twoCompMask = 1 <<  (sd.getNumberOfDataBits() - 1);
                    long maskValue = resValue & twoCompMask;
                    long invertTwoCompMask = 0;
                    long maskedResValue = resValue;
                    if (maskValue == twoCompMask) //most significant bit is 1
                    {
                        for (int i = 0; i < sd.getNumberOfDataBits() - 1; i++)
                        {
                            invertTwoCompMask |= (long)(1 << i);
                        }
                        resValue = resValue ^ maskValue;
                        maskedResValue = resValue ^ invertTwoCompMask;
                        maskedResValue = maskedResValue + 1;
                        maskedResValue *= -1;
                    }
                    returnValue = (double)maskedResValue;
                    break;
                default:
                    break;
            }
        }
        return returnValue;
    }

    /**
     * PRIVATE - ParseFrameLong()
     *
     * @param  message  CanFrame message
     * @param  sd       Definition of the signal that we should parse out.
     * @return          Parsed and scaled signal value.
     *
     */
    private long ParseFrameLong(CanFrame message, SignalDefinition sd)
    {
        double returnValue = 0;
        long resValue = getSubDataValue(message.data,sd.getStartBit(), sd.getNumberOfDataBits());

        returnValue = (double)resValue;
        for (SignalScale s: sd.getScaleValues())
        {
            SignalScaleType theType = s.getScaleType();
            switch (theType)
            {
                case ADD:
                    returnValue += s.getScaleValue();
                    break;
                case SUBTRACT:
                    returnValue -= s.getScaleValue();
                    break;
                case MULTIPLY:
                    returnValue *= s.getScaleValue();
                    break;
                case DIVIDE:
                    returnValue /= s.getScaleValue();
                    break;
                case TWOCOMP:
                    long twoCompMask = 0;
                    twoCompMask = 1 <<  (sd.getNumberOfDataBits() - 1);
                    long maskValue = resValue & twoCompMask;
                    long invertTwoCompMask = 0;
                    long maskedResValue = resValue;
                    if (maskValue == twoCompMask) //most significant bit is 1
                    {
                        for (int i = 0; i < sd.getNumberOfDataBits() - 1; i++)
                        {
                            invertTwoCompMask |= (long)(1 << i);
                        }
                        resValue = resValue ^ maskValue;
                        maskedResValue = resValue ^ invertTwoCompMask;
                        maskedResValue = maskedResValue + 1;
                        maskedResValue *= -1;
                    }
                    returnValue = (double)maskedResValue;
                    break;
                default:
                    break;
            }
        }
        return (long)returnValue;
    }

    /**
     * PRIVATE - ParseFrameFlag()
     *
     * @param  message  CanFrame message
     * @param  sd       Definition of the signal that we should parse out.
     * @return          Parsed and scaled signal value.
     *
     */
    private byte ParseFrameFlag(CanFrame message, SignalDefinition sd)
    {
        double returnValue = 0;
        long resValue = getSubDataValue(message.data,sd.getStartBit(), sd.getNumberOfDataBits());

        returnValue = (double)resValue;
        for (SignalScale s: sd.getScaleValues())
        {
            SignalScaleType theType = s.getScaleType();
            switch (theType)
            {
                case ADD:
                    returnValue += s.getScaleValue();
                    break;
                case SUBTRACT:
                    returnValue -= s.getScaleValue();
                    break;
                case MULTIPLY:
                    returnValue *= s.getScaleValue();
                    break;
                case DIVIDE:
                    returnValue /= s.getScaleValue();
                    break;
                case TWOCOMP:
                    long twoCompMask = 0;
                    twoCompMask = 1 <<  (sd.getNumberOfDataBits() - 1);
                    long maskValue = resValue & twoCompMask;
                    long invertTwoCompMask = 0;
                    long maskedResValue = resValue;
                    if (maskValue == twoCompMask) //most significant bit is 1
                    {
                        for (int i = 0; i < sd.getNumberOfDataBits() - 1; i++)
                        {
                            invertTwoCompMask |= (long)(1 << i);
                        }
                        resValue = resValue ^ maskValue;
                        maskedResValue = resValue ^ invertTwoCompMask;
                        maskedResValue = maskedResValue + 1;
                        maskedResValue *= -1;
                    }
                    returnValue = (double)maskedResValue;
                    break;
                default:
                    break;
            }
        }
        return (byte)returnValue;
    }

    /**
     * PUBLIC - ParseFrame()
     * Parse a CanFrame and split it up in the defined signalvalues that is found.
     * All signal is also scaled to presentation values according to the defined scale rules.
     *
     * @param  message  CAN Frame message
     * @return          List of all parsed and scaled signal values that matched the ID in the CanFrame
     *                  (according to our SignalDefinition).
     *
     */
    public List<SignalValue> ParseFrame(CanFrame message)
    {
        List<SignalValue> returnValues = new LinkedList<SignalValue>();

        for (SignalDefinition s: _signaldefinitions)
        {
            if(message.id == s.getCanId()) {
                SignalValue value = new SignalValue();
                value.setUnit(s.getUnit());
                value.setSignalDescription(s.getSignalDescription());
                value.setCanId(s.getCanId());
                value.setStartBit(s.getStartBit());
                SignalType theType = s.getOutputType();
                switch (theType) {
                    case FLAG:
                        value.setType(SignalType.FLAG);
                        value.setByteValue(ParseFrameFlag(message, s));
                        break;
                    case LONG:
                        value.setType(SignalType.LONG);
                        value.setLongValue(ParseFrameLong(message, s));
                        break;
                    case DOUBLE:
                        value.setType(SignalType.DOUBLE);
                        value.setDoubleValue(ParseFrameDouble(message, s));
                        break;
                    default:
                        break;
                }
                returnValues.add(value);
            }
        }
        return returnValues;
    }

    /**
     * PUBLIC - Add()
     * Use this method to add the signal definitions to the parser object.
     * All CanFrame messages that matches a signal definition will be parsed and the signal value will
     * be scaled to presentation values according to the defined scale rules.
     *
     * @param  newsignal  Signal definition
     *
     */
    public void Add(SignalDefinition newsignal)
    {
        _signaldefinitions.add(newsignal);
    }
}
