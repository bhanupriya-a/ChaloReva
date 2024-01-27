package com.example.buss;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class adminregister extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputEditText registername,registeruid,registeremail,registerpassword;
    private Button signupbutton,loginredirectbutton;
    FirebaseFirestore fstore;
    String userID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminregister);

        auth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        registername=(TextInputEditText) findViewById(R.id.name_text);
        registeruid=(TextInputEditText) findViewById(R.id.srn_text);
        registeremail=(TextInputEditText) findViewById(R.id.mail_text);
        registerpassword=(TextInputEditText) findViewById(R.id.password_text);
        signupbutton=findViewById(R.id.signup);
        loginredirectbutton=findViewById(R.id.haveaccount);


        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = registeremail.getText().toString().trim();
                String pass = registerpassword.getText().toString();
                String userName = registername.getText().toString();
                String uid = registeruid.getText().toString();



                if(userName.isEmpty()) {
                    registername.setError("name is required");
                    registername.requestFocus();
                    return;
                }
                if(uid.isEmpty()) {
                    registeruid.setError("srn is required");
                    registeruid.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    registeremail.setError("Enter an email address");
                    registeremail.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    registeremail.setError("Enter a valid email address");
                    registeremail.requestFocus();
                    return;
                }

                if (pass.isEmpty()) {
                    registerpassword.setError("Enter a password");
                    registerpassword.requestFocus();
                    return;
                }

                if (pass.length() < 8) {

                    registerpassword.setError("Password Length Must be 8 Digits");
                    registerpassword.requestFocus();
                    return;

                }
                else{
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(adminregister.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                userID = auth.getCurrentUser().getUid();
                                DocumentReference documentReference = fstore.collection("admin").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("name", userName);
                                user.put("uid", uid);
                                user.put("email", email);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "onSuccess: user Profile is created for" + userID);
                                    }

                                    void addOnFailureListener(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + e.toString());
                                    }
                                });
                                startActivity(new Intent(adminregister.this,adminlogin.class));
                            }else{
                                Toast.makeText(adminregister.this, "SignUp failed" + task.getException().getMessage() ,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }



        });
        loginredirectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminregister.this, adminlogin.class);
                startActivity(intent);

            }

        });

    }
}