package softproduct.volvo.com.eco_drive;

/**
 * Created by lesliedahlberg on 2016-11-30.
 */
public class RecordedData {
    CanBusInformation src;
    public void setDataSource(CanBusInformation src){
        this.src = src;
    }
    public void updateDataFromSource(){

    };
    public float[] getLiveValues(){
        return new float[]{0.0f};
    };
    public float[] getHistoricalDataValues(int type){
        return new float[]{0.0f};
    };
    public int getCurrentScore(){
        return 0;
    }
}
