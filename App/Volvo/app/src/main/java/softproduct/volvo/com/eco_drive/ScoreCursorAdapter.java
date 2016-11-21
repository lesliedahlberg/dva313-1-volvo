package softproduct.volvo.com.eco_drive;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lesliedahlberg on 2016-11-21.
 * This class connects a database cursor to a list view
 */
public class ScoreCursorAdapter extends CursorAdapter {

    int expandedId;

    public ScoreCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
        expandedId = -1;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.score_item, parent, false);
    }



    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //id
        final int currentId = cursor.getInt(cursor.getColumnIndexOrThrow(ScoreDbHelper.KEY_LIST_ID));

        //Get references to view objects in list
        TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        final TextView aliasTextView = (TextView) view.findViewById(R.id.aliasTextView);
        TextView scoreTextView = (TextView) view.findViewById(R.id.scoreTextView);
        final LinearLayout expandedScoreView = (LinearLayout) view.findViewById(R.id.expandedScoreView);
        //Get references to expanded objects in list
        TextView rpmTextView = (TextView) view.findViewById(R.id.rpmTextView);
        TextView accelerationTextView = (TextView) view.findViewById(R.id.accelerationTextView);
        TextView distanceTextView = (TextView) view.findViewById(R.id.distanceTextView);
        TextView loadTextView = (TextView) view.findViewById(R.id.loadTextView);
        TextView consumptionTextView = (TextView) view.findViewById(R.id.consumptionTextView);
        TextView altitudeTextView = (TextView) view.findViewById(R.id.altitudeTextView);
        RadarChart radarChart = (RadarChart) view.findViewById(R.id.radarChart);

        //Get cursor values
        String date = cursor.getString(cursor.getColumnIndexOrThrow(ScoreDbHelper.KEY_LIST_DATE));
        String alias = cursor.getString(cursor.getColumnIndexOrThrow(ScoreDbHelper.KEY_LIST_ALIAS));
        int score = cursor.getInt(cursor.getColumnIndexOrThrow(ScoreDbHelper.KEY_LIST_SCORE));
        //Get cursor values for expanded items
        double rpm = cursor.getDouble(cursor.getColumnIndexOrThrow(ScoreDbHelper.KEY_LIST_RPM));
        double acceleration = cursor.getDouble(cursor.getColumnIndexOrThrow(ScoreDbHelper.KEY_LIST_ACCELERATION));
        double distance = cursor.getDouble(cursor.getColumnIndexOrThrow(ScoreDbHelper.KEY_LIST_DISTANCE));
        double load = cursor.getDouble(cursor.getColumnIndexOrThrow(ScoreDbHelper.KEY_LIST_LOAD));
        double consumption = cursor.getDouble(cursor.getColumnIndexOrThrow(ScoreDbHelper.KEY_LIST_CONSUMPTION));
        double altitude = cursor.getDouble(cursor.getColumnIndexOrThrow(ScoreDbHelper.KEY_LIST_ALTITUDE));

        //Pass values to view objects
        dateTextView.setText(date);
        aliasTextView.setText(alias);
        scoreTextView.setText(Integer.toString(score));
        //Pass values to expanded view objects
        rpmTextView.setText("RPM: " + Double.toString(rpm));
        accelerationTextView.setText("Acceleration: " + Double.toString(acceleration) + "m/s/s");
        distanceTextView.setText("Distance: " + Double.toString(distance) + "km");
        loadTextView.setText("Load: " + Double.toString(load) + "kg");
        consumptionTextView.setText("Consumption: " + Double.toString(consumption) + "l/100km");
        altitudeTextView.setText("Altitude: " + Double.toString(altitude) + "m");
        //Pass values to radar chart



        List<RadarEntry> entries = new ArrayList<RadarEntry>();
        entries.add(new RadarEntry((float)rpm/100.0f, "HEELEL"));
        entries.add(new RadarEntry((float)acceleration*2500.0f));
        entries.add(new RadarEntry((float)distance));
        entries.add(new RadarEntry((float)load/15.0f));
        entries.add(new RadarEntry((float)consumption*13.0f));
        entries.add(new RadarEntry((float)altitude/10.0f));

        RadarDataSet set = new RadarDataSet(entries, "Score");
        RadarData data = new RadarData(set);

        XAxis xAxis = radarChart.getXAxis();


        // the labels that should be drawn on the XAxis
        final String[] quarters = new String[] { "RPM", "Acceleration", "Distance", "Load", "Consumption", "Altitude"};

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return quarters[(int) value%6];
            }


        };


        //xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);


        xAxis.setDrawLabels(true);
        //YAxis yAxis = radarChart.getYAxis();;
        Legend legend = radarChart.getLegend();
        legend.setEnabled(false);
        radarChart.setData(data);
        radarChart.invalidate();

        //Set Expanded View
        if(expandedId != currentId){
            expandedScoreView.setVisibility(View.GONE);
        }else{
            expandedScoreView.setVisibility(View.VISIBLE);
        }

        //Set expand on click
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = currentId;
                if(expandedId == id){
                    expandedId = -1;
                    expandedScoreView.setVisibility(View.GONE);
                }else{
                    expandedId = id;
                    expandedScoreView.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        });

    }
}
