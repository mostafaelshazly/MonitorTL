package com.aymansaad.monitortl;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class editaEmp extends AppCompatActivity {
    private FirebaseAuth mAuth = null,mAuthDelete=null;
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
        setContentView(R.layout.activity_edita_emp);
        //Bundle bundle = getIntent().getExtras();
        //String name = bundle.getString("name");
        //String email = bundle.getString("email");
        //String pass = bundle.getString("pass");
        //int salary = bundle.getInt("salary");

        final EditText etName = findViewById(R.id.etName);
        final TextView etEmail = findViewById(R.id.etEmail);
        final TextView etPass = findViewById(R.id.etPass);
        final EditText etSalary = findViewById(R.id.etSalary);
        Intent intent = getIntent();


        final String email = intent.getStringExtra("email");
        final String  password = intent.getStringExtra("password");
        db = FirebaseFirestore.getInstance();


        String name = intent.getStringExtra("name");
        String salary = intent.getStringExtra("salary");
        setTitle(name);
        etName.setText(name);
        etEmail.setText(email);
        etPass.setText(password);
        etSalary.setText(salary);
        mAuth = FirebaseAuth.getInstance();
        mAuthDelete = FirebaseAuth.getInstance();
        final Button add = findViewById(R.id.btnAdd);
        final Button delete = findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document(email)
                        .delete();

               /*
                db = FirebaseFirestore.getInstance();
                db.collection("users").document(email)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                */
                mAuthDelete.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User account deleted.");
                                                finish();
                                                Intent i = new Intent(editaEmp.this,MainActivity.class);
                                                startActivity(i);
                                            }
                                        }
                                    });



//                            finish();
//                            // String value=mAuth.getCurrentUser().getEmail();
//                            Intent intent = new Intent(log.this, Home.class);
//                            //intent.putExtra("emailUser",value);
//                            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });



           }

        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (add.getText().equals("Ok")) {
                    db = FirebaseFirestore.getInstance();
                    Map<String, Object> userDb = new HashMap<>();
                    userDb.put("name", etName.getText().toString());
                    userDb.put("salary", etSalary.getText().toString());
                    //userDb.put("email", etEmail.getText().toString());
                    db.collection("users").document(email)
                            .update(userDb)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void avoid) {
                                    Log.d("FR:", "DocumentSnapshot added with ID: ");
                                    finish();
                                    Intent i = new Intent(editaEmp.this,MainActivity.class);
                                    startActivity(i);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("FR:", "Error adding document", e);
                                }
                            });
//                    mAuth.signInWithEmailAndPassword(email, password);
//                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                    user.updatePassword( "112233445566");
                    //FirebaseAuth.getInstance().signOut();
//                    mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPass.getText().toString())
//                            .addOnCompleteListener(editaEmp.this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (task.isSuccessful()) {
//                                        // Sign in success, update UI with the signed-in user's information
//                                        Log.d(TAG, "createUserWithEmail:success");
//                                        FirebaseUser  user = mAuth.getCurrentUser();
//                                        finish();
//                                        Intent i = new Intent(editaEmp.this,MainActivity.class);
//                                        startActivity(i);
//
//                                    } else {
//                                        // If sign in fails, display a message to the user.
//                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                                        Toast.makeText(editaEmp.this, "Authentication failed.",
//                                                Toast.LENGTH_SHORT).show();
//
//                                    }
//
//                                    // ...
//                                }
//                            });




// Add a new document with a generated ID

                }

            }
        });

    }
}
