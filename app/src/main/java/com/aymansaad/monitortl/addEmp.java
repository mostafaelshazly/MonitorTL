package com.aymansaad.monitortl;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class addEmp extends AppCompatActivity {
    private FirebaseAuth mAuth = null;
    private String TAG = "Auth::";
    private FirebaseFirestore db;
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
        setContentView(R.layout.activity_add_emp);
        //Bundle bundle = getIntent().getExtras();
        //String name = bundle.getString("name");
        //String email = bundle.getString("email");
        //String pass = bundle.getString("pass");
        //int salary = bundle.getInt("salary");

        final EditText etName = findViewById(R.id.etName);
        final EditText etEmail = findViewById(R.id.etEmail);
        final EditText etPass = findViewById(R.id.etPass);
        final EditText etSalary = findViewById(R.id.etSalary);
        Intent intent = getIntent();


        final String email = intent.getStringExtra("email");



        String name = intent.getStringExtra("name");
        setTitle(name);
        etName.setText(name);
        etEmail.setText(email);
        //etPass.setText(pass);
        //etSalary.setText(salary);
        mAuth = FirebaseAuth.getInstance();
        final Button add = findViewById(R.id.btnAdd);



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (add.getText().equals("Add")) {

                    mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString())
                            .addOnCompleteListener(addEmp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
//                                        FirebaseUser  user = mAuth.getCurrentUser();
//                                        finish();
//                                        Intent i = new Intent(addEmp.this,MainActivity.class);
//                                        startActivity(i);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(addEmp.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                    // ...
                                }
                            });

                     db = FirebaseFirestore.getInstance();
                    Map<String, Object> userDb = new HashMap<>();
                    userDb.put("name", etName.getText().toString());
                    userDb.put("salary", etSalary.getText().toString());
                    userDb.put("email", etEmail.getText().toString());
                    userDb.put("password", etPass.getText().toString());
                    userDb.put("salaryTotal", "0");


// Add a new document with a generated ID
                    db.collection("users").document(etEmail.getText().toString())
                            .set(userDb)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void avoid) {
                                    finish();
                                    Intent i = new Intent(addEmp.this,MainActivity.class);
                                    startActivity(i);
                                    Log.d("FR:", "DocumentSnapshot added with ID: ");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("FR:", "Error adding document", e);
                                }
                            });
                }

            }
        });

    }
}
