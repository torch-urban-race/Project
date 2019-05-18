package com.example.torchapp.map;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.torchapp.R;
import com.example.torchapp.SaveSharedPreference;
import com.example.torchapp.database.DatabaseFacade;
import com.example.torchapp.model.CurrentUser;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class UIUtils {

    private static volatile UIUtils singleInstance;
    public static DrawerMapActivity drawerMapActivity;


    //private constructor.
    private UIUtils(){

        //Prevent form the reflection api.
        if (singleInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static UIUtils getInstance(DrawerMapActivity drawerMapActivitys) {
        //Double check locking pattern
        if (singleInstance == null) { //Check for the first time

            synchronized (UIUtils.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (singleInstance == null) {
                    singleInstance = new UIUtils();
                    drawerMapActivity = drawerMapActivitys;
                }
            }
        }

        return singleInstance;
    }

    public static UIUtils getInstanceIfExists() {
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
     * Disables a button
     */
    public void disableButton(Button button){
        button.setAlpha(.5f);
        button.setBackgroundColor(Color.RED);
        button.setClickable(false);
    }

    /**
     * Enables a button
     */
    public void enableButton(Button button){
        button.setAlpha(1);
        button.setBackgroundColor(Color.GREEN);
        button.setClickable(true);
    }




    /**
     * Attaching listeners to our menu button and possibly other buttons
     */

    public void addListenersOnButtons() {

        FloatingActionButton fab = drawerMapActivity.fab;
        Button pickupButton = drawerMapActivity.pickupButton;
        Button dropButton = drawerMapActivity.dropButton;
        ImageView menuImg = drawerMapActivity.menuImg;


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng pos = new LatLng(drawerMapActivity.mLastKnownLocation.getLatitude(), drawerMapActivity.mLastKnownLocation.getLongitude());

                DatabaseFacade.createTorch(drawerMapActivity, "Bork", pos.latitude, pos.longitude, SaveSharedPreference.getUserName(drawerMapActivity.getApplicationContext()), true);



                                //Temporary for debugging purpose
                Log.d(drawerMapActivity.TAG, "someone pressed the button!");

            }
        });


        pickupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Move the Selected Marker to the user's position
                //When the user picks up the marker make it their carriedMarker and enable the drop button
                if (drawerMapActivity.selectedMarker != null && drawerMapActivity.carriedMarker == null) {
                    drawerMapActivity.carriedMarker = drawerMapActivity.selectedMarker;
                    drawerMapActivity.selectedMarker = null;
                    drawerMapActivity.carriedMarker.setVisible(false);

                    initSnackbar();
                    showSnackbar();
                    initCountDownTimer();
                    //drawerMapActivity.startActivity(new Intent(drawerMapActivity,PopUp.class));


                    disableButton(drawerMapActivity.pickupButton);
                    enableButton(drawerMapActivity.dropButton);
                    drawerMapActivity.dropButton.setVisibility(View.VISIBLE);

                } else {
                }
            }
        });

        dropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Place the marker at the user's current location set the carriedMarker to null and update the carriedMarker's position

                LatLng userPosition = new LatLng(drawerMapActivity.mLastKnownLocation.getLatitude(), drawerMapActivity.mLastKnownLocation.getLongitude());
                drawerMapActivity.carriedMarker.setPosition(userPosition);
                drawerMapActivity.carriedMarker.setVisible(true);
                drawerMapActivity.carriedMarker = null;

                disableButton(drawerMapActivity.dropButton);
                drawerMapActivity.dropButton.setVisibility(View.INVISIBLE);

                dismissSnackbar();
                stopCountDownTimer();
            }
        });


        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerMapActivity.drawer.openDrawer(GravityCompat.START);
            }
        });


    }


    /**
     * Configure snackbar
     */
    public void initSnackbar(){


        drawerMapActivity.snackbar = Snackbar.make(drawerMapActivity.findViewById(R.id.map), "Start", Snackbar.LENGTH_INDEFINITE);
        View view = drawerMapActivity.snackbar.getView();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        params.topMargin = 80;
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
    }

    public void showSnackbar(){
        drawerMapActivity.snackbar.show();
    }

    public void dismissSnackbar(){
        drawerMapActivity.snackbar.dismiss();
        drawerMapActivity.snackbar = null;
    }


    public void stopCountDownTimer(){
        drawerMapActivity.countDownTimer.cancel();
    }

    public void initCountDownTimer(){
        drawerMapActivity.timeLeftInMilliseconds = CurrentUser.getInstance().getMaxCarryTime() * 60000;

        drawerMapActivity.countDownTimer = new CountDownTimer(drawerMapActivity.timeLeftInMilliseconds, 1000) {
            @Override
            public void onTick(long l) {

                drawerMapActivity.timeLeftInMilliseconds = l;

                setSnackbarTime();

            }

            @Override
            public void onFinish() {

              drawerMapActivity.dropButton.callOnClick();

            }
        }.start();
    }

    public void setSnackbarTime(){
        int minutes = (int) drawerMapActivity.timeLeftInMilliseconds / 60000;
        int seconds = (int) (drawerMapActivity.timeLeftInMilliseconds % 60000) / 1000;


        String timeLeft = "" + minutes + ":";
        if(seconds < 10) timeLeft += "0";

        timeLeft+= "" + seconds;

        drawerMapActivity.snackbar.setText(timeLeft);
    }

}