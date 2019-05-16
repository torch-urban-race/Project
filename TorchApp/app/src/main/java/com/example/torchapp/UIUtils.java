package com.example.torchapp;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.torchapp.database.DatabaseFacade;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class UIUtils {

    private static volatile UIUtils singleInstance;
    private static DrawerMapActivity drawerMapActivity;

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
                //TODO: ADD marker legit way do not place torch unless database accepted it
                LatLng pos = new LatLng(drawerMapActivity.mLastKnownLocation.getLatitude(), drawerMapActivity.mLastKnownLocation.getLongitude());

                drawerMapActivity.mMap.addMarker(new MarkerOptions().position(pos)
                        .title("Bork")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ttorch_icon)));

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
            }
        });


        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerMapActivity.drawer.openDrawer(GravityCompat.START);
            }
        });


    }


}