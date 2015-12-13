package com.application.Kevin_Wenwen.FreeLunch;

import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.view.WindowManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;





/**
 * Created by wenwen on 12/9/15.
 */
public class Feedback extends AppCompatActivity implements View.OnClickListener {
    Context context = this;
    private String TAG  = "Display One Worker";
    private String event_name;
    private String email;
    private String[] msg;
    private TextView event;
    ImageButton yes;
    ImageButton no;
    String feedback;
    String author_name;
    public final static String EXTRA_MESSAGE = "MESSAGE IN";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        event = (TextView) findViewById(R.id.event_name);
        yes = (ImageButton)findViewById(R.id.yes);
        no = (ImageButton)findViewById(R.id.no);
        yes.setEnabled(true);
        no.setEnabled(true);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);


        Intent intent = getIntent();
        msg = intent.getStringArrayExtra(EXTRA_MESSAGE);
        event_name = msg[1];
        event.setText("Is "+ msg[1]+" real?");

        setTitle("Feedback");






    }
    public void onClick(View v) {
        yes.setEnabled(false);
        no.setEnabled(false);

        switch (v.getId()) {
            case R.id.yes:
                feedback = "yes";
                break;
            case R.id.no:
               feedback = "no";
                break;
        }
        final String request_url = "http://freelunchforyou.appspot.com/GiveFeedback";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("feedback",feedback);
        params.put("event_name",event_name);
        httpClient.get(request_url, params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.i("LOAD WORKERS ", "Successfully give feedbacks");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e("LOAD WORKERS ", "There was a problem in retrieving the url : " + e.toString());
            }
        });





    }

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
                msg_out[0] = email;
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
                msg_out1[1] = email;
                intent2.putExtra(EXTRA_MESSAGE, msg_out1);
                // catch event that there's no activity to handle intent

                startActivity(intent2);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
