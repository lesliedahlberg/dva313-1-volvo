package com.volvo.softproduct.emulatorextensionlibrary.db_value;

import com.volvo.softproduct.emulatorextensionlibrary.db_enum.*;
/**
 * Created by a227304 on 2016-05-23.
 */
public class long_value {
    private long _timestamp;
    private long _lvalue;
    private value_status _status;

    /**
     * Constructor long_value()
     */
    public long_value(long lval,value_status status, long time)
    {
        _status = status;
        _lvalue = lval;
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
    public long getValue(){return _lvalue;};

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
    public void setValue(long value){_lvalue = value;};

    /**
     * setTimestamp()
     *
     * @param    time - timstampe of the value
     */
    public void setTimestamp(long time){_timestamp = time;};
}
