package com.example.studentlistview;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentlistview.AppBar.OrderActivity;
import com.example.studentlistview.REST.AddDataActivity;
import com.example.studentlistview.REST.Data;
import com.example.studentlistview.REST.DataActivity;
import com.example.studentlistview.REST.DataListenItemAdapter;
import com.example.studentlistview.REST.ReadHttpTask;
import com.example.studentlistview.AppBar.AppBarActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.Comparator;


public class MainActivity extends AppCompatActivity {
    private ShareActionProvider shareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //APPBAR__
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //APPBAR__

        Intent intent = getIntent();
        String username = intent.getStringExtra(LoginActivity.USERNAME);
        TextView welcomeView = findViewById(R.id.main_welcome_textview);
        welcomeView.setText("Welcome " + username);


        TextView listHeader = new TextView(this);
        listHeader.setText("Wristband Data");
        listHeader.setTextAppearance(this, android.R.style.TextAppearance_Large);
        ListView listView = findViewById(R.id.main_data_listview);
        listView.addHeaderView(listHeader);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ReadTask task = new ReadTask();
        task.execute("https://berthabackendrestprovider.azurewebsites.net/api/data/cramer");
    }

    public void addData(View view) {
        Intent intent = new Intent(this, AddDataActivity.class);
        startActivity(intent);
    }

    private class ReadTask extends ReadHttpTask {
        @Override
        protected void onPostExecute(CharSequence jsonString) {
        /*
            final List<Data> data = new ArrayList<>();
            try {
                JSONArray array = new JSONArray(jsonString.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    int deviceId = obj.getInt("DeviceId");
                    Double pm25 = obj.getDouble("pm25");
                    Double pm10 = obj.getDouble("pm10");
                    int co2 = obj.getInt("co2");
                    int o3 = obj.getInt("o3");
                    Double pressure = obj.getDouble("pressure");
                    Double temperature = obj.getDouble("temperature");
                    Double humidity = obj.getDouble("humidity");
                    Data data = new Data(deviceId, pm25, pm10, co2, o3, pressure, temperature, humidity);
                    data.add(data);
                }
                */
            Gson gson = new GsonBuilder().create();
            final Data[] data = gson.fromJson(jsonString.toString(), Data[].class);

            ListView listView = findViewById(R.id.main_data_listview);
            //ArrayAdapter<Data> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, data);
            DataListenItemAdapter adapter = new DataListenItemAdapter(getBaseContext(), R.layout.datalist_item, data);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getBaseContext(), DataActivity.class);
                    // Data data = data.get((int) deviceId);
                    // Data data = data[(int) deviceId];
                    Data data = (Data) parent.getItemAtPosition(position);
                    intent.putExtra(DataActivity.DATA, data);
                    startActivity(intent);
                }
            });
           /* } catch (JSONException ex) {
                messageTextView.setText(ex.getMessage());
                Log.e("Data", ex.getMessage());
            }*/
        }

        @Override
        protected void onCancelled(CharSequence message) {
            TextView messageTextView = findViewById(R.id.main_message_textview);
            messageTextView.setText(message);
            Log.e("DATA", message.toString());
        }


    }

    //APPBAR________________________

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareActionIntent("Want to join me for pizza?");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_order:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true; // true: menu processing done, no further actions
            case R.id.action_nothing_really:
                Toast.makeText(this, "Menu ...", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_absolutely_nothing:
                Toast.makeText(this, "Menu ...", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Head First, 2nd, page 333
    private void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        shareActionProvider.setShareIntent(intent);
    }


    //APPBAR________________________
}
