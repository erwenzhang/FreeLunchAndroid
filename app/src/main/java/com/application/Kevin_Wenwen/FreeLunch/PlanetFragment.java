package com.application.Kevin_Wenwen.FreeLunch;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wenwen on 12/9/15.
 */
public class PlanetFragment extends DialogFragment {
    public static final String ARG_PLANET_NUMBER = "planet_number";
    public final static String EXTRA_MESSAGE = "MESSAGE IN";

    ArrayList<String> allworkers_namesList = null;
    ArrayList<String> allworkers_ratingsList = null;
    static ArrayList<HashMap<String,String>> allworkers_list;

    public PlanetFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;

        rootView = inflater.inflate(R.layout.list_all_workers,container,false);

        final String request_url = "http://freelunchforyou.appspot.com/ViewAllWorkers";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(request_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                allworkers_namesList = new ArrayList<String>();
                allworkers_ratingsList = new ArrayList<String>();
                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    JSONArray names = jObject.getJSONArray("names");
                    JSONArray ratings = jObject.getJSONArray("ratings");
                    for (int i = 0; i < names.length(); i++) {
                        allworkers_namesList.add(names.getString(i));
                        allworkers_ratingsList.add(ratings.getString(i));
                    }

                    allworkers_list = new ArrayList<HashMap<String,String>>();

                    SimpleAdapter adapter = new SimpleAdapter(
                            getActivity(),
                            allworkers_list,
                            R.layout.list_all_workers_item,
                            new String[]{"name", "rating"},
                            new int[]{R.id.text1, R.id.text2}
                    );
                    allworkers_populateList();
//                    setListAdapter(adapter);
                    final ListView myList = (ListView) getActivity().findViewById(android.R.id.list);
                    myList.setAdapter(adapter);
                    myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position,
                                                long id) {

                            Intent intent = new Intent(getActivity(), DisplayOneWorker.class);
                            String[] msg_out = new String[4];

                            msg_out[0] = getArguments().getString(ARG_PLANET_NUMBER);
                            Log.d("eeeeee1:",msg_out[0]);
                            msg_out[1] = allworkers_namesList.get(position);
                            Log.d("eeeeee2:",msg_out[1]);
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
                Log.e("display all workers ", "There was a problem in retrieving the url : " + e.toString());
            }
        });
        return rootView;
    }





    private void allworkers_populateList() {
        for (int i = 0; i < allworkers_namesList.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", allworkers_namesList.get(i));
            List<String> elephantList = Arrays.asList(allworkers_namesList.get(i).split("@"));
            //Log.d("now   ",elephantList.get(0));
            map.put("name", elephantList.get(0));
            map.put("rating", allworkers_ratingsList.get(i).substring(0,3));
            allworkers_list.add(map);
        }
    }


  //    int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),"drawable", getActivity().getPackageName());
 //     ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
  //      getActivity().setTitle(planet);

    }

//    public View load_map(View rootView){
//        final String request_url = "http://freelunchforyou.appspot.com/MapView";
//        AsyncHttpClient httpClient = new AsyncHttpClient();
//        httpClient.get(request_url, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
//                final ArrayList<String> coverURLs = new ArrayList<String>();
//                final ArrayList<String> streams = new ArrayList<String>();
//                try {
//                    JSONObject jObject = new JSONObject(new String(response));
//                    JSONArray displayCovers = jObject.getJSONArray("displayCovers");
//                    JSONArray streamList = jObject.getJSONArray("streamList");
//                    // Log.d("wenwen TAG", "json successful");
//                    for(int i=0;i<displayCovers.length();i++) {
//
//                        coverURLs.add(displayCovers.getString(i));
//                        streams.add(streamList.getString(i));
//                        System.out.println(displayCovers.getString(i));
//                    }
//
//
//                }
//                catch(JSONException j){
//                    System.out.println("JSON Error");
//                }
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
//                Log.e("wq", "There was a problem in retrieving the url : " + e.toString());
//            }
//        });
//        return rootView;
//    }
//    public View load_calendar(View rootView){
//        final String request_url = "http://freelunchforyou.appspot.com/CalendarView";
//        AsyncHttpClient httpClient = new AsyncHttpClient();
//        httpClient.get(request_url, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
//                final ArrayList<String> coverURLs = new ArrayList<String>();
//                final ArrayList<String> streams = new ArrayList<String>();
//                try {
//                    JSONObject jObject = new JSONObject(new String(response));
//                    JSONArray displayCovers = jObject.getJSONArray("displayCovers");
//                    JSONArray streamList = jObject.getJSONArray("streamList");
//                    // Log.d("wenwen TAG", "json successful");
//                    for(int i=0;i<displayCovers.length();i++) {
//
//                        coverURLs.add(displayCovers.getString(i));
//                        streams.add(streamList.getString(i));
//                        System.out.println(displayCovers.getString(i));
//                    }
//
//
//                }
//                catch(JSONException j){
//                    System.out.println("JSON Error");
//                }
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
//                Log.e("wq", "There was a problem in retrieving the url : " + e.toString());
//            }
//        });
//        return rootView;
//
//    }




