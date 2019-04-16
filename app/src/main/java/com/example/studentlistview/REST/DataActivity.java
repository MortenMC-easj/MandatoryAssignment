package com.example.studentlistview.REST;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.example.studentlistview.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DataActivity extends AppCompatActivity {

    public static final String DATA = "DATA";
    private Data data;
    private EditText deviceIdView, pm25View, pm10View, co2View, o3View, pressureView, temperatureView, humidityView, userIdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        Intent intent = getIntent();
        data = (Data) intent.getSerializableExtra(DATA);

        TextView headingView = findViewById(R.id.data_heading_textview);
        headingView.setText("Data deviceId=" + data.getDeviceId());

        pm25View = findViewById(R.id.data_pm25_edittext);
        pm25View.setText(data.getPm25() + " ");

        pm10View = findViewById(R.id.data_pm10_edittext);
        pm10View.setText(data.getPm10() + " ");

        co2View = findViewById(R.id.data_co2_edittext);
        co2View.setText(data.getCo2() + " ");

        o3View = findViewById(R.id.data_o3_edittext);
        o3View.setText(data.getO3() + " ");

        pressureView = findViewById(R.id.data_pressure_edittext);
        pressureView.setText(data.getPressure() + "");

        temperatureView = findViewById(R.id.data_temperature_edittext);
        temperatureView.setText(data.getTemperature() + "");

        humidityView = findViewById(R.id.data_humidity_edittext);
        humidityView.setText(data.getHumidity() + " ");

        userIdView = findViewById(R.id.data_userId_edittext);
        userIdView.setText(data.getUserId() + " ");
    }

    public void deleteData(View view) {
        DeleteTask task = new DeleteTask();
        task.execute("https://berthabackendrestprovider.azurewebsites.net/api/data/cramer/" + data.getDeviceId());
        finish();
    }

    public void updateData(View view) {
        // code missing: Left as an exercise
    }


    public void back(View view) {
        finish();
    }

    private class DeleteTask extends AsyncTask<String, Void, CharSequence> {
        @Override
        protected CharSequence doInBackground(String... urls) {
            String urlString = urls[0];
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");
                int responseCode = connection.getResponseCode();
                if (responseCode % 100 != 2) {
                    throw new IOException("Response code: " + responseCode);
                }
                return "Nothing";
            } catch (MalformedURLException e) {
                return e.getMessage() + " " + urlString;
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onCancelled(CharSequence charSequence) {
            super.onCancelled(charSequence);
            TextView messageView = findViewById(R.id.data_message_textview);
            messageView.setText(charSequence);
        }
    }


}
