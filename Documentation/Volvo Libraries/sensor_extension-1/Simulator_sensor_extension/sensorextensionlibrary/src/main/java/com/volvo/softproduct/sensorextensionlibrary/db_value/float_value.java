package com.volvo.softproduct.sensorextensionlibrary.db_value;

import com.volvo.softproduct.sensorextensionlibrary.db_enum.*;
/**
 * Created by a227304 on 2016-05-23.
 */
public class float_value {
    private long _timestamp;
    private float _dvalue;
    private value_status _status;

    /**
     * Constructor float_value()
     */
    public float_value(float dval,value_status status, long time)
    {
        _status = status;
        _dvalue = dval;
        _timestamp = time;
    }

    /**
     * getStatus()
     *
     * @return    The status of the value.
     */
    public value_status getStatus(){return _status;};

    /**
     * getValue()
     *
     * @return    The value.
     */
    public float getValue(){return _dvalue;};

    /**
     * getTimestamp()
     *
     * @return    The value.
     */
    public long getTimestamp(){return _timestamp;};

    /**
     * setStatus()
     *
     * @param    value - The status of the value.
     */
    public void setStatus(value_status value){_status = value;};

    /**
     * setValue()
     *
     * @param    value - The value
     */
    public void setValue(float value){_dvalue = value;};

    /**
     * setTimestamp()
     *
     * @param    time - timstampe of the value
     */
    public void setTimestamp(long time){_timestamp = time;};
}
