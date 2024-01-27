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

public class userregister extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputEditText registername,registersrn,registeremail,registerpassword;
    private Button signupbutton,loginredirectbutton;
    FirebaseFirestore fstore;
    String userID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userregister);

        auth=FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        registername=(TextInputEditText) findViewById(R.id.name_text);
        registersrn=(TextInputEditText) findViewById(R.id.srn_text);
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
                String srn = registersrn.getText().toString();
                String role = "user";


                if(userName.isEmpty()) {
                    registername.setError("name is required");
                    registername.requestFocus();
                    return;
                }
                if(srn.isEmpty()) {
                    registersrn.setError("srn is required");
                    registersrn.requestFocus();
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
                                Toast.makeText(userregister.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                userID = auth.getCurrentUser().getUid();
                                DocumentReference documentReference = fstore.collection("users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("name", userName);
                                user.put("srn", srn);
                                user.put("email", email);
                                user.put("role",role);
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "onSuccess: user Profile is created for" + userID);
                                }

                                    void addOnFailureListener(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                                startActivity(new Intent(userregister.this, user_login.class));
                            }else{
                                Toast.makeText(userregister.this, "SignUp failed" + task.getException().getMessage() ,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }



        });
        loginredirectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(userregister.this, user_login.class);
                startActivity(intent);

            }

        });

    }
}