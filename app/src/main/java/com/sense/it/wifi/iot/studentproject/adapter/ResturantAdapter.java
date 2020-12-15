package com.sense.it.wifi.iot.studentproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sense.it.wifi.iot.studentproject.Common;
import com.sense.it.wifi.iot.studentproject.R;
import com.sense.it.wifi.iot.studentproject.ViewActivity;
import com.sense.it.wifi.iot.studentproject.model.ResturantModel;

import java.util.List;


public class ResturantAdapter extends RecyclerView.Adapter<ResturantAdapter.MyViewHolder> {
    Context mContext;
    List<ResturantModel> resturantModelList;


    public ResturantAdapter(Context mContext, List<ResturantModel> resturantModelList) {
        this.mContext = mContext;
        this.resturantModelList = resturantModelList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_rc_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ResturantModel model = resturantModelList.get(position);
        Glide.with(mContext).load(model.getImage_url()).into(holder.iv_icon);
        holder.iv_title.setText(model.getName());
        holder.iv_desc.setText(model.getAddress());


    }

    @Override
    public int getItemCount() {
        return resturantModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView iv_icon, iv_share;
        public TextView iv_title, iv_desc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv_icon);
            iv_title = itemView.findViewById(R.id.iv_title);
            iv_desc = itemView.findViewById(R.id.iv_discription);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Common.currentResturant = resturantModelList.get(position);
                mContext.startActivity(new Intent(mContext, ViewActivity.class));
            }

        }
    }
}
