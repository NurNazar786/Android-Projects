package com.example.whatsapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.adapters.ChatAdapter;
import com.example.whatsapp.databinding.ActivityChatBinding;
import com.example.whatsapp.models.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding chatBinding;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    String email;

    private boolean isActionShown=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatBinding=ActivityChatBinding.inflate(getLayoutInflater());

        setContentView(chatBinding.getRoot());
        getSupportActionBar().hide();

        database=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        chatBinding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

       final String senderId=firebaseAuth.getUid();

        String receiverId=getIntent().getStringExtra("userId");
        String userName=getIntent().getStringExtra("userName");
        String profilePic=getIntent().getStringExtra("profilePic");
        email=getIntent().getStringExtra("email");

        chatBinding.chatUserName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.user).into(chatBinding.userImg);

        final ArrayList<MessageModel> messageModels=new ArrayList<>();

        final ChatAdapter chatAdapter=new ChatAdapter(messageModels,this,receiverId);
        chatBinding.chatRecycle.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        chatBinding.chatRecycle.setLayoutManager(layoutManager);

        final String senderRoom=senderId+receiverId;
        final String receiverRoom=receiverId+senderId;

        database.getReference().child("chats")
                        .child(senderRoom).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageModels.clear();

                        for(DataSnapshot snapshot1:snapshot.getChildren())
                        {
                            MessageModel  models=snapshot1.getValue(MessageModel.class);
                            models.setMessageId(snapshot1.getKey());
                            messageModels.add(models);
                        }
                        chatBinding.chatRecycle.scrollToPosition(messageModels.size()-1);
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        chatBinding.edMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(chatBinding.edMessage.getText().toString()))
                {
                    chatBinding.sendMessage.setImageDrawable(getDrawable(R.drawable.ic_baseline_keyboard_voice_24));
                }
                else
                {
                    chatBinding.sendMessage.setImageDrawable(getDrawable(R.drawable.ic_baseline_send_24));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        chatBinding.sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message= chatBinding.edMessage.getText().toString();
                final MessageModel model=new MessageModel(senderId,message);
                model.setTimestamp(new Date().getTime());
                chatBinding.edMessage.setText("");

                database.getReference().child("chats").child(senderRoom)
                                       .push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                               database.getReference().child("chats")
                                                      .child(receiverRoom)
                                                      .push()
                                                      .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                           @Override
                                           public void onSuccess(Void unused) {

                                           }
                                       });
                            }
                        });
            }
        });

        chatBinding.userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this,UserProfileActivity.class));
            }
        });

        chatBinding.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ChatActivity.this, UserProfileActivity.class);
                intent.putExtra("userName",userName);
                intent.putExtra("profilePic",profilePic);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });

        chatBinding.fileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isActionShown)
                {
                   chatBinding.cardBtn.setVisibility(View.GONE);
                   isActionShown=false;
                }
                else
                {
                    chatBinding.cardBtn.setVisibility(View.VISIBLE);
                    isActionShown=true;

                }

            }
        });
    }
}