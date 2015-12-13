package com.application.Kevin_Wenwen.FreeLunch;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wenwen on 10/21/15.
 */
public class DisplayOneWorker extends AppCompatActivity {

    static ArrayList<HashMap<String,String>> list;
    private String[] msg;
    private String workerName;
    private String delEventName;

    private String TAG  = "Display one event";
    public final static String EXTRA_MESSAGE = "MESSAGE IN";
    // to mark which image has been displayed
    // private int building = 0;
    // private int pre_location = 0;

    // //msg from DisplayImages
    // private String[] msg;
    // private String email = null;
    // private String locationLat = null;
    // private String locationLong = null;
    // private String workerName;

    // // private SignInButton mSignInButton;
    // private TextView stream_name;

    // private Button upload;
    // private Button streams_button;
    // private Button more_pictures ;
    // private int morePageCount = 0;

    ArrayList<String> namesList = null;
    ArrayList<String> dtsStartList = null;
    ArrayList<String> buildingsList = null;

    Context context = this;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_one_worker);

        Intent intent = getIntent();
        msg = intent.getStringArrayExtra(EXTRA_MESSAGE);

        final String request_url = "http://freelunch-test1.appspot.com/ViewOneWorker";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        workerName = msg[1];
        delEventName = msg[2];

        params.put("worker_name", workerName);
        if (delEventName != null) {
            params.put("delete_item", delEventName);
        }

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
                    //        msg_out[0] = email;
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
        Intent intent = new Intent(context, DisplayOneWorker.class);
        String[] msg_out = new String[4];
        // msg_out[0] = email;
        msg_out[1] = workerName;
        msg_out[2] = ((TextView) parent.findViewById(R.id.text2)).getText().toString();
        System.out.println(msg_out[1] + "'s event to delete: " + msg_out[2]);
        intent.putExtra(EXTRA_MESSAGE, msg_out);
        startActivity(intent);
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


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.display_mysubscribe, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


//    public  void imageUpload(View view){
//        Intent intent = new Intent(this,ImageUpload.class);
//
//        intent.putExtra(EXTRA_MESSAGE,msg);
//
//        startActivity(intent);
//    }
//
//    public void viewAllImages(View view){
//        Intent intent= new Intent(this, DisplayImages.class);
//        //if(Homepage.login)
//        String[] msg_out = new String[3];
//        msg_out[0] = email;
//        intent.putExtra(EXTRA_MESSAGE,msg_out);
//        startActivity(intent);
//    }

}
