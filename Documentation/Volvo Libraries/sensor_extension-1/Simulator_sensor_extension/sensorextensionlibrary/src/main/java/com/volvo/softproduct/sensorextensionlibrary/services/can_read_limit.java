package com.volvo.softproduct.sensorextensionlibrary.services;

import android.app.ActionBar;

import com.volvo.softproduct.sensorextensionlibrary.db_value.long_value;

/**
 * Created by a227304 on 2016-07-06.
 */
public class can_read_limit {
    public long Id;
    public String MessageName;

    public can_read_limit(long id, String messageName)
    {
        Id = id;
        MessageName = messageName;
    }

}
