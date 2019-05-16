package com.example.torchapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class AchievementsFragment extends Fragment {
    ImageView friendInNeed;
    ImageView longDistanceRunner;
    ImageView makeItBrighter;
    ImageView olympicTorch;
    ImageView torchBearer;






    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_achievements, container, false);
        friendInNeed = (ImageView) view.findViewById(R.id.image_ach_friend_in_need);
        longDistanceRunner = (ImageView) view.findViewById(R.id.image_ach_long_distance_runner);
        makeItBrighter = ((ImageView) view.findViewById(R.id.image_ach_make_it_brighter));
        olympicTorch = (ImageView) view.findViewById(R.id.image_ach_olympic_torch);
        torchBearer= (ImageView) view.findViewById(R.id.image_ach_torch_bearer);

        friendInNeed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            toastMessage("Awarded for having another player carry your torch!");
                System.out.println("Achievement clicked");

            }
        });

        longDistanceRunner.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                toastMessage("Awarded for traveling 42 kilometers!");
                System.out.println("Achievement clicked");

            }
        });

        makeItBrighter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                toastMessage("Awarded for making 3 torches!");
                System.out.println("Achievement clicked");

            }
        });

        olympicTorch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                toastMessage("Awarded for having your torch travel 20,000 kilometers!");
                System.out.println("Achievement clicked");

            }
        });

        torchBearer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                toastMessage("Awarded for making your first torch!");
                System.out.println("Achievement clicked");

            }
        });


        return view;


    }


    private  void toastMessage (String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        System.out.println("print");

    }

}
