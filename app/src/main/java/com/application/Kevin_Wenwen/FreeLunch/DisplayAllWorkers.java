package com.application.Kevin_Wenwen.FreeLunch;
import android.app.ListActivity;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
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
import java.util.HashMap;


public class DisplayAllWorkers extends ListActivity {

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
        setContentView(R.layout.clistview_workers);

        Log.d("WENWENresulte ","back1");

        Intent intent = getIntent();
        intent.getClass();
        // String source = intent.getStringExtra("From");
        msg = intent.getStringArrayExtra(EXTRA_MESSAGE);

//        email = msg[0];

        final String request_url = "http://freelunch-test1.appspot.com/ViewAllWorkers";
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
                            R.layout.crowview_workers,
                            new String[] {"name", "rating"},
                            new int[] {R.id.text2, R.id.text3}
                    );
                    populateList();
                    setListAdapter(adapter);

                    // to change to table view
//                    GridView gridview = (GridView) findViewById(R.id.gridview);
//                    gridview.setAdapter(new ImageAdapter(context, dtsStartList, ratingsList));
//                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View v,
//                                                int position, long id) {
//
//                            Intent intent = new Intent(context, DisplayOneEvent.class);
//                            String[] msg_out = new String[4];
//                            msg_out[0] = email;
//                            msg_out[1] = namesList.get(position);
//                            intent.putExtra(EXTRA_MESSAGE, msg_out);
//                            startActivity(intent);
//                        }
//                    });
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
            map.put("name", namesList.get(i));
            map.put("rating", ratingsList.get(i));
            list.add(map);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
//		TextView t = (TextView) v.findViewById(R.id.text2);
//		t.setText("Tweet Clicked");
        Intent intent = new Intent(context, DisplayOneWorker.class);
        String[] msg_out = new String[4];
//        msg_out[0] = email;
        msg_out[1] = namesList.get(position);
        intent.putExtra(EXTRA_MESSAGE, msg_out);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_images, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startSearch(View v){
        Intent intent = new Intent(this,Search.class);
        EditText text = (EditText) findViewById(R.id.search_bar);
        String search_item = text.getText().toString();
        String[] msg_out = new String[4];
        msg_out[0] = email;
        msg_out[1] = search_item;
        intent.putExtra(EXTRA_MESSAGE,msg_out);
        startActivity(intent);
    }


}