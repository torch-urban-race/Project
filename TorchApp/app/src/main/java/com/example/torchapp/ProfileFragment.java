package com.example.torchapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.torchapp.model.CurrentUser;

import org.w3c.dom.Text;

public class ProfileFragment extends Fragment {


    private TextView usernameTextView;
    private TextView distanceTraveledTextView;
    private TextView torchesCreatedTextView;
    private TextView achievementsObtainedTextView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        usernameTextView = (TextView) v.findViewById(R.id.user_profile_name);
        distanceTraveledTextView = (TextView) v.findViewById(R.id.user_profile_distance);
        torchesCreatedTextView= (TextView) v.findViewById(R.id.user_profile_torches);
        achievementsObtainedTextView = (TextView) v.findViewById(R.id.user_profile_achievements);


        usernameTextView.setText(""+CurrentUser.getInstance().getUsername());
        distanceTraveledTextView.setText(""+CurrentUser.getInstance().getDistanceTraveled() + " km");
        torchesCreatedTextView.setText(""+CurrentUser.getInstance().getAmountTorchesCreated());
        achievementsObtainedTextView.setText(""+CurrentUser.getInstance().getAmountAchievements());


        return v;
    }
}
