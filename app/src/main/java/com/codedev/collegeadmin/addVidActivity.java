package com.codedev.collegeadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

public class addVidActivity extends AppCompatActivity {
    private TextView t2;
    private Button addVideoButton;
    private DatabaseReference databaseReference;
    private String title;
    private String id;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vid);


        t2=findViewById(R.id.addVidId);
        addVideoButton=findViewById(R.id.addVideoButton);
        pd=new ProgressDialog(this);

        databaseReference= FirebaseDatabase.getInstance().getReference();

        addVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                id=t2.getText().toString();

                 if(id.isEmpty()){
                    t2.setError("Empty");
                    t2.requestFocus();
                }else{
                    uploadvideo();
                }


            }
        });
    }

    private void uploadvideo() {
        pd.setTitle("Please Wait...");
        pd.setMessage("Uploading pdf...");
        pd.show();
        HashMap hp=new HashMap();

        hp.put("vidId",id);

        databaseReference.child("video");
        String uniqueKey=databaseReference.push().getKey();
        databaseReference.child("video").child(uniqueKey).setValue(hp).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(addVidActivity.this, "video Uploaded.", Toast.LENGTH_SHORT).show();
                t2.setText("");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(addVidActivity.this, "Failed to upload video", Toast.LENGTH_SHORT).show();
            }
        });
    }
}