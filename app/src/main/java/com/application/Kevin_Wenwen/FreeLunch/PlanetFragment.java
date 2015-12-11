package com.application.Kevin_Wenwen.FreeLunch;

import android.app.Fragment;
import android.content.DialogInterface;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.view.WindowManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
/**
 * Created by wenwen on 12/9/15.
 */
public class PlanetFragment extends Fragment {
    public static final String ARG_PLANET_NUMBER = "planet_number";
    public final static String EXTRA_MESSAGE = "MESSAGE IN";

    public PlanetFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        int i = getArguments().getInt(ARG_PLANET_NUMBER);
        switch (i){
            case 0:
                Log.d("1111"," events");
                rootView = getView();
                break;

            case 1:
                Log.d("2222"," map");
                rootView = inflater.inflate(R.layout.map, container, false);
                rootView = load_map(rootView);
                break;


            case 2:
                Log.d("333","calendar");
                rootView = inflater.inflate(R.layout.calendar, container, false);
                rootView = load_calendar(rootView);
                break;

            default:
                Log.d("444"," crowdworker");
                rootView = inflater.inflate(R.layout.crowdworkers, container, false);
                rootView = load_woker(rootView);
        }
        String planet = getResources().getStringArray(R.array.planets_array)[i];

  //    int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),"drawable", getActivity().getPackageName());
  //      ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
  //      getActivity().setTitle(planet);
        return rootView;
    }

    public View load_map(View rootView){
        final String request_url = "http://freelunchforyou.appspot.com/MapView";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(request_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                final ArrayList<String> coverURLs = new ArrayList<String>();
                final ArrayList<String> streams = new ArrayList<String>();
                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    JSONArray displayCovers = jObject.getJSONArray("displayCovers");
                    JSONArray streamList = jObject.getJSONArray("streamList");
                    // Log.d("wenwen TAG", "json successful");
                    for(int i=0;i<displayCovers.length();i++) {

                        coverURLs.add(displayCovers.getString(i));
                        streams.add(streamList.getString(i));
                        System.out.println(displayCovers.getString(i));
                    }


                }
                catch(JSONException j){
                    System.out.println("JSON Error");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e("wq", "There was a problem in retrieving the url : " + e.toString());
            }
        });
        return rootView;
    }
    public View load_calendar(View rootView){
        final String request_url = "http://freelunchforyou.appspot.com/CalendarView";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(request_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                final ArrayList<String> coverURLs = new ArrayList<String>();
                final ArrayList<String> streams = new ArrayList<String>();
                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    JSONArray displayCovers = jObject.getJSONArray("displayCovers");
                    JSONArray streamList = jObject.getJSONArray("streamList");
                    // Log.d("wenwen TAG", "json successful");
                    for(int i=0;i<displayCovers.length();i++) {

                        coverURLs.add(displayCovers.getString(i));
                        streams.add(streamList.getString(i));
                        System.out.println(displayCovers.getString(i));
                    }


                }
                catch(JSONException j){
                    System.out.println("JSON Error");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e("wq", "There was a problem in retrieving the url : " + e.toString());
            }
        });
        return rootView;

    }

    public View load_woker(final View rootView){
        final String request_url = "http://freelunchforyou.appspot.com/ViewAllWorkers";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(request_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                final ArrayList<String> names = new ArrayList<String>();
                final ArrayList<String> ratings = new ArrayList<String>();
                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    JSONArray displayNames = jObject.getJSONArray("names");
                    JSONArray displayRatings = jObject.getJSONArray("ratings");
                    // Log.d("wenwen TAG", "json successful");
                    for(int i=0;i<displayNames.length();i++) {

                        names.add(displayNames.getString(i));
                        ratings.add(displayRatings.getString(i));
                        System.out.println(displayNames.getString(i));
                    }
                    ListView listview = (ListView)getActivity().findViewById(R.id.listView);
                    listview.setAdapter(new mAdapter(rootView.getContext(),names));
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            Intent intent= new Intent(v.getContext(),OneWorker.class);
                            String[] msg_out = new String[4];
                            msg_out[0] = names.get(position);
                            intent.putExtra(EXTRA_MESSAGE, msg_out);
                            getActivity().startActivity(intent);

                        }
                    });



                }
                catch(JSONException j){
                    System.out.println("JSON Error");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e("LOAD WORKERS", "There was a problem in retrieving the url : " + e.toString());
            }
        });
        return rootView;

    }
}
