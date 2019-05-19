package com.example.torchapp.map;


import android.annotation.SuppressLint;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.torchapp.R;
import com.example.torchapp.database.DatabaseFacade;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

    private static volatile MapUtils singleInstance;
    private static DrawerMapActivity drawerMapActivity;
    private static  Map<Integer, Marker> systemMarkers;


    Marker mainMarker;
    private int markerCount;



    //Circle Color
    final int circleFill = 0x3322e0f0;
    private int obtainedTorches = 1;

    //private constructor.
    private MapUtils(){

        //Prevent form the reflection api.
        if (singleInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static MapUtils getInstance(DrawerMapActivity drawerMapActivitys) {

        //Double check locking pattern
        if (singleInstance == null) { //Check for the first time

            synchronized (MapUtils.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (singleInstance == null){
                    singleInstance = new MapUtils();
                    drawerMapActivity = drawerMapActivitys;
                    systemMarkers = new HashMap<Integer,Marker>(100);

                }

            }
        }

        return singleInstance;
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    public void updateLocationUI() {

        GoogleMap mMap = drawerMapActivity.mMap;
        boolean mLocationPermissionGranted = drawerMapActivity.mLocationPermissionGranted;
        Location mLastKnownLocation = drawerMapActivity.mLastKnownLocation;

        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.getUiSettings().setCompassEnabled(false);
                mMap.getUiSettings().setMapToolbarEnabled(false);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                drawerMapActivity.getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Places 4 test markers on the map relative to the user's initial position when starting the application.
     */

    public void addTestMarkers(){

        Location mLastKnownLocation = drawerMapActivity.mLastKnownLocation;
        GoogleMap mMap = drawerMapActivity.mMap;

        //Add test markers relative to your initial location
        double offset = 0.0001;

        for(int i = 0; i < 4; i++) {
            LatLng pos = new LatLng(mLastKnownLocation.getLatitude() + offset, mLastKnownLocation.getLongitude() + offset);


            mMap.addMarker(new MarkerOptions().position(pos)
                    .title("Marker " + i)
                    .snippet("This marker was about " + calculateDistance(mLastKnownLocation, pos) + " meters away \n from when you first launched the app!")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ttorch_icon)));
            //
            offset += offset;
        }


    }


    /**
     * Calculates the distance between the user's location and a marker position
     */
    public float calculateDistance(Location userLocation, LatLng markerPosition){
        Location markerLocation = new Location ("Location " + markerPosition.toString());
        markerLocation.setLatitude(markerPosition.latitude);
        markerLocation.setLongitude(markerPosition.longitude);
        return userLocation.distanceTo(markerLocation);
    }


    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    public void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */

        boolean mLocationPermissionGranted = drawerMapActivity.mLocationPermissionGranted;
        FusedLocationProviderClient mFusedLocationProviderClient = drawerMapActivity.mFusedLocationProviderClient;


        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();

                locationResult.addOnCompleteListener(drawerMapActivity, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            drawerMapActivity.mLastKnownLocation = task.getResult();
                            drawerMapActivity.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(drawerMapActivity.mLastKnownLocation.getLatitude(),
                                            drawerMapActivity.mLastKnownLocation.getLongitude()), drawerMapActivity.DEFAULT_ZOOM));


                            drawerMapActivity.pickupCircle = createPickupCircle(drawerMapActivity.MINIMUM_PICKUP_DISTANCE);
                            startLocationUpdates();
                            //addTestMarkers();
                            requestMainTorch();



                        } else {
                            drawerMapActivity.mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(drawerMapActivity.mDefaultLocation, drawerMapActivity.DEFAULT_ZOOM));
                            drawerMapActivity.mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }


    }



    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {

        LocationRequest mLocationRequest = drawerMapActivity.mLocationRequest;
        final long LOCATION_UPDATE_INTERVAL = drawerMapActivity.LOCATION_UPDATE_INTERVAL;
        final long LOCATION_UPDATE_FASTEST_INTERVAL = drawerMapActivity.LOCATION_UPDATE_INTERVAL;
        FusedLocationProviderClient mFusedLocationProviderClient = drawerMapActivity.mFusedLocationProviderClient;
        LocationCallback mLocationCallback = drawerMapActivity.mLocationCallback;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(LOCATION_UPDATE_FASTEST_INTERVAL);



        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

    }

    public void stopLocationUpdates(){

        FusedLocationProviderClient mFusedLocationProviderClient = drawerMapActivity.mFusedLocationProviderClient;
        LocationCallback mLocationCallback = drawerMapActivity.mLocationCallback;
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }


    /**
     * Updates the cetner of the circle which indicates the pick up radius
     */
    public void updateCircleLocation(){
        Circle pickupCircle = drawerMapActivity.pickupCircle;


        pickupCircle.setCenter(new LatLng(drawerMapActivity.mLastKnownLocation.getLatitude(), drawerMapActivity.mLastKnownLocation.getLongitude()));

    }

    /**
     * Creates a circle with specified pickup radius
     */
    public Circle createPickupCircle(int minimumPickupDistance){

        Circle circle = drawerMapActivity.mMap.addCircle(new CircleOptions()
                .center(new LatLng(drawerMapActivity.mLastKnownLocation.getLatitude(), drawerMapActivity.mLastKnownLocation.getLongitude()))
                .radius(minimumPickupDistance)
                .strokeColor(circleFill)
                .fillColor(circleFill));

        return circle;
    }

    /**
     * Places the initial marker from system if doesn't exist otherwise updates its coordinates
     */
    public void placeMainMarker(Double latitude, Double longitude){

        LatLng pos = new LatLng(latitude, longitude);
        GoogleMap mMap = drawerMapActivity.mMap;

        if(mainMarker == null){
            mainMarker =  mMap.addMarker(new MarkerOptions().position(pos)
                    .title("Main System Marker")
                    .snippet("This marker should always be present.")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ttorch_icon)));

            mainMarker.setTag(1);

            systemMarkers.put(1, mainMarker);
        } else {

            systemMarkers.get(1).setPosition(pos);

        }
    }

    /**
     * Update the number of torches in system
     */
    public void updateMarkerCount(int number){
        this.markerCount = number;
    }

    /**
     * Creates a marker on the map with a latitude and longitude
     */

    public void createMarkerWithLatlng(Integer id, LatLng position){
        GoogleMap mMap = drawerMapActivity.mMap;

        Marker marker = mMap.addMarker(new MarkerOptions().position(position)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ttorch_icon)));
        marker.setTag(id);
        systemMarkers.put(id, marker);

    }

    /**
     * Updates the marker's coordinates
     */
    public void updateMarkerCoordinates(Integer torchID, LatLng position){
        systemMarkers.get(torchID).setPosition(position);
    }


    public void setTorchPosition(Integer torchID, double latitude, double longitude){
        LatLng position = new LatLng(latitude,longitude);

        if(!systemMarkers.containsKey(torchID)){
            createMarkerWithLatlng(torchID, position);

        } else if(systemMarkers.containsKey(torchID)){
            updateMarkerCoordinates(torchID, position);
        }
    }


    /**
     * Add markers from database into map
     */
    public void handleMarkers(){
        DatabaseFacade.getTorchCount(drawerMapActivity);



        for(int i = 2; i <= markerCount; i++){
            DatabaseFacade.getTorchPosition(drawerMapActivity, i);
            System.out.println("Request this id: " + i);
        }

    }

    /**
     * Request main torch
     */
    public void requestMainTorch(){
        DatabaseFacade.getTorchCount(drawerMapActivity);
    }


}