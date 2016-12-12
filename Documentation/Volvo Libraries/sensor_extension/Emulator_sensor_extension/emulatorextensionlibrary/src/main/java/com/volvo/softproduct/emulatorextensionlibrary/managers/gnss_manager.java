package com.volvo.softproduct.emulatorextensionlibrary.managers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.volvo.softproduct.canparser.SignalType;
import com.volvo.softproduct.canparser.SignalValue;
import com.volvo.softproduct.emulatorextensionlibrary.db_enum.gnss_data;
import com.volvo.softproduct.emulatorextensionlibrary.db_enum.value_status;
import com.volvo.softproduct.emulatorextensionlibrary.db_value.flag_value;
import com.volvo.softproduct.emulatorextensionlibrary.db_value.float_value;
import com.volvo.softproduct.emulatorextensionlibrary.db_value.long_value;
import com.volvo.softproduct.emulatorextensionlibrary.helpers.CanFrame;
import com.volvo.softproduct.emulatorextensionlibrary.helpers.can_id_map;
import com.volvo.softproduct.emulatorextensionlibrary.helpers.signal_map;
import com.volvo.softproduct.emulatorextensionlibrary.helpers.ttd;
import com.volvo.softproduct.emulatorextensionlibrary.helpers.ttd_gnss;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by a227304 on 2016-10-19.
 */
public class gnss_manager {
    private Context _activityContext;
    private Intent _sendIntent;
    private long                _startTime;
    private Queue<ttd_gnss> _queueGNSS = new LinkedList<ttd_gnss>();

    /**
     * Constructor gnss_manager()
     *
     * @param  context   Context from Activity that uses the manager object.
     */
    public gnss_manager(Context context)
    {
        _activityContext = context;

        File filegnss = new File("/data/local/tmp/gnssdata.csv");
        try {
            if(filegnss.exists()) {
                String theFile = filegnss.getAbsolutePath().toString();
                BufferedReader br = new BufferedReader(new FileReader(theFile));
                String line;

                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if(line.length() > 0) {
                        line = line.replaceAll(";", "");
                        String[] candata = line.split(",");
                        long timestamp = Long.parseLong(candata[0]);
                        double latitude = Double.parseDouble(candata[1]);
                        double longitude = Double.parseDouble(candata[2]);
                        double altitude = Double.parseDouble(candata[3]);
                        byte longLatSqi = Byte.parseByte(candata[4]);
                        double heading = Double.parseDouble(candata[5]);
                        byte headingSqi = Byte.parseByte(candata[6]);
                        double speedOverGround = Double.parseDouble(candata[7]);
                        double courseOverGround = Double.parseDouble(candata[8]);
                        byte cogSogSqi = Byte.parseByte(candata[9]);
                        double ageOfDiff = Double.parseDouble(candata[10]);
                        byte gnssQuality = Byte.parseByte(candata[11]);
                        long gnssTime = Long.parseLong(candata[12]);

                        _queueGNSS.add(new ttd_gnss(gnssTime,gnssQuality,ageOfDiff,(byte)longLatSqi,
                                latitude,longitude, altitude,(byte)headingSqi,heading,
                                (byte)cogSogSqi, courseOverGround, speedOverGround,timestamp));
                    }
                }
                br.close();
            }
        }
        catch (IOException e) {
            Log.d("gnss_manager","GNSS Exception: " + e.getMessage().toString());
            //You'll need to add proper error handling here
        }


    }

    /**
     * Connect() - Call this to connect and start the service that listen on the CAN4 network and parses the data.
     *
     * @return    In this version always true...
     *
     */
    public boolean Connect(){

        _startTime = System.currentTimeMillis();
        return true;
    }

    /**
     * Disconnect() - Not used in the Emulator.
     *
     * @return    In this version always true...
     *
     */
    public boolean Disconnect() {
        return true;
    }

    public void startRecording()
    {
        Log.d("Emulator manager", "Note! Method 'startRecording' is not implemented!");
    }

    public void stopRecording()
    {
        Log.d("Emulator manager", "Note! Method 'stopRecording' is not implemented!");
    }

    public void saveRecording()
    {
        Log.d("Emulator manager", "Note! Method 'saveRecording' is not implemented!");
    }

    /**
     * getFlagSignal() - Call this to retrieve a signal value
     *
     * @param  dataid   Decide which data to fetch and return (see available signals in wlo_data,exc_data and art_data)
     * @return    The parsed and scaled value
     *
     */
    public flag_value getFlagSignal(int dataid){
        long currentTime = System.currentTimeMillis();
        long timeslot = currentTime - _startTime;
        ttd_gnss itemdata = null;
        boolean itemdatafound = false;

        Iterator it= _queueGNSS.iterator();
        while (it.hasNext()) {
            ttd_gnss item = (ttd_gnss) it.next();
            if (item.Timestamp < timeslot) //check next item
            {
                itemdata = item;
                itemdatafound = true;
            }
            else
            {
                break;
            }
        }

        flag_value val;
        value_status status = value_status.OK;
        if(itemdatafound) {
            if (timeslot - itemdata.Timestamp > 1000) //Check if OBSOLETE
                status = value_status.OBSOLETE;
            else
                status = value_status.OK;

            byte flagval = 0;
            if(dataid == gnss_data.gnssQuality.getCode())
                flagval = (byte)itemdata.GnssQuality;
            else if(dataid == gnss_data.headingSqi.getCode())
                flagval = (byte)itemdata.HeadingSqi;
            else if(dataid == gnss_data.cogSogSqi.getCode())
                flagval = (byte)itemdata.CogSogSqi;
            else if(dataid == gnss_data.longLatSqi.getCode())
                flagval = (byte)itemdata.LongLatSqi;

            val = new flag_value((byte) flagval, status, 0);
        }
        else
            val = new flag_value((byte) 0, value_status.ERR, 0);
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
        long currentTime = System.currentTimeMillis();
        long timeslot = currentTime - _startTime;
        ttd_gnss itemdata = null;
        boolean itemdatafound = false;

        Iterator it= _queueGNSS.iterator();
        while (it.hasNext()) {
            ttd_gnss item = (ttd_gnss) it.next();
            if (item.Timestamp < timeslot) //check next item
            {
                itemdata = item;
                itemdatafound = true;
            }
            else
            {
                break;
            }
        }

        float_value val;
        value_status status = value_status.OK;
        if(itemdatafound) {
            if (timeslot - itemdata.Timestamp > 1000) //Check if OBSOLETE
                status = value_status.OBSOLETE;
            else
                status = value_status.OK;

            double floatval = 0;
            if(dataid == gnss_data.ageOfDiff.getCode())
                floatval = (byte)itemdata.AgeOfDiff;
            else if(dataid == gnss_data.latitude.getCode())
                floatval = (byte)itemdata.Latitude;
            else if(dataid == gnss_data.longitude.getCode())
                floatval = (byte)itemdata.Longitude;
            else if(dataid == gnss_data.altitude.getCode())
                floatval = (byte)itemdata.Altitude;
            else if(dataid == gnss_data.heading.getCode())
                floatval = (byte)itemdata.Heading;
            else if(dataid == gnss_data.courseOverGround.getCode())
                floatval = (byte)itemdata.CourseOverGround;
            else if(dataid == gnss_data.speedOverGround.getCode())
                floatval = (byte)itemdata.SpeedOverGround;

            val = new float_value((float) floatval, status, 0);
        }
        else
            val = new float_value((float) 0.0, value_status.ERR, 0);
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
        long currentTime = System.currentTimeMillis();
        long timeslot = currentTime - _startTime;
        ttd_gnss itemdata = null;
        boolean itemdatafound = false;

        Iterator it= _queueGNSS.iterator();
        while (it.hasNext()) {
            ttd_gnss item = (ttd_gnss) it.next();
            if (item.Timestamp < timeslot) //check next item
            {
                itemdata = item;
                itemdatafound = true;
            }
            else
            {
                break;
            }
        }

        long_value val;
        value_status status = value_status.OK;
        if(itemdatafound) {
            if (timeslot - itemdata.Timestamp > 1000) //Check if OBSOLETE
                status = value_status.OBSOLETE;
            else
                status = value_status.OK;

            long longval = 0;
            if(dataid == gnss_data.gnssTime.getCode())
                longval = (byte)itemdata.AgeOfDiff;

            val = new long_value((long) longval, status, 0);
        }
        else
            val = new long_value((long) 0, value_status.ERR, 0);
        return val;
    }

    public void restartReplay()
    {
        _startTime = System.currentTimeMillis();
    }

    public long getCurrentTime()
    {
        long currentTime = System.currentTimeMillis();
        long timeslot = currentTime - _startTime;
        return timeslot;
    }

    public boolean isTimeScopeGNSSPassed()
    {
        long biggestTime = 0;
        long currentTime = System.currentTimeMillis();
        long timeslot = currentTime - _startTime;
        Iterator it= _queueGNSS.iterator();
        while (it.hasNext()) {
            ttd_gnss item = (ttd_gnss) it.next();
            if (item.Timestamp > biggestTime)
            {
                biggestTime = item.Timestamp;
            }
        }
        if(biggestTime < timeslot)
            return true;
        else
            return false;
    }
}

