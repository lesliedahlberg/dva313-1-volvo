package com.volvo.softproduct.sensorextensionlibrary.managers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.volvo.softproduct.sensorextensionlibrary.db_enum.value_status;
import com.volvo.softproduct.sensorextensionlibrary.db_value.flag_value;
import com.volvo.softproduct.sensorextensionlibrary.db_value.float_value;
import com.volvo.softproduct.sensorextensionlibrary.db_value.long_value;
import com.volvo.softproduct.sensorextensionlibrary.services.sensor_extension_listener_service;

/**
 * Created by a227304 on 2016-10-19.
 */
public class gnss_manager {
    private Context             _activityContext;
    private Intent              _sendIntent;
    private long                _startTime;
    private SharedPreferences   _simulatorValuePrefs;

    /**
     * Constructor gnss_manager()
     *
     * @param  context   Context from Activity that uses the manager object.
     */
    public gnss_manager(Context context)
    {
        _activityContext = context;

        _sendIntent =  new Intent(sensor_extension_listener_service.BROADCAST_ACTION);
        _simulatorValuePrefs = _activityContext.getSharedPreferences("simulatorvalue", _activityContext.MODE_WORLD_READABLE);
    }

    /**
     * Connect() - Call this to connect and start the service that listen on the CAN4 network and parses the data.
     *
     * @return    In this version always true...
     *
     */
    public boolean Connect(){
        Intent managerService = new Intent(_activityContext, sensor_extension_listener_service.class);
        _activityContext.startService(managerService);
        return true;
    }

    /**
     * Disconnect() - Call this to disconnect and shutdown the service that listen on the CAN4 network and parses the data.
     *
     * @return    In this version always true...
     *
     */
    public boolean Disconnect() {
        //Intent managerService = new Intent(_activityContext, com.volvo.softproduct.sensorextensionlibrary.services.sensor_extension_listener_service.class);
        //_activityContext.stopService(managerService);
        SendMessage("LISTENER_STOP");
        return true;
    }

    public void startRecording()
    {
        SendMessage("ACTION_START_REC");
        _startTime = System.currentTimeMillis();
    }

    public void stopRecording()
    {
        SendMessage("ACTION_STOP_REQ");
    }

    public void saveRecording()
    {
        SendMessage("ACTION_SAVE_REC");
    }

    private void SendMessage(String message)
    {
        _sendIntent.putExtra("message", message);
        _activityContext.sendBroadcast(_sendIntent);
    }

    /**
     * getFlagSignal() - Call this to retrieve a signal value
     *
     * @param  dataid   Decide which data to fetch and return (see available signals in wlo_data,exc_data and art_data)
     * @return    The parsed and scaled value
     *
     */
    public flag_value getFlagSignal(int dataid){
        flag_value val;
        val = RestoreFPreferencese(dataid);
        return val;
    }

    /**
     * getFloatSignal() - Call this to retrieve a signal value
     *
     * @param  dataid   Decide which data to fetch and return (see available signals in wlo_data,exc_data and art_data)
     * @return    The parsed and scaled value
     *
     */
    public float_value getFloatSignal(int dataid){
        float_value val;
        val = RestoreDPreferencese(dataid);
        return val;
    }

    /**
     * getFloatSignal() - Call this to retrieve a signal value
     *
     * @param  dataid   Decide which data to fetch and return (see available signals in wlo_data,exc_data and art_data)
     * @return    The parsed and scaled value
     *
     */
    public long_value getLongSignal(int dataid){
        long_value val;
        val = RestoreLPreferencese(dataid);
        return val;
    }

    private final String DATA_STATE = "data_";
    private final String TIME_STATE = "time_";
    private long_value RestoreLPreferencese(int dataid)
    {
        long currenttime = System.currentTimeMillis();
        String prefData = _simulatorValuePrefs.getString(DATA_STATE+dataid, "0");
        String prefTime = _simulatorValuePrefs.getString(TIME_STATE+dataid, "-1");
        long time = Long.parseLong(prefTime);
        value_status status = value_status.OK;
        if(time == -1)
            status = value_status.ERR;
        else if(currenttime - time > 1000)
            status = value_status.OBSOLETE;
        return new long_value(Long.parseLong(prefData),status,time);
    }

    private flag_value RestoreFPreferencese(int dataid)
    {
        long currenttime = System.currentTimeMillis();
        String prefData = _simulatorValuePrefs.getString(DATA_STATE+dataid, "0");
        String prefTime = _simulatorValuePrefs.getString(TIME_STATE+dataid, "-1");
        long time = Long.parseLong(prefTime);
        value_status status = value_status.OK;
        if(time == -1)
            status = value_status.ERR;
        else if(currenttime - time > 1000)
            status = value_status.OBSOLETE;
        return new flag_value(Byte.parseByte(prefData),status,time);
    }

    private float_value RestoreDPreferencese(int dataid)
    {
        long currenttime = System.currentTimeMillis();
        String prefData = _simulatorValuePrefs.getString(DATA_STATE+dataid, "0.0");
        String prefTime = _simulatorValuePrefs.getString(TIME_STATE+dataid, "-1");
        long time = Long.parseLong(prefTime);
        value_status status = value_status.OK;
        if(time == -1)
            status = value_status.ERR;
        else if(currenttime - time > 1000)
            status = value_status.OBSOLETE;
        return new float_value((float)Double.parseDouble(prefData),status,time);
    }

    public void restartReplay()
    {
        Log.d("Simulator manager", "Note! Method 'restartReplay' is not implemented!");
    }

    public long getCurrentTime()
    {
        long currentTime = System.currentTimeMillis();
        long timeslot = currentTime - _startTime;
        return timeslot;
    }


    public boolean isTimeScopeCAN4Passed()
    {
        Log.d("Simulator manager", "Note! Method 'isTimeScopeCAN4Passed' is not implemented!");
        return false;
    }

    public boolean isTimeScopeCAN3Passed()
    {
        Log.d("Simulator manager", "Note! Method 'isTimeScopeCAN3Passed' is not implemented!");
        return false;
    }
}

