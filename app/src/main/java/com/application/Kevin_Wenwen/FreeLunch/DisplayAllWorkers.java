package com.application.Kevin_Wenwen.FreeLunch;
import android.app.ListActivity;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class DisplayAllWorkers extends AppCompatActivity {

    static ArrayList<HashMap<String,String>> list;

    Context context = this;
    private String TAG  = "Display Images";
    private String email;
    private String[] msg;
    public final static String EXTRA_MESSAGE = "MESSAGE IN";
    public final static String EXTRA_MESSAGE1 = "com.displayimages.MESSAGE";

    ArrayList<String> namesList = null;
    ArrayList<String> ratingsList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_all_workers);


        Intent intent = getIntent();
        intent.getClass();
        // String source = intent.getStringExtra("From");
        msg = intent.getStringArrayExtra(EXTRA_MESSAGE);

        email = msg[0];

        final String request_url = "http://freelunchforyou.appspot.com/ViewAllWorkers";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(request_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                namesList = new ArrayList<String>();
                ratingsList = new ArrayList<String>();
                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    JSONArray names = jObject.getJSONArray("names");
                    JSONArray ratings = jObject.getJSONArray("ratings");
                    for (int i = 0; i < names.length(); i++) {
                        namesList.add(names.getString(i));
                        ratingsList.add(ratings.getString(i));
                    }

                    list = new ArrayList<HashMap<String,String>>();

                    SimpleAdapter adapter = new SimpleAdapter(
                            context,
                            list,
                            R.layout.list_all_workers_item,
                            new String[] {"name", "rating"},
                            new int[] {R.id.text1, R.id.text2}
                    );
                    populateList();
//                    setListAdapter(adapter);
                    final ListView myList = (ListView) findViewById(android.R.id.list);
                    myList.setAdapter(adapter);
                    myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {

                            Intent intent = new Intent(context, DisplayOneWorker.class);
                            String[] msg_out = new String[4];
                            msg_out[0] = email;
                            msg_out[1] = namesList.get(position);
                            intent.putExtra(EXTRA_MESSAGE, msg_out);
                            startActivity(intent);
                        }
                    });

                } catch (JSONException j) {
                    System.out.println("JSON Error");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e(TAG, "There was a problem in retrieving the url : " + e.toString());
            }
        });
    }

    private void populateList() {
        for (int i = 0; i < namesList.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            List<String> elephantList = Arrays.asList(namesList.get(i).split("@"));
            Log.d("now   ",elephantList.get(0));
            map.put("name", elephantList.get(0));
            map.put("rating", ratingsList.get(i));
            list.add(map);
        }
    }







}
