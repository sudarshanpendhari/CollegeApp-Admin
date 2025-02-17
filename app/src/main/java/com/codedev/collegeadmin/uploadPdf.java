package com.codedev.collegeadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class uploadPdf extends AppCompatActivity {

    private CardView addPdf;
    private final int REQ=1;
    private Uri pdfData;
    private TextView pdfTextView;
    private String pdfName,title;

    private EditText pdfTitle;
    private Button UploadPdfButton;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    String downloadurl="";
    private Spinner pdfCategory;
    private ProgressDialog pd;
    private String category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_pdf);
        addPdf=findViewById(R.id.addPdf);

        pdfTitle=findViewById(R.id.pdfTitle);
        pdfCategory=findViewById(R.id.pdfCategory);
        UploadPdfButton=findViewById(R.id.UploadPdfButton);
        pdfTextView=findViewById(R.id.pdfTextView);

        pd=new ProgressDialog(this);

        String[] items=new String[]{"Select Category","Computer","AI&DS","Mechanical","Civil","E&TC"};
        pdfCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,items));

        pdfCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category=pdfCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        databaseReference= FirebaseDatabase.getInstance().getReference();
        storageReference= FirebaseStorage.getInstance().getReference();
        addPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        UploadPdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title=pdfTitle.getText().toString();
                if(title.isEmpty()){
                    pdfTitle.setError("Empty");
                    pdfTitle.requestFocus();
                }else if(pdfData==null){
                    Toast.makeText(uploadPdf.this, "Please Upload Pdf", Toast.LENGTH_SHORT).show();
                }else if(category.equals("Select Category")){
                    Toast.makeText(uploadPdf.this, "Please select category", Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadpdf();
                }
            }
        });
    }

    private void uploadpdf() {
        pd.setTitle("Please Wait...");
        pd.setMessage("Uploading pdf...");
        pd.show();
        StorageReference reference=storageReference.child("pdf/"+pdfName+"-"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri=uriTask.getResult();
                        uploadData(String.valueOf(uri));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(uploadPdf.this, "Something Went wrong..", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadData(String url) {
        databaseReference=databaseReference.child("pdf").child(category);
        String uniqueKey=databaseReference.push().getKey();
        HashMap data=new HashMap();
        data.put("pdfTitle",title);
        data.put("pdfUrl",url);

        databaseReference.child(uniqueKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pd.dismiss();
                Toast.makeText(uploadPdf.this, "Pdf Uploaded.", Toast.LENGTH_SHORT).show();
                pdfTitle.setText("");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(uploadPdf.this, "Failed to upload pdf", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openGallery() {
        Intent intent=new Intent();
        intent.setType("*/");
        intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(intent,"Select Pdf file"),REQ);

    }



    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ && resultCode==RESULT_OK){
            pdfData=data.getData();
            if(pdfData.toString().startsWith("content://"))
            {
                Cursor cursor=null;
                try {
                    cursor=uploadPdf.this.getContentResolver().query(pdfData,null,null,null,null);
                    if(cursor!=null && cursor.moveToFirst()){
                        pdfName=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(pdfData.toString().startsWith("file://")){
                pdfName=new File(pdfData.toString()).getName();
            }
            pdfTextView.setText(pdfName);
        }
    }
}