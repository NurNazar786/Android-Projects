package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.databinding.ActivitySignUpBinding;
import com.example.whatsapp.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding signUpBinding;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase  database;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding= ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());
        getSupportActionBar().hide();

        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        progressDialog=new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Please Wait....");
        progressDialog.setMessage("We're creating your account.");
        signUpBinding.regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                firebaseAuth.createUserWithEmailAndPassword
                        (signUpBinding.regEmail.getText().toString(),signUpBinding.regPass.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();

                              if(task.isSuccessful()){
                                  Users users=new Users(signUpBinding.regUsername.getText().toString(),
                                                        signUpBinding.regEmail.getText().toString(),
                                                        signUpBinding.regPass.getText().toString());

                                  String id= Objects.requireNonNull(task.getResult().getUser()).getUid();
                                  database.getReference().child("Users").child(id).setValue(users);
                                  Toast.makeText(SignUpActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                                  startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                                  finish();
                              }
                              else {
                                  Toast.makeText(SignUpActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                              }
                            }
                        });
            }
        });

        signUpBinding.regFacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUpActivity.this, "Sign Up process by Facebook", Toast.LENGTH_SHORT).show();
            }
        });

        signUpBinding.regGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SignUpActivity.this, "Sign Up process by Gmail", Toast.LENGTH_SHORT).show();

            }
        });

        signUpBinding.loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }
}