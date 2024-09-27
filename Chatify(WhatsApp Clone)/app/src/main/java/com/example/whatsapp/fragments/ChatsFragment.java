package com.example.whatsapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.whatsapp.R;
import com.example.whatsapp.adapters.UsersAdapter;
import com.example.whatsapp.databinding.FragmentChatsBinding;
import com.example.whatsapp.models.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class  ChatsFragment extends Fragment {

  private FragmentChatsBinding chatsBinding;

  private FirebaseDatabase database;
  private   ArrayList<Users> arrayList ;

    public ChatsFragment() {
        // Required empty public constructor
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chatsBinding=FragmentChatsBinding.inflate(inflater,container,false);

         arrayList=new ArrayList<>();
         database=FirebaseDatabase.getInstance();
        UsersAdapter adapter=new UsersAdapter(arrayList,getContext());
        chatsBinding.recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        chatsBinding.recyclerView.setLayoutManager(layoutManager);


        chatsBinding.progressbar.setVisibility(View.VISIBLE);
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
               for(DataSnapshot dataSnapshot:snapshot.getChildren())
               {
                   Users users=dataSnapshot.getValue(Users.class);
                   users.setUserId(dataSnapshot.getKey());
                   chatsBinding.progressbar.setVisibility(View.GONE);
                   arrayList.add(users);
               }
               adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return chatsBinding.getRoot();
    }
}