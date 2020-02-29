package com.aymansaad.monitortl;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.aymansaad.monitortl.Model.LocationAddress;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class reportCard extends AppCompatActivity {
    private FirebaseFirestore db;
    int currentDay ;
    int currentMonth ;
    int currentYear ;
    private  String date ;

    GeoPoint location=null,locationEnd=null;
    TextView startLocation,startTime,endTime,textView2,endLocation;
    Timestamp timeStart=null,timeEnd=null;
    CheckBox mission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_card);
        startLocation=findViewById(R.id.tvStartLocation);
        startTime=findViewById(R.id.startTime);
        endTime=findViewById(R.id.endTime);
        endLocation=findViewById(R.id.tvEndLocation);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();


        final String email = intent.getStringExtra("email");

        String name = intent.getStringExtra("name");
        setTitle(name);

        Calendar localCalendar =  Calendar.getInstance();
        Date currentTime = localCalendar.getTime();
        currentDay = localCalendar.get(Calendar.DATE);

        currentMonth = localCalendar.get(Calendar.MONTH) + 1;
        currentYear = localCalendar.get(Calendar.YEAR);


        Toast.makeText(reportCard.this,email, Toast.LENGTH_SHORT).show();

        date= String.valueOf(currentDay)+"-"+String.valueOf(currentMonth)+"-"+String.valueOf(currentYear);
        Toast.makeText(reportCard.this,date, Toast.LENGTH_SHORT).show();
        final DocumentReference docRef = db.collection("Users").document(email).collection(String.valueOf("date")).document(date);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                   // Log.w(TAG, "Listen failed.", e);
                    Toast.makeText(reportCard.this, "Listen failed.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (snapshot != null && snapshot.exists()) {

                   // Log.d(TAG, "Current data: " + snapshot.getData());
                    location= snapshot.getGeoPoint("start_loc" );
                    locationEnd= snapshot.getGeoPoint("end_loc" );
                    timeStart=snapshot.getTimestamp("start_time");
                    timeEnd=snapshot.getTimestamp("end_time");


                    LocationAddress locationAddressStart = new LocationAddress();

                    if(location!=null){

                        locationAddressStart.getAddressFromLocation(location.getLatitude(), location.getLongitude(),
                                getApplicationContext(), new reportCard.GeocoderHandlerStart());
                    }

                    if(locationEnd!=null){

                        LocationAddress locationAddressEnd = new LocationAddress();
                        locationAddressEnd.getAddressFromLocation(locationEnd.getLatitude(), locationEnd.getLongitude(),
                                getApplicationContext(), new reportCard.GeocoderHandlerEnd());
                    }





                    if(timeStart!=null){

                        Calendar calStart = Calendar.getInstance(Locale.ENGLISH);
                        calStart.setTimeInMillis(timeStart.getSeconds() * 1000);
                        Date DateStart = calStart.getTime();

                        startTime.setText("start time : "+DateStart);
                    }

                    if(timeEnd!=null){

                        Calendar calEnd = Calendar.getInstance(Locale.ENGLISH);
                        calEnd.setTimeInMillis(timeEnd.getSeconds() * 1000);
                        Date DateEnd = calEnd.getTime();
                        endTime.setText("end time : "+DateEnd);
                    }






                } else {
                    //Log.d(TAG, "Current data: null");
                    //Toast.makeText(report.this, "Current data: null", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private   class GeocoderHandlerStart extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            startLocation.setText(" end address : "+locationAddress);
        }
    }
    private class GeocoderHandlerEnd extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            endLocation.setText(" end address : "+locationAddress);
        }
    }}
