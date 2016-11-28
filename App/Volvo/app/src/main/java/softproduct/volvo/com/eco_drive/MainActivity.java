package softproduct.volvo.com.eco_drive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    private String alias ="lava";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /* Callback for button to start Highscore activity for testing */
    public void startHighscoreActivity(View view) {
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }

    public void generateStatsDummyData(View view) {
        ScoreDbHelper.getInstance(this).createDummyData(20);
    }
    public void startTimeActivity(View view) {
        Intent intent = new Intent(this, TimeActivity.class);
        Bundle extras = new Bundle();
        extras.putString("alias", alias);
        intent.putExtras(extras);
        startActivity(intent);
    }
    public void startGameActivity(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
    public void startSampleSwipeActivity(View view) {
        Intent intent = new Intent(this, SampleSwipeActivity.class);
        startActivity(intent);
    }

    public void startAliasActivity(View view) {
        Intent intent = new Intent(this, AliasActivity.class);
        startActivity(intent);
    }

    public void startVehicleActivity(View view) {
        Intent intent = new Intent(this, VehicleActivity.class);
        startActivity(intent);
    }

}
