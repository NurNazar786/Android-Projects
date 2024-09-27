package com.example.whatsapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.activity.ChatActivity;
import com.example.whatsapp.activity.MainActivity;
import com.example.whatsapp.activity.ProfileViewActivity;
import com.example.whatsapp.activity.UserProfileActivity;
import com.example.whatsapp.activity.UsersProfilePictureActivity;
import com.example.whatsapp.models.Users;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class DialogViewProfile {

    private Context context;
    private Users users;
    Dialog dialog;

    private ImageView profilePic;
    private TextView profileName;
    private ImageButton btnBack, btnChat,btnCall,btnVideoCall,btnInfo;

    public DialogViewProfile(Context context,Users users)
    {
        this.context=context;
        this.users=users;
       dialog =new Dialog(context);
        dialog.setContentView(R.layout.dialog_profile_popup);
        dialog.show();

        clickListeners();


    }

    public void clickListeners() {

        profileName=dialog.findViewById(R.id.profileName);
        profilePic=dialog.findViewById(R.id.profilePic);
        btnBack=dialog.findViewById(R.id.btnArrow);

        btnChat=dialog.findViewById(R.id.chatIcon);
        btnCall=dialog.findViewById(R.id.callIcon);
        btnVideoCall=dialog.findViewById(R.id.videoCallIcon);
        btnInfo=dialog.findViewById(R.id.infoIcon);

        Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.user).into(profilePic);
        profileName.setText(users.getUserName());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MainActivity.class));
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Chat", Toast.LENGTH_SHORT).show();
            }
        });
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Call", Toast.LENGTH_SHORT).show();
            }
        });
        btnVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Video Call", Toast.LENGTH_SHORT).show();
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UserProfileActivity.class);
                intent.putExtra("userName",users.getUserName());
                intent.putExtra("profilePic",users.getProfilePic());
                intent.putExtra("email",users.getEmail());
                context.startActivity(intent);
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, UsersProfilePictureActivity.class);
                intent.putExtra("profilePic",users.getProfilePic());
                context.startActivity(intent);
            }
        });

    }
}
