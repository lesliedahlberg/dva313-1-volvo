package softproduct.volvo.com.eco_drive;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class StatsActivity extends Activity {

    //Declare view objects
    ListView statsListView;

    //Declare DB objects
    Cursor scoreCursor;
    ScoreCursorAdapter scoreCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_highscore);

        //Init view objects
        statsListView = (ListView) findViewById(R.id.statsListView);

        //Connect listView to DB
        scoreCursor = ScoreDbHelper.getInstance(this).getCursor();
        scoreCursorAdapter = new ScoreCursorAdapter(this, scoreCursor, 0);
        statsListView.setAdapter(scoreCursorAdapter);

        //Set empty view for ListView
        FrameLayout emptyListView = (FrameLayout) findViewById(R.id.emptyView);
        statsListView.setEmptyView(emptyListView);

    }
    public void sendData(View view){
        Intent intent = getIntent();
        /*RecordedData recordedData = new RecordedData();
        float[] dataList = recordedData.getNormalizedLiveValues();*/
        ArrayList<String> dataList = new ArrayList<String>();
        dataList.add(0, "fuelConsumptionData"); //This will be changed to the values
        dataList.add(1, "rpmData");
        dataList.add(2,"altitudeData");
        dataList.add(3,"accelerationData");
        dataList.add(4,"loadData");
        dataList.add(5, "distanceData");
        dataList.add(6,"currentData");
        new UploadData().execute(dataList);
    }
    public void returnToPrevious(View view) {
        finish();
    }
}
