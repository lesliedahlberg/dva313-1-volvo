package softproduct.volvo.com.eco_drive;

import java.util.ArrayList;

/**
 * Created by lesliedahlberg on 2016-11-30.
 */
public class RecordedData {
    CanBusInformation src;
    int x = 0;
    ArrayList<Float> fuelConsumption;
    ArrayList<Float> acceleration;
    ArrayList<Float> distance;
    ArrayList<Float> rpm;
    ArrayList<Float> load;

    public float u = 0.8f;
    public float v = 0.5f;
    public float k = 50;
    public float l = 50;
    public float m = 0.3f;

    public RecordedData(){
        fuelConsumption = new ArrayList<Float>();
        acceleration = new ArrayList<Float>();
        distance = new ArrayList<Float>();
        rpm = new ArrayList<Float>();
        load = new ArrayList<Float>();
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

    };
    public float[] getLiveValues(){
        return new float[]{fuelConsumption.get(fuelConsumption.size()-1), acceleration.get(acceleration.size()-1), distance.get(distance.size()-1), rpm.get(rpm.size()-1), load.get(load.size()-1)};
    };
    public float[] getNormalizedLiveValues(){
        return new float[]{k*fuelConsumption.get(fuelConsumption.size()-1), l*acceleration.get(acceleration.size()-1), u*distance.get(distance.size()-1), m*rpm.get(rpm.size()-1), v*load.get(load.size()-1)};
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
    public int getCurrentScore(){

        //Averaging should be done here
        float[] liveValues = getLiveValues();
        return (int) (1000*(liveValues[2] * u * liveValues[4] * v)/(liveValues[0] * k *liveValues[3] * l *liveValues[1] * m));
    }
    public int getX(){
        return x;
    }
}
