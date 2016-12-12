package com.volvo.softproduct.sensorextensionlibrary.db_value;

import com.volvo.softproduct.sensorextensionlibrary.db_enum.*;
/**
 * Created by a227304 on 2016-05-23.
 */
public class flag_value {
    private long _timestamp;
    private byte _bvalue;
    private value_status _status;

    /**
     * Constructor flag_value()
     */
    public flag_value(byte bval,value_status status, long time)
    {
        _status = status;
        _bvalue = bval;
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
    public byte getValue(){return _bvalue;};

    /**
     * getTimestamp()
     *
     * @return    The value.
     */
    public long getTimestamp(){return _timestamp;};

    /**
     * setStatus()
     *
     * @param    value - status of the value.
     */
    public void setStatus(value_status value){_status = value;};

    /**
     * setValue()
     *
     * @param    value - the value
     */
    public void setValue(byte value){_bvalue = value;};

    /**
     * setTimestamp()
     *
     * @param    time - timstampe of the value
     */
    public void setTimestamp(long time){_timestamp = time;};
}
