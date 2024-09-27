package com.example.whatsapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.whatsapp.R;
import com.example.whatsapp.adapters.CallAdapter;
import com.example.whatsapp.adapters.UsersAdapter;
import com.example.whatsapp.databinding.FragmentCallsBinding;
import com.example.whatsapp.databinding.FragmentChatsBinding;
import com.example.whatsapp.models.CallList;

import java.util.ArrayList;
import java.util.List;


public class CallsFragment extends Fragment {


    FragmentCallsBinding binding;
    private List<CallList> list;


    public CallsFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view=inflater.inflate(R.layout.fragment_calls, container, false);
        binding= FragmentCallsBinding.inflate(inflater,container,false);

         list=new ArrayList<>();

        CallAdapter adapter=new CallAdapter(list,getContext());
        binding.callRecyclerView.setAdapter(adapter);
        binding.callRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        list.add(new CallList("video","md nurnazar",""+R.drawable.user,"missed","20/05/2023, 8:00 pm"));
        list.add(new CallList("audio","Nazar",""+R.drawable.user,"outgoing","21/05/2023, 2:20 am"));
        list.add(new CallList("audio","Kashif",""+R.drawable.user,"missed","22/05/2023, 12:20 pm"));
        list.add(new CallList("video","Rakesh",""+R.drawable.user,"outgoing","25/09/2023 ,1:30 pm"));
        list.add(new CallList("video","Girish",""+R.drawable.user,"missed","27/10/2023, 8:10 am"));
        list.add(new CallList("audio","Amir",""+R.drawable.user,"what","25/09/2023, 8:50 pm"));

        list.add(new CallList("video","Aman",""+R.drawable.user,"missed","20/05/2023, 7:00 am"));
        list.add(new CallList("video","Ravi",""+R.drawable.user,"outgoing","21/05/2023, 6:20 pm"));
        list.add(new CallList("video","Saquib",""+R.drawable.user,"outgoing","22/05/2023, 8:20 am"));
        list.add(new CallList("audio","Ibrahim",""+R.drawable.user,"missed","25/09/2023, 9:00 pm"));
        list.add(new CallList("video","Noor",""+R.drawable.user,"missed","27/10/2023, 9:50 pm"));
        list.add(new CallList("audio","Maulik",""+R.drawable.user,"what","25/09/2023, 11:20 am"));

        list.add(new CallList("video","Janvi",""+R.drawable.user,"outgoing","20/05/2023, 3:00 pm"));
        list.add(new CallList("audio","Anish",""+R.drawable.user,"outgoing","21/05/2023, 7:20 am"));
        list.add(new CallList("video","Kashif",""+R.drawable.user,"missed","22/05/2023, 8:10 pm"));
        list.add(new CallList("audio","Nazar",""+R.drawable.user,"what","25/09/2023, 5:00 am"));
        list.add(new CallList("video","Rakesh",""+R.drawable.user,"missed","27/10/2023, 3:10 pm"));
        list.add(new CallList("audio","Amir",""+R.drawable.user,"what","25/09/2023, 8:30 am"));

        binding.callRecyclerView.setAdapter(new CallAdapter(list,getContext()));
        adapter.notifyDataSetChanged();

        return binding.getRoot();
    }





}