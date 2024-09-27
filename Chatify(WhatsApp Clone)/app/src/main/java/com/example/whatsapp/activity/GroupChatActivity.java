package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatsapp.R;
import com.example.whatsapp.adapters.ChatAdapter;
import com.example.whatsapp.databinding.ActivityGroupChatBinding;
import com.example.whatsapp.dialog.ViewGroupProfile;
import com.example.whatsapp.models.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding chatBinding;
    ImageButton btnArrow;
    TextView profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatBinding=ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(chatBinding.getRoot());
        getSupportActionBar().hide();

        chatBinding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        //chatBinding.chatUserName.setText("Best Friends Club");

        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final ArrayList<MessageModel> arrayList =new ArrayList<>();
        final String senderId= FirebaseAuth.getInstance().getUid();
        chatBinding.GroupName.setText("Best Friends Club");
        final ChatAdapter chatAdapter=new ChatAdapter(arrayList,this);

        chatBinding.chatRecycle.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        chatBinding.chatRecycle.setLayoutManager(layoutManager);

        database.getReference().child("groupChat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              arrayList.clear();
              for(DataSnapshot snapshot1 : snapshot.getChildren())
              {
                  MessageModel model1=snapshot1.getValue(MessageModel.class);
                  arrayList.add(model1);
              }
                chatBinding.chatRecycle.scrollToPosition(arrayList.size()-1);
              chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        chatBinding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message=chatBinding.edMessage.getText().toString();
                final MessageModel model=new MessageModel(senderId,message);

                model.setTimestamp(new Date().getTime());
                chatBinding.edMessage.setText("");

                database.getReference().child("groupChat")
                        .push().setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
            }
        });


        chatBinding.groupPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              new ViewGroupProfile(GroupChatActivity.this);
            }
        });

    }




}