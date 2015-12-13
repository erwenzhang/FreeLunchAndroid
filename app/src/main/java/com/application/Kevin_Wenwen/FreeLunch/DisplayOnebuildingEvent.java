package com.application.Kevin_Wenwen.FreeLunch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wenwen on 12/13/15.
 */
public class DisplayOnebuildingEvent extends AppCompatActivity {
    static ArrayList<HashMap<String,String>> list;
    private String[] msg;
    private String building;
    private String workerName;

    private String TAG  = "Display one day event";
    public final static String EXTRA_MESSAGE = "MESSAGE IN";


    ArrayList<String> namesList = null;
    ArrayList<String> dtsStartList = null;
    ArrayList<String> buildingsList = null;


    Context context = this;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_one_day);

        Intent intent = getIntent();
        msg = intent.getStringArrayExtra(EXTRA_MESSAGE);

        final String request_url = "http://freelunchforyou.appspot.com/ViewOnebuildingEvents";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        building = msg[0];
        workerName = msg[1];

//        delEventName = msg[2];

        params.put("building", building);


        httpClient.get(request_url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                dtsStartList = new ArrayList<String>();
                namesList = new ArrayList<String>();
                buildingsList = new ArrayList<String>();

                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    JSONArray dts_start = jObject.getJSONArray("events_dt_start");
                    JSONArray names = jObject.getJSONArray("events_name");
                    JSONArray buildings = jObject.getJSONArray("events_build");
                    for (int i = 0; i < dts_start.length(); i++) {
                        dtsStartList.add(dts_start.getString(i));
                        namesList.add(names.getString(i));
                        buildingsList.add(buildings.getString(i));
                        System.out.println(dts_start.getString(i));
                    }

                    list = new ArrayList<HashMap<String,String>>();

                    SimpleAdapter adapter = new SimpleAdapter(
                            context,
                            list,
                            R.layout.list_one_worker_item,
                            new String[] {"dt_start", "name", "building"},
                            new int[] {R.id.text1, R.id.text2, R.id.text3}
                    );
                    populateList();
                    final ListView myList=(ListView)findViewById(android.R.id.list);

                    myList.setAdapter(adapter);
//                    setListAdapter(adapter);
                    myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {

                            Intent intent = new Intent(context, DisplayOneEvent.class);
                            String[] msg_out = new String[4];
                            msg_out[0] = workerName;
                            msg_out[1] = namesList.get(position);
                            intent.putExtra(EXTRA_MESSAGE, msg_out);
                            startActivity(intent);
                        }
                    });

//                    myList.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(context, DisplayOneEvent.class);
//                            String[] msg_out = new String[4];
//                            //        msg_out[0] = email;
//                            msg_out[1] = workerName;
//                            int position = myList.getPositionForView(v);
//                            msg_out[2] = namesList.get(position);
//                            intent.putExtra(EXTRA_MESSAGE, msg_out);
//                            startActivity(intent);
//                        }
//                    });

                } catch (Exception e) {
                    System.out.println("!!!Got an exception!!!" + e.toString());
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e(TAG, "There was a problem in retrieving the url : " + e.toString());
            }
        });

    }

    private void populateList() {
        for (int i = 0; i < dtsStartList.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("dt_start", dtsStartList.get(i));
            map.put("name", namesList.get(i));
            map.put("building", buildingsList.get(i));
            list.add(map);
        }
    }

    public void deleteMyEvent(View v) {
        View parent = (View)v.getParent().getParent();
        String eventToDel = ((TextView) parent.findViewById(R.id.text2)).getText().toString();

        final String request_url = "http://freelunchforyou.appspot.com/ViewOneWorker";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("worker_name", workerName);
        params.put("delete_item", eventToDel);

        httpClient.get(request_url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                dtsStartList = new ArrayList<String>();
                namesList = new ArrayList<String>();
                buildingsList = new ArrayList<String>();

                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    JSONArray dts_start = jObject.getJSONArray("events_dt_start");
                    JSONArray names = jObject.getJSONArray("events_name");
                    JSONArray buildings = jObject.getJSONArray("events_build");
                    for (int i = 0; i < dts_start.length(); i++) {
                        dtsStartList.add(dts_start.getString(i));
                        namesList.add(names.getString(i));
                        buildingsList.add(buildings.getString(i));
                        System.out.println(dts_start.getString(i));
                    }

                    list = new ArrayList<HashMap<String,String>>();

                    SimpleAdapter adapter = new SimpleAdapter(
                            context,
                            list,
                            R.layout.list_one_worker_item,
                            new String[] {"dt_start", "name", "building"},
                            new int[] {R.id.text1, R.id.text2, R.id.text3}
                    );
                    populateList();
                    final ListView myList=(ListView)findViewById(android.R.id.list);

                    myList.setAdapter(adapter);
                    myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {

                            Intent intent = new Intent(context, DisplayOneEvent.class);
                            String[] msg_out = new String[4];
                            msg_out[0] = workerName;

                            msg_out[1] = namesList.get(position);
                            intent.putExtra(EXTRA_MESSAGE, msg_out);
                            startActivity(intent);
                        }
                    });
                } catch (Exception e) {
                    System.out.println("!!!Got an exception!!!" + e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e(TAG, "There was a problem in retrieving the url : " + e.toString());
            }
        });

    }


//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
////      TextView t = (TextView) v.findViewById(R.id.text2);
////      t.setText("Tweet Clicked");
//        Intent intent = new Intent(context, DisplayOneEvent.class);
//        String[] msg_out = new String[4];
////        msg_out[0] = email;
//        msg_out[1] = namesList.get(position);
//        intent.putExtra(EXTRA_MESSAGE, msg_out);
//        startActivity(intent);
//    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch(item.getItemId()) {
            case R.id.addEvent:
                // create intent to perform web search for this planet
                Intent intent = new Intent(context,AddEvent.class);
                String[] msg_out = new String[4];
                msg_out[0] = workerName;
                intent.putExtra(EXTRA_MESSAGE, msg_out);
                // catch event that there's no activity to handle intent

                startActivity(intent);
                return true;

            case R.id.sign_out_button:
                Intent intent1 = new Intent(context,Homepage.class);


                startActivity(intent1);
                return true;

            case R.id.myEvent:
                Intent intent2 = new Intent(context,DisplayOneWorker.class);
                String[] msg_out1 = new String[4];
                msg_out1[1] = workerName;
                intent2.putExtra(EXTRA_MESSAGE, msg_out1);
                // catch event that there's no activity to handle intent

                startActivity(intent2);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
