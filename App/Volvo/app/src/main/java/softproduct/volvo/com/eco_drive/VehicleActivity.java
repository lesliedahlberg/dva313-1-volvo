package softproduct.volvo.com.eco_drive;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class VehicleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);
    }

    public void excavatorClick(View view){
        Intent intent = new Intent(this, AliasActivity.class);
        String message = "Excavator";
        intent.putExtra("vehicle", message);
        startActivity(intent);
    }
}
