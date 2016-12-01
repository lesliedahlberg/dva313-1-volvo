package softproduct.volvo.com.eco_drive;

/**
 * Created by lesliedahlberg on 2016-11-30.
 */
public class CanBusInformation {
    public float getRPM(){
        return (float) (Math.random() * 3000) + 500;
    }
    public float getDistance(){
        return (float) (Math.random() * 500) + 5;
    }
    public float getFuelConsumption(){
        return (float) (Math.random() * 10) + 0.1f;
    }
    public float getAcceleration(){
        return (float) (Math.random() * 5) + 0;
    }
    public float getLoad(){
        return (float) (Math.random() * 1000) + 10;
    }
}
