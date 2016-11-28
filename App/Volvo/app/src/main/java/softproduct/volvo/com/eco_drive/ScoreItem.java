package softproduct.volvo.com.eco_drive;

/**
 * Created by lesliedahlberg on 2016-11-21.
 * This class is used to handle data from the score_table table
 */

public class ScoreItem {
    public int id;
    public String alias;
    public int score;
    public String date;
    public double rpm;
    public double acceleration;
    public double distance;
    public double load;
    public double consumption;
    public double altitude;

    public ScoreItem(int id, String alias, int score, String date, double rpm, double acceleration, double distance, double load, double consumption, double altitude) {
        this.id = id;
        this.alias = alias;
        this.score = score;
        this.date = date;
        this.rpm = rpm;
        this.acceleration = acceleration;
        this.distance = distance;
        this.load = load;
        this.consumption = consumption;
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return "[id:" + String.valueOf(id) + "; alias:" + String.valueOf(alias)
                + "; alias:" + alias
                + "; score:" + String.valueOf(score)
                + "; date:" + alias
                + "; rpm:" + String.valueOf(rpm)
                + "; acceleration:" + String.valueOf(acceleration)
                + "; distance:" + String.valueOf(distance)
                + "; load:" + String.valueOf(load) + ";]";
    }

}
