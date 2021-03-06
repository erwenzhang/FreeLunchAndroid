package com.application.Kevin_Wenwen.FreeLunch;
import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by wenwen on 10/21/15.
 */
public class DisplayOneEvent extends AppCompatActivity {
    private String TAG  = "Display one event";
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    public final static String EXTRA_MESSAGE = "MESSAGE IN";
    // to mark which image has been displayed
    private int building = 0;
    private int pre_location = 0;

    //msg from DisplayImages
    private String[] msg;
    private String email = null;
    private String locationLat = null;
    private String locationLong = null;
    private String eventName;

    // private SignInButton mSignInButton;
    private TextView stream_name;

    private Button upload;
    private Button streams_button;
    private Button feedback ;
    private int morePageCount = 0;

    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_one_event);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
//        // this part is optional
//        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
//            // ...
//        });


        Intent intent = getIntent();
        msg = intent.getStringArrayExtra(EXTRA_MESSAGE);

        final String request_url = "http://freelunchforyou.appspot.com/ViewOneEvent";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        eventName = msg[1];
        email = msg[0];
     Log.d("email,:::",email);
        params.put("event_name", eventName);
        feedback = (Button)findViewById(R.id.feedback);

        httpClient.get(request_url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                final String dtStart;
                final String dtEnd;
                final String building;
                final String room;
                final String des;
                final String coverUrl;
                final String link;
                String rating;
                final String author_name;

                try {
                    System.out.println("I'm getting into try");
                    JSONObject jObject = new JSONObject(new String(response));

                    dtStart = jObject.getString("dt_start");
                    System.out.println("getting dt_start " + dtStart);

                    dtEnd = jObject.getString("dt_end");
                    building = jObject.getString("building");
                    room = jObject.getString("room");
                    des = jObject.getString("description");
                    coverUrl = jObject.getString("coverUrl");
                    System.out.println("getting coverurl " + coverUrl);
                    link = jObject.getString("linkage");
                    rating = jObject.getString("ratings");

                    author_name = jObject.getString("author_name");
                    System.out.println("getting author_name " + author_name);
                    Log.d("wenwenwen0,", link);
                    Log.d("wenwenwen6,", author_name);

                    if(author_name.equals(email)){
                        feedback.setVisibility(View.INVISIBLE);

                    }


                    // adapters
//                    URL url = new URL(coverUrl);
//                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    ImageView imgView = (ImageView) findViewById(R.id.imgview);
//                    imgView.setImageBitmap(bmp);
                    Picasso.with(context).load(coverUrl).into(imgView);
                    System.out.println("Setting this image: " + coverUrl);
//
                    if (rating == "null") {
                        rating = "3.5";
                    }
                    float ratingValue = Float.parseFloat(rating);
                    RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
                    ratingBar.setRating(ratingValue);

                    TextView t = (TextView) findViewById(R.id.dtStart);
                    System.out.println("Found layout id dtStart");
                    t.setText(dtStart);
                    System.out.println("wrote to dtStart");
                    t = (TextView) findViewById(R.id.event_name);
                    t.setText(eventName);

                    t = (TextView) findViewById(R.id.dtEnd);
                    t.setText(dtEnd);
                    t = (TextView) findViewById(R.id.building);
                    t.setText(building);
                    t = (TextView) findViewById(R.id.room);
                    t.setText(room);
                    t = (TextView) findViewById(R.id.description);
                    t.setText(des);
                    t = (TextView) findViewById(R.id.author);
                    String[] author_name_split = author_name.split("@");
                    t.setText(author_name_split[0]);
                    Log.d("wenwenwen3,", author_name_split[0]);
                    t = (TextView) findViewById(R.id.link);

                    if (link.equals("null")) {
                    t.setText(link);
                        Log.d("wenwenwen1,", link);

                  } else {


                      t.setText(
                               Html.fromHtml(
                                       "<a href=\"" + link + "\">" + eventName + "</a> "));
                       t.setMovementMethod(LinkMovementMethod.getInstance());

                    }



                    System.out.println("I'm getting the event's detail!!!");


//                    ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
//                    shareButton.setShareContent(content);
//
//                    if (ShareDialog.canShow(ShareLinkContent.class)) {
//                        ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                                .setContentTitle("Hello Facebook")
//                                .setContentDescription(
//                                        "The 'Hello Facebook' sample  showcases simple Facebook integration")
//                                .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
//                                .build();
//
//                        shareDialog.show(linkContent);
//                    }

                    Button b1=(Button)findViewById(R.id.share);

                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            Uri screenshotUri = Uri.parse(coverUrl);

                            try {
                                InputStream stream = getContentResolver().openInputStream(screenshotUri);
                            }

                            catch (FileNotFoundException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            sharingIntent.setType("image/jpeg");

                            String message = eventName + " starts at " + dtStart + " at " + building + " " + room + ". Please check the following link (if any)\n" + link + "\n";
//                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Free food event");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);

                            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                            startActivity(Intent.createChooser(sharingIntent, "Share image using"));
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


        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Feedback.class);
                String[] msg = new String[4];
                msg[0] = email;
                msg[1] = eventName;
                intent.putExtra(EXTRA_MESSAGE,msg);
                startActivity(intent);


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
                msg_out1[0] = email;
                intent2.putExtra(EXTRA_MESSAGE, msg_out1);
                // catch event that there's no activity to handle intent

                startActivity(intent2);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }


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
