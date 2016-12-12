package softproduct.volvo.com.eco_drive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.DashPathEffect;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;


<<<<<<< HEAD
import org.json.JSONArray;
=======
>>>>>>> origin/master
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
<<<<<<< HEAD

=======
>>>>>>> origin/master
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Porya on 2016-11-25.
 */

public class GameActivity extends Activity {

    Context context;

    boolean uploaded = false;

    //Notification
    Gamification gamification;

    //CAN
    CanBusInformation can;
    RecordedData canData;

    //Charts
    private LineChart detailedLineChart;
    private RadarChart generalRadialChart;

    //Data arrays for charts
    ArrayList<Entry> fuelConsumptionData;
    ArrayList<Entry> rpmData;
    ArrayList<Entry> altitudeData;
    ArrayList<Entry> accelerationData;
    ArrayList<Entry> loadData;
    ArrayList<Entry> distanceData;
    ArrayList<Entry> currentData;
    ArrayList<RadarEntry> radarData;

    //UI
    ProgressBar progressBar;
    TextView score;
    TextView avgScore;

    //Color
    int currentGraphColor;
    int radialGraphColor;
    int radialAverageGraphColor;

    //Timer
    CountDownTimer timer;
<<<<<<< HEAD
    int minutes;
=======
    public int minutes;
>>>>>>> origin/master

    //Alias and machine
    public String alias;
    public String machine;

    //Live data
    boolean live = true;
    int xPos = 0;
    int updateInterval = 1000;
    int finalScore = 0;


<<<<<<< HEAD
=======

>>>>>>> origin/master
    //Radar graph
    private void radarGraph() {

        generalRadialChart = (RadarChart) findViewById(R.id.GameActivity_radarChart);
        //generalRadialChart.setBackgroundColor(Color.rgb(60, 65, 82)); //change later to something else

        generalRadialChart.getDescription().setEnabled(false);

        generalRadialChart.setWebLineWidth(1f);
        generalRadialChart.setWebColor(Color.LTGRAY);
        generalRadialChart.setWebLineWidthInner(1f);
        generalRadialChart.setWebColorInner(Color.LTGRAY);
        generalRadialChart.setWebAlpha(500);


        setRadarData();

        detailedLineChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = generalRadialChart.getXAxis();
        //xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(24f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"Fuel", "Acceleration", "Distance", "RPM", "Load"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = generalRadialChart.getYAxis();
        //yAxis.setTypeface(mTfLight);
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(12f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(1f);
        yAxis.setDrawLabels(false);
        Legend l = generalRadialChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        //l.setTypeface(mTfLight);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.WHITE);
        l.setEnabled(false);

    }

    public void setRadarData() {
        if (radarData != null) {
            if (radarData.size() > 0) {
                /*RadarDataSet set1 = new RadarDataSet(entries1, "Average");
                set1.setColor(Color.rgb(103, 110, 129));
                set1.setFillColor(radialGraphColor);
                set1.setDrawFilled(true);
                set1.setFillAlpha(200);
                set1.setLineWidth(4f);
                set1.setDrawHighlightCircleEnabled(true);
                set1.setDrawHighlightIndicators(false);*/

                RadarDataSet set2 = new RadarDataSet(radarData, "You");
                set2.setColor(Color.rgb(121, 162, 175));
                set2.setFillColor(radialAverageGraphColor);
                set2.setDrawFilled(true);
                set2.setFillAlpha(100);
                set2.setLineWidth(4f);
                set2.setDrawHighlightCircleEnabled(true);
                set2.setDrawHighlightIndicators(false);

                ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();

                sets.add(set2);
                //sets.add(set1);

                RadarData data = new RadarData(sets);
                //data.setValueTypeface(mTfLight);
                data.setValueTextSize(18f);
                data.setDrawValues(false);
                data.setValueTextColor(Color.WHITE);

                generalRadialChart.setData(data);
                generalRadialChart.invalidate();
            }
        }
    }


    //Line graph
    private void lineGraph() {
        detailedLineChart = (LineChart) findViewById(R.id.lineChart);

        detailedLineChart.getAxisRight().setEnabled(false);
        detailedLineChart.getAxisLeft().setEnabled(false);
        detailedLineChart.getXAxis().setEnabled(false);

        Description desc = new Description();
        desc.setText("");
        detailedLineChart.setDescription(desc);

        Legend legend = detailedLineChart.getLegend();
        legend.setEnabled(false);

        detailedLineChart.setPadding(0, 0, 0, 0);


        setLineDataToCurrent();
    }

    public void setLineDataToCurrent() {
        if (currentData != null) {
            if (currentData.size() > 0) {
                LineDataSet set1;
                set1 = new LineDataSet(currentData, "Insert Attribute here");
                set1.enableDashedLine(10f, 5f, 0f);
                set1.enableDashedHighlightLine(10f, 5f, 0f);
                set1.setColor(currentGraphColor);
                set1.setCircleColor(currentGraphColor);
                set1.setLineWidth(2f);
                set1.setCircleRadius(1f);
                set1.setDrawCircleHole(false);
                set1.setValueTextSize(18f);
                set1.setValueTextColor(Color.WHITE);

                set1.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return "";
                    }
                });

                set1.setFillColor(currentGraphColor);
                set1.setDrawFilled(true);
                set1.setFormLineWidth(1f);
                set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set1.setFormSize(15.f);
                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(set1); // add the datasets
                LineData data = new LineData(dataSets);
                detailedLineChart.setData(data);
            }
        }

    }

    public void clearLineData() {
        if (detailedLineChart != null) {
            if (detailedLineChart.getLineData() != null) {
                detailedLineChart.clearValues();
            }
        }
    }


    private void addEntryToDataSource(ArrayList<Entry> data, float x, float y) {
        data.add(new Entry(x, y));

    }


    //Set current data source buttons
    public void setFuel(View view) {
        setCurrentDataSource(fuelConsumptionData);
        currentGraphColor = Color.parseColor(this.getString(R.color.fuel));
        clearLineData();
        setLineDataToCurrent();
    }

    public void setAcceleration(View view) {
        setCurrentDataSource(accelerationData);
        currentGraphColor = Color.parseColor(this.getString(R.color.acceleration));
        clearLineData();
        setLineDataToCurrent();
    }

    public void setDistance(View view) {
        setCurrentDataSource(distanceData);
        currentGraphColor = Color.parseColor(this.getString(R.color.distance));
        clearLineData();
        setLineDataToCurrent();
    }

    public void setRPM(View view) {
        setCurrentDataSource(rpmData);
        currentGraphColor = Color.parseColor(this.getString(R.color.rpm));
        clearLineData();
        setLineDataToCurrent();
    }

    public void setLoad(View view) {
        setCurrentDataSource(loadData);
        currentGraphColor = Color.parseColor(this.getString(R.color.load));
        clearLineData();
        setLineDataToCurrent();

    }

    private void setCurrentDataSource(ArrayList<Entry> data) {
        currentData = data;
    }

    //New game
    public void newGame(View view) {
        Intent intent = new Intent(context, AliasActivity.class);
        startActivity(intent);
    }

    public void statistics(View view) {
        Intent intent = new Intent(context, StatsActivity.class);
        startActivity(intent);
    }

    //Stop game
    public void stop(View view) {
        timer.onFinish();
        timer.cancel();
        progressBar.setProgress(0);
        gameEnd();

    }

    //End game
    private void gameEnd() {
        live = false;
        Button newGameButton = (Button) findViewById(R.id.newGame);
        newGameButton.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        Button stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setVisibility(View.GONE);

<<<<<<< HEAD
        if(!uploaded) {
            ScoreDbHelper.getInstance(this).addListItem(new ScoreItem(0, alias, finalScore, new SimpleDateFormat("yyyy-MM-dd").format(new Date()), getAverage(rpmData), getAverage(accelerationData), getAverage(distanceData), getAverage(loadData), getAverage(fuelConsumptionData), 0.0));
            sendData();
        }
        uploaded = true;


=======
        //fix dialog prompt to ask if send data or not / need to set published to 0 or 1 for no / yes
        // THIS IS UPLOAD FUNCTION CALL
        sendData();

        ScoreDbHelper.getInstance(this).addListItem(new ScoreItem(0, alias, finalScore, new SimpleDateFormat("yyyy-MM-dd").format(new Date()), getAverage(rpmData), getAverage(accelerationData), getAverage(distanceData), getAverage(loadData), getAverage(fuelConsumptionData), 0.0));
>>>>>>> origin/master

    }

    //Timer for countdown
    private void startTimer() {
        startLiveUpdate();
        timer = new CountDownTimer(minutes * 60 * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) (millisUntilFinished / 1000));
            }

            public void onFinish() {
                gameEnd();

            }
        }.start();

    }

    //Start periodically reading live data
    private void startLiveUpdate() {
        final Handler h = new Handler();
        final int delay = 1000; //milliseconds

        h.postDelayed(new Runnable() {
            public void run() {
                updateLiveData();
                if (live) h.postDelayed(this, updateInterval);
            }
        }, delay);
    }

    //Get new live data
    private void updateLiveData() {
<<<<<<< HEAD

        canData.updateDataFromSource();
=======
        /*
        //canData.updateDataFromSource();
>>>>>>> origin/master
        String newScore = String.valueOf(canData.getCurrentScore());
        String overallScore = String.valueOf(canData.getOverallScore());
        float[] liveValues = canData.getLiveValues();
        addEntryToDataSource(fuelConsumptionData, xPos, liveValues[0]);
        addEntryToDataSource(accelerationData, xPos, liveValues[1]);
        addEntryToDataSource(distanceData, xPos, liveValues[2]);
        addEntryToDataSource(rpmData, xPos, liveValues[3]);
        addEntryToDataSource(loadData, xPos, liveValues[4]);
        xPos = canData.getX();

<<<<<<< HEAD
//

        /*float u = 100.0f;
=======
        float u = 100.0f;
>>>>>>> origin/master
        float v = 0.5f;
        float k = 2;
        float l = 0.01f;
        float m = 1;

        float fuelConsumption = can.getFuelConsumption();
        float rpm = can.getRPM();
        float acceleration = can.getAcceleration();
        float distance = can.getDistance();
        float load = can.getLoad();


        String newScore = String.valueOf((int) Math.floor((distance * u * load * v)/(fuelConsumption * k *rpm * l *acceleration * m)));
<<<<<<< HEAD
        */
//
=======


>>>>>>> origin/master

        //xPos++;
        clearLineData();
        setLineDataToCurrent();
        detailedLineChart.notifyDataSetChanged();


        float[] liveNormalizedValues = canData.getNormalizedLiveValues();
        //Log.d("APP", "updateLiveData: " + liveNormalizedValues[0] + ", " + liveNormalizedValues[1] + ", " + liveNormalizedValues[2] + ", " + liveNormalizedValues[3] + ", " + liveNormalizedValues[4]);

        radarData.clear();
        radarData.add(new RadarEntry(liveNormalizedValues[0]));
        radarData.add(new RadarEntry(liveNormalizedValues[1]));
        radarData.add(new RadarEntry(liveNormalizedValues[2]));
        radarData.add(new RadarEntry(liveNormalizedValues[3]));
        radarData.add(new RadarEntry(liveNormalizedValues[4]));
        setRadarData();


        gamification.updateScore(canData.getCurrentScore());

        if(gamification.overThreshold){
            score.setTextColor(Color.parseColor(this.getString(R.color.rpm)));
        }else{
            score.setTextColor(Color.parseColor(this.getString(R.color.text)));
        }
<<<<<<< HEAD
        finalScore = canData.getOverallScore();
=======
        //finalScore = overallScore;
>>>>>>> origin/master
        avgScore.setText(overallScore);
        score.setText(newScore);
        */
    }

    public void sendData() {
        RecordedData recordedData = new RecordedData();
        int currentScore = recordedData.getOverallScore();
        ArrayList<Float> fuelArray = recordedData.getHistoricalDataValues(0);
        ArrayList<Float> accelerationArray = recordedData.getHistoricalDataValues(1);
        ArrayList<Float> distanceArray = recordedData.getHistoricalDataValues(2);
        ArrayList<Float> rpmArray = recordedData.getHistoricalDataValues(3);
        ArrayList<Float> loadArray = recordedData.getHistoricalDataValues(4);


        //get current time
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
        // you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
        machine = "Excavator";
        String localTime = date.format(currentLocalTime);

<<<<<<< HEAD
        int [] timeList = new int[]{};
        for(int i = 0; i < fuelArray.size(); i ++) {
            timeList[i] = i;
        }




        //int[] timeList = {1, 2, 3, 4, 5}; // fix later to actually take time

        Toast.makeText(context, loadArray.toString(), Toast.LENGTH_SHORT).show();


        JSONObject sendObject = new JSONObject();
        try{

            sendObject.put("time", localTime);
=======
        int[] timeList = {1, 2, 3, 4, 5}; // fix later to actually take time
        ArrayList<String> sendObject = new ArrayList<String>();
        sendObject.add(localTime.toString());
        sendObject.add(alias);
        sendObject.add("100");
        sendObject.add("5");
        sendObject.add("1"); // change the 1 to result from dialog (yes/no) upload data
        sendObject.add(machine);
        sendObject.add(timeList.toString());
        sendObject.add(loadArray.toString());
        sendObject.add(fuelArray.toString());
        sendObject.add(distanceArray.toString());
        sendObject.add(accelerationArray.toString());
        sendObject.add(rpmArray.toString());
        new UploadData().execute(sendObject);
        /*JSONObject sendObject = new JSONObject();
        try{
            sendObject.put("time", localTime.toString());
>>>>>>> origin/master
            sendObject.put("alias", alias);
            sendObject.put("currentScore", currentScore);
            sendObject.put("duration", minutes);
            sendObject.put("published", 1); // change the 1 to result from dialog (yes/no) upload data
            sendObject.put("machine", machine);
            //sendObject.put("timeList", timeList);
            sendObject.put("load", new JSONArray(loadArray));
            sendObject.put("fuel", fuelArray);
            sendObject.put("distance", distanceArray);
            sendObject.put("speed", accelerationArray);
            sendObject.put("rpm", rpmArray);

        }catch (JSONException e){
            e.printStackTrace();

<<<<<<< HEAD
        Toast.makeText(context, sendObject.toString(), Toast.LENGTH_LONG).show();

        //new UploadData(this).execute(sendObject);
        new UploadData(this).execute(sendObject);
    }
=======
    } */


}
>>>>>>> origin/master


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_display);
        context = this;


        //Gamification
        gamification = new Gamification(this, 10);


        can = new CanBusInformation(this);
        canData = new RecordedData();
        canData.setDataSource(can);

        //Timer
        Intent intent = getIntent();
        minutes = intent.getIntExtra("time", 5);
        alias = intent.getStringExtra("alias");
        machine = intent.getStringExtra("machine");


        //Alias
        alias = intent.getStringExtra("alias");

        //Color
        radialGraphColor = Color.parseColor(this.getString(R.color.radial));
        radialAverageGraphColor = Color.parseColor(this.getString(R.color.radialAverage));

        //Datasets
        fuelConsumptionData = new ArrayList<Entry>();
        rpmData = new ArrayList<Entry>();
        altitudeData = new ArrayList<Entry>();
        accelerationData = new ArrayList<Entry>();
        loadData = new ArrayList<Entry>();
        distanceData = new ArrayList<Entry>();
        radarData = new ArrayList<RadarEntry>();

        //Create graphs
        lineGraph();
        radarGraph();

        //Insert average values
        //TextView averageValuesView = (TextView) findViewById(R.id.averageValuesView);
        //ScoreItem averageValues = ScoreDbHelper.getInstance(this).getAverageScoreItem();
        //averageValuesView.setText("Average Score: " + averageValues.toString());

        //Setup progress bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //progressBar.setScaleY(18f);
        progressBar.setMax(minutes*60);

        //Score
        score = (TextView) findViewById(R.id.score);
        score.setText("0");
        avgScore = (TextView) findViewById(R.id.avgScore);

        //Start countdown
        startTimer();

        //Set Fuel as start graph
        setFuel(null);


    }

    //@Override
    /*public void onBackPressed() {
        Toast.makeText(context, "You cannot go back while playing the game!", Toast.LENGTH_SHORT).show();

    }*/

    private float getAverage(ArrayList<Entry> a){
        float avg = 0;
        for (Entry e:a) {
            float ee = e.getY();
            avg += ee;
        }
        avg /= a.size();
        return avg;
    }

}
