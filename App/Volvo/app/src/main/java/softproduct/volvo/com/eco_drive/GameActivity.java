package softproduct.volvo.com.eco_drive;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.DashPathEffect;

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
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;

/**
 * Created by Porya on 2016-11-25.
 */

public class GameActivity extends Activity {

    //Charts
    private LineChart detailedLineChart;
    private RadarChart generalRadialChart;

    int minutes;
    Context context;

    //Data arrays for charts
    ArrayList<Entry> fuelConsumptionData;
    ArrayList<Entry> rpmData;
    ArrayList<Entry> altitudeData;
    ArrayList<Entry> accelerationData;
    ArrayList<Entry> loadData;
    ArrayList<Entry> distanceData;
    ArrayList<Entry> currentData;

    int currentGraphColor;
    int radialGraphColor;
    int radialAverageGraphColor;

    ProgressBar progressBar;

    private void addEntryToDataSource(ArrayList<Entry> data, float x, float y){
        data.add(new Entry(x, y));
    }


    private void setCurrentDataSource(ArrayList<Entry> data){
        currentData = data;
    }

    private void createDummyDataForDataSource(ArrayList<Entry> data, int count, float range){
        for (int i = 0; i < count; i++) {
        float val = (float) (Math.random() * range) + 3;
            addEntryToDataSource(data, i, val);
        }
    }

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

    public void clearLineData(){
        if(detailedLineChart != null)
            detailedLineChart.clearValues();
    }

    public void setLineDataToCurrent(){
        LineDataSet set1;
        set1 = new LineDataSet(currentData, "Insert Attribute here");
        set1.enableDashedLine(10f, 5f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(currentGraphColor);
        set1.setCircleColor(currentGraphColor);
        set1.setLineWidth(2f);
        set1.setCircleRadius(10f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(18f);
        set1.setValueTextColor(Color.WHITE);
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_display);

        context = this;

        Intent intent = getIntent();
        minutes = intent.getIntExtra("time", 5);

        radialGraphColor = Color.parseColor(this.getString(R.color.radial));
        radialAverageGraphColor = Color.parseColor(this.getString(R.color.radialAverage));


        fuelConsumptionData = new ArrayList<Entry>();
        rpmData = new ArrayList<Entry>();
        altitudeData = new ArrayList<Entry>();
        accelerationData = new ArrayList<Entry>();
        loadData = new ArrayList<Entry>();
        distanceData = new ArrayList<Entry>();



        createDummyDataForDataSource(fuelConsumptionData, 10, 100);
        createDummyDataForDataSource(rpmData, 10, 100);
        createDummyDataForDataSource(altitudeData, 10, 100);
        createDummyDataForDataSource(accelerationData, 10, 100);
        createDummyDataForDataSource(loadData, 10, 100);
        createDummyDataForDataSource(distanceData, 10, 100);

        lineGraph();
        radarGraph();

        setFuel(null);

        //Insert average values
        TextView averageValuesView = (TextView) findViewById(R.id.averageValuesView);
        ScoreItem averageValues = ScoreDbHelper.getInstance(this).getAverageScoreItem();
        averageValuesView.setText("Average Score: " + averageValues.toString());


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setScaleY(18f);
        progressBar.setMax(minutes*60);

        startTimer();


    }

    private void startTimer(){
        new CountDownTimer(minutes*60*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) (millisUntilFinished / 1000));
            }

            public void onFinish() {
                gameEnd();

            }
        }.start();
    }

    public void stop(View view){
        gameEnd();
    }

    private void gameEnd() {
        Intent intent = new Intent(context, StatsActivity.class);
        startActivity(intent);
    }


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
    private void radarGraph(){

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
        yAxis.setAxisMaximum(80f);
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

        float mult = 80;
        float min = 20;
        int cnt = 5;

        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            float val1 = (float) (Math.random() * mult) + min;
            entries1.add(new RadarEntry(val1));

            float val2 = (float) (Math.random() * mult) + min;
            entries2.add(new RadarEntry(val2));
        }

        RadarDataSet set1 = new RadarDataSet(entries1, "Average");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(radialGraphColor);
        set1.setDrawFilled(true);
        set1.setFillAlpha(200);
        set1.setLineWidth(4f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "You");
        set2.setColor(Color.rgb(121, 162, 175));
        set2.setFillColor(radialAverageGraphColor);
        set2.setDrawFilled(true);
        set2.setFillAlpha(100);
        set2.setLineWidth(4f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();

        sets.add(set2);
        sets.add(set1);

        RadarData data = new RadarData(sets);
        //data.setValueTypeface(mTfLight);
        data.setValueTextSize(18f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        generalRadialChart.setData(data);
        generalRadialChart.invalidate();
    }
}
