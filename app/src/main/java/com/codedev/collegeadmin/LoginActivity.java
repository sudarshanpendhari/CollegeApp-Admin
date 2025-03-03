package com.codedev.collegeadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText edname;
    private EditText edpass;
    private Button button;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edname=findViewById(R.id.edname);
        edpass=findViewById(R.id.edpass);
        button=findViewById(R.id.button);
        auth=FirebaseAuth.getInstance();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=edname.getText().toString();
                String pass=edpass.getText().toString();
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(pass)){
                    Toast.makeText(LoginActivity.this,"enter email and password both", Toast.LENGTH_SHORT).show();
                }
                else {
                    login(name,pass);
                }
            }
        });
    }

    private void login(String name, String pass) {
        auth.signInWithEmailAndPassword(name,pass).addOnSuccessListener(LoginActivity.this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this,"login successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

                LoginActivity.this.finish();
            } });

        }

    @Override
    public void onStart() {



        super.onStart();
        FirebaseUser currentUser =auth.getCurrentUser();
        if(currentUser!=null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
    }
}