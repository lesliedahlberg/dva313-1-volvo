package softproduct.volvo.com.eco_drive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;

public class TimeActivity extends Activity {

    private boolean pressed = false;
    private long millisUntilFinished;
    public CountDownTimer countDownTimer;
    public String val;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Intent intent = getIntent();
        val = intent.getStringExtra("alias");

        final Button startButton = (Button) findViewById(R.id.startButton);
        Button returnButton = (Button) findViewById(R.id.returnButton);
        final TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);

        timePicker.setIs24HourView(true);


        startButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                if (pressed != true) {
                    pressed = true;
                    countDownTimer = new CountDownTimer((timePicker.getCurrentHour()*60*60*1000)+(timePicker.getCurrentMinute()*60*1000), 60000) {
                        public void onTick(long sUntilFinished) {
                        }

                        public void onFinish() {
                            pressed = false;
                        }
                    }.start();
                    millisUntilFinished = (timePicker.getCurrentHour()*60*60*1000)+(timePicker.getCurrentMinute()*60*1000);
                    startGameActivity(startButton);
                }
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                pressed = false;
                countDownTimer.cancel();

                Intent mainIntent = new Intent(TimeActivity.this, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mainIntent);
                finish();
            }
        });

    }
    public void startGameActivity(View view) {

        Intent intent = new Intent(this, GameActivity.class);
        Bundle extras = new Bundle();

        extras.putString("alias", val);
        extras.putLong("time", millisUntilFinished);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
