package com.codedev.collegeadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.codedev.collegeadmin.faculty.updateFaculty;
import com.codedev.collegeadmin.notice.DeleteNotice;
import com.codedev.collegeadmin.notice.uploadNotice;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    CardView addNotice,addGalleryImage,addPdf,faculty,deleteNotice,addVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNotice = findViewById(R.id.addNotice);
        addVideo=findViewById(R.id.addVideo);
        addGalleryImage=findViewById(R.id.addGalleryImage);
        addPdf=findViewById(R.id.addPdf);
        faculty=findViewById(R.id.faculty);
        deleteNotice=findViewById(R.id.deleteNoticeBtn);

        addNotice.setOnClickListener(this);
        addGalleryImage.setOnClickListener(this);
        addPdf.setOnClickListener(this);
        addVideo.setOnClickListener(this);
        faculty.setOnClickListener(this);
        deleteNotice.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId())
        {
            case R.id.addNotice:
                intent=new Intent(MainActivity.this, uploadNotice.class);
                startActivity(intent);
                break;

            case R.id.addGalleryImage:
                intent=new Intent(MainActivity.this,UploadImage.class);
                startActivity(intent);
                break;
            case R.id.addPdf:
                intent=new Intent(MainActivity.this,uploadPdf.class);
                startActivity(intent);
                break;
            case R.id.faculty:
                intent=new Intent(MainActivity.this, updateFaculty.class);
                startActivity(intent);
                break;
            case R.id.deleteNoticeBtn:
                intent=new Intent(MainActivity.this, DeleteNotice.class);
                startActivity(intent);
                break;
            case R.id.addVideo:
                intent=new Intent(MainActivity.this,addVidActivity.class);
                startActivity(intent);
                break;

        }
    }
}