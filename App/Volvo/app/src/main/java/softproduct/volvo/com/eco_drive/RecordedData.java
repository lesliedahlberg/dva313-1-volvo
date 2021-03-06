package softproduct.volvo.com.eco_drive;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by lesliedahlberg on 2016-11-30.
 */
public class RecordedData {
    CanBusInformation src;
    Context context;
    int x = 0;
    ArrayList<Float> fuelConsumption; // l/h
    ArrayList<Float> acceleration; // m/s/s
    ArrayList<Float> distance; //km
    ArrayList<Float> rpm; //rpm
    ArrayList<Float> load; //% of capacity
    ArrayList<Integer> scores;

    Interval interval_rpm = new Interval(0, 5000);
    Interval interval_distance = new Interval(0, 50);
    Interval interval_fuel = new Interval(0, 25);
    Interval interval_acceleration = new Interval(0, 3);
    Interval interval_load = new Interval(0, 100);

    public float coef_rpm = 1.0f;
    public float coef_distance = 2.0f;
    public float coef_fuel = 1.0f;
    public float coef_accleration = 1.0f;
    public float coef_load = 2.0f;

    public RecordedData(Context context){
        fuelConsumption = new ArrayList<Float>();
        acceleration = new ArrayList<Float>();
        distance = new ArrayList<Float>();
        rpm = new ArrayList<Float>();
        load = new ArrayList<Float>();
        scores = new ArrayList<Integer>();
        this.context = context;
    }

    public void setDataSource(CanBusInformation src){
        this.src = src;
    }
    public void updateDataFromSource(){
        x++;
        fuelConsumption.add(src.getFuelConsumption());
        acceleration.add(src.getAcceleration());
        distance.add(src.getDistance());
        rpm.add(src.getRPM());
        load.add(src.getLoad());
        scores.add(getCurrentScore());

    };

    public float[] getLiveValues(){
        return new float[]{fuelConsumption.get(fuelConsumption.size()-1), acceleration.get(acceleration.size()-1), distance.get(distance.size()-1), rpm.get(rpm.size()-1), load.get(load.size()-1)};
    };
    public float[] getNormalizedLiveValues(){
        return new float[]{
                interval_fuel.normalizeToInterval(fuelConsumption.get(fuelConsumption.size()-1)),
                interval_acceleration.normalizeToInterval(acceleration.get(acceleration.size()-1)),
                interval_distance.normalizeToInterval(distance.get(distance.size()-1)),
                interval_rpm.normalizeToInterval(rpm.get(rpm.size()-1)),
                interval_load.normalizeToInterval(load.get(load.size()-1))
        };


                //k*fuelConsumption.get(fuelConsumption.size()-1), l*acceleration.get(acceleration.size()-1), u*distance.get(distance.size()-1), m*rpm.get(rpm.size()-1), v*load.get(load.size()-1)};
    };
    public ArrayList<Float> getHistoricalDataValues(int type){
        ArrayList<Float> r = null;
        switch (type){
            case 0:
                r = fuelConsumption;
            break;
            case 1:
                r =  acceleration;
            break;
            case 2:
                r =  distance;
            break;
            case 3:
                r =  rpm;
            break;
            case 4:
                r =  load;
            break;
        }
        return r;
    };

    public int getOverallScore(){
        float avg = 0.0f;
        for (int s:scores) {
            avg += (float) s;
        }
        avg /= scores.size();
        return (int) avg;
    }

    public int getCurrentScore(){

        //Averaging should be done here
        float[] liveValues = getNormalizedLiveValues();
        return (int) (
                (liveValues[2] * coef_distance * liveValues[4] * coef_load
                )/(
                        liveValues[0] * coef_fuel  *liveValues[3] * coef_rpm *liveValues[1] * coef_accleration
                ));
    }
    public int getX(){
        return x;
    }
}
