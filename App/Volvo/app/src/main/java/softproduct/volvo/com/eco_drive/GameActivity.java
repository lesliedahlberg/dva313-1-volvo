package softproduct.volvo.com.eco_drive;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.github.mikephil.charting.components.MarkerView;
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
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Porya on 2016-11-25.
 */

public class GameActivity extends Activity {
    private LineChart mChart;
    private RadarChart mChart2;

    ArrayList<Entry> fuelConsumptionData;
    ArrayList<Entry> rpmData;
    ArrayList<Entry> altitudeData;
    ArrayList<Entry> accelerationData;
    ArrayList<Entry> loadData;
    ArrayList<Entry> distanceData;

    ArrayList<Entry> currentData;

    private void updateData(ArrayList<Entry> data, float x, float y){
        data.add(new Entry(x, y));
    }


    private void setCurrentData(ArrayList<Entry> data){
        currentData = data;
    }

    private void createDummyData(ArrayList<Entry> data, int count, float range){
        for (int i = 0; i < count; i++) {
        float val = (float) (Math.random() * range) + 3;
            updateData(data, i, val);
        }
    }

    public void setFuel(View view) {
        setCurrentData(fuelConsumptionData);
        setLineDataToCurrent();
    }
    public void setAcceleration(View view) {
        setCurrentData(accelerationData);
        setLineDataToCurrent();
    }
    public void setDistance(View view) {
        setCurrentData(distanceData);
        setLineDataToCurrent();
    }
    public void setRPM(View view) {
        setCurrentData(rpmData);
        setLineDataToCurrent();
    }
    public void setLoad(View view) {
        setCurrentData(loadData);
        setLineDataToCurrent();

    }

    public void setLineDataToCurrent(){
        LineDataSet set1;
        set1 = new LineDataSet(currentData, "Insert Attribute here");
        set1.enableDashedLine(10f, 5f, 0f);
        set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.WHITE);
        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(1f);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        set1.setFormLineWidth(1f);
        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set1.setFormSize(15.f);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets
        LineData data = new LineData(dataSets);
        mChart.clearValues();
        mChart.setData(data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_display);


        fuelConsumptionData = new ArrayList<Entry>();
        rpmData = new ArrayList<Entry>();
        altitudeData = new ArrayList<Entry>();
        accelerationData = new ArrayList<Entry>();
        loadData = new ArrayList<Entry>();
        distanceData = new ArrayList<Entry>();

        currentData = fuelConsumptionData;

        createDummyData(fuelConsumptionData, 10, 100);
        createDummyData(rpmData, 10, 100);
        createDummyData(altitudeData, 10, 100);
        createDummyData(accelerationData, 10, 100);
        createDummyData(loadData, 10, 100);
        createDummyData(distanceData, 10, 100);

        lineGraph();
        radarGraph();

        //setLineDataToCurrent();


    }

    //////////////////////SET DATA///////////////////////////
    private void setLineData(int count, float range) {

        ArrayList<Entry> values = currentData;


        //for (int i = 0; i < count; i++) {

            //float val = (float) (Math.random() * range) + 3;
            //values.add(new Entry(i, val));
        //}

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "Insert Attribute here");

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.WHITE);
            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
                /*
                if (Utils.getSDKInt() >= 18) {
                    // fill drawable only supported on api level 18 and above
                    Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                    set1.setFillDrawable(drawable);
                }
                else {
                    set1.setFillColor(Color.BLACK);
                }*/

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1); // add the datasets

            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChart.setData(data);
        }
    }

    private void lineGraph() {
        mChart = (LineChart) findViewById(R.id.lineChart);

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);
        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        //Typeface tf = Typeface.createFromAsset(getAssets(), "Ariel.ttf");

        LimitLine ll1 = new LimitLine(150f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
        //ll1.setTypeface(tf);

        LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        ll2.setTextSize(10f);
        //ll2.setTypeface(tf);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(-50f);
        //leftAxis.setYOffset(20f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        setLineData(45, 100);


//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        mChart.animateX(2500);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);

    }
    private void radarGraph(){

        mChart2 = (RadarChart) findViewById(R.id.GameActivity_radarChart);
        //mChart2.setBackgroundColor(Color.rgb(60, 65, 82)); //change later to something else

        mChart2.getDescription().setEnabled(false);

        mChart2.setWebLineWidth(1f);
        mChart2.setWebColor(Color.LTGRAY);
        mChart2.setWebLineWidthInner(1f);
        mChart2.setWebColorInner(Color.LTGRAY);
        mChart2.setWebAlpha(500);



        setRadarData();

        mChart.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = mChart2.getXAxis();
        //xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(12f);
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

        YAxis yAxis = mChart2.getYAxis();
        //yAxis.setTypeface(mTfLight);
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(12f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);

        Legend l = mChart2.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        //l.setTypeface(mTfLight);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.WHITE);
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
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(4f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "You");
        set2.setColor(Color.rgb(121, 162, 175));
        set2.setFillColor(Color.rgb(121, 162, 175));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(4f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(sets);
        //data.setValueTypeface(mTfLight);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        mChart2.setData(data);
        mChart2.invalidate();
    }
}
