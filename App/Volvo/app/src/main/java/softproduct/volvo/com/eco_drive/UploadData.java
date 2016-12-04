package softproduct.volvo.com.eco_drive;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class UploadData extends AsyncTask<ArrayList<String>, Void, Void>{

    @Override
    protected Void doInBackground(ArrayList<String>... params) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://autoelektronikame.ipage.com/volvo/script.php");

        try {
            // Add data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
            nameValuePairs.add(new BasicNameValuePair("fuelConsumptionData", params[0].get(0)));
            nameValuePairs.add(new BasicNameValuePair("rpmData", params[0].get(1)));
            nameValuePairs.add(new BasicNameValuePair("altitudeData", params[0].get(2)));
            nameValuePairs.add(new BasicNameValuePair("accelerationData", params[0].get(3)));
            nameValuePairs.add(new BasicNameValuePair("loadData", params[0].get(4)));
            nameValuePairs.add(new BasicNameValuePair("distanceData", params[0].get(5)));
            nameValuePairs.add(new BasicNameValuePair("currentData", params[0].get(6)));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            // Execute HTTP Post Request
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
