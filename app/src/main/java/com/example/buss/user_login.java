package com.example.buss;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class user_login extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputEditText loginemail,loginpassword;
    private Button loginbutton ,signupredirectbutton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);

        auth=FirebaseAuth.getInstance();
        loginemail=(TextInputEditText) findViewById(R.id.mail_text);
        loginpassword=(TextInputEditText) findViewById(R.id.password_text);
        loginbutton=findViewById(R.id.button);
        signupredirectbutton=findViewById(R.id.register1);


        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email = loginemail.getText().toString().trim();
                String pass = loginpassword.getText().toString();



                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!pass.isEmpty()) {
                        auth.signInWithEmailAndPassword(email, pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(user_login.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(user_login.this, nextpage.class));
                                        finish();
                                    }


                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(user_login.this, "Login failed", Toast.LENGTH_SHORT).show();
                                    }


                                });

                    } else {
                        loginpassword.setError("password cannot be empty");
                    }
                }else if(email.isEmpty()){
                    loginemail.setError("email cannot be empty");
                }else{
                    loginemail.setError("please enter valid email");
                }

                }

        });


        signupredirectbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(user_login.this,userregister.class);
                startActivity(intent);
                finish();
            }
        });

    }
}