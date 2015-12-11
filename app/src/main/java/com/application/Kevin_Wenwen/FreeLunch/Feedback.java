package com.application.Kevin_Wenwen.FreeLunch;

import android.content.DialogInterface;
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
public class Feedback extends ActionBarActivity implements View.OnClickListener {
    Context context = this;
    private String TAG  = "Display One Worker";
    private String email;
    private String[] msg;
    private TextView event_name;
    ImageButton yes;
    ImageButton no;
    String feedback;
    String author_name;
    public final static String EXTRA_MESSAGE = "MESSAGE IN";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        event_name = (TextView) findViewById(R.id.event_name);
        yes = (ImageButton)findViewById(R.id.yes);
        no = (ImageButton)findViewById(R.id.no);
        yes.setEnabled(true);
        no.setEnabled(true);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);


        Intent intent = getIntent();
        msg = intent.getStringArrayExtra(EXTRA_MESSAGE);
        author_name = msg[0];
        event_name.setText("Is "+ msg[1]+" real?");

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
        params.put("author_name",author_name);
        httpClient.get(request_url, new AsyncHttpResponseHandler() {
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

}