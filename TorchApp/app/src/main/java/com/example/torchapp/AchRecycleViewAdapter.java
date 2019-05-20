package com.example.torchapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.torchapp.model.Achievement;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AchRecycleViewAdapter extends RecyclerView.Adapter<AchRecycleViewAdapter.ViewHolder> {


    private List<Achievement> mData;
    private LayoutInflater mInflater;

    public AchRecycleViewAdapter(Context context, List<Achievement> data){
        this.mData = data;
        this.mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AchRecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.recycleview_achievement_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchRecycleViewAdapter.ViewHolder viewHolder, int i) {
        Achievement ach = mData.get(i);
        viewHolder.achTitle.setText(ach.getTitle());
        viewHolder.achDesc.setText(ach.getDescripttion());
        viewHolder.achDate.setText(ach.getDateObtained());
        viewHolder.achImg.setImageDrawable(ach.getIcon());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView achTitle;
        TextView achDesc;
        TextView achDate;
        CircleImageView achImg;

        ViewHolder(View itemView) {
            super(itemView);
            achTitle = itemView.findViewById(R.id.ach_title);
            achDesc = itemView.findViewById(R.id.ach_desc);
            achDate = itemView.findViewById(R.id.ach_date);
            achImg = itemView.findViewById(R.id.ach_img);
        }


    }

    // convenience method for getting data at click position
    Achievement getItem(int id) {
        return mData.get(id);
    }


}
