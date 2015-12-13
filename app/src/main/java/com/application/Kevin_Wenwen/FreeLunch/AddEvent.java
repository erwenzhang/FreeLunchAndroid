package com.application.Kevin_Wenwen.FreeLunch;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;


import android.view.Menu;
import android.view.MenuItem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import android.content.Intent;

import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.Context;
import android.widget.Button;
import android.content.DialogInterface;

import android.widget.CheckBox;
import android.support.v7.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wenwen on 12/9/15.
 */
public class AddEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener{

    Context context = this;
    private TextView timeTextView;
    private TextView dateTextView;
    private CheckBox mode24Hours;
    private String hourString1;
    private String minuteString1;
    private String hourString2 ;
    private String minuteString2 ;
    private boolean flag = true;
    private String day;
    private String month;
    private String year;

    private AutoCompleteTextView locationView;
    private EditText eventView;
    private EditText detailView;
   // private ImageView uploadView;
    private EditText roomView;

    private Bitmap bitmapImage;



    private String TAG  = "Add Event";
    private String email;
    private String[] msg;
    public final static String EXTRA_MESSAGE = "MESSAGE IN";
    private static final int PICK_IMAGE = 1;
    private static final int USE_CAMERA = 2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        msg = getIntent().getStringArrayExtra(EXTRA_MESSAGE);
        email = msg[0];

        timeTextView = (TextView)findViewById(R.id.time_textview);
        dateTextView = (TextView)findViewById(R.id.date_textview);
        Button timeButton1 = (Button)findViewById(R.id.time_button1);
        Button timeButton2 = (Button)findViewById(R.id.time_button2);
        Button dateButton = (Button)findViewById(R.id.date_button);
        mode24Hours = (CheckBox)findViewById(R.id.mode_24_hours);


        eventView =  (EditText)findViewById(R.id.event_name);
        detailView = (EditText)findViewById(R.id.details);
        //uploadView =(ImageView) findViewById(R.id.upload_photo);
        Button chooseFromGallery = (Button)findViewById((R.id.gallery));
        Button submit = (Button)findViewById(R.id.submit_event);
        Button clear = (Button)findViewById(R.id.clear);
        roomView =(EditText)findViewById(R.id.room);
        locationView = (AutoCompleteTextView) findViewById(R.id.location);

// Get the string array
        String[] buildings = getResources().getStringArray(R.array.buildings_array);
// Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, buildings);
        locationView.setAdapter(adapter);


        // Show a timepicker when the timeButton is clicked
        timeButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        AddEvent.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        mode24Hours.isChecked()
                );
                tpd.enableSeconds(false);
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog1");
            }
        });

        timeButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;

                Calendar now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        AddEvent.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        mode24Hours.isChecked()
                );
                tpd.enableSeconds(false);
                tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Log.d("TimePicker", "Dialog was cancelled");
                    }
                });
                tpd.show(getFragmentManager(), "Timepickerdialog2");
            }
        });

        // Show a datepicker when the dateButton is clicked
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddEvent.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        chooseFromGallery.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // To do this, go to AndroidManifest.xml to add permission
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        // Start the Intent
                        startActivityForResult(galleryIntent, PICK_IMAGE);
                    }
                }
        );



        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String details = detailView.getText().toString().trim();
                String event_name = eventView.getText().toString().trim();
                String building = locationView.getText().toString().trim();
                String room = roomView.getText().toString().trim();

                if(hourString2==null||hourString1==null||day==null||month==null||year==null||details==null||event_name==null||building==null){
                    Toast.makeText(context, "You have not filled in all the needed infomation. ", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {

                String date_time1 = year+" "+month+" "+day+" "+hourString1+" "+minuteString1;
                    String date_time2 = year+" "+month+" "+day+" "+hourString2+" "+minuteString2;
                    Log.d("day1",date_time1);
                    Log.d("month1",date_time2);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmapImage.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] b = baos.toByteArray();
                    byte[] encodedImage = Base64.encode(b, Base64.DEFAULT);
                    String encodedImageStr = encodedImage.toString();

                    getUploadURL(b,details,event_name,building,room,date_time1,date_time2 );

                }


            }

        });

        clear.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

            detailView.setText("");
                eventView.setText("");
                locationView.setText("");
                roomView.setText("");

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.sign_out_button) {
            Intent intent = new Intent(context,Homepage.class);
//            String[] msg_out = new String[4];
//            msg_out[0] = email;
//            intent.putExtra(EXTRA_MESSAGE, msg_out);
            // catch event that there's no activity to handle intent

            startActivity(intent);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        TimePickerDialog tpd1 = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog1");
        TimePickerDialog tpd2 = (TimePickerDialog) getFragmentManager().findFragmentByTag("Timepickerdialog2");
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");

        if(tpd1 != null) tpd1.setOnTimeSetListener(this);
        if(tpd2 != null) tpd2.setOnTimeSetListener(this);
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        if(flag){
        hourString1 = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
        minuteString1 = minute < 10 ? "0"+minute : ""+minute;
        }
        else{
            hourString2 = hourOfDay < 10 ? "0"+hourOfDay : ""+hourOfDay;
            minuteString2 = minute < 10 ? "0"+minute : ""+minute;

        }

        if(hourString2!=null){
        String time = "You picked the following time: "+hourString1+"h"+minuteString1+"m ~ "+hourString2+"h"+minuteString2+"m";
        timeTextView.setText(time);
        }

    }

    @Override
    public void onDateSet(DatePickerDialog view, int theyear, int monthOfYear, int dayOfMonth) {
        day = Integer.toString(dayOfMonth);
        month = Integer.toString(monthOfYear+1);
        year = Integer.toString(theyear);
        Log.d("day",day);
        Log.d("month", month);

        String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+theyear;
        dateTextView.setText(date);
    }

    public void useCamera(View view){
        Intent cameraIntent = new Intent(context, CameraActivity.class);
        cameraIntent.putExtra(EXTRA_MESSAGE, msg);
        startActivityForResult(cameraIntent, USE_CAMERA);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();

            Log.i("photophoto:",selectedImage.toString());
            // User had pick an image.
            String imageFilePath="";
            String[] filePathColumn = {MediaStore.Images.ImageColumns.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if(cursor.moveToFirst()){
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imageFilePath = cursor.getString(columnIndex);
            }
           // cursor.moveToFirst();

            // Link to the image



            cursor.close();

            // Bitmap imaged created and show thumbnail

            ImageView imgView = (ImageView) findViewById(R.id.upload_photo);
            bitmapImage = BitmapFactory.decodeFile(imageFilePath);
            imgView.setImageBitmap(bitmapImage);

            // Enable the upload button once image has been uploaded


        } else if (requestCode == USE_CAMERA && resultCode == RESULT_OK && data != null      /*&& data.getData() != null*/) {

            String imageFilePath;
            String[] msg_from_camera = data.getStringArrayExtra(EXTRA_MESSAGE);
            imageFilePath = msg_from_camera[1];
            //  if(extras == null) {
            //    imageFilePath = null;
            //} else {
            //  imageFilePath = extras.getString("image_path");
            //}
          //  Log.d("TAGTAGTAG upload",msg[0]);
         //   Log.d("TAGTAGTAG upload", msg[1]);

            // Bitmap imaged created and show thumbnail

            ImageView imgView = (ImageView) findViewById(R.id.upload_photo);
            bitmapImage = BitmapFactory.decodeFile(imageFilePath);
            imgView.setImageBitmap(bitmapImage);

            // Enable the upload button once image has been uploaded


        }
    }

    private void getUploadURL(final byte[] encodedImage, final String details, final String event_name, final String building,final String room, final String date_time1, final String date_time2){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        String request_url="http://freelunchforyou.appspot.com/GetUploadURL";
        //System.out.println(request_url);
        httpClient.get(request_url, new AsyncHttpResponseHandler() {
            String upload_url;

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                try {
                    JSONObject jObject = new JSONObject(new String(response));

                    upload_url = jObject.getString("upload_url");
                    postToServer(encodedImage, details, event_name, building, room, date_time1, date_time2, upload_url);


                } catch (JSONException j) {
                    System.out.println("JSON Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e("Get_serving_url", "There was a problem in retrieving the url : " + e.toString());
            }
        });
    }

    private void postToServer(byte[] encodedImage,String details,final String event_name, final String building,final String room, final String date_time1, final String date_time2, String upload_url){

        RequestParams params = new RequestParams();
        params.put("file",new ByteArrayInputStream(encodedImage));
        params.put("details",details);
        params.put("worker_name", email);
        params.put("event_name", event_name);
        params.put("building", building);
        params.put("room", room);
        params.put("date_time1", date_time1);
        params.put("date_time2", date_time2);



        AsyncHttpClient client = new AsyncHttpClient();
        client.post(upload_url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                Log.i("async", "success!!!!");
                Toast.makeText(context, "Upload Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, FreeLunchList.class);

               intent.putExtra(EXTRA_MESSAGE, msg);
                startActivity(intent);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e("Posting_to_blob", "There was a problem in retrieving the url : " + e.toString());
            }
        });
    }

}