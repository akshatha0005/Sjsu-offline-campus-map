package edu.sjsu.sjsumap.service;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by i856547 on 10/28/16.
 */

public class RestHandler extends AsyncTask<Void,HashMap<String, String>,HashMap<String, String>> {
    HashMap<String,String> result = new HashMap<>();
    Double lat;
    Double lng;
    String dest;
    TextView distance;
    TextView time;

    public void setValues(Double lat, Double lng, String dest, TextView dist, TextView time) {
        this.lat = lat;
        this.lng = lng;
        this.dest = dest;
        this.distance = dist;
        this.time = time;
    }



    @Override
    protected HashMap<String, String> doInBackground(Void... params) {
        StringBuilder stringBuilder = new StringBuilder();
        Double dist = 0.0;
        Double dura = 0.0;
        try {

            dest = dest.replaceAll(" ", "%20");
            String url = "http://maps.googleapis.com/maps/api/directions/json?origin=" + lat + "," + lng + "&destination=" + dest + "&mode=walking&sensor=false";
            Log.d("url",url);

            HttpPost httppost = new HttpPost(url);

            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            stringBuilder = new StringBuilder();


            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject;
        try {

            jsonObject = new JSONObject(stringBuilder.toString());

            JSONArray array = jsonObject.getJSONArray("routes");

            JSONObject routes = array.getJSONObject(0);

            JSONArray legs = routes.getJSONArray("legs");

            JSONObject steps = legs.getJSONObject(0);

            JSONObject distance = steps.getJSONObject("distance");

            JSONObject duration = steps.getJSONObject("duration");

            Log.d("Distance", distance.getString("text"));
            Log.d("Duration", duration.getString("text"));

            if(distance.getString("text") != null && distance.getString("text").contains("ft")){
               //distance in feet
                dist = Double.parseDouble(distance.getString("text").replaceAll("[^\\.0123456789]", ""));
               dist = Double.valueOf(Math.round((dist/5280) * 10000.0) / 10000.0);


            }else {
                dist = Double.parseDouble(distance.getString("text").replaceAll("[^\\.0123456789]", ""));

            }
            dura = Double.parseDouble(duration.getString("text").replaceAll("[^\\.0123456789]", ""));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        result.put("dist",dist.toString());
        result.put("dura",dura.toString());
        return result;
    }

    @Override
    protected void onPostExecute(HashMap<String, String> res) {
        Log.d("distance",res.get("dist"));
        Log.d("duration",res.get("dura"));
        distance.setText(res.get("dist"));
        time.setText(res.get("dura"));
    }
}
