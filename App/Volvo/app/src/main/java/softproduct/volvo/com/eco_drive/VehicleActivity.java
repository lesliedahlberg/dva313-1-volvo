package softproduct.volvo.com.eco_drive;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putString("machine", "Excavator");
        edit.commit();
        //String message = "Excavator";
        //intent.putExtra("vehicle", message);
        startActivity(intent);
    }
}
