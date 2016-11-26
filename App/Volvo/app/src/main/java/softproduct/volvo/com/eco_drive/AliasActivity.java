package softproduct.volvo.com.eco_drive;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;

public class AliasActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alias);
    }

    public void confirmAlias(View view){
        Intent intent = new Intent(this, TimeActivity.class);
        EditText editText = (EditText) findViewById(R.id.editAlias);
        String message = editText.getText().toString();
        intent.putExtra("alias", message);
        startActivity(intent);
    }
}
