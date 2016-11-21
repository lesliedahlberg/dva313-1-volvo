package softproduct.volvo.com.eco_drive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

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

}
