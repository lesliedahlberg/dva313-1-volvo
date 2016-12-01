package softproduct.volvo.com.eco_drive;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Handler;

import java.util.ArrayList;

/*SIM
import com.volvo.softproduct.sensorextensionlibrary.db_enum.art_data;
import com.volvo.softproduct.sensorextensionlibrary.db_enum.exc_data;
import com.volvo.softproduct.sensorextensionlibrary.db_enum.gnss_data;
import com.volvo.softproduct.sensorextensionlibrary.db_enum.wlo_data;
import com.volvo.softproduct.sensorextensionlibrary.managers.*;
import com.volvo.softproduct.sensorextensionlibrary.db_value.*;
*/

/**
 * Created by lesliedahlberg on 2016-11-30.
 */
public class CanBusInformation {

    Context context;

    //SIM// private Handler handlerMachineData;
    //SIM// private machine_manager _mdm;

    float rpm = 0;
    float distance = 0;
    float fuel = 0;
    float acceleration = 0;
    float load = 0;
    float startTime;
    float endTime;
    float lastCheck;
    int lastCheckIndex;
    ArrayList<Float> speeds;



    public CanBusInformation(Context context){
        this.context = context;

        speeds = new ArrayList<Float>();
        speeds.add(0.0f);

        startTime = System.currentTimeMillis();

        //SIM// handlerMachineData = new Handler();
        //SIM// _mdm = new machine_manager(this);

        /*SIM
        if(_mdm.Connect() == true) {
            handlerMachineData.post(runnableMachineData);
        }
        */
    }

    public void end(){
        //SIM//_mdm.Disconnect();
    }


    public float getRPM(){
        return (float) (Math.random() * 3000) + 500;
        //SIM//return rpm;
    }
    public float getDistance(){
        return (float) (Math.random() * 500) + 5;
        //SIM//return calculateDistance();
    }
    public float getFuelConsumption(){
        return (float) (Math.random() * 10) + 0.1f;
        //SIM//return fuel;
    }
    public float getAcceleration(){
        return (float) (Math.random() * 5) + 0;
        //SIM//return calculateAcceleration();
    }
    public float getLoad(){
        return (float) (Math.random() * 1000) + 10;
        //SIM//return load;
    }

    /*SIM
    private Runnable runnableMachineData = new Runnable() {

        @Override
        public void run()
        {
            handlerMachineData.postDelayed(runnableMachineData, 500);

            rpm = _mdm.getFloatSignal(exc_data.Engine_speed.getCode());
            load = _mdm.getFloatSignal(exc_data.Weight_in_bucket.getCode());
            fuel = _mdm.getFloatSignal(exc_data.Instant_fuel_consumption.getCode());
            speeds.add(_mdm.getFloatSignal(exc_data.Vehicle_speed.getCode()));

            updateTime();
        }
    };*/


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
        float interval = endTime - lastCheck;
        float acc = speeds.get(speeds.size()-1) - speeds.get(speeds.size()-2);
        return acc / (interval/1000.0f/60.0f/60.0f);
    }


}
