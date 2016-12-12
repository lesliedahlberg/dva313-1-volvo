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
    String s = "fail";


    public UploadData(Context context) {
        this.context = context;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }

    @Override
    protected Void doInBackground(JSONObject... params) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://autoelektronikame.ipage.com/volvo/php/upload-data.php");

        try {
            // Add data



            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
            nameValuePairs.add(new BasicNameValuePair("data", params[0].toString()));

            /*nameValuePairs.add(new BasicNameValuePair("time", params[0].getJSONObject("time").toString()));
            nameValuePairs.add(new BasicNameValuePair("alias", params[0].getJSONObject("alias").toString()));
            nameValuePairs.add(new BasicNameValuePair("score", params[0].getJSONObject("currentScore").toString()));
            nameValuePairs.add(new BasicNameValuePair("duration", params[0].getJSONObject("duration").toString()));
            nameValuePairs.add(new BasicNameValuePair("published", params[0].getJSONObject("published").toString()));
            nameValuePairs.add(new BasicNameValuePair("machine", params[0].getJSONObject("machine").toString()));
            nameValuePairs.add(new BasicNameValuePair("timeList", params[0].getJSONObject("timeList").toString()));
            nameValuePairs.add(new BasicNameValuePair("load", params[0].getJSONObject("load").toString()));
            nameValuePairs.add(new BasicNameValuePair("fuel", params[0].getJSONObject("fuel").toString()));
            nameValuePairs.add(new BasicNameValuePair("distance", params[0].getJSONObject("distance").toString()));
            nameValuePairs.add(new BasicNameValuePair("speed", params[0].getJSONObject("speed").toString()));
            nameValuePairs.add(new BasicNameValuePair("rpm", params[0].getJSONObject("rpm").toString()));*/

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);


        } catch (ClientProtocolException e) {
            System.out.println("First Exception of HttpResponse :" + e);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Second Exception of HttpResponse :" + e);
            e.printStackTrace();
        }/*catch (JSONException e) {
            System.out.println("Second Exception of HttpResponse :" + e);
            e.printStackTrace();
        }*/

        return null;
    }

}
