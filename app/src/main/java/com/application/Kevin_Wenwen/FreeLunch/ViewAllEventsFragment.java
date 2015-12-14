package com.application.Kevin_Wenwen.FreeLunch;

import android.content.Context;
import android.support.v4.app.DialogFragment;


import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wenwen on 12/9/15.
 */
public class ViewAllEventsFragment extends DialogFragment {
    public static final String ARG_PLANET_NUMBER = "planet_number";
    public final static String EXTRA_MESSAGE = "MESSAGE IN";

    static ArrayList<HashMap<String,String>> list;

    private String TAG  = "Display Images";
    private String email;
    private String[] msg;

    public final static String EXTRA_MESSAGE1 = "com.displayimages.MESSAGE";

    ArrayList<String> namesList = null;
    ArrayList<String> dtsStartList = null;
    ArrayList<String> buildingsList = null;

    public ViewAllEventsFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        rootView = inflater.inflate(R.layout.clistview,container,false);

        final String request_url = "http://freelunchforyou.appspot.com/ViewAllEvents";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(request_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                dtsStartList = new ArrayList<String>();
                namesList = new ArrayList<String>();
                buildingsList = new ArrayList<String>();
                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    JSONArray dts_start = jObject.getJSONArray("dts_start");
                    JSONArray names = jObject.getJSONArray("names");
                    JSONArray buildings = jObject.getJSONArray("buildings");
                    for (int i = 0; i < dts_start.length(); i++) {
                        dtsStartList.add(dts_start.getString(i));
                        namesList.add(names.getString(i));
                        buildingsList.add(buildings.getString(i));
                        System.out.println(dts_start.getString(i));
                    }

                    list = new ArrayList<HashMap<String, String>>();

                    SimpleAdapter adapter = new SimpleAdapter(
                            getActivity(),
                            list,
                            R.layout.crowview,
                            new String[] {"date", "time", "name", "building"},
                            new int[] {R.id.text1, R.id.text10, R.id.text2, R.id.text3}
                    );
                    populateList();

                    final ListView myList = (ListView) getActivity().findViewById(android.R.id.list);
                    myList.setAdapter(adapter);
                    myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {

                            Intent intent = new Intent(getActivity(), DisplayOneEvent.class);
                            String[] msg_out = new String[4];
                            msg_out[0] = getArguments().getString(ARG_PLANET_NUMBER);
                            Log.d("eeeeee3:",msg_out[0]);

                            msg_out[1] = namesList.get(position);
                            Log.d("eeeeee4:",msg_out[1]);
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
        return rootView;
    }

    private void populateList() {
        for (int i = 0; i < dtsStartList.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            DateTimeProcess dt = new DateTimeProcess(dtsStartList.get(i));
            map.put("date", dt.date());
            map.put("time", dt.hourMinute());
            map.put("name", namesList.get(i));
            map.put("building", buildingsList.get(i));
            list.add(map);
        }
    }

}
