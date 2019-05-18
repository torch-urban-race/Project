package com.example.torchapp.model;

import com.example.torchapp.database.DatabaseFacade;
import com.example.torchapp.map.DrawerMapActivity;
import com.example.torchapp.map.UIUtils;

public class CurrentUser {


    private Integer userID;
    private String username;
    private Integer maxCarryTime;
    private Double distanceTraveled;
    private Integer amountTorchesCreated;
    private Integer amountAchievements;


    private static volatile CurrentUser singleInstance;



    //private constructor.
    private CurrentUser(){

        //Prevent form the reflection api.
        if (singleInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static CurrentUser getInstance() {
        //Double check locking pattern
        if (singleInstance == null) { //Check for the first time

            synchronized (UIUtils.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (singleInstance == null) {
                    singleInstance = new CurrentUser();


                }
            }
        }

        return singleInstance;
    }


    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getMaxCarryTime() {
        return maxCarryTime;
    }

    public void setMaxCarryTime(Integer maxCarryTime) {
        this.maxCarryTime = maxCarryTime;
    }

    public Double getDistanceTraveled() {
        return distanceTraveled;
    }

    public void setDistanceTraveled(Double distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }

    public Integer getAmountTorchesCreated() {
        return amountTorchesCreated;
    }

    public void setAmountTorchesCreated(Integer amountTorchesCreated) {
        this.amountTorchesCreated = amountTorchesCreated;
    }

    public Integer getAmountAchievements() {
        return amountAchievements;
    }

    public void setAmountAchievements(Integer amountAchievements) {
        this.amountAchievements = amountAchievements;
    }

    public void updateCurrentUser(String userName, Integer maxCarryTime, Double distanceTraveled, Integer amountTorchesCreated, Integer amountAchievements){
        setUsername(userName);
        setMaxCarryTime(maxCarryTime);
        setDistanceTraveled(distanceTraveled);
        setAmountTorchesCreated(amountTorchesCreated);
        setAmountAchievements(amountAchievements);
    }

    public void requestUserUpdate(DrawerMapActivity drawerMapActivity, Integer userID){

        DatabaseFacade.getUserInformation(drawerMapActivity, userID);

    }



}
