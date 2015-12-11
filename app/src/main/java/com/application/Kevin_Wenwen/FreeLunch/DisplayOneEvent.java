package com.application.Kevin_Wenwen.FreeLunch;
import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
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

/**
 * Created by wenwen on 10/21/15.
 */
public class DisplayOneEvent extends ActionBarActivity {
    private String TAG  = "Display Subscribes";
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
    private Button more_pictures ;
    private int morePageCount = 0;

    Context context = this;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_one_event);


        final String request_url = "http://freelunch-test1.appspot.com/ViewOneEvent";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("event_name", eventName);

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
                final String rating;
                final String author_name;

                try {
                    JSONObject jObject = new JSONObject(new String(response));

                    dtStart = jObject.getString("dt_start");
                    dtEnd = jObject.getString("dt_end");
                    building = jObject.getString("building");
                    room = jObject.getString("room");
                    des = jObject.getString("description");
                    coverUrl = jObject.getString("coverUrl");
                    link = jObject.getString("linkage");
                    rating = jObject.getString("ratings");
                    author_name = jObject.getString("author_name");

//                    if(email!=null) {
//                        if (email.split("@",2)[0].equals(stream_author.toLowerCase()) ){
//                        upload.setEnabled(true);
//                     //   Log.d("Upload ability","OK!!");
//                        } else {
//                            upload.setEnabled(false);
//                            //   Log.d("Upload ability", "not OK!!");
//                        }
//                    } else {
//                        upload.setEnabled(false);
//                     //   Log.d("Upload ability", "not OK!!");
//                    }
//
                    // adapters
                    URL url = new URL(coverUrl);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    ImageView imgView = (ImageView) findViewById(R.id.imgview);
                    imgView.setImageBitmap(bmp);

                    float ratingValue = Float.parseFloat(rating);
                    RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
                    ratingBar.setRating(ratingValue);

                    TextView t = (TextView) findViewById(R.id.dtStart);
                    t.setText(dtStart);
                    t = (TextView) findViewById(R.id.dtEnd);
                    t.setText(dtEnd);
                    t = (TextView) findViewById(R.id.building);
                    t.setText(building);
                    t = (TextView) findViewById(R.id.room);
                    t.setText(room);
                    t = (TextView) findViewById(R.id.description);
                    t.setText(des);
                    t = (TextView) findViewById(R.id.link);
                    t.setText(link);
                    t = (TextView) findViewById(R.id.author);
                    t.setText("HeHe!!!");


//                    GridView gridview = (GridView) findViewById(R.id.gridview);
//                    //              if (imageURLs.size()>0){
//                    gridview.setAdapter(new ImageAdapter(context, imageURLs,captionList));
//                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View v,
//                                                int position, long id) {
//
//                            Toast.makeText(context, captionList.get(position), Toast.LENGTH_SHORT).show();
//
//                            // Intent intent= new Intent(this,viewSingleStream.class,streamURLs.get(position), );
//                            //   startActivity(intent);
//                            Dialog imageDialog = new Dialog(context);
//                            imageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                            imageDialog.setContentView(R.layout.thumbnail);
//                            ImageView image = (ImageView) imageDialog.findViewById(R.id.thumbnail_IMAGEVIEW);
//
//                            Picasso.with(context).load(imageURLs.get(position)).into(image);
//
//                            imageDialog.show();
//                        }
//                    });

                } catch (Exception e) {
                    System.out.println("Exception");
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e(TAG, "There was a problem in retrieving the url : " + e.toString());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_mysubscribe, menu);
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


    public  void imageUpload(View view){
        Intent intent = new Intent(this,ImageUpload.class);

        intent.putExtra(EXTRA_MESSAGE,msg);

        startActivity(intent);
    }

    public void viewAllImages(View view){
        Intent intent= new Intent(this, DisplayImages.class);
        //if(Homepage.login)
        String[] msg_out = new String[3];
        msg_out[0] = email;
        intent.putExtra(EXTRA_MESSAGE,msg_out);
        startActivity(intent);
    }

}
