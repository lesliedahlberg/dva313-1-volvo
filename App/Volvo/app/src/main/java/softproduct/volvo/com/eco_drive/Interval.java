package softproduct.volvo.com.eco_drive;

/**
 * Created by lesliedahlberg on 2016-12-02.
 */
public class Interval {
    public float start;
    public float end;
    public Interval(float start, float end){
        this.start = start;
        this.end = end;
    }
    public float length(){
        return end - start;
    }
    public float normalizeToInterval(float number){
        float a = number - start;
        float b = length();
        return a/b;
    }

}
