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
        TextView userIdView = dataView.findViewById(R.id.datalist_item_userId);
        deviceIdView.setText("Device: " + Integer.toString(deviceId));
        userIdView.setText(" User: " + userId);
        return dataView;
    }




}
