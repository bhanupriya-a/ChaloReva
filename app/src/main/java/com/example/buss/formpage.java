package com.example.buss;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class formpage extends AppCompatActivity {

    private final int gall_req = 1000;
    ImageView imageView;
    EditText name, srn, course, sem, pick;
    Button btn1,btn;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference reference=FirebaseStorage.getInstance().getReference();
    Uri imageuri;

    String sr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formpage);

        firebaseDatabase = firebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        imageView = findViewById(R.id.iv);
        btn = findViewById(R.id.upload);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent ig = new Intent(Intent.ACTION_PICK);
                ig.setData((MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                startActivityForResult(ig, gall_req);

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == gall_req) {
                imageuri=data.getData();
                imageView.setImageURI(imageuri);
                uploadFirebase(imageuri);


            }
        }




        name = findViewById(R.id.name);
        srn = findViewById(R.id.srn);
        course = findViewById(R.id.course);
        sem = findViewById(R.id.sem);
        pick = findViewById(R.id.pick);
        btn1 = findViewById(R.id.pi);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String nam = name.getText().toString();
                sr = srn.getText().toString();
                String cou = course.getText().toString();
                String se = sem.getText().toString();
                String pic = pick.getText().toString();

                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("name", nam);
                hashMap.put("srn",sr);
                hashMap.put("ourse",cou);
                hashMap.put("sem",se);
                hashMap.put("pick",pic);





                boolean check = validate(nam, sr, cou, se, pic);
                if (check == true) {

                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show(); databaseReference.setValue(nam);
                    databaseReference.child("students")
                            .child(sr)
                            .setValue(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent i = new Intent(formpage.this, payment_page.class);
                                    startActivity(i);

                                }
                            });




                } else {
                    Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void uploadFirebase(Uri uri) {
        StorageReference reference1=reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        reference1.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Model model = new Model(uri.toString());
                        databaseReference.child(sr).setValue(model);
                        Toast.makeText(formpage.this, "upload success",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private String getFileExtension(Uri muri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap= MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(muri));
    }

    private Boolean validate(String nam, String sr, String cou, String se, String pic) {

        if (nam.length() == 0) {
            name.requestFocus();
            name.setError("field cant be empty");
            return false;
        } else if (!nam.matches("[a-zA-Z]+")) {
            name.requestFocus();
            name.setError("enter only alphabets");
            return false;
        } else if (sr.length() == 0) {
            srn.requestFocus();
            srn.setError("field cant be empty");
            return false;
        } else if (cou.length() == 0) {
            name.requestFocus();
            name.setError("field cant be empty");
            return false;
        } else if (!cou.matches("[a-zA-Z]+")) {
            course.requestFocus();
            course.setError("enter only alphabets");
            return false;
        } else if (se.length() == 0) {
            sem.requestFocus();
            sem.setError("enter only alphabets");
            return false;
        } else if (!se.matches("[0-9]+")) {
            sem.requestFocus();
            sem.setError("enter only numeric");
            return false;
        }
        if (pic.length() == 0) {
            pick.requestFocus();
            pick.setError("field cant be empty");
            return false;
        } else if (!pic.matches("[a-zA-Z]+")) {
            pick.requestFocus();
            pick.setError("enter only alphabets");
            return false;

        } else {
            return true;
        }
    }
}