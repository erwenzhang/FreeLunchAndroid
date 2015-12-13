package com.application.Kevin_Wenwen.FreeLunch;

/**
 * Created by wenwen on 12/12/15.
 */
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This shows how to create a simple activity with a map and a marker on the map.
 */
public class MapView extends AppCompatActivity implements OnMapReadyCallback,ActivityCompat.OnRequestPermissionsResultCallback {
    Context context = this;
    public final static String EXTRA_MESSAGE = "MESSAGE IN";
    private String email = null;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view);

        email = getIntent().getStringArrayExtra(EXTRA_MESSAGE)[0];

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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


            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we
     * just add a marker near Africa.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        final String request_url = "http://freelunchforyou.appspot.com/MapView";
        mMap = map;

        enableMyLocation();

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(request_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                final ArrayList<String> location = new ArrayList<String>();
                final ArrayList<String> building = new ArrayList<String>();
                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    JSONArray display_location = jObject.getJSONArray("display_location");
                    JSONArray display_building = jObject.getJSONArray("display_building");


                    // Log.d("wenwen TAG", "json successful");
                    for (int i = 0; i < display_building.length(); i++) {

                        building.add(display_building.getString(i));
                        location.add(display_location.getString(i));
                        String tmp_loc = display_location.getString(i);
                        String tmp_building = display_building.getString(i);
                        List<String> locList = Arrays.asList(tmp_loc.split(","));
                        mMap.addMarker(new MarkerOptions().position(new LatLng(Float.parseFloat(locList.get(0)), Float.parseFloat(locList.get(1)))).title(tmp_building));

                    }


                } catch (JSONException j) {
                    System.out.println("JSON Error");
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e("wq", "There was a problem in retrieving the url : " + e.toString());
            }
        });

    }
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


}

