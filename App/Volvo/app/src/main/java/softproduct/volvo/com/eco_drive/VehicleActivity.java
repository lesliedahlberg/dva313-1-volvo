package softproduct.volvo.com.eco_drive;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class VehicleActivity extends Activity {

    private boolean ispreset = false;
    private SharedPreferences sharedPref;
    String preset_value = "key_is_preset";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ispreset){
            sharedPref = this.getSharedPreferences(getString(R.string.preset_vehicle), Context.MODE_PRIVATE);
            ispreset = sharedPref.getBoolean(preset_value, false);
            Intent intent = new Intent(this, AliasActivity.class);
            startActivity(intent);
        }
    }

    public void excavatorClick(View view){
        Intent intent = new Intent(this, AliasActivity.class);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preset_vehicle), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putBoolean(preset_value, true);
        edit.putString("machine", "Excavator");
        edit.commit();
        //String message = "Excavator";
        //intent.putExtra("vehicle", message);
        startActivity(intent);
    }
}
