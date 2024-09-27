package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.databinding.ActivitySettingBinding;
import com.example.whatsapp.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding settingBinding;
    FirebaseStorage storage;
   FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingBinding=ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(settingBinding.getRoot());
        getSupportActionBar().hide();

        storage=FirebaseStorage.getInstance();
       auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        settingBinding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        settingBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=settingBinding.edName.getText().toString();
                String about=settingBinding.about.getText().toString();

                HashMap<String ,Object> objectHashMap = new HashMap<>();
                objectHashMap.put("userName",name);
                objectHashMap.put("about",about);

                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .updateChildren(objectHashMap);
                Toast.makeText(SettingActivity.this, "Profile Updated..", Toast.LENGTH_SHORT).show();
            }
        });

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Users users=snapshot.getValue(Users.class);
                                Picasso.get().load(users.getProfilePic())
                                        .placeholder(R.drawable.user)
                                        .into(settingBinding.profileImg);

                                settingBinding.edName.setText(users.getUserName());
                                settingBinding.about.setText(users.getAbout());


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        settingBinding.addProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityIfNeeded(intent,30);

            }
        });

        settingBinding.profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SettingActivity.this,ProfileViewActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData()!=null)
        {
            Uri  uri=data.getData();
            settingBinding.profileImg.setImageURI(uri);

            final StorageReference reference=storage.getReference().child("profile_image")
                                   .child(FirebaseAuth.getInstance().getUid());

            reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                      @Override
                      public void onSuccess(Uri uri) {

                          database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                  .child("profilePic").setValue(uri.toString());
                      }
                  });
                }
            });

        }
    }
}