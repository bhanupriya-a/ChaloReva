package com.example.buss;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buss.databinding.ActivityReadDataBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReadData extends AppCompatActivity {

    private TextView tv;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    ImageView rImage;


    ActivityReadDataBinding binding;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityReadDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tv = findViewById(R.id.tvValidity);
        rImage = findViewById(R.id.rImage);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        DatabaseReference getimageUrl = databaseReference.child("students");
        getimageUrl.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange (@NonNull DataSnapshot dataSnapshot)
            {

                String link = dataSnapshot.getValue(String.class);
                Picasso.get().load(link).into(rImage);
            }

            @Override
            public void onCancelled (@NonNull DatabaseError databaseError)
            {

                Toast.makeText(ReadData.this, "Error Loading Image", Toast.LENGTH_SHORT).show();
            }
        });

        binding.readdataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                calendar = Calendar.getInstance();

                calendar.add(Calendar.DATE, 3);
                calendar.add(Calendar.MONTH, 1);
                calendar.add(Calendar.YEAR, 1);
                SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");

                tv.setText(dateOnly.format(calendar.getTime()));
                String username = binding.etusername.getText().toString();
                if (!username.isEmpty()) {

                    readData(username);
                } else {

                    Toast.makeText(ReadData.this, "PLease Enter Username", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



    private void readData(String username) {

        reference = FirebaseDatabase.getInstance().getReference("students");
        reference.child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()){

                    if (task.getResult().exists()){

                        Toast.makeText(ReadData.this,"Successfully Read",Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        String Name = String.valueOf(dataSnapshot.child("name").getValue());
                        String Srn = String.valueOf(dataSnapshot.child("srn").getValue());
                        String Course = String.valueOf(dataSnapshot.child("ourse").getValue());
                        String Sem = String.valueOf(dataSnapshot.child("sem").getValue());
                        String Pick= String.valueOf(dataSnapshot.child("pick").getValue());
                        binding.tvFirstName.setText(Name);
                        binding.tvSrn.setText(Srn);
                        binding.tvCourse.setText(Course);
                        binding.tvSemester.setText(Sem);
                        binding.tvPickUp.setText(Pick);


                    }else {

                        Toast.makeText(ReadData.this,"User Doesn't Exist",Toast.LENGTH_SHORT).show();

                    }


                }else {

                    Toast.makeText(ReadData.this,"Failed to read",Toast.LENGTH_SHORT).show();
                }

            }
        });


        }

    }
