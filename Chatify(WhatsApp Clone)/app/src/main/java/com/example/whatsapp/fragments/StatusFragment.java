package com.example.whatsapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsapp.R;
import com.example.whatsapp.databinding.FragmentChatsBinding;
import com.example.whatsapp.databinding.FragmentStatusBinding;
import com.example.whatsapp.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class StatusFragment extends Fragment {

    private FragmentStatusBinding statusBinding;
    private FirebaseDatabase database;

    public StatusFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        statusBinding= FragmentStatusBinding.inflate(inflater, container, false);

        database= FirebaseDatabase.getInstance();
        getProfile();
        return statusBinding.getRoot();
    }

    private void getProfile() {

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users=snapshot.getValue(Users.class);
                        Picasso.get().load(users.getProfilePic())
                                .placeholder(R.drawable.user)
                                .into(statusBinding.statusUserImg);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}