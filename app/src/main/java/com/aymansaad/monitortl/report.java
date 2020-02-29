package com.aymansaad.monitortl;

import android.annotation.SuppressLint;
//import android.content.Intent;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
//import android.text.format.DateFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
//import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.fragment.app.Fragment;

//import com.google.android.gms.tasks.OnCompleteListener;
import com.aymansaad.monitortl.Model.LocationAddress;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
//import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
//import java.util.TimeZone;


public class report extends AppCompatActivity {
   // private FirebaseAuth mAuth = null;
    private static final String mamoria = "mamoria";
    private static final String action = "action";
    private static final String updateSalaryTotal = "salaryTotal";
    private FirebaseFirestore db,dbUbdateAction,dbUbdateActionto,dbupdateSalary,dbS,dbmontheSalary;
    private String TAG = "Ayman:";
//    boolean missionValuo;
    Button ok;
   int write=0;
    int currentDay ;
    int currentMonth ;
    int currentYear ;
    int CurrentDayOfYear;
    int mins,hours;
    double discount,montheSalary,overTime;
    ImageView trueFalse;

    private  String date ,dateMonthlySalary, endTimeForOvertime;
    Double salaryDay=0.0;
    double Total=0.0;
//    int currentDayOfWeek ;
//    int currentDayOfMonth ;
//    int CurrentDayOfYear ;
 //   double deductionValuo;
    GeoPoint location=null,locationEnd=null;
    TextView startLocation,startTime,endTime,textView2,endLocation,dateText;
    Timestamp timeStart=null,timeEnd=null;
    CheckBox mission;
    RadioGroup deduction;
    String checkedDb;
    Boolean mamoriaDb;
    String actionDb;
     Double salary ,discountDay;
     Double salaryTotal ;
    //ConstraintLayout parnt;
  //  ScrollView scrollmonth;
    LinearLayout makeAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String languageToLoad  = "en"; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_report);



        Intent intent = getIntent();

        //Toast.makeText(report.this, ">>>>>>>"+"email", Toast.LENGTH_SHORT).show();
        final String email = intent.getStringExtra("email");

//        final Double salary = Double.valueOf(intent.getStringExtra("salary"));
//        final Double salaryTotal = Double.valueOf(intent.getStringExtra("salaryTotal"));
//        Toast.makeText(report.this, email, Toast.LENGTH_SHORT).show();
//        Toast.makeText(report.this, "---------", Toast.LENGTH_SHORT).show();
//        Toast.makeText(report.this, String.valueOf(email), Toast.LENGTH_SHORT).show();
        String name = intent.getStringExtra("name");
        setTitle(name);
        ok=findViewById(R.id.ok);
        //parnt=findViewById(R.id.parnt);
        mission=findViewById(R.id.mission);
        makeAction=findViewById(R.id.makeAction);
        deduction=findViewById(R.id.deduction);
        textView2=findViewById(R.id.textView2);
        trueFalse=findViewById(R.id.trueFalse);



        trueFalse.setVisibility(View.GONE);
//        deduction.setVisibility(View.GONE);
//        mission.setVisibility(View.GONE);
        //ok.setVisibility(View.GONE);
        startLocation=findViewById(R.id.startLocation);
        dateText=findViewById(R.id.date);
        startTime=findViewById(R.id.startTime);
        endTime=findViewById(R.id.endTime);
        endLocation=findViewById(R.id.endLocation);
        db = FirebaseFirestore.getInstance();
        dbUbdateAction=FirebaseFirestore.getInstance();
        dbupdateSalary=FirebaseFirestore.getInstance();
        dbUbdateActionto=FirebaseFirestore.getInstance();
        dbS=FirebaseFirestore.getInstance();
        dbmontheSalary=FirebaseFirestore.getInstance();


        Calendar localCalendar =  Calendar.getInstance();
        Date currentTime = localCalendar.getTime();
        currentDay = localCalendar.get(Calendar.DATE);

        currentMonth = localCalendar.get(Calendar.MONTH) + 1;
        currentYear = localCalendar.get(Calendar.YEAR);
        CurrentDayOfYear = localCalendar.get(Calendar.DAY_OF_YEAR);
        //String dayIndex= getNameOfDay( currentYear,  CurrentDayOfYear);
        //Toast.makeText(report.this, "dayIndex>"+dayIndex, Toast.LENGTH_SHORT).show();
        //Toast.makeText(report.this,email, Toast.LENGTH_SHORT).show();

        date= String.valueOf(currentMonth)+"-"+String.valueOf(currentYear);
        dateText.setText(currentDay+"-"+date);
        //Toast.makeText(report.this,date, Toast.LENGTH_SHORT).show();
        final DocumentReference docRef = db.collection("users").document(email).collection(String.valueOf(date)).document(String.valueOf(currentDay));
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    Toast.makeText(report.this, "Listen failed.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    if (write!=-2) {
                        Log.d(TAG, "Current data: " + snapshot.getData());
                        location = snapshot.getGeoPoint("start_loc");
                        locationEnd = snapshot.getGeoPoint("end_loc");
                        timeStart = snapshot.getTimestamp("start_time");
                        timeEnd = snapshot.getTimestamp("end_time");
                        checkedDb = snapshot.getString("checked").toString();
                        mamoriaDb = snapshot.getBoolean("mamoria");
                        actionDb = snapshot.getString("action");
                        //Toast.makeText(report.this, " >>>>  " + mamoriaDb, Toast.LENGTH_SHORT).show();


                        LocationAddress locationAddressStart = new LocationAddress();

                        if (location != null) {

                            locationAddressStart.getAddressFromLocation(location.getLatitude(), location.getLongitude(),
                                    getApplicationContext(), new GeocoderHandlerStart());
                        }

                        if (locationEnd != null) {

                            LocationAddress locationAddressEnd = new LocationAddress();
                            locationAddressEnd.getAddressFromLocation(locationEnd.getLatitude(), locationEnd.getLongitude(),
                                    getApplicationContext(), new GeocoderHandlerEnd());
                        }


                        if (timeStart != null&&timeEnd== null) {

                            Calendar calStart = Calendar.getInstance(Locale.ENGLISH);
                            calStart.setTimeInMillis(timeStart.getSeconds() * 1000);
                            Date DateStart = calStart.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");


                            startTime.setText("Time : " +   sdf.format(DateStart));
                           // endTime.setText("end time : " +   sdfEnd.format(DateEnd));
                            //endTimeForOvertime=sdfEnd.format(DateEnd);
                        }
                        else if (timeStart != null&&timeEnd!= null) {

                            Calendar calStart = Calendar.getInstance(Locale.ENGLISH);
                            calStart.setTimeInMillis(timeStart.getSeconds() * 1000);
                            Date DateStart = calStart.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");

                            Calendar calEnd = Calendar.getInstance(Locale.ENGLISH);
                            calEnd.setTimeInMillis(timeEnd.getSeconds() * 1000);
                            Date DateEnd = calEnd.getTime();
                            SimpleDateFormat sdfEnd = new SimpleDateFormat("hh:mm a");
                            startTime.setText("Time : " +   sdf.format(DateStart)+" -->> "+sdfEnd.format(DateEnd));
                            // endTime.setText("end time : " +   sdfEnd.format(DateEnd));
                            endTimeForOvertime=sdfEnd.format(DateEnd);
                        }


                        if(Integer.valueOf(checkedDb)==1){
                            ok.setVisibility(View.GONE);
                            textView2.setVisibility(View.GONE);
                            deduction.setVisibility(View.GONE);
                            mission.setVisibility(View.GONE);
                            trueFalse.setVisibility(View.VISIBLE);
                        }


                    }



                } else {
                    Log.d(TAG, "Current data: null");
                    Toast.makeText(report.this, "Current data: null", Toast.LENGTH_SHORT).show();
                }

            }
        });

        final DocumentReference docRefS = dbS.collection("users").document(email);
        docRefS.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    Toast.makeText(report.this, "Listen failed.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    if (write!=-2) {

                        salary = Double.valueOf(snapshot.getString("salary"));
                        salaryTotal = Double.valueOf(snapshot.getString("salaryTotal"));


                    }


                } else {
                    Log.d(TAG, "Current data: null");
                    Toast.makeText(report.this, "Current data: null", Toast.LENGTH_SHORT).show();
                }

            }
        });

        if(currentDay==1){
            salaryTotal=0.0;
}

//        currentDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
//        currentDayOfMonth = localCalendar.get(Calendar.DAY_OF_MONTH);
//        CurrentDayOfYear = localCalendar.get(Calendar.DAY_OF_YEAR);


//        parnt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        ok.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                if (timeStart != null && timeEnd != null) {

                if (Integer.valueOf(checkedDb) == 0) {
                    //Toast.makeText(report.this, "000000", Toast.LENGTH_SHORT).show();


                    int dayIndex = getNameOfDay(currentYear, CurrentDayOfYear);
                    // Toast.makeText(report.this, "dayIndex"+dayIndex, Toast.LENGTH_SHORT).show();

                    salaryDay = (salary / 26);
                    discountDay = (salaryDay * discount);
                    if (dayIndex == 6) {
                        salaryDay = salaryDay * 2;
                    }
                    Total = (salaryTotal + salaryDay) - discountDay;
                    if (mission.isChecked()) {
                        Total = Total + 100;
                    }
                    SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");

                    try {
                        Date date1 = format.parse(endTimeForOvertime);
                        Date date2 = format.parse("05:00 pm");
                        long mills = date1.getTime() - date2.getTime();
                        Log.v("Data1", "" + date1.getTime());
                        Log.v("Data2", "" + date2.getTime());
                        hours = (int) (mills / (1000 * 60 * 60));
                        mins = (int) (mills / (1000 * 60)) % 60;
//                        Toast.makeText(report.this, endTimeForOvertime, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(report.this, hours+" : "+mins, Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (hours > 0 || mins > 0) {
                        Double Salarymins = (salaryDay / (8 * 60));
                        overTime = Salarymins * (hours * 60) + Salarymins * mins;

                    }


//                   if(selectedRadioButtonText=="deduction 1/2 day"){
//                       Total=salaryDay-(salaryDay/4);
//
//                   }else if(selectedRadioButtonText=="deduction 1/2 day"){
//                       Total=salaryDay-(salaryDay/2);
//                       Toast.makeText(report.this, Total, Toast.LENGTH_SHORT).show();
//
//                   }else if (selectedRadioButtonText=="deduction 1 day" ){
//                       Total=salaryDay-salaryDay;
//
//                   } else if (selectedRadioButtonText=="deduction 2 day"){
//                       Total=salaryDay-(2*salaryDay);
//                   }
//
                    //  Toast.makeText(report.this, selectedRadioButtonText, Toast.LENGTH_SHORT).show();
                    date = String.valueOf(currentMonth) + "-" + String.valueOf(currentYear);
                    montheSalary = Total + overTime;
                    Map<String, Object> userDb = new HashMap<>();
                    userDb.put(updateSalaryTotal, String.valueOf(montheSalary));

                    db.collection("users").document(email)
                            .update(userDb)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void avoid) {


                                    Log.d("FR:", "DocumentSnapshot added with ID: ");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("FR:", "Error adding document", e);
                                }
                            });
                    Map<String, Object> newContact = new HashMap<>();
                    newContact.put(mamoria, mission.isChecked());
                    newContact.put(action, String.valueOf(discount));
                    newContact.put("checked", "1");
                    newContact.put("overTime", String.valueOf(overTime));
                    newContact.put("overTimeMins", String.valueOf((hours * 60) + mins));
                    discount = -1;
                    db.collection("users").document(email).collection(String.valueOf(date)).document(String.valueOf(currentDay))
                            .update(newContact)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });


                } else if (Integer.valueOf(checkedDb) == 1) {
                    //Toast.makeText(report.this, "checkedDb !!!  "+String.valueOf(checkedDb), Toast.LENGTH_SHORT).show();

                    salaryDay = (salary / 26);
                    //Toast.makeText(report.this, "salaryDay   "+String.valueOf(salaryDay), Toast.LENGTH_SHORT).show();
                    Total = salaryTotal + (salaryDay * Double.valueOf(actionDb));
                    //Toast.makeText(report.this, "Total   "+String.valueOf(Total), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(report.this, " mamoriaDb "+String.valueOf(mamoriaDb), Toast.LENGTH_SHORT).show();
                    if (mamoriaDb == true) {

                        Total = Total - 100;
                        // Toast.makeText(report.this, "Total mamoriaDb "+String.valueOf(Total), Toast.LENGTH_SHORT).show();
                    }


                    //salaryDay=(salary/26);
                    Double Totalnow = Total - (salaryDay * discount);
                    // Toast.makeText(report.this, "Totalnow " + String.valueOf(Totalnow), Toast.LENGTH_SHORT).show();

                    if (mission.isChecked()) {
                        Totalnow = Totalnow + 100;

                    }


//                   if(selectedRadioButtonText=="deduction 1/2 day"){
//                       Total=salaryDay-(salaryDay/4);
//
//                   }else if(selectedRadioButtonText=="deduction 1/2 day"){
//                       Total=salaryDay-(salaryDay/2);
//                       Toast.makeText(report.this, Total, Toast.LENGTH_SHORT).show();
//
//                   }else if (selectedRadioButtonText=="deduction 1 day" ){
//                       Total=salaryDay-salaryDay;
//
//                   } else if (selectedRadioButtonText=="deduction 2 day"){
//                       Total=salaryDay-(2*salaryDay);
//                   }
                    montheSalary = Totalnow;
                    //Toast.makeText(report.this, "Total to " + Totalnow, Toast.LENGTH_SHORT).show();
                    // date = String.valueOf(currentDay) + "-" + String.valueOf(currentMonth) + "-" + String.valueOf(currentYear);
                    Map<String, Object> userDb = new HashMap<>();
                    userDb.put(updateSalaryTotal, String.valueOf(Totalnow));
                    dbupdateSalary.collection("users").document(email)
                            .update(userDb)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void avoid) {

                                    Log.d("FR:", "DocumentSnapshot added with ID: ");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("FR:", "Error adding document", e);
                                }
                            });

                    //Toast.makeText(report.this, "checkedDb  " + checkedDb, Toast.LENGTH_SHORT).show();


                    Map<String, Object> newContact = new HashMap<>();
                    newContact.put(mamoria, mission.isChecked());
                    newContact.put(action, String.valueOf(discount));
                    newContact.put("checked", "1");
                    write = -2;


                    dbUbdateActionto.collection("users").document(email).collection(String.valueOf(date)).document(String.valueOf(currentDay))
                            .update(newContact)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    write = -2;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });
                    //dbUbdateActionto.terminate();
                    //


                }

                dateMonthlySalary = String.valueOf(currentMonth) + "-" + String.valueOf(currentYear);
                Map<String, Object> newContactMonthe = new HashMap<>();
                newContactMonthe.put(updateSalaryTotal, String.valueOf(montheSalary));
                newContactMonthe.put("salary", String.valueOf(salary));

                dbmontheSalary.collection("users").document(email).collection(String.valueOf("monthlySalary")).document(dateMonthlySalary)
                        .set(newContactMonthe)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                finish();
//                                Intent i = new Intent(report.this,MainActivity.class);
//                                startActivity(i);
                                write = -2;
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
              /*      else{

                        Toast.makeText(report.this, "Nothing selected from Radio Group.", Toast.LENGTH_SHORT).show();

                        Map<String, Object> newContact = new HashMap<>();
                        newContact.put(mamoria, mission.isChecked());
                        newContact.put(updateSalaryTotal, String.valueOf(Total));
                        newContact.put("checked", "1");


                        dbUbdateActionto.collection("users").document(email).collection(String.valueOf("date")).document(currentDay+"-"+currentMonth+"-"+currentYear)
                                .update(newContact)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });

                    }

*/

            }


            }
                //onRadioButtonClicked(view);


        });
        makeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deduction.setVisibility(View.VISIBLE);
                mission.setVisibility(View.VISIBLE);
                ok.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);

            }
        });




     /*   db.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( QuerySnapshot documentSnapshots ,  FirebaseFirestoreException e) {
                if(e!=null){
                    Toast.makeText(report.this, "no data", Toast.LENGTH_SHORT).show();
                }

                for (DocumentSnapshot doc : documentSnapshots){
                    namesDb.add(doc.getString("name"));
                    emailsDb.add(doc.getString("email"));


                    //i++;
                    //Toast.makeText(MainActivity.this, emailsDb.get(1), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, namesDb.get(0), Toast.LENGTH_SHORT).show();


                }

            }
        });

      */


//        Map<String, Object> city = new HashMap<>();
//        city.put("name", "Los Angeles");
//        city.put("state", "CA");
//        city.put("country", "USA");
//        db.collection("Users").document("aymansaadhack@gmail.com")
//                .set(city)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error writing document", e);
//                    }
//                });

//
//
        /*
        db.collection("Users"+"/"+email+"/"+"date"+"/"+currentDay+"-"+currentMonth+"-"+currentYear)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Ayman:", document.getId() + " => " + document.getData());

                            }
                        } else {
                            Log.w("Ayman:", "Error getting documents.", task.getException());
                        }
                    }
                });

         */






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
            startLocation.setText("start address : "+locationAddress);
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
            endLocation.setText("end address : "+locationAddress);
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton:
                if(checked)
                    discount = .25;
                break;
            case R.id.radioButton2:
                if(checked)
                    discount = .5;
                break;
            case R.id.radioButton3:
                if(checked)
                    discount = 1;
                break;
            case R.id.radioButton4:
                if(checked)
                    discount = 2;
                break;
            case R.id.radioButton5:
                if(checked)
                    discount = 0;
                break;
        }
        //Toast.makeText(getApplicationContext(), (int) discount, Toast.LENGTH_SHORT).show();
    }
    private int getNameOfDay(int year,  int dayOfYear) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);

        calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
       //String days[] = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        int dayIndex = calendar.get(Calendar.DAY_OF_WEEK);
                //Toast.makeText(getApplicationContext(), "fff<<"+String.valueOf(dayIndex), Toast.LENGTH_SHORT).show();
        return dayIndex;
    }
}
