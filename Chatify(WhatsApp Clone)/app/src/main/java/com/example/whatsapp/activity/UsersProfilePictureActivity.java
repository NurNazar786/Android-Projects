package com.example.whatsapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatsapp.R;
import com.example.whatsapp.databinding.ActivityUsersProfilePictureBinding;
import com.example.whatsapp.fragments.ChatsFragment;
import com.squareup.picasso.Picasso;

public class UsersProfilePictureActivity extends AppCompatActivity {

    ActivityUsersProfilePictureBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityUsersProfilePictureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        String profilePic=getIntent().getStringExtra("profilePic");
        Picasso.get().load(profilePic).placeholder(R.drawable.user).into(binding.usersPic);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UsersProfilePictureActivity.this, MainActivity.class));
            }
        });
    }
}