package com.example.whatsapp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.whatsapp.R;
import com.example.whatsapp.activity.GroupChatActivity;
import com.example.whatsapp.activity.MainActivity;


public class ViewGroupProfile {
    private Context context;
    Dialog dialog;

    private TextView profileName;
    private ImageButton btnBack, btnChat,btnCall,btnVideoCall;

    public ViewGroupProfile(Context context)
    {
        this.context=context;
        dialog =new Dialog(context);
        dialog.setContentView(R.layout.group_profile_dialog);
        dialog.show();

        clickListeners();


    }

    public void clickListeners() {

        profileName = dialog.findViewById(R.id.profileName);

        btnBack = dialog.findViewById(R.id.btnArrow);

        btnChat = dialog.findViewById(R.id.chatIcon);
        btnCall = dialog.findViewById(R.id.callIcon);
        btnVideoCall = dialog.findViewById(R.id.videoCallIcon);

        profileName.setText("Best Friends Club");



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, GroupChatActivity.class));
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


    }
}
