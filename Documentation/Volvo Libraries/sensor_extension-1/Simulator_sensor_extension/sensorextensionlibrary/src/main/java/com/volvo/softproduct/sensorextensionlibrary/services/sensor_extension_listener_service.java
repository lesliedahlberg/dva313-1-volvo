package com.volvo.softproduct.sensorextensionlibrary.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.os.Handler;

import com.volvo.softproduct.canparser.*;

import com.volvo.softproduct.sensorextensionlibrary.db_enum.*;
import com.volvo.softproduct.sensorextensionlibrary.helpers.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import se.cpacsystems.common.Position;
import se.cpacsystems.common.Sqi;
import se.cpacsystems.position.PositionManager;
import se.cpacsystems.rawcan.RawCan;
import se.cpacsystems.rawcan.CanFrame;

public class sensor_extension_listener_service extends Service {

    public static final String BROADCAST_ACTION = "com.volvo.softproduct.sensorextensionlibrary.services.serviceevent";

    public static Service                       _myService;
    private RawCan                              _rawCan3Manager;
    private RawCan                              _rawCan4Manager;
    private CanParser                           _theParser;
    private List<signal_map>                    _wloSignalMap;
    private List<signal_map>                    _excSignalMap;
    private List<signal_map>                    _artSignalMap;
    private List<signal_map>                    _signalMap;
    private static int                          _instCounter = 0;

    private HashMap<Integer, Long>              _allowedCanMap;
    public int                                  actionRequested = -1;
    public long                                 startTimestamp;
    private Queue<ttd>                          _queueCAN3 = new LinkedList<ttd>();
    private Queue<ttd>                          _queueCAN4 = new LinkedList<ttd>();
    private Queue<ttd_gnss>                     _queueGNSS = new LinkedList<ttd_gnss>();
    private final Object                        _mutex = new Object();

    private PositionManager                     _positionManager;
    private boolean                             _positionConnected = false;
    private Handler                             _gnssHandler;
    private SharedPreferences                   _simulatorValuePrefs;
    private SharedPreferences.Editor            _prefsEditor;
    private long                                _messageTimestamp;


    public sensor_extension_listener_service() {
    }

    private Runnable gnssRunnable = new Runnable() {

        @Override
        public void run()
        {
            _gnssHandler.postDelayed(gnssRunnable, 500);

            if(_positionConnected)
            {
                long timestamp = System.currentTimeMillis();

                Position newPos = _positionManager.getPosition();

                SaveLPreferencese(gnss_data.gnssTime.getCode(),newPos.time,timestamp);
                SaveFPreferencese(gnss_data.gnssQuality.getCode(),newPos.gnssQuality,timestamp);
                SaveDPreferencese(gnss_data.ageOfDiff.getCode(),newPos.ageOfDiff,timestamp);
                SaveFPreferencese(gnss_data.longLatSqi.getCode(),newPos.longLatSqi,timestamp);
                SaveDPreferencese(gnss_data.latitude.getCode(),newPos.latitude,timestamp);
                SaveDPreferencese(gnss_data.longitude.getCode(),newPos.longitude,timestamp);
                SaveDPreferencese(gnss_data.altitude.getCode(),newPos.altitude,timestamp);
                SaveFPreferencese(gnss_data.headingSqi.getCode(),newPos.headingSqi,timestamp);
                SaveDPreferencese(gnss_data.heading.getCode(),newPos.heading,timestamp);
                SaveFPreferencese(gnss_data.cogSogSqi.getCode(),newPos.cogSogSqi,timestamp);
                SaveDPreferencese(gnss_data.courseOverGround.getCode(),newPos.courseOverGround,timestamp);
                SaveDPreferencese(gnss_data.speedOverGround.getCode(),newPos.speedOverGround,timestamp);

                if(actionRequested == 1)
                {
                    synchronized (_mutex) {
                        _queueGNSS.add(new ttd_gnss(newPos.time,(byte)newPos.gnssQuality,newPos.ageOfDiff,(byte)newPos.longLatSqi,
                                newPos.latitude,newPos.longitude, newPos.altitude,(byte)newPos.headingSqi,newPos.heading,
                                (byte)newPos.cogSogSqi, newPos.courseOverGround, newPos.speedOverGround,timestamp - startTimestamp));
                    }
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        _myService = this;
        _rawCan3Manager = new RawCan();
        _rawCan4Manager = new RawCan();
        _theParser = new CanParser();
        _positionManager = new PositionManager(this);

        //  _canIdInfo = new ArrayList<can_read_limit>();
        _signalMap = new LinkedList<signal_map>();
        _allowedCanMap = new HashMap<Integer, Long>();

        _messageTimestamp = 0;

        _simulatorValuePrefs = this.getSharedPreferences("simulatorvalue", MODE_WORLD_READABLE);
        _prefsEditor = _simulatorValuePrefs.edit();

        registerReceiver(broadcastReceiver, new IntentFilter(BROADCAST_ACTION));

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
                _allowedCanMap.put(s.getCanId(), (long)0);
                _signalMap.add(s);
            }
        }
        for (signal_map s : _excSignalMap) {
            if(_signalMap.contains(s)) {
                continue;
            }
            else {
                _allowedCanMap.put(s.getCanId(), (long)0);
                _signalMap.add(s);
            }
        }
        for (signal_map s : _artSignalMap) {
            if(_signalMap.contains(s)) {
                continue;
            }
            else {
                _allowedCanMap.put(s.getCanId(), (long)0);
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

        int filterId[] = {0,0};

        //Init CAN3, no filter!
        if (_rawCan3Manager.init(RawCan.CAN3, filterId) == true) {

            _rawCan3Manager.registerCanMessageListener(new RawCan.CanMessageListener() {
                public void newCanMessageReceived(CanFrame newframe) {
                    if (newframe.id != 0)
                    {
                        long timestamp = System.currentTimeMillis();

                        if(actionRequested == 1)
                        {
                            synchronized (_mutex) {
                                _queueCAN3.add(new ttd(newframe,timestamp - startTimestamp));
                            }
                        }

                    }
                }
            });
        } else {
            Log.d("canlistener_service", "Initiation of CAN3 Error");
        }

        //Init CAN4, no filter!
        if (_rawCan4Manager.init(RawCan.CAN4, filterId) == true) {

            _rawCan4Manager.registerCanMessageListener(new RawCan.CanMessageListener() {
                public void newCanMessageReceived(CanFrame newframe) {
                    if (newframe.id != 0)
                    {
                        if(_allowedCanMap.containsKey(newframe.id)) {
                            long messageTimestamp= _allowedCanMap.get(newframe.id);
                            long timestamp = System.currentTimeMillis();
                            if(timestamp - messageTimestamp > 100) {
                                _allowedCanMap.put(newframe.id, timestamp);

                                if (actionRequested == 1) {
                                    synchronized (_mutex) {
                                        _queueCAN4.add(new ttd(newframe, timestamp - startTimestamp));
                                    }
                                }

                                //Send the CAN Frame message to the parser and let it find all signals
                                //for each CAN Frame ID that is defined.
                                //Return parsed and scaled valued in a list of SignalValues.
                                List<SignalValue> parsedValues = _theParser.ParseFrame(new com.volvo.softproduct.canparser.CanFrame(newframe.channel, newframe.id, newframe.dlc, newframe.data));

                                SaveMessagePreferencese(parsedValues, timestamp);
                            }
                        }
                    }
                }
            });
        } else {
            Log.d("canlistener_service", "Initiation of CAN4 Error");
        }

        if(_positionConnected == false)
        {
            _positionManager.connect();
            _positionConnected = true;
        }

        _gnssHandler = new Handler();
        _gnssHandler.post(gnssRunnable);
    }

    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        _instCounter ++;
        Log.d("canlistener_service", String.format("Number of taken instances %d",_instCounter));
        return START_STICKY;
    }

    private List<signal_map> wloSignalSettingsFromStorage()
    {
        List<signal_map> signalMap = new LinkedList<signal_map>();

        //ID, startbit, numberofbits, factor, offset, result type, unit, description
        //wlo_data
        signalMap.add(new signal_map(0x00001111,0,2,1,0,false,SignalType.FLAG,"-","Red central warning", wlo_data.Red_central_warning.getCode()));
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


    @Override
    public void onDestroy() {
        super.onDestroy();
      //  Toast.makeText(getBaseContext(),"onDestroy", Toast.LENGTH_LONG).show();
        try
        {
            unregisterReceiver(broadcastReceiver);
        }
        catch(Exception e)
        {
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            MessageReceiver(intent);
        }
    };

    private void MessageReceiver(Intent intent)
    {
        String message = intent.getStringExtra("message");
        Log.d("canlistener_service", message);

        if(message.equals("LISTENER_STOP"))
        {
            _instCounter--;
            Log.d("canlistener_service", String.format("Number of taken instances %d",_instCounter));
            if(_instCounter <= 0)
            {
                Log.d("canlistener_service", "Reference counter is zero, stop the service!");
                stopSelf();
            }
        }
        else if(message.equals("ACTION_START_REC"))
        {
            _queueCAN3.clear();;
            _queueCAN4.clear();;
            actionRequested = 1;
            startTimestamp = System.currentTimeMillis();
        }
        else if(message.equals("ACTION_STOP_REQ"))
        {
            actionRequested = 2;

        }
        else if(message.equals("ACTION_SAVE_REC"))
        {
            actionRequested = 3;
            try {
                synchronized (_mutex)
                {
                    boolean filesSaved = false;
                    usbhandler usbobject = new usbhandler();
                    String usbPath = usbobject.getActivePath();
                    if(usbPath.length() > 0) {
                     //   File sdcard = Environment.getExternalStorageDirectory();

                        Log.d("canlistener_service", "USB storage = " + usbPath);
                        File file3 = new File(usbPath,"can3data.csv");
                        BufferedWriter bufferedWriter3 = new BufferedWriter(new FileWriter(file3));
                        Iterator it3= _queueCAN3.iterator();
                        while (it3.hasNext())
                        {
                            ttd item = (ttd)it3.next();
                            String data = item.getTimeStamp() + "," + item.getCanFrame().channel + "," + item.getCanFrame().id + "," + item.getCanFrame().dlc + ",";
                            for(int i =0;i<item.getCanFrame().dlc;i++ ) {
                                if(i> 0)
                                    data += "_";
                                long bt = 0x000000ffL & item.getCanFrame().data[i];

                                data += bt;
                            }
                            data += ";";
                            bufferedWriter3.write(data);
                            bufferedWriter3.newLine();
                        }
                        _queueCAN3.clear();
                        bufferedWriter3.close();

                        File file4 = new File(usbPath,"can4data.csv");
                        BufferedWriter bufferedWriter4 = new BufferedWriter(new FileWriter(file4));
                        Iterator it4= _queueCAN4.iterator();
                        while (it4.hasNext())
                        {
                            ttd item = (ttd)it4.next();
                            String data = item.getTimeStamp() + "," + item.getCanFrame().channel + "," + item.getCanFrame().id + "," + item.getCanFrame().dlc + ",";
                            for(int i =0;i<item.getCanFrame().dlc;i++ ) {
                                if(i > 0)
                                    data += "_";

                                long bt = 0x000000ffL & item.getCanFrame().data[i];

                                data += bt;
                            }
                            data += ";";
                            bufferedWriter4.write(data);
                            bufferedWriter4.newLine();
                        }
                        _queueCAN4.clear();
                        bufferedWriter4.close();

                        File fileGnss = new File(usbPath,"gnssdata.csv");
                        BufferedWriter bufferedWriterGnss = new BufferedWriter(new FileWriter(fileGnss));
                        Iterator itGnss= _queueGNSS.iterator();
                        while (itGnss.hasNext())
                        {
                            ttd_gnss item = (ttd_gnss)itGnss.next();
                            String data = item.Timestamp + "," + item.Latitude + "," + item.Longitude + "," + item.Altitude + ",";
                            data += item.LongLatSqi + "," + item.Heading + "," + item.HeadingSqi + "," + item.SpeedOverGround + ",";
                            data += item.CourseOverGround + "," + item.CogSogSqi + "," + item.AgeOfDiff + "," + item.GnssQuality + ",";
                            data += item.GnssTime + ";";

                            bufferedWriterGnss.write(data);
                            bufferedWriterGnss.newLine();
                        }
                        _queueGNSS.clear();
                        bufferedWriterGnss.close();

                        if(file4.exists() && file3.exists() && fileGnss.exists())
                            filesSaved = true;
                    }

                    Intent	sendIntent =  new Intent(sensor_extension_listener_service.BROADCAST_ACTION);

                    if(usbPath.length() > 0 && filesSaved == true) {
                        Log.d("canlistener_service", "SAVE_DONE");
                        sendIntent.putExtra("message", "SAVE_DONE");
                    }
                    else {
                        Log.d("canlistener_service", "SAVE_ERR");
                        sendIntent.putExtra("message", "SAVE_ERR");
                    }
                    this.sendBroadcast(sendIntent);
                    actionRequested = -1;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final String DATA_STATE = "data_";
    private final String TIME_STATE = "time_";
    private void SaveMessagePreferencese(List<SignalValue> parsedValues, long time) {
      //  SharedPreferences.Editor prefsEditor = _simulatorValuePrefs.edit();
        for (SignalValue sv : parsedValues) {
            int storageId = 0;
            for(signal_map s : _signalMap)
            {
                if((s.getCanId() == sv.getCanId()) && (s.getStartBit() == sv.getStartBit())) {
                    storageId = s.getStorageId();

                    if (sv.getTypeValue() == SignalType.DOUBLE) {
                        _prefsEditor.putString(DATA_STATE + storageId, Double.toString(sv.getDoubleValue()));
                    } else if (sv.getTypeValue() == SignalType.FLAG) {
                        _prefsEditor.putString(DATA_STATE + storageId, Long.toString(sv.getByteValue()));
                    } else if (sv.getTypeValue() == SignalType.LONG) {
                        _prefsEditor.putString(DATA_STATE + storageId, Long.toString(sv.getLongValue()));
                    }
                    _prefsEditor.putString(TIME_STATE + storageId, Long.toString(time));
                }
            }
        }
        _prefsEditor.apply();//.commit();
    }

    private void SaveLPreferencese(int dataid, long value, long time)
    {
        //SharedPreferences.Editor prefsEditor = _simulatorValuePrefs.edit();
        _prefsEditor.putString(DATA_STATE+dataid, Long.toString(value));
        _prefsEditor.putString(TIME_STATE+dataid, Long.toString(time));
        _prefsEditor.apply();
    }

    private void SaveFPreferencese(int dataid, long value, long time)
    {
        //SharedPreferences.Editor prefsEditor = _simulatorValuePrefs.edit();
        _prefsEditor.putString(DATA_STATE+dataid, Long.toString(value));
        _prefsEditor.putString(TIME_STATE+dataid, Long.toString(time));
        _prefsEditor.apply();
    }

    private void SaveDPreferencese(int dataid, double value, long time)
    {
        //SharedPreferences.Editor prefsEditor = _simulatorValuePrefs.edit();
        _prefsEditor.putString(DATA_STATE+dataid, Double.toString(value));
        _prefsEditor.putString(TIME_STATE+dataid, Long.toString(time));
        _prefsEditor.apply();
    }


}
