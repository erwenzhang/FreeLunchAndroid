package com.application.Kevin_Wenwen.FreeLunch;

/**
 * Created by wenwen on 12/9/15.
 */

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.ListActivity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.os.Bundle;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;


import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import com.google.android.gms.location.LocationListener;
import  com.google.android.gms.location.LocationServices;

//import com.google.android.gms.location.LocationClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.app.PendingIntent;
import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FreeLunchList extends  AppCompatActivity  implements OnMapReadyCallback,DatePickerDialog.OnDateSetListener {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    private String email = null;
    Context context = this;
    public final static String EXTRA_MESSAGE = "MESSAGE IN";


    private String day;
    private String month;
    private String year;
    JSONArray display_name;
    JSONArray display_date;

    private CaldroidFragment caldroidFragment;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//         getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
         setContentView(R.layout.events);
       // setContentView(R.layout.events);
        email = getIntent().getStringArrayExtra(EXTRA_MESSAGE)[0];


        mTitle = mDrawerTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, mPlanetTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
         getCalendarData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.addEvent).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
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
                finish();
                return true;
//        	    Intent intent_2 = new Intent(this,Homepage.class);
//                String[] msg_out = new String[4];
//                msg_out[0] = "hi wenwen";
//                intent_2.putExtra(EXTRA_MESSAGE,msg_out);
//        	    startActivityintent_2);
            case R.id.myEvent:
                Intent intent1 = new Intent(context,DisplayOneWorker.class);
                String[] msg_out1 = new String[4];
                msg_out1[1] = email;
                intent1.putExtra(EXTRA_MESSAGE, msg_out1);
                // catch event that there's no activity to handle intent

                startActivity(intent1);
                return true;



        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        if(position == 2){
           // super.onCreate(savedInstanceState);
        //    setContentView(R.layout.mcalendar);

            final SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd");

            // Setup caldroid fragment
            // **** If you want normal CaldroidFragment, use below line ****
            caldroidFragment = new CaldroidFragment();
                Bundle args = new Bundle();
                Calendar cal = Calendar.getInstance();
                args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
                args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
                args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
                args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);


                caldroidFragment.setArguments(args);

            setCustomResourceForDates();

            // Attach to the activity
            FragmentTransaction t = getSupportFragmentManager().beginTransaction();
            t.replace(R.id.content_frame, caldroidFragment);
            t.commit();



            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(mPlanetTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

            // Setup listener
            final CaldroidListener listener = new CaldroidListener() {

                @Override
                public void onSelectDate(Date date, View view) {
                    Toast.makeText(getApplicationContext(), formatter.format(date),
                            Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onChangeMonth(int month, int year) {
                    String text = "month: " + month + " year: " + year;
                    Toast.makeText(getApplicationContext(), text,
                            Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onLongClickDate(Date date, View view) {
                    Toast.makeText(getApplicationContext(),
                            "Long click " + formatter.format(date),
                            Toast.LENGTH_SHORT).show();

                    displayOnedayEvents(date);
                }


//                public void onCaldroidViewCreated() {
//                    if (caldroidFragment.getLeftArrowButton() != null) {
//                        Toast.makeText(getApplicationContext(),
//                                "Caldroid view is created", Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                }

            };

            // Setup Caldroid
            caldroidFragment.setCaldroidListener(listener);


        }
        else if(position == 1){
            Log.d("11111 new "," map");
//            SupportMapFragment mapFragment =
//                    (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
//            mapFragment.getMapAsync(this);
            /**
             * for date filter at map view
             */
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    FreeLunchList.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.show(getFragmentManager(), "Datepickerdialog");



        }
      else  {
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }


    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */

    /**
     * for calender view
     */
    private void setCustomResourceForDates(){
        getCalendarData();
        final SimpleDateFormat mformatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
        for (int i = 0; i < display_date.length(); i++) {
            String tmp_str = display_date.getString(i).substring(0, 10);
            Log.d("time1  ", tmp_str);
            try {
                Date tmp_date = mformatter.parse(tmp_str);
                caldroidFragment.setBackgroundResourceForDate(R.color.light_blue_900,
                        tmp_date);
                caldroidFragment.setTextColorForDate(R.color.white, tmp_date);

            } catch (ParseException e) {
                Log.e("wenwenwen ", " parse exception");

            }


        }
        } catch (JSONException j) {
            System.out.println("JSON Error");
        }
    }

    private void getCalendarData() {

        final String request_url = "http://freelunchforyou.appspot.com/CalendarView";
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(request_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                final ArrayList<String> event_date = new ArrayList<String>();
                final ArrayList<String> event_name = new ArrayList<String>();
                final ArrayList<Date> event_date_formal = new ArrayList<Date>();

                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    display_date = jObject.getJSONArray("display_date");
                    display_name = jObject.getJSONArray("display_name");
                    // Log.d("wenwen TAG", "json successful");



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

    public void onDateSet(DatePickerDialog view, int theyear, int monthOfYear, int dayOfMonth) {
        day = Integer.toString(dayOfMonth);
        month = Integer.toString(monthOfYear+1);
        year = Integer.toString(theyear);

        Intent intent = new Intent(context,MapView.class);
        String[] msg = new String[4];


        msg[0] = email;
        msg[1] = year;
        msg[2] = month;
        msg[3] = day;

        intent.putExtra(EXTRA_MESSAGE, msg);
        //finish();
        startActivity(intent);

        String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+theyear;
       // dateTextView.setText(date);
    }
    @Override
    public void onResume() {
        super.onResume();

        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");


        if(dpd != null) dpd.setOnDateSetListener(this);
    }
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void displayOnedayEvents(Date date){
        Intent intent = new Intent(context,DisplayOnedayEvent.class);
        String[] msg = new String[3];

        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//        Log.d("markermarker","eee");
//        System.out.print(date);

        msg[0]= df.format(date);
//        Log.d("markermarker",msg[0]);

        msg[1] = email;
        intent.putExtra(EXTRA_MESSAGE,msg);
        startActivity(intent);





    }

}
