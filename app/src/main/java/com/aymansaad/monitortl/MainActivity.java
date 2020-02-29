package com.aymansaad.monitortl;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aymansaad.monitortl.Adapter.myListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    //private String[] names = {"Ayman Saad", "Mohamed Shaban", "Ahmed Mostafa", "Tarek Mohamed"};
    //private String[] emails = {"aymansaad@gmail.com", "mohamedshaban@gmail.com", "ahmedmostafa@gmail.com", "tarekmohamed@gmail.com"};
    ArrayList<String> namesDb = new ArrayList<String>();
    ArrayList<String> emailsDb = new ArrayList<String>();
    ArrayList<String> passwordsDb = new ArrayList<String>();
    ArrayList<String>  salaryDb = new ArrayList<String>();
    ArrayList<String>  salaryTotal = new ArrayList<String>();

    //String[] arrayNames ={};
   // String[] arrayEmails={};



    private String[] imgs = {};
    private FirebaseFirestore db;
private FloatingActionButton fab;


    @Override
    protected void onStart() {
        super.onStart();


    }

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
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent( QuerySnapshot documentSnapshots ,  FirebaseFirestoreException e) {
                if(e!=null){
                    Toast.makeText(MainActivity.this, "no data", Toast.LENGTH_SHORT).show();
                }

                for (DocumentSnapshot doc : documentSnapshots){
                    namesDb.add(doc.getString("name"));
                    emailsDb.add(doc.getString("email"));
                    passwordsDb.add(doc.getString("password"));
                    salaryDb.add(doc.getString("salary"));
                    salaryTotal.add(doc.getString("salaryTotal"));




                }

            }
        });
        setTitle("Select Employee");


        myListAdapter myListAdapter = new myListAdapter(this,this, namesDb, emailsDb, imgs,passwordsDb,salaryDb,salaryTotal);
        listView = findViewById(R.id.listView);
        listView.setAdapter(myListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



            }
        });
/*
        db.collection("Users").document("a@gmail.com")
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

 */

        fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,addEmp.class);
                startActivity(i);

            }
        });

    }

}
