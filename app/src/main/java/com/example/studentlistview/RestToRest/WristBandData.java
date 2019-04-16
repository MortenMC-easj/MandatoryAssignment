package com.example.studentlistview.RestToRest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.studentlistview.R;
import com.example.studentlistview.REST.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class WristBandData extends AppCompatActivity {
    public static final String BASE_URI = "https://berthawristbandrestprovider.azurewebsites.net/api/wristbanddata";
    public static final String LOG_TAG = "simple_rest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //String single_url = BASE_URI + "/1";
        /*try {
            String jsonString = readJSonFeed(single_url);
        } catch (IOException e) {
           Log.e(LOG_TAG, e.getMessage());
        }*/

        final ReadJSONFeedTask task = new ReadJSONFeedTask();
        task.execute(BASE_URI);
    }


    private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return readJSonFeed(urls[0]);
            } catch (IOException ex) {
                Log.e(LOG_TAG, ex.toString());
                cancel(true);
                return ex.toString();
            }
        }

        @Override
        protected void onPostExecute(String jsonString) {
            final TextView textView = findViewById(R.id.mainResultTextView);
            Log.d(LOG_TAG, jsonString);
            try {
                List<Data> datas = new ArrayList<>();
                JSONArray array = new JSONArray(jsonString);
                //JSONObject jsonObject = new JSONObject(jsonString);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    final int deviceId = obj.getInt("deviceId");
                    final double pm25 = obj.getDouble("pm25");
                    final double pm10 = obj.getDouble("pm10");
                    final int co2 = obj.getInt("co2");
                    final int o3 = obj.getInt("o3");
                    final double pressure = obj.getDouble("pressure");
                    final double temperature = obj.getDouble("temperature");
                    final double humidity = obj.getDouble("humidity");
                    final String userId = obj.getString("userId");
                    Data data = new Data(deviceId, pm25, pm10, co2, o3, pressure, temperature, humidity, userId);
                    datas.add(data);
                    //textView.setText(title + ": " + body + "\n");
                }
                ArrayAdapter<Data> adapter =
                        new ArrayAdapter<Data>(getBaseContext(), android.R.layout.simple_list_item_1, datas);
                ListView listView = findViewById(R.id.mainListView);
                listView.setAdapter(adapter);

            } catch (JSONException ex) {
                textView.append(ex.toString());
            }
        }

        @Override
        protected void onCancelled(String message) {
            super.onCancelled(message);
            final TextView textView = findViewById(R.id.mainResultTextView);
            textView.setText(message);
        }
    }


    private String readJSonFeed(String urlString) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        final InputStream content = openHttpConnection(urlString);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        while (true) {
            final String line = reader.readLine();
            if (line == null)
                break;
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private InputStream openHttpConnection(final String urlString) throws IOException {
        final URL url = new URL(urlString);
        final URLConnection conn = url.openConnection();
        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        final HttpURLConnection httpConn = (HttpURLConnection) conn;
        httpConn.setAllowUserInteraction(false);
        // No user interaction like dialog boxes, etc.
        httpConn.setInstanceFollowRedirects(true);
        // follow redirects, response code 3xx
        httpConn.setRequestMethod("GET");
        httpConn.connect();
        final int response = httpConn.getResponseCode();
        if (response == HttpURLConnection.HTTP_OK) {
            return httpConn.getInputStream();
        } else {
            throw new IOException("HTTP response not OK");
        }
    }






}
