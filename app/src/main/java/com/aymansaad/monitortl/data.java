package com.aymansaad.monitortl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class data extends AppCompatActivity {
Button button3;
int currentDay=1;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        db = FirebaseFirestore.getInstance();
        button3=findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> newContact = new HashMap<>();
                newContact.put("ordarBy",String.valueOf(currentDay) );
                newContact.put("mamoria", true);
                newContact.put("action", "1");
                newContact.put("checked", "1");
                newContact.put("overTime", ".5");
                newContact.put("overTimeMins", "20");
                newContact.put("start_time", new Timestamp(new Date()));
                newContact.put("end_time", new Timestamp(new Date()));

                db.collection("users").document("bob@gmail.com").collection("12-2019").document(String.valueOf(currentDay))
                        .set(newContact)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("ss", "DocumentSnapshot successfully written!");
                                currentDay++;
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Error", "Error writing document", e);
                            }
                        });

            }
        });
    }
}
