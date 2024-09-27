package com.example.whatsapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.example.whatsapp.activity.ChatActivity;
import com.example.whatsapp.dialog.DialogViewProfile;
import com.example.whatsapp.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    ArrayList<Users> arrayList;
    Context context;

    public UsersAdapter(ArrayList<Users> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_show_users,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
     Users user=arrayList.get(position);
        Picasso.get().load(user.getProfilePic()).placeholder(R.drawable.user).into(holder.image);
        holder.name.setText(user.getUserName());

        FirebaseDatabase.getInstance().getReference().child("chats")
                        .child(FirebaseAuth.getInstance().getUid() + user.getUserId())
                        .orderByChild("timestamp")
                        .limitToLast(1)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChildren())
                                {
                                    for (DataSnapshot dataSnapshot:snapshot.getChildren())
                                    {
                                        holder.lastMessage.setText(dataSnapshot.child("message").getValue().toString());
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("userId",user.getUserId());
                intent.putExtra("profilePic",user.getProfilePic());
                intent.putExtra("userName",user.getUserName());
                intent.putExtra("email",user.getEmail());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure want to be deleted this User?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "User Deleted Process..", Toast.LENGTH_SHORT).show();
                            }


                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();


                return false;
            }
        });
        



        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new DialogViewProfile(context,user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView name,lastMessage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image=itemView.findViewById(R.id.userImg);
            name=itemView.findViewById(R.id.userName);
            lastMessage=itemView.findViewById(R.id.lastMessage);
        }
    }
}
