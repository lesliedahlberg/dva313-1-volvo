package softproduct.volvo.com.eco_drive;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;

public class AliasActivity extends Activity {

    String preset_value = "key_is_preset";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alias);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preset_vehicle), Context.MODE_PRIVATE);
        String defaultValue = "missing";
        String test = sharedPref.getString("machine", defaultValue);

        EditText editText = (EditText) findViewById(R.id.editAlias);
        editText.setHint(test);
    }

    public void confirmAlias(View view){
        Intent intent = new Intent(this, TimeActivity.class);
        EditText editText = (EditText) findViewById(R.id.editAlias);
        String message = editText.getText().toString();
        intent.putExtra("alias", message);
        startActivity(intent);
    }

    public void clearPreferences(View view){
        Intent intent = new Intent(this, VehicleActivity.class);
        startActivity(intent);
    }
}
