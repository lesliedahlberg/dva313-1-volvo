package com.volvo.softproduct.canparser;

/**
 * Class used to describe a mathematical operation that should be used
 * in the scaling of a parsed signal received in a CAN frame message.
 */
public class SignalScale
{
    private SignalScaleType _type;
    private double _scaleValue;

    /**
     * SignalScale()
     *
     * @param  type   Kind of operation (ADD, SUBTRACT, MULTIPLY, DIVIDE)
     * @param  value  The value to Add, Subtract, Multiply or Divide with.
     */
    public SignalScale(SignalScaleType type, double value)
    {
        _type = type;
        _scaleValue = value;
    }

	 /**
     * getScaleValue()
     *
     * @return    The value to Add, Subtract, Multiply or Divide with.     
     */
    public double getScaleValue(){return _scaleValue;};

	/**
     * getScaleType()
     *
     * @return    Kind of operation (ADD, SUBTRACT, MULTIPLY, DIVIDE)  
     */
    public SignalScaleType getScaleType(){return _type;};


	/**
     * setScaleValue()
     *
     * @param    value The value to Add, Subtract, Multiply or Divide with.
     */
    public void setScaleValue(double value){_scaleValue = value;};

	/**
     * setScaleType()
     *
     * @param    value Kind of operation (ADD, SUBTRACT, MULTIPLY, DIVIDE)
     */
    public void setScaleType(SignalScaleType value){_type = value;};
}
