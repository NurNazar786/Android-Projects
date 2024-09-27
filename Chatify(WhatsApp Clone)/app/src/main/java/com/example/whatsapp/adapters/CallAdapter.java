package com.example.whatsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.example.whatsapp.models.CallList;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.MyHolder> {

    private List<CallList> list;
    private Context context;
    public CallAdapter(List<CallList> list,Context context)
    {
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_calls_layout,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        CallList callList=list.get(position);
        holder.callUserName.setText(callList.getUserName());
        holder.callTime.setText(callList.getDate());

        if(callList.getCallType().equals("missed"))
        {
            holder.callType.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_downward_24));
            holder.callType.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_red_dark));
        }
        else if(callList.getCallType().equals("outgoing"))
        {
            holder.callType.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_outward_24));
            holder.callType.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_green_dark));
        }
        else
        {
            holder.callType.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_arrow_outward_24));
            holder.callType.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_green_dark));
        }

        Picasso.get().load(callList.getUserProfile()).placeholder(R.drawable.user).into(holder.callProfile);

        if(callList.getCalling().equals("video"))
        {
            holder.calling.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_videocam_24));
        }
        else {
            holder.calling.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_call_24));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class MyHolder extends RecyclerView.ViewHolder {

        private CircleImageView callProfile;
        private TextView callUserName,callTime;
        private ImageView callType,calling;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            callProfile=itemView.findViewById(R.id.callUserImg);
            callUserName=itemView.findViewById(R.id.callUserName);
            callTime=itemView.findViewById(R.id.callTime);
            callType=itemView.findViewById(R.id.callType);
            calling=itemView.findViewById(R.id.calling);
        }
    }

}
