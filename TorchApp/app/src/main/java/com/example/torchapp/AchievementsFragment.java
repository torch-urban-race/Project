package com.example.torchapp;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.torchapp.map.DrawerMapActivity;
import com.example.torchapp.model.Achievement;

import java.util.ArrayList;

public class AchievementsFragment extends Fragment {
    AchRecycleViewAdapter achRecycleViewAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_achievements, container, false);
        ArrayList<Achievement> achievements = AchievementUtils.getInstance((DrawerMapActivity) getActivity()).getSystemAchievements();


        RecyclerView recyclerView = view.findViewById(R.id.achievements_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        achRecycleViewAdapter = new AchRecycleViewAdapter(getActivity(), achievements);
        recyclerView.setAdapter(achRecycleViewAdapter);


        return view;


    }


    private Drawable[] iconSorting(Drawable[] icon, Boolean[] trueOrFalse ){

        for(int i=0; i<icon.length; i++){
            if(!trueOrFalse[i]){
                icon[i]=ContextCompat.getDrawable(getActivity(), R.drawable.blaack);

            }
      }

        return icon;
    }

    private String[] getDatesComplted(Boolean[] tnfDebug){
        String[] dates = new String[tnfDebug.length];
        for(int i=0; i<tnfDebug.length; i++){
            if(tnfDebug[i]){
                dates[i]="testdate";
                //TODO bring date from sever
            }else{
                dates[i]=" ";
            }

        }
        return dates;

    }


}
