package softproduct.volvo.com.eco_drive;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadData extends AsyncTask<JSONObject, Void, Void>{

    Context context;
    public UploadData(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(JSONObject... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://autoelektronikame.ipage.com/volvo/php/upload-data.php");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
            nameValuePairs.add(new BasicNameValuePair("data", params[0].toString()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
        } catch (ClientProtocolException e) {
            System.out.println("First Exception of HttpResponse :" + e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Second Exception of HttpResponse :" + e);
            e.printStackTrace();
        }
        return null;
    }

}
