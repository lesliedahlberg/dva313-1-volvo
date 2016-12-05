package com.volvo.softproduct.emulatorextensionlibrary.managers;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;

import com.volvo.softproduct.canparser.CanParser;
import com.volvo.softproduct.canparser.SignalDefinition;
import com.volvo.softproduct.canparser.SignalScale;
import com.volvo.softproduct.canparser.SignalScaleType;
import com.volvo.softproduct.canparser.SignalType;
import com.volvo.softproduct.canparser.SignalValue;
import com.volvo.softproduct.emulatorextensionlibrary.db_enum.*;
import com.volvo.softproduct.emulatorextensionlibrary.db_value.*;
import com.volvo.softproduct.emulatorextensionlibrary.helpers.CanFrame;
import com.volvo.softproduct.emulatorextensionlibrary.helpers.can_id_map;
import com.volvo.softproduct.emulatorextensionlibrary.helpers.signal_map;
import com.volvo.softproduct.emulatorextensionlibrary.helpers.ttd;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Class used to retrieve data from the machine sensor.
 */
public class machine_manager {
    private Context             _activityContext;
    private Intent	 			_sendIntent;
    private long                _startTime;
    private Queue<ttd>          _queueCAN3 = new LinkedList<ttd>();
    private Queue<ttd>          _queueCAN4 = new LinkedList<ttd>();
    private List<can_id_map>    _can_id_map;
    private CanParser           _theParser;
    private List<signal_map>    _wloSignalMap;
    private List<signal_map>    _excSignalMap;
    private List<signal_map>    _artSignalMap;
    private List<signal_map>    _signalMap;

    /**
     * Constructor machine_manager()
     *
     * @param  context   Context from Activity that uses the manager object.
     */
    public machine_manager(Context context)
    {
        _can_id_map = new LinkedList<can_id_map>();

         _can_id_map.add(new can_id_map(0x00001111, wlo_data.Red_central_warning.getCode()));
         _can_id_map.add(new can_id_map(0x00001111, wlo_data.Yellow_central_warning.getCode()));
         _can_id_map.add(new can_id_map(0x00001111, wlo_data.CDC_active.getCode()));
         _can_id_map.add(new can_id_map(0x00001111, wlo_data.BSS_active.getCode()));
         _can_id_map.add(new can_id_map(0x00001111, wlo_data.Tool_lock_active.getCode()));
         _can_id_map.add(new can_id_map(0x00001112, wlo_data.Parking_break_applied.getCode()));
         _can_id_map.add(new can_id_map(0x00001112, wlo_data.Active_gear.getCode()));
         _can_id_map.add(new can_id_map(0x00001112, wlo_data.Vehicle_speed.getCode()));
         _can_id_map.add(new can_id_map(0x00001112, wlo_data.Engine_speed.getCode()));
         _can_id_map.add(new can_id_map(0x00001114, wlo_data.Fuel_level.getCode()));
         _can_id_map.add(new can_id_map(0x00001114, wlo_data.Instant_fuel_consumption.getCode()));
         _can_id_map.add(new can_id_map(0x00001115, wlo_data.Weight_in_bucket.getCode()));
         _can_id_map.add(new can_id_map(0x00001115, wlo_data.Current_load.getCode()));
         _can_id_map.add(new can_id_map(0x00001115, wlo_data.Machine_hours.getCode()));
         _can_id_map.add(new can_id_map(0x00001116, wlo_data.Steering_angle.getCode()));
         _can_id_map.add(new can_id_map(0x00001116, wlo_data.Tilt_angle.getCode()));
         _can_id_map.add(new can_id_map(0x00001116, wlo_data.Boom_angle.getCode()));
         _can_id_map.add(new can_id_map(0x00001116, wlo_data.Outdoor_temp.getCode()));

         _can_id_map.add(new can_id_map(0x00001111, exc_data.Red_central_warning.getCode()));
         _can_id_map.add(new can_id_map(0x00001111, exc_data.Yellow_central_warning.getCode()));
         _can_id_map.add(new can_id_map(0x00001111, exc_data.Tool_lock_active.getCode()));
         _can_id_map.add(new can_id_map(0x00001112, exc_data.Parking_break_applied.getCode()));
         _can_id_map.add(new can_id_map(0x00001112, exc_data.Vehicle_speed.getCode()));
         _can_id_map.add(new can_id_map(0x00001112, exc_data.Engine_speed.getCode()));
         _can_id_map.add(new can_id_map(0x00001114, exc_data.Fuel_level.getCode()));
         _can_id_map.add(new can_id_map(0x00001114, exc_data.Instant_fuel_consumption.getCode()));
         _can_id_map.add(new can_id_map(0x00001115, exc_data.Weight_in_bucket.getCode()));
         _can_id_map.add(new can_id_map(0x00001115, exc_data.Current_load.getCode()));
         _can_id_map.add(new can_id_map(0x00001115, exc_data.Machine_hours.getCode()));
         _can_id_map.add(new can_id_map(0x00001116, exc_data.Outdoor_temp.getCode()));

         _can_id_map.add(new can_id_map(0x18FECA17, art_data.Red_central_warning.getCode()));
         _can_id_map.add(new can_id_map(0x18FECA17, art_data.Yellow_central_warning.getCode()));
         _can_id_map.add(new can_id_map(0x18FEF117, art_data.Parking_break_applied.getCode()));
         _can_id_map.add(new can_id_map(0x00001113, art_data.Active_gear.getCode()));
         _can_id_map.add(new can_id_map(0x18FEF117, art_data.Vehicle_speed.getCode()));
         _can_id_map.add(new can_id_map(0x0CF00417, art_data.Engine_speed.getCode()));
         _can_id_map.add(new can_id_map(0x18FEFC17, art_data.Fuel_level.getCode()));
         _can_id_map.add(new can_id_map(0x18FEF217, art_data.Instant_fuel_consumption.getCode()));
         _can_id_map.add(new can_id_map(0x00001115, art_data.Weight_in_bucket.getCode()));
         _can_id_map.add(new can_id_map(0x00001115, art_data.Current_load.getCode()));
         _can_id_map.add(new can_id_map(0x18FEE717, art_data.Machine_hours.getCode()));
         _can_id_map.add(new can_id_map(0x00001116, art_data.Steering_angle.getCode()));
         _can_id_map.add(new can_id_map(0x00001115, art_data.Unloaded.getCode()));
         _can_id_map.add(new can_id_map(0x00001115, art_data.Dump_body_up.getCode()));
         _can_id_map.add(new can_id_map(0x18FEF517, art_data.Outdoor_temp.getCode()));

        _theParser = new CanParser();
        _signalMap = new LinkedList<signal_map>();

        //Setup a map between canframe and UI element....
        //We need to know where we should present the data when it is received.
        _wloSignalMap = wloSignalSettingsFromStorage();
        _excSignalMap = excSignalSettingsFromStorage();
        _artSignalMap = artSignalSettingsFromStorage();

        for (signal_map s : _wloSignalMap) {
            if(_signalMap.contains(s)) {
                continue;
            }
            else {
                _signalMap.add(s);
            }
        }
        for (signal_map s : _excSignalMap) {
            if(_signalMap.contains(s)) {
                continue;
            }
            else {
                _signalMap.add(s);
            }
        }
        for (signal_map s : _artSignalMap) {
            if(_signalMap.contains(s)) {
                continue;
            }
            else {
                _signalMap.add(s);
            }
        }

        for (signal_map s : _signalMap) {
            //Define a signal and its conversion (scale) rules.
            List<SignalScale> conversionRules = new LinkedList<SignalScale>();
            if(s.getTwoComp())
                conversionRules.add(new SignalScale(SignalScaleType.TWOCOMP, s.getFactor()));
            conversionRules.add(new SignalScale(SignalScaleType.MULTIPLY, s.getFactor()));
            conversionRules.add(new SignalScale(SignalScaleType.ADD, s.getOffset()));
            _theParser.Add(new SignalDefinition(s.getCanId(), s.getStartBit(), s.getNumberOfBits(), conversionRules, s.getType(), s.getUnit(), s.getDescription()));
        }

        File file3 = new File("/data/local/tmp/can3data.csv");
        try {
            if(file3.exists()) {
                String theFile = file3.getAbsolutePath().toString();
                BufferedReader br3 = new BufferedReader(new FileReader(theFile));
                String line;

                while ((line = br3.readLine()) != null) {
                    line = line.trim();
                    if(line.length() > 0) {
                        line = line.replaceAll(";", "");
                        String[] candata = line.split(",");
                        long time = Long.parseLong(candata[0]);
                        int channel = Integer.parseInt(candata[1]);
                        int id = Integer.parseInt(candata[2]);
                        int dcl = Integer.parseInt(candata[3]);
                        byte[] data = new byte[8];
                        for (int ib = 0; ib < 8; ib++)
                            data[ib] = 0;
                        if(dcl > 0) {
                            if (candata[4].contains("_")) {
                                String[] bytedata = candata[4].split("_");
                                for (int idcl = 0; idcl < dcl; idcl++)
                                    data[idcl] = (byte) Integer.parseInt(bytedata[idcl]);
                            } else {
                                if (candata[4].length() > 0)
                                    data[0] = (byte) Integer.parseInt(candata[4]);
                            }
                        }
                        _queueCAN3.add(new ttd(new CanFrame(channel, id, dcl, data), time));
                    }
                }
                br3.close();
            }
        }
        catch (IOException e) {
            Log.d("machine_manager","CAN3 Exception: " + e.getMessage().toString());
            //You'll need to add proper error handling here
        }

        File file4 = new File("/data/local/tmp/can4data.csv");
        try {
            if(file4.exists()) {
                String theFile = file4.getAbsolutePath().toString();
                BufferedReader br4 = new BufferedReader(new FileReader(theFile));
                String line;

                while ((line = br4.readLine()) != null) {
                    line = line.trim();
                    if(line.length() > 0) {
                        line = line.replaceAll(";", "");
                        String[] candata = line.split(",");
                        long time = Long.parseLong(candata[0]);
                        int channel = Integer.parseInt(candata[1]);
                        int id = Integer.parseInt(candata[2]);
                        int dcl = Integer.parseInt(candata[3]);
                        byte[] data = new byte[8];
                        for (int ib = 0; ib < 8; ib++)
                            data[ib] = 0;
                        if(dcl > 0) {
                            if (candata[4].contains("_")) {
                                String[] bytedata = candata[4].split("_");
                                for (int idcl = 0; idcl < dcl; idcl++)
                                    data[idcl] = (byte) Integer.parseInt(bytedata[idcl]);
                            } else {
                                if (candata[4].length() > 0)
                                    data[0] = (byte) Integer.parseInt(candata[4]);
                            }
                        }
                        _queueCAN4.add(new ttd(new CanFrame(channel, id, dcl, data), time));
                    }
                }
                br4.close();
            }
        }
        catch (IOException e) {
            Log.d("machine_manager","CAN4 Exception: " + e.getMessage().toString());
            //You'll need to add proper error handling here
        }

        _activityContext = context;

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
        CanFrame itemdata = null;
        long itemtime = 0;
        boolean itemdatafound = false;

        //Seatch up the right can message (that fits the dataid and the correct time)
        for (can_id_map sid: _can_id_map)
        {
            if(sid.getStorageId() == dataid) {
                Iterator it4= _queueCAN4.iterator();
                while (it4.hasNext()) {
                    ttd item = (ttd) it4.next();
                    if(item.getCanFrame().id == sid.getCanId()) {
                        if (item.getTimeStamp() < timeslot) //check next item
                        {
                            itemtime = item.getTimeStamp();
                            itemdata = item.getCanFrame();
                            itemdatafound = true;
                        }
                        else
                        {
                            break;
                        }
                    }
                }
                break;
            }
        }
        flag_value val;
        value_status status = value_status.OK;
        if(itemdatafound) {
            if (timeslot - itemtime > 1000) //Check if OBSOLETE
                status = value_status.OBSOLETE;
            else
                status = value_status.OK;

            //Send the CAN Frame message to the parser and let it find all signals
            //for each CAN Frame ID that is defined.
            //Return parsed and scaled valued in a list of SignalValues.
            List<SignalValue> parsedValues = _theParser.ParseFrame(new com.volvo.softproduct.canparser.CanFrame(itemdata.channel, itemdata.id, itemdata.dlc, itemdata.data));

            byte flagval = 0;
            for (SignalValue sv : parsedValues) {
                for(signal_map s : _signalMap)
                {
                    if((s.getCanId() == sv.getCanId()) && (s.getStartBit() == sv.getStartBit()) && s.getStorageId() == dataid) {
                        if (sv.getTypeValue() == SignalType.FLAG) {
                            flagval = sv.getByteValue();
                        } else {
                            flagval = 0;
                        }
                    }
                }
            }
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
        CanFrame itemdata = null;
        long itemtime = 0;
        boolean itemdatafound = false;

        //Seatch up the right can message (that fits the dataid and the correct time)
        for (can_id_map sid: _can_id_map)
        {
            if(sid.getStorageId() == dataid) {
                Iterator it4= _queueCAN4.iterator();
                while (it4.hasNext()) {
                    ttd item = (ttd) it4.next();
                    if(item.getCanFrame().id == sid.getCanId()) {
                        if (item.getTimeStamp() <= timeslot) //check next item
                        {
                            itemtime = item.getTimeStamp();
                            itemdata = item.getCanFrame();
                            itemdatafound = true;
                        }
                        else
                        {
                            break;
                        }
                    }
                }
                break;
            }
        }
        float_value val;
        value_status status = value_status.OK;
        if(itemdatafound) {
            if (timeslot - itemtime > 1000) //Check if OBSOLETE
                status = value_status.OBSOLETE;
            else
                status = value_status.OK;

            //Send the CAN Frame message to the parser and let it find all signals
            //for each CAN Frame ID that is defined.
            //Return parsed and scaled valued in a list of SignalValues.
            List<SignalValue> parsedValues = _theParser.ParseFrame(new com.volvo.softproduct.canparser.CanFrame(itemdata.channel, itemdata.id, itemdata.dlc, itemdata.data));

            double floatval = (double)0.0;
            for (SignalValue sv : parsedValues) {
                for(signal_map s : _signalMap)
                {
                    if((s.getCanId() == sv.getCanId()) && (s.getStartBit() == sv.getStartBit()) && s.getStorageId() == dataid) {
                        if (sv.getTypeValue() == SignalType.DOUBLE) {
                            floatval = sv.getDoubleValue();
                        } else {
                            floatval = (double)0.0;
                        }
                    }
                }
            }
            val = new float_value((float) floatval, status, 0);
        }
        else
            val = new float_value((float) 0, value_status.ERR, 0);
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
        CanFrame itemdata = null;
        long itemtime = 0;
        boolean itemdatafound = false;

        //Seatch up the right can message (that fits the dataid and the correct time)
        for (can_id_map sid: _can_id_map)
        {
            if(sid.getStorageId() == dataid) {
                Iterator it4= _queueCAN4.iterator();
                while (it4.hasNext()) {
                    ttd item = (ttd) it4.next();
                    if(item.getCanFrame().id == sid.getCanId()) {
                        if (item.getTimeStamp() < timeslot) //check next item
                        {
                            itemtime = item.getTimeStamp();
                            itemdata = item.getCanFrame();
                            itemdatafound = true;
                        }
                        else
                        {
                            break;
                        }
                    }
                }
                break;
            }
        }
        long_value val;
        value_status status = value_status.OK;
        if(itemdatafound) {
            if (timeslot - itemtime > 1000) //Check if OBSOLETE
                status = value_status.OBSOLETE;
            else
                status = value_status.OK;

            //Send the CAN Frame message to the parser and let it find all signals
            //for each CAN Frame ID that is defined.
            //Return parsed and scaled valued in a list of SignalValues.
            List<SignalValue> parsedValues = _theParser.ParseFrame(new com.volvo.softproduct.canparser.CanFrame(itemdata.channel, itemdata.id, itemdata.dlc, itemdata.data));

            long longval = (long)0;
            for (SignalValue sv : parsedValues) {
                for(signal_map s : _signalMap)
                {
                    if((s.getCanId() == sv.getCanId()) && (s.getStartBit() == sv.getStartBit()) && s.getStorageId() == dataid) {
                        if (sv.getTypeValue() == SignalType.LONG) {
                            longval = sv.getLongValue();
                        } else {
                            longval = (long)0;
                        }
                    }
                }
            }
            val = new long_value((long) longval, status, 0);
        }
        else
            val = new long_value((long) 0, value_status.ERR, 0);
        return val;
    }

    private List<signal_map> wloSignalSettingsFromStorage()
    {
        List<signal_map> signalMap = new LinkedList<signal_map>();

        //ID, startbit, numberofbits, factor, offset, result type, unit, description
        //wlo_data
        signalMap.add(new signal_map(0x00001111,0,2,1,0,false, SignalType.FLAG,"-","Red central warning", wlo_data.Red_central_warning.getCode()));
        signalMap.add(new signal_map(0x00001111,2,2,1,0,false,SignalType.FLAG,"-","Yellow central warning", wlo_data.Yellow_central_warning.getCode()));
        signalMap.add(new signal_map(0x00001111,4,2,1,0,false,SignalType.FLAG,"-","CDC active", wlo_data.CDC_active.getCode()));
        signalMap.add(new signal_map(0x00001111,6,2,1,0,false,SignalType.FLAG,"-","BSS active", wlo_data.BSS_active.getCode()));
        signalMap.add(new signal_map(0x00001111,8,2,1,0,false,SignalType.FLAG,"-","Tool lock active", wlo_data.Tool_lock_active.getCode()));
        signalMap.add(new signal_map(0x00001112,0,2,1,0,false,SignalType.FLAG,"-","Parking break applied", wlo_data.Parking_break_applied.getCode()));
        signalMap.add(new signal_map(0x00001112,2,4,1,-4,false,SignalType.LONG,"F1-4, N, R1-4","Active gear", wlo_data.Active_gear.getCode()));
        signalMap.add(new signal_map(0x00001112,6,16,0.00390625,0,false,SignalType.DOUBLE,"km/h","Vehicle speed", wlo_data.Vehicle_speed.getCode()));
        signalMap.add(new signal_map(0x00001112,22,16,0.125,0,false,SignalType.DOUBLE,"rpm","Engine speed", wlo_data.Engine_speed.getCode()));
        signalMap.add(new signal_map(0x00001114,0,8,0.4,0,false,SignalType.DOUBLE,"%","Fuel level", wlo_data.Fuel_level.getCode()));
        signalMap.add(new signal_map(0x00001114,8,16,0.05,0,false,SignalType.DOUBLE,"l/h","Instant fuel consumption", wlo_data.Instant_fuel_consumption.getCode()));
        signalMap.add(new signal_map(0x00001115,0,16,1,0,false,SignalType.DOUBLE,"kg","Current material weight in bucket", wlo_data.Weight_in_bucket.getCode()));
        signalMap.add(new signal_map(0x00001115,16,8,1,0,false,SignalType.DOUBLE,"%","Current load (% of total capacity)", wlo_data.Current_load.getCode()));
        signalMap.add(new signal_map(0x00001115,24,32,0.05,0,false,SignalType.LONG,"h","Machine hours", wlo_data.Machine_hours.getCode()));
        signalMap.add(new signal_map(0x00001116,0,12,1,0,true,SignalType.DOUBLE,"deg","Steering angle", wlo_data.Steering_angle.getCode()));
        signalMap.add(new signal_map(0x00001116,12,12,1,0,true,SignalType.DOUBLE,"deg","Tilt angle", wlo_data.Tilt_angle.getCode()));
        signalMap.add(new signal_map(0x00001116,24,12,1,0,true,SignalType.DOUBLE,"deg","Boom angle", wlo_data.Boom_angle.getCode()));
        signalMap.add(new signal_map(0x00001116,36,12,0.03125,-273,false,SignalType.DOUBLE,"deg celsius","Outdoor temp", wlo_data.Outdoor_temp.getCode()));

        return signalMap;
    }

    private List<signal_map> excSignalSettingsFromStorage()
    {
        List<signal_map> signalMap = new LinkedList<signal_map>();

        //ID, startbit, numberofbits, factor, offset, result type, unit, description
        //exc_data
        signalMap.add(new signal_map(0x00001111,0,2,1,0,false,SignalType.FLAG,"-","Red central warning", exc_data.Red_central_warning.getCode()));
        signalMap.add(new signal_map(0x00001111,2,2,1,0,false,SignalType.FLAG,"-","Yellow central warning", exc_data.Yellow_central_warning.getCode()));
        signalMap.add(new signal_map(0x00001111,8,2,1,0,false,SignalType.FLAG,"-","Tool lock active", exc_data.Tool_lock_active.getCode()));
        signalMap.add(new signal_map(0x00001112,0,2,1,0,false,SignalType.FLAG,"-","Parking break applied", exc_data.Parking_break_applied.getCode()));
        signalMap.add(new signal_map(0x00001112,6,16,0.00390625,0,false,SignalType.DOUBLE,"km/h","Vehicle speed", exc_data.Vehicle_speed.getCode()));
        signalMap.add(new signal_map(0x00001112,22,16,0.125,0,false,SignalType.DOUBLE,"rpm","Engine speed", exc_data.Engine_speed.getCode()));
        signalMap.add(new signal_map(0x00001114,0,8,0.4,0,false,SignalType.DOUBLE,"%","Fuel level", exc_data.Fuel_level.getCode()));
        signalMap.add(new signal_map(0x00001114,8,16,0.05,0,false,SignalType.DOUBLE,"l/h","Instant fuel consumption", exc_data.Instant_fuel_consumption.getCode()));
        signalMap.add(new signal_map(0x00001115,0,16,1,0,false,SignalType.DOUBLE,"kg","Current material weight in bucket", exc_data.Weight_in_bucket.getCode()));
        signalMap.add(new signal_map(0x00001115,16,8,1,0,false,SignalType.DOUBLE,"%","Current load (% of total capacity)", exc_data.Current_load.getCode()));
        signalMap.add(new signal_map(0x00001115,24,32,0.05,0,false,SignalType.LONG,"h","Machine hours", exc_data.Machine_hours.getCode()));
        signalMap.add(new signal_map(0x00001116,36,12,0.03125,-273,false,SignalType.DOUBLE,"deg celsius","Outdoor temp", exc_data.Outdoor_temp.getCode()));

        return signalMap;
    }

    private List<signal_map> artSignalSettingsFromStorage()
    {
        List<signal_map> signalMap = new LinkedList<signal_map>();

        //ID, startbit, numberofbits, factor, offset, result type, unit, description
        //art_data
        signalMap.add(new signal_map(0x18FECA17,4,2,1,0,false,SignalType.FLAG,"-","Red central warning", art_data.Red_central_warning.getCode()));
        signalMap.add(new signal_map(0x18FECA17,2,2,1,0,false,SignalType.FLAG,"-","Yellow central warning", art_data.Yellow_central_warning.getCode()));
        signalMap.add(new signal_map(0x18FEF117,2,2,1,0,false,SignalType.FLAG,"-","Parking break applied", art_data.Parking_break_applied.getCode()));
        signalMap.add(new signal_map(0x00001113,0,4,1,-3,false,SignalType.LONG,"F1-4, N, R1-4","Active gear", art_data.Active_gear.getCode()));
        signalMap.add(new signal_map(0x18FEF117,8,16,0.00390625,0,false,SignalType.DOUBLE,"km/h","Vehicle speed", art_data.Vehicle_speed.getCode()));
        signalMap.add(new signal_map(0x0CF00417,24,16,0.125,0,false,SignalType.DOUBLE,"rpm","Engine speed", art_data.Engine_speed.getCode()));
        signalMap.add(new signal_map(0x18FEFC17,8,8,0.4,0,false,SignalType.DOUBLE,"%","Fuel level", art_data.Fuel_level.getCode()));
        signalMap.add(new signal_map(0x18FEF217,0,16,0.05,0,false,SignalType.DOUBLE,"l/h","Instant fuel consumption", art_data.Instant_fuel_consumption.getCode()));
        signalMap.add(new signal_map(0x00001115,0,16,1,0,false,SignalType.DOUBLE,"kg","Current material weight in bucket", art_data.Weight_in_bucket.getCode()));
        signalMap.add(new signal_map(0x00001115,16,8,1,0,false,SignalType.DOUBLE,"%","Current load (% of total capacity)", art_data.Current_load.getCode()));
        signalMap.add(new signal_map(0x18FEE717,0,32,0.05,0,false,SignalType.LONG,"h","Machine hours", art_data.Machine_hours.getCode()));
        signalMap.add(new signal_map(0x00001116,0,12,1,0,true,SignalType.DOUBLE,"deg","Steering angle", art_data.Steering_angle.getCode()));
        signalMap.add(new signal_map(0x00001115,56,2,1,0,false,SignalType.FLAG,"-","Unloaded", art_data.Unloaded.getCode()));
        signalMap.add(new signal_map(0x00001115,58,2,1,0,false,SignalType.FLAG,"-","Dump body up", art_data.Dump_body_up.getCode()));
        signalMap.add(new signal_map(0x18FEF517,24,16,0.03125,-273,false,SignalType.DOUBLE,"deg celsius","Outdoor temp", art_data.Outdoor_temp.getCode()));

        return signalMap;
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


    public boolean isTimeScopeCAN4Passed()
    {
        long biggestTime = 0;
        long currentTime = System.currentTimeMillis();
        long timeslot = currentTime - _startTime;
        Iterator it4= _queueCAN4.iterator();
        while (it4.hasNext()) {
            ttd item = (ttd) it4.next();
            if (item.getTimeStamp() > biggestTime)
            {
                biggestTime = item.getTimeStamp();
            }
        }
        if(biggestTime < timeslot)
            return true;
        else
            return false;
    }

    public boolean isTimeScopeCAN3Passed()
    {
        long biggestTime = 0;
        long currentTime = System.currentTimeMillis();
        long timeslot = currentTime - _startTime;
        Iterator it3= _queueCAN3.iterator();
        while (it3.hasNext()) {
            ttd item = (ttd) it3.next();
            if (item.getTimeStamp() > biggestTime)
            {
                biggestTime = item.getTimeStamp();
            }
        }
        if(biggestTime < timeslot)
            return true;
        else
            return false;
    }
}


