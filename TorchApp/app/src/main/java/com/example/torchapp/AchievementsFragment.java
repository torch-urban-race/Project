package com.example.torchapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class AchievementsFragment extends Fragment {
    ImageButton ach1;





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_achievements, container, false);
        ach1 = (ImageButton) view.findViewById(R.id.image_button_android0);

        ach1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            toastMessage("Hahaha");

            }
        });


        return inflater.inflate(R.layout.fragment_achievements, container, false);


    }


    private  void toastMessage (String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        System.out.println("print");

    }
}
