package com.example.whatsapp.chat_gpt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.example.whatsapp.adapters.ChatAdapter;
import com.example.whatsapp.chat_gpt.models.MessageModel;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter {

    private final ArrayList<MessageModel> arrayList;
    private final Context context;
    public MyAdapter(ArrayList<MessageModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view;
       switch (viewType)
       {
           case 0:
               view= LayoutInflater.from(context).inflate(R.layout.user_msg_rv_item,parent,false);
           return new UserViewHolder(view);

           case 1:
               view = LayoutInflater.from(context).inflate(R.layout.chatgpt_msg_rv_item,parent, false);
               return new ChatGptHolder(view);
       }
       return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messageModel=arrayList.get(position);
       switch (messageModel.getSentBy())
       {
           case "user":
           ((UserViewHolder)holder).userText.setText(messageModel.getMessage());
           break;
           case "bot":
               ((ChatGptHolder)holder).chatGptText.setText(messageModel.getMessage());
               break;
       }

    }

    @Override
    public int getItemViewType(int position) {
       switch (arrayList.get(position).getSentBy())
       {
           case "user":
               return 0;
           case "bot":
               return 1;
           default:
               return -1;
       }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userText;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            userText=itemView.findViewById(R.id.userMsg);
        }
    }

    public static class ChatGptHolder extends RecyclerView.ViewHolder {
        TextView chatGptText;
        public ChatGptHolder(@NonNull View itemView) {
            super(itemView);

            chatGptText=itemView.findViewById(R.id.chatGptMsg);
        }
    }
}
