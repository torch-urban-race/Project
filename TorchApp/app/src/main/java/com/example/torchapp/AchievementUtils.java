package com.example.torchapp;

import android.support.v4.content.ContextCompat;

import com.example.torchapp.database.DatabaseFacade;
import com.example.torchapp.map.DrawerMapActivity;
import com.example.torchapp.map.UIUtils;
import com.example.torchapp.model.Achievement;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Map;

public class AchievementUtils {

    private static volatile AchievementUtils singleInstance;
    public static DrawerMapActivity drawerMapActivity;
    private static ArrayList<Achievement> systemAchievements;


    //private constructor.
    private AchievementUtils(){

        //Prevent form the reflection api.
        if (singleInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static AchievementUtils getInstance(DrawerMapActivity drawerMapActivitys) {
        //Double check locking pattern
        if (singleInstance == null) { //Check for the first time

            synchronized (UIUtils.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (singleInstance == null) {
                    singleInstance = new AchievementUtils();
                    drawerMapActivity = drawerMapActivitys;
                    systemAchievements = new ArrayList<Achievement>();
                }
            }
        }

        return singleInstance;
    }

    public static AchievementUtils getInstanceIfExists() {
        //Double check locking pattern
        if (singleInstance != null) { //Check for the first time

            synchronized (UIUtils.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (singleInstance != null) {
                    return singleInstance;
                } else{
                    return null;
                }
            }
        }

        return null;
    }


    /**
     * Assigns date to some achievement that the user has obtained
     */

    /**
     * Populates the achievements in the system
     *
     */
    /**
     * Add markers from database into map
     */
    public void handleAchievements(){

        for(int i = 1; i <= 13; i++){
            DatabaseFacade.getAchievementInformation(i);
            System.out.println("Request this  achievement id: " + i);
        }

    }


    public void createAchievement(String achName, String achDesc, String achReward, Integer id){
       Achievement achievement = new Achievement();
       achievement.setTitle(achName);
       achievement.setDescripttion(achDesc);
       achievement.setReward("Reward: " +achReward);
       achievement.setId(id);

       if(id < 6){
           achievement.setIcon(ContextCompat.getDrawable(drawerMapActivity.getApplicationContext(), R.drawable.friend_in_need_ach));
       } else if (id < 11) {
           achievement.setIcon(ContextCompat.getDrawable(drawerMapActivity.getApplicationContext(), R.drawable.make_it_brighter_ach));
       } else if (id == 11){
           achievement.setIcon(ContextCompat.getDrawable(drawerMapActivity.getApplicationContext(), R.drawable.torch_bearer_achievement));
       } else if (id == 12){
           achievement.setIcon(ContextCompat.getDrawable(drawerMapActivity.getApplicationContext(), R.drawable.olympic_torch_ach));
       } else {
           achievement.setIcon(ContextCompat.getDrawable(drawerMapActivity.getApplicationContext(), R.drawable.long_distance_runner_ach));
       }


       systemAchievements.add(achievement);

    }



    public static ArrayList<Achievement> getSystemAchievements() {

        return systemAchievements;
    }
}
