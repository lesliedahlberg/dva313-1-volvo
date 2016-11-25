package softproduct.volvo.com.eco_drive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.FrameLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;

public class TimeActivity extends Activity {

    private boolean pressed = false;
    private long millisUntilFinished;
    public CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Intent intent = getIntent();
        final String val = intent.getStringExtra("lo");

        final Button startButton = (Button) findViewById(R.id.startButton);
        Button returnButton = (Button) findViewById(R.id.returnButton);
        final TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        final TextView timeLabel = (TextView) findViewById(R.id.timeLabel);

        timePicker.setIs24HourView(true);


        startButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                if (pressed != true) {
                    pressed = true;
                    countDownTimer = new CountDownTimer(((timePicker.getCurrentHour()*60)+timePicker.getCurrentMinute()) * 1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            timeLabel.setText("Time remaining: " + millisUntilFinished / 1000);
                            ;
                        }

                        public void onFinish() {
                            timeLabel.setText("done!");
                            pressed = false;
                        } // DATA BUNDLE SEND INFO OF TIME TO NEXT ACTIVITY AND TAKE DATA FROM PREVIOUS
                    }.start();
                    millisUntilFinished = (timePicker.getCurrentHour()*60)+timePicker.getCurrentMinute() * 1000;
                }
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
            pressed = false;
            countDownTimer.cancel();
            timeLabel.setText("Time reset");
        }
        }

        );

    }
    public void startGameActivity(View view) {
        Intent intent = new Intent(this, TimeActivity.class);
        Bundle extras = new Bundle();
        extras.putLong("time", millisUntilFinished);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
