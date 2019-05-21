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


        //Truth table
        Boolean[] tnfDebug = new Boolean[]{true,false,true,false,true};

        String[] achievementNames = new String[]{"A Friend in Need", "Long Distance Runner", "Make It Brighter", "Olympic Torch", "Torch Bearer"};
        String[] achievementDescriptions = new String[]{"Obtained for having another player carry a torch you made.", "Obtained for traveling over 42 km with a torch.", "Obtained for creating 3 torches.", "Obtained for having a torch you've made travel 40,000 km.", "Obtained for carrying your first torch."};
        String[] achievementDates = new String[]{"2019-05-22", "2019-05-19","2019-05-12", "2019-05-25", "2019-05-03"};
        Drawable[] achievementIcons = new Drawable[]{ContextCompat.getDrawable(getActivity(), R.drawable.friend_in_need_ach), ContextCompat.getDrawable(getActivity(), R.drawable.long_distance_runner_ach),
        ContextCompat.getDrawable(getActivity(), R.drawable.make_it_brighter_ach),ContextCompat.getDrawable(getActivity(), R.drawable.olympic_torch_ach),ContextCompat.getDrawable(getActivity(), R.drawable.torch_bearer_achievement)};

        //achievementIcons =iconSorting(achievementIcons,tnfDebug);





        RecyclerView recyclerView = view.findViewById(R.id.achievements_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        achRecycleViewAdapter = new AchRecycleViewAdapter(getActivity(), achievements);
        recyclerView.setAdapter(achRecycleViewAdapter);



        return view;


    }


    private Drawable[] iconSorting(Drawable[] icon, Boolean[] trueOrFalse){

        for(int i=0; i<icon.length; i++){
            if(!trueOrFalse[i]){
                icon[i]=ContextCompat.getDrawable(getActivity(), R.drawable.blaack);
            }


        }


        return icon;
    }




}
