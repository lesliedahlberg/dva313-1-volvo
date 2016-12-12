//Algorithm that describes the score calculation for the Volvo Eco-Drive application
public class EcoDriveAlgorithm {

    //Instataneous data values
    public ArrayList<Float> fuelConsumption;
    public ArrayList<Float> acceleration;
    public ArrayList<Float> distance;
    public ArrayList<Float> rpm;
    public ArrayList<Float> load;

    //Instataneous scores
    public ArrayList<Integer> scores;

    //Score calculation coefficients for data
    public float coef_rpm = 1.0f;
    public float coef_distance = 2.0f;
    public float coef_fuel = 1.0f;
    public float coef_accleration = 1.0f;
    public float coef_load = 2.0f;

    //Intervals with common boundary values for data used for normalization to the interval [0,1]
    Interval interval_rpm = new Interval(500, 3000);
    Interval interval_distance = new Interval(0, 10);
    Interval interval_fuel = new Interval(0.1f, 10);
    Interval interval_acceleration = new Interval(0.001f, 1.0f);
    Interval interval_load = new Interval(0, 1000);

    //Data source
    CanBusInformation src;

    public EcoDriveAlgorithm(CanBusInformation src){
        fuelConsumption = new ArrayList<Float>();
        acceleration = new ArrayList<Float>();
        distance = new ArrayList<Float>();
        rpm = new ArrayList<Float>();
        load = new ArrayList<Float>();
        scores = new ArrayList<Integer>();
        this.src = src;
    }

    public void updateDataFromSource(){
        fuelConsumption.add(src.getFuelConsumption());
        acceleration.add(src.getAcceleration());
        distance.add(src.getDistance());
        rpm.add(src.getRPM());
        load.add(src.getLoad());
        scores.add(getCurrentScore());
        x++;
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
        float[] liveValues = getNormalizedLiveValues();
        return (int) ((liveValues[2] * coef_distance * liveValues[4] * coef_load)/(liveValues[0] * coef_fuel  *liveValues[3] * coef_rpm *liveValues[1] * coef_accleration));
    }

}
