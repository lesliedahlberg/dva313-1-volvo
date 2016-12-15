package softproduct.volvo.com.eco_drive;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Handler;

import java.util.ArrayList;


import com.volvo.softproduct.sensorextensionlibrary.db_enum.art_data;
import com.volvo.softproduct.sensorextensionlibrary.db_enum.exc_data;
import com.volvo.softproduct.sensorextensionlibrary.db_enum.gnss_data;
import com.volvo.softproduct.sensorextensionlibrary.db_enum.wlo_data;
import com.volvo.softproduct.sensorextensionlibrary.managers.*;
import com.volvo.softproduct.sensorextensionlibrary.db_value.*;


/**
 * Created by lesliedahlberg on 2016-11-30.
 */

public class CanBusInformation {

    Context context;

    private Handler handlerMachineData;
    private machine_manager _mdm;

    float rpm = 0; //RPM
    float distance = 0;
    float fuel = 0; //l/h
    float acceleration = 0;
    float load = 0; //% of capacity
    long startTime;
    long endTime;
    long lastCheck;
    int lastCheckIndex;
    ArrayList<Float> speeds; //km/h

    public CanBusInformation(Context context){
        this.context = context;
        speeds = new ArrayList<Float>();
        speeds.add(0.0f);
        startTime = System.currentTimeMillis();
         handlerMachineData = new Handler();
        _mdm = new machine_manager(context);
        if(_mdm.Connect() == true) {
            handlerMachineData.post(runnableMachineData);
        }

    }

    public void end(){
        _mdm.Disconnect();
    }


    public float getRPM(){
        //float v = (float) (Math.random() * 3000) + 500;
        //return v;
        return rpm;
    }
    public float getDistance(){
        //float v = (float) (Math.random() * 10) + 0;
        //return v;
        return calculateDistance();
    }
    public float getFuelConsumption(){
        //float v = (float) (Math.random() * 10) + 0.1f;
        //return v;
        return fuel;
    }
    public float getAcceleration(){
        //float v = (float) (Math.random() * 1.0f) + 0.001f;
        //return v;
        return calculateAcceleration();
    }
    public float getLoad(){
        //float v = (float) (Math.random() * 1000) + 0;
        //return v;
        return load;
    }


    private Runnable runnableMachineData = new Runnable() {

        @Override
        public void run()
        {
            handlerMachineData.postDelayed(runnableMachineData, 500);

            rpm = _mdm.getFloatSignal(exc_data.Engine_speed.getCode()).getValue();
            load = _mdm.getFloatSignal(exc_data.Weight_in_bucket.getCode()).getValue();
            fuel = _mdm.getFloatSignal(exc_data.Instant_fuel_consumption.getCode()).getValue();
            speeds.add(_mdm.getFloatSignal(exc_data.Vehicle_speed.getCode()).getValue());

            updateTime();
        }
    };


    private void updateTime(){
        lastCheck = endTime;
        endTime = System.currentTimeMillis();
    }

    private float calculateDistance(){
        float avgSpeed = 0;
        if(speeds.size()>0){
            for (float s:speeds) {
                avgSpeed += s;
            }
            avgSpeed /= speeds.size();
        }
        return avgSpeed * ((endTime - startTime)/1000.0f/60.0f/60.0f);
    }

    private float calculateAcceleration(){
        long interval = endTime - lastCheck;
        float acc = speeds.get(speeds.size()-1) - speeds.get(speeds.size()-2);
        return (acc / (interval/1000.0f))*0.2778f;
    }


}
