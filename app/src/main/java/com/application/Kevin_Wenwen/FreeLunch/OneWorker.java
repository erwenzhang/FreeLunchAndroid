package com.application.Kevin_Wenwen.FreeLunch;

import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.view.WindowManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;





/**
 * Created by wenwen on 12/9/15.
 */
public class OneWorker extends ActionBarActivity {
    Context context = this;
    private String TAG  = "Display One Worker";
    private String email;
    private String[] msg;
    public final static String EXTRA_MESSAGE = "MESSAGE IN";
    public final static String EXTRA_MESSAGE1 = "com.displayimages.MESSAGE";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_worker);

        final String request_url = "http://freelunchforyou.appspot.com/ViewOneWorker";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(request_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                final ArrayList<String> events_name = new ArrayList<String>();
                final ArrayList<String> events_date = new ArrayList<String>();
                final ArrayList<String> events_loc = new ArrayList<String>();
                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    JSONArray displayNames = jObject.getJSONArray("events_name");
                    JSONArray displayDates = jObject.getJSONArray("events_date");
                    JSONArray displayLocs = jObject.getJSONArray("events_loc");
                    // Log.d("wenwen TAG", "json successful");
                    for (int i = 0; i < displayNames.length(); i++) {

                        events_name.add(displayNames.getString(i));
                        events_date.add(displayDates.getString(i));
                        events_loc.add(displayLocs.getString(i));
                        System.out.println(displayNames.getString(i));
                    }

                    ListView listview = (ListView)findViewById(R.id.listView);
                    listview.setAdapter(new mAdapter(context,events_name));
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {
                            Intent intent= new Intent(v.getContext(),OneEvent.class);
                            String[] msg_out = new String[4];
                            msg_out[0] = events_name.get(position);
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
                Log.e("LOAD WORKERS", "There was a problem in retrieving the url : " + e.toString());
            }
        });

    }
}
