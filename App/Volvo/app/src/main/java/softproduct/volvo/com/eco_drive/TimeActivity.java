package softproduct.volvo.com.eco_drive;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import java.sql.Time;

public class TimeActivity extends Activity {

    int minutes;
    public String val;
    public String machine;
    NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Intent intent = getIntent();
        val = intent.getStringExtra("alias");
        machine = intent.getStringExtra("machine");

        numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(60);
        numberPicker.setValue(5);
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


    }

    public void start(View view) {

        Intent intent = new Intent(this, GameActivity.class);
        Bundle extras = new Bundle();

        extras.putString("alias", val);
        extras.putInt("time", numberPicker.getValue());
        extras.putString("machine", machine);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void _return(View view) {
       finish();
    }
}
