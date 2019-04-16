package com.example.studentlistview.REST;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.studentlistview.R;

import java.util.List;


public class DataListenItemAdapter extends ArrayAdapter<Data> {
    private final int resource;

    public DataListenItemAdapter(Context context, int resource, List<Data> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    public DataListenItemAdapter(Context context, int resource, Data[] objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Data data = getItem(position);
        Integer deviceId = data.getDeviceId();
        Double pm25 = data.getPm25();
        Double pm10 = data.getPm10();
        Integer co2 = data.getCo2();
        Integer o3 = data.getO3();
        Double pressure = data.getPressure();
        Double temperature = data.getTemperature();
        Double humidity = data.getHumidity();
        String userId = data.getUserId();
        LinearLayout dataView;
        if (convertView == null) {
            dataView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(inflater);
            li.inflate(resource, dataView, true);
        } else {
            dataView = (LinearLayout) convertView;
        }
        TextView deviceIdView = dataView.findViewById(R.id.datalist_item_deviceId);
        TextView pm25View = dataView.findViewById(R.id.datalist_item_pm25);
        TextView pm10View = dataView.findViewById(R.id.datalist_item_pm10);
        TextView co2View = dataView.findViewById(R.id.datalist_item_co2);
        TextView o3View = dataView.findViewById(R.id.datalist_item_o3);
        TextView pressureView = dataView.findViewById(R.id.datalist_item_pressure);
        TextView temperatureView = dataView.findViewById(R.id.datalist_item_temperature);
        TextView humidityView = dataView.findViewById(R.id.datalist_item_humidity);
        TextView userIdView = dataView.findViewById(R.id.datalist_item_userId);
        deviceIdView.setText(" Device: " + Integer.toString(deviceId));
        pm25View.setText(" Pm25: " + Double.toString(pm25));
        pm10View.setText(" Pm10: " + Double.toString(pm10));
        co2View.setText(" Co2: " + Integer.toString(co2));
        o3View.setText(" o3: " + Integer.toString(o3));
        pressureView.setText(" Pressure: " + Double.toString(pressure));
        temperatureView.setText(" Temperature: " + Double.toString(temperature));
        humidityView.setText(" Humidity: " + Double.toString(humidity));
        userIdView.setText("User: " + userId);
        return dataView;
    }




}
