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
import com.example.studentlistview.REST.PostHttpTask;
import com.example.studentlistview.REST.ReadHttpTask;
import com.example.studentlistview.AppBar.AppBarActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;


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


        // 1. Fetch data from public api (using cramer) - form url/endpoint #1
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                ReadDatabaseApiTask task = new ReadDatabaseApiTask();
                task.execute("https://berthabackendrestprovider.azurewebsites.net/api/data/cramer");
            }
        }, 0, 3000);



        Timer timer2 = new Timer();
        timer2.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                // The wristbandDataTask does all these 3 things:
                // -------------------------
                // 2.1 Fetch single dynamic (auto-generated) wristband data - from url/endpoint #2
                // 2.2 Append "userid"
                // 2.3 Post to Database API
                ReadWristbandApiTask wristbandDataTask = new ReadWristbandApiTask();
                wristbandDataTask.execute("https://berthawristbandrestprovider.azurewebsites.net/api/wristbanddata");
            }
        }, 0, 7000);


    }

    public void addData(View view) {
        Intent intent = new Intent(this, AddDataActivity.class);
        startActivity(intent);
    }

    /*
     * CLASS FOR CONSUMING DATABASE API
     */
    private class ReadDatabaseApiTask extends ReadHttpTask {
        @Override
        protected void onPostExecute(CharSequence jsonString) {

            // 1. Fetch data
            Gson gson = new GsonBuilder().create();
            final Data[] data = gson.fromJson(jsonString.toString(), Data[].class);

            // 2. Add data to Listview:
            ListView listView = findViewById(R.id.main_data_listview);
            //ArrayAdapter<Data> adapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, data);
            DataListenItemAdapter adapter = new DataListenItemAdapter(getBaseContext(), R.layout.datalist_item, data);
            listView.setAdapter(adapter);

            // 3. Add event handler (to inserted object/item)
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

    /*
     * CLASS FOR CONSUMING WRISTBAND API
     */
    private class ReadWristbandApiTask extends ReadHttpTask {

        // Hook triggered/called just BEFORE we get the content from the URL
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // Hook triggered/called AFTER we got the response:
        @Override
        protected void onPostExecute(CharSequence jsonString) {

            // 1. Fetch and parse "auto-generated" json object from wristband api:
            Gson gson = new GsonBuilder().create();
            final Data data = gson.fromJson(jsonString.toString(), Data.class);

            // 2. Append additional data ("userId"):
            data.setUserId("cramer");

            // 3. Post "data" to Database API:
            PostHttpTask post = new PostHttpTask();
            String dataAsJsonString = gson.toJson(data);
            post.execute("https://berthabackendrestprovider.azurewebsites.net/api/data", dataAsJsonString);

            // ...


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
