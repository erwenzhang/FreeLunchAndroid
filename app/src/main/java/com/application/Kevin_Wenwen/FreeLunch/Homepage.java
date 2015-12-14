package com.application.Kevin_Wenwen.FreeLunch;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.view.View;


import com.google.android.gms.location.LocationListener;
import  com.google.android.gms.location.LocationServices;

//import com.google.android.gms.location.LocationClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import android.app.Activity;
import android.content.Intent;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentSender.SendIntentException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Button;

public class Homepage extends Activity implements
        ConnectionCallbacks, OnConnectionFailedListener,View.OnClickListener,
        LocationListener{

    private static final String TAG = "FreeLunch-login";
    //final int RQS_GooglePlayServices = 1;
    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;

    private static final int RC_SIGN_IN = 0;

    private static final String SAVED_PROGRESS = "sign_in_progress";

    // GoogleApiClient wraps our service connection to Google Play services and
    // provides access to the users sign in state and Google's APIs.
    private GoogleApiClient mGoogleApiClient;

    // We use mSignInProgress to track whether user has clicked sign in.
    // mSignInProgress can be one of three values:
    //
    //       STATE_DEFAULT: The default state of the application before the user
    //                      has clicked 'sign in', or after they have clicked
    //                      'sign out'.  In this state we will not attempt to
    //                      resolve sign in errors and so will display our
    //                      Activity in a signed out state.
    //       STATE_SIGN_IN: This state indicates that the user has clicked 'sign
    //                      in', so resolve successive errors preventing sign in
    //                      until the user has successfully authorized an account
    //                      for our app.
    //   STATE_IN_PROGRESS: This state indicates that we have started an intent to
    //                      resolve an error, and so we should not start further
    //                      intents until the current intent completes.
    private int mSignInProgress;

    // Used to store the PendingIntent most recently returned by Google Play
    // services until the user clicks 'sign in'.
    private PendingIntent mSignInIntent;

    // Used to store the error code most recently returned by Google Play services
    // until the user clicks 'sign in'.
    private int mSignInError;
    public static boolean login = false;
    public final static String EXTRA_MESSAGE = "MESSAGE IN";

    // Used to determine if we should ask for a server auth code when connecting the
    // GoogleApiClient.  False by default so that this sample can be used without configuring
    // a WEB_CLIENT_ID and SERVER_BASE_URL.

    // Used to mock the state of a server that would receive an auth code to exchange
    // for a refresh token,  If true, the client will assume that the server has the
    // permissions it wants and will not send an auth code on sign in.  If false,
    // the client will request offline access on sign in and send and new auth code
    // to the server.  True by default because this sample does not implement a server
    // so there would be nowhere to send the code.

    private SignInButton mSignInButton;


    //location variable
    private String mLatitudeText = "0";
    private String mLongitudeText = "0";
    //private LocationClient mLocationClient;
    //private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private String[] msg;
    Context context = this;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);


        setGooglePlusButtonText(mSignInButton, "Sign in        ");

        // Button listeners
        mSignInButton.setOnClickListener(this);


        if (savedInstanceState != null) {
            mSignInProgress = savedInstanceState
                    .getInt(SAVED_PROGRESS, STATE_DEFAULT);

        }

        mGoogleApiClient = buildGoogleApiClient();

      /*  if (servicesConnected()) {
            mLocationClient = new LocationClient(this,this,this);

        }*/

    }
    
   
  

    public void onLocationChanged(Location location) {
        Log.d("Location wenwen", location.toString());
        //System.out.print();
        mLongitudeText = String.valueOf(location.getLongitude());
        mLatitudeText = String.valueOf(location.getLatitude());

    }

    private void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                tv.setTextSize(16);
                return;
            }
        }
    }

    private GoogleApiClient buildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and
        // connection failed callbacks should be returned, which Google APIs our
        // app uses and which OAuth 2.0 scopes our app requests.
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addApi(LocationServices.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
    //    mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();

//      if (mGoogleApiClient.isConnected()) {
//          Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//           mGoogleApiClient.disconnect();
//        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, mSignInProgress);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void onClick(View v) {
        if (!mGoogleApiClient.isConnecting()) {
            // We only process button clicks when GoogleApiClient is not transitioning
            // between connected and not connected.
            if (!isOnline()) {
                Toast.makeText(getApplicationContext(),
                        "You need internet access to perform this action.", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d(TAG, "click!!!");
            //System.out.println("!!!!");
            switch (v.getId()) {
                case R.id.sign_in_button:
                   resolveSignInError();
                    break;
//                case R.id.sign_out_button:
//                    // We clear the default account on sign out so that Google Play
//                    // services will not return an onConnected callback without user
//                    // interaction.
//                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//                    mGoogleApiClient.disconnect();
//                    mGoogleApiClient.connect();
//                    //Homepage.login = true;
//                    Log.i(TAG, "2222");
//                    email = null;
//                    Toast.makeText(getApplicationContext(), "You are now signed out", Toast.LENGTH_SHORT).show();
//                    login_msg_shown = false;
//                    break;
//                case R.id.revoke_access_button:
//                    // After we revoke permissions for the user with a GoogleApiClient
//                    // instance, we must discard it and create a new one.
//                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//                    // Our sample has caches no user data from Google+, however we
//                    // would normally register a callback on revokeAccessAndDisconnect
//                    // to delete user data so that we comply with Google developer
//                    // policies.
//                    Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
//                    mGoogleApiClient = buildGoogleApiClient();
//                    mGoogleApiClient.connect();
////                    Homepage.login = true;
////                    Log.i(TAG, "33333");
//                    email = null;
//                    Toast.makeText(getApplicationContext(), "You've just revoked Connexus to access your basic account info.", Toast.LENGTH_LONG).show();
//                    login_msg_shown = false;
//                    break;
            }
        }
    }

    ImageView imageView = null;
    private static boolean login_msg_shown = false;
    public static String email = null;

    /* onConnected is called when our Activity successfully connects to Google
     * Play services.  onConnected indicates that an account was selected on the
     * device, that the selected account has granted any requested permissions to
     * our app and that we were able to establish a service connection to Google
     * Play services.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Reaching onConnected means we consider the user signed in.
        Log.i(TAG, "onConnected 1");


        // Update the user interface to reflect that the user is signed in.


        //location_api
     //   mLocationRequest = LocationRequest.create();
   //     mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
       // mLocationRequest.setInterval(1000);
      //  LocationServices.FusedLocationApi.requestLocationUpdates(
        //        mGoogleApiClient, mLocationRequest, this);


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
              mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText = String.valueOf(mLastLocation.getLatitude());
            mLongitudeText = String.valueOf(mLastLocation.getLongitude());

        }


        // Retrieve some profile information to personalize our app for the user.
        final Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
        email = Plus.AccountApi.getAccountName(mGoogleApiClient);
        System.out.println(email);
        // Indicate that the sign in process is complete.
        mSignInProgress = STATE_DEFAULT;





     //   msg[1] = mLatitudeText;
     //   msg[2] = mLongitudeText;
//        System.out.println(Homepage.login);
//        if(Homepage.login) {
            Intent intent= new Intent(this, FreeLunchList.class);

            //if(Homepage.login)
            String[] msg = new String[3];
            //  System.out.print(Homepage.email);
            //System.out.print(mLatitudeText);
            //System.out.print(mLongitudeText);

            msg[0] = Homepage.email;
            intent.putExtra(EXTRA_MESSAGE, msg);

        startActivityForResult(intent,STATE_SIGN_IN);
       // }

    }


    /* onConnectionFailed is called when our Activity could not connect to Google
     * Play services.  onConnectionFailed indicates that the user needs to select
     * an account, grant permissions or resolve an error in order to sign in.
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might
        // be returned in onConnectionFailed.
       // int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
 //       Log.i("WENWEN0", "IS Available "
   //             + GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()));
        Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
       // GooglePlayServicesUtil.getErrorDialog(resultCode,this,RQS_GooglePlayServices ).show();

        if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            Log.d("WENWEN", "HERE");
            // An API requested for GoogleApiClient is not available. The device's current
            // configuration might not be supported with the requested API or a required component
            // may not be installed, such as the Android Wear application. You may need to use a
            // second GoogleApiClient to manage the application's optional APIs.
        } else if (mSignInProgress != STATE_IN_PROGRESS) {
            Log.d("WENWEN", "HERE1");
            // We do not have an intent in progress so we should store the latest
            // error resolution intent for use when the sign in button is clicked.
            mSignInIntent = result.getResolution();
            mSignInError = result.getErrorCode();

            if (mSignInProgress == STATE_SIGN_IN) {
                // STATE_SIGN_IN indicates the user already clicked the sign in button
                // so we should continue processing errors until the user is signed in
                // or they click cancel.
                resolveSignInError();
                System.out.print(STATE_SIGN_IN);
                Log.d("WENWEN1", "111");
                System.out.print(mSignInProgress);

            }
        }
        Log.d("WENWEN", "HERE3");
        // In this sample we consider the user signed out whenever they do not have
        // a connection to Google Play services.
        onSignedOut();
    }

    /* Starts an appropriate intent or dialog for user interaction to resolve
     * the current error preventing the user from being signed in.  This could
     * be a dialog allowing the user to select an account, an activity allowing
     * the user to consent to the permissions being requested by your app, a
     * setting to enable device networking, etc.
     */
    private void resolveSignInError() {
        if (mSignInIntent != null) {
            // We have an intent which will allow our user to sign in or
            // resolve an error.  For example if the user needs to
            // select an account to sign in with, or if they need to consent
            // to the permissions your app is requesting.
            // Log.d("WENWEN1","2" );
            try {
                // Send the pending intent that we stored on the most recent
                // OnConnectionFailed callback.  This will allow the user to
                // resolve the error currently preventing our connection to
                // Google Play services.
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
                mGoogleApiClient.connect();
                Log.d("WENWEN1", "1");
            } catch (SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());
                // The intent was canceled before it was sent.  Attempt to connect to
                // get an updated ConnectionResult.
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
//                Homepage.login = true;
//                Log.i(TAG, "444444");
            }
        } else {
            mGoogleApiClient.connect();

            // Google Play services wasn't able to provide an intent for some
            // error types, so we show the default Google Play services error
            // dialog which may still start an intent on our behalf if the
            // user can resolve the issue.
            //showDialog(DIALOG_PLAY_SERVICES_ERROR);
           //  Log.d("WENWEN1","22" );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    // If the error resolution was successful we should continue
                    // processing errors.
                    mSignInProgress = STATE_SIGN_IN;
                } else {
                    // If the error resolution was not successful or the user canceled,
                    // we should stop processing errors.
                    mSignInProgress = STATE_DEFAULT;
                }

                if (!mGoogleApiClient.isConnecting()) {
                    // If Google Play services resolved the issue with a dialog then
                    // onStart is not called so we need to re-attempt connection here.
                   Log.i("Wenwen","88");
                    mGoogleApiClient.connect();
                }
                break;
            case STATE_SIGN_IN:


                if  (mGoogleApiClient.isConnected()) {
//
               //
                   Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);

                    mGoogleApiClient.disconnect();
                    Log.d("wenwen", "activity result");
                }


                onSignedOut();
                email = null;
                Toast.makeText(getApplicationContext(), "You are now signed out", Toast.LENGTH_SHORT).show();
                login_msg_shown = false;


        }
    }

    private void onSignedOut() {
        // Update the UI to reflect that the user is signed out.

        mSignInButton.setEnabled(true);



      //  Button uploadButton = (Button) findViewById(R.id.open_image_upload_page);
      //  uploadButton.setClickable(false);

        if (imageView != null) {
            ((ViewGroup) imageView.getParent()).removeView(imageView);
            imageView = null;
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason.
        // We call connect() to attempt to re-establish the connection or get a
        // ConnectionResult that we can attempt to resolve.
        mGoogleApiClient.connect();
//        Homepage.login = true;
//        Log.i(TAG, "55555");
    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates",
                    "Google Play services is available.");
            // Continue
            return true;
            // Google Play services was not available for some reason.
            // resultCode holds the error code.
        } else {
            System.out.println("CONNECTION FAILED");

            return false;
        }
    }



}