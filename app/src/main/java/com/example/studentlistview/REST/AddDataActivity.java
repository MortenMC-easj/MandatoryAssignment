package com.example.studentlistview.REST;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.studentlistview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AddDataActivity extends AppCompatActivity {

    public String userId = "cramer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        //APPBAR__

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //APPBAR__
    }

    public void addData2(View view) {
        String deviceIdString = ((EditText) findViewById(R.id.add_data_deviceId_edittext)).getText().toString();
        int deviceId = Integer.parseInt(deviceIdString);
        String pm25String = ((EditText) findViewById(R.id.add_data_pm25_edittext)).getText().toString();
        double pm25 = Double.parseDouble(pm25String);
        String pm10String = ((EditText) findViewById(R.id.add_data_pm10_edittext)).getText().toString();
        double pm10 = Double.parseDouble(pm10String);
        String co2String = ((EditText) findViewById(R.id.add_data_co2_edittext)).getText().toString();
        int co2 = Integer.parseInt(co2String);
        String o3String = ((EditText) findViewById(R.id.add_data_o3_edittext)).getText().toString();
        int o3 = Integer.parseInt(o3String);
        String pressureString = ((EditText) findViewById(R.id.add_data_pressure_edittext)).getText().toString();
        double pressure = Double.parseDouble(pressureString);
        String temperatureString = ((EditText) findViewById(R.id.add_data_temperature_edittext)).getText().toString();
        double temperature = Double.parseDouble(temperatureString);
        String humidityString = ((EditText) findViewById(R.id.add_data_humidity_edittext)).getText().toString();
        double humidity = Double.parseDouble(humidityString);
        String userId = ((EditText) findViewById(R.id.add_data_userId_edittext)).getText().toString();


        //String jsonDocument =
        //        "{\"Title\":\"" + title + "\", \"Author\":\"" + author + "\", \"Publisher\":\"" + publisher + "\", \"Price\":" + price + "}";

        TextView messageView = findViewById(R.id.add_data_message_textview);
        try { // Alternative: make a Book object + use Gson
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("deviceId", deviceId);
            jsonObject.put("pm25", pm25);
            jsonObject.put("pm10", pm10);
            jsonObject.put("co2", co2);
            jsonObject.put("o3", o3);
            jsonObject.put("pressure", pressure);
            jsonObject.put("temperature", temperature);
            jsonObject.put("humidity", humidity);
            jsonObject.put("userId", userId);
            String jsonDocument = jsonObject.toString();
            messageView.setText(jsonDocument);
            PostBookTask task = new PostBookTask();
            task.execute("https://berthabackendrestprovider.azurewebsites.net/api/data", jsonDocument);
        } catch (JSONException ex) {
            messageView.setText(ex.getMessage());
        }

    }

    private class PostBookTask extends AsyncTask<String, Void, CharSequence> {
        //private final String JsonDocument;

        //PostBookTask(String JsonDocument) {
        //    this.JsonDocument = JsonDocument;
        //}

        @Override
        protected CharSequence doInBackground(String... params) {
            String urlString = params[0];
            String jsonDocument = params[1];
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                osw.write(jsonDocument);
                osw.flush();
                osw.close();
                int responseCode = connection.getResponseCode();
                if (responseCode / 100 != 2) {
                    String responseMessage = connection.getResponseMessage();
                    throw new IOException("HTTP response code: " + responseCode + " " + responseMessage);
                }
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String line = reader.readLine();
                return line;
            } catch (MalformedURLException ex) {
                cancel(true);
                String message = ex.getMessage() + " " + urlString;
                Log.e("DATA", message);
                return message;
            } catch (IOException ex) {
                cancel(true);
                Log.e("DATA", ex.getMessage());
                return ex.getMessage();
            }
        }

        @Override
        protected void onPostExecute(CharSequence charSequence) {
            super.onPostExecute(charSequence);
            TextView messageView = findViewById(R.id.add_data_message_textview);
            messageView.setText(charSequence);
            Log.d("MINE", charSequence.toString());
            finish();
        }

        @Override
        protected void onCancelled(CharSequence charSequence) {
            super.onCancelled(charSequence);
            TextView messageView = findViewById(R.id.add_data_message_textview);
            messageView.setText(charSequence);
            Log.d("MINE", charSequence.toString());
            finish();
        }
    }

}
