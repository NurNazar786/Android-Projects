package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.adapters.FragmentAdapter;
import com.example.whatsapp.chat_gpt.activity.ChatWithExpertActivity;
import com.example.whatsapp.chat_gpt.activity.GenerateImageActivity;
import com.example.whatsapp.databinding.ActivityLoginBinding;
import com.example.whatsapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    FirebaseAuth firebaseAuth;
    private static final int CAMERA_REQUEST = 11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        getSupportActionBar().setElevation(0);

        firebaseAuth=FirebaseAuth.getInstance();
        mainBinding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        mainBinding.tabLayout.setupWithViewPager(mainBinding.viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         switch (item.getItemId())
         {

             case R.id.camera:
                 openCamera();
                 break;

             case R.id.search:
                 Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();
                 break;

             case R.id.group:
                 Intent intent=new Intent(MainActivity.this,GroupChatActivity.class);
                 startActivity(intent);
                 break;

             case R.id.bot:
                 startActivity(new Intent(getApplicationContext(),ChatWithExpertActivity.class));
                 break;

             case R.id.chatGpt:
                 startActivity(new Intent(getApplicationContext(), GenerateImageActivity.class));
                 break;

             case R.id.setting:
                 startActivity(new Intent(getApplicationContext(),SettingActivity.class));
                 break;

             case R.id.back:
                 mainBinding.mainLayout.setBackgroundResource(R.color.dark_back);
                 break;

             case R.id.normal:
                 mainBinding.mainLayout.setBackgroundResource(R.color.chat_background);
                 break;

             case R.id.white:
                 mainBinding.mainLayout.setBackgroundResource(R.color.white);
                 break;

             case R.id.logout:
                 logOut();

                 break;


         }
        return super.onOptionsItemSelected(item);
    }

   private void logOut()
    {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure want to be logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    }


                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void openCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityIfNeeded(intent, CAMERA_REQUEST);
    }

}